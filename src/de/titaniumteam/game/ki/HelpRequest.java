package ki;

import game.Player;
import graph.Vertex;

/**
 * Created by Tim Bolz on 24.06.2016.
 */
public class HelpRequest extends Request {
	private int amountOfUnits;
	private Vertex place;

	public HelpRequest(Player parent, int amountOfUnits, Vertex place) {
		super(parent);
		this.amountOfUnits = amountOfUnits;
		this.place = place;
	}

	public int getAmountOfUnits() {
		return amountOfUnits;
	}

	public Vertex getPlace() {
		return place;
	}

	@Override
	void decline() {

	}

	@Override
	void accept() {

	}
}
