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

    UNIT {

        @Override
        public void production(Field field) {

        }

        @Override
        public boolean isMoveable() {
            return true;
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

            int iron = n/2;
            productionResource(field,Resources.IRON,this,2,iron);

            int gold = n - 2;
            //productionResource(field,Resources.GOLD,this,3,gold);
        }

        @Override
        public boolean isMoveable() {
            return false;
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

            int horses = random.nextInt(2);
            //productionResource(field, Resources.HORSES,this,2,horses);

            int cattle = 1;
            productionResource(field, Resources.CATTLE,this,3,cattle);
        }

        @Override
        public boolean isMoveable() {
            return false;
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

            if(field.getResources().get(Resources.WHEAT) > 0){
                field.getResources().put(Resources.WHEAT, field.getResources().get(Resources.WHEAT)-1);
                productionResource(field,Resources.FOOD,this,1,8);
            }
        }

        @Override
        public boolean isMoveable() {
            return false;
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
    BUTCHER {
        @Override
        public void production(Field field) {

            if(field.getResources().get(Resources.CATTLE) > 0){
                field.getResources().put(Resources.CATTLE, field.getResources().get(Resources.CATTLE)-1);
                productionResource(field,Resources.FOOD,this,1,18);
            }
        }

        @Override
        public boolean isMoveable() {
            return false;
        }

        @Override
        public Recipe getRecipe() {
            return new Recipe(this).addIngredient(new RecipeResource(Resources.POPULATION,1))
                    .addIngredient(new RecipeResource(Resources.WOOD,20))
                    .addIngredient(new RecipeResource(Resources.STONE,15))
                    .addIngredient(new RecipeResource(Resources.CATTLE,2));
        }

        @Override
        public String getName() {
            return "Butchers";
        }
    },
    BARRACKS {
        @Override
        public void production(Field field) {

        }

        @Override
        public boolean isMoveable() {
            return false;
        }

        @Override
        public Recipe getRecipe() {
            return new Recipe(this).addIngredient(new RecipeResource(Resources.WOOD,3))
                    .addIngredient(new RecipeResource(Resources.STONE,2))
                    .addIngredient(new RecipeResource(Resources.IRON,1));
        }

        @Override
        public String getName() {
            return "Barracks";
        }
    };

    public static boolean isBuildable(Building building, Field field){

        Recipe recipe = building.getRecipe();

        for (RecipeResource resource : recipe.getItemIngredients()){

            if(!field.getResources().containsKey(resource.getResource()) || field.getResources().get(resource.getResource()) < resource.getAmount()){
                return false;
            }
        }

        if(field.getBuildings().get(Buildings.FARM) == field.getFertility() ||
                field.getBuildings().get(Buildings.MINE) == field.getMountains() ||
                field.getBuildings().get(Buildings.WINDMILL) == 1 ||
                field.getBuildings().get(Buildings.BUTCHER) == 1 ||
                field.getBuildings().get(Buildings.BARRACKS) == 1){
            return false;
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
            field.getBuildings().put(building, field.getBuildings().get(building)+1);
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
