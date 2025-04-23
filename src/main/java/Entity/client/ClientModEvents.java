package vallesminecraftmods.belly_of_the_beast.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import vallesminecraftmods.belly_of_the_beast.BellyOfTheBeast;
import vallesminecraftmods.belly_of_the_beast.client.model.SunkenSailorModel; // Importieren
import vallesminecraftmods.belly_of_the_beast.client.renderer.SunkenSailorRenderer; // Importieren
import vallesminecraftmods.belly_of_the_beast.init.EntityInit;

@Mod.EventBusSubscriber(modid = BellyOfTheBeast.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        // Hier könnten noch andere Client-Initialisierungen erfolgen
    }

    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityInit.SUNKEN_SAILOR.get(), SunkenSailorRenderer::new);
    }

    // Diese Methode ist notwendig, um die Modelldefinition (Knochen/Würfel) zu registrieren
    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(SunkenSailorModel.LAYER_LOCATION, SunkenSailorModel::createBodyLayer);
    }
}