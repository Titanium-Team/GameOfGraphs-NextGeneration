package ki;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import field.resource.Resources;
import game.GameOfGraphs;
import game.Player;
import graph.Vertex;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import static game.GameOfGraphs.getGame;

/**
 * Created by Tim on 10.06.2016.
 */
public class KIFraction extends Player{

    @JsonIgnore
    private ArrayList<Property> properties = new ArrayList<Property>();
    @JsonIgnore
    private Random r = new Random();
    @JsonIgnore
    private int developingChance = r.nextInt(10);
    @JsonIgnore
    private HashMap<Player,Integer> trust=new HashMap<Player,Integer>();
    @JsonIgnore
    private HashMap<Vertex,HashMap<Resources,Integer>> goals = new HashMap<>();

    public KIFraction(String name, Color color) {
        //super(name);
        super(name, color);
        int chance = r.nextInt(Property.values().length);
        properties.add(Property.values()[chance]);
    }

    @JsonCreator
    public KIFraction(@JsonProperty("name") String name, @JsonProperty("red")  int red, @JsonProperty("blue")  int blue, @JsonProperty("green")  int green) {
        super(name, new Color(red, green, blue));
        int chance = r.nextInt(Property.values().length);
        properties.add(Property.values()[chance]);
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

    @JsonIgnore
    public boolean isFraction(){
        return !name.equalsIgnoreCase( "independent");
    }

    public HashMap<Player, Integer> getTrust() {

        if(this.trust.size()< getGame().getPlayers().size()-1) {
            LinkedList<Player> players = getGame().getPlayers();
            players.remove(this);

            for (Player p : players) {
                if(!trust.containsKey(p)) {
                    if (properties.contains(Property.DISTRUSTFUL)) {
                        trust.put(p, 10);
                    } else {
                        trust.put(p, 40);
                    }
                }
            }

        }

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

    public HashMap<Vertex,HashMap<Resources,Integer>> getGoals() {
        return goals;
    }
}
