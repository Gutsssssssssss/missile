package core.map;

import com.jme3.app.Application;
import com.jme3.asset.AssetManager;
import com.jme3.asset.ModelKey;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.scene.Node;
import core.util.WrappedBaseAppState;
import core.util.PhysicsAppState;

public class LauncherAppState extends WrappedBaseAppState {

    private final Node rootNode;
    private final AssetManager assetManager;
    private Node launcherNode;

    public LauncherAppState(Node rootNode, AssetManager assetManager) {
        this.rootNode = rootNode;
        this.assetManager = assetManager;
    }

    @Override
    protected void initialize(Application application) {
        PhysicsAppState physicsAppState = getState(PhysicsAppState.class);

        this.launcherNode = (Node) assetManager.loadModel(new ModelKey("Objects/launcher/MissileLauncher.glb"));
        this.launcherNode.setName("Launcher");
        this.launcherNode.setLocalTranslation(0, -100, 300);
        this.launcherNode.setLocalScale(6f);

        RigidBodyControl radarRigidBody = new RigidBodyControl(0f);
        this.launcherNode.addControl(radarRigidBody);

        rootNode.attachChild(this.launcherNode);
        physicsAppState.addToPhysicsSpace(this.launcherNode);
    }
}
