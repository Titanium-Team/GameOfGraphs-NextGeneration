package mapEditor;

import de.SweetCode.e.E;
import de.SweetCode.e.input.InputEntry;
import de.SweetCode.e.rendering.GameScene;
import de.SweetCode.e.rendering.layers.Layers;
import field.resource.Resources;
import game.GameOfGraphs;
import game.GraphDrawer;
import graph.Graph;
import graph.Vector;
import graph.Vertex;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class MapEditorView extends GameScene{
    private Graph graph;
    private static boolean dragEdge;
    private static Vertex[] dragEdgePos;

    private MapEditorController mapEditorController;
    private Vertex temp;

    private int chooser = 0;
    private static BufferedImage previewBackground, previewVertex, previewEdge;

    private PropertiesVertex propertiesVertex;

    public MapEditorView() {
        Random r = new Random();

        graph = new Graph();
        for (int i = 0; i < 20; i++){
            graph.addVertex(new Vertex(r.nextInt(10000) + "", r.nextInt(500), r.nextInt(500), GameOfGraphs.getGame().getFieldController().createField(null)));
        }
    }

    @Override
    public void render(Layers layers) {

        GraphDrawer.drawer(layers.first().getGraphics2D(), graph, "MapEditor");

        if (propertiesVertex != null){
            propertiesVertex.drawer(layers.first().getGraphics2D());
        }
    }

    @Override
    public void update(InputEntry inputEntry, long l) {
        if (propertiesVertex != null){
            propertiesVertex.update(inputEntry, l);
        }
        inputEntry.getMouseEntries().forEach(mouseEntry -> {
            propertiesVertex = null;
            //propertiesEdge.edge.setMark(false);

            final Vertex vertex = graph.getVertex((int)(mouseEntry.getPoint().getX()/GraphDrawer.getZoom()), (int)(mouseEntry.getPoint().getY()/GraphDrawer.getZoom()));

            if (mouseEntry.getButton() == 1){
                switch (chooser) {
                    case 0:
                        if (vertex != null) {
                            temp = vertex;
                        } else {
                            ArrayList<Vertex> vertexList = graph.getVertices();

                            //graph.addVertex(new Vertex(String.valueOf(vertexList.size() + 1), (int) (mouseEntry.getPoint().getX()/GraphDrawer.getZoom()), (int) (mouseEntry.getPoint().getY()/GraphDrawer.getZoom()), new Field(0, 0, null, 1, null)));

                            graph.addVertex(new Vertex(String.valueOf(vertexList.size() + 1), (int) mouseEntry.getPoint().getX(), (int) mouseEntry.getPoint().getY(), GameOfGraphs.getGame().getFieldController().createField(null)));
                        }
                        break;
                    case 1:
                        if (vertex != null) {
                            dragEdge = true;
                            temp = vertex;
                            dragEdgePos = new Vertex[]{new Vertex("equals", vertex.getX(), vertex.getY(), null), new Vertex("equals", vertex.getX(), vertex.getY(), null)};
                        }
                        break;
                }
            }else if (mouseEntry.getButton() == 3) {
                if (vertex != null) {
                    vertex.setMarkTarget(true);

                    //int x = (vertex.getX() * GraphDrawer.getZoom() + graph.getRadius() + propertiesVertex.getWidth()) > 500 ? (vertex.getX() * GraphDrawer.getZoom() - jScrollPane.getHorizontalScrollBar().getValue() - propertiesVertex.getWidth()) : (vertex.getX() * GraphDrawer.getZoom());
                    //int y = (vertex.getY() * GraphDrawer.getZoom() + graph.getRadius() + propertiesVertex.getHeight()) > 500 ? (vertex.getY() * GraphDrawer.getZoom() - jScrollPane.getVerticalScrollBar().getValue() - propertiesVertex.getHeight()) : (vertex.getY() * GraphDrawer.getZoom());

                    int x = 100;
                    int y = 100;


                    propertiesVertex = new PropertiesVertex(vertex, new int[]{x,y});

                   /* propertiesVertex.getRemove().addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            graph.removeVertex(vertex);

                            graphPanel.remove(propertiesVertex);
                            propertiesVertex = null;

                            graphPanel.revalidate();
                            graphPanel.repaint();
                        }
                    });*/
                }
            }
        });

        inputEntry.getMouseDraggedEntries().forEach(mouseEntry -> {
            switch (chooser){
                case 0:
                    if (temp!=null) {
                        double x = mouseEntry.getPoint().getX() / GraphDrawer.getZoom();
                        double y = mouseEntry.getPoint().getY() / GraphDrawer.getZoom();

                        /*if (mouseEntry.getPoint().getX() - graph.getRadius() - jScrollPane.getHorizontalScrollBar().getValue() < 0) {
                            x = jScrollPane.getHorizontalScrollBar().getValue()/graphPanel.getZoom()+graph.getRadius();
                        }else if (mouseEntry.getPoint().getX() + graph.getRadius() - jScrollPane.getHorizontalScrollBar().getValue() > 1263){
                            x = (1263+jScrollPane.getHorizontalScrollBar().getValue())/graphPanel.getZoom()-graph.getRadius();
                        }

                        if (mouseEntry.getPoint().getY() - graph.getRadius() - jScrollPane.getVerticalScrollBar().getValue() < 0){
                            y = jScrollPane.getVerticalScrollBar().getValue()/graphPanel.getZoom()+graph.getRadius();
                        }else if (e.getY() + graph.getRadius() - jScrollPane.getVerticalScrollBar().getValue() > 523){
                            y = (523+jScrollPane.getVerticalScrollBar().getValue())/graphPanel.getZoom()-graph.getRadius();
                        }*/

                        ArrayList<Vertex> vertexNear = graph.getVertexList(temp, (int) x, (int) y, graph.getRadius() * 3);
                        if (vertexNear.isEmpty()) {
                            temp.setPosition((int) x, (int) y);
                        }else {
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

                            temp.setPosition((int) vector1.getX(), (int) vector1.getY());
                        }
                    }
                    break;
                case 1:
                    //linePos[2] = (int)(mouseEntry.getPoint().getX()/GraphDrawer.getZoom());
                    //linePos[3] = (int)(mouseEntry.getPoint().getY()/GraphDrawer.getZoom());
            }
        });
    }

    @Override
    public boolean isActive() {
        return (E.getE().getScreen().getCurrent().getClass().equals(this.getClass()));
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

    public static Vertex[] getDragEdgePos() {
        return dragEdgePos;
    }

    public static boolean isDragEdge() {
        return dragEdge;
    }

    private class PropertiesVertex{
        private Vertex v;
        private int[] p;

        private EditText name, population, biom;

        public PropertiesVertex(Vertex v, int[] p) {
            this.v = v;
            this.p = p;

            name = new EditText(83, 25, p[0] - 5, p[1] - 5, v.getID(), false);
            population = new EditText(83, 25, p[0] - 5, p[1] - 30, v.getField().getResources().get(Resources.POPULATION).toString(), true);
        }

        public void drawer(Graphics2D g){
            g.drawRect(p[0], p[1], 175, 200);

            g.drawString("Name: ", p[0] - 5, p[1] - 5);
            name.drawer(g);

            g.drawString("Population: ", p[0] - 5, p[1] - 30);
            population.drawer(g);

            g.drawString("Biom", p[0] - 5, p[1] - 55);
            //biom.drawer(g);


        }

        public void update(InputEntry inputEntry, long l){
            v.setId(name.update(inputEntry, l));
            v.getField().getResources().put(Resources.POPULATION, Integer.parseInt(population.update(inputEntry, l)));
        }
    }

    private class EditText{
        private int width, height;
        private int x, y;

        private String text;
        private boolean onlyNumbers;

        public EditText(int width, int height, int x, int y, String text, boolean onlyNumbers) {
            this.width = width;
            this.height = height;
            this.x = x;
            this.y = y;
            this.text = text;
            this.onlyNumbers = onlyNumbers;
        }

        public void drawer(Graphics2D g){
            g.drawRect(x, y, width, height);

            g.setClip(new Rectangle(x, y, width, height));
            g.drawString(text, x, y);
        }

        public String update(InputEntry inputEntry, long l){
            inputEntry.getKeyEntries().forEach(keyEntry -> {
                if (onlyNumbers){
                    if (keyEntry.equals(1) || keyEntry.equals(2) || keyEntry.equals(3) || keyEntry.equals(4) || keyEntry.equals(5) || keyEntry.equals(6) || keyEntry.equals(7) || keyEntry.equals(8) || keyEntry.equals(9) || keyEntry.equals(0)){
                        text += keyEntry.getCharacter();
                    }
                }else {
                    text += keyEntry.getCharacter();
                }
            });

            return text;
        }
    }
}
