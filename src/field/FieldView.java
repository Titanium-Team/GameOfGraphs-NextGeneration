package field;

import com.sun.org.apache.regexp.internal.RE;
import de.SweetCode.e.E;
import de.SweetCode.e.input.InputEntry;

import de.SweetCode.e.math.ILocation;
import de.SweetCode.e.rendering.GameScene;
import de.SweetCode.e.rendering.layers.Layers;
import field.buildings.Building;
import field.buildings.Buildings;
import field.resource.Resource;
import game.GameOfGraphs;
import game.GraphDrawer;
import game.ui.DropDownMenu;
import game.ui.UIComponent;
import graph.Graph;
import graph.Vertex;

import java.awt.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by boeschemeier on 08.06.2016.
 */
public class FieldView extends GameScene{

    private Field currentField = null;
    private Graph graph;
    private UIComponent dropDownMenu = new DropDownMenu<Building>(this, new ILocation(700, 600), new LinkedList<Building>() {{
        Buildings.values();
    }}, (value) -> {});

    public FieldView(){

        this.graph = new Graph();
        graph.addVertex(new Vertex("Test",300,300,GameOfGraphs.getGame().getFieldController().createField(null)));
        graph.addVertex(new Vertex("Test2",500,200,GameOfGraphs.getGame().getFieldController().createField(null)));
        //E.getE().addComponent(dropDownMenu);

    }

    @Override
    public void render(Layers layers) {

        Graphics2D g = layers.first().getGraphics2D();

        GraphDrawer.drawer(g,graph,"Field");

        g.setColor(Color.BLACK);
        g.drawLine(0,500,1280,500);
        g.drawLine(420, 500, 420, 720);
        g.setBackground(Color.WHITE);

        if(currentField == null) {
            g.drawString("Kein Field ausgewählt", 620, 600);
        }else{
            g.setColor(Color.GREEN);
            g.drawString(String.valueOf("FERTILITY: " + currentField.getFertility()), 20, 520);
            g.setColor(Color.DARK_GRAY);
            g.drawString(String.valueOf("MOUNTAIN: " + currentField.getMountains()), 120, 520);
            g.setColor(Color.RED);
            g.drawString("UNITS: " + currentField.getUnits().size() + " (" + currentField.getUnmovedUnits().size() + ")", 220, 520);
            g.setColor(Color.BLUE);
            g.drawString(String.valueOf("SPECIAL: ") + currentField.getLocalResource().getName(), 320, 520);
            g.setColor(Color.BLACK);

            //this.dropDownMenu.handleDraw(layers.first());

            Map<Resource, Integer> resources = currentField.getResources();
            Map<Building, Integer> buildings = currentField.getBuildings();

            final int[] y = {0};
            resources.forEach((resource, amount) -> {

                g.drawString(resource.getName() + ": " + amount, 20, 540 + y[0] * 20);
                y[0]++;
            });

            y[0] = 0;
            buildings.forEach((building, amount) -> {

                g.drawString(building.getName() + ": " + amount, 120, 540 + y[0] * 20);
                y[0]++;
            });



        }
    }

    @Override
    public void update(InputEntry inputEntry, long l) {

        inputEntry.getMouseEntries().forEach(entry -> {

            if(entry.getPoint().getY() <= 500 && entry.getButton() == 1) {
                Vertex vertex = this.graph.getVertex((int) entry.getPoint().getX(), (int) entry.getPoint().getY());
                if (vertex != null) {
                    this.currentField = vertex.getField();
                } else {
                    this.currentField = null;
                }
            }
        });

    }

    @Override
    public boolean isActive() {
        return (E.getE().getScreen().getCurrent() == this);
    }

}
