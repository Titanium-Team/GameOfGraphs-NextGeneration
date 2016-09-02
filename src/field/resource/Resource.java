package field.resource;

/**
 * Created by 204g03 on 20.06.2016.
 */
public interface Resource {

    /**
     * Gibt zurück, ob die Resource ein Mineral ist oder nicht.
     * @return
     */
    boolean isMineral();

    /**
     * Gibt den Namen zurück.
     * @return
     */
    String getName();

}
