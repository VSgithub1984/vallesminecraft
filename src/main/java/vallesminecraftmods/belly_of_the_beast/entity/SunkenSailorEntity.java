package vallesminecraftmods.belly_of_the_beast.entity; // Behalte das Paket, das du zuletzt verwendet hast

import net.minecraft.world.entity.AnimationState;; // <<< FIX 1: Fehlender Import hinzugefügt
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
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation; // <<< FIX 2: Import für fehlende Methode
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation; // <<< FIX 2: Import für fehlende Methode
import net.minecraft.world.entity.ai.util.RandomPos;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
// Stelle sicher, dass diese Imports korrekt auf dein Projekt verweisen

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

    private static final boolean DEBUG_MODE = false;
    private static final boolean DEBUG_VANILLA_ANIMATIONS = true;

    private final ServerBossEvent bossInfo = new ServerBossEvent(this.getDisplayName(), ServerBossEvent.BossBarColor.PINK, ServerBossEvent.BossBarOverlay.PROGRESS);

    // --- Server-Side State Variables ---
    private int attackCounter = 0;
    private int animationTimer = 0;
    private boolean isRangedAttacking = false;
    private int projectileDelayTimer = 0;
    private boolean projectileFired = false;
    private Player targetPlayer = null;
    private BlockPos spawnPos;

    public SunkenSailorEntity(EntityType<? extends SunkenSailorEntity> type, Level world) {
        super(type, world);
        // Verwende MoveControl und LookControl des Mobs
        this.moveControl = new MoveControl(this);
        this.lookControl = new LookControl(this);
        // Navigation für Wassermobs initialisieren
        this.navigation = createWaterNavigation(world); // <<< FIX 2: Methode wird jetzt gefunden
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER_BORDER, 0.0F);
        xpReward = 50;
        setNoAi(false);
        setMaxUpStep(1.0f); // Erlaubt das Bewegen über 1-Block hohe Stufen (im Wasser relevant?)
        setPersistenceRequired(); // Verhindert Despawnen
        // spawnPos wird sicher in tick() oder readAdditionalSaveData initialisiert
    }

    // <<< FIX 2: Fehlende Methode wieder hinzugefügt >>>
    protected PathNavigation createWaterNavigation(Level pLevel) {
        return new WaterBoundPathNavigation(this, pLevel);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_MOVING, false);
        this.entityData.define(TRACKING_PLAYER, false);
        this.entityData.define(IS_ATTACKING, false);
    }

    // --- IS_MOVING Accessors ---
    public void setMoving(boolean moving) {
        if (this.isMoving() != moving) {
            this.entityData.set(IS_MOVING, moving);
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

    // --- TRACKING_PLAYER Accessors ---
    public boolean isTrackingPlayer() {
        return this.entityData.get(TRACKING_PLAYER);
    }
    public void setTrackingPlayer(boolean tracking) {
        this.entityData.set(TRACKING_PLAYER, tracking);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    // --- AI Goals ---
    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new EntityAICirclePlayer(this));
        this.goalSelector.addGoal(1, new RandomWaterAreaGoal(this, 0.6D, 10));
        this.goalSelector.addGoal(2, new RandomStrollGoal(this, 0.4D) {
            // ... (Implementierung von RandomStrollGoal wie zuvor) ...
            @Override
            public Vec3 getPosition() {
                // Initialisiere spawnPos sicher, falls noch nicht geschehen
                if (SunkenSailorEntity.this.spawnPos == null && !SunkenSailorEntity.this.level().isClientSide) {
                    SunkenSailorEntity.this.spawnPos = SunkenSailorEntity.this.blockPosition();
                }
                // Zufällige Position im Wasser nahe dem Spawn
                if (SunkenSailorEntity.this.spawnPos != null) {
                    for(int i = 0; i < 10; ++i) { // Versuche 10 Mal, eine Wasserposition zu finden
                        BlockPos randomOffset = RandomPos.generateRandomDirection(SunkenSailorEntity.this.random, 15, 7);
                        // Stelle sicher, dass die Höhe nicht zu stark abweicht, falls getWaterSurfaceY fehlschlägt
                        Vec3 targetPos = SunkenSailorEntity.this.spawnPos.offset((int) randomOffset.getX(), 0 , (int) randomOffset.getZ()).getCenter();
                        double targetY = SunkenSailorEntity.this.getWaterSurfaceY(targetPos.x, targetPos.z);

                        // Wenn getWaterSurfaceY keinen sinnvollen Wert liefert (z.B. weit weg von Wasser), nutze eine Fallback-Höhe
                        if (targetY <= SunkenSailorEntity.this.level().getMinBuildHeight()) {
                            targetY = SunkenSailorEntity.this.getY(); // Bleibe auf aktueller Höhe als Fallback
                        }

                        BlockPos checkPos = BlockPos.containing(targetPos.x, targetY - 0.1, targetPos.z); // Knapp unter der Oberfläche prüfen
                        if (SunkenSailorEntity.this.level().getFluidState(checkPos).is(FluidTags.WATER)) {
                            return new Vec3(targetPos.x, targetY - 0.5, targetPos.z); // Leicht unter der Oberfläche bleiben
                        }
                    }
                }
                // Fallback, falls keine gute Position gefunden wurde
                return super.getPosition();
            }
            @Override
            public boolean canUse() {
                return !SunkenSailorEntity.this.isRangedAttacking &&
                        SunkenSailorEntity.this.getTarget() == null &&
                        super.canUse() &&
                        SunkenSailorEntity.this.isInWaterOrBubble();
            }
            @Override
            public void start() {
                super.start();
                SunkenSailorEntity.this.setMoving(true);
            }
            @Override
            public void stop() {
                super.stop();
                SunkenSailorEntity.this.setMoving(false);
            }
        });
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
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
        if (compound.contains("SpawnPosX")) {
            this.spawnPos = new BlockPos(compound.getInt("SpawnPosX"), compound.getInt("SpawnPosY"), compound.getInt("SpawnPosZ"));
        }
    }

    // --- Ticks ---
    @Override
    public void tick() {
        super.tick();
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
            } else {
                this.targetPlayer = null;
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
                            projectileFired = true; // Verhindere erneutes Feuern ohne Sichtlinie
                        }
                    }
                }

                if (animationTimer > 0) {
                    animationTimer--;
                    if (animationTimer == 0) {
                        this.isRangedAttacking = false;
                        this.projectileFired = false;
                        this.entityData.set(IS_ATTACKING, false);
                    }
                }
            } else {
                if(this.entityData.get(IS_ATTACKING)) {
                    this.entityData.set(IS_ATTACKING, false);
                }
            }

            // Water Positioning
            // Water Positioning
            if (this.isInWaterOrBubble()) {
                double waterSurfaceY = getWaterSurfaceY(this.getX(), this.getZ());
                double targetY = waterSurfaceY - 0.5; // Ziel leicht unterhalb der Oberfläche
                Vec3 currentDelta = this.getDeltaMovement();
                double dy = 0.0; // Standard: keine erzwungene Y-Änderung

                // Wende nur eine sanfte Korrektur an, wenn die Navigation NICHT aktiv versucht,
                // die Y-Achse signifikant zu ändern ODER wenn die Navigation fertig/inaktiv ist.
                // Reduziere die Aggressivität der Korrektur.
                boolean navigationIdle = !this.getNavigation().isInProgress() || this.getNavigation().isDone();
                boolean targetNearby;
                targetNearby = this.getNavigation().getTargetPos() != null && Math.abs(this.getNavigation().getTargetPos().getY() - this.getY()) < 0.5;

                if (navigationIdle || targetNearby) {
                    dy = Mth.clamp((targetY - this.getY()) * 0.05, -0.05, 0.05); // VIEL sanftere Anpassung
                }

                // Wende leichtes Dämpfen und die sanfte Anpassung an
                this.setDeltaMovement(currentDelta.x * 0.98, currentDelta.y * 0.95 + dy, currentDelta.z * 0.98);

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

        if (isHurt || isDead) {
            this.idleAnimationState.stop();
            this.walkAnimationState.stop();
            this.attackAnimationState.stop();
            this.hurtAnimationState.startIfStopped(this.tickCount);
            // if (isDead) { /* deathAnimationState.startIfStopped(...) */ }
        } else {
            if (this.hurtAnimationState.isStarted()) {
                this.hurtAnimationState.stop();
            }
            // if (this.deathAnimationState.isStarted()) { ... }

            if (isClientAttacking) {
                this.idleAnimationState.stop();
                this.walkAnimationState.stop();
                this.attackAnimationState.startIfStopped(this.tickCount);
            } else {
                if (this.attackAnimationState.isStarted()) {
                    this.attackAnimationState.stop();
                }

                if (isClientMoving) {
                    this.idleAnimationState.stop();
                    this.walkAnimationState.startIfStopped(this.tickCount);
                } else {
                    this.walkAnimationState.stop();
                    this.idleAnimationState.startIfStopped(this.tickCount);
                }
            }
        }
    }

    // --- Dimensionen & Boss Bar ---
    @Override public EntityDimensions getDimensions(Pose pose) {
        return EntityDimensions.scalable(1.5F, 2.5F); // ANPASSEN!
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
                .add(Attributes.MOVEMENT_SPEED, 0.4D)
                .add(Attributes.MAX_HEALTH, 200)
                .add(Attributes.ARMOR, 10)
                .add(Attributes.ATTACK_DAMAGE, 15)
                .add(Attributes.FOLLOW_RANGE, 64)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.8);
    }

    // --- Spawning ---
    // Die init() Methode ist veraltet und sollte entfernt werden.
    // Die Registrierung erfolgt in EntityInit.registerSpawnPlacements().
    // @Deprecated
    // public static void init() { ... }
    @Override public boolean checkSpawnObstruction(LevelReader pLevelReader) { return pLevelReader.isUnobstructed(this); }


    // --- Wasser-Navigation ---
    @Override
    public void travel(Vec3 travelVector) {
        if (this.isEffectiveAi() && this.isInWater()) {
            this.moveRelative(this.getSpeed(), travelVector); // Passe getSpeed() an Wasser an, falls nötig
            this.move(MoverType.SELF, this.getDeltaMovement());
            // Wasserbewegung etwas dämpfen
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9D));
        } else {
            super.travel(travelVector);
        }
    }
    // Vanilla Methode für Wasser-Bewegungsgeschwindigkeit
    @Override
    public float getWaterSlowDown() {
        return 0.9f; // Standard ist 0.8F, etwas schneller im Wasser
    }

    // --- Wasser Oberflächen Höhe ---
    private double getWaterSurfaceY(double x, double z) {
        Level level = this.level();
        int ix = Mth.floor(x);
        int iz = Mth.floor(z);
        int iy = Mth.floor(this.getY(1.0)); // Start Y

        // Suche abwärts
        for (int y = iy; y >= level.getMinBuildHeight(); --y) {
            BlockPos pos = new BlockPos(ix, y, iz);
            BlockState state = level.getBlockState(pos);
            if (state.getFluidState().is(FluidTags.WATER)) {
                return (double)y + state.getFluidState().getHeight(level, pos);
            }
            if (!state.isAir() && !state.canBeReplaced() && !state.getFluidState().is(FluidTags.WATER)) {
                break;
            }
        }
        // Suche aufwärts
        for (int y = iy + 1; y <= level.getMaxBuildHeight(); ++y) {
            BlockPos pos = new BlockPos(ix, y, iz);
            BlockState state = level.getBlockState(pos);
            if (state.getFluidState().is(FluidTags.WATER)) {
                return (double)y + state.getFluidState().getHeight(level, pos);
            }
            if (!state.isAir() && !state.canBeReplaced()) {
                break;
            }
        }
        return level.getSeaLevel(); // Fallback
    }

    // --- Angriffslogik ---
    public void performRangedAttack(LivingEntity target) {
        if (this.isRangedAttacking || this.level().isClientSide) return;

        this.isRangedAttacking = true;
        this.entityData.set(IS_ATTACKING, true);
        this.setMoving(false);
        this.getNavigation().stop();

        if (target instanceof Player player) {
            this.targetPlayer = player;
        } else {
            this.targetPlayer = null;
        }

        this.animationTimer = 92; // Dauer des AngriffsZUSTANDS (nicht unbedingt der Animation)
        this.projectileDelayTimer = 60; // Verzögerung bis zum Schuss
        this.projectileFired = false;
    }

    private void fireProjectiles(LivingEntity target) {
        Level level = this.level();
        if (level.isClientSide || target == null) return;

        if (DEBUG_VANILLA_ANIMATIONS) {
            System.out.println("SERVER: fireProjectiles CALLED (Attack " + (attackCounter % 2 == 0 ? "1 - Single" : "2 - Triple") + ")");
        }

        Vec3 spawnPos = this.position().add(0, this.getBbHeight() * 0.7, 0);
        double targetX = target.getX();
        double targetY = target.getY() + target.getEyeHeight() * 0.5;
        double targetZ = target.getZ();

        if (attackCounter % 2 == 0) {
            // Attack 1: Tracking
            level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.SKELETON_SHOOT, SoundSource.HOSTILE, 1.5F, 0.8F);
            SkeletonfishprojectileEntity projectile = new SkeletonfishprojectileEntity(level, this, (byte) 1, target);
            projectile.setPos(spawnPos);
            double dx = targetX - spawnPos.x;
            double dy = targetY - spawnPos.y;
            double dz = targetZ - spawnPos.z;
            projectile.shoot(dx, dy, dz, 1.1f, 1.0f);
            projectile.setProjectileDamage(0); // Schaden durch Explosion
            projectile.setProjectileKnockback(0);
            level.addFreshEntity(projectile);
        } else {
            // Attack 2: Ghast-like
            level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.GHAST_SHOOT, SoundSource.HOSTILE, 1.5F, 0.6F);
            Vec3 ghastSpawnPos = this.position().add(0, this.getBbHeight() + 0.5, 0);
            Vec3 aimDir = new Vec3(targetX - ghastSpawnPos.x, targetY - ghastSpawnPos.y, targetZ - ghastSpawnPos.z).normalize();
            Vec3 sideOffset = new Vec3(-aimDir.z, 0, aimDir.x).normalize().scale(0.8);
            Vec3[] offsets = { sideOffset.scale(-1), Vec3.ZERO, sideOffset };

            for (Vec3 offset : offsets) {
                Vec3 actualSpawnPos = ghastSpawnPos.add(offset);
                SkeletonfishprojectileEntity projectile = new SkeletonfishprojectileEntity(level, this, (byte) 2, null);
                projectile.setPos(actualSpawnPos);
                projectile.shoot(aimDir.x, aimDir.y, aimDir.z, 1.0f, 2.0f);
                projectile.setProjectileDamage(0); // Schaden durch Explosion
                projectile.setProjectileKnockback(0);
                level.addFreshEntity(projectile);
            }
        }
        this.attackCounter++;
    }

    // --- Custom AI Goal ---
    // (EntityAICirclePlayer Klasse wie zuvor, keine Änderungen notwendig)
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
        private boolean tryingToReachWater = false; // Flag, falls aus dem Wasser gekommen

        public EntityAICirclePlayer(SunkenSailorEntity entity) {
            this.entity = entity;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            // Kann nur starten, wenn NICHT gerade angegriffen wird
            if (entity.isRangedAttacking) {
                return false;
            }
            LivingEntity target = entity.getTarget();
            if (target instanceof Player player && target.isAlive()) { // Pattern Matching verwenden
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
            // Kann weiterlaufen, wenn NICHT angegriffen wird, Ziel existiert und in Reichweite ist
            return !entity.isRangedAttacking &&
                    targetPlayer != null &&
                    targetPlayer.isAlive() &&
                    entity.getTarget() == targetPlayer && // Sicherstellen, dass das Ziel nicht gewechselt hat
                    entity.distanceToSqr(targetPlayer) < entity.getAttributeValue(Attributes.FOLLOW_RANGE) * entity.getAttributeValue(Attributes.FOLLOW_RANGE);
        }

        @Override
        public void start() {
            // Starte zufällig im Halbkreis vor dem Spieler
            this.currentAngle = entity.getRandom().nextDouble() * Math.PI; // 0 bis PI für Halbkreis
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


        @Override
        public void tick() {
            if (targetPlayer == null || !targetPlayer.isAlive()) {
                return; // Abbruch, wenn Ziel ungültig
            }

            entity.getLookControl().setLookAt(targetPlayer, 30.0F, 30.0F);

            // Prüfen, ob feststeckt (Logik bleibt, aber Fallback wird verbessert)
            if (entity.position().distanceToSqr(this.lastPos) < 0.03 * 0.03 && entity.getNavigation().isDone()) {
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
                    if (DEBUG_VANILLA_ANIMATIONS)
                        System.out.println("Entity stuck, trying to move towards player's water position: " + targetPos);
                } else {
                    if (entity.spawnPos != null) {
                        double spawnWaterY = entity.getWaterSurfaceY(entity.spawnPos.getX() + 0.5, entity.spawnPos.getZ() + 0.5);
                        targetPos = new Vec3(entity.spawnPos.getX() + 0.5, spawnWaterY > entity.level().getMinBuildHeight() ? spawnWaterY - 0.5 : entity.getY(), entity.spawnPos.getZ() + 0.5);
                        if (DEBUG_VANILLA_ANIMATIONS)
                            System.out.println("Entity stuck, cannot find player water pos, trying to move towards spawn water pos: " + targetPos);
                    } else {
                        targetPos = entity.position().add(entity.getLookAngle().scale(2.0));
                        if (DEBUG_VANILLA_ANIMATIONS)
                            System.out.println("Entity stuck, no player water or spawn pos, moving in look direction.");
                    }
                }
                entity.getNavigation().moveTo(targetPos.x, targetPos.y, targetPos.z, 1.0D); // Höhere Geschwindigkeit zum Befreien
                stuckTicks = 0;
                this.tryingToReachWater = true; // Markieren, dass wir repositionieren
                return;
            }

            // Wenn wir versuchen, aus Stuck-Situation zu kommen
            if (tryingToReachWater) {
                if (entity.getNavigation().isDone() || !entity.getNavigation().isInProgress()) { // Prüfe auch !isInProgress
                    tryingToReachWater = false;
                } else {
                    entity.setMoving(true); // Sicherstellen, dass Animation läuft
                    return; // Warten
                }
            }

            // Angriffslogik (bleibt gleich)
            if (attackCooldown > 0) attackCooldown--;
            checkCanAttackTimer--;
            if (checkCanAttackTimer <= 0 && attackCooldown <= 0) {
                // ... (Rest der Angriffslogik bleibt) ...
                if (entity.distanceToSqr(targetPlayer) < attackRangeSq && entity.hasLineOfSight(targetPlayer)) {
                    entity.performRangedAttack(targetPlayer);
                    this.attackCooldown = MIN_ATTACK_INTERVAL + entity.getRandom().nextInt(MAX_ATTACK_INTERVAL - MIN_ATTACK_INTERVAL + 1);
                    return;
                }
            }

            // --- Bewegungslogik (Halbkreis) ---
            double angleIncrement = 0.04 * circleDirection;
            currentAngle += angleIncrement;
            if (currentAngle > Math.PI) {
                currentAngle = Math.PI;
                circleDirection = -1;
            } else if (currentAngle < 0) {
                currentAngle = 0;
                circleDirection = 1;
            }

            double desiredDistance = circleRadius;
            // ===> KREISZENTRUM ÄNDERN: <===
            // Vec3 playerLook = Vec3.directionFromRotation(targetPlayer.getXRot(), targetPlayer.getYRot()); // ALT
            // Vec3 circleCenter = targetPlayer.position().add(playerLook.scale(-desiredDistance * 0.5)); // ALT
            Vec3 circleCenter = targetPlayer.position(); // NEU: Zentrum ist Spieler X/Z

            double targetX = circleCenter.x + Mth.cos((float) currentAngle) * desiredDistance;
            double targetZ = circleCenter.z + Mth.sin((float) currentAngle) * desiredDistance;
            double targetY = entity.getWaterSurfaceY(targetX, targetZ);

            Vec3 idealTargetPos = Vec3.ZERO; // Initialisieren

            // Prüfen, ob Wasseroberfläche gefunden wurde
            if (targetY <= entity.level().getMinBuildHeight()) {
                // Keine Wasseroberfläche gefunden, versuche direkt zur Spieler-Wasserposition
                targetY = entity.getWaterSurfaceY(targetPlayer.getX(), targetPlayer.getZ());
                if (targetY > entity.level().getMinBuildHeight()) {
                    idealTargetPos = new Vec3(targetPlayer.getX(), targetY - 0.5, targetPlayer.getZ());
                    if (DEBUG_VANILLA_ANIMATIONS)
                        System.out.println("Circle Goal: Target Y failed, using player water Y: " + idealTargetPos);
                } else {
                    // Spieler auch nicht über Wasser, Bewegung stoppen für diesen Tick
                    if (DEBUG_VANILLA_ANIMATIONS)
                        System.out.println("Circle Goal: Target Y and Player water Y failed, stopping.");
                    entity.getNavigation().stop();
                    entity.setMoving(false);
                    stuckTicks += 5; // Schneller als "stuck" markieren
                    return;
                }
            } else {
                idealTargetPos = new Vec3(targetX, targetY - 0.5, targetZ); // Reguläres Ziel
            }

            // Stelle sicher, dass das Ziel (idealTargetPos) im Wasser und erreichbar ist
            BlockPos targetBlockPos = BlockPos.containing(idealTargetPos);
            boolean targetIsWater = entity.level().getFluidState(targetBlockPos).is(FluidTags.WATER) &&
                    entity.level().getBlockState(targetBlockPos).isPathfindable(entity.level(), targetBlockPos, PathComputationType.WATER);

            if (!targetIsWater) {
                // Wenn Ziel nicht im Wasser/erreichbar ist, versuche NICHT sofort zum Spieler zu springen,
                // sondern überspringe einfach die Navigation für diesen Tick und hoffe, der nächste Kreispunkt ist besser.
                // Das verhindert zu häufiges Stoppen/Zurückspringen.
                if (DEBUG_VANILLA_ANIMATIONS)
                    System.out.println("Circle Goal: Calculated ideal target " + idealTargetPos + " is not valid water/pathable. Skipping move.");
                entity.setMoving(false); // Nicht bewegen, wenn kein gültiges Ziel
                // Optional: stuckTicks leicht erhöhen, damit es nicht ewig an einer Land-Kante hängen bleibt?
                // stuckTicks += 1;
                return; // Nächsten Tick abwarten
            }

            // Aktualisiere Navigation nur periodisch oder wenn Ziel weit weg
            if (entity.tickCount % 10 == 0 || moveTargetPos.distanceToSqr(idealTargetPos) > 2.0 * 2.0) {
                // Prüfe Pfad vor dem Bewegen (optional, aber sicherer)
                // net.minecraft.world.level.pathfinder.Path path = entity.getNavigation().createPath(targetBlockPos, 0);
                // if (path != null && !path.isDone()) {
                this.moveTargetPos = idealTargetPos;
                entity.getNavigation().moveTo(idealTargetPos.x, idealTargetPos.y, idealTargetPos.z, 1.0D); // Normale Geschwindigkeit
                // entity.getNavigation().moveTo(path, 1.0D); // Alternative mit Path
                if (DEBUG_VANILLA_ANIMATIONS && entity.tickCount % 40 == 0)
                    System.out.println("Circle Goal: Updating navigation target.");
                // } else {
                //    if (DEBUG_VANILLA_ANIMATIONS && entity.tickCount % 40 == 0) System.out.println("Circle Goal: Cannot create path to target " + idealTargetPos);
                //    stuckTicks += 5; // Pfad ungültig -> als stuck behandeln
                //    entity.setMoving(false);
                // }
            }

            // Update moving state basierend auf tatsächlicher Bewegung
            entity.setMoving(entity.getNavigation().isInProgress() && !entity.getNavigation().isDone());


            // Aktualisiere Navigation nur periodisch oder wenn das Ziel weit weg ist
            if (entity.tickCount % 10 == 0 || moveTargetPos.distanceToSqr(idealTargetPos) > 2.0 * 2.0) {
                this.moveTargetPos = idealTargetPos;
                entity.getNavigation().moveTo(idealTargetPos.x, idealTargetPos.y, idealTargetPos.z, 1.0D); // Bewegungsgeschwindigkeit anpassen?
                if (DEBUG_VANILLA_ANIMATIONS && entity.tickCount % 40 == 0)
                    System.out.println("Updating navigation target."); // Seltener loggen
            }

            // Update moving state basierend auf tatsächlicher Bewegung
            entity.setMoving(entity.getNavigation().isInProgress() && !entity.getNavigation().isDone()); // Setze Moving, wenn Navigation aktiv ist

        }
    } // Ende EntityAICirclePlayer

    // Methode zum Berechnen der Schwerkraft (wird von baseTick verwendet)
    protected double getGravity() {
        // Standard-Schwerkraft, kann überschrieben werden, wenn die Entität leichter/schwerer sein soll
        return 0.08D; // Vanilla default für die meisten Entities
    }
    public class RandomWaterAreaGoal extends Goal {
        private final SunkenSailorEntity mob;
        private final double speedModifier;
        private final int maxDist; // Max X/Z-Abstand vom spawnPos
        private double wantedX;
        private double wantedY;
        private double wantedZ;
        // Reduziere die Startverzögerung und das Intervall, damit es häufiger versucht
        private int interval = 60 + mob.getRandom().nextInt(60); // 3-6 Sekunden

        public RandomWaterAreaGoal(SunkenSailorEntity mob, double speedModifier, int maxDist) {
            this.mob = mob;
            this.speedModifier = speedModifier;
            this.maxDist = maxDist; // Dies ist der Radius (10 für 10x10 wäre eher 5?) - Verwende es als max X/Z-Abstand
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            // Nur verwenden, wenn NICHT gekämpft wird, SPAWNPOS existiert und
            // die Navigation fertig ist ODER eine gewisse Zeit abgelaufen ist.
            if (this.mob.getTarget() != null || this.mob.spawnPos == null || this.mob.isRangedAttacking) {
                return false;
            }

            // Versuche nur, wenn nicht schon unterwegs oder nach Ablauf des Intervalls
            if (!this.mob.getNavigation().isDone() && this.mob.isMoving()) {
                // Wenn schon unterwegs zu einem Idle-Ziel, nicht sofort neu suchen
                return false;
            }

            if (--this.interval <= 0) {
                this.interval = 80 + this.mob.getRandom().nextInt(80); // Intervall zurücksetzen (4-8 Sek)
                Vec3 targetPos = this.findRandomWaterPosition();
                if (targetPos != null) {
                    this.wantedX = targetPos.x;
                    this.wantedY = targetPos.y;
                    this.wantedZ = targetPos.z;
                    // Debug-Ausgabe entfernt, um Konsole sauber zu halten
                    // if (SunkenSailorEntity.DEBUG_VANILLA_ANIMATIONS) System.out.println("Idle Goal: Found new water target: " + targetPos);
                    return true; // Gültiges Ziel gefunden
                }
                // Wenn kein Ziel gefunden wurde, warte länger, bevor erneut gesucht wird
                this.interval = 120 + this.mob.getRandom().nextInt(100);
            }

            return false; // Kein neues Ziel benötigt oder gefunden
        }

        @Override
        public boolean canContinueToUse() {
            // Weiterlaufen, solange Navigation aktiv ist UND kein Ziel/Angriff da ist
            return !this.mob.getNavigation().isDone() && this.mob.getTarget() == null && !this.mob.isRangedAttacking;
        }

        @Override
        public void start() {
            // Bewege zum Ziel
            this.mob.getNavigation().moveTo(this.wantedX, this.wantedY, this.wantedZ, this.speedModifier);
            this.mob.setMoving(true); // Animation starten
            // if (SunkenSailorEntity.DEBUG_VANILLA_ANIMATIONS) System.out.println("Idle Goal: Starting navigation.");
        }

        @Override
        public void stop() {
            // Navigation stoppen und Animation beenden
            this.mob.getNavigation().stop();
            this.mob.setMoving(false);
            // if (SunkenSailorEntity.DEBUG_VANILLA_ANIMATIONS) System.out.println("Idle Goal: Stopping navigation.");
            // Kurzes Intervall, falls sofort wieder canUse geprüft wird
            this.interval = 20;
        }

        @Override
        public void tick() {
            // Sicherstellen, dass der Moving-Status korrekt ist, falls die Navigation abbricht
            this.mob.setMoving(!this.mob.getNavigation().isDone());
        }


        private Vec3 findRandomWaterPosition() {
            if (this.mob.spawnPos == null) return null;

            for (int i = 0; i < 15; ++i) { // Mehr Versuche
                // Zufällige X/Z-Position innerhalb des maxDist-Bereichs um spawnPos
                double targetX = this.mob.spawnPos.getX() + (this.mob.getRandom().nextDouble() * 2.0D - 1.0D) * this.maxDist;
                double targetZ = this.mob.spawnPos.getZ() + (this.mob.getRandom().nextDouble() * 2.0D - 1.0D) * this.maxDist;

                // Finde Wasseroberfläche an dieser Position
                double targetY = this.mob.getWaterSurfaceY(targetX, targetZ);

                // Prüfe, ob eine gültige Wasseroberfläche gefunden wurde
                if (targetY > this.mob.level().getMinBuildHeight()) {
                    Vec3 potentialTarget = new Vec3(targetX, targetY - 0.5, targetZ); // Leicht unterhalb der Oberfläche
                    BlockPos checkPos = BlockPos.containing(potentialTarget);

                    // Prüfe, ob der Block Wasser ist UND für Wasser-Pathfinding geeignet ist
                    if (this.mob.level().getFluidState(checkPos).is(FluidTags.WATER) &&
                            this.mob.level().getBlockState(checkPos).isPathfindable(this.mob.level(), checkPos, PathComputationType.WATER))
                    {
                        // Zusätzlicher Check: Ist die Position zu nah an der aktuellen?
                        if (this.mob.position().distanceToSqr(potentialTarget) > 3.0 * 3.0) { // Nur wenn weiter als 3 Blöcke weg
                            return potentialTarget;
                        }
                    }
                }
            }
            // if (SunkenSailorEntity.DEBUG_VANILLA_ANIMATIONS) System.out.println("Idle Goal: Failed to find random water position after 15 tries.");
            return null; // Kein geeignetes Ziel gefunden
        }
    }
} // Ende SunkenSailorEntity