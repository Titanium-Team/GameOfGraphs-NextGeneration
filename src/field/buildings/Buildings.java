package field.buildings;

import field.Field;
import field.recipe.Recipe;
import field.recipe.RecipeResource;
import field.resource.Resource;
import field.resource.Resources;
import simulation.Unit;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by boeschemeier on 20.06.2016.
 */
public enum Buildings implements Building {
    /**
     * Die Auflistung aller Gebäude, die man auf einem Feld bauen kann.
     */
    UNIT {

        @Override
        public void production(Field field) {

        }

        @Override
        public Recipe getRecipe() {
            return new Recipe(this).addIngredient(new RecipeResource(Resources.POPULATION,1))
                    .addIngredient(new RecipeResource(Resources.IRON,1))
                    .addIngredient(new RecipeResource(Resources.STONE,2))
                    .addIngredient(new RecipeResource(Resources.FOOD,2));
        }

        @Override
        public String getName() {
            return "Units";
        }
    },
    MINE {

        @Override
        public void production(Field field) {
            int n = field.getBuildings().get(this);


            int stone = n;
            productionResource(field,Resources.STONE,this,1,stone);

            int iron = 1;
            productionResource(field,Resources.IRON,this,2,iron);

            int gold = 1;
            productionResource(field,Resources.GOLD,this,3,gold);
        }

        @Override
        public Recipe getRecipe() {
            return new Recipe(this).addIngredient(new RecipeResource(Resources.POPULATION,1))
                    .addIngredient(new RecipeResource(Resources.WOOD,3))
                    .addIngredient(new RecipeResource(Resources.FOOD,2));
        }

        @Override
        public String getName() {
            return "Mines";
        }
    },
    FARM {
        @Override
        public void production(Field field) {
            int n = field.getBuildings().get(this);
            Random random = new Random();

            int food = n*2;
            productionResource(field,Resources.FOOD,this,1,food);

            int wheat = 1;
            productionResource(field,Resources.WHEAT,this,2,wheat);

            int tree = 1;
            productionResource(field, Resources.TREE,this,3,tree);
        }

        @Override
        public Recipe getRecipe() {
            return new Recipe(this).addIngredient(new RecipeResource(Resources.POPULATION,1))
                    .addIngredient(new RecipeResource(Resources.WOOD,3))
                    .addIngredient(new RecipeResource(Resources.STONE,2));
        }

        @Override
        public String getName() {
            return "Farms";
        }
    },
    WINDMILL {
        @Override
        public void production(Field field) {

            if(field.getResources().get(Resources.WHEAT) > 1){
                field.getResources().put(Resources.WHEAT, field.getResources().get(Resources.WHEAT)-2);
                productionResource(field,Resources.FOOD,this,1,8);
            }
        }

        @Override
        public Recipe getRecipe() {
            return new Recipe(this).addIngredient(new RecipeResource(Resources.POPULATION,1))
                    .addIngredient(new RecipeResource(Resources.WOOD,10))
                    .addIngredient(new RecipeResource(Resources.STONE,8))
                    .addIngredient(new RecipeResource(Resources.WHEAT,2));
        }

        @Override
        public String getName() {
            return "Windmills";
        }
    },
    WOODCUTTER {
        @Override
        public void production(Field field) {

            if(field.getResources().get(Resources.TREE) > 1){
                field.getResources().put(Resources.TREE, field.getResources().get(Resources.TREE)-2);
                productionResource(field,Resources.WOOD,this,1,10);
            }
        }

        @Override
        public Recipe getRecipe() {
            return new Recipe(this).addIngredient(new RecipeResource(Resources.POPULATION,1))
                    .addIngredient(new RecipeResource(Resources.WOOD,18))
                    .addIngredient(new RecipeResource(Resources.STONE,12))
                    .addIngredient(new RecipeResource(Resources.TREE,2));
        }

        @Override
        public String getName() {
            return "Butchers";
        }
    },
    SLAVE_MARKET {
        @Override
        public void production(Field field) {

        }

        @Override
        public Recipe getRecipe() {
            return new Recipe(this).addIngredient(new RecipeResource(Resources.WOOD, 10))
                    .addIngredient(new RecipeResource(Resources.STONE,16))
                    .addIngredient(new RecipeResource(Resources.IRON,2));
        }

        @Override
        public String getName() {
            return "Slave Market";
        }
    },
    MARKETPLACE {
        @Override
        public void production(Field field) {
        }

        @Override
        public Recipe getRecipe() {
            return new Recipe(this).addIngredient(new RecipeResource(Resources.WOOD, 15))
                    .addIngredient(new RecipeResource(Resources.STONE,20))
                    .addIngredient(new RecipeResource(Resources.GOLD,2));
        }

        @Override
        public String getName() {
            return "Marketplace";
        }
    };

    public static boolean isBuildable(Building building, Field field){

        Recipe recipe = building.getRecipe();

        for (RecipeResource resource : recipe.getItemIngredients()){

            if(!field.getResources().containsKey(resource.getResource()) || field.getResources().get(resource.getResource()) < resource.getAmount()){
                return false;
            }
        }

        if(building == Buildings.FARM){
            if(field.getBuildings().get(building) == field.getFertility()){
                return false;
            }
        } else if(building == Buildings.MINE){
            if(field.getBuildings().get(building) == field.getMountains()){
                return false;
            }
        } else if(building == Buildings.WINDMILL || building == Buildings.WOODCUTTER || building == Buildings.SLAVE_MARKET || building == Buildings.MARKETPLACE){
            if(field.getBuildings().get(building) == 1){
                return false;
            }
        }

        return true;
    }

    public static void productionResource(Field field, Resources resource, Building building, int condition, int amount){

        if(field.getBuildings().get(building) >= condition){
            if(Arrays.asList(Resources.getSpecialResources()).contains(resource)){
                if(field.getLocalResource() == resource) {
                    field.getResources().put(resource, field.getResources().get(resource) + amount);
                }
            } else {
                field.getResources().put(resource,field.getResources().get(resource) + amount);
            }

        }

    }

    public static void build(Building building, Field field, boolean freebuild){

        if(freebuild){
            if(building != UNIT){
                field.getBuildings().put(building, field.getBuildings().get(building)+1);
            }else{
                field.getUnits().add(new Unit(field.getPlayer()));
            }
        } else if (isBuildable(building,field)){

            Recipe recipe = building.getRecipe();

            for (RecipeResource resource : recipe.getItemIngredients()){
                field.getResources().put(resource.getResource(),field.getResources().get(resource.getResource())-resource.getAmount());
            }

            if(building != UNIT){
                field.getBuildings().put(building, field.getBuildings().get(building)+1);
            }else{
                field.getUnits().add(new Unit(field.getPlayer()));
            }
        }
    }
}
