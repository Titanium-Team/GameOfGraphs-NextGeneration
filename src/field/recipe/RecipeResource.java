package field.recipe;

import field.resource.Resource;
import field.resource.Resources;

/**
 * Created by Yonas on 04.09.2015.
 */
public class RecipeResource {

    private Resources resource;
    private int amount;

    public RecipeResource(Resources resource, int amount) {
        this.resource = resource;
        this.amount = amount;
    }

    public RecipeResource(Resources resource) {
        this(resource, 1);
    }

    public Resources getResource() {
        return this.resource;
    }

    public int getAmount() {
        return this.amount;
    }

}
