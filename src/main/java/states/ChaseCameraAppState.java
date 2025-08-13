package states;

import com.jme3.input.ChaseCamera;
import com.jme3.input.InputManager;
import com.jme3.math.FastMath;
import com.jme3.renderer.Camera;
import com.jme3.scene.Spatial;

public class ChaseCameraAppState extends AbstractEmptyAppState {

    private final Camera camera;
    private final InputManager inputManager;

    public ChaseCameraAppState(Camera camera, InputManager inputManager) {
        this.camera = camera;
        this.inputManager = inputManager;
    }

    public void setChaseCamera(Spatial obj) {
        ChaseCamera chaseCamera = new ChaseCamera(camera, obj, inputManager);
        chaseCamera.setDefaultDistance(100);
        chaseCamera.setDefaultVerticalRotation(FastMath.DEG_TO_RAD * 45);
    }
}
