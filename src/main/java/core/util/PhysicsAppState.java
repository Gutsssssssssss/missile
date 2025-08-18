package core.util;

import com.jme3.app.Application;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.scene.Spatial;

/**
 * Manages the physics simulation using jMonkeyEngine's Bullet physics engine.
 * **/
public class PhysicsAppState extends WrappedBaseAppState {

    private BulletAppState bulletAppState;

    @Override
    protected void initialize(Application application) {
        bulletAppState = new BulletAppState();
        getStateManager().attach(bulletAppState);
    }

    public void addToPhysicsSpace(Spatial obj) {
        this.bulletAppState.getPhysicsSpace().add(obj);
    }

    public void removeFromPhysicsSpace(RigidBodyControl rigidBodyControl) {
        this.bulletAppState.getPhysicsSpace().remove(rigidBodyControl);
    }

    public PhysicsSpace getPhysicsSpace() {
        return this.bulletAppState.getPhysicsSpace();
    }

}
