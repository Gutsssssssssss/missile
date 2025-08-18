package map;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import util.AbstractEmptyAppState;

public class LightAppState extends AbstractEmptyAppState {

    @Override
    protected void initialize(Application application) {
        Node rootNode = ((SimpleApplication) application).getRootNode();

        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(1.3f));
        rootNode.addLight(al);

        DirectionalLight dl1 = new DirectionalLight();
        dl1.setColor(ColorRGBA.White);
        dl1.setDirection(new Vector3f(2.8f, -2.8f, -2.8f).normalizeLocal());
        rootNode.addLight(dl1);

        DirectionalLight dl2 = new DirectionalLight();
        dl2.setColor(ColorRGBA.White);
        dl2.setDirection(new Vector3f(-2.8f, -2.8f, -2.8f).normalizeLocal());
        rootNode.addLight(dl2);
    }

}
