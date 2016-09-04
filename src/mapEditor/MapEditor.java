package mapEditor;

import connection.Connector;
import de.SweetCode.e.E;
import de.SweetCode.e.input.InputEntry;
import de.SweetCode.e.math.IBoundingBox;
import de.SweetCode.e.math.ILocation;
import de.SweetCode.e.math.Location;
import de.SweetCode.e.rendering.GameScene;
import de.SweetCode.e.rendering.layers.Layers;
import field.buildings.Buildings;
import field.resource.Resources;
import game.GameOfGraphs;
import game.MenuView;
import game.Player;
import game.sprite.Textures;
import game.ui.Button;
import game.ui.*;
import graph.*;
import ki.KIFraction;
import simulation.Unit;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

public class MapEditor extends GameScene{
    private Graph graph;
    private static Vertex[] dragEdge;

    private Vertex temp;

    private int chooser = 0;

    private boolean leftMouse = false;

    private Vertex currentVertex;
    private Edge currentEdge;
    private boolean question = false;

    private ArrayList<game.ui.EditText<String>> editTexts;
    private DropDownMenu<Resources> specialResources;
    private DropDownMenu<Integer> forest;
    private DropDownMenu<Player> owner;
    private EditText<String> weight;
    private EditText<String> name;

    private Button<String> randomizeButton = new Button<>(this, "Randomize", new ILocation(631, 698), (c, v) -> {

        currentVertex.setField(GameOfGraphs.getGame().getFieldController().createField(this.currentVertex.getField().getPlayer(), false));
        this.updateView();

    });
    private Button<String> randomizeAllButton = new Button<>(this, "Randomize", new ILocation(1000, 700), (c, v) -> {

        this.graph.getVertices().forEach(e -> e.setField(GameOfGraphs.getGame().getFieldController().createField(e.getField().getPlayer(), false)));
        if (currentVertex != null) {
            this.updateView();
        }

    });

    private PlayerChooser playerChooser;

    public MapEditor() {
        graph = GameOfGraphs.getGame().getGraphController().getGraph();

        E.getE().addComponent(this.randomizeAllButton);
        this.randomizeButton.setEnabled(false);
        E.getE().addComponent(randomizeButton);

        playerChooser = new PlayerChooser(this, new ILocation(500, 500), (component, value) -> {


            GameOfGraphs.getGame().getPlayers().add(value instanceof KIFraction ? (KIFraction) value : (Player) value);

            LinkedList<Player> list = new LinkedList(GameOfGraphs.getGame().getPlayers());
            list.add(new Player("add New Player", null));

            owner.setOptions(list);
            currentVertex.getField().setPlayer(value instanceof KIFraction ? (KIFraction) value : (Player) value);

        });
        E.getE().addComponent(playerChooser);
        playerChooser.setEnabled(false);

        ArrayList<game.ui.Button<String>> buttons = new ArrayList<>();

        buttons.add(new game.ui.Button<>(this, "Add Vertex", new ILocation(1000, 505), (component, value) -> {
            chooser = 0;
        }));
        buttons.add(new game.ui.Button<>(this, "Add Edge", new ILocation(1000, 530), (component, value) -> {
            chooser = 1;
        }));
        buttons.add(new game.ui.Button<>(this, "Save", new ILocation(1000, 555), (component, value) -> {
            GameOfGraphs.getGame().getGraphController().save(graph);
        }));
        buttons.add(new game.ui.Button<>(this, "Load", new ILocation(1000, 580), (component, value) -> {
            Object[] g = GameOfGraphs.getGame().getGraphController().load();
            if (g != null){
                graph = (Graph) g[0];
            }
        }));
        buttons.add(new game.ui.Button<>(this, "Move", new ILocation(1000, 605), (component, value) -> {
            GameOfGraphs.getGame().getGraphController().checkGraph();
        }));
        buttons.add(new game.ui.Button<>(this, "Check", new ILocation(1000, 630), (component, value) -> {
            chooser = 2;
        }));
        buttons.add(new Button<>(this, "Remove All", new ILocation(1000, 655), (component, value) -> {
            for (Vertex v : graph.getVertices()){
                graph.removeVertex(v);
            }

            GameOfGraphs.getGame().getPlayers().removeAll(GameOfGraphs.getGame().getPlayers());
            KIFraction fraction1 = new KIFraction("Independent", new Color(232, 77, 91));
            GameOfGraphs.getGame().getPlayers().add(fraction1);

            LinkedList<Player> list = new LinkedList(GameOfGraphs.getGame().getPlayers());
            list.add(new Player("add New Player", null));

            playerChooser.reset();

            owner.setOptions(list);
        }));
        buttons.add(new Button<>(this, "Remove", new ILocation(1000, 680), (component, value) -> {
            chooser = 3;
        }));

        for (game.ui.Button<String> b : buttons){
            E.getE().addComponent(b);
        }

        E.getE().addComponent(this.questionButton);

        editTexts = new ArrayList<>();
        editTexts.add(new game.ui.EditText<String>(this, "0", new IBoundingBox(new Location(85, 505), new Location(100, 525)), true, false, 1, (component, value) -> {
            currentVertex.getField().setFertility(Integer.parseInt(value.equals("") ? "0" : value));
        }));
        editTexts.add(new game.ui.EditText<String>(this, "0", new IBoundingBox(new Location(190, 505), new Location(205, 525)), true, false, 1, (component, value) -> {
            currentVertex.getField().setMountains(Integer.parseInt(value.equals("") ? "0" : value));
        }));
        editTexts.add(new game.ui.EditText<String>(this, "0", new IBoundingBox(new Location(265, 505), new Location(290, 525)), true, false, 2, (component, value) -> {
            ArrayList<Unit> units = new ArrayList<Unit>();
            for (int i = 0; i < Integer.parseInt(value.equals("") ? "0" : value); i++){
                units.add(new Unit(currentVertex.getField().getPlayer()));
            }
            currentVertex.getField().setUnits(units);
        }));

        editTexts.add(new game.ui.EditText<String>(this, "0", new IBoundingBox(new Location(85, 525), new Location(99, 545)), true, false, 1, (component, value) -> {
            currentVertex.getField().getResources().put(Resources.POPULATION, (Integer.parseInt(value.equals("") ? "0" : value)));
        }));
        editTexts.add(new game.ui.EditText<String>(this, "0", new IBoundingBox(new Location(85, 550), new Location(99, 570)), true, false, 1, (component, value) -> {
            currentVertex.getField().getResources().put(Resources.FOOD, (Integer.parseInt(value.equals("") ? "0" : value)));
        }));
        editTexts.add(new game.ui.EditText<String>(this, "0", new IBoundingBox(new Location(85, 575), new Location(99, 595)), true, false, 1, (component, value) -> {
            currentVertex.getField().getResources().put(Resources.STONE, (Integer.parseInt(value.equals("") ? "0" : value)));
        }));
        editTexts.add(new game.ui.EditText<String>(this, "0", new IBoundingBox(new Location(85, 600), new Location(99, 620)), true, false, 1, (component, value) -> {
            currentVertex.getField().getResources().put(Resources.WOOD, (Integer.parseInt(value.equals("") ? "0" : value)));
        }));
        editTexts.add(new game.ui.EditText<String>(this, "0", new IBoundingBox(new Location(85, 625), new Location(99, 645)), true, false, 1, (component, value) -> {
            currentVertex.getField().getResources().put(Resources.WHEAT, (Integer.parseInt(value.equals("") ? "0" : value)));
        }));
        editTexts.add(new game.ui.EditText<String>(this, "0", new IBoundingBox(new Location(85, 650), new Location(99, 670)), true, false, 1, (component, value) -> {
            currentVertex.getField().getResources().put(Resources.TREE, (Integer.parseInt(value.equals("") ? "0" : value)));
        }));
        editTexts.add(new game.ui.EditText<String>(this, "0", new IBoundingBox(new Location(85, 675), new Location(99, 695)), true, false, 1, (component, value) -> {
            currentVertex.getField().getResources().put(Resources.IRON, (Integer.parseInt(value.equals("") ? "0" : value)));
        }));
        editTexts.add(new game.ui.EditText<String>(this, "0", new IBoundingBox(new Location(85, 700), new Location(99, 720)), true, false, 1, (component, value) -> {
            currentVertex.getField().getResources().put(Resources.GOLD, (Integer.parseInt(value.equals("") ? "0" : value)));
        }));


        editTexts.add(new game.ui.EditText<String>(this, "0", new IBoundingBox(new Location(190, 525), new Location(205, 545)), true, false, 1, (component, value) -> {
            currentVertex.getField().getBuildings().put(Buildings.MINE, (Integer.parseInt(value.equals("") ? "0" : value)));
        }));
        editTexts.add(new game.ui.EditText<String>(this, "0", new IBoundingBox(new Location(190, 550), new Location(205, 570)), true, false, 1, (component, value) -> {
            currentVertex.getField().getBuildings().put(Buildings.FARM, (Integer.parseInt(value.equals("") ? "0" : value)));
        }));
        editTexts.add(new game.ui.EditText<String>(this, "0", new IBoundingBox(new Location(190, 575), new Location(205, 595)), true, false, 1, (component, value) -> {
            currentVertex.getField().getBuildings().put(Buildings.WINDMILL, (Integer.parseInt(value.equals("") ? "0" : value)));
        }));
        editTexts.add(new game.ui.EditText<String>(this, "0", new IBoundingBox(new Location(190, 600), new Location(205, 620)), true, false, 1, (component, value) -> {
            currentVertex.getField().getBuildings().put(Buildings.LUMBERJACK, (Integer.parseInt(value.equals("") ? "0" : value)));
        }));
        editTexts.add(new game.ui.EditText<String>(this, "0", new IBoundingBox(new Location(190, 625), new Location(205, 645)), true, false, 1, (component, value) -> {
            currentVertex.getField().getBuildings().put(Buildings.BAZAAR, (Integer.parseInt(value.equals("") ? "0" : value)));
        }));
        editTexts.add(new game.ui.EditText<String>(this, "0", new IBoundingBox(new Location(190, 650), new Location(205, 670)), true, false, 1, (component, value) -> {
            currentVertex.getField().getBuildings().put(Buildings.SLAVE_MARKET, (Integer.parseInt(value.equals("") ? "0" : value)));
        }));
        editTexts.add(new game.ui.EditText<String>(this, "0", new IBoundingBox(new Location(190, 675), new Location(205, 695)), true, false, 1, (component, value) -> {
            currentVertex.getField().getBuildings().put(Buildings.MARKETPLACE, (Integer.parseInt(value.equals("") ? "0" : value)));
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

        name = new EditText<String>(this, "", new IBoundingBox(new ILocation(375, 580), new ILocation(425, 600)), false, false, (component, value) -> {
            currentVertex.setId(value);
        });
        name.setLineColor(Color.YELLOW);
        name.setTextColor(Color.YELLOW);
        E.getE().addComponent(name);

        owner = new DropDownMenu<Player>(this, new ILocation(375, 555), new LinkedList<Player>(){{
            for (Player player:GameOfGraphs.getGame().getPlayers()){
                add(player);
            }
            add(new Player("add New Player", null));
        }}, (component, value) -> {
            if (value.getName().equals("add New Player")){
                playerChooser.setEnabled(true);
            }else {
                if (value instanceof KIFraction){
                    currentVertex.getField().setPlayer(new KIFraction("Independent", new Color(232, 77, 91)));
                }else {
                    currentVertex.getField().setPlayer((Player) value);
                }
            }
        });
        owner.setBackground(Color.DARK_GRAY);
        owner.setForeground(Color.BLACK);
        E.getE().addComponent(owner);

        forest = new DropDownMenu<Integer>(this, new ILocation(375, 530), new LinkedList<Integer>(){{
            add(1);
            add(2);
            add(3);
        }}, (component, value) -> {
            currentVertex.getField().setForestType(value);
        });
        forest.setBackground(Color.DARK_GRAY);
        forest.setForeground(Color.GREEN);
        E.getE().addComponent(forest);

        specialResources = new DropDownMenu<Resources>(this, new ILocation(375, 505), new LinkedList<Resources>(){{
            for(Resources resources : Resources.values()){
                if (resources.getName().equals("Iron") || resources.getName().equals("Gold") || resources.getName().equals("Wheat") || resources.getName().equals("Tree")) {
                    this.add(resources);
                }
            }
        }}, (component, value) -> {
            currentVertex.getField().setLocalResource(value);
        });
        specialResources.setBackground(Color.DARK_GRAY);
        specialResources.setForeground(Color.BLUE);
        E.getE().addComponent(specialResources);

        weight = new EditText<String>(this, "", new IBoundingBox(new Location(65, 505), new Location(105, 525)), true, true, (component, value) -> {
            currentEdge.setWeight(Double.parseDouble(value.equals("") ? String.valueOf(0) : value));
        });
        weight.setLineColor(Color.ORANGE);
        weight.setTextColor(Color.ORANGE);
        E.getE().addComponent(weight);
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

        g.setStroke(new BasicStroke(1));

        g.setColor(Color.DARK_GRAY);
        g.fillRect(0,500,1280,220);
        g.setColor(Color.BLACK);
        g.drawLine(0,500,1280,500);
        g.drawLine(700, 500, 700, 720);
        g.setBackground(Color.WHITE);

        if(currentVertex == null && currentEdge == null) {
            g.drawString("No field or edge selected.", 520, 600);

            for (game.ui.EditText<String> text : editTexts){
                text.setEnabled(false);
            }
            specialResources.setEnabled(false);
            forest.setEnabled(false);
            owner.setEnabled(false);
            name.setEnabled(false);
            randomizeButton.setEnabled(false);

            weight.setEnabled(false);

        }else if (currentEdge == null){
            weight.setEnabled(false);

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
            g.drawString(String.valueOf("FOREST: "), 320, 545);
            g.setColor(Color.BLACK);
            g.drawString("OWNER: ", 320, 570);
            g.setColor(Color.YELLOW);
            g.drawString("NAME: ", 320, 595);

            for (game.ui.EditText<String> text : editTexts){
                text.setEnabled(true);
            }
            specialResources.setEnabled(true);
            forest.setEnabled(true);
            owner.setEnabled(true);
            name.setEnabled(true);
            randomizeButton.setEnabled(true);

            g.setColor(Color.LIGHT_GRAY);

            //Zeichnen der Resourcen und Gebäude
            Map<Resources, Integer> resources = currentVertex.getField().getResources();
            Map<Buildings, Integer> buildings = currentVertex.getField().getBuildings();

            final int[] y = {0};
            resources.forEach((resource, amount) -> {

                g.drawString(resource.getName() + ": ", 20, 540 + y[0] * 25);

                y[0]++;
            });


            y[0] = 0;
            buildings.forEach((building, amount) -> {

                g.drawString(building.getName() + ": ", 120, 540 + y[0] * 25);
                y[0]++;
            });

        }else {
            for (game.ui.EditText<String> text : editTexts){
                text.setEnabled(false);
            }
            specialResources.setEnabled(false);
            forest.setEnabled(false);
            owner.setEnabled(false);
            name.setEnabled(false);
            randomizeButton.setEnabled(false);

            weight.setEnabled(true);

            //Zeichnen der Statistiken
            g.setColor(Color.ORANGE);
            g.drawString(String.valueOf("Weight: "), 20, 520);
        }
    }

    @Override
    public void update(InputEntry inputEntry, long l) {
        GraphDrawer.update(inputEntry, l);

        inputEntry.getMouseEntries().forEach(mouseEntry -> {
            if (!(mouseEntry.getPoint().getX() >= 1280-25 && mouseEntry.getPoint().getX() <= 1280 || mouseEntry.getPoint().getY() >= 500-25 && mouseEntry.getPoint().getY() <= 500)) {
                if (!(mouseEntry.getPoint().getX() >= 0 && mouseEntry.getPoint().getX() <= 1280 && mouseEntry.getPoint().getY() >= 500 && mouseEntry.getPoint().getY() <= 720)) {
                    if (!question) {
                        currentEdge = null;
                        currentVertex = null;

                        final Vertex vertex = graph.getVertex((int) ((mouseEntry.getPoint().getX() + GraphDrawer.getHorizontal().getValue()) / GraphDrawer.getZoom()), (int) ((mouseEntry.getPoint().getY() + GraphDrawer.getVertical().getValue()) / GraphDrawer.getZoom()));
                        if (mouseEntry.getButton() == 1) {
                            leftMouse = true;
                            if (vertex != null) {
                                currentVertex = vertex;
                                this.updateView();
                            }else {
                                Edge edge = graph.getEdge(((int) mouseEntry.getPoint().getX()), ((int) mouseEntry.getPoint().getY()), 5);

                                if (edge != null){
                                    currentEdge = edge;

                                    weight.setText(String.valueOf(edge.getWeight()));
                                }
                            }
                            switch (chooser) {
                                case 0:
                                    if (vertex != null) {
                                        temp = vertex;
                                    } else if (currentEdge == null){
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
                                case 3:
                                    if (vertex != null && currentEdge == null){
                                        graph.removeVertex(vertex);
                                        currentVertex = null;
                                    }else {
                                        Edge edge = graph.getEdge(((int) mouseEntry.getPoint().getX()), ((int) mouseEntry.getPoint().getY()), 5);
                                        if (edge != null){
                                            graph.removeEdge(edge);
                                            currentEdge = null;
                                        }
                                    }
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

        inputEntry.getKeyEntries().forEach(e -> {

            if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                E.getE().show(MenuView.class);
            }

        });
    }

    @Override
    public boolean isActive() {
        return E.getE().getScreen().getCurrent() == this;
    }

    public void updateView() {
        for(int i = 0; i < owner.getOptions().size(); i++){
            if (currentVertex.getField().getPlayer().getName().equals(owner.getOptions().get(i).getName())){
                owner.setSelectedIndex(i);
            }
        }

        name.setText(currentVertex.getID());
        forest.setSelectedIndex(currentVertex.getField().getForestType()-1);

        switch (currentVertex.getField().getLocalResource().getName()){
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
                    editTexts.get(i).setText(String.valueOf(currentVertex.getField().getFertility()));
                    break;
                case 1:
                    editTexts.get(i).setText(String.valueOf(currentVertex.getField().getMountains()));
                    break;
                case 2:
                    editTexts.get(i).setText(String.valueOf(currentVertex.getField().getUnits().size()));
                    break;
                case 3:
                    editTexts.get(i).setText(String.valueOf(currentVertex.getField().getResources().get(Resources.POPULATION)));
                    break;
                case 4:
                    editTexts.get(i).setText(String.valueOf(currentVertex.getField().getResources().get(Resources.FOOD)));
                    break;
                case 5:
                    editTexts.get(i).setText(String.valueOf(currentVertex.getField().getResources().get(Resources.STONE)));
                    break;
                case 6:
                    editTexts.get(i).setText(String.valueOf(currentVertex.getField().getResources().get(Resources.WOOD)));
                    break;
                case 7:
                    editTexts.get(i).setText(String.valueOf(currentVertex.getField().getResources().get(Resources.WHEAT)));
                    break;
                case 8:
                    editTexts.get(i).setText(String.valueOf(currentVertex.getField().getResources().get(Resources.TREE)));
                    break;
                case 9:
                    editTexts.get(i).setText(String.valueOf(currentVertex.getField().getResources().get(Resources.IRON)));
                    break;
                case 10:
                    editTexts.get(i).setText(String.valueOf(currentVertex.getField().getResources().get(Resources.GOLD)));
                    break;
                case 11:
                    editTexts.get(i).setText(String.valueOf(currentVertex.getField().getBuildings().get(Buildings.MINE)));
                    break;
                case 12:
                    editTexts.get(i).setText(String.valueOf(currentVertex.getField().getBuildings().get(Buildings.FARM)));
                    break;
                case 13:
                    editTexts.get(i).setText(String.valueOf(currentVertex.getField().getBuildings().get(Buildings.WINDMILL)));
                    break;
                case 14:
                    editTexts.get(i).setText(String.valueOf(currentVertex.getField().getBuildings().get(Buildings.LUMBERJACK)));
                    break;
                case 15:
                    editTexts.get(i).setText(String.valueOf(currentVertex.getField().getBuildings().get(Buildings.BAZAAR)));
                    break;
                case 16:
                    editTexts.get(i).setText(String.valueOf(currentVertex.getField().getBuildings().get(Buildings.SLAVE_MARKET)));
                    break;
                case 17:
                    editTexts.get(i).setText(String.valueOf(currentVertex.getField().getBuildings().get(Buildings.MARKETPLACE)));
                    break;
            }
        }

        owner.setOptions(GameOfGraphs.getGame().getPlayers());
    }

    public static Vertex[] getDragEdge() {
        return dragEdge;
    }

    private class PlayerChooser extends UIComponent{

        private ArrayList<MyColor> colorList;
        private ILocation location;

        private DropDownMenu<MyColor> colorMenu;
        private EditText<String> name;

        private Button<String> cancel, submit;

        private boolean firstStart = true;

        public PlayerChooser(GameScene gameScene, ILocation location, Trigger trigger) {
            super(gameScene, trigger);
            this.location = location;

            colorList = new ArrayList<>();
            colorList.add(new MyColor(Color.CYAN, "Cyan"));
            colorList.add(new MyColor(Color.BLACK, "Black"));
            colorList.add(new MyColor(Color.BLUE, "Blue"));
            colorList.add(new MyColor(Color.RED, "Red"));
            colorList.add(new MyColor(Color.MAGENTA, "Magenta"));
            colorList.add(new MyColor(Color.YELLOW, "Yellow"));
            colorList.add(new MyColor(Color.GRAY, "Gray"));

            name = new EditText<String>(gameScene, "", new IBoundingBox(new ILocation(location.getX()+40, location.getY()+10), new ILocation(location.getX()+100, location.getY()+30)), false, false, "Name", (component, value) -> {});
            name.setTextColor(Color.BLACK);

            cancel = new Button<>(gameScene, "Cancel", new ILocation(location.getX()+25, location.getY()+125), (component, value) -> {
                setEnabled(false);

                colorMenu.setEnabled(false);
                name.setEnabled(false);
                cancel.setEnabled(false);
                submit.setEnabled(false);
            });
            submit = new Button<>(gameScene, "Submit", new ILocation(location.getX()+75, location.getY()+125), (component, value) -> {
                if (name.getText().length() != 0) {
                    LinkedList<Player> players = GameOfGraphs.getGame().getPlayers();
                    for (Player p : players){
                        if (p.getName().equals(name.getText())){
                            setEnabled(false);

                            name.setText("");
                            colorMenu.setEnabled(false);
                            name.setEnabled(false);
                            cancel.setEnabled(false);
                            submit.setEnabled(false);

                            return;
                        }
                    }


                    getTrigger().call(this, new Player(name.getText(), colorMenu.getOption().getColor()));


                    colorList.remove(colorMenu.getOption());

                    colorMenu.setOptions(new LinkedList<>(colorList));

                }
                setEnabled(false);

                name.setText("");
                colorMenu.setEnabled(false);
                name.setEnabled(false);
                cancel.setEnabled(false);
                submit.setEnabled(false);
            });

            colorMenu = new DropDownMenu(gameScene, new ILocation(location.getX()+30, location.getY()+40), new LinkedList<MyColor>(){{
                for(MyColor c : colorList){
                    add(c);
                }
            }}, (component, value) -> {});
            colorMenu.setBackground(Color.LIGHT_GRAY);
            colorMenu.setForeground(Color.BLACK);

            name.setText("");
            colorMenu.setEnabled(false);
            name.setEnabled(false);
            cancel.setEnabled(false);
            submit.setEnabled(false);
        }

        @Override
        public void render(Layers layers) {
            Graphics2D g = layers.first().getGraphics2D();

            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(location.getX(), location.getY(), 150, 150);

            g.setColor(Color.BLACK);
            g.drawRect(location.getX(), location.getY(), 150, 150);

            g.setColor(colorMenu.getOption().getColor());
            g.fillRect(location.getX()+85, location.getY()+40, 20, 20);

            if (firstStart){
                E.getE().addComponent(name);
                E.getE().addComponent(cancel);
                E.getE().addComponent(submit);
                E.getE().addComponent(colorMenu);

                firstStart = false;
            }
        }

        @Override
        public void update(InputEntry inputEntry, long l) {
            colorMenu.setEnabled(isEnabled());
            name.setEnabled(isEnabled());
            cancel.setEnabled(isEnabled());
            submit.setEnabled(isEnabled());
        }

        @Override
        public boolean isActive() {
            return this.getGameScene().isActive() && this.isEnabled();
        }

        public void reset() {
            colorList = new ArrayList<>();
            colorList.add(new MyColor(Color.CYAN, "Cyan"));
            colorList.add(new MyColor(Color.BLACK, "Black"));
            colorList.add(new MyColor(Color.BLUE, "Blue"));
            colorList.add(new MyColor(Color.RED, "Red"));
            colorList.add(new MyColor(Color.MAGENTA, "Magenta"));
            colorList.add(new MyColor(Color.YELLOW, "Yellow"));
            colorList.add(new MyColor(Color.GRAY, "Gray"));

            colorMenu.setOptions(new LinkedList<>(colorList));
        }

        private class MyColor{
            private String name;
            private Color color;

            public MyColor(Color color, String name) {
                this.name = name;
                this.color = color;
            }

            @Override
            public String toString() {
                return name;
            }

            public Color getColor() {
                return color;
            }
        }
    }
}
