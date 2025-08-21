package core.util;

import com.jme3.math.Vector3f;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TrajectoryCalculatorTest {

    @Test
    void testSimpleBallisticVelocity() {
        Vector3f start = new Vector3f(0, 10, 0);
        Vector3f target = new Vector3f(100, 0, 0);
        float ballisticAngle = (float) Math.toRadians(45f);
        float actualVelocity = TrajectoryCalculator.calculateBallisticVelocity(start, target, ballisticAngle);

        float expectedVelocity = 29.863f;

        assertEquals(expectedVelocity, actualVelocity, 0.001f);
    }

    @Test
    void testInvalidBallisticAngle() {
        Vector3f start = new Vector3f(0, 10, 0);
        Vector3f target = new Vector3f(100, 0, 0);
        float ballisticAngle1 = (float) Math.toRadians(90f);
        float ballisticAngle2 = (float) Math.toRadians(0f);

        assertThrows(IllegalArgumentException.class,
                () -> TrajectoryCalculator.calculateBallisticVelocity(start, target, ballisticAngle1));
        assertThrows(IllegalArgumentException.class,
                () -> TrajectoryCalculator.calculateBallisticVelocity(start, target, ballisticAngle2));
    }

    @Test
    @DisplayName("Target and missile are moving in the same velocity.")
    void testPNSameVelocity() {
        Vector3f startPos = new Vector3f(0, 0, 0);
        Vector3f startVel = new Vector3f(100, 0, 0);
        Vector3f endPos = new Vector3f(1000, 0, 0);
        Vector3f endVel = new Vector3f(100, 0, 0);

        Vector3f a = TrajectoryCalculator.calculatePNAcceleration(startPos, startVel, endPos, endVel);

        assertEquals(0.0, a.length(), 0.01f);
    }

    @Test
    @DisplayName("Target is stationary and the missile is heading straight toward it.")
    void testPNTargetStationary() {
        Vector3f startPos = new Vector3f(0, 0, 0);
        Vector3f startVel = new Vector3f(100, 0, 0);
        Vector3f endPos = new Vector3f(1000, 0, 0);
        Vector3f endVel = new Vector3f(0, 0, 0);

        Vector3f a = TrajectoryCalculator.calculatePNAcceleration(startPos, startVel, endPos, endVel);

        assertEquals(0.0, a.length(), 0.01f);
    }

    @Test
    @DisplayName("Target is moving at a constant velocity perpendicular to the missile.")
    void testPNTargetMovingPerpendicular() {
        Vector3f startPos = new Vector3f(0, 0, 0);
        Vector3f startVel = new Vector3f(100, 0, 0);
        Vector3f endPos = new Vector3f(1000, 0, 0);
        Vector3f endVel = new Vector3f(0, 100, 0);

        Vector3f a = TrajectoryCalculator.calculatePNAcceleration(startPos, startVel, endPos, endVel);
        System.out.println(a);
        assertTrue(Math.abs(a.y) > 0.0f);
    }
}