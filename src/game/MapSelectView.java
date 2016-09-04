package game;

import de.SweetCode.e.E;
import de.SweetCode.e.input.InputEntry;
import de.SweetCode.e.rendering.GameScene;
import de.SweetCode.e.rendering.layers.Layers;
import field.FieldView;
import graph.Graph;
import ki.KIFraction;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.LinkedHashMap;
import java.util.Map;

public class MapSelectView extends GameScene {

    private int selectedOption = 0;
    private final Map<String, Integer> options = new LinkedHashMap<>();

    {
        this.options.put("Play", 0);
        this.options.put("Select Map", 1);
    }



    public MapSelectView() {}

    @Override
    public void render(Layers layers) {

        Graphics2D g = layers.first().getGraphics2D();
        g.setBackground(Color.WHITE);

        //draw menu
        int x = 0;
        for(Map.Entry<String, Integer> entry : this.options.entrySet()) {

            boolean gray = GameOfGraphs.getGame().getGraphController().getGraph() == null && entry.getKey().equalsIgnoreCase("Play");

            if(x == this.selectedOption) {
                Image image = GameOfGraphs.getGame().getTextBuilder().toImage(entry.getKey(), 15, gray);
                g.drawImage(image, 640 - image.getWidth(null) / 2, 150 + x * 35, null);
            } else {
                Image image = GameOfGraphs.getGame().getTextBuilder().toImage(entry.getKey(), 10, gray);
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
                int clazz = this.options.get(this.options.keySet().toArray(new String[this.options.size()])[this.selectedOption]);


                switch (clazz) {

                    case 0:

                        if(GameOfGraphs.getGame().getGraphController().getGraph() == null) {
                            return;
                        }

                        E.getE().show(FieldView.class);
                        if(GameOfGraphs.getGame().getCurrentPlayer() instanceof KIFraction) {
                            GameOfGraphs.getGame().nextTurn();
                        }

                        break;

                    case 1:

                        Object[] graph = GameOfGraphs.getGame().getGraphController().load();

                        if(!(graph == null)) {
                            GameOfGraphs.getGame().getGraphController().setGraph((Graph) graph[0], true);
                        }

                        break;

                }


            }

            if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                E.getE().show(MenuView.class);
            }

        });
    }

    @Override
    public boolean isActive() {
        return E.getE().getScreen().getCurrent() == this;
    }

}
