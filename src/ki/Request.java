package ki;

import game.Player;

import java.util.ArrayList;

/**
 * Created by Tim Bolz on 17.06.2016.
 */
public abstract class Request {
    private Player parent;


    public Request(Player parent) {
        this.parent=parent;
    }

	public Player getParent() {
        return parent;
    }

	abstract void decline();
	abstract void accept();

}
