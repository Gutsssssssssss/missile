package states;

import com.jme3.app.Application;
import com.jme3.asset.AssetManager;
import com.jme3.asset.ModelKey;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

public class BallisticMissileAppState extends AbstractEmptyAppState {

    private final Node rootNode;
    private final AssetManager assetManager;
    private Node missileNode;

    public BallisticMissileAppState(Node rootNode, AssetManager assetManager) {
        this.rootNode = rootNode;
        this.assetManager = assetManager;
    }

    @Override
    protected void initialize(Application application) {
        PhysicsAppState physicsAppState = getState(PhysicsAppState.class);
        CollisionAppState collisionAppState = getState(CollisionAppState.class);
        CityAppState cityAppState = getState(CityAppState.class);
        ChaseCameraAppState chaseCameraAppState = getState(ChaseCameraAppState.class);

        this.missileNode = (Node) assetManager.loadModel(new ModelKey("Objects/missile/ballisticMissile.glb"));
        this.missileNode.setName("BallisticMissile");
        this.missileNode.rotate(0, -FastMath.PI, 0);
        this.missileNode.setLocalTranslation(new Vector3f(-17000, 29f, 0));
        RigidBodyControl missileControl = new RigidBodyControl(1f);
        this.missileNode.addControl(missileControl);

        Vector3f start = this.missileNode.getWorldTranslation();
        Vector3f target = cityAppState.getWorldTranslation();
        Vector3f missileVelocity = getMissileVelocity(target, start, 70f);
        missileControl.setLinearVelocity(missileVelocity);
        rootNode.attachChild(this.missileNode);
        physicsAppState.addToPhysicsSpace(this.missileNode);

        collisionAppState.setListener(event -> {
            String a = event.getNodeA().getName();
            String b = event.getNodeB().getName();

            if (isMissileHitCity(a, b)) {
                Vector3f contactPoint = event.getPositionWorldOnB();
                spawnExplosion(contactPoint, assetManager, rootNode);
                removeMissile(physicsAppState);
            }
        });

        chaseCameraAppState.setChaseCamera(missileNode);
    }

    private static Vector3f getMissileVelocity(Vector3f target, Vector3f start, float thetaDeg) {
        float g = 9.81f;

        Vector3f toTarget = target.subtract(start);
        float dx = (float) Math.sqrt(toTarget.x * toTarget.x + toTarget.z * toTarget.z);
        float thetaRad = (float)Math.toRadians(thetaDeg);
        float v = (float)Math.sqrt(dx * g / Math.sin(2 * thetaRad));

        float vxz = (float)(v * Math.cos(thetaRad));
        float vy  = (float)(v * Math.sin(thetaRad));

        Vector3f dirXZ = new Vector3f(toTarget.x, 0, toTarget.z).normalizeLocal();
        return dirXZ.mult(vxz).add(0, vy, 0);
    }

    private boolean isMissileHitCity(String a, String b) {
        return (a.equals("BallisticMissile") && b.equals("City"))
                || (a.equals("City") && b.equals("BallisticMissile"));
    }

    private void spawnExplosion(Vector3f location, AssetManager assetManager, Node rootNode) {
        ParticleEmitter explosion = new ParticleEmitter("BigExplosion", ParticleMesh.Type.Triangle, 150);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
        mat.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/flame.png"));
        explosion.setMaterial(mat);

        explosion.setImagesX(2);
        explosion.setImagesY(2);
        explosion.setStartColor(ColorRGBA.Orange);
        explosion.setEndColor(ColorRGBA.Red);
        explosion.setStartSize(6f);
        explosion.setEndSize(1f);
        explosion.setGravity(0, -4, 0);
        explosion.setLowLife(1f);
        explosion.setHighLife(3f);
        explosion.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 12, 0));
        explosion.getParticleInfluencer().setVelocityVariation(3f);

        explosion.setLocalTranslation(location);
        rootNode.attachChild(explosion);
        explosion.emitAllParticles();
    }

    private void removeMissile(PhysicsAppState physicsAppState) {
        physicsAppState.removeFromPhysicsSpace(this.missileNode.getControl(RigidBodyControl.class));
        this.missileNode.removeFromParent();
    }

    @Override
    public void update(float tpf) {
        RigidBodyControl ballisticMissileControl = this.missileNode.getControl(RigidBodyControl.class);
        Vector3f velocity = ballisticMissileControl.getLinearVelocity();
        if (velocity.lengthSquared() > 0.001f) {
            Vector3f direction = velocity.normalize();
            Quaternion rotation = new Quaternion();
            rotation.lookAt(direction, Vector3f.UNIT_Y);

            Quaternion offset = new Quaternion().fromAngleAxis(FastMath.HALF_PI, Vector3f.UNIT_Y);
            rotation = rotation.mult(offset);

            this.missileNode.setLocalRotation(rotation);
        }
    }
}
