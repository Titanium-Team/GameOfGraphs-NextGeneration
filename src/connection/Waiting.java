package connection;

import de.SweetCode.e.E;
import de.SweetCode.e.input.InputEntry;
import de.SweetCode.e.rendering.GameScene;
import de.SweetCode.e.rendering.layers.Layers;
import field.FieldView;
import game.GameOfGraphs;
import game.MenuView;
import game.Player;
import graph.Graph;
import ki.KIFraction;
import mapEditor.MapEditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Waiting extends GameScene {
    private int selectedOption = 0;
    private final Map<String, Class<? extends GameScene>> options = new LinkedHashMap<>();
    private int left;

    {
        this.options.put("Waiting for 0 players.", null);
        this.options.put("Exit", null);
    }

    @Override
    public void render(Layers layers) {

        Graphics2D g = layers.first().getGraphics2D();
        g.setBackground(Color.WHITE);

        int x = 0;
        for(Map.Entry<String, Class<? extends GameScene>> entry : this.options.entrySet()) {
            if(x == this.selectedOption) {
                Image image = GameOfGraphs.getGame().getTextBuilder().toImage(entry.getKey(), 15);
                g.drawImage(image, 640 - image.getWidth(null) / 2, 150 + x * 35, null);
            } else {
                Image image = GameOfGraphs.getGame().getTextBuilder().toImage(entry.getKey(), 10);
                g.drawImage(image, 640 - image.getWidth(null) / 2, 150 + x * 35, null);
            }
            x++;
        }
    }

    @Override
    public void update(InputEntry inputEntry, long l) {
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
                if(this.selectedOption == 0) {

                } else if(this.selectedOption == 1) {
                    E.getE().show(MenuView.class);
                }
            }

        });

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            left = Connector.gameReady();
            if (Connector.gameStarted() || left == 0){
                JOptionPane.showMessageDialog(null, "You are player: " + Connector.getMyPlayer().getName());

                Connector.setEnabledMutiplayer(true);


                GameOfGraphs.getGame().setCurrentPlayer(Connector.getCurrentPlayer());

                E.getE().show(FieldView.class);
            }
        },0,500, TimeUnit.MILLISECONDS);
    }

    @Override
    public boolean isActive() {
        return E.getE().getScreen().getCurrent() == this;
    }
}
