package game;

import event.EventManager;
import field.FieldController;
import game.loading.LoadingManager;
import game.sprite.Textures;
import graph.GraphController;
import ki.KIController;
import simulation.SimulationController;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GameOfGraphs {

    private static GameOfGraphs game;


    private int currentPlayer = 0;
    private List<Player> players = new ArrayList<>();

    private TextBuilder textBuilder = new TextBuilder();
    private LoadingManager loadingManager = new LoadingManager();

    private boolean isFirstTurn = true;

    private EventManager eventManager;
    private FieldController fieldController;
    private GraphController graphController;
    private KIController kiController;
    private SimulationController simulationController;

    public GameOfGraphs() {
        GameOfGraphs.game = this;

        // load stuff
        this.loadingManager.add(Textures.values());
        this.loadingManager.load();

        //Players
        this.players.add(new Player("Jan", false, Color.CYAN));
        this.players.add(new Player("Jonas", false, Color.RED));
        //this.players.add(new KIFraction("Independent"));

        //Controller
        eventManager = new EventManager();
        fieldController = new FieldController();
        graphController = new GraphController();
        kiController = new KIController();
        simulationController = new SimulationController(this.getCurrentPlayer());



    }

    public static GameOfGraphs getGame() {
        return game;
    }

    public void nextTurn() {

        fieldController.run(this.getCurrentPlayer());
        this.currentPlayer++;

        if(this.currentPlayer >= this.players.size()) {
            this.currentPlayer = 0;
            this.isFirstTurn = false;
        }

        while(!(this.getCurrentPlayer().isActive())) {
            this.currentPlayer++;
            if(this.currentPlayer >= this.players.size()) {
                this.currentPlayer = 0;
            }
        }

        if(this.currentPlayer >= this.players.size()) {
            this.currentPlayer = 0;
        }

        kiController.run();
        simulationController.run(this.getCurrentPlayer());

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

    public simulation.SimulationController getSimulationController() {
        return simulationController;
    }

    public Player getCurrentPlayer() {
        return this.players.get(this.currentPlayer);
    }

    public List<Player> getPlayers() {
        return this.players;
    }

    public boolean isFirstTurn() {
        return isFirstTurn;
    }
}
