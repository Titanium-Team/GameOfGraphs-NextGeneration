package game.ui;

import de.SweetCode.e.input.InputEntry;
import de.SweetCode.e.math.IBoundingBox;
import de.SweetCode.e.math.ILocation;
import de.SweetCode.e.rendering.GameScene;
import de.SweetCode.e.rendering.layers.Layers;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by 204g02 on 29.08.2016.
 */
public class DropDownMenu<T> extends UIComponent<T> {

    private final ILocation location;
    private IBoundingBox openBoxBoundings;

    private final Map<T, IBoundingBox> boundingBoxes = new HashMap<>();
    private List<T> options;

    private int selectedIndex = 0;
    private boolean open = false;

    private Color background = null;
    private Color foreground = null;

    public DropDownMenu(GameScene gameScene, ILocation location, LinkedList<T> options, Trigger<T> trigger) {
        super(gameScene, trigger);
        this.location = location;
        this.options = options;
    }

    public void setOptions(LinkedList<T> options) {
        this.options = options;
        this.boundingBoxes.clear();
        this.selectedIndex = 0;
    }

    public int getSelectedIndex() {
        return this.selectedIndex;
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

                                IBoundingBox box = this.boundingBoxes.get(this.options.get(i));

                                if (!(box == null) && box.contains(new ILocation(entry.getPoint()))) {
                                    this.getTrigger().call(this, this.options.get(i));
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
                        IBoundingBox box = this.boundingBoxes.get(this.options.get(0));
                        if (box.contains(new ILocation(entry.getPoint()))) {
                            this.open = true;
                        }
                    }

                }

            });
        }

    }

    @Override
    public void render(Layers layers) {

        Graphics2D g = layers.first().g();
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

                this.boundingBoxes.put(this.options.get(i), new IBoundingBox(new ILocation(location.getX(), location.getY() + height * i), new ILocation(location.getX() + maxWidth + 4, location.getY() + (height + 2) * (i + 1))));

            }

            this.openBoxBoundings = new IBoundingBox(new ILocation(location.getX(), location.getY()), new ILocation(location.getX() + maxWidth + 4, location.getY() + totalHeight + 2));

        }

        if(this.open) {

            if (this.background != null){
                g.setColor(this.background);
                g.fillRect(this.location.getX(), this.location.getY(), this.openBoxBoundings.getWidth(), this.openBoxBoundings.getHeight());
            }

            if (this.foreground != null){
                g.setColor(this.foreground);
            }

            g.drawRect(this.location.getX(), this.location.getY(), this.openBoxBoundings.getWidth(), this.openBoxBoundings.getHeight());

            int y = location.getY();
            for(int i = 0; i < this.options.size(); i++) {

                T entry = this.options.get(i);

                Font defaultFont = g.getFont();

                if(this.selectedIndex == i) {
                    g.setFont(new Font("default", Font.BOLD, defaultFont.getSize()));
                }

                g.drawString(String.valueOf(entry), location.getX() + 2, y + height - 1);
                y += height;

                g.setFont(defaultFont);

            }

        } else if(!(this.options.isEmpty())) {

            if (this.background != null){
                g.setColor(this.background);
                g.fillRect(this.location.getX(), this.location.getY(), maxWidth + 4, height + 4);
            }

            if (this.foreground != null){
                g.setColor(this.foreground);
            }

            g.drawRect(this.location.getX(), this.location.getY(), maxWidth + 4, height + 4);
            g.drawString(String.valueOf(this.options.get(this.selectedIndex)), location.getX() + 2, (location.getY() + height - 1));

        }
    }

    @Override
    public boolean isActive() {
        return this.getGameScene().isActive() && this.isEnabled();
    }

    public void setBackground(Color background) {
        this.background = background;
    }

    public void setForeground(Color foreground) {
        this.foreground = foreground;
    }

    public List<T> getOptions() {
        return options;
    }

}
