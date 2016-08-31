package ki;

import field.resource.Resource;

import java.util.HashMap;

/**
 * Created by 204g05 on 29.08.2016.
 */
public class Depot {
    private boolean possible;
    private HashMap<Resource,Integer> map;

    public Depot(boolean possible, HashMap<Resource, Integer> map) {
        this.possible = possible;
        this.map = map;
    }

    public boolean isPossible() {
        return possible;
    }

    public HashMap<Resource, Integer> getMap() {
        return map;
    }
}
