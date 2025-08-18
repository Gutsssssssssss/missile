package states;

import com.jme3.app.Application;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

public class FloorAppState extends AbstractEmptyAppState {

    private final Node rootNode;
    private final AssetManager assetManager;

    public FloorAppState(Node rootNode, AssetManager assetManager) {
        this.rootNode = rootNode;
        this.assetManager = assetManager;
    }

    @Override
    protected void initialize(Application application) {
        PhysicsAppState physicsAppState = getState(PhysicsAppState.class);

        Geometry floor = new Geometry("Floor", new Box(40f, 0.1f, 20f));
        Material matFloor = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        matFloor.setTexture("ColorMap", assetManager.loadTexture("Textures/Terrain/BrickWall/BrickWall.jpg"));
        floor.setMaterial(matFloor);
        floor.setLocalTranslation(new Vector3f(-17000, 0, 0));
        RigidBodyControl floorControl = new RigidBodyControl(0);
        floor.addControl(floorControl);

        rootNode.attachChild(floor);
        physicsAppState.addToPhysicsSpace(floor);
    }
}
