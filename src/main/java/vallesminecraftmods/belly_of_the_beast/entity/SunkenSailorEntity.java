package vallesminecraftmods.belly_of_the_beast.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
// Entferne den Import für RandomStrollGoal, wenn nicht mehr benötigt
// import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.ai.util.RandomPos; // Behalten für den Fallback
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
// Importiere den neuen Goal (stelle sicher, dass der Pfad stimmt!)
import vallesminecraftmods.belly_of_the_beast.entity.ai.RandomWaterAreaGoal; // Passe ggf. den Pfad an

import javax.annotation.Nullable; // Import für @Nullable
import java.util.EnumSet;

public class SunkenSailorEntity extends Monster {

    // --- Synced Data ---
    public static final EntityDataAccessor<Boolean> IS_MOVING = SynchedEntityData.defineId(SunkenSailorEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> TRACKING_PLAYER = SynchedEntityData.defineId(SunkenSailorEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> IS_ATTACKING = SynchedEntityData.defineId(SunkenSailorEntity.class, EntityDataSerializers.BOOLEAN);

    // --- Animation States ---
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState walkAnimationState = new AnimationState();
    public final AnimationState attackAnimationState = new AnimationState();
    public final AnimationState hurtAnimationState = new AnimationState();
    // public final AnimationState deathAnimationState = new AnimationState(); // Optional

    // Debug Flags (können auf false gesetzt werden)
    private static final boolean DEBUG_MODE = false;
    private static final boolean DEBUG_VANILLA_ANIMATIONS = true; // Setze auf false, um Konsolenausgaben zu reduzieren

    private final ServerBossEvent bossInfo = new ServerBossEvent(this.getDisplayName(), ServerBossEvent.BossBarColor.PINK, ServerBossEvent.BossBarOverlay.PROGRESS);

    // --- Server-Side State Variables ---
    private int attackCounter = 0;
    private int animationTimer = 0;
    private boolean isRangedAttacking = false; // Behalte private, füge Getter hinzu
    private int projectileDelayTimer = 0;
    private boolean projectileFired = false;
    private Player targetPlayer = null;
    private BlockPos spawnPos; // Behalte private, füge Getter hinzu

    public SunkenSailorEntity(EntityType<? extends SunkenSailorEntity> type, Level world) {
        super(type, world);
        this.moveControl = new MoveControl(this); // Standard MoveControl
        this.lookControl = new LookControl(this); // Standard LookControl
        this.navigation = createWaterNavigation(world);
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER_BORDER, 0.0F); // Wichtig für Uferbereiche
        xpReward = 50;
        setNoAi(false);
        setMaxUpStep(1.0f); // Erlaubt das Bewegen über 1-Block hohe Stufen
        setPersistenceRequired(); // Verhindert Despawnen
    }

    // KORREKTUR: @Override entfernt
    protected PathNavigation createWaterNavigation(Level pLevel) {
        return new WaterBoundPathNavigation(this, pLevel);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_MOVING, false);
        this.entityData.define(TRACKING_PLAYER, false); // Wird dieser noch verwendet? Ggf. entfernen.
        this.entityData.define(IS_ATTACKING, false);
    }

    // --- IS_MOVING Accessors ---
    public void setMoving(boolean moving) {
        // Nur setzen, wenn sich der Wert ändert, um Netzwerkverkehr zu sparen
        if (this.isMoving() != moving) {
            this.entityData.set(IS_MOVING, moving);
            // Client-seitige Debug-Ausgabe (optional)
            if (DEBUG_VANILLA_ANIMATIONS && level().isClientSide) {
                System.out.println("CLIENT: Setting IS_MOVING to: " + moving);
            }
        }
    }
    public boolean isMoving() {
        return this.entityData.get(IS_MOVING);
    }

    // --- MobType & Sounds ---
    @Override public MobType getMobType() { return MobType.UNDEAD; }
    @Override public SoundEvent getAmbientSound() { return SoundEvents.SKELETON_AMBIENT; } // Beispiel
    @Override public SoundEvent getHurtSound(DamageSource ds) { return SoundEvents.SKELETON_HURT; } // Beispiel
    @Override public SoundEvent getDeathSound() { return SoundEvents.SKELETON_DEATH; } // Beispiel
    @Override protected SoundEvent getSwimSound() { return SoundEvents.PLAYER_SWIM; } // Beispiel
    @Override protected SoundEvent getSwimSplashSound() { return SoundEvents.PLAYER_SPLASH; } // Beispiel

    // --- TRACKING_PLAYER Accessors (Prüfen, ob noch benötigt) ---
    public boolean isTrackingPlayer() {
        return this.entityData.get(TRACKING_PLAYER);
    }
    public void setTrackingPlayer(boolean tracking) {
        this.entityData.set(TRACKING_PLAYER, tracking);
    }

    // --- Getters für externe Goals ---
    @Nullable // Kann null sein, wenn noch nicht gesetzt
    public BlockPos getSpawnPos() {
        return this.spawnPos;
    }

    public boolean isCurrentlyRangedAttacking() {
        return this.isRangedAttacking;
    }
    // --- Ende Getters ---


    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    // --- AI Goals ---
    @Override
    protected void registerGoals() {
        super.registerGoals();

        // Priorität 0: Kampf-Bewegung (Kreisen), läuft nur wenn getTarget() != null (durch canUse geprüft)
        this.goalSelector.addGoal(0, new EntityAICirclePlayer(this));

        // Priorität 1: Idle-Wandering im Wasserbereich, läuft nur wenn getTarget() == null
        // Parameter: Entity, Geschwindigkeit, Max X/Z-Abstand vom Spawn (Radius)
        this.goalSelector.addGoal(1, new RandomWaterAreaGoal(this, 0.6D, 10)); // Geschwindigkeit 0.6, Bereich +/-10 Blöcke X/Z

        // Priorität 2: Zufällig umschauen (niedrige Priorität)
        this.goalSelector.addGoal(2, new RandomLookAroundGoal(this));

        // Target Selectors (Bleiben gleich)
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    // --- NBT Daten ---
    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("AttackCounter", this.attackCounter);
        compound.putBoolean("IsRangedAttacking", this.isRangedAttacking);
        compound.putInt("AnimationTimer", this.animationTimer);
        compound.putInt("ProjectileDelayTimer", this.projectileDelayTimer);
        compound.putBoolean("ProjectileFired", this.projectileFired);
        if (this.spawnPos != null) {
            compound.putInt("SpawnPosX", this.spawnPos.getX());
            compound.putInt("SpawnPosY", this.spawnPos.getY());
            compound.putInt("SpawnPosZ", this.spawnPos.getZ());
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("AttackCounter")) this.attackCounter = compound.getInt("AttackCounter");
        if (compound.contains("IsRangedAttacking")) this.isRangedAttacking = compound.getBoolean("IsRangedAttacking");
        if (compound.contains("AnimationTimer")) this.animationTimer = compound.getInt("AnimationTimer");
        if (compound.contains("ProjectileDelayTimer")) this.projectileDelayTimer = compound.getInt("ProjectileDelayTimer");
        if (compound.contains("ProjectileFired")) this.projectileFired = compound.getBoolean("ProjectileFired");
        if (compound.contains("SpawnPosX") && compound.contains("SpawnPosY") && compound.contains("SpawnPosZ")) {
            // KORREKTUR: Verwende create() oder new BlockPos()
            this.spawnPos = new BlockPos(compound.getInt("SpawnPosX"), compound.getInt("SpawnPosY"), compound.getInt("SpawnPosZ"));
        }
    }

    // --- Ticks ---
    @Override
    public void tick() {
        super.tick();
        // Initialisiere spawnPos sicher nach dem ersten Tick auf dem Server
        if (this.spawnPos == null && !this.level().isClientSide && this.tickCount > 1) {
            this.spawnPos = this.blockPosition();
        }
        if (this.level().isClientSide()) {
            this.updateAnimationStates();
        }
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (!this.level().isClientSide) {
            // Target Update
            LivingEntity currentTarget = this.getTarget();
            if (currentTarget instanceof Player player) { // Verwende instanceof mit Pattern Matching
                this.targetPlayer = player;
                this.setTrackingPlayer(true); // Setze Tracking-Flag (falls noch verwendet)
            } else {
                this.targetPlayer = null;
                this.setTrackingPlayer(false); // Setze Tracking-Flag (falls noch verwendet)
            }

            // Attack Timer Logic
            if (this.isRangedAttacking) {
                if (projectileDelayTimer > 0) {
                    projectileDelayTimer--;
                    if (projectileDelayTimer == 0 && !projectileFired && this.targetPlayer != null) {
                        if (this.hasLineOfSight(this.targetPlayer)) {
                            fireProjectiles(this.targetPlayer);
                            projectileFired = true;
                        } else {
                            // Ziel verloren, Angriff abbrechen? Oder warten?
                            // Hier: Brechen wir den Angriff ab, wenn keine Sichtlinie mehr da ist
                            this.isRangedAttacking = false;
                            this.projectileFired = false; // Wichtig zurücksetzen
                            this.animationTimer = 0; // Timer stoppen
                            this.entityData.set(IS_ATTACKING, false);
                            if (DEBUG_VANILLA_ANIMATIONS) System.out.println("SERVER: Attack cancelled, lost line of sight before firing.");
                        }
                    }
                }

                if (animationTimer > 0) {
                    animationTimer--;
                    if (animationTimer == 0) {
                        // Angriff beendet
                        this.isRangedAttacking = false;
                        this.projectileFired = false; // Zurücksetzen für nächsten Angriff
                        this.entityData.set(IS_ATTACKING, false);
                    }
                }
            } else {
                // Sicherstellen, dass der Angriffsstatus korrekt ist
                if(this.entityData.get(IS_ATTACKING)) {
                    this.entityData.set(IS_ATTACKING, false);
                }
            }

            // --- ANGEPASSTE Water Positioning ---
            if (this.isInWaterOrBubble()) {
                double waterSurfaceY = getWaterSurfaceY(this.getX(), this.getZ());
                // Nur anpassen, wenn eine gültige Oberfläche gefunden wurde
                if (waterSurfaceY > this.level().getMinBuildHeight()) {
                    double targetY = waterSurfaceY - 0.5; // Ziel leicht unterhalb der Oberfläche
                    Vec3 currentDelta = this.getDeltaMovement();
                    double dy = 0.0; // Standard: keine erzwungene Y-Änderung

                    // Wende nur eine sanfte Korrektur an, wenn die Navigation NICHT aktiv versucht,
                    // die Y-Achse signifikant zu ändern ODER wenn die Navigation fertig/inaktiv ist.
                    boolean navigationIdle = !this.getNavigation().isInProgress() || this.getNavigation().isDone();
                    // Prüfe, ob das Ziel Y (falls vorhanden) nah am aktuellen Y ist
                    // KORREKTUR: Verwende getY() statt .y
                    boolean targetNearbyOrIdle = navigationIdle || (this.getNavigation().getTargetPos() != null && Math.abs(this.getNavigation().getTargetPos().getY() - this.getY()) < 0.75); // Toleranz erhöht

                    if (targetNearbyOrIdle) {
                        // Sanftere Anpassung, vermeidet "Ruckeln"
                        dy = Mth.clamp((targetY - this.getY()) * 0.04, -0.04, 0.04);
                    }

                    // Wende leichtes Dämpfen und die sanfte Anpassung an
                    // Reduziere Dämpfung leicht, damit es nicht zu langsam wird
                    this.setDeltaMovement(currentDelta.x * 0.97, currentDelta.y * 0.96 + dy, currentDelta.z * 0.97);
                }

            } else { // Schwerkraft außerhalb des Wassers (Logik bleibt)
                if (!this.onGround() && !this.isNoGravity()) {
                    this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -this.getGravity(), 0.0D));
                }
            }
        }
    }

    // --- Animation State Logic (Client-Side) ---
    private void updateAnimationStates() {
        boolean isClientAttacking = this.entityData.get(IS_ATTACKING);
        boolean isClientMoving = this.isMoving();
        boolean isHurt = this.hurtTime > 0;
        boolean isDead = this.isDeadOrDying();

        // Priorität: Tod > Hurt > Attack > Walk > Idle
        if (isDead) {
            // Hier Todesanimation starten, falls vorhanden
            this.idleAnimationState.stop();
            this.walkAnimationState.stop();
            this.attackAnimationState.stop();
            this.hurtAnimationState.stop();
            // this.deathAnimationState.startIfStopped(this.tickCount);
            return; // Keine anderen Animationen bei Tod
        }

        if (isHurt) {
            // Stoppe andere und starte Hurt
            this.idleAnimationState.stop();
            this.walkAnimationState.stop();
            this.attackAnimationState.stop();
            this.hurtAnimationState.startIfStopped(this.tickCount);
        } else {
            // Stoppe Hurt, wenn nicht mehr verletzt
            if (this.hurtAnimationState.isStarted()) {
                this.hurtAnimationState.stop();
            }

            // Prüfe Angriff
            if (isClientAttacking) {
                this.idleAnimationState.stop();
                this.walkAnimationState.stop();
                this.attackAnimationState.startIfStopped(this.tickCount);
            } else {
                // Stoppe Angriff, wenn nicht mehr angreifend
                if (this.attackAnimationState.isStarted()) {
                    this.attackAnimationState.stop();
                }

                // Prüfe Bewegung
                if (isClientMoving) {
                    this.idleAnimationState.stop();
                    this.walkAnimationState.startIfStopped(this.tickCount);
                } else {
                    // Stoppe Bewegung und starte Idle
                    this.walkAnimationState.stop();
                    this.idleAnimationState.startIfStopped(this.tickCount);
                }
            }
        }
    }

    // --- Dimensionen & Boss Bar ---
    @Override public EntityDimensions getDimensions(Pose pose) {
        return EntityDimensions.scalable(1.5F, 2.5F); // Passe ggf. an dein Modell an
    }
    @Override public boolean canChangeDimensions() { return false; }
    @Override public void startSeenByPlayer(ServerPlayer player) { super.startSeenByPlayer(player); this.bossInfo.addPlayer(player); }
    @Override public void stopSeenByPlayer(ServerPlayer player) { super.stopSeenByPlayer(player); this.bossInfo.removePlayer(player); }
    @Override public void customServerAiStep() {
        super.customServerAiStep();
        if (!this.level().isClientSide) {
            this.bossInfo.setProgress(this.getHealth() / this.getMaxHealth());
        }
    }

    // --- Attribute ---
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.4D) // Basis-Geschwindigkeit
                .add(Attributes.MAX_HEALTH, 200)
                .add(Attributes.ARMOR, 10)
                .add(Attributes.ATTACK_DAMAGE, 15) // Wird für Projektile nicht direkt genutzt
                .add(Attributes.FOLLOW_RANGE, 64)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.8);
    }

    // --- Spawning ---
    @Override public boolean checkSpawnObstruction(LevelReader pLevelReader) {
        // Standardprüfung + Sicherstellen, dass es im Wasser spawnt
        return super.checkSpawnObstruction(pLevelReader) && pLevelReader.isUnobstructed(this) && pLevelReader.getFluidState(this.blockPosition()).is(FluidTags.WATER);
    }
    // Optional: checkSpawnRules für spezifischere Bedingungen (z.B. nur in tiefem Wasser)
    // public static boolean checkSpawnRules(EntityType<? extends SunkenSailorEntity> pEntityType, ServerLevelAccessor pLevel, MobSpawnType pSpawnType, BlockPos pPos, RandomSource pRandom) { ... }


    // --- Wasser-Navigation ---
    @Override
    public void travel(Vec3 travelVector) {
        if (this.isEffectiveAi() && this.isInWater()) {
            // Angepasste Wasserbewegung
            this.moveRelative(this.getWaterSlowDown(), travelVector); // Verwende getWaterSlowDown()
            this.move(MoverType.SELF, this.getDeltaMovement());
            // Stärkere Dämpfung im Wasser
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9D));
        } else {
            super.travel(travelVector);
        }
    }
    // Vanilla Methode für Wasser-Bewegungsgeschwindigkeit
    @Override
    public float getWaterSlowDown() {
        // Wert zwischen 0.0 (sofort stopp) und 1.0 (keine Verlangsamung)
        // Standard für die meisten Mobs ist 0.8F
        // Für einen agileren Boss vielleicht etwas höher?
        return 0.92f; // Etwas weniger Verlangsamung als Standard
    }

    // --- Wasser Oberflächen Höhe ---
    // KORREKTUR: Nur EINE Definition dieser Methode, Sichtbarkeit public oder protected
    public double getWaterSurfaceY(double x, double z) {
        Level level = this.level();
        if (level == null) return Double.MIN_VALUE; // Sicherstellen, dass Level existiert

        int ix = Mth.floor(x);
        int iz = Mth.floor(z);
        // Starte Suche von einer vernünftigen Höhe (z.B. aktuelle Höhe + etwas Puffer)
        int startY = Mth.floor(this.getY() + 1.0);

        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos(ix, startY, iz);

        // Suche abwärts nach dem ersten Wasserblock ODER festem Block
        for (int y = startY; y >= level.getMinBuildHeight(); --y) {
            mutablePos.setY(y);
            BlockState state = level.getBlockState(mutablePos);
            if (state.getFluidState().is(FluidTags.WATER)) {
                // Wasser gefunden, gib die exakte Oberfläche zurück
                return (double)y + state.getFluidState().getHeight(level, mutablePos);
            }
            // Wenn wir auf einen nicht-ersetzbaren Block treffen, der KEIN Wasser ist, bevor wir Wasser finden,
            // dann ist hier vermutlich Land oder der Meeresboden. Stoppe die Suche nach unten.
            if (!state.canBeReplaced() && !state.liquid()) {
                break;
            }
        }

        // Suche aufwärts nach dem ersten Wasserblock (falls wir unter Wasser gestartet sind)
        for (int y = startY + 1; y <= level.getMaxBuildHeight(); ++y) {
            mutablePos.setY(y);
            BlockState state = level.getBlockState(mutablePos);
            if (state.getFluidState().is(FluidTags.WATER)) {
                return (double)y + state.getFluidState().getHeight(level, mutablePos);
            }
            // Wenn wir auf einen nicht-ersetzbaren Block treffen, bevor wir Wasser finden,
            // dann ist hier vermutlich Luft oder Decke. Stoppe die Suche nach oben.
            if (!state.canBeReplaced() && !state.liquid()) {
                break;
            }
        }

        // Fallback, wenn absolut kein Wasser gefunden wurde (sollte selten sein, wenn im Wasser gespawnt)
        return level.getMinBuildHeight() - 1.0; // Ungültiger Wert, um anzuzeigen, dass nichts gefunden wurde
    }

    // --- Angriffslogik ---
    public void performRangedAttack(LivingEntity target) {
        if (this.isRangedAttacking || this.level().isClientSide || target == null) return;

        this.isRangedAttacking = true;
        this.entityData.set(IS_ATTACKING, true);
        this.setMoving(false); // Stoppe Bewegung während des Angriffsstarts
        this.getNavigation().stop(); // Stoppe Navigation

        // Ziel zwischenspeichern (bereits in baseTick erledigt, aber hier zur Sicherheit)
        if (target instanceof Player player) {
            this.targetPlayer = player;
        } else {
            // Sollte nicht passieren, da NearestAttackableTargetGoal nur Player auswählt
            this.isRangedAttacking = false;
            this.entityData.set(IS_ATTACKING, false);
            return;
        }

        this.lookControl.setLookAt(target, 30.0F, 30.0F); // Schaue Ziel an

        this.animationTimer = 92; // Dauer des AngriffsZUSTANDS (Animation kann kürzer/länger sein)
        this.projectileDelayTimer = 60; // Verzögerung bis zum Schuss (Anpassen an Animation)
        this.projectileFired = false; // Wichtig: Zurücksetzen
    }

    private void fireProjectiles(LivingEntity target) {
        Level level = this.level();
        if (level.isClientSide || target == null) return; // Doppelte Prüfung

        if (DEBUG_VANILLA_ANIMATIONS) {
            System.out.println("SERVER: fireProjectiles CALLED (Attack " + (attackCounter % 2 == 0 ? "1 - Single" : "2 - Triple") + ")");
        }

        // Leicht angepasste Spawn-Position (etwas vor und über dem Zentrum?)
        Vec3 lookAngle = this.getLookAngle();
        Vec3 spawnOffset = lookAngle.scale(1.0).add(0, this.getBbHeight() * 0.6, 0); // Etwas nach vorne
        Vec3 baseSpawnPos = this.position().add(spawnOffset);

        double targetX = target.getX();
        double targetY = target.getEyeY() - 0.2; // Ziele etwas tiefer als die Augen
        double targetZ = target.getZ();

        if (attackCounter % 2 == 0) {
            // Attack 1: Tracking Projectile
            level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.SKELETON_SHOOT, SoundSource.HOSTILE, 1.5F, 0.8F);
            SkeletonfishprojectileEntity projectile = new SkeletonfishprojectileEntity(level, this, (byte) 1, target); // Übergebe Ziel für Tracking
            projectile.setPos(baseSpawnPos); // Setze Position
            // Direkte Berechnung für initiale Richtung (Tracking übernimmt später)
            double dx = targetX - baseSpawnPos.x;
            double dy = targetY - baseSpawnPos.y;
            double dz = targetZ - baseSpawnPos.z;
            projectile.shoot(dx, dy, dz, 1.1f, 1.0f); // Geschwindigkeit, Ungenauigkeit
            projectile.setProjectileDamage(0); // Schaden durch Explosion
            projectile.setProjectileKnockback(0);
            level.addFreshEntity(projectile);
        } else {
            // Attack 2: Ghast-like Triple Shot
            level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.GHAST_SHOOT, SoundSource.HOSTILE, 1.5F, 0.6F);
            // Zielrichtung berechnen
            Vec3 aimDir = new Vec3(targetX - baseSpawnPos.x, targetY - baseSpawnPos.y, targetZ - baseSpawnPos.z).normalize();
            // Seitlicher Offset senkrecht zur Blickrichtung
            Vec3 sideOffset = new Vec3(-aimDir.z, 0, aimDir.x).normalize().scale(0.6); // Etwas weniger Abstand

            Vec3[] offsets = { sideOffset.scale(-1), Vec3.ZERO, sideOffset }; // Links, Mitte, Rechts

            for (Vec3 offset : offsets) {
                Vec3 actualSpawnPos = baseSpawnPos.add(offset);
                SkeletonfishprojectileEntity projectile = new SkeletonfishprojectileEntity(level, this, (byte) 2, null); // Kein Ziel für nicht-tracking
                projectile.setPos(actualSpawnPos);
                // Leichte Streuung hinzufügen?
                Vec3 shootDir = aimDir.add(
                        (this.random.nextDouble() - 0.5) * 0.1,
                        (this.random.nextDouble() - 0.5) * 0.1,
                        (this.random.nextDouble() - 0.5) * 0.1
                ).normalize(); // Leichte zufällige Abweichung
                projectile.shoot(shootDir.x, shootDir.y, shootDir.z, 1.0f, 0.5f); // Geschwindigkeit, Ungenauigkeit
                projectile.setProjectileDamage(0); // Schaden durch Explosion
                projectile.setProjectileKnockback(0);
                level.addFreshEntity(projectile);
            }
        }
        this.attackCounter++;
    }

    // --- Custom AI Goal ---
    public static class EntityAICirclePlayer extends Goal {
        private final SunkenSailorEntity entity;
        private Player targetPlayer;
        private Vec3 moveTargetPos = Vec3.ZERO;
        private double currentAngle;
        private int attackCooldown;
        private int checkCanAttackTimer;
        private int circleDirection; // 1 für eine Richtung, -1 für die andere
        private int stuckTicks;
        private Vec3 lastPos = Vec3.ZERO;
        private final double circleRadius = 15.0; // Radius des Kreises/Halbkreises
        private final double attackRangeSq = 20.0 * 20.0; // Quadrat der Angriffsreichweite
        private static final int MIN_ATTACK_INTERVAL = 60;  // 3 Sekunden
        private static final int MAX_ATTACK_INTERVAL = 100; // 5 Sekunden
        private static final int CHECK_ATTACK_RATE = 10; // Wie oft auf Angriff prüfen (Ticks)
        private boolean tryingToReachWater = false; // Flag für Repositionierung

        public EntityAICirclePlayer(SunkenSailorEntity entity) {
            this.entity = entity;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            // Kann nur starten, wenn NICHT gerade angegriffen wird
            // KORREKTUR: Verwende Getter
            if (entity.isCurrentlyRangedAttacking()) {
                return false;
            }
            LivingEntity target = entity.getTarget();
            if (target instanceof Player player && target.isAlive() && entity.isInWaterOrBubble()) { // Nur im Wasser starten
                // Prüfe Distanz innerhalb der Follow-Range
                if (entity.distanceToSqr(target) < entity.getAttributeValue(Attributes.FOLLOW_RANGE) * entity.getAttributeValue(Attributes.FOLLOW_RANGE)) {
                    this.targetPlayer = player;
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean canContinueToUse() {
            // Kann weiterlaufen, wenn NICHT angegriffen wird, Ziel existiert, in Reichweite ist UND im Wasser ist
            // KORREKTUR: Verwende Getter
            return !entity.isCurrentlyRangedAttacking() &&
                    targetPlayer != null &&
                    targetPlayer.isAlive() &&
                    entity.getTarget() == targetPlayer && // Sicherstellen, dass das Ziel nicht gewechselt hat
                    entity.isInWaterOrBubble() && // Muss im Wasser bleiben
                    entity.distanceToSqr(targetPlayer) < entity.getAttributeValue(Attributes.FOLLOW_RANGE) * entity.getAttributeValue(Attributes.FOLLOW_RANGE);
        }

        @Override
        public void start() {
            // Starte zufällig im Halbkreis UM den Spieler
            this.currentAngle = entity.getRandom().nextDouble() * Math.PI * 2.0; // Voller Kreis jetzt
            this.attackCooldown = MIN_ATTACK_INTERVAL + entity.getRandom().nextInt(MAX_ATTACK_INTERVAL - MIN_ATTACK_INTERVAL + 1);
            this.checkCanAttackTimer = 0;
            this.circleDirection = entity.getRandom().nextBoolean() ? 1 : -1; // Zufällige Startrichtung
            this.stuckTicks = 0;
            this.lastPos = entity.position();
            this.tryingToReachWater = false;
            // setMoving wird durch die Navigation ausgelöst
        }

        @Override
        public void stop() {
            // Aufräumen, wenn das Goal endet
            if (!entity.level().isClientSide) { // Nur serverseitig Navigation stoppen
                this.entity.getNavigation().stop();
            }
            this.entity.setMoving(false); // Bewegung beenden
            this.targetPlayer = null;
            this.moveTargetPos = Vec3.ZERO;
        }

        // KORREKTUR: Entferne doppeltes @Override
        @Override
        public void tick() {
            if (targetPlayer == null || !targetPlayer.isAlive()) {
                return; // Abbruch, wenn Ziel ungültig
            }

            entity.getLookControl().setLookAt(targetPlayer, 30.0F, 30.0F);

            // Prüfen, ob feststeckt (Logik bleibt, aber Fallback wird verbessert)
            // Toleranz leicht erhöhen, um Jitter zu vermeiden
            if (entity.position().distanceToSqr(this.lastPos) < 0.05 * 0.05 && !entity.getNavigation().isInProgress()) {
                stuckTicks++;
            } else {
                stuckTicks = 0;
            }
            this.lastPos = entity.position();

            // Wenn zu lange feststeckt -> Verbesserter Fallback
            if (stuckTicks > 60) {
                double playerWaterY = entity.getWaterSurfaceY(targetPlayer.getX(), targetPlayer.getZ());
                Vec3 targetPos;
                if (playerWaterY > entity.level().getMinBuildHeight()) {
                    targetPos = new Vec3(targetPlayer.getX(), playerWaterY - 0.5, targetPlayer.getZ());
                    if (DEBUG_VANILLA_ANIMATIONS) System.out.println("Entity stuck, trying to move towards player's water position: " + targetPos);
                } else {
                    // KORREKTUR: Verwende Getter für spawnPos
                    BlockPos currentSpawnPos = entity.getSpawnPos();
                    if (currentSpawnPos != null) {
                        double spawnWaterY = entity.getWaterSurfaceY(currentSpawnPos.getX()+0.5, currentSpawnPos.getZ()+0.5);
                        // Prüfe ob Spawn über Wasser ist, sonst bleibe auf Entity Y
                        targetPos = new Vec3(currentSpawnPos.getX() + 0.5, spawnWaterY > entity.level().getMinBuildHeight() ? spawnWaterY - 0.5 : entity.getY(), currentSpawnPos.getZ() + 0.5);
                        if (DEBUG_VANILLA_ANIMATIONS) System.out.println("Entity stuck, cannot find player water pos, trying to move towards spawn water pos: " + targetPos);
                    } else {
                        // Letzter Ausweg: Zufällige Position in der Nähe - KORREKTUR der Methode
                        // targetPos = RandomPos.generateRandomPos(entity, 5, 3); // ALT - FALSCH
                        // NEU: Nutze generateRandomDirection oder eine andere gültige Methode
                        Vec3 randomDir = RandomPos.generateRandomDirection(entity.getRandom(), 5, 3).getCenter();
                        targetPos = entity.position().add(randomDir);
                        // Optional: Versuche Y an Wasser anzupassen
                        double randomY = entity.getWaterSurfaceY(targetPos.x, targetPos.z);
                        if (randomY > entity.level().getMinBuildHeight()) {
                            targetPos = new Vec3(targetPos.x, randomY - 0.5, targetPos.z);
                        }

                        if (DEBUG_VANILLA_ANIMATIONS) System.out.println("Entity stuck, no player water or spawn pos, moving randomly nearby.");
                    }
                }
                entity.getNavigation().moveTo(targetPos.x, targetPos.y, targetPos.z, 1.0D); // Normale Geschwindigkeit zum Befreien
                stuckTicks = 0; // Timer zurücksetzen
                this.tryingToReachWater = true; // Markieren, dass wir repositionieren
                return; // Nächsten Tick abwarten
            }

            // Wenn wir versuchen, aus Stuck-Situation zu kommen
            if (tryingToReachWater) {
                // Prüfe, ob Navigation fertig ODER nicht mehr läuft
                if (entity.getNavigation().isDone() || !entity.getNavigation().isInProgress()) {
                    tryingToReachWater = false; // Ziel erreicht oder abgebrochen
                } else {
                    entity.setMoving(true); // Sicherstellen, dass Animation läuft
                    return; // Warten
                }
            }

            // Angriffslogik (bleibt gleich)
            if (attackCooldown > 0) attackCooldown--;
            checkCanAttackTimer--;
            if (checkCanAttackTimer <= 0 && attackCooldown <= 0) {
                double distanceSq = entity.distanceToSqr(targetPlayer);
                if (distanceSq < attackRangeSq && entity.hasLineOfSight(targetPlayer)) {
                    entity.performRangedAttack(targetPlayer);
                    this.attackCooldown = MIN_ATTACK_INTERVAL + entity.getRandom().nextInt(MAX_ATTACK_INTERVAL - MIN_ATTACK_INTERVAL + 1);
                    // Angriff gestartet, tick() wird durch canContinueToUse() beendet
                }
            }

            // Nur weiter bewegen, wenn nicht gerade angegriffen wird
            // KORREKTUR: Verwende Getter
            if(entity.isCurrentlyRangedAttacking()) {
                entity.getNavigation().stop(); // Sicherstellen, dass während des Angriffs nicht bewegt wird
                entity.setMoving(false);
                return;
            }


            // --- Bewegungslogik (Kreis um Spieler) ---
            double angleIncrement = 0.05 * circleDirection; // Etwas schneller kreisen
            currentAngle += angleIncrement;
            // Modulo für vollen Kreis
            currentAngle = (currentAngle + Math.PI * 2.0) % (Math.PI * 2.0);


            double desiredDistance = circleRadius;
            // ===> KREISZENTRUM BLEIBT SPIELER X/Z <===
            Vec3 circleCenter = targetPlayer.position();

            double targetX = circleCenter.x + Mth.cos((float)currentAngle) * desiredDistance;
            double targetZ = circleCenter.z + Mth.sin((float)currentAngle) * desiredDistance;
            double targetY = entity.getWaterSurfaceY(targetX, targetZ);

            Vec3 idealTargetPos = Vec3.ZERO; // Initialisieren

            // Prüfen, ob Wasseroberfläche gefunden wurde
            if (targetY <= entity.level().getMinBuildHeight()) {
                // Keine Wasseroberfläche am Kreispunkt gefunden.
                // Versuche näher am Spieler zu kreisen
                desiredDistance = circleRadius * 0.7; // Näherer Radius
                targetX = circleCenter.x + Mth.cos((float)currentAngle) * desiredDistance;
                targetZ = circleCenter.z + Mth.sin((float)currentAngle) * desiredDistance;
                targetY = entity.getWaterSurfaceY(targetX, targetZ); // Erneut Y suchen

                if(targetY <= entity.level().getMinBuildHeight()){
                    // Immer noch kein Wasser -> Bewegung stoppen für diesen Tick
                    if (DEBUG_VANILLA_ANIMATIONS) System.out.println("Circle Goal: Target Y failed even at closer radius, stopping.");
                    entity.getNavigation().stop();
                    entity.setMoving(false);
                    stuckTicks += 3; // Etwas schneller als stuck markieren
                    return;
                }
                // Gültiges Y im näheren Kreis gefunden
                idealTargetPos = new Vec3(targetX, targetY - 0.5, targetZ);
                if (DEBUG_VANILLA_ANIMATIONS) System.out.println("Circle Goal: Target Y failed, using closer circle pos: " + idealTargetPos);

            } else {
                idealTargetPos = new Vec3(targetX, targetY - 0.5, targetZ); // Reguläres Ziel
            }

            // Stelle sicher, dass das Ziel (idealTargetPos) im Wasser und erreichbar ist
            BlockPos targetBlockPos = BlockPos.containing(idealTargetPos);
            // Checke Block UND Fluid
            boolean targetIsWater = entity.level().getFluidState(targetBlockPos).is(FluidTags.WATER);
            // Prüfe, ob der Block für WASSER-Pathfinding geeignet ist
            boolean targetIsPathable = entity.level().getBlockState(targetBlockPos).isPathfindable(entity.level(), targetBlockPos, PathComputationType.WATER);

            if (!targetIsWater || !targetIsPathable) {
                // Wenn Ziel nicht im Wasser/erreichbar ist, überspringe Navigation für diesen Tick
                if (DEBUG_VANILLA_ANIMATIONS) System.out.println("Circle Goal: Calculated ideal target " + idealTargetPos + " is not valid water/pathable. Skipping move.");
                entity.setMoving(false);
                stuckTicks += 1; // Leicht erhöhen, um Hängenbleiben zu vermeiden
                return; // Nächsten Tick abwarten
            }


            // Aktualisiere Navigation nur periodisch oder wenn Ziel weit weg
            if (entity.tickCount % 8 == 0 || moveTargetPos.distanceToSqr(idealTargetPos) > 1.5 * 1.5) { // Häufiger und bei kleinerer Distanz
                this.moveTargetPos = idealTargetPos; // Ziel speichern
                entity.getNavigation().moveTo(idealTargetPos.x, idealTargetPos.y, idealTargetPos.z, 1.0D); // Geschwindigkeit anpassen?
                // if (DEBUG_VANILLA_ANIMATIONS && entity.tickCount % 40 == 0) System.out.println("Circle Goal: Updating navigation target.");
            }

            // Update moving state basierend auf tatsächlicher Bewegung
            entity.setMoving(entity.getNavigation().isInProgress() && !entity.getNavigation().isDone());
        } // Ende tick()
    } // Ende EntityAICirclePlayer

    // Methode zum Berechnen der Schwerkraft (wird von baseTick verwendet)
    protected double getGravity() {
        // Standard-Schwerkraft, kann überschrieben werden, wenn die Entität leichter/schwerer sein soll
        return 0.08D; // Vanilla default für die meisten Entities
    }



} // Ende SunkenSailorEntity
