package vallesminecraftmods.belly_of_the_beast.entity.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis; // NEUER Import für Rotationen
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import vallesminecraftmods.belly_of_the_beast.BellyOfTheBeast;
import vallesminecraftmods.belly_of_the_beast.BellyOfTheBeast;
import vallesminecraftmods.belly_of_the_beast.entity.client.model.SkeletonfishModel; // Import Model
import vallesminecraftmods.belly_of_the_beast.entity.SkeletonfishprojectileEntity;

public class SkeletonfishProjectileRenderer extends EntityRenderer<SkeletonfishprojectileEntity> {

    private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(BellyOfTheBeast.MODID, "resources/assets/belly_of_the_beast/projectiles/textures/bonefish.png"); // Pfad zur Textur anpassen!
    private final SkeletonfishModel model;

    public SkeletonfishProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
        // Lade das Modell über die Layer Definition
       this.model = new SkeletonfishModel(context.bakeLayer(SkeletonfishModel.LAYER_LOCATION));
        this.shadowRadius = 0.3F; // Kleiner Schatten für Projektil
    }

    @Override
    public void render(SkeletonfishprojectileEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();

        // Rotation basierend auf Projektilbewegung
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.getYRot()) - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, entity.xRotO, entity.getXRot())));

        // Skalierung, falls nötig
        poseStack.scale(1.0f, 1.0f, 1.0f); // Passe Skalierung an Modellgröße an

        // Model rendern
        VertexConsumer vertexconsumer = bufferSource.getBuffer(this.model.renderType(this.getTextureLocation(entity)));
        this.model.setupAnim(entity, 0, 0, entity.tickCount + partialTicks, 0, 0); // Einfache Animation aufrufen
        this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(SkeletonfishprojectileEntity entity) {
        return TEXTURE_LOCATION;
    }
}