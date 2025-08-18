package missile;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

public class PurePursuitGuidedMissileAppState extends GuidedMissileAppState {

    public PurePursuitGuidedMissileAppState(Node rootNode, AssetManager assetManager) {
        super(rootNode, assetManager);
    }

    @Override
    public void update(float tpf) {
        this.elapsedTime += tpf;
        if (!this.chasing) {
            if (this.elapsedTime >= 1f) {
                this.chasing = true;
            } else {
                return;
            }
        }

        Vector3f targetTranslation = this.target.getWorldTransition();
        Vector3f missileTranslation = this.missileNode.getWorldTranslation();
        Vector3f LOS = targetTranslation.subtract(missileTranslation);
        LOS = LOS.mult(10);
        this.missileNode.getControl(RigidBodyControl.class).setLinearVelocity(LOS);

        Quaternion rotation = new Quaternion();
        rotation.lookAt(LOS.normalize(), Vector3f.UNIT_Y);
        Quaternion offset = new Quaternion().fromAngleAxis(FastMath.HALF_PI, Vector3f.UNIT_Y);
        rotation = rotation.mult(offset);
        this.missileNode.getControl(RigidBodyControl.class).setPhysicsRotation(rotation);
    }
}
