package core.util;

import com.jme3.math.Vector3f;

public class TrajectoryCalculator {
    private static final float GRAVITY = 9.81f;

    public static float calculateBallisticVelocity(Vector3f start, Vector3f target, float ballisticAngle) {
        if (ballisticAngle <= Math.toRadians(0f) || ballisticAngle >= Math.toRadians(90f)) {
            throw new IllegalArgumentException("Ballistic angle should be between 1 and 89 degrees");
        }

        Vector3f toTarget = target.subtract(start);
        float distanceXZ = (float) Math.sqrt(toTarget.x * toTarget.x + toTarget.z * toTarget.z);
        float distanceY = toTarget.y;
        float numerator = GRAVITY * distanceXZ * distanceXZ;
        float denominator = 2 * (float)Math.cos(ballisticAngle) * (float)Math.cos(ballisticAngle)
                * (distanceXZ * (float)Math.tan(ballisticAngle) - distanceY);

        if (denominator <= 0) {
            throw new IllegalArgumentException("Cannot launch under given conditions");
        }

        return (float)Math.sqrt(numerator / denominator);
    }

    public static Vector3f convertToVector(Vector3f start, Vector3f target, float velocity, float ballisticAngle) {
        Vector3f toTarget = target.subtract(start);
        float velocityXZ = (float) (velocity * Math.cos(ballisticAngle));
        float velocityY = (float) (velocity * Math.sin(ballisticAngle));

        Vector3f directionXZ = new Vector3f(toTarget.x, 0, toTarget.z).normalizeLocal();
        return directionXZ.mult(velocityXZ).add(0, velocityY, 0);
    }
}
