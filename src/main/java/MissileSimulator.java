import com.jme3.app.SimpleApplication;
import com.jme3.math.ColorRGBA;
import com.jme3.system.AppSettings;
import states.*;

public class MissileSimulator extends SimpleApplication {
    public static void main(String[] args) {
        AppSettings settings = new AppSettings(true);
        settings.setResolution(1280, 720);
        settings.setTitle("Missile Simulator");

        MissileSimulator simulator = new MissileSimulator();
        simulator.setSettings(settings);
        simulator.start();
    }

    @Override
    public void simpleInitApp() {
        setDisplayStatView(false);
        viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
        cam.setFrustumFar(50000f);
        flyCam.setMoveSpeed(1000);

        stateManager.attach(new PhysicsAppState());
        stateManager.attach(new LightAppState());
        stateManager.attach(new CityAppState(rootNode, assetManager));
        stateManager.attach(new BallisticMissileAppState(rootNode, assetManager));
        stateManager.attach(new CollisionAppState());
        stateManager.attach(new FloorAppState(rootNode, assetManager));
        stateManager.attach(new ChaseCameraAppState(cam, inputManager));

//        cam.setLocation(new Vector3f(-17000, 10, 0));
    }


}
