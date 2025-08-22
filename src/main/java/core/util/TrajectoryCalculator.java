package core.util;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
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
        float denominator = 2 * (float) Math.cos(ballisticAngle) * (float) Math.cos(ballisticAngle)
                * (distanceXZ * (float) Math.tan(ballisticAngle) - distanceY);

        if (denominator <= 0) {
            throw new IllegalArgumentException("Cannot launch under given conditions");
        }

        return (float) Math.sqrt(numerator / denominator);
    }

    public static Vector3f convertToVector(Vector3f start, Vector3f target, float velocity, float ballisticAngle) {
        Vector3f toTarget = target.subtract(start);
        float velocityXZ = (float) (velocity * Math.cos(ballisticAngle));
        float velocityY = (float) (velocity * Math.sin(ballisticAngle));

        Vector3f directionXZ = new Vector3f(toTarget.x, 0, toTarget.z).normalizeLocal();
        return directionXZ.mult(velocityXZ).add(0, velocityY, 0);
    }

    public static Vector3f calculatePNAcceleration(Vector3f startPos, Vector3f startVel, Vector3f endPos, Vector3f endVel) {
        float N = 3f;

        Vector3f distance = endPos.subtract(startPos);
        Vector3f relVel = endVel.subtract(startVel);
        Vector3f angularVel = distance.cross(relVel).divide(distance.dot(distance));

        return distance.normalize().cross(angularVel).mult(-N * relVel.length());
    }

    public static void correctAngle(Vector3f direction, RigidBodyControl ctrl) {
        Quaternion rotation = new Quaternion();
        rotation.lookAt(direction, Vector3f.UNIT_Y);
        Quaternion offset = new Quaternion().fromAngleAxis(FastMath.HALF_PI, Vector3f.UNIT_Y);
        rotation = rotation.mult(offset);
        ctrl.setPhysicsRotation(rotation);
    }
}
