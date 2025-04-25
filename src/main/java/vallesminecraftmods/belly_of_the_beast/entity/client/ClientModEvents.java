package vallesminecraftmods.belly_of_the_beast.entity.client;

import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;


import vallesminecraftmods.belly_of_the_beast.BellyOfTheBeast;
import vallesminecraftmods.belly_of_the_beast.entity.SkeletonfishprojectileEntity;
import vallesminecraftmods.belly_of_the_beast.entity.client.renderer.SkeletonfishProjectileRenderer;
import vallesminecraftmods.belly_of_the_beast.entity.client.renderer.SunkenSailorRenderer; // Importieren
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
        // Renderer für das Projektil hinzufügen:
        event.registerEntityRenderer(EntityInit.SKELETONFISH_PROJECTILE.get(),
                // Verwende ArrowRenderer, wenn es wie ein Pfeil aussehen soll (braucht Pfeil-Textur!)
                // oder einen benutzerdefinierten Renderer, wenn es anders aussehen soll.
                context -> new SkeletonfishProjectileRenderer(context) {
                    @Override
                    public ResourceLocation getTextureLocation(SkeletonfishprojectileEntity skeletonfishprojectileEntity) {
                        return null;
                    }
                } // Beispiel: Standardpfeil-Textur
                // ODER wenn es ein Item (z.B. Knochenfisch?) rendern soll:
                // context -> new net.minecraft.client.renderer.entity.ThrownItemRenderer<>(context) // Braucht dann ein Item
        );
    }
    // Diese Methode ist notwendig, um die Modelldefinition (Knochen/Würfel) zu registrieren
    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(vallesminecraftmods.belly_of_the_beast.entity.client.model.SunkenSailorModel.LAYER_LOCATION, vallesminecraftmods.belly_of_the_beast.entity.client.model.SunkenSailorModel::createBodyLayer);
    }

}