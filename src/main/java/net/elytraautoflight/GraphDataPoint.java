package net.elytraautoflight;
import net.fabricmc.loader.util.sat4j.core.Vec;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

public class GraphDataPoint {
    public Vec3d realPosition;
    public double horizontalDelta;
    public double velocity;
    public boolean pullUp;
    public boolean pullDown;

    public GraphDataPoint(Vec3d realPosition) {
        this.realPosition = realPosition;
    }

    public GraphDataPoint(Vec3d realPosition, Vec3d previousPosition) {
        this.realPosition = realPosition;

        Vec3d delta = new Vec3d(realPosition.x - previousPosition.x, 0, realPosition.z - previousPosition.z);
        this.horizontalDelta = delta.length();

    }
}
