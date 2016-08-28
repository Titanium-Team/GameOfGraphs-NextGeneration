package ki;

import field.resource.Resources;
import graph.Vertex;

/**
 * Created by Tim Bolz on 20.06.2016.
 */
public class Rebellion extends Notifications {
	private Vertex place;

	public Rebellion(boolean successful,Vertex place) {
		super(!successful);
		this.place = place;
	}

	public boolean isSuccessful() {
		return !isFightWon();
	}

	public Vertex getPlace() {
		return place;
	}

	@Override
	public String getDisplayMessage() {
		return "A Rebellion has risen in the city of " + place.getID() + ".\n There are currently "
				+ place.getField().getUnits().size() + " Units in this city and " + place.getField().getResources().get(Resources.POPULATION)/2 +" rebel troop(s) fighting them.";
	}
}
