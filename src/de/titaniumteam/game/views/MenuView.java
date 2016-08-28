package de.titaniumteam.game.views;

import de.SweetCode.e.input.InputEntry;
import de.SweetCode.e.rendering.GameScene;
import de.SweetCode.e.rendering.layers.Layers;

import java.awt.*;
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

            layer.drawString(entry.getKey(), 640, 150 + x * 35);
            x++;

        }

    }

    @Override
    public void update(InputEntry inputEntry, long l) {

    }

    @Override
    public boolean isActive() {
        return true;
    }

}
