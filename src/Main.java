import de.SweetCode.e.E;
import de.SweetCode.e.Settings;
import de.SweetCode.e.utils.Version;
import field.Field;
import field.FieldView;
import game.GameOfGraphs;
import game.views.MenuView;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by EnragedPotato on 28.08.2016.
 */
public class Main {

    public static void main(String[] args) {
        new GameOfGraphs();
        E e = new E(new Settings() {

            @Override
            public String getName() {
                return "Test";
            }

            @Override
            public Version getVersion() {
                return new Version(1, 0, 0, 0, Version.ReleaseTag.ALPHA);
            }

            @Override
            public TimeUnit getDeltaUnit() {
                return TimeUnit.MILLISECONDS;
            }

            @Override
            public boolean roundDelta() {
                return true;
            }

            @Override
            public int getWidth() {
                return 1280;
            }

            @Override
            public int getHeight() {
                return 720;
            }

            @Override
            public int getTargetFPS() {
                return 50;
            }

            @Override
            public int getLogCapacity() {
                return 1024;
            }

            @Override
            public int getAmountOfLayers() {
                return 1;
            }

            @Override
            public boolean isDecorated() {
                return false;
            }

            @Override
            public boolean isResizable() {
                return false;
            }

            @Override
            public boolean fixAspectRatio() {
                return true;
            }

            @Override
            public Map<RenderingHints.Key, Object> getRenderingHints() {
                return new HashMap<>();
            }
        });

        e.addScene(new FieldView());
        e.show(FieldView.class);
        e.run();

    }

}
