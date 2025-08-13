package states;

import com.jme3.app.Application;
import com.jme3.bullet.collision.PhysicsCollisionEvent;

import java.util.function.Consumer;

public class CollisionAppState extends AbstractEmptyAppState {

    private Consumer<PhysicsCollisionEvent> listener;

    public void setListener(Consumer<PhysicsCollisionEvent> listener) {
        this.listener = listener;
    }

    @Override
    protected void initialize(Application application) {
        getState(PhysicsAppState.class)
                .getPhysicsSpace()
                .addCollisionListener(event -> {
                    if (listener != null) {
                        listener.accept(event);
                    }
                });
    }

}
