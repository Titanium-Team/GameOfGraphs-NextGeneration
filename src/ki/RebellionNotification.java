package ki;

import field.resource.Resources;
import graph.Vertex;

/**
 * Created by Tim Bolz on 20.06.2016.
 */
public class RebellionNotification extends Notification {

	private Vertex place;
	private boolean successful;

	public RebellionNotification(boolean successful, Vertex place) {
		this.successful = successful;
		this.place = place;
	}

	public boolean isSuccessful() {
		return this.successful;
	}

	public Vertex getPlace() {
		return place;
	}

	@Override
	public String getDisplayMessage() {
		return "A RebellionNotification has risen in the city of " + place.getID() + ".\n There are currently "
				+ place.getField().getUnits().size() + " Units in this city and " + place.getField().getResources().get(Resources.POPULATION)/2 +" rebel troop(s) fighting them.";
	}
}
