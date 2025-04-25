package vallesminecraftmods.belly_of_the_beast;

import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;

import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import vallesminecraftmods.belly_of_the_beast.entity.client.ClientModEvents;
import vallesminecraftmods.belly_of_the_beast.init.EntityInit;
import vallesminecraftmods.belly_of_the_beast.init.ItemInit; // Erstellen wir gleich noch

@Mod(BellyOfTheBeast.MODID)
public class BellyOfTheBeast {
    public static final String MODID = "belly_of_the_beast";
    private static final Logger LOGGER = LogUtils.getLogger();


    public BellyOfTheBeast() {
        // Hole den Mod-Event-Bus über die Instanz des Ladekontexts
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus(); // <<< KORRIGIERTE ZEILE

        // Registriere Deferred Registers
        EntityInit.ENTITY_TYPES.register(modEventBus);
        ItemInit.ITEMS.register(modEventBus); // Für Spawn-Egg

        // Registriere Event-Listener für Mod-Bus
        modEventBus.addListener(this::commonSetup);
        // Registriere Client-Setup über separate Klasse
        modEventBus.addListener(ClientModEvents::onClientSetup); // Stelle sicher, dass diese Klasse existiert
        // Registriere Attribut-Erstellung über separate Klasse/Methode in EntityInit
        modEventBus.addListener(EntityInit::registerAttributes);

        // Registriere dich für Forge-Events (z.B. Server-Start)
        MinecraftForge.EVENT_BUS.register(this);

        // Listener für Creative Tab hinzufügen (optional, für Spawn-Egg)
        // Statt this::addCreative besser die Methode aus ItemInit verwenden, wenn sie dort definiert ist
        modEventBus.addListener(ItemInit::addCreative); // Stelle sicher, dass ItemInit existiert und Methode hat
    }

    // ... (Rest der Klasse bleibt gleich) ...
    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Starting Common Setup for {}", MODID);
        event.enqueueWork(EntityInit::registerSpawnPlacements);
    }

    // Kann entfernt werden, wenn ItemInit.addCreative verwendet wird
    // private void addCreative(BuildCreativeModeTabContentsEvent event) {
    // }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("Server Starting for {}", MODID);
    }
}