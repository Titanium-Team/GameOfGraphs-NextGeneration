package ki;

import game.GameOfGraphs;
import game.Player;

import java.util.*;

/**
 * Created by Tim on 10.06.2016.
 */
public class KIFraction extends Player{

    private ArrayList<Property> properties;
    private Random r;
    private int developingChance;
    private HashMap<Player,Integer> trust;


    public KIFraction(String name) {
        super(name);
        properties = new ArrayList<Property>();
        r=new Random();
        trust=new HashMap<Player,Integer>();
        int chance = r.nextInt(Property.values().length);
        properties.add(Property.values()[chance]);
        developingChance=r.nextInt(10);
        for(Player p: GameOfGraphs.getGame().getPlayers()){
            if(properties.contains(Property.DISTRUSTFUL)) {
                trust.put(p,10);
            }else{
                trust.put(p,40);
            }
        }
    }

    public int getDevelopingChance() {
        return developingChance;
    }

    public void setDevelopingChance(int developingChance) {
        this.developingChance = developingChance;
    }

    public ArrayList<Property> getProperties() {
        return properties;
    }

    public boolean isFraction(){
        return !name.equals( "independent");
    }

    public HashMap<Player, Integer> getTrust() {
        return trust;
    }


    public void addAlly(Player p){
        alliances.add(p);
    }

    public void addProperty(Property p){
        properties.add(p);
    }


    public void setName(String name) {
        this.name = name;
    }
}
