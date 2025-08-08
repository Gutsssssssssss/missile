package example;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

public class HelloLoop extends SimpleApplication {
    private Geometry player1;
    private float pulseTime = 0;
    @Override
    public void simpleInitApp() {
        Box box = new Box(1, 1, 1);
        player1 = new Geometry("blue cube", box);
        Material mat = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        player1.setMaterial(mat);
        rootNode.attachChild(player1);

    }

    @Override
    public void simpleUpdate(float tpf) {
        player1.rotate(2*tpf, -2*tpf, 2*tpf);

        pulseTime += tpf;
        float scale = 1.0f + 0.2f * FastMath.sin(pulseTime * 2);
        player1.setLocalScale(scale);
    }
}
