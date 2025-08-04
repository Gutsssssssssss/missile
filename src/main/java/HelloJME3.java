import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppState;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

public class HelloJME3 extends SimpleApplication {
    public static void main(String[] args) {
        HelloJME3 helloJME3 = new HelloJME3();
        helloJME3.start();
        new AppState()
    }

    @Override
    public void simpleInitApp() {
        Box box = new Box(1, 1, 1);
        Geometry geom = new Geometry("Box", box);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        geom.setMaterial(mat);
        rootNode.attachChild(geom);
    }
}
