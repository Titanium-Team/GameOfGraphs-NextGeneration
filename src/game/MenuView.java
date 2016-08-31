package game;

import de.SweetCode.e.E;
import de.SweetCode.e.input.InputEntry;
import de.SweetCode.e.math.ILocation;
import de.SweetCode.e.rendering.GameScene;
import de.SweetCode.e.rendering.layers.Layers;
import field.FieldView;
import game.ui.CheckBox;
import game.ui.DropDownMenu;
import game.ui.UIComponent;
import ki.TradeView;
import mapEditor.MapEditorView;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

public class MenuView extends GameScene {

    private int selectedOption = 0;
    private UIComponent checkBox = new CheckBox(this, new ILocation(300, 300), 20, 20, (value) -> { System.out.println(value); });
    private UIComponent dropDownMenu = new DropDownMenu<String>(this, new ILocation(300, 350), new LinkedList<String>() {{
        this.add("ABCEFGH");
        this.add("IJKLMNO");
        this.add("PQRSTUV");
    }}, (value) -> {});

    private final Map<String, Class<? extends GameScene>> options = new LinkedHashMap<>();

    {
        this.options.put("Play", FieldView.class);
        this.options.put("Map Editor", MapEditorView.class);
        this.options.put("Exit", null);
    }

    public MenuView() {
        E.getE().addComponent(checkBox);
        E.getE().addComponent(dropDownMenu);
    }

    @Override
    public void render(Layers layers) {

        Graphics2D g = layers.first().getGraphics2D();
        g.setBackground(Color.WHITE);

        this.checkBox.handleDraw(layers.first());
        this.dropDownMenu.handleDraw(layers.first());

        TradeView.drawer(200,300,g,null,null);

        //draw menu
        int x = 0;
        for(Map.Entry<String, Class<? extends GameScene>> entry : this.options.entrySet()) {

            if(x == this.selectedOption) {
                Image image = GameOfGraphs.getGame().getTextBuilder().toImage(entry.getKey(), 15);
                g.drawImage(image, 480 - image.getWidth(null) / 2, 150 + x * 35, null);
            } else {
                Image image = GameOfGraphs.getGame().getTextBuilder().toImage(entry.getKey(), 10);
                g.drawImage(image, 480 - image.getWidth(null) / 2, 150 + x * 35, null);
            }
            x++;

        }

    }

    @Override
    public void update(InputEntry inputEntry, long delta) {

        inputEntry.getKeyEntries().forEach(e -> {

            if(e.getKeyCode() == KeyEvent.VK_W) {
                this.selectedOption--;

                if(this.selectedOption < 0) {
                    this.selectedOption = 0;
                }
            }

            if(e.getKeyCode() == KeyEvent.VK_S) {
                this.selectedOption++;

                if(this.selectedOption >= this.options.size()) {
                    this.selectedOption = this.options.size() - 1;
                }
            }

            if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                Class<?> clazz = this.options.get(this.options.keySet().toArray(new String[this.options.size()])[this.selectedOption]);

                if(clazz == null) {
                    System.exit(1);
                    return;
                }

                E.getE().show(clazz);

            }

        });

    }

    @Override
    public boolean isActive() {
        return E.getE().getScreen().getCurrent() == this;
    }

}
