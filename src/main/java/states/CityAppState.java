package states;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.asset.AssetManager;
import com.jme3.asset.ModelKey;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.scene.Node;

public class CityAppState extends BaseAppState {
    @Override
    protected void initialize(Application application) {
        SimpleApplication simulator = (SimpleApplication) application;
        Node rootNode = simulator.getRootNode();
        AssetManager assetManager = simulator.getAssetManager();

        Node city = (Node) assetManager.loadModel(new ModelKey("Scenes/city/city.glb"));
        city.setLocalScale(0.03f);

        CollisionShape cityShape = CollisionShapeFactory.createMeshShape(city);
        RigidBodyControl cityRigidBody = new RigidBodyControl(cityShape, 0);
        city.addControl(cityRigidBody);

        rootNode.attachChild(city);
        PhysicsAppState physics = getState(PhysicsAppState.class);
        physics.getBulletAppState().getPhysicsSpace().add(city);
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
