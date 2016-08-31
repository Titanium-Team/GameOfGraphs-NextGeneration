package field;

import de.SweetCode.e.E;
import de.SweetCode.e.input.InputEntry;

import de.SweetCode.e.math.ILocation;
import de.SweetCode.e.rendering.GameScene;
import de.SweetCode.e.rendering.layers.Layers;
import field.buildings.Building;
import field.buildings.Buildings;
import field.recipe.RecipeResource;
import field.resource.Resource;
import game.GameOfGraphs;
import game.GraphDrawer;
import game.ui.*;
import game.ui.Button;
import graph.Edge;
import graph.Graph;
import graph.Vertex;
import org.omg.CORBA.INTERNAL;

import java.awt.*;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by boeschemeier on 08.06.2016.
 */
public class FieldView extends GameScene{

    private Vertex currentVertex = null;
    private Field currentField = null;
    private Graph graph;
    private DropDownMenu<Building> buildingDropDownMenu = new DropDownMenu<Building>(this, new ILocation(440, 510), new LinkedList<Building>() {{
        for( Building building : Buildings.values()){
            this.add(building);
        }
    }}, (value) -> {});

    private DropDownMenu<Integer> unitDropDownMenu = new DropDownMenu<Integer>(this, new ILocation(220, 530), new LinkedList<Integer>() {{
    }}, (value) -> {});


    private Button<String> buildButton = new Button<String>(this, "Build", new ILocation(540, 510),(value -> {

        if( Buildings.isBuildable(buildingDropDownMenu.getOption(), currentField)){

            Buildings.build(buildingDropDownMenu.getOption(), currentField);
        }

    }));

    public FieldView(){

        this.graph = GameOfGraphs.getGame().getGraphController().getGraph();
        //graph.addVertex(new Vertex("Test",300,300,GameOfGraphs.getGame().getFieldController().createField(null, false)));
        //graph.addVertex(new Vertex("Test2",500,200,GameOfGraphs.getGame().getFieldController().createField(null, true)));
        //graph.addEdge(new Edge(new String[] {"Test","Test2"}, 20));
        E.getE().addComponent(buildingDropDownMenu);
        E.getE().addComponent(buildButton);
        E.getE().addComponent(unitDropDownMenu);



    }

    @Override
    public void render(Layers layers) {

        Graphics2D g = layers.first().getGraphics2D();

        GraphDrawer.drawer(g,graph,"Field");

        g.setColor(Color.DARK_GRAY);
        g.fillRect(0,500,1280,220);
        g.setColor(Color.BLACK);
        g.drawLine(0,500,1280,500);
        g.drawLine(420, 500, 420, 720);
        g.setBackground(Color.WHITE);

        if(currentField == null) {
            g.drawString("Kein Field ausgewählt", 620, 600);
        }else{
            g.setColor(Color.ORANGE);
            g.drawString(String.valueOf("FERTILITY: " + currentField.getFertility()), 20, 520);
            g.setColor(Color.DARK_GRAY);
            g.drawString(String.valueOf("MOUNTAIN: " + currentField.getMountains()), 120, 520);
            g.setColor(Color.RED);
            g.drawString("UNITS: " + currentField.getUnits().size() + " (" + currentField.getUnmovedUnits().size() + ")", 220, 520);
            g.setColor(Color.BLUE);
            g.drawString(String.valueOf("SPECIAL: ") + currentField.getLocalResource().getName(), 320, 520);
            g.setColor(Color.GREEN);
            g.drawString(String.valueOf("FOREST: ") + currentField.getForestType(), 320, 540);
            g.setColor(Color.BLACK);

            this.buildingDropDownMenu.handleDraw(layers.first());
            this.buildButton.handleDraw(layers.first());
            this.unitDropDownMenu.handleDraw(layers.first());

            Map<Resource, Integer> resources = currentField.getResources();
            Map<Building, Integer> buildings = currentField.getBuildings();
            List<RecipeResource> recipeList = buildingDropDownMenu.getOption().getRecipe().getItemIngredients();

            final int[] y = {0};
            resources.forEach((resource, amount) -> {

                g.drawString(resource.getName() + ": " + amount, 20, 540 + y[0] * 20);

                for (RecipeResource recipe : recipeList){

                    if(recipe.getResource() == resource){

                        if(recipe.getAmount() <= amount){
                            g.setColor(Color.GREEN);
                        } else {
                            g.setColor(Color.RED);
                        }
                        g.drawString(" -" + recipe.getAmount(), 80, 540 + y[0] * 20);
                        g.setColor(Color.BLACK);
                    }

                }

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

        GraphDrawer.update(inputEntry,l);

        inputEntry.getMouseEntries().forEach(entry -> {

            if (entry.getPoint().getY() <= 500 && entry.getButton() == 1) {
                Vertex vertex = this.graph.getVertex((int) entry.getPoint().getX() + GraphDrawer.getHorizontal().getValue(), (int) entry.getPoint().getY() + GraphDrawer.getVertical().getValue());
                if (vertex != null) {
                    this.currentVertex = vertex;
                    this.currentField = this.currentVertex.getField();
                    this.unitDropDownMenu.setOptions(new LinkedList<Integer>() {{

                        for (int i = 0; i <= currentField.getUnmovedUnits().size(); i++) {
                            this.add(i);
                        }

                    }});
                } else {
                    this.currentVertex = null;
                    this.currentField = null;
                }
            }
        });

        if (this.currentField != null && this.currentVertex != null){
            if (this.unitDropDownMenu.getOption() > 0) {

                GameOfGraphs.getGame().getSimulationController().showMovementPossibilities(this.currentVertex);
            }
        }

    }

    @Override
    public boolean isActive() {
        return (E.getE().getScreen().getCurrent() == this);
    }

}
