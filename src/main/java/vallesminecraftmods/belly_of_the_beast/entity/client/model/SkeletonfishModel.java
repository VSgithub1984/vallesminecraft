package vallesminecraftmods.belly_of_the_beast.entity.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

import vallesminecraftmods.belly_of_the_beast.BellyOfTheBeast;
import vallesminecraftmods.belly_of_the_beast.entity.SkeletonfishprojectileEntity;

public class SkeletonfishModel extends HierarchicalModel<SkeletonfishprojectileEntity> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            new ResourceLocation(BellyOfTheBeast.MODID, "skeletonfish_projectile"), "main"); // Eigener Name für Layer

    private final ModelPart root;
    // Füge hier ggf. Teile hinzu, die animiert werden sollen (z.B. Flossen?)
    // private final ModelPart tailFin;

    public SkeletonfishModel(ModelPart root) {
        this.root = root;
        // Weise Teile zu, basierend auf createBodyLayer
        // this.tailFin = root.getChild("tail_fin"); // Beispiel
    }

    // !!! ERSETZE DIES DURCH DEINE ECHTE LAYER DEFINITION AUS BLOCKBENCH !!!
    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        // Platzhalter - UNBEDINGT ERSETZEN!
        partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -1.0F, -3.0F, 2.0F, 2.0F, 6.0F), PartPose.ZERO);

        return LayerDefinition.create(meshdefinition, 16, 16); // Texturgröße anpassen!
    }

    @Override
    public void setupAnim(SkeletonfishprojectileEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        // Keine komplexe Animation für ein Projektil nötig.
        // Optional: Leichte Rotation oder Wobble basierend auf ageInTicks?
        float wobble = Mth.sin(ageInTicks * 0.4F) * 0.1F;
        this.root.zRot = wobble;

        // Oder lasse es einfach starr nach vorne zeigen (wird durch Renderer gedreht)
    }

    @Override
    public @NotNull ModelPart root() {
        return this.root;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.root().render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}