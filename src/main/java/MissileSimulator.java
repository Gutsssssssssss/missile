import com.jme3.system.AppSettings;
import example.HelloAsset;

public class MissileSimulator {
    public static void main(String[] args) {
        AppSettings settings = new AppSettings(true);
        settings.setResolution(1280, 720);
        settings.setTitle("Missile Simulator");

        CoreApplication coreApp = new CoreApplication();
        coreApp.setSettings(settings);
        coreApp.start();
    }
}
