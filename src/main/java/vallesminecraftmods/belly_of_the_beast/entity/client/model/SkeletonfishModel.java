package vallesminecraftmods.belly_of_the_beast.entity.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

import vallesminecraftmods.belly_of_the_beast.BellyOfTheBeast;
import vallesminecraftmods.belly_of_the_beast.entity.SkeletonfishprojectileEntity;

public class SkeletonfishModel extends HierarchicalModel<SkeletonfishprojectileEntity> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            new ResourceLocation(BellyOfTheBeast.MODID, "skeletonfishmodel"), "main"); // Eigener Name für Layer

    private final ModelPart root;
    // Füge hier ggf. Teile hinzu, die animiert werden sollen (z.B. Flossen?)
    // private final ModelPart tailFin;

    private final ModelPart bone;
    private final ModelPart dafeeesh;
    private final ModelPart tail;


    public SkeletonfishModel(ModelPart root) {
        this.bone = root.getChild("bone");
        this.dafeeesh = this.bone.getChild("dafeeesh");
        this.tail = this.dafeeesh.getChild("tail");
        this.root= root;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition dafeeesh = bone.addOrReplaceChild("dafeeesh", CubeListBuilder.create().texOffs(0, 18).addBox(-2.0F, -2.0F, 19.0F, 4.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-1.0F, -1.0F, 3.0F, 2.0F, 2.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(24, 25).addBox(-3.0F, -1.0F, 18.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(24, 27).addBox(-3.0F, 0.0F, 18.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(4, 32).addBox(-2.0F, 3.0F, 18.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(28, 27).addBox(-3.0F, 4.0F, 16.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 25).addBox(-4.0F, 0.0F, 16.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(12, 29).addBox(-3.0F, -1.0F, 16.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(18, 29).addBox(-3.0F, 4.0F, 14.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(4, 25).addBox(-4.0F, 0.0F, 14.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(28, 29).addBox(-3.0F, -1.0F, 14.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 30).addBox(-3.0F, 4.0F, 12.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(8, 25).addBox(-4.0F, 0.0F, 12.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(6, 30).addBox(-3.0F, -1.0F, 12.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(30, 25).addBox(-3.0F, 3.0F, 10.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(12, 31).addBox(-4.0F, 0.0F, 10.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(16, 31).addBox(-3.0F, -1.0F, 10.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(22, 31).addBox(-3.0F, -1.0F, 8.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 32).addBox(-4.0F, 0.0F, 8.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(28, 31).addBox(-3.0F, 2.0F, 8.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(8, 32).addBox(-2.0F, -1.0F, 6.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(16, 33).addBox(-3.0F, 0.0F, 6.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(20, 33).addBox(-2.0F, 1.0F, 6.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(24, 33).addBox(-2.0F, 0.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(24, 18).addBox(0.0F, -3.0F, 11.0F, 0.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(24, 25).mirror().addBox(1.0F, -1.0F, 18.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(24, 27).mirror().addBox(2.0F, 0.0F, 18.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(4, 32).mirror().addBox(1.0F, 3.0F, 18.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(28, 27).mirror().addBox(1.0F, 4.0F, 16.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 25).mirror().addBox(3.0F, 0.0F, 16.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(12, 29).mirror().addBox(1.0F, -1.0F, 16.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(18, 29).mirror().addBox(1.0F, 4.0F, 14.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(4, 25).mirror().addBox(3.0F, 0.0F, 14.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(28, 29).mirror().addBox(1.0F, -1.0F, 14.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 30).mirror().addBox(1.0F, 4.0F, 12.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(8, 25).mirror().addBox(3.0F, 0.0F, 12.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(6, 30).mirror().addBox(1.0F, -1.0F, 12.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(30, 25).mirror().addBox(1.0F, 3.0F, 10.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(12, 31).mirror().addBox(3.0F, 0.0F, 10.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(16, 31).mirror().addBox(1.0F, -1.0F, 10.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(22, 31).mirror().addBox(1.0F, -1.0F, 8.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 32).mirror().addBox(3.0F, 0.0F, 8.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(28, 31).mirror().addBox(1.0F, 2.0F, 8.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(8, 32).mirror().addBox(1.0F, -1.0F, 6.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(16, 33).mirror().addBox(2.0F, 0.0F, 6.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(20, 33).mirror().addBox(1.0F, 1.0F, 6.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(24, 33).mirror().addBox(1.0F, 0.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, -5.0F, -23.0F));

        PartDefinition tail = dafeeesh.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(14, 18).addBox(0.0F, -3.0F, -5.0F, 0.0F, 6.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 3.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
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