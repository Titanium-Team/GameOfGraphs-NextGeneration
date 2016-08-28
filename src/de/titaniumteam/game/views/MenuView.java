package de.titaniumteam.game.views;

import de.SweetCode.e.input.InputEntry;
import de.SweetCode.e.rendering.GameScene;
import de.SweetCode.e.rendering.layers.Layers;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.LinkedHashMap;
import java.util.Map;

public class MenuView extends GameScene {

    private int selectedOption = 0;
    private final Map<String, Class<? extends GameScene>> options = new LinkedHashMap<>();

    {
        this.options.put("Play", null);
        this.options.put("Map Editor", null);
        this.options.put("Exit", null);
    }

    public MenuView() {}

    @Override
    public void render(Layers layers) {

        Graphics2D layer = layers.first().getGraphics2D();

        //draw menu
        int x = 0;
        for(Map.Entry<String, Class<? extends GameScene>> entry : this.options.entrySet()) {

            layer.setColor(x == selectedOption ? Color.MAGENTA : Color.BLACK);
            layer.drawString(entry.getKey(), 640, 150 + x * 35);
            x++;

        }

    }

    @Override
    public void update(InputEntry inputEntry, long delta) {

        inputEntry.getKeyEntries().forEachOrdered(e -> {

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

        });

    }

    @Override
    public boolean isActive() {
        return true;
    }

}
