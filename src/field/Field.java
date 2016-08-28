package field;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import field.buildings.Building;
import field.resource.Resource;
import game.Player;
import simulation.Unit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Field {

    private ArrayList<Unit> units;

    private int fertility;
    private int mountains;
    private Resource localResource;

    private  int forestType;

    @JsonIgnore
    private Map<Resource, Integer> resources;

    @JsonIgnore
    private Map<Building, Integer> buildings;

    private Player player;

    @JsonCreator
    public Field(@JsonProperty("fertility") int fertility,@JsonProperty("mountains")  int mountains,@JsonProperty("player")  Player player,@JsonProperty("forestType")  int forestType,@JsonProperty("localResource")  Resource localResource) {
        units = new ArrayList<>();
        buildings = new HashMap<>();
        resources = new HashMap<>();

        this.player = player;
        this.fertility = fertility;
        this.mountains = mountains;
        this.localResource = localResource;

        this.forestType = forestType;



    }

    public ArrayList<Unit> getUnits() {
        return units;
    }

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


    public Resource getLocalResource() {
        return localResource;
    }

    public void setLocalResource(Resource localResource) {
        this.localResource = localResource;
    }

    public Map<Resource, Integer> getResources() {
        return resources;
    }

    public Map<Building, Integer> getBuildings() {
        return buildings;
    }
}
