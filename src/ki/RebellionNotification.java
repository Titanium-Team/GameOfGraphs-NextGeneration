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
		String result = "A Rebellion has risen in the city of " + place.getID() +".";
		if(successful){
			result+="Unfortunately, the rebel troops were too strong and you have lost the city";
		}else{
			result+="Fortunately, your troops were able to deflect the riot and defended the city.";
		}
		return result;
	}
}
