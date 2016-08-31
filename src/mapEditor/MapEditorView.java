package mapEditor;

import de.SweetCode.e.E;
import de.SweetCode.e.input.InputEntry;
import de.SweetCode.e.rendering.GameScene;
import de.SweetCode.e.rendering.layers.Layers;
import game.GameOfGraphs;
import game.GraphDrawer;
import game.ui.CheckBox;
import graph.Edge;
import graph.Graph;
import graph.Vector;
import graph.Vertex;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class MapEditorView extends GameScene{
    private Graph graph;
    private static Vertex[] dragEdge;

    private Vertex temp;

    private int chooser = 0;
    private static BufferedImage previewBackground, previewVertex, previewEdge;

    private PropertiesVertex propertiesVertex;
    private PropertiesEdge propertiesEdge;

    private boolean leftMouse = false;

    private Button addVertex, addEdge, save, load, move, check;
    private TextureChooser vertexChooser;

    public MapEditorView() {
        graph = GameOfGraphs.getGame().getGraphController().getGraph();

        addVertex = new Button(5, 505, 310, 100, Color.WHITE, Color.BLACK, "Add Vertex");
        addEdge = new Button(320, 505, 310, 100, Color.WHITE, Color.BLACK, "Add Edge");
        save = new Button(950, 505, 310, 48, Color.WHITE, Color.BLACK, "Save");
        load = new Button(950, 557, 310, 48, Color.WHITE, Color.BLACK, "Load");
        move = new Button(950, 615, 310, 48, Color.WHITE, Color.BLACK, "Move");
        check = new Button(950, 667, 310, 48, Color.WHITE, Color.BLACK, "Check");

        vertexChooser = new TextureChooser("Vertex", 5, 610);
    }

    @Override
    public void render(Layers layers) {
        Graphics2D g = layers.first().getGraphics2D();

        GraphDrawer.drawer(g, graph, "MapEditor");

        if (propertiesVertex != null){
            propertiesVertex.drawer(layers.first().getGraphics2D());
        }

        if (propertiesEdge != null){
            propertiesEdge.drawer(layers.first().getGraphics2D());
        }

        g.setColor(Color.BLACK);
        g.fillRect(0, 500, 1280, 220);

        addVertex.draw(g);
        addEdge.draw(g);
        save.draw(g);
        load.draw(g);
        move.draw(g);
        check.draw(g);

        vertexChooser.draw(g);
    }

    @Override
    public void update(InputEntry inputEntry, long l) {
        GraphDrawer.update(inputEntry, l);

        final boolean[] mouseEnabled = {true};

        if (propertiesVertex != null){
            propertiesVertex.update(inputEntry, l);

            inputEntry.getMouseEntries().forEach(mouseEntry -> {
                if ((mouseEntry.getPoint().getX() + GraphDrawer.getHorizontal().getValue()) >= propertiesVertex.getP()[0] && (mouseEntry.getPoint().getX() + GraphDrawer.getHorizontal().getValue()) <= propertiesVertex.getP()[0] + propertiesVertex.getWidth() && (mouseEntry.getPoint().getY() + GraphDrawer.getVertical().getValue()) >= propertiesVertex.getP()[1] && (mouseEntry.getPoint().getY() + GraphDrawer.getVertical().getValue()) <= propertiesVertex.getP()[1] + propertiesVertex.getHeight()){
                    mouseEnabled[0] = false;
                }
            });
        }else if(propertiesEdge != null){
            propertiesEdge.update(inputEntry, l);

            inputEntry.getMouseEntries().forEach(mouseEntry -> {
                if ((mouseEntry.getPoint().getX() + GraphDrawer.getHorizontal().getValue()) >= propertiesEdge.getP()[0] && (mouseEntry.getPoint().getX() + GraphDrawer.getHorizontal().getValue()) <= propertiesEdge.getP()[0] + propertiesEdge.getWidth() && (mouseEntry.getPoint().getY() + GraphDrawer.getVertical().getValue()) >= propertiesEdge.getP()[1] && (mouseEntry.getPoint().getY() + GraphDrawer.getVertical().getValue()) <= propertiesEdge.getP()[1] + propertiesEdge.getHeight()){
                    mouseEnabled[0] = false;
                }
            });
        }

        if (mouseEnabled[0]) {
            inputEntry.getMouseEntries().forEach(mouseEntry -> {
                if (propertiesVertex != null) {
                    propertiesVertex.getV().setMarkTarget(false);
                    propertiesVertex = null;
                }

                if (propertiesEdge != null) {
                    propertiesEdge.getE().setMark(false);
                    propertiesEdge = null;
                }

                if (!(mouseEntry.getPoint().getX() >= 1280-25 && mouseEntry.getPoint().getX() <= 1280 || mouseEntry.getPoint().getY() >= 500-25 && mouseEntry.getPoint().getY() <= 500)) {
                    if (mouseEntry.getPoint().getX() >= 0 && mouseEntry.getPoint().getX() <= 1280 && mouseEntry.getPoint().getY() >= 500 && mouseEntry.getPoint().getY() <= 720){
                        if (addVertex.isPushed(mouseEntry.getPoint())){
                            chooser = 0;
                        }else if (addEdge.isPushed(mouseEntry.getPoint())){
                            chooser = 1;
                        }else if (save.isPushed(mouseEntry.getPoint())){
                            GameOfGraphs.getGame().getGraphController().save(graph);
                        }else if (load.isPushed(mouseEntry.getPoint())){
                            graph = GameOfGraphs.getGame().getGraphController().load();
                        }else if (check.isPushed(mouseEntry.getPoint())){
                            GameOfGraphs.getGame().getGraphController().checkGraph();
                        }else if (move.isPushed(mouseEntry.getPoint())){
                            chooser = 2;
                        }else {
                            vertexChooser.update(inputEntry, l);
                        }
                    } else {

                        final Vertex vertex = graph.getVertex((int) ((mouseEntry.getPoint().getX() + GraphDrawer.getHorizontal().getValue()) / GraphDrawer.getZoom()), (int) ((mouseEntry.getPoint().getY() + GraphDrawer.getVertical().getValue()) / GraphDrawer.getZoom()));

                        if (mouseEntry.getButton() == 1) {
                            leftMouse = true;

                            switch (chooser) {
                                case 0:
                                    if (vertex != null) {
                                        temp = vertex;
                                    } else {
                                        ArrayList<Vertex> vertexList = graph.getVertices();

                                        graph.addVertex(new Vertex(String.valueOf(vertexList.size() + 1), (int) ((mouseEntry.getPoint().getX() - GraphDrawer.getHorizontal().getValue()) / GraphDrawer.getZoom()), (int) ((mouseEntry.getPoint().getY() - GraphDrawer.getVertical().getValue()) / GraphDrawer.getZoom()), GameOfGraphs.getGame().getFieldController().createField(null, false)));
                                    }
                                    break;
                                case 1:
                                    if (vertex != null) {
                                        temp = vertex;
                                        dragEdge = new Vertex[]{new Vertex("equals", vertex.getX(), vertex.getY(), null), new Vertex("equals", vertex.getX(), vertex.getY(), null)};
                                    }
                                    break;
                            }
                        } else if (mouseEntry.getButton() == 3) {
                            if (vertex != null) {
                                vertex.setMarkTarget(true);

                                int x = (int) ((vertex.getX() * GraphDrawer.getZoom() + graph.getRadius() + 175) > 1280 - 25 ? (vertex.getX() * GraphDrawer.getZoom() - GraphDrawer.getHorizontal().getValue() - 175) : (vertex.getX() * GraphDrawer.getZoom()));
                                int y = (int) ((vertex.getY() * GraphDrawer.getZoom() + graph.getRadius() + 200) > 500 - 25 ? (vertex.getY() * GraphDrawer.getZoom() - GraphDrawer.getVertical().getValue() - 200) : (vertex.getY() * GraphDrawer.getZoom()));


                                propertiesVertex = new PropertiesVertex(vertex, new int[]{x, y});
                            } else {
                                final Edge edge = graph.getEdge((int) ((mouseEntry.getPoint().getX() + GraphDrawer.getHorizontal().getValue()) / GraphDrawer.getZoom()), (int) ((mouseEntry.getPoint().getY() + GraphDrawer.getVertical().getValue()) / GraphDrawer.getZoom()), 10);

                                if (edge != null) {
                                    edge.setMark(true);

                                    int x = (int) ((mouseEntry.getPoint().getX() - GraphDrawer.getHorizontal().getValue() + graph.getRadius() + 175) > 1280 - 25 ? (mouseEntry.getPoint().getX() - 175) : (mouseEntry.getPoint().getX()));
                                    int y = (int) ((mouseEntry.getPoint().getY() - GraphDrawer.getVertical().getValue() + graph.getRadius() + 50) > 500 - 25 ? (mouseEntry.getPoint().getY() - 50) : (mouseEntry.getPoint().getY()));

                                    propertiesEdge = new PropertiesEdge(edge, new int[]{x, y});
                                }
                            }
                        }
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
                            Vertex vertex = graph.getVertex((int)((mouseEntry.getPoint().getX() + GraphDrawer.getHorizontal().getValue())/GraphDrawer.getZoom()), (int)((mouseEntry.getPoint().getY() + GraphDrawer.getVertical().getValue())/GraphDrawer.getZoom()));
                            if (vertex != null && temp != null) {
                                graph.addEdge(new Edge(new String[]{temp.getID(), vertex.getID()}, 0));
                            }
                            dragEdge = null;
                            temp = null;
                            break;
                    }
                }
            });

            inputEntry.getMouseDraggedEntries().forEach(mouseEntry -> {
                if (leftMouse) {
                    switch (chooser) {
                        case 0:
                            if (temp != null) {
                                double x = (mouseEntry.getPoint().getX() + GraphDrawer.getHorizontal().getValue()) / GraphDrawer.getZoom();
                                double y = (mouseEntry.getPoint().getY() + GraphDrawer.getVertical().getValue()) / GraphDrawer.getZoom();

                                if (mouseEntry.getPoint().getX() - graph.getRadius() < 0) {
                                    x = GraphDrawer.getHorizontal().getValue()/GraphDrawer.getZoom()+graph.getRadius();
                                }else if (mouseEntry.getPoint().getX() + graph.getRadius() > 1280 - 25){
                                    x = (1280-25+GraphDrawer.getHorizontal().getValue())/GraphDrawer.getZoom()-graph.getRadius();
                                }

                                if (mouseEntry.getPoint().getY() - graph.getRadius() < 0){
                                    y = GraphDrawer.getVertical().getValue()/GraphDrawer.getZoom()+graph.getRadius();
                                }else if (mouseEntry.getPoint().getY() + graph.getRadius() > 500 - 25){
                                    y = (500-25+GraphDrawer.getVertical().getValue())/GraphDrawer.getZoom()-graph.getRadius();
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
                                        vector1.setX(GraphDrawer.getHorizontal().getValue()/GraphDrawer.getZoom()+graph.getRadius());
                                    }else if (vector1.getX() + graph.getRadius() > 1280 - 25){
                                        vector1.setX((1280-25+GraphDrawer.getHorizontal().getValue())/GraphDrawer.getZoom()-graph.getRadius());
                                    }

                                    if (vector1.getY() - graph.getRadius() < 0){
                                        vector1.setY(GraphDrawer.getVertical().getValue()/GraphDrawer.getZoom()+graph.getRadius());
                                    }else if (vector1.getY() + graph.getRadius() > 500 - 25){
                                        vector1.setY((500-25+GraphDrawer.getVertical().getValue())/GraphDrawer.getZoom()-graph.getRadius());
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
    }

    @Override
    public boolean isActive() {
        return E.getE().getScreen().getCurrent() == this;
    }

    public static BufferedImage getPreviewBackground() {
        return previewBackground;
    }

    public static BufferedImage getPreviewVertex() {
        return previewVertex;
    }

    public static BufferedImage getPreviewEdge() {
        return previewEdge;
    }

    public static Vertex[] getDragEdge() {
        return dragEdge;
    }

    private BufferedImage getImage(Color color){
        BufferedImage bufferedImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        bufferedImage.setRGB(0, 0, color.getRGB());
        return bufferedImage;
    }

    private class PropertiesVertex{
        private Vertex v;
        private int[] p;

        private final int width = 175;
        private final int height = 200;

        private EditText enabled;

        private EditText name, population, biom;

        public PropertiesVertex(Vertex v, int[] p) {
            this.v = v;
            this.p = p;

            name = new EditText(83, 25, p[0] + 85, p[1] + 5, v.getID(), false, false);
           // population = new EditText(83, 25, p[0] + 85, p[1] + 30, e.getField().getResources().get(Resources.POPULATION).toString(), true, false);
        }

        public void drawer(Graphics2D g){
            g.setColor(Color.WHITE);
            g.fillRect(p[0], p[1], width, height);

            g.setColor(Color.BLACK);
            g.drawRect(p[0], p[1], width, height);

            g.drawString("Name: ", p[0] + 5, p[1] + 20);
            name.drawer(g);

            //g.drawString("Population: ", p[0] + 5, p[1] + 30);
         //   population.drawer(g);

            //g.drawString("Biom", p[0] + 5, p[1] + 55);
            //biom.drawer(g);


        }

        public void update(InputEntry inputEntry, long l){
            inputEntry.getMouseEntries().forEach(mouseEntry -> {
                if ((mouseEntry.getPoint().getX() + GraphDrawer.getHorizontal().getValue()) >= name.getX() && (mouseEntry.getPoint().getX() + GraphDrawer.getHorizontal().getValue()) <= name.getX() + name.getWidth() && (mouseEntry.getPoint().getY() + GraphDrawer.getVertical().getValue()) >= name.getY() && (mouseEntry.getPoint().getY() + GraphDrawer.getVertical().getValue()) <= name.getY() + name.getHeight()){
                    enabled = name;
                }
            });

            if (enabled != null) {
                enabled.update(inputEntry, l);
            }

            if (enabled == name){
                v.setId(enabled.getText());
            }

//            e.getField().getResources().put(Resources.POPULATION, Integer.parseInt(population.update(inputEntry, l)));
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public int[] getP() {
            return p;
        }

        public Vertex getV() {
            return v;
        }
    }

    private class PropertiesEdge {
        private Edge e;
        private int[] p;
        private final int width = 175;
        private final int height = 50;

        private EditText enabled;

        private EditText weight;

        public PropertiesEdge(Edge e, int[] p) {
            this.e = e;
            this.p = p;

            weight = new EditText(83, 25, p[0] + 85, p[1] + 5, String.valueOf(e.getWeight()), true, true);
        }

        public void drawer(Graphics2D g){
            g.setColor(Color.WHITE);
            g.fillRect(p[0], p[1], width, height);

            g.setColor(Color.BLACK);
            g.drawRect(p[0], p[1], width, height);

            g.drawString("Weight: ", p[0] + 5, p[1] + 20);
            weight.drawer(g);
        }

        public void update(InputEntry inputEntry, long l){
            inputEntry.getMouseEntries().forEach(mouseEntry -> {
                if ((mouseEntry.getPoint().getX() + GraphDrawer.getHorizontal().getValue()) >= weight.getX() && (mouseEntry.getPoint().getX() + GraphDrawer.getHorizontal().getValue()) <= weight.getX() + weight.getWidth() && (mouseEntry.getPoint().getY() + GraphDrawer.getVertical().getValue()) >= weight.getY() && (mouseEntry.getPoint().getY() + GraphDrawer.getVertical().getValue()) <= weight.getY() + weight.getHeight()){
                    enabled = weight;
                }
            });

            if (enabled != null) {
                enabled.update(inputEntry, l);
            }

            if (enabled.equals(weight)){
                e.setWeight(Double.parseDouble(enabled.getText()));
            }
        }

        public int[] getP() {
            return p;
        }

        public Edge getE() {
            return e;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {

            return height;
        }
    }

    private class TextureChooser{
        private CheckBox textured;
        private String whatTexture;

        private int x;
        private int y;

        private Button white, black, red, green, blue, yellow, colorChooser, wood, sand, paper, leather, bricks, imageChooser;

        public TextureChooser(String whatTexture, int x, int y) {
            this.whatTexture = whatTexture;
            this.x = x;
            this.y = y;

            white = new Button(x+5, y+5, 40, 40, Color.WHITE, Color.WHITE, "");
            black = new Button(x+49, y+5, 40, 40, Color.BLACK, Color.BLACK, "");
            red = new Button(x+93, y+5, 40, 40, Color.RED, Color.RED, "");
            green = new Button(x+137, y+5, 40, 40, Color.GREEN, Color.GREEN, "");
            blue = new Button(x+181, y+5, 40, 40, Color.BLUE, Color.BLUE, "");
            yellow = new Button(x+225, y+5, 40, 40, Color.YELLOW, Color.YELLOW, "");

        }

        public void draw(Graphics2D g){
            g.setColor(Color.GRAY);
            g.fillRect(x, y, 310, 100);

            white.draw(g);
            black.draw(g);
            red.draw(g);
            green.draw(g);
            blue.draw(g);
            yellow.draw(g);
        }

        public void update(InputEntry inputEntry, long l){
            inputEntry.getMouseEntries().forEach(mouseEntry -> {
                if (white.isPushed(mouseEntry.getPoint())){
                    switch (whatTexture) {
                        case "Background":
                            graph.setBackground(getImage(Color.WHITE));
                            break;
                        case "Vertex":
                            graph.setVertexImage(getImage(Color.WHITE));
                            break;
                        case "Edge":
                            graph.setEdgeImage(getImage(Color.WHITE));
                            break;
                    }
                }else if (black.isPushed(mouseEntry.getPoint())){
                    switch (whatTexture) {
                        case "Background":
                            graph.setBackground(getImage(Color.BLACK));
                            break;
                        case "Vertex":
                            graph.setVertexImage(getImage(Color.BLACK));
                            break;
                        case "Edge":
                            graph.setEdgeImage(getImage(Color.BLACK));
                            break;
                    }
                }else if (red.isPushed(mouseEntry.getPoint())){
                    switch (whatTexture) {
                        case "Background":
                            graph.setBackground(getImage(Color.RED));
                            break;
                        case "Vertex":
                            graph.setVertexImage(getImage(Color.RED));
                            break;
                        case "Edge":
                            graph.setEdgeImage(getImage(Color.RED));
                            break;
                    }
                }else if (green.isPushed(mouseEntry.getPoint())){
                    switch (whatTexture) {
                        case "Background":
                            graph.setBackground(getImage(Color.GREEN));
                            break;
                        case "Vertex":
                            graph.setVertexImage(getImage(Color.GREEN));
                            break;
                        case "Edge":
                            graph.setEdgeImage(getImage(Color.GREEN));
                            break;
                    }
                }else if (blue.isPushed(mouseEntry.getPoint())){
                    switch (whatTexture) {
                        case "Background":
                            graph.setBackground(getImage(Color.BLUE));
                            break;
                        case "Vertex":
                            graph.setVertexImage(getImage(Color.BLUE));
                            break;
                        case "Edge":
                            graph.setEdgeImage(getImage(Color.BLUE));
                            break;
                    }
                }else if (yellow.isPushed(mouseEntry.getPoint())){
                    switch (whatTexture) {
                        case "Background":
                            graph.setBackground(getImage(Color.YELLOW));
                            break;
                        case "Vertex":
                            graph.setVertexImage(getImage(Color.YELLOW));
                            break;
                        case "Edge":
                            graph.setEdgeImage(getImage(Color.YELLOW));
                            break;
                    }
                }
            });
        }
    }
}
