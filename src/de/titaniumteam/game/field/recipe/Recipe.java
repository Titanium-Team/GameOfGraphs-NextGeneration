package field.recipe;

import field.buildings.Building;
import game.inventory.resource.ResourceStack;

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

    public Recipe addIngredient(RecipeResource recipeResource) {
        this.itemIngredients.add(recipeResource);
        return this;
    }


    public List<RecipeResource> getItemIngredients() {
        return this.itemIngredients;
    }

    public Building getResult() {
        return this.result;
    }

}
