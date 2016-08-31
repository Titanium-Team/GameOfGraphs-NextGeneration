package game.ui;

import de.SweetCode.e.input.InputEntry;
import de.SweetCode.e.math.BoundingBox;
import de.SweetCode.e.math.ILocation;
import de.SweetCode.e.rendering.GameScene;
import de.SweetCode.e.rendering.layers.Layer;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by 204g02 on 29.08.2016.
 */
public class DropDownMenu<T> implements UIComponent {

    private final GameScene gameScene;
    private final ILocation location;
    private final Trigger<T> callable;
    private BoundingBox openBoxBoundings;

    private final Map<T, BoundingBox> boundingBoxes = new HashMap<>();
    private List<T> options;

    private int selectedIndex = 0;
    private boolean open = false;

    public DropDownMenu(GameScene gameScene, ILocation location, LinkedList<T> options, Trigger<T> callable) {
        this.gameScene = gameScene;
        this.location = location;
        this.options = options;
        this.callable = callable;
    }

    public void setOptions(LinkedList<T> options) {
        this.options = options;
        this.boundingBoxes.clear();
        this.selectedIndex = 0;
    }

    public void setSelectedIndex(int selectedIndex){
        this.selectedIndex = selectedIndex;
    }

    public T getOption() {
        if(this.options.isEmpty()) {
            return null;
        }

        return this.options.get(this.selectedIndex);
    }

    @Override
    public void update(InputEntry inputEntry, long delta) {

        if(!(this.boundingBoxes.isEmpty())) {
            inputEntry.getMouseEntries().forEach(entry -> {

                // Wenn das Menu aktuell offen ist...
                if (this.open) {

                    // Die BoundingBox bereits existiert
                    if (!(this.openBoxBoundings == null)) {

                        // Die Mouse irgendwo in den offenen Bereich geclickt hat & mit dem linken MouseButton geclickt wurde...
                        if (this.openBoxBoundings.contains(new ILocation(entry.getPoint())) && (entry.getButton() == MouseEvent.BUTTON1)) {

                            for (int i = 0; i < this.options.size(); i++) {

                                if (this.boundingBoxes.get(this.options.get(i)).contains(new ILocation(entry.getPoint()))) {
                                    this.callable.call(this.options.get(i));
                                    this.selectedIndex = i;
                                    this.open = false;
                                }

                            }

                        } else {
                            // Falls nicht dann wird das Menu wieder geschlossen...
                            this.open = false;
                        }

                    }

                } else {

                    if (entry.getButton() == MouseEvent.BUTTON1) {
                        BoundingBox box = this.boundingBoxes.get(this.options.get(0));

                        if (box.contains(new ILocation(entry.getPoint()))) {
                            this.open = true;
                        }
                    }

                }

            });
        }

    }

    @Override
    public void handleDraw(Layer layer) {

        Graphics2D g = layer.g();
        FontMetrics fontMetrics = g.getFontMetrics();

        // max width
        int maxWidth = 0;
        for(T entry : this.options) {
            maxWidth = Math.max(maxWidth, fontMetrics.stringWidth(String.valueOf(entry)));
        }
        // height
        int height = fontMetrics.getHeight();
        int totalHeight = this.options.size() * height;

        // bounding boxes
        if(this.boundingBoxes.isEmpty()) {

            for (int i = 0; i < this.options.size(); i++) {

                this.boundingBoxes.put(this.options.get(i), new BoundingBox(new ILocation(location.getX(), location.getY() + height * i), new ILocation(location.getX() + maxWidth, location.getY() + height * i + height)));

            }

            this.openBoxBoundings = new BoundingBox(new ILocation(location.getX(), location.getY()), new ILocation(location.getX() + maxWidth, location.getY() + totalHeight));

        }


        if(this.open) {

            g.drawRect(location.getX(), location.getY(), maxWidth, totalHeight);

            int y = location.getY();
            for(T entry : this.options) {

                g.drawString(String.valueOf(entry), location.getX(), y + height);
                y += height;

            }

        } else {

            g.drawRect(location.getX(), location.getY(), maxWidth, height);
            g.drawString(String.valueOf(this.options.get(selectedIndex)), location.getX(), (location.getY() + height));

        }

    }

    @Override
    public boolean isActive() {
        return this.gameScene.isActive();
    }

}
