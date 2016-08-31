package game;

import event.EventManager;
import field.FieldController;
import game.loading.LoadingManager;
import game.sprite.Textures;
import graph.GraphController;
import ki.KIController;
import mapEditor.MapEditorController;
import simulation.SimulationController;

import java.util.LinkedList;
import java.util.List;

public class GameOfGraphs {

    private static GameOfGraphs game;

    private List<Player> players = new LinkedList<>();
    private int currentPlayerIndex = 0;

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
        return this.players.get(this.currentPlayerIndex);
    }

    public List<Player> getPlayers() {
        return this.players;
    }
}
