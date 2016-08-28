package game;

import de.SweetCode.e.E;
import de.SweetCode.e.Settings;
import de.SweetCode.e.utils.Version;
import event.EventManager;
import field.FieldController;
import game.loading.LoadingManager;
import game.sprite.Textures;
import graph.GraphController;
import ki.KIController;
import mapEditor.MapEditorController;
import simulation.SimulationController;
import game.views.MenuView;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class GameOfGraphs {

    private static GameOfGraphs game;

    private TextBuilder textBuilder = new TextBuilder();
    private LoadingManager loadingManager = new LoadingManager();

    private EventManager eventManager = new EventManager();
    private FieldController fieldController = new FieldController();
    private GraphController graphController = new GraphController();
    private KIController kiController = new KIController();
    private MapEditorController mapEditorController = new MapEditorController();
    private SimulationController SimulationController = new SimulationController();

    public GameOfGraphs() {
        GameOfGraphs.game = this;

        // load stuff
        this.loadingManager.add(Textures.values());
        this.loadingManager.load();

    }

    public static GameOfGraphs getGame() {
        return game;
    }

    public TextBuilder getTextBuilder() {
        return textBuilder;
    }

    public EventManager getEventManager() {
        return eventManager;
    }

    public FieldController getFieldController() {
        return fieldController;
    }

    public GraphController getGraphController() {
        return graphController;
    }

    public KIController getKiController() {
        return kiController;
    }

    public MapEditorController getMapEditorController() {
        return mapEditorController;
    }

    public simulation.SimulationController getSimulationController() {
        return SimulationController;
    }

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

        e.addScene(new MenuView());
        e.show(MenuView.class);
        e.run();

    }

    public Player getCurrentPlayer() {
        return null;
    }

    public List<Player> getPlayers() {
        return new ArrayList<>();
    }
}
