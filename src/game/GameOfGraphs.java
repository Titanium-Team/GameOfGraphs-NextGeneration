package game;

import connection.Connector;
import event.EventManager;
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

	private EventManager eventManager;
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
		/*fieldController.run(this.getCurrentPlayer());
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


		while(this.getCurrentPlayer() instanceof KIFraction) {
			kiController.run(this.getCurrentPlayer());
			currentPlayer++;
			if(this.currentPlayer >= this.players.size()) {
				this.currentPlayer = 0;
				this.isFirstTurn=false;
			}
		}

		
		kiController.run(this.getCurrentPlayer());
		simulationController.run(this.getCurrentPlayer());*/

        boolean breakCondition = false;
		while (!(breakCondition)) {

			this.currentPlayer++;

			if(this.currentPlayer >= this.players.size()) {
				this.currentPlayer = 0;
			}

			if(this.getCurrentPlayer().isActive()) {
                breakCondition = true;
			}

		}

		this.kiController.run(this.getCurrentPlayer());
		this.fieldController.run(this.getCurrentPlayer());
		this.simulationController.run(this.getCurrentPlayer());

		if(GameOfGraphs.getGame().getCurrentPlayer() instanceof KIFraction) {
			GameOfGraphs.getGame().nextTurn();
		}

		if (Connector.isEnabledMutiplayer()) {
			Connector.nextTurn(getCurrentPlayer().getName(), getGraphController().getGraph());
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

	public simulation.SimulationController getSimulationController() {
		return simulationController;
	}

	public Player getCurrentPlayer() {
		return this.players.get(this.currentPlayer);
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
