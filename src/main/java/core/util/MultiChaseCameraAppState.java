package core.util;

import com.jme3.input.ChaseCamera;
import com.jme3.input.InputManager;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import java.util.HashMap;
import java.util.Map;

public class MultiChaseCameraAppState extends WrappedBaseAppState {

    private final Map<String, ChaseCamera> chaseCameras = new HashMap<>();
    private final Node rootNode;
    private final InputManager inputManager;
    private final RenderManager renderManager;
    private final Camera defaultCamera;

    public MultiChaseCameraAppState(Node rootNode, InputManager inputManager, RenderManager renderManager, Camera defaultCamera) {
        this.rootNode = rootNode;
        this.inputManager = inputManager;
        this.renderManager = renderManager;
        this.defaultCamera = defaultCamera;
    }

    public void addChaseCamera(String id, Spatial obj, float left, float right, float bottom, float top) {
        if (chaseCameras.containsKey(id)) {
            return;
        }
        Camera cloneCam = defaultCamera.clone();
        cloneCam.setViewPort(left, right, bottom, top);
        ViewPort missileView = renderManager.createMainView(id, cloneCam);
        missileView.setClearFlags(true, true, true);
        missileView.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
        missileView.attachScene(rootNode);

        ChaseCamera chaseCamera = new ChaseCamera(cloneCam, obj, inputManager);
        chaseCamera.setDefaultDistance(100);
        chaseCamera.setDefaultVerticalRotation(FastMath.DEG_TO_RAD * -180);
        chaseCameras.put(id, chaseCamera);
    }

}
