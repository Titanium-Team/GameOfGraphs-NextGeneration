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
import game.ui.Button;
import game.ui.DropDownMenu;
import graph.Graph;
import graph.Vertex;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by boeschemeier on 08.06.2016.
 */
public class FieldView extends GameScene{

    private Vertex currentVertex = null;
    private Field currentField = null;
    private Graph graph;
    private boolean move = false;
    private List<Vertex> marked = new ArrayList<>();
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

    private Button<String> nextTurnButton = new Button<String>(this, "Next Turn", new ILocation(1100, 700),(value -> {

        GameOfGraphs.getGame().nextTurn();

    }));

    public FieldView(){

        this.graph = GameOfGraphs.getGame().getGraphController().getGraph();

        E.getE().addComponent(buildingDropDownMenu);
        E.getE().addComponent(buildButton);
        E.getE().addComponent(unitDropDownMenu);
        E.getE().addComponent(nextTurnButton);



    }

    @Override
    public void render(Layers layers) {

        Graphics2D g = layers.first().getGraphics2D();


        GraphDrawer.drawer(g,graph,"Field");

        g.drawString("Player: " + GameOfGraphs.getGame().getCurrentPlayer().getName(), 50, 50);

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
            g.setColor(Color.LIGHT_GRAY);
            g.drawString(String.valueOf("MOUNTAIN: " + currentField.getMountains()), 120, 520);
            g.setColor(Color.RED);
            g.drawString("UNITS: " + currentField.getUnits().size() + " (" + currentField.getUnmovedUnits().size() + ")", 220, 520);
            g.setColor(Color.BLUE);
            g.drawString(String.valueOf("SPECIAL: ") + currentField.getLocalResource().getName(), 320, 520);
            g.setColor(Color.GREEN);
            g.drawString(String.valueOf("FOREST: ") + currentField.getForestType(), 320, 540);
            g.setColor(Color.BLACK);
            g.drawString("OWNER: " + this.currentField.getPlayer().getName(), 20, 700);

            g.setColor(Color.LIGHT_GRAY);
            this.buildingDropDownMenu.handleDraw(layers.first());
            this.buildButton.handleDraw(layers.first());
            this.unitDropDownMenu.handleDraw(layers.first());
            this.nextTurnButton.handleDraw(layers.first());
            g.setColor(Color.BLACK);

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

            if (entry.getPoint().getY() <= 475 && entry.getPoint().getX() <= 1255 && entry.getButton() == 1 && move != true) {
                Vertex vertex = this.graph.getVertex((int) entry.getPoint().getX() + GraphDrawer.getHorizontal().getValue(), (int) entry.getPoint().getY() + GraphDrawer.getVertical().getValue());
                if (vertex != null) {
                    if(currentVertex != null) {
                        this.currentVertex.setMarkTarget(false);
                    }
                    this.currentVertex = vertex;
                    this.currentVertex.setMarkStart(true);
                    this.currentField = this.currentVertex.getField();
                    this.unitDropDownMenu.setOptions(new LinkedList<Integer>() {{

                        for (int i = 0; i <= currentField.getUnmovedUnits().size(); i++) {
                            this.add(i);
                        }

                    }});
                } else {
                    if(currentVertex != null) {
                        this.currentVertex.setMarkTarget(false);
                    }
                    this.currentVertex = null;
                    this.currentField = null;
                }
            } else if(move == true){

                Vertex vertex = this.graph.getVertex((int) entry.getPoint().getX() + GraphDrawer.getHorizontal().getValue(), (int) entry.getPoint().getY() + GraphDrawer.getVertical().getValue());
                if(vertex != null) {
                    if (vertex.isMarkTarget()) {
                        GameOfGraphs.getGame().getSimulationController().moveUnits(this.currentVertex, vertex, this.unitDropDownMenu.getOption());
                    }
                }
                this.unitDropDownMenu.setSelectedIndex(0);

            }
        });

        if (this.currentField != null && this.currentVertex != null){
            if (this.unitDropDownMenu.getOption() > 0) {

                marked = GameOfGraphs.getGame().getSimulationController().showMovementPossibilities(this.currentVertex);
                move = true;
            } else {
                if(move == true){
                    for (Vertex vertex : marked){
                        vertex.setMarkTarget(false);
                    }
                }
                move = false;
            }
        }


    }

    @Override
    public boolean isActive() {
        return (E.getE().getScreen().getCurrent() == this);
    }

}
