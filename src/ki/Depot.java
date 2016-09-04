package ki;

import field.resource.Resource;
import field.resource.Resources;

import java.util.HashMap;

/**
 * Created by 204g05 on 29.08.2016.
 */
public class Depot {
    private boolean possible;
    private HashMap<Resources,Integer> map;

    public Depot(boolean possible, HashMap<Resources, Integer> map) {
        this.possible = possible;
        this.map = map;
    }

    public boolean isPossible() {
        return possible;
    }

    public HashMap<Resources, Integer> getMap() {
        return map;
    }
}
