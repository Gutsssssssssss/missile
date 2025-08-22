package core.missile;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import core.util.TrajectoryCalculator;

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

        Vector3f targetPos = this.target.getPos();
        Vector3f missilePos = this.missileNode.getWorldTranslation();
        Vector3f LOS = targetPos.subtract(missilePos);
        LOS = LOS.mult(0.05f);
        missileCtrl.setLinearVelocity(LOS);
        this.missileCtrl.setPhysicsRotation(TrajectoryCalculator.correctAngle(LOS.normalize()));
    }
}
