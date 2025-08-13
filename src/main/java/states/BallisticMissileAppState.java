package states;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
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
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

public class BallisticMissileAppState extends AbstractEmptyAppState {

    private Node ballisticMissile;

    @Override
    protected void initialize(Application application) {
        SimpleApplication simulator = (SimpleApplication) application;
        Node rootNode = simulator.getRootNode();
        AssetManager assetManager = simulator.getAssetManager();
        PhysicsAppState physics = getState(PhysicsAppState.class);
        CollisionAppState collisionState = getState(CollisionAppState.class);

        CityAppState cityAppState = simulator.getStateManager().getState(CityAppState.class);

        ballisticMissile = (Node) assetManager.loadModel(new ModelKey("Objects/missile/ballisticMissile.glb"));
        ballisticMissile.setName("BallisticMissile");
        ballisticMissile.rotate(0, -FastMath.PI, 0);
        ballisticMissile.setLocalTranslation(new Vector3f(-17000, 29f, 0));
        RigidBodyControl ballisticMissileControl = new RigidBodyControl(1f);
        ballisticMissile.addControl(ballisticMissileControl);

        // velocity
        Vector3f start = ballisticMissile.getWorldTranslation();
        Vector3f target = cityAppState.getWorldTranslation();
        Vector3f ballisticMissileVelocity = getBallisticMissileVelocity(target, start, 45f);
        ballisticMissileControl.setLinearVelocity(ballisticMissileVelocity);
        rootNode.attachChild(ballisticMissile);
        physics.addToPhysicsSpace(ballisticMissile);

        // floor
        Geometry floor = new Geometry("Floor", new Box(40f, 0.1f, 20f));
        Material matFloor = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        matFloor.setTexture("ColorMap", assetManager.loadTexture("Textures/Terrain/BrickWall/BrickWall.jpg"));
        floor.setMaterial(matFloor);
        floor.setLocalTranslation(new Vector3f(-17000, 0, 0));
        RigidBodyControl floorControl = new RigidBodyControl(0);
        floor.addControl(floorControl);
        rootNode.attachChild(floor);
        physics.addToPhysicsSpace(ballisticMissile);

        // chase cam
//        ChaseCamera chaseCamera = new ChaseCamera(simulator.getCamera(), ballisticMissile, simulator.getInputManager());
//        chaseCamera.setDefaultDistance(100);
//        chaseCamera.setDefaultVerticalRotation(FastMath.DEG_TO_RAD * 45);


        collisionState.setListener(event -> {
            String a = event.getNodeA().getName();
            String b = event.getNodeB().getName();

            if (isMissileHitCity(a, b)) {
                Vector3f contactPoint = event.getPositionWorldOnB();
                spawnExplosion(contactPoint, assetManager, rootNode);
                removeMissile(physics);
            }
        });
    }

    private static Vector3f getBallisticMissileVelocity(Vector3f target, Vector3f start, float thetaDeg) {
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

    private void removeMissile(PhysicsAppState physics) {
        physics.removeFromPhysicsSpace(ballisticMissile.getControl(RigidBodyControl.class));
        ballisticMissile.removeFromParent();
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
