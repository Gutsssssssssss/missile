package core.util;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;

/**
 * A wrapper around {@link com.jme3.app.state.BaseAppState}
 * that provides empty implementations for lifecycle methods.
 *
 * This reduces boilerplate when creating new app states,
 * allowing subclasses to override only the methods they need.
 */
public class WrappedBaseAppState extends BaseAppState {

    @Override
    protected void initialize(Application application) {
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

    @Override
    public void update(float tpf) {
    }
}
