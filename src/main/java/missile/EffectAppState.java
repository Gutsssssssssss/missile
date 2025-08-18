package missile;

import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import util.AbstractEmptyAppState;

public class EffectAppState extends AbstractEmptyAppState {

    private final Node rootNode;
    private final AssetManager assetManager;

    public EffectAppState(Node rootNode, AssetManager assetManager) {
        this.rootNode = rootNode;
        this.assetManager = assetManager;
    }

    public void spawnExplosionEffects(Vector3f location) {
        ParticleEmitter explosion = new ParticleEmitter("BigExplosion", ParticleMesh.Type.Triangle, 150);
        Material mat = new Material(this.assetManager, "Common/MatDefs/Misc/Particle.j3md");
        mat.setTexture("Texture", this.assetManager.loadTexture("Effects/Explosion/flame.png"));
        explosion.setMaterial(mat);

        explosion.setImagesX(2);
        explosion.setImagesY(2);
        explosion.setStartColor(ColorRGBA.Orange);
        explosion.setEndColor(ColorRGBA.Red);
        explosion.setStartSize(6f);
        explosion.setEndSize(1f);
        explosion.setGravity(0, -4, 0);
        explosion.setLowLife(1f);
        explosion.setHighLife(3f);
        explosion.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 12, 0));
        explosion.getParticleInfluencer().setVelocityVariation(3f);

        explosion.setLocalTranslation(location);
        rootNode.attachChild(explosion);
        explosion.emitAllParticles();
    }

    public void addBoosterEffects(Node missileNode) {
        ParticleEmitter booster = new ParticleEmitter("Booster", ParticleMesh.Type.Triangle, 100);
        Material boosterMat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
        boosterMat.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/flame.png"));
        booster.setMaterial(boosterMat);
        booster.setStartColor(ColorRGBA.Orange);
        booster.setEndColor(ColorRGBA.Yellow);
        booster.setStartSize(2f);
        booster.setEndSize(0.5f);
        booster.setGravity(0, 0, 0);
        booster.getParticleInfluencer().setInitialVelocity(new Vector3f(0, -10, 0));
        booster.getParticleInfluencer().setVelocityVariation(0.3f);
        missileNode.attachChild(booster);

        ParticleEmitter smoke = new ParticleEmitter("Smoke", ParticleMesh.Type.Triangle, 50);
        Material smokeMat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
        smokeMat.setTexture("Texture", assetManager.loadTexture("Effects/Smoke/Smoke.png"));
        smoke.setMaterial(smokeMat);
        smoke.setStartColor(new ColorRGBA(0.2f, 0.2f, 0.2f, 0.5f));
        smoke.setEndColor(new ColorRGBA(0.2f, 0.2f, 0.2f, 0f));
        smoke.setStartSize(3f);
        smoke.setEndSize(6f);
        smoke.setLowLife(2f);
        smoke.setHighLife(4f);
        smoke.setGravity(0, 0, 0);
        smoke.getParticleInfluencer().setInitialVelocity(new Vector3f(0, -3, 0));
        smoke.getParticleInfluencer().setVelocityVariation(1f);
        missileNode.attachChild(smoke);
    }
}
