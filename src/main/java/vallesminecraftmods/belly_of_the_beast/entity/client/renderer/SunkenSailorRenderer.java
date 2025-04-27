package vallesminecraftmods.belly_of_the_beast.entity.client.renderer; // Oder dein korrektes Renderer-Paket

// --- Korrekte Imports ---
import net.minecraft.client.renderer.entity.EntityRendererProvider; // Wichtig für den Kontext
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import vallesminecraftmods.belly_of_the_beast.BellyOfTheBeast;
// Importiere deine Entitäts- und Model-Klasse mit dem korrekten Pfad!
import vallesminecraftmods.belly_of_the_beast.entity.SunkenSailorEntity; // Passe Pfad ggf. an
import vallesminecraftmods.belly_of_the_beast.entity.client.model.SunkenSailorModel;   // Passe Pfad ggf. an


// Verwende die importierten Klassen in den Generics
public class SunkenSailorRenderer extends MobRenderer<SunkenSailorEntity, SunkenSailorModel> {

    // Korrigierter Pfad: textures/entity/... relativ zu assets/belly_of_the_beast/
    private static final ResourceLocation TEXTURE_LOCATION =
            new ResourceLocation(BellyOfTheBeast.MODID, "/textures/piratebosstemp.png");

    // Korrekter Konstruktor mit EntityRendererProvider.Context
    public SunkenSailorRenderer(EntityRendererProvider.Context context) {
        // Korrekter Aufruf des super-Konstruktors:
        // 1. Übergib den context
        // 2. Erstelle eine NEUE INSTANZ DES MODELLS (SunkenSailorModel),
        //    indem du den Layer aus dem context bäckst (bakeLayer).
        // 3. Setze die Schattengröße (z.B. 0.8f)
        super(context,
                new SunkenSailorModel(context.bakeLayer(SunkenSailorModel.LAYER_LOCATION)), // Modell erstellen
                0.8f); // Schattenradius
    }

    @Override
    public ResourceLocation getTextureLocation(SunkenSailorEntity entity) {
        // Gib die (korrigierte) statische Textur-Location zurück
        return TEXTURE_LOCATION;
    }
}