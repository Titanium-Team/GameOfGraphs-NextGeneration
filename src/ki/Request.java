package ki;

import game.Player;

import java.util.ArrayList;

/**
 * Created by Tim Bolz on 17.06.2016.
 */
public abstract class Request {
	private Player recipient;
	private Player parent;


    public Request(Player parent,Player recipient) {
        this.parent=parent;
	    this.recipient=recipient;
    }

	public Player getParent() {
        return parent;
    }

	public Player getRecipient() {
		return recipient;
	}

	abstract void decline();
	abstract void accept();

}
