package vallesminecraftmods.belly_of_the_beast.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel; // Geändert!
import net.minecraft.client.model.geom.ModelLayerLocation; // Importieren
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth; // Für Mth.clamp etc.
import vallesminecraftmods.belly_of_the_beast.BellyOfTheBeast;
import vallesminecraftmods.belly_of_the_beast.client.animation.SunkenSailorAnimations; // Importiere deine Animationsklasse
import Entity.custom.SunkenSailorEntity;

public class SunkenSailorModel extends HierarchicalModel<SunkenSailorEntity> { // Geändert!

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            new ResourceLocation(BellyOfTheBeast.MODID, "sunken_sailor"), "main");

    private final ModelPart root;
    // Füge hier Felder für die wichtigen Teile hinzu, die du direkt ansteuern willst (z.B. Kopf)
    private final ModelPart head;
    // ... andere Teile nach Bedarf ...

    public SunkenSailorModel(ModelPart root) {
        this.root = root;
        // Weise die Teile anhand der Namen aus createBodyLayer zu
        this.head = root.getChild("head"); // Name muss mit createBodyLayer übereinstimmen!
        // ... andere Teile zuweisen ...
    }

    // !!! WICHTIG: DIESE METHODE MUSS DEINE MODELLSTRUKTUR ENTHALTEN !!!
    // !!! Exportiere sie aus Blockbench (Modded Entity -> Forge -> Layer Definition) !!!
    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        // Beispiel-Struktur (ersetze dies durch deine echte Struktur!)
        PartDefinition theguyhimself = partdefinition.addOrReplaceChild("theguyhimself", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
        // ... alle anderen Teile ("boat", "upperbody", "lowerbody", "neck", "head", "jaw", "arms", "legs" etc.) ...
        // Stelle sicher, dass die Namen ("head", "armleft", "jaw", etc.) mit denen in den AnimationDefinitions übereinstimmen!
        PartDefinition upperbody = theguyhimself.addOrReplaceChild("upperbody", CubeListBuilder.create(), PartPose.ZERO); // Beispiel
        PartDefinition head = upperbody.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F), PartPose.offset(0.0F, -10.0F, 0.0F)); // Beispiel-Kopf
        head.addOrReplaceChild("jaw", CubeListBuilder.create(), PartPose.ZERO); // Beispiel-Kiefer
        // ... FÜGE HIER DEINE KOMPLETTE MODELLSTRUKTUR EIN ...


        return LayerDefinition.create(meshdefinition, 128, 128); // Texturbreite/-höhe anpassen!
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
