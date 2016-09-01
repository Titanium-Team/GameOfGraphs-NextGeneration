package ki;

/**
 * Created by Tim Bolz on 20.06.2016.
 */
public abstract class Notification {

	private String name;

	public Notification() {}

	public String getName() {
		return name;
	}

	public abstract  String getDisplayMessage();

}
