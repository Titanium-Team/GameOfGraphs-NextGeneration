package simulation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.Player;

public class Unit {

    private boolean moved;
    private Player player;

    @JsonCreator
    public Unit(@JsonProperty("player") Player player){
        this.player = player;
    }

    public boolean isMoved(){
        return moved;
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

}
