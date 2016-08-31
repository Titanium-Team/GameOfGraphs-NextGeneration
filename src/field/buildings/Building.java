package field.buildings;

import field.Field;
import field.recipe.Recipe;

/**
 * Created by boeschemeier on 20.06.2016.
 */
public interface Building {

    void production(Field field);

    boolean isMoveable();

    Recipe getRecipe();

    String getName();
}
