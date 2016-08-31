package ki;

import game.Player;

/**
 * Created by Tim Bolz on 24.06.2016.
 */
public class AllianceRequest extends Request {
	public AllianceRequest(Player parent,Player recipient) {
		super(parent,recipient);
	}

	@Override
	void decline() {
		if(this.getParent() instanceof KIFraction){
			((KIFraction) this.getParent()).getTrust().put(this.getRecipient(),((KIFraction) this.getParent()).getTrust().get(this.getRecipient())-10);
		}
	}

	@Override
	void accept() {
		this.getRecipient().getAlliances().add(this.getParent());
		this.getParent().getAlliances().add(this.getRecipient());
		if(this.getRecipient() instanceof KIFraction)((KIFraction) this.getRecipient()).getTrust().put(this.getParent(),75);
		if(this.getParent() instanceof KIFraction)((KIFraction) this.getParent()).getTrust().put(this.getRecipient(),75);

	}

}
