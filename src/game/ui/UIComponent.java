package game.ui;

import de.SweetCode.e.GameComponent;
import de.SweetCode.e.rendering.layers.Layer;

public interface UIComponent extends GameComponent {

    void handleDraw(Layer layer);

}
