package ki;

import de.SweetCode.e.E;
import field.resource.Resource;
import game.GameOfGraphs;
import game.Player;
import graph.Vertex;

import java.util.*;

import static game.GameOfGraphs.getGame;

/**
 * Created by Tim on 10.06.2016.
 */
public class KIFraction extends Player{

    private ArrayList<Property> properties = new ArrayList<Property>();
    private Random r = new Random();
    private int developingChance = r.nextInt(10);
    private HashMap<Player,Integer> trust = new HashMap<Player,Integer>();
    private HashMap<Vertex,HashMap<Resource,Integer>> goals = new HashMap<Vertex, HashMap<Resource, Integer>>();


    public KIFraction(String name) {
        super(name, true);

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

    public boolean isFraction(){
        return !name.equals( "independent");
    }

    public HashMap<Player, Integer> getTrust() {

        if(!(this.trust.isEmpty())) {
            Player p;
            ArrayList<Vertex> fields = getGame().getGraphController().getGraph().getVertices();
            fields.removeAll(this.getFields());
            for (Vertex v : fields) {
                p = v.getField().getPlayer();
                if (properties.contains(Property.DISTRUSTFUL)) {
                    trust.put(p, 10);
                } else {
                    trust.put(p, 40);
                }
                fields.removeAll(p.getFields());
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

    public HashMap<Vertex,HashMap<Resource,Integer>> getGoals() {
        return goals;
    }
}
