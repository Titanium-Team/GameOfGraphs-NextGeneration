package ki;

import game.Player;
import graph.Vertex;

/**
 * Created by Tim Bolz on 20.06.2016.
 */
public class Attack extends Notifications {
	private Player opponent;
	private Vertex place;
	private boolean defense;


	public Attack(Player opponent, Vertex place, boolean defense,boolean fightWon) {
		super(fightWon);
		this.opponent = opponent;
		this.place = place;
		this.defense = defense;
	}

	public Player getOpponent() {
		return opponent;
	}

	public Vertex getPlace() {
		return place;
	}

	public boolean isDefense() {
		return defense;
	}

	@Override
	public String getDisplayMessage() {
		String answer;
		if(this.isDefense()){
			answer="The city " + place.getID() + " has been attacked by " + opponent.getName();
			if(isFightWon()){
				answer+=",but your troops were able to prevent a defeat.";
			}else{
				answer+=" and your troops couldn't save the city from being conquered";
			}
		}else{
			answer="You have attacked the city " + place.getID();
			if(isFightWon()){
				answer+="and you have successfully sacked the city.";
			}else{
				answer+=", but you've been defeated by " + opponent.getName() + ".";
			}
		}
		return answer;
	}

}
