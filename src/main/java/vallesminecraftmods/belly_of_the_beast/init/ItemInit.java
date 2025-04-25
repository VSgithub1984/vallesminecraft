package vallesminecraftmods.belly_of_the_beast.init;

import net.minecraft.world.item.CreativeModeTabs; // Import hinzufügen
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem; // Korrekten Import verwenden
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent; // Import für Tab-Event
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import vallesminecraftmods.belly_of_the_beast.BellyOfTheBeast;

import static vallesminecraftmods.belly_of_the_beast.init.EntityInit.*;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, BellyOfTheBeast.MODID);

    // Spawn Egg Item
    public static final RegistryObject<Item> SUNKEN_SAILOR_SPAWN_EGG = ITEMS.register("sunken_sailor_spawn_egg",
            () -> new ForgeSpawnEggItem(SUNKEN_SAILOR, // vallesminecraftmods.vallesminecraftmods.BellyOfTheBeast.entity Type
                    0x4B6A6F, // Hintergrundfarbe (Dunkelgrau/Blau) - Beispiel, anpassen!
                    0xACACAC, // Vordergrundfarbe (Hellgrau) - Beispiel, anpassen!
                    new Item.Properties())
    );

    // Methode zum Hinzufügen zum Creative Tab (wird von vallesminecraftmods.vallesminecraftmods.BellyOfTheBeast.java aufgerufen)
    public static void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
            event.accept(SUNKEN_SAILOR_SPAWN_EGG.get());
        }
    }
}
