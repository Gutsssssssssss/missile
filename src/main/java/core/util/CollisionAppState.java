package core.util;

import com.jme3.app.Application;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.math.Vector3f;
import core.ObjectType;
import core.missile.BallisticMissileAppState;
import core.missile.EffectAppState;
import core.missile.PurePursuitGuidedMissileAppState;

import java.util.function.Consumer;

/**
 * Manages collision detection by registering a listener for physics events.
 * **/
public class CollisionAppState extends AbstractEmptyAppState {

    private Consumer<PhysicsCollisionEvent> listener;

    @Override
    protected void initialize(Application application) {
        BallisticMissileAppState ballisticMissileAppState = getState(BallisticMissileAppState.class);
        EffectAppState effectAppState = getState(EffectAppState.class);
        getState(PhysicsAppState.class)
                .getPhysicsSpace()
                .addCollisionListener(event -> {
                    if (listener != null) {
                        listener.accept(event);
                    }
                });

        listener = event -> {
            String a = event.getNodeA().getName();
            String b = event.getNodeB().getName();

            if (isMissileHitCity(a, b)) {
                Vector3f contactPoint = event.getPositionWorldOnB();
                effectAppState.spawnExplosionEffects(contactPoint);
                ballisticMissileAppState.removeMissile();
            } else if (isMissileHitMissile(a, b)) {
                Vector3f contactPoint = event.getPositionWorldOnB();
                effectAppState.spawnExplosionEffects(contactPoint);
                ballisticMissileAppState.removeMissile();
                PurePursuitGuidedMissileAppState guidedMissileAppState = getState(PurePursuitGuidedMissileAppState.class);
                guidedMissileAppState.removeMissile();
            }
        };
    }

    private boolean isMissileHitCity(String a, String b) {
        return ((a.equals(ObjectType.BALLISTIC_MISSILE) && b.equals(ObjectType.CITY))
                || (a.equals(ObjectType.CITY) && b.equals(ObjectType.BALLISTIC_MISSILE)));
    }

    private boolean isMissileHitMissile(String a, String b) {
        return ((a.equals(ObjectType.BALLISTIC_MISSILE) && b.equals(ObjectType.GUIDED_MISSILE))
                || (a.equals(ObjectType.GUIDED_MISSILE) && b.equals(ObjectType.BALLISTIC_MISSILE)));
    }
}
