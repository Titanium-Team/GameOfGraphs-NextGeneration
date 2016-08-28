package field.buildings;

import field.Field;
import field.recipe.Recipe;
import field.recipe.RecipeResource;
import field.resource.Resource;
import field.resource.Resources;
import simulation.Unit;

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
                    .addIngredient(new RecipeResource(Resources.STONE,2));
        }
    },
    MINE {

        @Override
        public void production(Field field) {
            int n = field.getBuildings().get(this);
            int m = field.getMountains();
            Random random = new Random();

            int stone = n <= m ? random.nextInt(n*m) : random.nextInt(m^2) + (n-m)*random.nextInt(2);
            productionResource(field,Resources.STONE,this,1,stone);

            int iron = n <= m ? n : m;
            productionResource(field,Resources.IRON,this,2,iron);

            int gold = n - 2;
            productionResource(field,Resources.GOLD,this,3,gold);
        }

        @Override
        public boolean isMoveable() {
            return false;
        }

        @Override
        public Recipe getRecipe() {
            return new Recipe(this).addIngredient(new RecipeResource(Resources.POPULATION,1))
                    .addIngredient(new RecipeResource(Resources.WOOD,3))
                    .addIngredient(new RecipeResource(Resources.STONE,1));
        }
    },
    FARM {
        @Override
        public void production(Field field) {
            int n = field.getBuildings().get(this);
            int m = field.getFertility();
            Random random = new Random();

            int food = n <= m ? random.nextInt(n*m) : random.nextInt(m^2) + (n-m)*random.nextInt(2);
            productionResource(field,Resources.FOOD,this,1,food);

            int wheat = random.nextInt(n <= m ? n : m);
            productionResource(field,Resources.WHEAT,this,1,wheat);

            int horses = random.nextInt(2);
            productionResource(field, Resources.HORSES,this,2,horses);

            int cattle = random.nextInt(n <= m ? n : m);
            productionResource(field, Resources.CATTLE,this,2,cattle);
        }

        @Override
        public boolean isMoveable() {
            return false;
        }

        @Override
        public Recipe getRecipe() {
            return new Recipe(this).addIngredient(new RecipeResource(Resources.POPULATION,1))
                    .addIngredient(new RecipeResource(Resources.WOOD,3))
                    .addIngredient(new RecipeResource(Resources.FOOD,1));
        }
    },
    WINDMILL {
        @Override
        public void production(Field field) {

            if(field.getResources().get(Resources.WHEAT) > 0){
                field.getResources().put(Resources.WHEAT, field.getResources().get(Resources.WHEAT)-1);
                productionResource(field,Resources.FOOD,this,1,7);
            }
        }

        @Override
        public boolean isMoveable() {
            return false;
        }

        @Override
        public Recipe getRecipe() {
            return new Recipe(this).addIngredient(new RecipeResource(Resources.POPULATION,1))
                    .addIngredient(new RecipeResource(Resources.WOOD,2))
                    .addIngredient(new RecipeResource(Resources.STONE,2))
                    .addIngredient(new RecipeResource(Resources.WHEAT,2));
        }
    },
    BUTCHER {
        @Override
        public void production(Field field) {

            if(field.getResources().get(Resources.CATTLE) > 0){
                field.getResources().put(Resources.CATTLE, field.getResources().get(Resources.CATTLE)-1);
                productionResource(field,Resources.FOOD,this,1,12);
            }
        }

        @Override
        public boolean isMoveable() {
            return false;
        }

        @Override
        public Recipe getRecipe() {
            return new Recipe(this).addIngredient(new RecipeResource(Resources.POPULATION,1))
                    .addIngredient(new RecipeResource(Resources.WOOD,3))
                    .addIngredient(new RecipeResource(Resources.STONE,2))
                    .addIngredient(new RecipeResource(Resources.CATTLE,2));
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
    };

    public static boolean isBuildable(Building building, Field field){

        Recipe recipe = building.getRecipe();

        for (RecipeResource resource : recipe.getItemIngredients()){

            if(!field.getResources().containsKey(resource.getResource()) || field.getResources().get(resource.getResource()) < resource.getAmount()){
                return false;
            }
        }

        return true;
    }

    public static void productionResource(Field field, Resource resource, Building building, int condition, int amount){

        if(field.getLocalResource() == resource && field.getBuildings().get(building) >= condition){
            field.getResources().put(resource,field.getResources().get(resource) + amount);
        }

    }

    public static void build(Building building, Field field){

        if (isBuildable(building,field)){

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
