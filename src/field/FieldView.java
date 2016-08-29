package field;

import de.SweetCode.e.E;
import de.SweetCode.e.input.InputEntry;
import de.SweetCode.e.math.BoundingBox;
import de.SweetCode.e.math.Location;
import de.SweetCode.e.rendering.GameScene;
import de.SweetCode.e.rendering.layers.Layers;
import field.resource.Resource;
import game.GameOfGraphs;

import game.GraphDrawer;
import graph.Graph;
import graph.GraphController;


import graph.Vertex;

import java.awt.*;
import java.util.Map;

/**
 * Created by boeschemeier on 08.06.2016.
 */
public class FieldView extends GameScene{

    private Field currentField = null;
    private Graph graph;

    @Override
    public void render(Layers layers) {

        Graphics2D g = layers.first().getGraphics2D();

        this.graph = new Graph();
        graph.addVertex(new Vertex("Test",300,300,GameOfGraphs.getGame().getFieldController().createField(null)));

        GraphDrawer.drawer(g,graph,"Field");

        g.setColor(Color.BLACK);
        g.drawLine(0,500,1280,500);

        if(currentField == null) {
            g.drawString("Kein Field ausgewählt", 620, 600);
        }else{
            g.drawString(String.valueOf("FERTILITY: " + currentField.getFertility()), 20, 520);
            g.drawString(String.valueOf("MOUNTAIN: " + currentField.getMountains()), 70, 520);

            Map<Resource, Integer> resources = currentField.getResources();

            final int[] x = {0};
            final int[] y = {0};
            resources.forEach((resource, amount) -> {

                g.drawString(resource.getName() + ": " + amount, 20 + x[0] * 50, 540 + y[0] * 20);
                x[0]++;

                if (x[0] <= 3) {
                    x[0] = 0;
                    y[0]++;
                }
            });

        }
    }

    @Override
    public void update(InputEntry inputEntry, long l) {

        inputEntry.getMouseEntries().forEachOrdered(entry -> {

            Vertex vertex = this.graph.getVertex((int) entry.getPoint().getX(),(int) entry.getPoint().getY());

            if(vertex != null){
                this.currentField = vertex.getField();
            }else {
                this.currentField = null;
            }
        });

    }

    @Override
    public boolean isActive() {
        return (E.getE().getScreen().getCurrent() == this);
    }
}
