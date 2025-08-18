package core.missile;

import com.jme3.app.Application;
import com.jme3.asset.AssetManager;
import com.jme3.asset.ModelKey;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import core.ObjectType;
import core.map.CityAppState;
import core.util.AbstractEmptyAppState;
import core.util.MultiChaseCameraAppState;
import core.util.PhysicsAppState;

public class BallisticMissileAppState extends AbstractEmptyAppState {

    private final Node rootNode;
    private final AssetManager assetManager;
    private Node missileNode;
    private PhysicsAppState physicsAppState;

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
        Vector3f missileVelocity = calculateMissileVelocity(target, start, 45f);
        missileControl.setLinearVelocity(missileVelocity);
        this.rootNode.attachChild(this.missileNode);
        physicsAppState.addToPhysicsSpace(this.missileNode);

        effectAppState.addBoosterEffects(missileNode);
        multiChaseCameraAppState.addChaseCamera(ObjectType.BALLISTIC_MISSILE, missileNode, 0.7f, 1.0f, 0.0f, 0.3f);
    }

    private Vector3f calculateMissileVelocity(Vector3f target, Vector3f start, float thetaDeg) {
        float g = 9.81f;

        Vector3f toTarget = target.subtract(start);
        float distanceXZ = (float) Math.sqrt(toTarget.x * toTarget.x + toTarget.z * toTarget.z);
        float thetaRad = (float)Math.toRadians(thetaDeg);
        float velocity = (float)Math.sqrt(distanceXZ * g / Math.sin(2 * thetaRad));

        float velocityXZ = (float)(velocity * Math.cos(thetaRad));
        float velocityY  = (float)(velocity * Math.sin(thetaRad));

        Vector3f directionXZ = new Vector3f(toTarget.x, 0, toTarget.z).normalizeLocal();
        return directionXZ.mult(velocityXZ).add(0, velocityY, 0);
    }

    public void removeMissile() {
        this.physicsAppState.removeFromPhysicsSpace(this.missileNode.getControl(RigidBodyControl.class));
        this.missileNode.removeFromParent();
    }

    public Vector3f getWorldTransition() {
        return this.missileNode.getWorldTranslation();
    }

    @Override
    public void update(float tpf) {
        RigidBodyControl ctrl = this.missileNode.getControl(RigidBodyControl.class);
        Vector3f vel = ctrl.getLinearVelocity();
        if (vel.lengthSquared() > 1e-3f) {
            Quaternion rot = new Quaternion();
            rot.lookAt(vel.normalize(), Vector3f.UNIT_Y);
            Quaternion offset = new Quaternion().fromAngleAxis(FastMath.HALF_PI, Vector3f.UNIT_Y);
            rot = rot.mult(offset);
            ctrl.setPhysicsRotation(rot);
        }
    }

}
