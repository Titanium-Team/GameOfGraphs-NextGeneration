package game.ui;

import de.SweetCode.e.input.InputEntry;
import de.SweetCode.e.math.IBoundingBox;
import de.SweetCode.e.math.ILocation;
import de.SweetCode.e.math.Location;
import de.SweetCode.e.rendering.GameScene;
import de.SweetCode.e.rendering.layers.Layers;

import java.awt.*;

public class CheckBox extends UIComponent<Boolean> {

    private final ILocation location;
    private final IBoundingBox boundingBox;

    private boolean checked = false;

    public CheckBox(GameScene gameScene, ILocation location, int width, int height, Trigger<Boolean> trigger) {
        super(gameScene, trigger);
        this.location = location;
        this.boundingBox = new IBoundingBox(this.location, new ILocation(this.location.getX() + width, this.location.getY() + height));
    }

    public boolean isChecked() {
        return this.checked;
    }

    @Override
    public void update(InputEntry inputEntry, long l) {

        inputEntry.getMouseEntries().forEach(entry -> {

            if(this.boundingBox.contains(new Location(entry.getPoint()))) {

                this.checked = !checked;
                this.getTrigger().call(this, this.checked);

            }

        });

    }

    @Override
    public void render(Layers layers) {

        layers.first().g().setColor(this.checked ? Color.GREEN : Color.RED);
        layers.first().g().fillRect(this.boundingBox.getMin().getX(), this.boundingBox.getMin().getY(), this.boundingBox.getWidth(), this.boundingBox.getHeight());
        layers.first().g().setColor(Color.BLACK);
        layers.first().g().drawRect(this.boundingBox.getMin().getX(), this.boundingBox.getMin().getY(), this.boundingBox.getWidth(), this.boundingBox.getHeight());

    }

    @Override
    public boolean isActive() {
        return this.getGameScene().isActive() && this.isEnabled();
    }

}
