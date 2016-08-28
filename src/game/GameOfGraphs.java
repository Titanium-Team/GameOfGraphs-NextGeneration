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



    public Player getCurrentPlayer() {
        return null;
    }

    public List<Player> getPlayers() {
        return new ArrayList<Player>();
    }
}
