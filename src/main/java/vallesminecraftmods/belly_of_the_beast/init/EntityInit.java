package vallesminecraftmods.belly_of_the_beast.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import vallesminecraftmods.belly_of_the_beast.BellyOfTheBeast;
import vallesminecraftmods.belly_of_the_beast.entity.SunkenSailorEntity;

public class EntityInit {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, BellyOfTheBeast.MODID);

    public static final RegistryObject<EntityType<SunkenSailorEntity>> SUNKEN_SAILOR =
            ENTITY_TYPES.register("sunken_sailor",
                    () -> EntityType.Builder.of(SunkenSailorEntity::new, MobCategory.MONSTER)
                            // ACHTUNG: size() an die tatsächliche OBJ-Modellgröße anpassen!
                            // Die alten Werte (5.0F, 8.0F) waren für GeckoLib und riesig.
                            // Starte mit etwas Vernünftigem wie (1.5F, 2.5F) und passe es an.
                            .sized(1.5f, 2.5f)
                            .build(new ResourceLocation(BellyOfTheBeast.MODID, "sunken_sailor").toString())
            );

    // Registriert die Attribute für die Entität
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(SUNKEN_SAILOR.get(), SunkenSailorEntity.createAttributes().build());
    }

    // Registriert die Spawn-Bedingungen
    public static void registerSpawnPlacements() {
        SpawnPlacements.register(SUNKEN_SAILOR.get(),
                SpawnPlacements.Type.IN_WATER, // Korrekt für Wasser-Mobs
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkMonsterSpawnRules); // Standard Monster Spawn Rules in Wasser
    }
}