import de.SweetCode.e.E;
import de.SweetCode.e.GameComponent;
import de.SweetCode.e.Settings;
import de.SweetCode.e.input.InputEntry;
import de.SweetCode.e.utils.Version;
import field.FieldView;
import game.GameOfGraphs;
import game.MapSelectView;
import game.MenuView;
import game.Player;
import ki.KIFraction;
import mapEditor.MapEditor;

import javax.swing.*;
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
        e.addScene(new MapEditor());
        e.addScene(new MapSelectView());

        e.show(MenuView.class);

        E.getE().addComponent(new GameComponent() {
            @Override
            public void update(InputEntry inputEntry, long l) {

                Player player = GameOfGraphs.getGame().getCurrentPlayer();

                if(!(player == null) && !(player.getNotifications().isEmpty()) && !(player instanceof KIFraction)) {

                    player.getNotifications().forEach(e -> {
                        JOptionPane.showMessageDialog(null, e.getDisplayMessage());
                    });
                    player.getNotifications().clear();

                }

            }

            @Override
            public boolean isActive() {
                return true;
            }
        });

        e.run();

    }

}
