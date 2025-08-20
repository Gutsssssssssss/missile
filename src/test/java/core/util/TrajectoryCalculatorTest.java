package core.util;

import com.jme3.math.Vector3f;
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


}