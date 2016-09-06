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
		simulationController = new SimulationController();
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
		this.fieldController.run(this.getCurrentPlayer());

		boolean breakCondition=false;
		while (!(breakCondition)) {

			this.currentPlayer++;

			if(this.currentPlayer >= this.players.size()) {
				this.currentPlayer = 0;
                this.isFirstTurn = false;
			}

			if(this.getCurrentPlayer().isActive()) {
                breakCondition=true;
			}else{
				this.getPlayers().remove(this.getCurrentPlayer());
			}
        }

        this.simulationController.run(this.getCurrentPlayer());
        this.kiController.run(this.getCurrentPlayer());
		if(GameOfGraphs.getGame().getCurrentPlayer() instanceof KIFraction) {
			GameOfGraphs.getGame().nextTurn();
		}else {
			if (Connector.isEnabledMutiplayer()) {
				//for (Player p : getPlayers()) {
				//	if (!(p instanceof KIFraction) && !p.getName().equals(Connector.getMyPlayer().getName())) {
						//currentPlayer = getPlayerIndex(p);
						Connector.nextTurn(getCurrentPlayer().getName(), getGraphController().getGraph());
				//		break;
					//}
				//}
			}
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
     * Gibt den GraphController zurück.
     * @return
     */
	public GraphController getGraphController() {
		return graphController;
	}

    /**
     * Gibt den KIController zurück.
     * @return
     */
	public KIController getKiController() {
		return kiController;
	}

    /**
     * Gibt den SimulationController zurück.
     * @return
     */
	public simulation.SimulationController getSimulationController() {
		return simulationController;
	}

    /**
     * Gibt den Spieler zurück der aktuell am Zug ist.
     * @return
     */
	public Player getCurrentPlayer() {
        if(this.players.isEmpty()) {
            return null;
        }

		return this.players.get(this.currentPlayer);
	}

	/**
	 * Setzt den aktuellen Spieler.
	 * @param name
	 */
	public void setCurrentPlayer(String name) {
		for (Player p : players){
			if (p.getName().equals(name)){
				currentPlayer = getPlayerIndex(p);
			}
		}

	}

    /**
     * Gibt den Index eines Spielers zurück.
     * @param player
     * @return
     */
	public int getPlayerIndex(Player player) {
		for(int i = 0; i < this.players.size(); i++) {
			if(this.players.get(i) == player) {
				return i;
			}
		}

		throw new IllegalArgumentException("Player not found.");
	}

    /**
     * Gibt alle Spieler die am Spiel teilnehmen oder teilnahmen zurück.
     * @return
     */
	public LinkedList<Player> getPlayers() {
		return this.players;
	}

    /**
     * Setzt die Spieler.
     * @param players
     */
	public void setPlayers(LinkedList<Player> players) {
		this.players = players;
	}

    /**
     * Gibt zurück ob man sich aktuell im ersten Turn befindet.
     * @return
     */
	public boolean isFirstTurn() {
		return isFirstTurn;
	}
}
