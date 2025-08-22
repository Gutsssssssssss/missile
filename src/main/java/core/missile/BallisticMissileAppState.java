package core.missile;

import com.jme3.app.Application;
import com.jme3.asset.AssetManager;
import com.jme3.asset.ModelKey;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import core.ObjectType;
import core.map.CityAppState;
import core.util.MultiChaseCameraAppState;
import core.util.PhysicsAppState;
import core.util.TrajectoryCalculator;
import core.util.WrappedBaseAppState;

public class BallisticMissileAppState extends WrappedBaseAppState {

    private final Node rootNode;
    private final AssetManager assetManager;
    private Node missileNode;
    private PhysicsAppState physicsAppState;
    private RigidBodyControl ctrl;

    public BallisticMissileAppState(Node rootNode, AssetManager assetManager) {
        this.rootNode = rootNode;
        this.assetManager = assetManager;
    }

    @Override
    protected void initialize(Application application) {
        physicsAppState = getState(PhysicsAppState.class);
        CityAppState cityAppState = getState(CityAppState.class);
        MultiChaseCameraAppState multiChaseCameraAppState = getState(MultiChaseCameraAppState.class);
        EffectAppState effectAppState = getState(EffectAppState.class);

        this.missileNode = (Node) this.assetManager.loadModel(new ModelKey("Objects/missile/ballisticMissile.glb"));
        this.missileNode.setName(ObjectType.BALLISTIC_MISSILE);
        this.missileNode.setLocalTranslation(new Vector3f(-17000, 29f, 0));
        RigidBodyControl missileControl = new RigidBodyControl(1f);
        this.missileNode.addControl(missileControl);

        Vector3f start = this.missileNode.getWorldTranslation();
        Vector3f target = cityAppState.getWorldTranslation();
        float ballisticAngle = (float) Math.toRadians(45f);
        float missileVelocity = TrajectoryCalculator.calculateBallisticVelocity(start, target, ballisticAngle);
        missileControl.setLinearVelocity(TrajectoryCalculator.convertToVector(start, target, missileVelocity, ballisticAngle));
        this.rootNode.attachChild(this.missileNode);
        physicsAppState.addToPhysicsSpace(this.missileNode);

        ctrl = this.missileNode.getControl(RigidBodyControl.class);
        effectAppState.addBoosterEffects(missileNode);
        multiChaseCameraAppState.addChaseCamera(ObjectType.BALLISTIC_MISSILE, missileNode, 0.7f, 1.0f, 0.0f, 0.3f);
    }

    public void removeMissile() {
        this.physicsAppState.removeFromPhysicsSpace(this.missileNode.getControl(RigidBodyControl.class));
        this.missileNode.removeFromParent();
    }

    public Vector3f getPos() {
        return this.missileNode.getWorldTranslation();
    }

    public Vector3f getVel() {
        return this.ctrl.getLinearVelocity();
    }

    @Override
    public void update(float tpf) {
        Vector3f vel = this.ctrl.getLinearVelocity();
        if (vel.lengthSquared() > 1e-3f) {
            this.ctrl.setPhysicsRotation(TrajectoryCalculator.getRotationTowards(vel.normalize()));
        }
    }

}
