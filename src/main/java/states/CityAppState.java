package states;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.asset.ModelKey;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

public class CityAppState extends AbstractEmptyAppState {

    private Node city;

    @Override
    protected void initialize(Application application) {
        SimpleApplication simulator = (SimpleApplication) application;
        Node rootNode = simulator.getRootNode();
        AssetManager assetManager = simulator.getAssetManager();

        this.city = (Node) assetManager.loadModel(new ModelKey("Scenes/city/city.glb"));
        this.city.setName("City");
        this.city.setLocalScale(0.03f);
        this.city.setLocalTranslation(new Vector3f(0, -100, 0));

        CollisionShape cityShape = CollisionShapeFactory.createMeshShape(this.city);
        RigidBodyControl cityRigidBody = new RigidBodyControl(cityShape, 0);
        this.city.addControl(cityRigidBody);

        rootNode.attachChild(this.city);
        PhysicsAppState physics = getState(PhysicsAppState.class);
        physics.addToPhysicsSpace(this.city);
    }

    public Vector3f getWorldTranslation() {
        return this.city.getWorldTranslation();
    }
}
