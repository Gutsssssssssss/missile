package states;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.asset.AssetManager;
import com.jme3.asset.ModelKey;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.ChaseCamera;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

public class BallisticMissileAppState extends BaseAppState {

    private Node ballisticMissile;
    @Override
    protected void initialize(Application application) {
        SimpleApplication simulator = (SimpleApplication) application;
        Node rootNode = simulator.getRootNode();
        AssetManager assetManager = simulator.getAssetManager();
        PhysicsAppState physics = getState(PhysicsAppState.class);

        CityAppState cityAppState = simulator.getStateManager().getState(CityAppState.class);
        Node city = cityAppState.getCity();

        ballisticMissile = (Node) assetManager.loadModel(new ModelKey("Objects/missile/ballisticMissile.glb"));
        ballisticMissile.rotate(0, -FastMath.PI, 0);

        ballisticMissile.setLocalTranslation(new Vector3f(-17000, 29f, 0));
        RigidBodyControl ballisticMissileControl = new RigidBodyControl(1f);
        ballisticMissile.addControl(ballisticMissileControl);
        // velocity
        Vector3f start = ballisticMissile.getWorldTranslation();
        Vector3f target = city.getWorldTranslation();
        float thetaDeg = 45f;
        float g = 9.81f;

        Vector3f toTarget = target.subtract(start);
        float dx = (float) Math.sqrt(toTarget.x * toTarget.x + toTarget.z * toTarget.z);
        float thetaRad = (float)Math.toRadians(thetaDeg);
        float v = (float)Math.sqrt(dx * g / Math.sin(2 * thetaRad));

        float vxz = (float)(v * Math.cos(thetaRad));
        float vy  = (float)(v * Math.sin(thetaRad));

        Vector3f dirXZ = new Vector3f(toTarget.x, 0, toTarget.z).normalizeLocal();
        Vector3f initialVelocity = dirXZ.mult(vxz).add(0, vy, 0);
        ballisticMissileControl.setLinearVelocity(initialVelocity);
//        ballisticMissileControl.setLinearVelocity(new Vector3f(1700, 0, 0));
        rootNode.attachChild(ballisticMissile);
        physics.getBulletAppState().getPhysicsSpace().add(ballisticMissile);

        // launcher
        Geometry floor = new Geometry("Floor", new Box(40f, 0.1f, 20f));
        Material matFloor = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        matFloor.setTexture("ColorMap", assetManager.loadTexture("Textures/Terrain/BrickWall/BrickWall.jpg"));
        floor.setMaterial(matFloor);
        floor.setLocalTranslation(new Vector3f(-17000, 0, 0));
        RigidBodyControl floorControl = new RigidBodyControl(0);
        floor.addControl(floorControl);
        rootNode.attachChild(floor);
        physics.getBulletAppState().getPhysicsSpace().add(floorControl);

//        // chase cam
        ChaseCamera chaseCamera = new ChaseCamera(simulator.getCamera(), ballisticMissile, simulator.getInputManager());
        chaseCamera.setDefaultDistance(100);
        chaseCamera.setDefaultVerticalRotation(FastMath.DEG_TO_RAD * 45);
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
        RigidBodyControl ballisticMissileControl = ballisticMissile.getControl(RigidBodyControl.class);
        Vector3f velocity = ballisticMissileControl.getLinearVelocity();

        if (velocity.lengthSquared() > 0.001f) {
            Vector3f direction = velocity.normalize();
            Quaternion rotation = new Quaternion();
            rotation.lookAt(direction, Vector3f.UNIT_Y);

            Quaternion offset = new Quaternion().fromAngleAxis(FastMath.HALF_PI, Vector3f.UNIT_Y);
            rotation = rotation.mult(offset);

            ballisticMissile.setLocalRotation(rotation);
        }
    }
}
