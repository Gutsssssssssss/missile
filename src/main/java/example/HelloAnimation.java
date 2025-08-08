package example;

import com.jme3.anim.AnimComposer;
import com.jme3.anim.tween.Tween;
import com.jme3.anim.tween.Tweens;
import com.jme3.anim.tween.action.Action;
import com.jme3.anim.tween.action.BlendAction;
import com.jme3.anim.tween.action.BlendSpace;
import com.jme3.anim.tween.action.LinearBlendSpace;
import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
 import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

public class HelloAnimation extends SimpleApplication {

    private AnimComposer control;
    private Action advance;

    Node player;
    @Override
    public void simpleInitApp() {
        viewPort.setBackgroundColor(ColorRGBA.LightGray);
        initKeys();
        DirectionalLight dl = new DirectionalLight();
        dl.setDirection(new Vector3f(-0.1f, -1f, -1).normalizeLocal());
        rootNode.addLight(dl);
        player = (Node) assetManager.loadModel("Models/Oto/Oto.mesh.xml");
        player.setLocalScale(0.5f);
        rootNode.attachChild(player);
        control = player.getControl(AnimComposer.class);
        control.setCurrentAction("stand");

        BlendSpace quickBlend = new LinearBlendSpace(0f, 0.5f);
        Action halt = control.actionBlended("halt", quickBlend, "stand", "push");
        halt.setLength(0.5);

        Action walk = control.action("push");
        Tween doneTween = Tweens.callMethod(this, "onAdvanceDone");
        advance = control.actionSequence("advance", walk, halt, doneTween);
    }

    void onAdvanceDone() {
        control.setCurrentAction("stand");
    }

    private void initKeys() {
        inputManager.addMapping("Push", new KeyTrigger(KeyInput.KEY_SPACE));

        ActionListener handler = new ActionListener() {
            @Override
            public void onAction(String name, boolean keyPressed, float typ) {
                if (keyPressed && control.getCurrentAction() != advance) {
                    control.setCurrentAction("advance");
                }
            }
        };
        inputManager.addListener(handler, "Push");
    }
}
