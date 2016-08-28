package field.recipe;

import field.resource.Resource;

/**
 * Created by Yonas on 04.09.2015.
 */
public class RecipeResource {

    private Resource resource;
    private int amount;

    public RecipeResource(Resource resource, int amount) {
        this.resource = resource;
        this.amount = amount;
    }

    public RecipeResource(Resource resource) {
        this(resource, 1);
    }

    public Resource getResource() {
        return this.resource;
    }

    public int getAmount() {
        return this.amount;
    }

}
