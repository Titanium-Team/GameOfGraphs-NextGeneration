package game.ui;

public interface Trigger<T> {

    /**
     * Diese Methode wird aufgerufen, wenn mit der UIComponent interagiert wird.
     * @param component
     * @param value
     */
    void call(UIComponent component, T value);

}
