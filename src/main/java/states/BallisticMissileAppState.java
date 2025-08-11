package states;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.asset.AssetManager;
import com.jme3.asset.ModelKey;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

public class BallisticMissileAppState extends BaseAppState {
    @Override
    protected void initialize(Application application) {
        SimpleApplication simulator = (SimpleApplication) application;
        Node rootNode = simulator.getRootNode();
        AssetManager assetManager = simulator.getAssetManager();
        PhysicsAppState physics = getState(PhysicsAppState.class);

        CityAppState cityAppState = simulator.getStateManager().getState(CityAppState.class);
        Node city = cityAppState.getCity();


        Node ballisticMissile = (Node) assetManager.loadModel(new ModelKey("Objects/missile/ballisticMissile.glb"));
        ballisticMissile.rotate(0, 0, (float)Math.toRadians(-90));
        ballisticMissile.setLocalTranslation(new Vector3f(17000, 29f, 0));
        RigidBodyControl ballisticMissileControl = new RigidBodyControl(0f);
        ballisticMissile.addControl(ballisticMissileControl);
//        ballisticMissileControl.setLinearVelocity(new Vector3f(0,-3000, 0));
        rootNode.attachChild(ballisticMissile);
        physics.getBulletAppState().getPhysicsSpace().add(ballisticMissile);

        // launcher
        Geometry floor = new Geometry("Floor", new Box(40f, 0.1f, 20f));
        Material matFloor = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        matFloor.setTexture("ColorMap", assetManager.loadTexture("Textures/Terrain/BrickWall/BrickWall.jpg"));
        floor.setMaterial(matFloor);
        floor.setLocalTranslation(new Vector3f(17000, 0, 0));
        RigidBodyControl floorControl = new RigidBodyControl(0);
        floor.addControl(floorControl);
        rootNode.attachChild(floor);
        physics.getBulletAppState().getPhysicsSpace().add(floorControl);
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
