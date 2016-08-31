package game.ui;

import de.SweetCode.e.input.InputEntry;
import de.SweetCode.e.math.IBoundingBox;
import de.SweetCode.e.math.ILocation;
import de.SweetCode.e.math.Location;
import de.SweetCode.e.rendering.GameScene;
import de.SweetCode.e.rendering.layers.Layer;

import java.awt.*;

public class Button<T> implements UIComponent {

    private IBoundingBox boundingBox;

    private final GameScene gameScene;
    private final T text;
    private final ILocation location;
    private final Trigger trigger;

    public Button(GameScene gameScene, T text, ILocation location, Trigger<T> trigger) {
        this.gameScene = gameScene;
        this.text = text;
        this.location = location;
        this.trigger = trigger;
    }

    @Override
    public void handleDraw(Layer layer) {

        Graphics2D g = layer.g();

        if(this.boundingBox == null) {
            this.boundingBox = new IBoundingBox(this.location, new ILocation(this.location.getX() + g.getFontMetrics().stringWidth(this.text.toString()), this.location.getY() + g.getFontMetrics().getHeight()));
        }

        g.drawRect(this.boundingBox.getMin().getX(), this.boundingBox.getMin().getY(), this.boundingBox.getWidth(), this.boundingBox.getHeight());
        g.drawString(this.text.toString(), this.boundingBox.getMin().getX(), this.boundingBox.getMin().getY() + this.boundingBox.getHeight());

    }

    @Override
    public void update(InputEntry inputEntry, long l) {

        if(!(this.boundingBox == null)) {

            inputEntry.getMouseEntries().forEach(e -> {

                if(this.boundingBox.contains(new Location(e.getPoint()))) {
                    this.trigger.call(this.text);
                }

            });

        }

    }

    @Override
    public boolean isActive() {
        return this.gameScene.isActive();
    }
}
