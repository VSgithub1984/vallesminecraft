package vallesminecraftmods.belly_of_the_beast.entity;

// --- Vanilla/Forge Imports ---
import net.minecraft.core.particles.ParticleTypes; // Für Partikel
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel; // Für UUID -> Entity Auflösung
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource; // Für Explosion DamageSource
import net.minecraft.world.entity.Entity; // Für UUID Auflösung
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
// import net.minecraft.world.entity.projectile.ItemSupplier; // Entfernt, da nicht mehr benötigt
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Explosion; // Für Explosion
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
// --- Mod Imports ---
import vallesminecraftmods.belly_of_the_beast.init.EntityInit;

// --- Java Util Imports ---
import javax.annotation.Nullable;
import java.util.Optional; // Für Optional<UUID> SyncedData
import java.util.UUID; // Für Target UUID

// Klasse implementiert ItemSupplier nicht mehr
public class SkeletonfishprojectileEntity extends AbstractArrow {

    // --- Synced Data Accessors ---
    // Speichert den Angriffs-Typ (1 = Tracking/AoE, 2 = Ghast-like)
    private static final EntityDataAccessor<Byte> DATA_ATTACK_TYPE = SynchedEntityData.defineId(SkeletonfishprojectileEntity.class, EntityDataSerializers.BYTE);
    // Speichert die UUID des Ziels für Typ 1
    private static final EntityDataAccessor<Optional<UUID>> DATA_TARGET_UUID = SynchedEntityData.defineId(SkeletonfishprojectileEntity.class, EntityDataSerializers.OPTIONAL_UUID);

    // --- Felder ---
    private int lifeTicks = 0; // Zählt die Lebensdauer (besonders für Typ 1)
    private static final int MAX_LIFE_TICKS = 100; // Max. Lebensdauer für Typ 1 in Ticks (5 Sekunden)
    private static final float EXPLOSION_RADIUS_TYPE_1 = 1.8F; // Radius für AoE (Typ 1)
    private static final float EXPLOSION_RADIUS_TYPE_2 = 2.5F; // Radius für Ghast-like (Typ 2)
    // ItemStack für getPickupItem (obwohl es nicht aufhebbar ist)
    private static final ItemStack PROJECTILE_ITEM_STACK = new ItemStack(Items.STICK);

    // --- Konstruktoren ---

    /** Standardkonstruktor für Forge-Registrierung */
    public SkeletonfishprojectileEntity(EntityType<? extends SkeletonfishprojectileEntity> type, Level world) {
        super(type, world);
        this.pickup = Pickup.DISALLOWED; // Nicht aufhebbar
    }

    /** Hauptkonstruktor zum Spawnen durch den Sunken Sailor */
    public SkeletonfishprojectileEntity(Level world, LivingEntity shooter, byte attackType, @Nullable LivingEntity target) {
        super(EntityInit.SKELETONFISH_PROJECTILE.get(), shooter, world); // Korrekte EntityType Referenz
        this.pickup = Pickup.DISALLOWED;
        this.setAttackType(attackType); // Setze den Typ über die neue Methode
        if (attackType == 1 && target != null) {
            this.setTargetEntityUUID(target.getUUID()); // Setze Ziel für Tracking über neue Methode
        }
        if (attackType == 2) {
            this.setNoGravity(true); // Typ 2 ignoriert Schwerkraft
        }
    }

    /** Konstruktor für Netzwerk-Spawning */
    public SkeletonfishprojectileEntity(PlayMessages.SpawnEntity packet, Level world) {
        this(EntityInit.SKELETONFISH_PROJECTILE.get(), world); // Delegiert an Standardkonstruktor
    }

    // Optional: Weitere Konstruktoren, falls benötigt (z.B. für Spawnen über Commands)
    public SkeletonfishprojectileEntity(EntityType<? extends SkeletonfishprojectileEntity> type, double x, double y, double z, Level world) {
        super(type, x, y, z, world);
        this.pickup = Pickup.DISALLOWED;
    }
    public SkeletonfishprojectileEntity(EntityType<? extends SkeletonfishprojectileEntity> type, LivingEntity shooter, Level world) {
        super(type, shooter, world);
        this.pickup = Pickup.DISALLOWED;
    }


    // --- Synced Data Initialisierung ---
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData(); // Wichtig!
        this.entityData.define(DATA_ATTACK_TYPE, (byte) 1); // Standardwert setzen
        this.entityData.define(DATA_TARGET_UUID, Optional.empty()); // Standardwert setzen
    }

    // --- Getter/Setter für Synced Data ---
    // FIX: Fehlende Methoden hinzugefügt
    public byte getAttackType() {
        try {
            return this.entityData.get(DATA_ATTACK_TYPE);
        } catch (Exception e) {
            // Fallback oder Log, falls Data noch nicht registriert ist (sollte nicht passieren)
            return 1;
        }
    }

    public void setAttackType(byte type) {
        this.entityData.set(DATA_ATTACK_TYPE, type);
    }

    @Nullable
    public UUID getTargetEntityUUID() {
        // Gibt die UUID oder null zurück
        return this.entityData.get(DATA_TARGET_UUID).orElse(null);
    }

    public void setTargetEntityUUID(@Nullable UUID uuid) {
        // Setzt die optionale UUID
        this.entityData.set(DATA_TARGET_UUID, Optional.ofNullable(uuid));
    }

    // --- Helfermethode zum Holen des Ziel-Entities ---
    @Nullable
    private LivingEntity getTrackedTarget() {
        UUID targetUUID = getTargetEntityUUID();
        if (targetUUID != null && this.level() instanceof ServerLevel serverLevel) {
            Entity target = serverLevel.getEntity(targetUUID);
            if (target instanceof LivingEntity livingTarget && livingTarget.isAlive()) {
                return livingTarget; // Gültiges Ziel gefunden
            } else {
                // Ziel ist ungültig (tot, nicht gefunden, etc.)
                if (!level().isClientSide) { // Nur Server sollte UUID zurücksetzen
                    setTargetEntityUUID(null); // Tracking beenden
                }
                return null;
            }
        }
        return null; // Keine UUID oder Client-Seite
    }


    // --- Tick Logic ---
    @Override
    public void tick() {
        super.tick(); // Wichtig für Basisbewegung, etc.
        lifeTicks++;

        byte attackType = getAttackType();

        // Tracking für Typ 1 (Nur Server-Side)
        if (!this.level().isClientSide && attackType == 1 && !this.inGround) {
            LivingEntity target = getTrackedTarget();
            if (target != null) {
                // Nur lenken, wenn nicht zu nah dran
                if (this.distanceToSqr(target) > 1.5 * 1.5) {
                    Vec3 currentMotion = this.getDeltaMovement();
                    Vec3 pos = this.position();
                    Vec3 targetCenter = target.position().add(0, target.getBbHeight() / 2.0, 0);
                    Vec3 directionToTarget = targetCenter.subtract(pos).normalize();

                    double trackingStrength = 0.08; // Stärke der Lenkung (kleiner = langsamer)
                    // Behalte die aktuelle Geschwindigkeit bei, ändere nur die Richtung leicht
                    Vec3 desiredMotion = directionToTarget.scale(currentMotion.length());
                    Vec3 newMotion = currentMotion.lerp(desiredMotion, trackingStrength);

                    // Verhindere Geschwindigkeitszunahme durch Lenkung
                    double speed = newMotion.length();
                    if (speed > 0 && currentMotion.length() > 0) {
                        newMotion = newMotion.scale(currentMotion.length() / speed);
                    }
                    this.setDeltaMovement(newMotion);
                }
            }

            // Explodieren nach max. Lebensdauer (Typ 1)
            if (lifeTicks > MAX_LIFE_TICKS) {
                this.explode(EXPLOSION_RADIUS_TYPE_1);
                this.discard();
                return;
            }
        }

        // Entfernen, wenn im Boden (Server-Side)
        if (!this.level().isClientSide && this.inGround && this.inGroundTime > (attackType == 1 ? 1 : 4)) {
            if (attackType == 1) this.explode(EXPLOSION_RADIUS_TYPE_1);
            else if (attackType == 2) this.explode(EXPLOSION_RADIUS_TYPE_2); // Typ 2 explodiert auch
            this.discard();
            return;
        }

        // Partikel (Client-Side)
        if (this.level().isClientSide) {
            Vec3 motion = this.getDeltaMovement();
            double speedFactor = motion.lengthSqr() * 0.1;
            this.level().addParticle(attackType == 1 ? ParticleTypes.BUBBLE_COLUMN_UP : ParticleTypes.BUBBLE_POP,
                    this.getX() + (this.random.nextDouble() - 0.5) * this.getBbWidth(),
                    this.getY() + (this.random.nextDouble() - 0.5) * this.getBbHeight(),
                    this.getZ() + (this.random.nextDouble() - 0.5) * this.getBbWidth(),
                    motion.x * -speedFactor, motion.y * -speedFactor + 0.05, motion.z * -speedFactor);
        }
    }


    // --- Hit Logic ---
    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity targetEntity = result.getEntity();
        if (!this.level().isClientSide) {
            byte attackType = getAttackType();
            if (attackType == 1 || attackType == 2) {
                // Typ 1 und 2 explodieren bei Treffer
                float explosionRadius = (attackType == 1 ? EXPLOSION_RADIUS_TYPE_1 : EXPLOSION_RADIUS_TYPE_2);
                this.explode(explosionRadius);
                // Verursache minimalen Schaden, um Hurt-Effekte auszulösen, aber Hauptschaden kommt von Explosion
                DamageSource damagesource = this.damageSources().arrow(this, this.getOwner());
                targetEntity.hurt(damagesource, 0.1F);
            } else {
                // Fallback (sollte nicht passieren) - Standardverhalten
                super.onHitEntity(result);
            }
            this.discard(); // Immer entfernen
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        byte attackType = getAttackType();
        if (!this.level().isClientSide) {
            if (attackType == 1) this.explode(EXPLOSION_RADIUS_TYPE_1);
            else if (attackType == 2) this.explode(EXPLOSION_RADIUS_TYPE_2);
            this.discard();
        }
        // Sound abspielen & inGround setzen (wichtig für Tick-Logik zum Entfernen)
        this.playSound(this.getHitGroundSoundEvent(), 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
        Vec3 vec3 = result.getLocation().subtract(this.getX(), this.getY(), this.getZ());
        this.setDeltaMovement(vec3);
        Vec3 vec31 = vec3.normalize().scale(0.05F);
        this.setPosRaw(this.getX() - vec31.x, this.getY() - vec31.y, this.getZ() - vec31.z);
        this.inGround = true;
        this.setPierceLevel((byte)0); // Verhindert Durchschießen nach Blocktreffer
    }

    // --- Explosion Method ---
    private void explode(float radius) {
        if (!this.level().isClientSide) {
            Entity explosionSource = this.getOwner() != null ? this.getOwner() : this;
            DamageSource damageSource = this.damageSources().explosion(this, explosionSource);
            // Verwende Interaction.MOB, um hauptsächlich Entities zu schädigen
            this.level().explode(explosionSource, damageSource, null, this.getX(), this.getY(), this.getZ(), radius, false, Level.ExplosionInteraction.MOB);

            if(this.level() instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.EXPLOSION_EMITTER, this.getX(), this.getY(), this.getZ(), 1, 0.0, 0.0, 0.0, 0.0);
                serverLevel.sendParticles(ParticleTypes.BUBBLE, this.getX(), this.getY(), this.getZ(), 30, radius * 0.5, radius * 0.5, radius * 0.5, 0.1);
            }
        }
    }


    // --- Pickup ---
    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY; // Ist nicht aufhebbar
    }

    // --- Physik ---
    @Override
    public boolean isNoGravity() {
        return getAttackType() == 2 || super.isNoGravity(); // Typ 2 hat keine Gravitation
    }

    @Override
    protected float getWaterInertia() {
        // Typ 2 wird kaum durch Wasser verlangsamt
        return getAttackType() == 2 ? 0.99F : super.getWaterInertia();
    }


    // --- NBT Speichern/Laden ---
    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        // Speichere wichtige Zustände
        compound.putByte("AttackType", getAttackType());
        compound.putInt("LifeTicks", this.lifeTicks);
        if (getTargetEntityUUID() != null) {
            compound.putUUID("TargetUUID", getTargetEntityUUID());
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        // Lade Zustände und aktualisiere SyncedData
        if (compound.contains("AttackType", CompoundTag.TAG_BYTE)) {
            setAttackType(compound.getByte("AttackType"));
        }
        if (compound.contains("LifeTicks", CompoundTag.TAG_INT)) {
            this.lifeTicks = compound.getInt("LifeTicks");
        }
        if (compound.hasUUID("TargetUUID")) {
            setTargetEntityUUID(compound.getUUID("TargetUUID"));
        }
        // Stelle NoGravity wieder her, falls Typ 2 geladen wurde
        if (getAttackType() == 2) {
            this.setNoGravity(true);
        }
    }


    // --- Custom Damage/Knockback Setters ---
    public void setProjectileDamage(double damage) {
        this.setBaseDamage(damage);
    }

    public void setProjectileKnockback(int knockback) {
        this.setKnockback(knockback);
    }

    // --- Networking ---
    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    // --- ItemSupplier nicht mehr benötigt ---
    // @Override @OnlyIn(Dist.CLIENT) public ItemStack getItem() { return PROJECTILE_ITEM_STACK; }
}