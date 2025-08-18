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
    private ParticleEmitter booster;
    private ParticleEmitter smoke;

    public BallisticMissileAppState(Node rootNode, AssetManager assetManager) {
        this.rootNode = rootNode;
        this.assetManager = assetManager;
    }

    @Override
    protected void initialize(Application application) {
        PhysicsAppState physicsAppState = getState(PhysicsAppState.class);
        CollisionAppState collisionAppState = getState(CollisionAppState.class);
        CityAppState cityAppState = getState(CityAppState.class);
        MultiChaseCameraAppState multiChaseCameraAppState = getState(MultiChaseCameraAppState.class);

        this.missileNode = (Node) this.assetManager.loadModel(new ModelKey("Objects/missile/ballisticMissile.glb"));
        this.missileNode.setName("BallisticMissile");
        this.missileNode.setLocalTranslation(new Vector3f(-17000, 29f, 0));
        RigidBodyControl missileControl = new RigidBodyControl(1f);
        this.missileNode.addControl(missileControl);

        Vector3f start = this.missileNode.getWorldTranslation();
        Vector3f target = cityAppState.getWorldTranslation();
        Vector3f missileVelocity = calculateMissileVelocity(target, start, 45f);
        missileControl.setLinearVelocity(missileVelocity);
        this.rootNode.attachChild(this.missileNode);
        physicsAppState.addToPhysicsSpace(this.missileNode);

        collisionAppState.setListener(event -> {
            String a = event.getNodeA().getName();
            String b = event.getNodeB().getName();

            if (isMissileHitCity(a, b)) {
                Vector3f contactPoint = event.getPositionWorldOnB();
                spawnExplosion(contactPoint);
                removeMissile(physicsAppState);
            }
        });

        initEffects(assetManager, missileNode);
        multiChaseCameraAppState.addChaseCamera("BallisticMissile", missileNode, 0.7f, 1.0f, 0.0f, 0.3f);
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

//    private boolean isMissileHitCity(String a, String b) {
//        return (a.equals("BallisticMissile") && b.equals("City"))
//                || (a.equals("City") && b.equals("BallisticMissile"));
//    }

    private boolean isMissileHitCity(String a, String b) {
        return (a.equals("BallisticMissile")
                || b.equals("BallisticMissile"));
    }

    private void spawnExplosion(Vector3f location) {
        ParticleEmitter explosion = new ParticleEmitter("BigExplosion", ParticleMesh.Type.Triangle, 150);
        Material mat = new Material(this.assetManager, "Common/MatDefs/Misc/Particle.j3md");
        mat.setTexture("Texture", this.assetManager.loadTexture("Effects/Explosion/flame.png"));
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
        this.rootNode.attachChild(explosion);
        explosion.emitAllParticles();
    }

    private void removeMissile(PhysicsAppState physicsAppState) {
        physicsAppState.removeFromPhysicsSpace(this.missileNode.getControl(RigidBodyControl.class));
        this.missileNode.removeFromParent();
    }

    private void initEffects(AssetManager assetManager, Node missileNode) {
        booster = new ParticleEmitter("Booster", ParticleMesh.Type.Triangle, 100);
        Material boosterMat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
        boosterMat.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/flame.png"));
        booster.setMaterial(boosterMat);
        booster.setStartColor(ColorRGBA.Orange);
        booster.setEndColor(ColorRGBA.Yellow);
        booster.setStartSize(2f);
        booster.setEndSize(0.5f);
        booster.setGravity(0, 0, 0);
        booster.getParticleInfluencer().setInitialVelocity(new Vector3f(0, -10, 0));
        booster.getParticleInfluencer().setVelocityVariation(0.3f);
        missileNode.attachChild(booster);

        smoke = new ParticleEmitter("Smoke", ParticleMesh.Type.Triangle, 50);
        Material smokeMat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
        smokeMat.setTexture("Texture", assetManager.loadTexture("Effects/Smoke/Smoke.png"));
        smoke.setMaterial(smokeMat);
        smoke.setStartColor(new ColorRGBA(0.2f, 0.2f, 0.2f, 0.5f));
        smoke.setEndColor(new ColorRGBA(0.2f, 0.2f, 0.2f, 0f));
        smoke.setStartSize(3f);
        smoke.setEndSize(6f);
        smoke.setLowLife(2f);
        smoke.setHighLife(4f);
        smoke.setGravity(0, 0, 0);
        smoke.getParticleInfluencer().setInitialVelocity(new Vector3f(0, -3, 0));
        smoke.getParticleInfluencer().setVelocityVariation(1f);
        missileNode.attachChild(smoke);

        booster.setEnabled(false);
        smoke.setEnabled(false);
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

        booster.setEnabled(vel.y != 0f);
        smoke.setEnabled(vel.y != 0f);
    }

}
