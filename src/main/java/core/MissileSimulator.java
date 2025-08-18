package core;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.ColorRGBA;
import com.jme3.system.AppSettings;
import core.map.CityAppState;
import core.map.FloorAppState;
import core.map.LauncherAppState;
import core.map.LightAppState;
import core.missile.BallisticMissileAppState;
import core.missile.EffectAppState;
import core.missile.PurePursuitGuidedMissileAppState;
import core.util.*;

/**
 * Main application class for the Missile Simulator.
 * Initializes the jMonkeyEngine application, sets up the scene, and attaches
 * AppState class to manage physics, objects etc.
 **/
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

        stateManager.attach(new TextAppState(guiNode, assetManager));
        stateManager.attach(new PhysicsAppState());
        stateManager.attach(new LightAppState());
        stateManager.attach(new CityAppState(rootNode, assetManager));
        stateManager.attach(new BallisticMissileAppState(rootNode, assetManager));
        stateManager.attach(new CollisionAppState());
        stateManager.attach(new FloorAppState(rootNode, assetManager));
        stateManager.attach(new LauncherAppState(rootNode, assetManager));
        stateManager.attach(new MultiChaseCameraAppState(rootNode, inputManager, renderManager, cam));
        stateManager.attach(new EffectAppState(rootNode, assetManager));

        inputManager.addMapping("LaunchGuidedMissile", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addListener((ActionListener) (name, keyPressed, tpf) -> {
            if (name.equals("LaunchGuidedMissile") && !keyPressed) {
                stateManager.attach(new PurePursuitGuidedMissileAppState(rootNode, assetManager));
        }
    }, "LaunchGuidedMissile");
    }
}
