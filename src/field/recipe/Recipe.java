package field.recipe;

import field.buildings.Building;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yonas on 04.09.2015.
 */
public class Recipe {

    private Building result;

    private List<RecipeResource> itemIngredients = new ArrayList<RecipeResource>();

    public Recipe(Building result) {
        this.result = result;
    }

    /**
     * Hinzufügen einzeilner Bestandteile des "Gesamtrezeptes"
     * @param recipeResource
     * @return
     */
    public Recipe addIngredient(RecipeResource recipeResource) {
        this.itemIngredients.add(recipeResource);
        return this;
    }


    /**
     * Liste aller Bestandteile
     * @return
     */
    public List<RecipeResource> getItemIngredients() {
        return this.itemIngredients;
    }

    public Building getResult() {
        return this.result;
    }

}
