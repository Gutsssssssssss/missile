package states;

import com.jme3.app.Application;
import com.jme3.asset.AssetManager;
import com.jme3.asset.ModelKey;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

public class CityAppState extends AbstractEmptyAppState {

    private final Node rootNode;
    private final AssetManager assetManager;
    private Node cityNode;

    public CityAppState(Node rootNode, AssetManager assetManager) {
        this.rootNode = rootNode;
        this.assetManager = assetManager;
    }

    @Override
    protected void initialize(Application application) {
        PhysicsAppState physics = getState(PhysicsAppState.class);

        this.cityNode = (Node) assetManager.loadModel(new ModelKey("Scenes/city/city.glb"));
        this.cityNode.setName("City");
        this.cityNode.setLocalScale(0.03f);
        this.cityNode.setLocalTranslation(new Vector3f(0, -100, 0));

        RigidBodyControl cityRigidBody = new RigidBodyControl(0);
        this.cityNode.addControl(cityRigidBody);

        rootNode.attachChild(this.cityNode);
        physics.addToPhysicsSpace(this.cityNode);
    }

    public Vector3f getWorldTranslation() {
        return this.cityNode.getWorldTranslation();
    }
}
