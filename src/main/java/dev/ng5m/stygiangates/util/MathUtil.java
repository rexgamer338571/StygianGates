package dev.ng5m.stygiangates.util;

import org.bukkit.util.Vector;
import org.joml.Vector3d;

import static java.lang.Math.*;

public class MathUtil {

    public static Tuple<Float, Float> getAngles(Vector pos, Vector target) {
        Vector3d d = new Vector3d(target.getX() - pos.getX(), target.getY() - pos.getY(), target.getZ() - pos.getZ());

        float yaw = (float) (toDegrees(atan2(d.z, d.x)) - 90.0);
        float pitch = (float) -toDegrees(atan2(d.y, sqrt(d.x * d.x + d.z * d.z)));

        return new Tuple<>(yaw, pitch);
    }

    public record Tuple<V1, V2>(V1 v1, V2 v2) {}

}
