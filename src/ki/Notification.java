package ki;

/**
 * Created by Tim Bolz on 20.06.2016.
 */
public abstract class Notification {
	private String name;
	private boolean fightWon;

	public Notification(boolean fightWon) {
		this.fightWon = fightWon;
	}

	public String getName() {
		return name;
	}


	public abstract  String getDisplayMessage();

	public boolean isFightWon() {
		return fightWon;
	}
}