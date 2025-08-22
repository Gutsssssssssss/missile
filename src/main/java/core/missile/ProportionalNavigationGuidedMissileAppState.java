package core.missile;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import core.util.TrajectoryCalculator;

public class ProportionalNavigationGuidedMissileAppState extends GuidedMissileAppState {

    public ProportionalNavigationGuidedMissileAppState(Node rootNode, AssetManager assetManager) {
        super(rootNode, assetManager);
    }

    @Override
    public void update(float tpf) {
        this.elapsedTime += tpf;
        if (!this.chasing) {
            if (this.elapsedTime >= 1f) {
                this.chasing = true;
                this.missileCtrl.setLinearVelocity(new Vector3f(0, 200f, 0));
            } else {
                return;
            }
        }

        Vector3f missilePos = this.missileNode.getWorldTranslation();
        Vector3f missileVel = this.missileCtrl.getLinearVelocity();
        Vector3f targetPos = this.target.getPos();
        Vector3f targetVel = this.target.getVel();

        Vector3f a = TrajectoryCalculator.calculatePNAcceleration(missilePos, missileVel, targetPos, targetVel);
        missileVel = missileVel.add(a.mult(tpf));
        this.missileCtrl.setLinearVelocity(missileVel);
        this.missileCtrl.setPhysicsRotation(TrajectoryCalculator.correctAngle(missileVel.normalize()));
    }

}
