package mapEditor;

import de.SweetCode.e.input.InputEntry;
import de.SweetCode.e.input.MouseEntry;
import de.SweetCode.e.rendering.GameScene;
import de.SweetCode.e.rendering.layers.Layers;
import field.Field;
import field.resource.Resource;
import field.resource.Resources;
import game.GraphDrawer;
import graph.Graph;
import graph.GraphPanel;
import graph.Vertex;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.stream.Stream;

public class MapEditorView extends GameScene{
    private Graph graph;
    private boolean dragEdge;
    private Vertex[] dragEdgePos;

    private MapEditorController mapEditorController;
    private Vertex temp;

    private int chooser = 2;
    private BufferedImage previewBackground, previewVertex, previewEdge;

    private PropertiesVertex propertiesVertex;

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
        inputEntry.getMouseEntries().forEachOrdered(mouseEntry -> {
            propertiesVertex = null;
            //propertiesEdge.edge.setMark(false);

            final Vertex vertex = graph.getVertex((int)(mouseEntry.getPoint().getX()/GraphDrawer.getZoom()), (int)(mouseEntry.getPoint().getY()/GraphDrawer.getZoom()));

            if (mouseEntry.getButton() == 1){
                switch (chooser) {
                    case 0:
                        if (vertex != null) {
                            temp = vertex;
                        } else {
                            int id = 0;
                            ArrayList<Vertex> vertexList = graph.getVertices();
                            for (Vertex v:vertexList){
                                try {
                                    if (Integer.valueOf(vertex.getID()) > id) {
                                        id = Integer.valueOf(vertex.getID());
                                    }
                                }catch (NumberFormatException e){
                                }
                            }

                            graph.addVertex(new Vertex(String.valueOf(id + 1), (int) (mouseEntry.getPoint().getX()/GraphDrawer.getZoom()), (int) (mouseEntry.getPoint().getY()/GraphDrawer.getZoom()), new Field(0, 0, null, null, null)));
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
    }

    @Override
    public boolean isActive() {
        return false;
    }
    
    private class PropertiesVertex{
        private Vertex v;
        private int[] p;

        private EditText name, population;

        public PropertiesVertex(Vertex v, int[] p) {
            this.v = v;
            this.p = p;

            name = new EditText(83, 50, p[0] - 5, p[1] - 5, v.getID(), false);
            population = new EditText(83, 50, p[0] - 5, p[1] - 55, v.getField().getResources().get(Resources.POPULATION).toString(), true);

        }

        public void drawer(Graphics2D g){
            g.drawRect(p[0], p[1], 175, 200);

            g.drawString("Name: ", p[0] - 5, p[1] - 5);
            name.drawer(g);

            g.drawString("Population: ", p[0] - 5, p[1] - 55);
            population.drawer(g);
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
            inputEntry.getKeyEntries().forEachOrdered(keyEntry -> {
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
