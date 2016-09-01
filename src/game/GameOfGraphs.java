package game;

import event.EventManager;
import field.FieldController;
import game.loading.LoadingManager;
import game.sprite.Textures;
import graph.GraphController;
import ki.KIController;
import ki.KIFraction;
import mapEditor.MapEditorController;
import simulation.SimulationController;

import java.util.ArrayList;
import java.util.List;

public class GameOfGraphs {

    private static GameOfGraphs game;


    private int currentPlayer = 0;
    private List<Player> players = new ArrayList<>();

    private TextBuilder textBuilder = new TextBuilder();
    private LoadingManager loadingManager = new LoadingManager();

    private EventManager eventManager;
    private FieldController fieldController;
    private GraphController graphController;
    private KIController kiController;
    private MapEditorController mapEditorController;
    private SimulationController simulationController;

    public GameOfGraphs() {
        GameOfGraphs.game = this;

        // load stuff
        this.loadingManager.add(Textures.values());
        this.loadingManager.load();

        //Players
        this.players.add(new Player("1", false));
        this.players.add(new Player("2", false));
        this.players.add(new KIFraction("Independent"));

        //Controller
        eventManager = new EventManager();
        fieldController = new FieldController();
        graphController = new GraphController();
        kiController = new KIController();
        mapEditorController = new MapEditorController();
        simulationController = new SimulationController(this.players.get(0));


    }

    public static GameOfGraphs getGame() {
        return game;
    }

    public void nextTurn() {

        fieldController.run(this.getCurrentPlayer());
        kiController.run();
        simulationController.run(this.getCurrentPlayer());

        this.currentPlayer++;

        if(this.currentPlayer >= this.players.size()) {
            this.currentPlayer = 0;
        }



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
        return simulationController;
    }

    public Player getCurrentPlayer() {
        return this.players.get(this.currentPlayer);
    }

    public List<Player> getPlayers() {
        return this.players;
    }


}
