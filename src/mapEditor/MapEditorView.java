package mapEditor;

import de.SweetCode.e.E;
import de.SweetCode.e.input.InputEntry;
import de.SweetCode.e.math.IBoundingBox;
import de.SweetCode.e.math.ILocation;
import de.SweetCode.e.math.Location;
import de.SweetCode.e.rendering.GameScene;
import de.SweetCode.e.rendering.layers.Layers;
import field.Field;
import field.buildings.Building;
import field.buildings.Buildings;
import field.recipe.RecipeResource;
import field.resource.Resource;
import field.resource.Resources;
import game.GameOfGraphs;
import game.GraphDrawer;
import game.Player;
import game.sprite.Textures;
import game.ui.*;
import graph.Edge;
import graph.Graph;
import graph.Vector;
import graph.Vertex;
import simulation.Unit;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;

public class MapEditorView extends GameScene{
    private Graph graph;
    private static Vertex[] dragEdge;

    private Vertex temp;

    private int chooser = 0;

    private boolean leftMouse = false;

    private Button addVertex, addEdge, save, load, move, check;
    private Field currentField;
    private boolean question = false;

    private ArrayList<game.ui.EditText<String>> editTexts;
    private DropDownMenu<Resources> specialResources;
    private DropDownMenu<Integer> forest;
    private DropDownMenu<Player> owner;

    public MapEditorView() {
        graph = GameOfGraphs.getGame().getGraphController().getGraph();

        addVertex = new Button(5, 505, 310, 100, Color.WHITE, Color.BLACK, "Add Vertex");
        addEdge = new Button(320, 505, 310, 100, Color.WHITE, Color.BLACK, "Add Edge");
        save = new Button(950, 505, 310, 48, Color.WHITE, Color.BLACK, "Save");
        load = new Button(950, 557, 310, 48, Color.WHITE, Color.BLACK, "Load");
        move = new Button(950, 615, 310, 48, Color.WHITE, Color.BLACK, "Move");
        check = new Button(950, 667, 310, 48, Color.WHITE, Color.BLACK, "Check");

        E.getE().addComponent(this.questionButton);

        editTexts = new ArrayList<>();
        editTexts.add(new game.ui.EditText<String>(this, "0", new IBoundingBox(new Location(85, 505), new Location(100, 525)), true, false, 1, (component, value) -> {
            currentField.setFertility(Integer.parseInt(value.equals("") ? "0" : value));
        }));
        editTexts.add(new game.ui.EditText<String>(this, "0", new IBoundingBox(new Location(190, 505), new Location(205, 525)), true, false, 1, (component, value) -> {
            currentField.setMountains(Integer.parseInt(value.equals("") ? "0" : value));
        }));
        editTexts.add(new game.ui.EditText<String>(this, "0", new IBoundingBox(new Location(265, 505), new Location(290, 525)), true, false, 2, (component, value) -> {
            ArrayList<Unit> units = new ArrayList<Unit>();
            for (int i = 0; i < Integer.parseInt(value.equals("") ? "0" : value); i++){
                units.add(new Unit(currentField.getPlayer()));
            }
            currentField.setUnits(units);
        }));

        editTexts.add(new game.ui.EditText<String>(this, "0", new IBoundingBox(new Location(65, 525), new Location(80, 545)), true, false, 1, (component, value) -> {
            currentField.getResources().put(Resources.POPULATION, (Integer.parseInt(value.equals("") ? "0" : value)));
        }));
        editTexts.add(new game.ui.EditText<String>(this, "0", new IBoundingBox(new Location(55, 545), new Location(70, 565)), true, false, 1, (component, value) -> {
            currentField.getResources().put(Resources.FOOD, (Integer.parseInt(value.equals("") ? "0" : value)));
        }));
        editTexts.add(new game.ui.EditText<String>(this, "0", new IBoundingBox(new Location(60, 565), new Location(75, 585)), true, false, 1, (component, value) -> {
            currentField.getResources().put(Resources.STONE, (Integer.parseInt(value.equals("") ? "0" : value)));
        }));
        editTexts.add(new game.ui.EditText<String>(this, "0", new IBoundingBox(new Location(60, 585), new Location(75, 605)), true, false, 1, (component, value) -> {
            currentField.getResources().put(Resources.WOOD, (Integer.parseInt(value.equals("") ? "0" : value)));
        }));
        editTexts.add(new game.ui.EditText<String>(this, "0", new IBoundingBox(new Location(65, 605), new Location(80, 625)), true, false, 1, (component, value) -> {
            currentField.getResources().put(Resources.WHEAT, (Integer.parseInt(value.equals("") ? "0" : value)));
        }));
        editTexts.add(new game.ui.EditText<String>(this, "0", new IBoundingBox(new Location(55, 625), new Location(70, 645)), true, false, 1, (component, value) -> {
            currentField.getResources().put(Resources.TREE, (Integer.parseInt(value.equals("") ? "0" : value)));
        }));
        editTexts.add(new game.ui.EditText<String>(this, "0", new IBoundingBox(new Location(50, 645), new Location(65, 665)), true, false, 1, (component, value) -> {
            currentField.getResources().put(Resources.IRON, (Integer.parseInt(value.equals("") ? "0" : value)));
        }));
        editTexts.add(new game.ui.EditText<String>(this, "0", new IBoundingBox(new Location(55, 665), new Location(70, 685)), true, false, 1, (component, value) -> {
            currentField.getResources().put(Resources.GOLD, (Integer.parseInt(value.equals("") ? "0" : value)));
        }));


        editTexts.add(new game.ui.EditText<String>(this, "0", new IBoundingBox(new Location(160, 525), new Location(175, 545)), true, false, 1, (component, value) -> {
            currentField.getBuildings().put(Buildings.MINE, (Integer.parseInt(value.equals("") ? "0" : value)));
        }));
        editTexts.add(new game.ui.EditText<String>(this, "0", new IBoundingBox(new Location(160, 545), new Location(175, 565)), true, false, 1, (component, value) -> {
            currentField.getBuildings().put(Buildings.FARM, (Integer.parseInt(value.equals("") ? "0" : value)));
        }));
        editTexts.add(new game.ui.EditText<String>(this, "0", new IBoundingBox(new Location(180, 565), new Location(195, 585)), true, false, 1, (component, value) -> {
            currentField.getBuildings().put(Buildings.WINDMILL, (Integer.parseInt(value.equals("") ? "0" : value)));
        }));
        editTexts.add(new game.ui.EditText<String>(this, "0", new IBoundingBox(new Location(170, 585), new Location(185, 605)), true, false, 1, (component, value) -> {
            currentField.getBuildings().put(Buildings.WOODCUTTER, (Integer.parseInt(value.equals("") ? "0" : value)));
        }));
        editTexts.add(new game.ui.EditText<String>(this, "0", new IBoundingBox(new Location(165, 605), new Location(180, 625)), true, false, 1, (component, value) -> {
            currentField.getBuildings().put(Buildings.BAZAAR, (Integer.parseInt(value.equals("") ? "0" : value)));
        }));
        editTexts.add(new game.ui.EditText<String>(this, "0", new IBoundingBox(new Location(190, 625), new Location(205, 645)), true, false, 1, (component, value) -> {
            currentField.getBuildings().put(Buildings.SLAVE_MARKET, (Integer.parseInt(value.equals("") ? "0" : value)));
        }));
        editTexts.add(new game.ui.EditText<String>(this, "0", new IBoundingBox(new Location(190, 645), new Location(205, 665)), true, false, 1, (component, value) -> {
            currentField.getBuildings().put(Buildings.MARKETPLACE, (Integer.parseInt(value.equals("") ? "0" : value)));
        }));

        for (int i = 0; i < editTexts.size(); i++){
            if (i == 0){
                editTexts.get(i).setTextColor(Color.ORANGE);
                editTexts.get(i).setLineColor(Color.ORANGE);
            }else if (i == 2){
                editTexts.get(i).setTextColor(Color.RED);
                editTexts.get(i).setLineColor(Color.RED);
            }else {
                editTexts.get(i).setTextColor(Color.LIGHT_GRAY);
                editTexts.get(i).setLineColor(Color.LIGHT_GRAY);
            }

            E.getE().addComponent(editTexts.get(i));
        }

        specialResources = new DropDownMenu<Resources>(this, new ILocation(375, 505), new LinkedList<Resources>(){{
            for(Resources resources : Resources.values()){
                if (resources.getName().equals("Iron") || resources.getName().equals("Gold") || resources.getName().equals("Wheat") || resources.getName().equals("Tree")) {
                    this.add(resources);
                }
            }
        }}, (component, value) -> {
            currentField.setLocalResource(value);
        });
        specialResources.setBackground(Color.DARK_GRAY);
        specialResources.setForeground(Color.BLUE);
        E.getE().addComponent(specialResources);

        forest = new DropDownMenu<Integer>(this, new ILocation(375, 525), new LinkedList<Integer>(){{
            add(1);
            add(2);
            add(3);
        }}, (component, value) -> {
            currentField.setForestType(value);
        });
        forest.setBackground(Color.DARK_GRAY);
        forest.setForeground(Color.GREEN);
        E.getE().addComponent(forest);

        owner = new DropDownMenu<Player>(this, new ILocation(375, 545), new LinkedList<Player>(){{
            for (Player player:GameOfGraphs.getGame().getPlayers()){
                add(player);
            }
        }}, (component, value) -> {
            currentField.setPlayer(value);
        });
        owner.setBackground(Color.DARK_GRAY);
        owner.setForeground(Color.BLACK);
        E.getE().addComponent(owner);
    }

    private game.ui.Button<String> questionButton = new game.ui.Button<>(this, "?", new ILocation(1235, 10), (ui, value) -> {

        JOptionPane.showMessageDialog(null, null, "Help", 0, new ImageIcon(Textures.HELP_POPUP.getImage()));
        question = true;
    });

    @Override
    public void render(Layers layers) {
        Graphics2D g = layers.first().getGraphics2D();

        GraphDrawer.drawer(g, graph, "MapEditor");

        g.setColor(Color.BLACK);
        g.fillRect(0, 500, 1280, 220);

        g.setColor(Color.DARK_GRAY);
        g.fillRect(0,500,1280,220);
        g.setColor(Color.BLACK);
        g.drawLine(0,500,1280,500);
        //g.drawLine(420, 500, 420, 720);
        g.drawLine(700, 500, 700, 720);
        g.setBackground(Color.WHITE);

        if(currentField == null) {
            g.drawString("No field selected.", 520, 600);

            for (game.ui.EditText<String> text : editTexts){
                text.setEnabled(false);
            }
            specialResources.setEnabled(false);
            forest.setEnabled(false);
            owner.setEnabled(false);

        }else{
            //Zeichnen der Statistiken
            g.setColor(Color.ORANGE);
            g.drawString(String.valueOf("FERTILITY: "), 20, 520);
            g.setColor(Color.LIGHT_GRAY);
            g.drawString(String.valueOf("MOUNTAIN: "), 120, 520);
            g.setColor(Color.RED);
            g.drawString("UNITS: ", 220, 520);
            g.setColor(Color.BLUE);
            g.drawString(String.valueOf("SPECIAL: "), 320, 520);
            g.setColor(Color.GREEN);
            g.drawString(String.valueOf("FOREST: "), 320, 540);
            g.setColor(Color.BLACK);
            g.drawString("OWNER: ", 320, 560);

            for (game.ui.EditText<String> text : editTexts){
                text.setEnabled(true);
            }
            specialResources.setEnabled(true);
            forest.setEnabled(true);
            owner.setEnabled(true);

            g.setColor(Color.LIGHT_GRAY);

            //Zeichnen der Resourcen und Gebäude
            Map<Resources, Integer> resources = currentField.getResources();
            Map<Buildings, Integer> buildings = currentField.getBuildings();

            final int[] y = {0};
            resources.forEach((resource, amount) -> {

                g.drawString(resource.getName() + ": ", 20, 540 + y[0] * 20);

                y[0]++;
            });


            y[0] = 0;
            buildings.forEach((building, amount) -> {

                g.drawString(building.getName() + ": ", 120, 540 + y[0] * 20);
                y[0]++;
            });

        }

        /*addVertex.draw(g);
        addEdge.draw(g);*/
        save.draw(g);
        load.draw(g);
        move.draw(g);
        check.draw(g);
    }

    @Override
    public void update(InputEntry inputEntry, long l) {
        GraphDrawer.update(inputEntry, l);

        inputEntry.getMouseEntries().forEach(mouseEntry -> {
            if (!(mouseEntry.getPoint().getX() >= 1280-25 && mouseEntry.getPoint().getX() <= 1280 || mouseEntry.getPoint().getY() >= 500-25 && mouseEntry.getPoint().getY() <= 500)) {
                if (mouseEntry.getPoint().getX() >= 0 && mouseEntry.getPoint().getX() <= 1280 && mouseEntry.getPoint().getY() >= 500 && mouseEntry.getPoint().getY() <= 720) {
                    if (addVertex.isPushed(mouseEntry.getPoint())) {
                        chooser = 0;
                    } else if (addEdge.isPushed(mouseEntry.getPoint())) {
                        chooser = 1;
                    } else if (save.isPushed(mouseEntry.getPoint())) {
                        GameOfGraphs.getGame().getGraphController().save(graph);
                    } else if (load.isPushed(mouseEntry.getPoint())) {
                        graph = GameOfGraphs.getGame().getGraphController().load();
                    } else if (check.isPushed(mouseEntry.getPoint())) {
                        GameOfGraphs.getGame().getGraphController().checkGraph();
                    } else if (move.isPushed(mouseEntry.getPoint())) {
                        chooser = 2;
                    }

                } else {
                    if (!question) {
                        final Vertex vertex = graph.getVertex((int) ((mouseEntry.getPoint().getX() + GraphDrawer.getHorizontal().getValue()) / GraphDrawer.getZoom()), (int) ((mouseEntry.getPoint().getY() + GraphDrawer.getVertical().getValue()) / GraphDrawer.getZoom()));
                        if (mouseEntry.getButton() == 1) {
                            leftMouse = true;
                            if (vertex != null) {
                                currentField = vertex.getField();

                                for(int i = 0; i < owner.getOptions().size(); i++){
                                    if (currentField.getPlayer().getName().equals(owner.getOptions().get(i).getName())){
                                        owner.setSelectedIndex(i);
                                    }
                                }

                                forest.setSelectedIndex(currentField.getForestType()-1);

                                switch (currentField.getLocalResource().getName()){
                                    case "Wheat":
                                        specialResources.setSelectedIndex(0);
                                        break;
                                    case "Tree":
                                        specialResources.setSelectedIndex(1);
                                        break;
                                    case "Iron":
                                        specialResources.setSelectedIndex(2);
                                        break;
                                    case "Gold":
                                        specialResources.setSelectedIndex(3);
                                        break;
                                }

                                for (int i = 0; i < editTexts.size(); i++){
                                    switch (i){
                                        case 0:
                                            editTexts.get(i).setText(String.valueOf(currentField.getFertility()));
                                            break;
                                        case 1:
                                            editTexts.get(i).setText(String.valueOf(currentField.getMountains()));
                                            break;
                                        case 2:
                                            editTexts.get(i).setText(String.valueOf(currentField.getUnits().size()));
                                            break;
                                        case 3:
                                            editTexts.get(i).setText(String.valueOf(currentField.getResources().get(Resources.POPULATION)));
                                            break;
                                        case 4:
                                            editTexts.get(i).setText(String.valueOf(currentField.getResources().get(Resources.FOOD)));
                                            break;
                                        case 5:
                                            editTexts.get(i).setText(String.valueOf(currentField.getResources().get(Resources.STONE)));
                                            break;
                                        case 6:
                                            editTexts.get(i).setText(String.valueOf(currentField.getResources().get(Resources.WOOD)));
                                            break;
                                        case 7:
                                            editTexts.get(i).setText(String.valueOf(currentField.getResources().get(Resources.WHEAT)));
                                            break;
                                        case 8:
                                            editTexts.get(i).setText(String.valueOf(currentField.getResources().get(Resources.TREE)));
                                            break;
                                        case 9:
                                            editTexts.get(i).setText(String.valueOf(currentField.getResources().get(Resources.IRON)));
                                            break;
                                        case 10:
                                            editTexts.get(i).setText(String.valueOf(currentField.getResources().get(Resources.GOLD)));
                                            break;
                                        case 11:
                                            editTexts.get(i).setText(String.valueOf(currentField.getBuildings().get(Buildings.MINE)));
                                            break;
                                        case 12:
                                            editTexts.get(i).setText(String.valueOf(currentField.getBuildings().get(Buildings.FARM)));
                                            break;
                                        case 13:
                                            editTexts.get(i).setText(String.valueOf(currentField.getBuildings().get(Buildings.WINDMILL)));
                                            break;
                                        case 14:
                                            editTexts.get(i).setText(String.valueOf(currentField.getBuildings().get(Buildings.WOODCUTTER)));
                                            break;
                                        case 15:
                                            editTexts.get(i).setText(String.valueOf(currentField.getBuildings().get(Buildings.BAZAAR)));
                                            break;
                                        case 16:
                                            editTexts.get(i).setText(String.valueOf(currentField.getBuildings().get(Buildings.SLAVE_MARKET)));
                                            break;
                                        case 17:
                                            editTexts.get(i).setText(String.valueOf(currentField.getBuildings().get(Buildings.MARKETPLACE)));
                                            break;
                                    }
                                }
                            }
                            switch (chooser) {
                                case 0:
                                    if (vertex != null) {
                                        temp = vertex;
                                    } else {
                                        ArrayList<Vertex> vertexList = graph.getVertices();
                                        graph.addVertex(new Vertex(String.valueOf(vertexList.size() + 1), (int) ((mouseEntry.getPoint().getX() - GraphDrawer.getHorizontal().getValue()) / GraphDrawer.getZoom()), (int) ((mouseEntry.getPoint().getY() - GraphDrawer.getVertical().getValue()) / GraphDrawer.getZoom()), GameOfGraphs.getGame().getFieldController().createField(GameOfGraphs.getGame().getPlayers().get(0), true)));
                                    }
                                    break;
                                case 1:
                                    if (vertex != null) {
                                        temp = vertex;
                                        dragEdge = new Vertex[]{new Vertex("equals", vertex.getX(), vertex.getY(), null), new Vertex("equals", vertex.getX(), vertex.getY(), null)};
                                    }
                                    break;
                            }
                        }
                    }
                    question = false;
                }
            }
        });
        inputEntry.getMouseReleasedQueue().forEach(mouseEntry -> {
            leftMouse = false;
            if (mouseEntry.getButton()==1) {
                switch (chooser) {
                    case 0:
                        if (temp != null) {
                            temp = null;
                        }
                        break;
                    case 1:
                        Vertex vertex = graph.getVertex((int) ((mouseEntry.getPoint().getX() + GraphDrawer.getHorizontal().getValue()) / GraphDrawer.getZoom()), (int) ((mouseEntry.getPoint().getY() + GraphDrawer.getVertical().getValue()) / GraphDrawer.getZoom()));
                        if (vertex != null && temp != null) {
                            graph.addEdge(new Edge(new String[]{temp.getID(), vertex.getID()}, 0));
                        }
                        dragEdge = null;
                        temp = null;
                        break;
                }
            }
        });
        inputEntry.getMouseDraggedEntries().forEach(mouseEntry -> {if (leftMouse) {
            switch (chooser) {
                case 0:
                    if (temp != null) {
                        double x = (mouseEntry.getPoint().getX() + GraphDrawer.getHorizontal().getValue()) / GraphDrawer.getZoom();
                        double y = (mouseEntry.getPoint().getY() + GraphDrawer.getVertical().getValue()) / GraphDrawer.getZoom();
                        if (mouseEntry.getPoint().getX() - graph.getRadius() < 0) {
                            x = GraphDrawer.getHorizontal().getValue() / GraphDrawer.getZoom() + graph.getRadius();
                        } else if (mouseEntry.getPoint().getX() + graph.getRadius() > 1280 - 25) {
                            x = (1280 - 25 + GraphDrawer.getHorizontal().getValue()) / GraphDrawer.getZoom() - graph.getRadius();
                        }
                        if (mouseEntry.getPoint().getY() - graph.getRadius() < 0) {
                            y = GraphDrawer.getVertical().getValue() / GraphDrawer.getZoom() + graph.getRadius();
                        } else if (mouseEntry.getPoint().getY() + graph.getRadius() > 500 - 25) {
                            y = (500 - 25 + GraphDrawer.getVertical().getValue()) / GraphDrawer.getZoom() - graph.getRadius();
                        }
                        ArrayList<Vertex> vertexNear = graph.getVertexList(temp, (int) x, (int) y, graph.getRadius() * 3);
                        if (vertexNear.isEmpty()) {
                            temp.setPosition((int) x, (int) y);
                        } else {
                            int iTemp = 0, xTemp = 0, yTemp = 0;
                            for (Vertex vertex : vertexNear) {
                                iTemp++;
                                xTemp += vertex.getX();
                                yTemp += vertex.getY();
                            }
                            xTemp = xTemp / iTemp;
                            yTemp = yTemp / iTemp;
                            Vector vector = new Vector(x - xTemp, y - yTemp);
                            vector.normalize();
                            vector.setX(vector.getX() * graph.getRadius() * 3);
                            vector.setY(vector.getY() * graph.getRadius() * 3);
                            Vertex vertex1 = graph.getVertex(temp, (int) x, (int) y, graph.getRadius() * 3);
                            Vector vector1 = new Vector(vertex1.getX() - vector.getX(), vertex1.getY() - vector.getY());
                            vector1.setX(vertex1.getX() - vector1.getX() + vertex1.getX());
                            vector1.setY(vertex1.getY() - vector1.getY() + vertex1.getY());
                            if (vector1.getX() - graph.getRadius() < 0) {
                                vector1.setX(GraphDrawer.getHorizontal().getValue() / GraphDrawer.getZoom() + graph.getRadius());
                            } else if (vector1.getX() + graph.getRadius() > 1280 - 25) {
                                vector1.setX((1280 - 25 + GraphDrawer.getHorizontal().getValue()) / GraphDrawer.getZoom() - graph.getRadius());
                            }
                            if (vector1.getY() - graph.getRadius() < 0) {
                                vector1.setY(GraphDrawer.getVertical().getValue() / GraphDrawer.getZoom() + graph.getRadius());
                            } else if (vector1.getY() + graph.getRadius() > 500 - 25) {
                                vector1.setY((500 - 25 + GraphDrawer.getVertical().getValue()) / GraphDrawer.getZoom() - graph.getRadius());
                            }
                            temp.setPosition((int) vector1.getX(), (int) vector1.getY());
                        }
                    }
                    break;
                case 1:
                    if (dragEdge != null) {
                        dragEdge[1].setPosition((int) ((mouseEntry.getPoint().getX() + GraphDrawer.getHorizontal().getValue()) / GraphDrawer.getZoom()), (int) ((mouseEntry.getPoint().getY() + GraphDrawer.getVertical().getValue()) / GraphDrawer.getZoom()));
                    }
            }
        }
        });
    }

    @Override
    public boolean isActive() {
        return E.getE().getScreen().getCurrent() == this;
    }

    public static Vertex[] getDragEdge() {
        return dragEdge;
    }
}
