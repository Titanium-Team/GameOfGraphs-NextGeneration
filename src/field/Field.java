package field;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import field.buildings.Building;
import field.buildings.Buildings;
import field.resource.Resource;
import field.resource.Resources;
import game.Player;
import graph.Vertex;
import simulation.Unit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Field {

    private ArrayList<Unit> units;

    //Vorfaktoren
    private int fertility;
    private int mountains;
    //Specialresource
    private Resources localResource;

    private  int forestType;

    //Alle Resouorcen eines Fields werden in einer Map gemanaged
    private Map<Resources, Integer> resources;

    //Alle Buildings eines Fields werden in einer Map gemanaged
    private Map<Buildings, Integer> buildings;

    //Der Player dem das Field gehört
    private Player player;

    @JsonCreator
    public Field(@JsonProperty("fertility") int fertility, @JsonProperty("mountains")  int mountains, @JsonProperty("player")  Player player, @JsonProperty("forestType")  int forestType, @JsonProperty("localResource")  Resources localResource, @JsonProperty("start") boolean start) {
        units = new ArrayList<>();
        this.player = player;

        if(start == true){
            for (int i = 0; i < 4; i++ ) {
                units.add(new Unit(player));
            }
        }
        this.fertility = fertility;
        this.mountains = mountains;
        this.localResource = localResource;

        this.forestType = forestType;

        buildings = new LinkedHashMap<Buildings, Integer>(){{

            for(Buildings key : Buildings.values()) {
                if (key != Buildings.UNIT) {
                    this.put(key, 0);
                }
            }
        }};
        resources = new LinkedHashMap<Resources, Integer>(){{

            for(Resources key : Resources.values()) {
                if(key == Resources.FOOD){
                    this.put(key, fertility);
                } else if (key == Resources.STONE){
                    this.put(key, mountains);
                } else if (key == Resources.WOOD){
                    this.put(key, forestType);
                } else {
                    this.put(key, 0);
                }
            }

        }};


    }

    /**
     * Gibt die Untis zurück, die auf dem Feld stehen.
     * @return
     */
    public ArrayList<Unit> getUnits() {
        return units;
    }

    /**
     * Gibt die Untis zurück, die auf dem Feld stehen und sich noch nicht bewegt haben.
     * @return
     */
    @JsonIgnore
    public ArrayList<Unit> getUnmovedUnits(){

        ArrayList<Unit> unmovedUnits = new ArrayList<>();

        for(Unit unit : units){

            if(!unit.isMoved()){
                unmovedUnits.add(unit);
            }
        }

        return unmovedUnits;
    }

    public void setUnits(ArrayList<Unit> units) {
        this.units = units;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getFertility() {
        return fertility;
    }

    public void setFertility(int fertility) {
        this.fertility = fertility;
    }

    public int getMountains() {
        return mountains;
    }

    public void setMountains(int mountains) {
        this.mountains = mountains;
    }

    public int getForestType() {
        return forestType;
    }

    public void setForestType(int forestType) {
        this.forestType = forestType;
    }


    public Resources getLocalResource() {
        return localResource;
    }

    public void setLocalResource(Resources localResource) {
        this.localResource = localResource;
    }

    public Map<Resources, Integer> getResources() {
        return resources;
    }

    public Map<Buildings, Integer> getBuildings() {
        return buildings;
    }
}
