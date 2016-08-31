package ki;

import game.Player;
import graph.Vertex;

import java.util.ArrayList;

import static game.GameOfGraphs.getGame;

/**
 * Created by Tim Bolz on 24.06.2016.
 */
public class HelpRequest extends Request {
	private int amountOfUnits;
	private Vertex place;

	public HelpRequest(Player parent, int amountOfUnits, Vertex place,Player recipient) {
		super(parent,recipient);
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
		if(this.getParent() instanceof KIFraction) {
			((KIFraction) this.getParent()).getTrust().put(this.getRecipient(), ((KIFraction) this.getParent()).getTrust().get(this.getRecipient()) - 10);
		}
		}

	@Override
	void accept() {
		ArrayList<Vertex> origin = new ArrayList<>();
		ArrayList<Vertex> neighbors = getGame().getGraphController().getGraph().getNeighbours(this.getPlace());
		int amount = this.getAmountOfUnits();
		for (Vertex v : neighbors) {
			if (getRecipient().equals(v.getField().getPlayer())) {
				if(amount>0) {
					origin.add(v);
					amount-=v.getField().getUnmovedUnits().size()-3;
				}
			}
		}
		amount = this.getAmountOfUnits();
		for (Vertex v : origin) {
			if(amount>0) {
				if (v.getField().getUnmovedUnits().size() + 3 >= amount) {
					getGame().getSimulationController().moveUnits(v, this.getPlace(), amount);
					amount = 0;
				} else {
					getGame().getSimulationController().moveUnits(v, this.getPlace(), v.getField().getUnmovedUnits().size() - 3);
					amount -= this.getPlace().getField().getUnmovedUnits().size() - 3;
				}
			}
		}
		if(this.getParent() instanceof KIFraction){
			((KIFraction) this.getParent()).getTrust().put(this.getRecipient(),((KIFraction) this.getParent()).getTrust().get(this.getRecipient())+10);
		}
	}
}
