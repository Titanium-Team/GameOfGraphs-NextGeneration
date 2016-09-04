package game;

import connection.Connector;

import field.FieldController;
import game.loading.LoadingManager;
import game.sprite.Textures;
import graph.GraphController;
import ki.KIController;
import ki.KIFraction;
import simulation.SimulationController;

import javax.swing.*;
import java.util.LinkedList;


public class GameOfGraphs {

	private static GameOfGraphs game;

	private int currentPlayer = 0;
	private LinkedList<Player> players = new LinkedList<>();

	private TextBuilder textBuilder = new TextBuilder();
	private LoadingManager loadingManager = new LoadingManager();

	private boolean isFirstTurn = true;

	private FieldController fieldController;
	private GraphController graphController;
	private KIController kiController;
	private SimulationController simulationController;

	public GameOfGraphs() {
		GameOfGraphs.game = this;

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {}

		// load stuff
		this.loadingManager.add(Textures.values());
		this.loadingManager.load();

		//Controller
		fieldController = new FieldController();
		graphController = new GraphController();
		kiController = new KIController();
		simulationController = new SimulationController(this.getCurrentPlayer());
	}

	public static GameOfGraphs getGame() {
		return game;
	}

    /**
     *  Wird aufgerufen um den nächsten Zug des nächsten Spielers einzuleiten.
     *
     *  Wenn der nächste Spieler eine KI ist dann wird dessen Zug automatisch ausgeführt.
     */
	public void nextTurn() {
        boolean breakCondition = false;
		this.fieldController.run(this.getCurrentPlayer());
		while (!(breakCondition)) {

			this.currentPlayer++;

			if(this.currentPlayer >= this.players.size()) {
				this.currentPlayer = 0;
                this.isFirstTurn = false;
			}

			if(this.getCurrentPlayer().isActive()) {
                breakCondition = true;
			}
		}

		this.kiController.run(this.getCurrentPlayer());
		this.simulationController.run(this.getCurrentPlayer());

		if(GameOfGraphs.getGame().getCurrentPlayer() instanceof KIFraction) {
			GameOfGraphs.getGame().nextTurn();
		}

		if (Connector.isEnabledMutiplayer()) {
			Connector.nextTurn(getCurrentPlayer().getName(), getGraphController().getGraph());
		}
	}

    /**
     * Gibt den TextBuilder zurück.
     * @return
     */
	public TextBuilder getTextBuilder() {
		return textBuilder;
	}



    /**
     * Gibt den FieldController zurück
     * @return
     */
	public FieldController getFieldController() {
		return fieldController;
	}

    /**
     * Gibt den Graph
     * @return
     */
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

	public void setCurrentPlayer(Player player) {
		this.players.set(getPlayerIndex(player), player);
	}

	public int getPlayerIndex(Player player) {
		for(int i = 0; i < this.players.size(); i++) {
			if(this.players.get(i) == player) {
				return i;
			}
		}

		throw new IllegalArgumentException("Daniel sucks balls.");
	}

	public LinkedList<Player> getPlayers() {
		return this.players;
	}

	public void setPlayers(LinkedList<Player> players) {
		this.players = players;
	}

	public boolean isFirstTurn() {
		return isFirstTurn;
	}
}
