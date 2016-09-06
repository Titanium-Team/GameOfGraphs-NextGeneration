package game;

import connection.Connector;
import connection.Waiting;
import de.SweetCode.e.E;
import de.SweetCode.e.input.InputEntry;
import de.SweetCode.e.rendering.GameScene;
import de.SweetCode.e.rendering.layers.Layers;
import field.FieldView;
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

public class MenuView extends GameScene {

    private int selectedOption = 0;
    private final Map<String, Class<? extends GameScene>> options = new LinkedHashMap<>();

    {
        this.options.put("Play", null);
        this.options.put("Map Editor", MapEditor.class);
        this.options.put("Multiplayer", null);
        this.options.put("Exit", null);
    }

    public MenuView() {}

    @Override
    public void render(Layers layers) {

        Graphics2D g = layers.first().getGraphics2D();
        g.setBackground(Color.WHITE);

        //draw menu
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

                if(this.selectedOption == 0) {
                    Object[] graph = GameOfGraphs.getGame().getGraphController().load();

                    if(!(graph == null)) {
                        GameOfGraphs.getGame().getGraphController().setGraph((Graph) graph[0], true);

                        E.getE().show(FieldView.class);
                        if(GameOfGraphs.getGame().getCurrentPlayer() instanceof KIFraction) {
                            GameOfGraphs.getGame().nextTurn();
                        }
                    }
                } else if(this.selectedOption == 1) {
                    GameOfGraphs.getGame().getGraphController().setGraph(new Graph(), false);
                    E.getE().show(clazz);
                } else if(this.selectedOption == 3) {
                    System.exit(1);
                }else if(this.selectedOption == 2){
                    if (Connector.isHost()){
                        Object[] g = GameOfGraphs.getGame().getGraphController().load();
                        if (g != null){
                            GameOfGraphs.getGame().getGraphController().setGraph((Graph) g[0], true);
                        }

                        Connector.createGame(GameOfGraphs.getGame().getGraphController().getGraph());
                    }else {
                        GameOfGraphs.getGame().getGraphController().setGraph(Connector.getGraph(), true);
                    }


                    Player player = Connector.unusedPlayers().get(0);
                    Connector.joinGame(player);
                    Connector.nextTurn(player.getName());

                    E.getE().show(Waiting.class);
                }

            }

        });
    }

    @Override
    public boolean isActive() {
        return E.getE().getScreen().getCurrent() == this;
    }

}
