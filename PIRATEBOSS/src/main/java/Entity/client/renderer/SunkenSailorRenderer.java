package vallesminecraftmods.belly_of_the_beast.client.renderer;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import vallesminecraftmods.belly_of_the_beast.BellyOfTheBeast;
import vallesminecraftmods.belly_of_the_beast.client.model.SunkenSailorModel;
import vallesminecraftmods.belly_of_the_beast.entity.SunkenSailorEntity;

public class SunkenSailorRenderer extends MobRenderer<SunkenSailorEntity, SunkenSailorModel> {

    private static final ResourceLocation TEXTURE_LOCATION =
            new ResourceLocation(BellyOfTheBeast.MODID, "textures/entity/sunken_sailor.png");

    public SunkenSailorRenderer(EntityRendererProvider.Context context) {
        // Korrekte Initialisierung mit ModelLayerLocation
        super(context, new SunkenSailorModel(context.bakeLayer(SunkenSailorModel.LAYER_LOCATION)), 0.8f); // Schatten anpassen
    }

    @Override
    public ResourceLocation getTextureLocation(SunkenSailorEntity entity) {
        return TEXTURE_LOCATION;
    }
}