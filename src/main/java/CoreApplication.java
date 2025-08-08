import com.jme3.app.SimpleApplication;
import com.jme3.asset.ModelKey;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;

public class CoreApplication extends SimpleApplication {
    @Override
    public void simpleInitApp() {
        // Set up physics
        BulletAppState bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);

        setDisplayStatView(false);

        viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
        flyCam.setMoveSpeed(1000);

        setUpLight();

        // Load scene model
        Node city = (Node) assetManager.loadModel(new ModelKey("Scenes/city/city.glb"));
        city.setLocalScale(0.03f);

        CollisionShape cityShape = CollisionShapeFactory.createMeshShape(city);
        RigidBodyControl cityRigidBody = new RigidBodyControl(cityShape, 0);
        city.addControl(cityRigidBody);

        rootNode.attachChild(city);
        bulletAppState.getPhysicsSpace().add(city);
    }

    private void setUpLight() {
        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(1.3f));
        rootNode.addLight(al);

        DirectionalLight dl = new DirectionalLight();
        dl.setColor(ColorRGBA.White);
        dl.setDirection(new Vector3f(2.8f, -2.8f, -2.8f).normalizeLocal());
        rootNode.addLight(dl);

        DirectionalLight dl2 = new DirectionalLight();
        dl2.setColor(ColorRGBA.White);
        dl2.setDirection(new Vector3f(-2.8f, -2.8f, -2.8f).normalizeLocal());
        rootNode.addLight(dl2);
    }
}
