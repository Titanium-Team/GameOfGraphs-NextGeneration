import de.SweetCode.e.E;
import de.SweetCode.e.Settings;
import de.SweetCode.e.utils.Version;
import field.FieldView;
import game.GameOfGraphs;
import game.MenuView;
import mapEditor.MapEditorView;

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
                return "Game Of Graphs - Best Game Ever";
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
                return 60;
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
                return false;
            }

            @Override
            public Map<RenderingHints.Key, Object> getRenderingHints() {
                return new HashMap<RenderingHints.Key, Object>() {{

                    this.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    this.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                }};
            }
        });

        e.addScene(new MenuView());
        e.addScene(new FieldView());
        e.addScene(new MapEditorView());

<<<<<<< HEAD
<<<<<<< HEAD
        e.show(MapEditorView.class);
=======
        e.show(FieldView.class);
>>>>>>> a006bf0b2f5cf0bb1148ccff578811ac95e6d392
=======

        e.show(MenuView.class);

>>>>>>> 16a502345a78ac7fe9b2da698319677f53967201

        e.run();

    }

}
