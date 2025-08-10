package states;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.bullet.BulletAppState;

public class PhysicsAppState extends BaseAppState {
    private BulletAppState bulletAppState;
    @Override
    protected void initialize(Application application) {
        bulletAppState = new BulletAppState();
        getStateManager().attach(bulletAppState);
    }

    public BulletAppState getBulletAppState() {
        return this.bulletAppState;
    }

    @Override
    protected void cleanup(Application application) {

    }

    @Override
    protected void onEnable() {

    }

    @Override
    protected void onDisable() {

    }
}
