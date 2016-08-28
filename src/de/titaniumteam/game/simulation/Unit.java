package de.titaniumteam.game.simulation;

import game.Player;

public class Unit {

    private boolean moved;
    private Player player;

    public Unit(Player player){
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
