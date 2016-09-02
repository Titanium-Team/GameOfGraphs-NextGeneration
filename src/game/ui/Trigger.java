package game.ui;

public interface Trigger<T> {

    void call(UIComponent component, T value);

}
