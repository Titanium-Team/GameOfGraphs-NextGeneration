package game;

import connection.ConnectionController;
import event.EventManager;
import field.FieldController;
import game.loading.LoadingManager;
import game.sprite.Textures;
import game.view.ViewManager;
import game.view.views.GameMenuView;
import game.view.views.LoadingView;
import graph.GraphController;
import graph.GraphView;
import ki.KIController;
import mapEditor.MapEditorController;
import mapEditor.MapEditorViewMenu;
import simulation.SimulationController;

import java.util.LinkedList;

public class GameOfGraphs {

    private static GameOfGraphs game;

    private ViewManager viewManager = new ViewManager();
    private GameView gameView = new GameView();
    private LoadingManager loadingManager = new LoadingManager();
    private TextBuilder textBuilder = new TextBuilder();

    private final ConnectionController connectionController = new ConnectionController();
    private final FieldController fieldController = new FieldController();
    private final GraphController graphController = new GraphController();
    private final KIController kiController = new KIController();
    private final SimulationController simulationController = new SimulationController();
    private final EventManager eventManager = new EventManager();
    private final MapEditorController mapEditorController = new MapEditorController();

    private LinkedList<Player> players = new LinkedList<>();
    private int currentPlayer;

    {
       // this.players.add(new Player("A", Membership.GREEN));
       // this.players.add(new Player("B", Membership.RED));
       // this.players.add(new Player("C", Membership.UNKNOWN));
    }

    public GameOfGraphs() {
        GameOfGraphs.game = this;

        //Adding loading screen
        this.viewManager.register(new LoadingView());
        this.viewManager.switchView(LoadingView.class);

        this.loadingManager.add(Textures.values());
        this.loadingManager.load();

        /**
         * Entweder Graph-View mit dem provisorischen FieldMenu, oder Map-Editor
         */
        //this.viewManager.register(new GraphView(new FieldMenu(GameOfGraphs.getGame().getFieldController().createField())));
        //this.viewManager.switchView(GraphView.class);
        this.viewManager.register(new GraphView(new MapEditorViewMenu()));
        this.viewManager.register(new GameMenuView());

        this.viewManager.switchView(GameMenuView.class);

    }

    public static GameOfGraphs getGame() {
        return GameOfGraphs.game;
    }

    public void nextTurn() {

        this.currentPlayer++;

        if(this.currentPlayer == this.players.size() - 1) {
            this.currentPlayer = 0;
        }

        this.eventManager.run();
        //this.connectionController.run(this.getCurrentPlayer());
        this.fieldController.run(this.getCurrentPlayer());
        //this.graphController.run(this.getCurrentPlayer());
        this.kiController.run(this.getCurrentPlayer().getFields());
        //this.simulationController.run(this.getCurrentPlayer());

    }

    public TextBuilder getTextBuilder() {
        return this.textBuilder;
    }

    public Player getCurrentPlayer() {
        return this.players.get(this.currentPlayer);
    }

    public ViewManager getViewManager() {
        return this.viewManager;
    }

    public ConnectionController getConnectionController() {
        return this.connectionController;
    }

    public SimulationController getSimulationController() {
        return this.simulationController;
    }

    public FieldController getFieldController() {
        return this.fieldController;
    }

    public KIController getKiController() {
        return this.kiController;
    }

    public GraphController getGraphController() {
        return this.graphController;
    }

    public MapEditorController getMapEditorController() {
        return mapEditorController;
    }

    public GameView getGameView() {
        return this.gameView;
    }

    public LinkedList<Player> getPlayers() {
        return this.players;
    }

    public static void main(String[] args) {
        new GameOfGraphs();
    }

    public LoadingManager getLoadingManager() {
        return loadingManager;
    }
}
