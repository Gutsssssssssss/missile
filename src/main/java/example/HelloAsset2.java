package example;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.ZipLocator;
import com.jme3.scene.Spatial;

public class HelloAsset2 extends SimpleApplication {

    @Override
    public void simpleInitApp() {
        assetManager.registerLocator("town.zip", ZipLocator.class);
        Spatial gameLevel = assetManager.loadModel("main.scene");
        gameLevel.setLocalTranslation(0, -5.2f, 0);
        gameLevel.setLocalScale(2);
        rootNode.attachChild(gameLevel);
    }
}
