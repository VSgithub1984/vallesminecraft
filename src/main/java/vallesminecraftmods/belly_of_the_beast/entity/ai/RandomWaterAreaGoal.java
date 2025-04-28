package vallesminecraftmods.belly_of_the_beast.entity.ai; // Oder dein korrektes AI-Paket (ggf. .ai anhängen)

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.RandomPos;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import vallesminecraftmods.belly_of_the_beast.entity.SunkenSailorEntity; // Importiere deine Entity

import java.util.EnumSet;

// Neuer Goal für zufälliges Schwimmen in einem Bereich nahe der Oberfläche
public class RandomWaterAreaGoal extends Goal {
    private SunkenSailorEntity mob = null;
    private final double speedModifier;
    private final int maxDist; // Max X/Z-Abstand vom spawnPos
    private double wantedX;
    private double wantedY;
    private double wantedZ;
    // Reduziere die Startverzögerung und das Intervall, damit es häufiger versucht
    private int interval = 60 + mob.getRandom().nextInt(60); // 3-6 Sekunden

    // Flag um Debug-Ausgaben zu steuern (kann auf false gesetzt werden, wenn alles läuft)
    private static final boolean DEBUG_GOAL = true;

    public RandomWaterAreaGoal(SunkenSailorEntity mob, double speedModifier, int maxDist) {
        this.mob = mob;
        this.speedModifier = speedModifier;
        this.maxDist = maxDist; // Dies ist der Max X/Z-Abstand (für 10x10 ist 10 ok)
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        // Nur verwenden, wenn NICHT gekämpft wird, SPAWNPOS existiert und
        // die Navigation fertig ist ODER eine gewisse Zeit abgelaufen ist.
        if (this.mob.getTarget() != null || this.mob.getSpawnPos() == null ||this.mob.isCurrentlyRangedAttacking()) {
            return false;
        }

        // Versuche nur, wenn nicht schon unterwegs oder nach Ablauf des Intervalls
        if (!this.mob.getNavigation().isDone() && this.mob.isMoving()) {
            // Wenn schon unterwegs zu einem Idle-Ziel, nicht sofort neu suchen
            return false;
        }

        if (--this.interval <= 0) {
            this.interval = 80 + this.mob.getRandom().nextInt(80); // Intervall zurücksetzen (4-8 Sek)
            Vec3 targetPos = this.findRandomWaterPosition();
            if (targetPos != null) {
                this.wantedX = targetPos.x;
                this.wantedY = targetPos.y;
                this.wantedZ = targetPos.z;
                if (DEBUG_GOAL) System.out.println("Idle Goal: Found new water target: " + targetPos);
                return true; // Gültiges Ziel gefunden
            }
            // Wenn kein Ziel gefunden wurde, warte länger, bevor erneut gesucht wird
            this.interval = 120 + this.mob.getRandom().nextInt(100);
        }

        return false; // Kein neues Ziel benötigt oder gefunden
    }

    @Override
    public boolean canContinueToUse() {
        // Weiterlaufen, solange Navigation aktiv ist UND kein Ziel/Angriff da ist
        return !this.mob.getNavigation().isDone() && this.mob.getTarget() == null && !this.mob.isCurrentlyRangedAttacking();
    }

    @Override
    public void start() {
        // Bewege zum Ziel
        this.mob.getNavigation().moveTo(this.wantedX, this.wantedY, this.wantedZ, this.speedModifier);
        this.mob.setMoving(true); // Animation starten
        if (DEBUG_GOAL) System.out.println("Idle Goal: Starting navigation to " + new BlockPos((int)wantedX, (int)wantedY, (int)wantedZ));
    }

    @Override
    public void stop() {
        // Navigation stoppen und Animation beenden
        this.mob.getNavigation().stop();
        this.mob.setMoving(false);
        if (DEBUG_GOAL) System.out.println("Idle Goal: Stopping navigation.");
        // Kurzes Intervall, falls sofort wieder canUse geprüft wird
        this.interval = 20;
    }

    @Override
    public void tick() {
        // Sicherstellen, dass der Moving-Status korrekt ist, falls die Navigation abbricht
        boolean isNavigating = !this.mob.getNavigation().isDone() && this.mob.getNavigation().isInProgress();
        this.mob.setMoving(isNavigating);
    }


    private Vec3 findRandomWaterPosition() {
        if (this.mob.getSpawnPos() == null) return null;

        for (int i = 0; i < 15; ++i) { // Mehr Versuche
            // Zufällige X/Z-Position innerhalb des maxDist-Bereichs um spawnPos
            double targetX = this.mob.getSpawnPos().getX() + (this.mob.getRandom().nextDouble() * 2.0D - 1.0D) * this.maxDist;
            double targetZ = this.mob.getSpawnPos().getZ() + (this.mob.getRandom().nextDouble() * 2.0D - 1.0D) * this.maxDist;

            // Finde Wasseroberfläche an dieser Position
            // Verwende die Methode aus der Entity-Klasse (muss public oder protected sein, oder wir brauchen eine Referenz)
            // DA WIR HIER SIND, nehmen wir an, getWaterSurfaceY ist in SunkenSailorEntity public oder wir kopieren sie hierher.
            // Fürs Erste nehmen wir an, sie ist zugänglich.
            double targetY = this.mob.getWaterSurfaceY(targetX, targetZ); // getWaterSurfaceY muss public sein!

            // Prüfe, ob eine gültige Wasseroberfläche gefunden wurde
            if (targetY > this.mob.level().getMinBuildHeight()) {
                Vec3 potentialTarget = new Vec3(targetX, targetY - 0.5, targetZ); // Leicht unterhalb der Oberfläche
                BlockPos checkPos = BlockPos.containing(potentialTarget);

                // Prüfe, ob der Block Wasser ist UND für Wasser-Pathfinding geeignet ist
                if (this.mob.level().getFluidState(checkPos).is(FluidTags.WATER) &&
                        this.mob.level().getBlockState(checkPos).isPathfindable(this.mob.level(), checkPos, PathComputationType.WATER))
                {
                    // Zusätzlicher Check: Ist die Position zu nah an der aktuellen?
                    if (this.mob.position().distanceToSqr(potentialTarget) > 3.0 * 3.0) { // Nur wenn weiter als 3 Blöcke weg
                        return potentialTarget;
                    }
                }
            }
        }
        if (DEBUG_GOAL) System.out.println("Idle Goal: Failed to find random water position after 15 tries.");
        return null; // Kein geeignetes Ziel gefunden
    }
}