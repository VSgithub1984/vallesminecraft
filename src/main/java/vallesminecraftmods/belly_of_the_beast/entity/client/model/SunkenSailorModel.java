package vallesminecraftmods.belly_of_the_beast.entity.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel; // Geändert!
import net.minecraft.client.model.geom.ModelLayerLocation; // Importieren
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth; // Für Mth.clamp etc.


import org.jetbrains.annotations.NotNull;
import vallesminecraftmods.belly_of_the_beast.BellyOfTheBeast;
import vallesminecraftmods.belly_of_the_beast.entity.client.animation.SunkenSailorAnimations; // Importiere deine Animationsklasse
import vallesminecraftmods.belly_of_the_beast.entity.SunkenSailorEntity;

public class SunkenSailorModel extends HierarchicalModel<SunkenSailorEntity> { // Geändert!

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            new ResourceLocation(BellyOfTheBeast.MODID, "sunken_sailor"), "main");

    private final ModelPart theguyhimself;
    private final ModelPart torso;
    private final ModelPart upperbody;
    private final ModelPart armleft;
    private final ModelPart lowerarmleft;
    private final ModelPart handleft;
    private final ModelPart fingerlinks3;
    private final ModelPart fingerlinks32;
    private final ModelPart fingerlinks31;
    private final ModelPart fingerlinks1;
    private final ModelPart fingerlinks12;
    private final ModelPart fingerlinks11;
    private final ModelPart daumenleft;
    private final ModelPart daumenleft2;
    private final ModelPart daumenleft1;
    private final ModelPart fingerlinks2;
    private final ModelPart fingerlinks22;
    private final ModelPart fingerlinks21;
    private final ModelPart armright;
    private final ModelPart lowerarmright;
    private final ModelPart handright;
    private final ModelPart fingerrechts3;
    private final ModelPart fingerrechts32;
    private final ModelPart fingerrechts31;
    private final ModelPart fingerrechts1;
    private final ModelPart fingerrechts13;
    private final ModelPart fingerrechts11;
    private final ModelPart fingerrechts2;
    private final ModelPart fingerrechts22;
    private final ModelPart fingerechts21;
    private final ModelPart daumenright;
    private final ModelPart daumenright2;
    private final ModelPart daumenright1;
    private final ModelPart fishspear2;
    private final ModelPart neck;
    private final ModelPart head;
    private final ModelPart jaw;
    private final ModelPart jawleft;
    private final ModelPart jawright;
    private final ModelPart lowerbody;
    private final ModelPart boat;
    private final ModelPart fishspear;
    private ModelPart root;

    public SunkenSailorModel(@NotNull ModelPart root) {
        this.theguyhimself = root.getChild("theguyhimself");
        this.torso = this.theguyhimself.getChild("torso");
        this.upperbody = this.torso.getChild("upperbody");
        this.armleft = this.upperbody.getChild("armleft");
        this.lowerarmleft = this.armleft.getChild("lowerarmleft");
        this.handleft = this.lowerarmleft.getChild("handleft");
        this.fingerlinks3 = this.handleft.getChild("fingerlinks3");
        this.fingerlinks32 = this.fingerlinks3.getChild("fingerlinks32");
        this.fingerlinks31 = this.fingerlinks32.getChild("fingerlinks31");
        this.fingerlinks1 = this.handleft.getChild("fingerlinks1");
        this.fingerlinks12 = this.fingerlinks1.getChild("fingerlinks12");
        this.fingerlinks11 = this.fingerlinks12.getChild("fingerlinks11");
        this.daumenleft = this.handleft.getChild("daumenleft");
        this.daumenleft2 = this.daumenleft.getChild("daumenleft2");
        this.daumenleft1 = this.daumenleft2.getChild("daumenleft1");
        this.fingerlinks2 = this.handleft.getChild("fingerlinks2");
        this.fingerlinks22 = this.fingerlinks2.getChild("fingerlinks22");
        this.fingerlinks21 = this.fingerlinks22.getChild("fingerlinks21");
        this.armright = this.upperbody.getChild("armright");
        this.lowerarmright = this.armright.getChild("lowerarmright");
        this.handright = this.lowerarmright.getChild("handright");
        this.fingerrechts3 = this.handright.getChild("fingerrechts3");
        this.fingerrechts32 = this.fingerrechts3.getChild("fingerrechts32");
        this.fingerrechts31 = this.fingerrechts32.getChild("fingerrechts31");
        this.fingerrechts1 = this.handright.getChild("fingerrechts1");
        this.fingerrechts13 = this.fingerrechts1.getChild("fingerrechts13");
        this.fingerrechts11 = this.fingerrechts13.getChild("fingerrechts11");
        this.fingerrechts2 = this.handright.getChild("fingerrechts2");
        this.fingerrechts22 = this.fingerrechts2.getChild("fingerrechts22");
        this.fingerechts21 = this.fingerrechts22.getChild("fingerechts21");
        this.daumenright = this.handright.getChild("daumenright");
        this.daumenright2 = this.daumenright.getChild("daumenright2");
        this.daumenright1 = this.daumenright2.getChild("daumenright1");
        this.fishspear2 = this.handright.getChild("fishspear2");
        this.neck = this.upperbody.getChild("neck");
        this.head = this.neck.getChild("head");
        this.jaw = this.head.getChild("jaw");
        this.jawleft = this.jaw.getChild("jawleft");
        this.jawright = this.jaw.getChild("jawright");
        this.lowerbody = this.torso.getChild("lowerbody");
        this.boat = root.getChild("boat");
        this.fishspear = this.boat.getChild("fishspear");
        this.root = root;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();


        PartDefinition theguyhimself = partdefinition.addOrReplaceChild("theguyhimself", CubeListBuilder.create(), PartPose.offset(0.0F, 4.0F, 15.6F));

        PartDefinition torso = theguyhimself.addOrReplaceChild("torso", CubeListBuilder.create(), PartPose.offset(0.0F, -1.0F, -1.0F));

        PartDefinition upperbody = torso.addOrReplaceChild("upperbody", CubeListBuilder.create().texOffs(124, 23).addBox(-1.0F, -34.0F, -4.0F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(28, 98).addBox(-1.0F, -23.0F, 2.0F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(12, 112).addBox(-2.0F, -32.0F, -1.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(24, 112).addBox(-2.0F, -31.0F, 0.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(12, 112).mirror().addBox(-1.0F, -32.0F, -1.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(112, 53).addBox(-2.0F, -30.0F, 1.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(24, 112).mirror().addBox(-1.0F, -31.0F, 0.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(48, 113).addBox(-2.0F, -28.0F, 2.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(112, 53).mirror().addBox(-1.0F, -30.0F, 1.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(98, 65).addBox(-2.0F, -26.0F, 2.0F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(48, 113).mirror().addBox(-1.0F, -28.0F, 2.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(56, 99).addBox(-2.0F, -20.0F, 1.0F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(98, 65).mirror().addBox(-1.0F, -26.0F, 2.0F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(28, 98).mirror().addBox(-2.0F, -23.0F, 2.0F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(80, 102).addBox(-2.0F, -16.0F, 0.0F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(56, 99).mirror().addBox(-1.0F, -20.0F, 1.0F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(104, 16).addBox(-2.0F, -13.0F, -1.0F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(80, 102).mirror().addBox(-1.0F, -16.0F, 0.0F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(12, 105).addBox(-2.0F, -10.0F, -3.0F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(104, 16).mirror().addBox(-1.0F, -13.0F, -1.0F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(12, 105).mirror().addBox(-1.0F, -10.0F, -3.0F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(112, 114).addBox(-1.0F, -33.0F, -3.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(112, 114).mirror().addBox(-2.0F, -33.0F, -3.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(20, 121).addBox(-5.5934F, -22.0294F, 6.1898F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(116, 59).mirror().addBox(2.0F, -27.3807F, 0.6939F, 6.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(118, 3).addBox(2.0F, -23.3807F, 0.6939F, 7.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(118, 0).mirror().addBox(2.0F, -19.3807F, 0.6939F, 6.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(124, 78).mirror().addBox(2.0F, -15.3807F, 0.6939F, 5.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(124, 78).mirror().addBox(2.0F, -31.3807F, 0.6939F, 5.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(48, 39).addBox(-12.2535F, -34.0749F, -1.339F, 8.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(112, 120).addBox(-13.2535F, -34.0749F, -0.339F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(20, 121).mirror().addBox(1.5934F, -22.0294F, 6.1898F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(53, 39).mirror().addBox(9.2535F, -34.0749F, -1.339F, 8.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(54, 32).addBox(1.9948F, -30.3135F, 4.2158F, 14.0F, 11.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(76, 34).addBox(1.7584F, -24.7907F, 5.0576F, 11.0F, 8.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(44, 51).addBox(3.2535F, -33.0749F, 3.661F, 14.0F, 10.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(115, 118).mirror().addBox(17.2535F, -34.0749F, -2.339F, 1.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(124, 78).addBox(-7.0F, -15.3807F, 0.6939F, 5.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(124, 78).addBox(-7.0F, -31.3807F, 0.6939F, 5.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(53, 39).addBox(-17.2535F, -34.0749F, -1.339F, 8.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(44, 51).mirror().addBox(-17.2535F, -33.0749F, 3.661F, 14.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(115, 118).addBox(-18.2535F, -34.0749F, -2.339F, 1.0F, 3.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(116, 59).addBox(-8.0F, -27.3807F, 0.6939F, 6.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(118, 3).mirror().addBox(-9.0F, -23.3807F, 0.6939F, 7.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(118, 0).addBox(-8.0F, -19.3807F, 0.6939F, 6.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(54, 32).mirror().addBox(-15.9948F, -30.3135F, 4.2158F, 14.0F, 11.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(76, 34).mirror().addBox(-12.7584F, -24.7907F, 5.0576F, 11.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, -9.0F, -1.0F));

        PartDefinition cube_r1 = upperbody.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(219, 216).addBox(-16.2317F, -0.285F, -2.4387F, 16.0F, 25.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, -27.0F, 4.0F, 0.0F, -0.3927F, 0.0F));

        PartDefinition cube_r2 = upperbody.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(219, 216).mirror().addBox(0.2317F, -0.285F, -2.4387F, 16.0F, 25.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(4.0F, -27.0F, 4.0F, 0.0F, 0.3927F, 0.0F));

        PartDefinition cube_r3 = upperbody.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(219, 216).addBox(-16.2317F, -1.285F, -2.4387F, 16.0F, 25.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, -16.0F, 2.0F, 0.0F, -0.3927F, 0.0F));

        PartDefinition cube_r4 = upperbody.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(219, 216).addBox(-16.2317F, -1.285F, -2.4387F, 16.0F, 25.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.0F, -22.0F, -22.0F, 0.0F, 0.7854F, 0.0F));

        PartDefinition cube_r5 = upperbody.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(123, 78).addBox(-7.0F, -1.3807F, -1.3061F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, -10.0F, -17.0F, 0.0F, 0.0F, 0.3927F));

        PartDefinition cube_r6 = upperbody.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(123, 78).addBox(-7.0F, -1.3807F, -1.3061F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.0F, -18.0F, -21.0F, 0.0F, 0.0F, 0.3927F));

        PartDefinition cube_r7 = upperbody.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(123, 78).addBox(-7.0F, -1.3807F, -1.3061F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, -26.0F, -17.0F, 0.0F, 0.0F, 0.3927F));

        PartDefinition cube_r8 = upperbody.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(118, 6).addBox(-6.2317F, -0.285F, -1.4387F, 6.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(118, 6).addBox(-6.2317F, 15.715F, -1.4387F, 6.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.0F, -31.0F, 2.0F, 0.0F, -0.3927F, 0.0F));

        PartDefinition cube_r9 = upperbody.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(118, 6).addBox(-6.2317F, -0.285F, -1.4387F, 6.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(118, 6).addBox(-6.2317F, 15.715F, -1.4387F, 6.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-13.0F, -31.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition cube_r10 = upperbody.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(0, 56).addBox(-1.6696F, -3.7405F, -12.3756F, 1.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 56).addBox(-1.6696F, 12.2595F, -12.3756F, 1.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-17.0F, -27.0F, 0.0F, 0.0F, -0.3927F, 0.0F));

        PartDefinition cube_r11 = upperbody.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(0, 56).addBox(-1.6696F, -3.7405F, -12.3756F, 1.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 56).addBox(-1.6696F, 12.2595F, -12.3756F, 1.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-16.0F, -27.0F, -8.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition cube_r12 = upperbody.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(0, 56).addBox(-1.6696F, -3.7405F, -12.3756F, 1.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 56).addBox(-1.6696F, 4.2595F, -12.3756F, 1.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-19.0F, -23.0F, -10.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition cube_r13 = upperbody.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(-1, 55).addBox(-1.6696F, -3.7405F, -13.3756F, 1.0F, 2.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-21.0F, -19.0F, -12.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition cube_r14 = upperbody.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(0, 56).addBox(-1.6696F, -3.7405F, -12.3756F, 1.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 56).addBox(-1.6696F, -11.7405F, -12.3756F, 1.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-20.0F, -15.0F, -2.0F, 0.0F, -0.3927F, 0.0F));

        PartDefinition cube_r15 = upperbody.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(-1, 55).addBox(-1.6696F, -3.7405F, -13.3756F, 1.0F, 2.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-22.0F, -19.0F, -3.0F, 0.0F, -0.3927F, 0.0F));

        PartDefinition cube_r16 = upperbody.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(106, 50).addBox(-8.2317F, -0.285F, -1.4387F, 8.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-16.0F, -23.0F, -1.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition cube_r17 = upperbody.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(106, 97).addBox(-7.2317F, -0.285F, -1.4387F, 7.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(96, 85).addBox(-7.2317F, -8.285F, -1.4387F, 7.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-15.0F, -19.0F, -1.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition cube_r18 = upperbody.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(96, 85).addBox(-7.2317F, -0.285F, -1.4387F, 7.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, -27.0F, 2.0F, 0.0F, -0.3927F, 0.0F));

        PartDefinition cube_r19 = upperbody.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(106, 50).addBox(-8.2317F, -0.285F, -1.4387F, 8.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.0F, -23.0F, 2.0F, 0.0F, -0.3927F, 0.0F));

        PartDefinition cube_r20 = upperbody.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(0, 56).mirror().addBox(0.6696F, -3.7405F, -12.3756F, 1.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 56).mirror().addBox(0.6696F, 12.2595F, -12.3756F, 1.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(16.0F, -27.0F, -8.0F, 0.0F, 0.7854F, 0.0F));

        PartDefinition cube_r21 = upperbody.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(0, 56).mirror().addBox(0.6696F, -3.7405F, -12.3756F, 1.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 56).mirror().addBox(0.6696F, 12.2595F, -12.3756F, 1.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(17.0F, -27.0F, 0.0F, 0.0F, 0.3927F, 0.0F));

        PartDefinition cube_r22 = upperbody.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(118, 6).mirror().addBox(0.2317F, -0.285F, -1.4387F, 6.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(118, 6).mirror().addBox(0.2317F, 15.715F, -1.4387F, 6.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(13.0F, -31.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        PartDefinition cube_r23 = upperbody.addOrReplaceChild("cube_r23", CubeListBuilder.create().texOffs(123, 78).mirror().addBox(1.0F, -1.3807F, -1.3061F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(6.0F, -18.0F, -21.0F, 0.0F, 0.0F, -0.3927F));

        PartDefinition cube_r24 = upperbody.addOrReplaceChild("cube_r24", CubeListBuilder.create().texOffs(123, 78).mirror().addBox(1.0F, -1.3807F, -1.3061F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(2.0F, -10.0F, -17.0F, 0.0F, 0.0F, -0.3927F));

        PartDefinition cube_r25 = upperbody.addOrReplaceChild("cube_r25", CubeListBuilder.create().texOffs(123, 78).mirror().addBox(1.0F, -1.3807F, -1.3061F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(2.0F, -26.0F, -17.0F, 0.0F, 0.0F, -0.3927F));

        PartDefinition cube_r26 = upperbody.addOrReplaceChild("cube_r26", CubeListBuilder.create().texOffs(118, 6).mirror().addBox(0.2317F, -0.285F, -1.4387F, 6.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(118, 6).mirror().addBox(0.2317F, 15.715F, -1.4387F, 6.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(7.0F, -31.0F, 2.0F, 0.0F, 0.3927F, 0.0F));

        PartDefinition cube_r27 = upperbody.addOrReplaceChild("cube_r27", CubeListBuilder.create().texOffs(0, 56).mirror().addBox(0.6696F, -3.7405F, -12.3756F, 1.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 56).mirror().addBox(0.6696F, -11.7405F, -12.3756F, 1.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(19.0F, -15.0F, -10.0F, 0.0F, 0.7854F, 0.0F));

        PartDefinition cube_r28 = upperbody.addOrReplaceChild("cube_r28", CubeListBuilder.create().texOffs(-1, 55).mirror().addBox(0.6696F, -3.7405F, -13.3756F, 1.0F, 2.0F, 9.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(21.0F, -19.0F, -12.0F, 0.0F, 0.7854F, 0.0F));

        PartDefinition cube_r29 = upperbody.addOrReplaceChild("cube_r29", CubeListBuilder.create().texOffs(0, 56).mirror().addBox(0.6696F, -3.7405F, -12.3756F, 1.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 56).mirror().addBox(0.6696F, -11.7405F, -12.3756F, 1.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(20.0F, -15.0F, -2.0F, 0.0F, 0.3927F, 0.0F));

        PartDefinition cube_r30 = upperbody.addOrReplaceChild("cube_r30", CubeListBuilder.create().texOffs(-1, 55).mirror().addBox(0.6696F, -3.7405F, -13.3756F, 1.0F, 2.0F, 9.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(22.0F, -19.0F, -3.0F, 0.0F, 0.3927F, 0.0F));

        PartDefinition cube_r31 = upperbody.addOrReplaceChild("cube_r31", CubeListBuilder.create().texOffs(96, 85).mirror().addBox(0.2317F, -0.285F, -1.4387F, 7.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(106, 97).mirror().addBox(0.2317F, 7.715F, -1.4387F, 7.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(15.0F, -27.0F, -1.0F, 0.0F, 0.7854F, 0.0F));

        PartDefinition cube_r32 = upperbody.addOrReplaceChild("cube_r32", CubeListBuilder.create().texOffs(106, 50).mirror().addBox(0.2317F, -0.285F, -1.4387F, 8.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(16.0F, -23.0F, -1.0F, 0.0F, 0.7854F, 0.0F));

        PartDefinition cube_r33 = upperbody.addOrReplaceChild("cube_r33", CubeListBuilder.create().texOffs(96, 85).mirror().addBox(0.2317F, -0.285F, -1.4387F, 7.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(106, 97).mirror().addBox(0.2317F, 7.715F, -1.4387F, 7.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(8.0F, -27.0F, 2.0F, 0.0F, 0.3927F, 0.0F));

        PartDefinition cube_r34 = upperbody.addOrReplaceChild("cube_r34", CubeListBuilder.create().texOffs(106, 50).mirror().addBox(0.2317F, -0.285F, -1.4387F, 8.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(9.0F, -23.0F, 2.0F, 0.0F, 0.3927F, 0.0F));

        PartDefinition armleft = upperbody.addOrReplaceChild("armleft", CubeListBuilder.create().texOffs(63, 65).mirror().addBox(1.2535F, -2.0749F, -1.739F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 0).mirror().addBox(3.2535F, -1.0749F, -0.739F, 25.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(28, 66).mirror().addBox(23.2535F, -2.0749F, -1.739F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(18.0F, -30.0F, -1.6F));

        PartDefinition lowerarmleft = armleft.addOrReplaceChild("lowerarmleft", CubeListBuilder.create().texOffs(59, 38).mirror().addBox(2.2535F, -2.0749F, -3.339F, 4.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 14).mirror().addBox(5.2535F, -1.0749F, -0.339F, 22.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(16, 22).mirror().addBox(6.2535F, -2.0749F, -3.339F, 21.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(90, 36).mirror().addBox(26.2535F, -2.0749F, -3.339F, 3.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(30.0F, 0.0F, 0.6F));

        PartDefinition handleft = lowerarmleft.addOrReplaceChild("handleft", CubeListBuilder.create().texOffs(61, 145).addBox(2.3672F, -1.0F, -1.2458F, 5.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(84, 129).addBox(8.3672F, -1.0F, -2.2458F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(84, 129).addBox(2.3672F, -1.0F, -6.2458F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(81, 162).addBox(3.3672F, -1.0F, 4.7542F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(62, 150).addBox(-0.6328F, -1.0F, 2.7542F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(64, 140).addBox(-0.6328F, -1.0F, -3.2458F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(32.0F, 0.0F, 0.0F));

        PartDefinition fingerlinks3 = handleft.addOrReplaceChild("fingerlinks3", CubeListBuilder.create(), PartPose.offset(11.0F, 0.0F, 6.0F));

        PartDefinition fingerlinks32 = fingerlinks3.addOrReplaceChild("fingerlinks32", CubeListBuilder.create().texOffs(97, 191).addBox(1.3672F, -1.0F, -1.2458F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(126, 99).addBox(6.3672F, -1.0F, -1.2458F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(102, 126).addBox(2.3672F, -1.0F, -0.2458F, 5.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(84, 129).addBox(-5.6328F, -1.0F, -5.2458F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(84, 129).addBox(-1.6328F, -1.0F, -4.2458F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(69, 143).addBox(-3.6328F, -1.0F, -0.2458F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 0.0F, 2.0F));

        PartDefinition fingerlinks31 = fingerlinks32.addOrReplaceChild("fingerlinks31", CubeListBuilder.create().texOffs(133, 23).addBox(2.1428F, -1.1393F, -1.8007F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(95, 75).addBox(7.1428F, -1.1393F, -1.8007F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(140, 122).addBox(4.1428F, -1.1393F, -0.8007F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(9.2244F, 0.1393F, 0.5549F));

        PartDefinition fingerlinks1 = handleft.addOrReplaceChild("fingerlinks1", CubeListBuilder.create(), PartPose.offset(11.0F, 0.0F, -8.0F));

        PartDefinition fingerlinks12 = fingerlinks1.addOrReplaceChild("fingerlinks12", CubeListBuilder.create().texOffs(102, 126).addBox(1.3672F, -1.0F, -0.2458F, 5.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(126, 99).addBox(5.3672F, -1.0F, -1.2458F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(104, 131).addBox(0.3672F, -1.0F, -1.2458F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(103, 145).addBox(-6.6328F, -1.0F, -2.2458F, 5.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(7.0F, 0.0F, -1.0F));

        PartDefinition fingerlinks11 = fingerlinks12.addOrReplaceChild("fingerlinks11", CubeListBuilder.create().texOffs(140, 122).addBox(3.3672F, -1.0F, -0.2458F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(95, 75).addBox(6.3672F, -1.0F, -1.2458F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(133, 23).addBox(1.3672F, -1.0F, -1.2458F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(9.0F, 0.0F, 0.0F));

        PartDefinition daumenleft = handleft.addOrReplaceChild("daumenleft", CubeListBuilder.create(), PartPose.offset(5.0F, 0.0F, -8.0F));

        PartDefinition daumenleft2 = daumenleft.addOrReplaceChild("daumenleft2", CubeListBuilder.create().texOffs(110, 161).addBox(-1.6328F, -1.0F, 1.7542F, 4.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 0.0F, -3.0F));

        PartDefinition cube_r35 = daumenleft2.addOrReplaceChild("cube_r35", CubeListBuilder.create().texOffs(99, 120).addBox(-2.5F, -1.0F, 2.25F, 5.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(79, 143).addBox(-2.5F, -1.0F, 6.25F, 5.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(102, 125).addBox(-1.5F, -1.0F, 4.25F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.8672F, 0.0F, -7.4958F, 0.0F, -0.7854F, 0.0F));

        PartDefinition daumenleft1 = daumenleft2.addOrReplaceChild("daumenleft1", CubeListBuilder.create(), PartPose.offset(6.0F, 0.0F, -6.0F));

        PartDefinition cube_r36 = daumenleft1.addOrReplaceChild("cube_r36", CubeListBuilder.create().texOffs(49, 103).addBox(-2.5F, -1.0F, -9.75F, 5.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(53, 106).addBox(-1.5F, -1.0F, -5.75F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(125, 128).addBox(-2.5F, -1.0F, -4.75F, 5.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1328F, 0.0F, 0.5042F, 0.0F, -0.7854F, 0.0F));

        PartDefinition fingerlinks2 = handleft.addOrReplaceChild("fingerlinks2", CubeListBuilder.create(), PartPose.offset(12.0F, 0.0F, 0.0F));

        PartDefinition fingerlinks22 = fingerlinks2.addOrReplaceChild("fingerlinks22", CubeListBuilder.create().texOffs(102, 126).addBox(1.3672F, -1.0F, -0.2458F, 5.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(126, 99).addBox(5.3672F, -1.0F, -1.2458F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(102, 162).addBox(0.3672F, -1.0F, -1.2458F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(87, 111).addBox(-7.6328F, -1.0F, -3.2458F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(7.0F, 0.0F, 0.0F));

        PartDefinition fingerlinks21 = fingerlinks22.addOrReplaceChild("fingerlinks21", CubeListBuilder.create().texOffs(140, 122).addBox(4.3672F, -1.0F, -0.2458F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(95, 75).addBox(7.3672F, -1.0F, -1.2458F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(133, 23).addBox(2.3672F, -1.0F, -1.2458F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(8.0F, 0.0F, 0.0F));

        PartDefinition armright = upperbody.addOrReplaceChild("armright", CubeListBuilder.create().texOffs(28, 66).addBox(-28.2535F, -2.0749F, -2.739F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-28.2535F, -1.0749F, -1.739F, 25.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(63, 65).addBox(-6.2535F, -2.0749F, -2.739F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(220, 225).addBox(-32.2317F, -1.285F, 2.1613F, 14.0F, 20.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(223, 214).addBox(-12.2317F, -1.285F, 1.1613F, 13.0F, 13.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(221, 214).addBox(-22.2317F, -1.285F, 1.1613F, 14.0F, 16.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-18.0F, -30.0F, -0.6F));

        PartDefinition lowerarmright = armright.addOrReplaceChild("lowerarmright", CubeListBuilder.create().texOffs(59, 38).addBox(-5.2535F, -2.0749F, -2.339F, 4.0F, 4.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 12).addBox(-26.2535F, -1.0749F, 0.661F, 22.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(16, 22).addBox(-26.2535F, -2.0749F, -2.339F, 21.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(90, 36).addBox(-28.2535F, -2.0749F, -2.339F, 3.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-31.0F, 0.0F, -1.4F));

        PartDefinition handright = lowerarmright.addOrReplaceChild("handright", CubeListBuilder.create().texOffs(57, 161).mirror().addBox(-9.3672F, -1.0F, -0.2458F, 5.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(95, 171).mirror().addBox(-6.3672F, -1.0F, -5.2458F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(95, 171).mirror().addBox(-3.3672F, -1.0F, 3.7542F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(105, 153).mirror().addBox(-3.3672F, -1.0F, -2.2458F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-29.0F, 0.0F, 0.0F));

        PartDefinition fingerrechts3 = handright.addOrReplaceChild("fingerrechts3", CubeListBuilder.create(), PartPose.offset(-11.0F, 0.0F, 8.0F));

        PartDefinition fingerrechts32 = fingerrechts3.addOrReplaceChild("fingerrechts32", CubeListBuilder.create().texOffs(81, 174).mirror().addBox(-2.3672F, -1.0F, -1.2458F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(123, 100).mirror().addBox(-6.3672F, -1.0F, -1.2458F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(106, 135).mirror().addBox(-5.3672F, -1.0F, -0.2458F, 5.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(95, 171).mirror().addBox(5.6328F, -1.0F, -5.2458F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(125, 170).mirror().addBox(0.6328F, -1.0F, -4.2458F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(73, 168).mirror().addBox(2.6328F, -1.0F, -0.2458F, 3.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(78, 129).mirror().addBox(8.6328F, -1.0F, -3.2458F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-7.0F, 0.0F, 1.0F));

        PartDefinition fingerrechts31 = fingerrechts32.addOrReplaceChild("fingerrechts31", CubeListBuilder.create().texOffs(130, 24).mirror().addBox(-4.3672F, -1.0F, -1.2458F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(92, 76).mirror().addBox(-9.3672F, -1.0F, -1.2458F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(137, 123).mirror().addBox(-7.3672F, -1.0F, -0.2458F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-7.0F, 0.0F, 0.0F));

        PartDefinition fingerrechts1 = handright.addOrReplaceChild("fingerrechts1", CubeListBuilder.create(), PartPose.offset(-15.0F, 0.0F, -8.0F));

        PartDefinition fingerrechts13 = fingerrechts1.addOrReplaceChild("fingerrechts13", CubeListBuilder.create().texOffs(113, 146).mirror().addBox(-7.3672F, -1.0F, -0.2458F, 5.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(123, 100).mirror().addBox(-8.3672F, -1.0F, -1.2458F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(106, 126).mirror().addBox(-3.3672F, -1.0F, -1.2458F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(38, 160).mirror().addBox(0.6328F, -1.0F, -0.2458F, 5.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-4.0F, 0.0F, 0.0F));

        PartDefinition fingerrechts11 = fingerrechts13.addOrReplaceChild("fingerrechts11", CubeListBuilder.create().texOffs(137, 123).mirror().addBox(-6.3672F, -1.0F, -0.2458F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(92, 76).mirror().addBox(-8.3672F, -1.0F, -1.2458F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(130, 24).mirror().addBox(-3.3672F, -1.0F, -1.2458F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-10.0F, 0.0F, 0.0F));

        PartDefinition fingerrechts2 = handright.addOrReplaceChild("fingerrechts2", CubeListBuilder.create(), PartPose.offset(-15.0F, 0.0F, 1.0F));

        PartDefinition fingerrechts22 = fingerrechts2.addOrReplaceChild("fingerrechts22", CubeListBuilder.create().texOffs(119, 142).mirror().addBox(-8.3672F, -1.0F, -0.2458F, 5.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(123, 100).mirror().addBox(-9.3672F, -1.0F, -1.2458F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(111, 180).mirror().addBox(-4.3672F, -1.0F, -1.2458F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(83, 143).mirror().addBox(0.6328F, -1.0F, -2.2458F, 5.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(95, 171).mirror().addBox(6.6328F, -1.0F, -2.2458F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-4.0F, 0.0F, 0.0F));

        PartDefinition fingerechts21 = fingerrechts22.addOrReplaceChild("fingerechts21", CubeListBuilder.create().texOffs(137, 123).mirror().addBox(-7.3672F, -1.0F, -0.2458F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(92, 76).mirror().addBox(-9.3672F, -1.0F, -1.2458F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(130, 24).mirror().addBox(-4.3672F, -1.0F, -1.2458F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-10.0F, 0.0F, 0.0F));

        PartDefinition daumenright = handright.addOrReplaceChild("daumenright", CubeListBuilder.create(), PartPose.offset(-8.0F, 0.0F, -6.0F));

        PartDefinition daumenright2 = daumenright.addOrReplaceChild("daumenright2", CubeListBuilder.create().texOffs(63, 133).mirror().addBox(-1.3672F, -1.0F, 2.7542F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-2.0F, 0.0F, -4.0F));

        PartDefinition cube_r37 = daumenright2.addOrReplaceChild("cube_r37", CubeListBuilder.create().texOffs(86, 151).mirror().addBox(-2.5F, -1.0F, 2.25F, 5.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(119, 153).mirror().addBox(-2.5F, -1.0F, 6.25F, 5.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(99, 126).mirror().addBox(-1.5F, -1.0F, 4.25F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-6.8672F, 0.0F, -6.4958F, 0.0F, 0.7854F, 0.0F));

        PartDefinition daumenright1 = daumenright2.addOrReplaceChild("daumenright1", CubeListBuilder.create(), PartPose.offset(-4.0F, 0.0F, -7.0F));

        PartDefinition cube_r38 = daumenright1.addOrReplaceChild("cube_r38", CubeListBuilder.create().texOffs(46, 104).mirror().addBox(-2.5F, -1.0F, -9.75F, 5.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(50, 107).mirror().addBox(-1.5F, -1.0F, -5.75F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(122, 129).mirror().addBox(-2.5F, -1.0F, -4.75F, 5.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.8672F, 0.0F, 2.5042F, 0.0F, 0.7854F, 0.0F));

        PartDefinition fishspear2 = handright.addOrReplaceChild("fishspear2", CubeListBuilder.create().texOffs(172, 136).addBox(-60.798F, -65.5944F, 35.5564F, 3.0F, 11.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(174, 0).addBox(-59.798F, -6.5944F, 36.5564F, 2.0F, 59.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(174, 0).addBox(-59.798F, -54.5944F, 36.5564F, 2.0F, 72.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(174, 138).addBox(-63.798F, -64.5944F, 36.5564F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(174, 138).addBox(-65.798F, -62.5944F, 36.5564F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(174, 138).addBox(-67.798F, -60.5944F, 36.5564F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(174, 138).addBox(-69.798F, -62.5944F, 36.5564F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(174, 138).addBox(-71.798F, -64.5944F, 36.5564F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(174, 138).addBox(-70.798F, -66.5944F, 36.5564F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(175, 139).addBox(-68.798F, -68.5944F, 37.5564F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(174, 138).addBox(-67.798F, -70.5944F, 36.5564F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(175, 139).addBox(-65.798F, -72.5944F, 37.5564F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(175, 139).addBox(-64.798F, -74.5944F, 37.5564F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(177, 139).addBox(-62.798F, -81.5944F, 37.5564F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(171, 136).addBox(-57.798F, -67.5944F, 35.5564F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(174, 138).addBox(-54.798F, -66.5944F, 36.5564F, 3.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(173, 138).addBox(-53.798F, -63.5944F, 36.5564F, 4.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(173, 138).addBox(-51.798F, -63.5944F, 36.5564F, 4.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(174, 138).addBox(-51.798F, -66.5944F, 36.5564F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(173, 138).addBox(-54.798F, -69.5944F, 36.5564F, 5.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(174, 138).addBox(-55.798F, -71.5944F, 36.5564F, 4.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(175, 139).addBox(-57.798F, -73.5944F, 37.5564F, 5.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(174, 138).addBox(-59.798F, -82.5944F, 36.5564F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(175, 139).addBox(-58.798F, -85.5944F, 36.5564F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(176, 139).addBox(-57.798F, -89.5944F, 36.5564F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(173, 138).addBox(-50.798F, -62.5944F, 36.5564F, 4.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(172, 136).addBox(-60.798F, 8.4056F, 35.5564F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(172, 136).addBox(-60.798F, 1.4056F, 35.5564F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(172, 136).addBox(-60.798F, -5.5944F, 35.5564F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(172, 136).addBox(-60.798F, -11.5944F, 35.5564F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-11.0F, 0.0F, -0.6F, 1.2435F, 0.3585F, 2.9332F));

        PartDefinition neck = upperbody.addOrReplaceChild("neck", CubeListBuilder.create().texOffs(78, 139).addBox(-0.6429F, -11.2126F, -11.3821F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(98, 139).addBox(-0.6429F, -9.2126F, -10.3821F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(106, 139).addBox(-0.6429F, -7.2126F, -9.3821F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(52, 123).addBox(-0.6429F, -6.2126F, -8.3821F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(62, 123).addBox(-0.6429F, -5.2126F, -6.3821F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(72, 123).addBox(-0.6429F, -3.2126F, -4.3821F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.3571F, -32.6429F, -0.4286F));

        PartDefinition head = neck.addOrReplaceChild("head", CubeListBuilder.create().texOffs(75, 94).addBox(4.0F, -1.0F, -14.0F, 3.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(1, 23).addBox(-3.0F, -9.0F, -11.0F, 4.0F, 2.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(36, 67).addBox(1.0F, -5.0F, -13.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(36, 68).addBox(0.0F, -5.0F, -13.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(20, 56).addBox(6.0F, -5.0F, -7.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(20, 56).addBox(3.0F, -6.0F, -1.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(60, 71).addBox(-1.0F, -3.0F, -14.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(94, 24).addBox(-4.0F, -7.0F, -15.0F, 2.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(30, 131).addBox(-4.0F, -6.0F, -13.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(131, 8).addBox(6.0F, 0.0F, -14.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 86).addBox(-4.0F, -6.0F, -12.0F, 2.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(92, 54).addBox(-4.0F, -2.0F, -8.0F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(132, 28).addBox(4.0F, 0.0F, -12.0F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(132, 28).mirror().addBox(-4.0F, 0.0F, -12.0F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(132, 28).mirror().addBox(-4.0F, -1.0F, -13.0F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(36, 67).mirror().addBox(0.0F, -6.0F, -13.0F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(68, 87).addBox(-1.0F, -3.0F, -14.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(66, 86).addBox(-4.0F, -2.0F, -15.0F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(132, 46).addBox(2.0F, -1.0F, -15.0F, 5.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(132, 46).addBox(-5.0F, -1.0F, -15.0F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(134, 45).addBox(5.0F, -3.0F, -6.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(126, 135).addBox(-1.0F, -6.0F, -14.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(64, 57).addBox(-2.0F, -10.0F, -10.0F, 3.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(115, 19).addBox(-5.0F, -1.0F, -15.0F, 5.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(116, 20).addBox(-1.0F, 0.0F, -14.0F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(116, 20).addBox(-5.0F, -1.0F, -14.0F, 5.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(20, 56).mirror().addBox(-5.0F, -5.0F, -7.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(18, 54).mirror().addBox(-5.0F, -2.0F, -13.0F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(32, 82).mirror().addBox(-2.0F, -7.0F, -15.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(142, 38).addBox(-5.0F, -2.0F, -8.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(24, 50).addBox(-5.0F, -6.0F, -13.0F, 1.0F, 1.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(142, 38).mirror().addBox(5.0F, -2.0F, -8.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(16, 87).addBox(-4.0F, -7.0F, -7.0F, 1.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(122, 120).addBox(-4.0F, -3.0F, -7.0F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(122, 120).addBox(-4.0F, -3.0F, -13.0F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(123, 121).addBox(-4.0F, -3.0F, -12.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(123, 121).addBox(-4.0F, -1.0F, -10.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(122, 120).addBox(-4.0F, -3.0F, -13.0F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(78, 97).mirror().addBox(-5.0F, -1.0F, -11.0F, 3.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 46).addBox(-3.0F, -8.0F, -14.0F, 3.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(8, 52).addBox(6.0F, -6.0F, -7.0F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(9, 53).addBox(5.0F, -3.0F, -7.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(8, 52).addBox(0.0F, -6.0F, -13.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(6, 51).addBox(-1.0F, -6.0F, -14.0F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(10, 121).addBox(-4.0F, -5.0F, -11.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(54, 80).addBox(0.0F, -2.0F, -1.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(138, 69).addBox(-1.0F, -3.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(122, 64).mirror().addBox(-2.0F, -6.0F, -1.0F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(84, 57).addBox(-3.0F, -8.0F, -2.0F, 6.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(138, 69).mirror().addBox(1.0F, -3.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(122, 64).addBox(1.0F, -6.0F, -1.0F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(124, 64).addBox(5.0F, -3.0F, -4.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(123, 65).addBox(0.0F, -6.0F, -1.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(122, 64).addBox(0.0F, -8.0F, -2.0F, 3.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(96, 36).addBox(-5.0F, -1.0F, -15.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(53, 120).addBox(5.0F, -1.0F, -13.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(53, 120).addBox(6.0F, 0.0F, -14.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(52, 119).addBox(4.0F, -2.0F, -14.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(53, 120).addBox(5.0F, -1.0F, -15.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(60, 66).addBox(1.0F, -5.0F, -13.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(60, 66).mirror().addBox(0.0F, -3.0F, -13.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(28, 91).addBox(0.0F, -3.0F, -14.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(18, 65).addBox(2.0F, 1.0F, -14.0F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(50, 117).mirror().addBox(-5.0F, -2.0F, -13.0F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(52, 119).mirror().addBox(-4.0F, -1.0F, -12.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(53, 75).addBox(4.0F, -2.0F, -9.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 65).addBox(-4.0F, -3.0F, -14.0F, 1.0F, 3.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(132, 93).addBox(5.0F, -3.0F, -7.0F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(118, 12).addBox(1.0F, 0.0F, -14.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(31, 27).addBox(1.0F, -9.0F, -11.0F, 1.0F, 2.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(30, 29).addBox(-2.0F, -10.0F, -11.0F, 3.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(49, 84).mirror().addBox(-3.0F, -2.0F, -15.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(49, 84).mirror().addBox(-4.0F, 0.0F, -14.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(48, 84).mirror().addBox(-4.0F, -3.0F, -14.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(118, 12).mirror().addBox(-5.0F, 0.0F, -14.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(132, 93).mirror().addBox(-4.0F, -3.0F, -7.0F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(32, 84).mirror().addBox(-4.0F, -3.0F, -13.0F, 2.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(4, 68).mirror().addBox(5.0F, -4.0F, -6.0F, 1.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 65).mirror().addBox(4.0F, -2.0F, -14.0F, 2.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(54, 76).mirror().addBox(-4.0F, -2.0F, -8.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-0.6429F, -8.3571F, -11.5714F));

        PartDefinition jaw = head.addOrReplaceChild("jaw", CubeListBuilder.create(), PartPose.offset(1.0F, 2.0F, -11.0F));

        PartDefinition jawleft = jaw.addOrReplaceChild("jawleft", CubeListBuilder.create().texOffs(110, 70).addBox(2.0F, 0.0F, -4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(112, 59).addBox(2.0F, 1.0F, -4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(98, 137).addBox(5.0F, 0.0F, -4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(134, 142).addBox(6.0F, 1.0F, -4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(88, 112).mirror().addBox(3.0F, 0.0F, -4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(142, 139).addBox(5.0F, 1.0F, -4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(114, 73).mirror().addBox(1.0F, 3.0F, -4.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(68, 10).addBox(3.0F, 2.0F, -4.0F, 4.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(92, 94).mirror().addBox(3.0F, 3.0F, -3.0F, 3.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(142, 120).mirror().addBox(4.0F, 1.0F, -4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(94, 131).addBox(6.0F, -1.0F, 0.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, -1.0F, 1.0F));

        PartDefinition jawright = jaw.addOrReplaceChild("jawright", CubeListBuilder.create().texOffs(12, 103).addBox(-7.0F, 3.0F, -4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(12, 103).addBox(-6.0F, 3.0F, -4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(88, 112).addBox(-4.0F, 0.0F, -4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(142, 120).addBox(-5.0F, 1.0F, -4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(112, 59).mirror().addBox(-3.0F, 1.0F, -4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(134, 142).mirror().addBox(-7.0F, 1.0F, -4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(114, 73).addBox(-7.0F, 3.0F, -4.0F, 6.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(92, 94).addBox(-6.0F, 3.0F, -3.0F, 3.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(68, 10).mirror().addBox(-7.0F, 2.0F, -4.0F, 4.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(98, 137).mirror().addBox(-6.0F, 0.0F, -4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(94, 131).mirror().addBox(-7.0F, -1.0F, 0.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(18, 65).mirror().addBox(-2.0F, 0.0F, -4.0F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(12, 103).mirror().addBox(5.0F, 0.0F, -4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(110, 70).mirror().addBox(-3.0F, 0.0F, -4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(142, 139).mirror().addBox(-6.0F, 1.0F, -4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(1.0F, -1.0F, 1.0F));

        PartDefinition lowerbody = torso.addOrReplaceChild("lowerbody", CubeListBuilder.create().texOffs(92, 100).addBox(-1.0F, -16.6205F, -10.7531F, 4.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(36, 105).addBox(-2.0F, -26.6205F, -16.7531F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(106, 7).addBox(-2.0F, -23.6205F, -17.7531F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(36, 105).mirror().addBox(-1.0F, -26.6205F, -16.7531F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(106, 43).addBox(-2.0F, -20.6205F, -16.7531F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(106, 7).mirror().addBox(-1.0F, -23.6205F, -17.7531F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(78, 89).addBox(-2.0F, -18.6205F, -14.7531F, 3.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(106, 43).mirror().addBox(-1.0F, -20.6205F, -16.7531F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(78, 89).mirror().addBox(-1.0F, -18.6205F, -14.7531F, 3.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(92, 100).mirror().addBox(-3.0F, -16.6205F, -10.7531F, 4.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(116, 100).addBox(-2.0F, -13.6205F, -8.7531F, 3.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(124, 49).addBox(-1.0F, -11.6205F, -7.7531F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(116, 100).mirror().addBox(-1.0F, -13.6205F, -8.7531F, 3.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 7.7193F, 19.137F));

        PartDefinition cube_r39 = lowerbody.addOrReplaceChild("cube_r39", CubeListBuilder.create().texOffs(78, 24).addBox(5.7584F, 1.2093F, -3.9424F, 1.0F, 5.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -13.6205F, -11.7531F, 0.0F, 0.0F, 0.3927F));

        PartDefinition cube_r40 = lowerbody.addOrReplaceChild("cube_r40", CubeListBuilder.create().texOffs(88, 6).addBox(1.2535F, -5.0749F, 2.661F, 8.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(78, 110).mirror().addBox(1.7584F, -0.7907F, -3.9424F, 5.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(108, 91).addBox(-0.2416F, 2.2093F, 3.0576F, 7.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(132, 48).mirror().addBox(-0.4066F, 3.9706F, 3.1898F, 3.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(16, 97).addBox(9.2535F, -4.0749F, -1.339F, 1.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(86, 0).addBox(-0.0052F, -2.3135F, 3.2158F, 8.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 75).addBox(1.5934F, 2.9706F, -2.8102F, 1.0F, 6.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -14.6205F, -11.7531F, 0.0F, 0.0F, 0.3927F));

        PartDefinition cube_r41 = lowerbody.addOrReplaceChild("cube_r41", CubeListBuilder.create().texOffs(78, 24).mirror().addBox(-6.7584F, 1.2093F, -3.9424F, 1.0F, 5.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.0F, -13.6205F, -11.7531F, 0.0F, 0.0F, -0.3927F));

        PartDefinition cube_r42 = lowerbody.addOrReplaceChild("cube_r42", CubeListBuilder.create().texOffs(88, 6).mirror().addBox(-9.2535F, -5.0749F, 2.661F, 8.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(76, 109).addBox(-6.7584F, -0.7907F, -3.9424F, 5.0F, 8.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(108, 91).mirror().addBox(-6.7584F, 2.2093F, 3.0576F, 7.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(132, 48).addBox(-2.5934F, 3.9706F, 3.1898F, 3.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(16, 97).mirror().addBox(-10.2535F, -4.0749F, -1.339F, 1.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(86, 0).mirror().addBox(-7.9948F, -2.3135F, 3.2158F, 8.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 75).mirror().addBox(-2.5934F, 2.9706F, -2.8102F, 1.0F, 6.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.0F, -14.6205F, -11.7531F, 0.0F, 0.0F, -0.3927F));

        PartDefinition boat = partdefinition.addOrReplaceChild("boat", CubeListBuilder.create().texOffs(160, 0).mirror().addBox(-8.0F, 0.0F, -77.4F, 16.0F, 8.0F, 16.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(160, 0).mirror().addBox(-8.0F, 0.0F, -61.4F, 16.0F, 8.0F, 16.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(160, 0).mirror().addBox(-8.0F, 0.0F, -93.4F, 16.0F, 8.0F, 16.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(160, 0).mirror().addBox(-8.0F, 0.0F, -45.4F, 16.0F, 8.0F, 16.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(160, 0).mirror().addBox(-8.0F, 0.0F, -29.4F, 16.0F, 8.0F, 16.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(160, 0).mirror().addBox(-8.0F, 0.0F, -13.4F, 16.0F, 8.0F, 16.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(160, 0).mirror().addBox(-8.0F, -8.0F, -13.4F, 16.0F, 8.0F, 16.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(160, 0).addBox(-16.0F, 0.0F, -77.4F, 8.0F, 8.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(160, 0).addBox(-16.0F, -8.0F, -77.4F, 8.0F, 8.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(160, 0).addBox(-24.0F, -8.0F, -77.4F, 8.0F, 8.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(160, 0).addBox(32.0F, -8.0F, -45.4F, 8.0F, 8.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(160, 0).addBox(24.0F, -8.0F, -45.4F, 8.0F, 8.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(160, 0).addBox(-24.0F, -8.0F, -93.4F, 8.0F, 8.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(160, 0).addBox(-16.0F, -8.0F, -93.4F, 8.0F, 8.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(160, 0).addBox(-16.0F, 0.0F, -93.4F, 8.0F, 8.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(160, 0).addBox(16.0F, -8.0F, -77.4F, 8.0F, 8.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(160, 0).addBox(8.0F, -8.0F, -77.4F, 8.0F, 8.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(160, 0).addBox(8.0F, 0.0F, -77.4F, 8.0F, 8.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(160, 0).addBox(-24.0F, -8.0F, -61.4F, 16.0F, 8.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(160, 0).addBox(-32.0F, 0.0F, -45.4F, 8.0F, 8.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(160, 0).addBox(-32.0F, -8.0F, -45.4F, 8.0F, 8.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(160, 0).addBox(-40.0F, -8.0F, -45.4F, 8.0F, 8.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(160, 0).addBox(-32.0F, -16.0F, -13.4F, 8.0F, 8.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(160, 0).addBox(-48.0F, -24.0F, -13.4F, 8.0F, 8.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(160, 0).addBox(-24.0F, 0.0F, -45.4F, 16.0F, 8.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(160, 8).addBox(0.0F, 0.0F, -101.4F, 16.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(160, 0).addBox(0.0F, -8.0F, -109.4F, 8.0F, 8.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(160, 0).addBox(8.0F, -8.0F, -109.4F, 16.0F, 8.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(160, 0).addBox(8.0F, 0.0F, -45.4F, 16.0F, 8.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(160, 0).addBox(-24.0F, -8.0F, -29.4F, 16.0F, 8.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(160, 0).addBox(8.0F, -8.0F, -61.4F, 16.0F, 8.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(160, 0).addBox(-16.0F, -8.0F, -13.4F, 8.0F, 8.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(160, 0).addBox(-16.0F, 0.0F, -13.4F, 8.0F, 8.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(160, 0).addBox(16.0F, -8.0F, -93.4F, 8.0F, 8.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(160, 0).addBox(8.0F, -8.0F, -93.4F, 8.0F, 8.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(160, 0).addBox(8.0F, 0.0F, -93.4F, 8.0F, 8.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(160, 0).addBox(8.0F, -8.0F, -29.4F, 16.0F, 8.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(160, 0).addBox(16.0F, -8.0F, -13.4F, 8.0F, 8.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(160, 0).addBox(8.0F, -8.0F, -13.4F, 8.0F, 8.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(160, 0).addBox(8.0F, 0.0F, -13.4F, 8.0F, 8.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(160, 0).addBox(-24.0F, -8.0F, -109.4F, 16.0F, 8.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(160, 0).addBox(-8.0F, -8.0F, -125.4F, 8.0F, 8.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(160, 8).addBox(-16.0F, 0.0F, -101.4F, 16.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(160, 0).addBox(32.0F, -24.0F, -13.4F, 8.0F, 8.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(160, 0).addBox(24.0F, -24.0F, -13.4F, 8.0F, 8.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(160, 0).addBox(24.0F, -16.0F, -13.4F, 8.0F, 8.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(160, 0).addBox(0.0F, -8.0F, -125.4F, 8.0F, 8.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(160, 0).addBox(-8.0F, -8.0F, -109.4F, 8.0F, 8.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(160, 0).addBox(8.0F, -16.0F, -13.4F, 16.0F, 8.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(160, 0).addBox(24.0F, 0.0F, -45.4F, 8.0F, 8.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(160, 0).mirror().addBox(-24.0F, -8.0F, -13.4F, 8.0F, 8.0F, 16.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(160, 0).mirror().addBox(-32.0F, -8.0F, -13.4F, 8.0F, 8.0F, 16.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(160, 0).mirror().addBox(-40.0F, -16.0F, -13.4F, 8.0F, 8.0F, 16.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(160, 0).mirror().addBox(-24.0F, -16.0F, -13.4F, 16.0F, 8.0F, 16.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(160, 0).mirror().addBox(-48.0F, -24.0F, -13.4F, 16.0F, 8.0F, 16.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 16.0F, 51.0F));

        PartDefinition fishspear = boat.addOrReplaceChild("fishspear", CubeListBuilder.create().texOffs(172, 136).addBox(-5.6407F, 25.6857F, 3.7033F, 3.0F, 11.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(174, 0).addBox(-4.6407F, 84.6857F, 4.7033F, 2.0F, 59.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(174, 0).addBox(-4.6407F, 36.6857F, 4.7033F, 2.0F, 72.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(174, 138).addBox(-8.6407F, 26.6857F, 4.7033F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(174, 138).addBox(-10.6407F, 28.6857F, 4.7033F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(174, 138).addBox(-12.6407F, 30.6857F, 4.7033F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(174, 138).addBox(-14.6407F, 28.6857F, 4.7033F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(174, 138).addBox(-16.6407F, 26.6857F, 4.7033F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(174, 138).addBox(-15.6407F, 24.6857F, 4.7033F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(175, 139).addBox(-13.6407F, 22.6857F, 5.7033F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(174, 138).addBox(-12.6407F, 20.6857F, 4.7033F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(175, 139).addBox(-10.6407F, 18.6857F, 5.7033F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(175, 139).addBox(-9.6407F, 16.6857F, 5.7033F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(177, 139).addBox(-7.6407F, 9.6857F, 5.7033F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(171, 136).addBox(-2.6407F, 23.6857F, 3.7033F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(174, 138).addBox(0.3593F, 24.6857F, 4.7033F, 3.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(173, 138).addBox(1.3593F, 27.6857F, 4.7033F, 4.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(173, 138).addBox(3.3593F, 27.6857F, 4.7033F, 4.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(174, 138).addBox(3.3593F, 24.6857F, 4.7033F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(173, 138).addBox(0.3593F, 21.6857F, 4.7033F, 5.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(174, 138).addBox(-0.6407F, 19.6857F, 4.7033F, 4.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(175, 139).addBox(-2.6407F, 17.6857F, 5.7033F, 5.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(174, 138).addBox(-4.6407F, 8.6857F, 4.7033F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(175, 139).addBox(-3.6407F, 5.6857F, 4.7033F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(176, 139).addBox(-2.6407F, 1.6857F, 4.7033F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(173, 138).addBox(4.3593F, 28.6857F, 4.7033F, 4.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(172, 136).addBox(-5.6407F, 99.6857F, 3.7033F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(172, 136).addBox(-5.6407F, 92.6857F, 3.7033F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(172, 136).addBox(-5.6407F, 85.6857F, 3.7033F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(172, 136).addBox(-5.6407F, 79.6857F, 3.7033F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -10.0F, -92.0F, 1.2435F, 0.3585F, 2.9332F));

        return LayerDefinition.create(meshdefinition, 256, 256);
    }

    @Override
    public void setupAnim(SunkenSailorEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        // Setzt alle Teile auf ihre Basis-Pose zurück, bevor Animationen angewendet werden
        this.root().getAllParts().forEach(ModelPart::resetPose);

        // Wende die Kopfrotation basierend auf der Blickrichtung der Entität an
        this.applyHeadRotation(netHeadYaw, headPitch);

        // Wende die Animationen aus den Definitionen an
        // Nutze die AnimationStates von der Entität
        // Parameter: AnimationState, AnimationDefinition, ageInTicks, speedMultiplier (1.0f = normal)
        this.animate(entity.idleAnimationState, SunkenSailorAnimations.idle, ageInTicks, 1.0f);
        this.animate(entity.walkAnimationState, SunkenSailorAnimations.realwalkanimation, ageInTicks, 1.0f);
        // Prüfe den Namen der Attack-Animation in deiner Definitions-Klasse! Ist es "realrangedattackanimation" oder "screambeamattack"?
        // Ich nehme hier "realrangedattackanimation" an, passe es ggf. an.
        this.animate(entity.attackAnimationState, SunkenSailorAnimations.realrangedattackanimation, ageInTicks, 1.0f);
        this.animate(entity.hurtAnimationState, SunkenSailorAnimations.hurtanimation, ageInTicks, 1.0f);
        // Füge hier ggf. weitere Animationen hinzu (z.B. Tod)
    }

    private void applyHeadRotation(float pNetHeadYaw, float pHeadPitch) {
        // Begrenze die Werte, um unnatürliche Drehungen zu vermeiden
        pNetHeadYaw = Mth.clamp(pNetHeadYaw, -75.0F, 75.0F);
        pHeadPitch = Mth.clamp(pHeadPitch, -45.0F, 45.0F); // Beispiel-Grenzen

        // Drehe den Kopf-Teil
        if (this.head != null) {
            this.head.yRot = pNetHeadYaw * Mth.DEG_TO_RAD; // Grad in Radiant umrechnen
            this.head.xRot = pHeadPitch * Mth.DEG_TO_RAD;
        }
    }

    @Override
    public ModelPart root() {
        // Gibt den Wurzel-Teil des Modells zurück
        return this.root;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        // Standard-Rendering für HierarchicalModel
        this.root().render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
