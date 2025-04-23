package vallesminecraftmods.belly_of_the_beast;

import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import vallesminecraftmods.belly_of_the_beast.client.ClientModEvents;
import vallesminecraftmods.belly_of_the_beast.init.EntityInit;
import vallesminecraftmods.belly_of_the_beast.init.ItemInit; // Erstellen wir gleich noch

@Mod(BellyOfTheBeast.MODID)
public class BellyOfTheBeast {
    public static final String MODID = "belly_of_the_beast";
    private static final Logger LOGGER = LogUtils.getLogger();

    public BellyOfTheBeast() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Registriere Deferred Registers
        EntityInit.ENTITY_TYPES.register(modEventBus);
        ItemInit.ITEMS.register(modEventBus); // Für Spawn-Egg

        // Registriere Event-Listener für Mod-Bus
        modEventBus.addListener(this::commonSetup);
        // Registriere Client-Setup über separate Klasse
        modEventBus.addListener(ClientModEvents::onClientSetup);
        // Registriere Attribut-Erstellung über separate Klasse/Methode in EntityInit
        modEventBus.addListener(EntityInit::registerAttributes);

        // Registriere dich für Forge-Events (z.B. Server-Start)
        MinecraftForge.EVENT_BUS.register(this);

        // Listener für Creative Tab hinzufügen (optional, für Spawn-Egg)
        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Starting Common Setup for {}", MODID);
        // Hier z.B. Spawning registrieren (wird in EntityInit behandelt)
        event.enqueueWork(EntityInit::registerSpawnPlacements);
    }

    // Optional: Spawn Egg zum Creative Tab hinzufügen
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        // Beispiel: Zum Spawn-Egg-Tab hinzufügen
        // if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
        //     event.accept(ItemInit.SUNKEN_SAILOR_SPAWN_EGG.get());
        // }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("Server Starting for {}", MODID);
    }

    // ClientModEvents wird Client-spezifische Dinge wie Renderer-Registrierung handhaben
    // EntityAttributeCreationEvent wird von EntityInit::registerAttributes gehandhabt
}