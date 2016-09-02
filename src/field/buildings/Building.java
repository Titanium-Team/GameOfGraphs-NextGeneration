package field.buildings;

import field.Field;
import field.recipe.Recipe;

/**
 * Created by boeschemeier on 20.06.2016.
 */
public interface Building {

    /**
     * Sorgt für die Produktion jedes Gebäudes
     * @param field
     */
    void production(Field field);

    Recipe getRecipe();

    String getName();
}
