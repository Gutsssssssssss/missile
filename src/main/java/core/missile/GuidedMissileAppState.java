package core.missile;

import com.jme3.app.Application;
import com.jme3.asset.AssetManager;
import com.jme3.asset.ModelKey;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import core.ObjectType;
import core.util.MultiChaseCameraAppState;
import core.util.PhysicsAppState;
import core.util.TrajectoryCalculator;
import core.util.WrappedBaseAppState;

public class GuidedMissileAppState extends WrappedBaseAppState {

    private final Node rootNode;
    private final AssetManager assetManager;
    protected BallisticMissileAppState target;
    protected Node missileNode;
    protected boolean chasing;
    protected float elapsedTime;
    protected RigidBodyControl missileCtrl;
    private PhysicsAppState physicsAppState;

    protected GuidedMissileAppState(Node rootNode, AssetManager assetManager) {
        this.rootNode = rootNode;
        this.assetManager = assetManager;
    }

    @Override
    protected void initialize(Application application) {
        physicsAppState = getState(PhysicsAppState.class);
        MultiChaseCameraAppState multiChaseCameraAppState = getState(MultiChaseCameraAppState.class);
        EffectAppState effectAppState = getState(EffectAppState.class);
        target = getState(BallisticMissileAppState.class);


        this.missileNode = (Node) assetManager.loadModel(new ModelKey("Objects/missile/guidedMissile.glb"));
        this.missileNode.setName(ObjectType.GUIDED_MISSILE);
        this.missileNode.setLocalTranslation(new Vector3f(0, -50, 300));

        RigidBodyControl missileRigidBody = new RigidBodyControl(1f);
        this.missileNode.addControl(missileRigidBody);
        missileRigidBody.setLinearVelocity(new Vector3f(0, 100, 0));
        missileRigidBody.setPhysicsRotation(TrajectoryCalculator.correctAngle(missileRigidBody.getLinearVelocity()));

        rootNode.attachChild(this.missileNode);
        physicsAppState.addToPhysicsSpace(this.missileNode);

        missileCtrl = this.missileNode.getControl(RigidBodyControl.class);
        effectAppState.addBoosterEffects(missileNode);
        multiChaseCameraAppState.addChaseCamera(ObjectType.GUIDED_MISSILE, missileNode, 0.7f, 1.0f, 0.3f, 0.6f);
    }

    public void removeMissile() {
        this.physicsAppState.removeFromPhysicsSpace(this.missileNode.getControl(RigidBodyControl.class));
        this.missileNode.removeFromParent();
    }

}
