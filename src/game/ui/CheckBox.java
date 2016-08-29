package game.ui;

import de.SweetCode.e.input.InputEntry;
import de.SweetCode.e.math.IBoundingBox;
import de.SweetCode.e.math.ILocation;
import de.SweetCode.e.math.Location;
import de.SweetCode.e.rendering.GameScene;
import de.SweetCode.e.rendering.layers.Layer;

import java.awt.*;

public class CheckBox implements UIComponent {

    private final GameScene gameScene;

    private final ILocation location;
    private final IBoundingBox boundingBox;
    private final Trigger<Boolean> trigger;

    private boolean checked = false;

    public CheckBox(GameScene gameScene, ILocation location, int width, int height, Trigger<Boolean> trigger) {
        this.gameScene = gameScene;
        this.location = location;
        this.boundingBox = new IBoundingBox(this.location, new ILocation(this.location.getX() + width, this.location.getY() + height));
        this.trigger = trigger;
    }

    public boolean isChecked() {
        return this.checked;
    }

    @Override
    public void update(InputEntry inputEntry, long l) {

        inputEntry.getMouseEntries().forEach(entry -> {

            if(this.boundingBox.contains(new Location(entry.getPoint()))) {

                this.checked = !checked;
                this.trigger.call(this.checked);

            }

        });

    }

    @Override
    public void handleDraw(Layer layer) {

        layer.g().setColor(this.checked ? Color.GREEN : Color.RED);
        layer.g().fillRect(this.boundingBox.getMin().getX(), this.boundingBox.getMin().getY(), this.boundingBox.getWidth(), this.boundingBox.getHeight());
        layer.g().setColor(Color.BLACK);
        layer.g().drawRect(this.boundingBox.getMin().getX(), this.boundingBox.getMin().getY(), this.boundingBox.getWidth(), this.boundingBox.getHeight());

    }

    @Override
    public boolean isActive() {
        return this.gameScene.isActive();
    }


}
