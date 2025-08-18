package core.util;

import com.jme3.app.Application;
import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;

public class TextAppState extends AbstractEmptyAppState {

    private final Node guiNode;
    private final AssetManager assetManager;
    private BitmapText bitmapText;
    private float elapsed;

    public TextAppState(Node guiNode, AssetManager assetManager) {
        this.guiNode = guiNode;
        this.assetManager = assetManager;
    }

    @Override
    protected void initialize(Application application) {
        BitmapFont font = assetManager.loadFont("Interface/Fonts/Default.fnt");
        bitmapText = new BitmapText(font);
        bitmapText.setSize(font.getCharSet().getRenderedSize());
        bitmapText.setColor(ColorRGBA.White);
        bitmapText.setText("Elapsed: 0s");
        bitmapText.setLocalTranslation(0, 50, 0);

        guiNode.attachChild(bitmapText);
    }

    @Override
    public void update(float tpf) {
        elapsed += tpf;
        bitmapText.setText("Elapsed: " + String.format("%.1f", elapsed) + "s");
    }
}
