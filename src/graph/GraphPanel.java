package graph;

import de.SweetCode.e.input.InputEntry;
import de.SweetCode.e.rendering.GameScene;
import de.SweetCode.e.rendering.layers.Layers;
import game.GameOfGraphs;
import game.view.View;
import mapEditor.MapEditorView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class GraphPanel extends GameScene {
    private JScrollPane jScrollPane;
    private Graph graph;
    private View view;

    private double zoom = 1.0;
    private final double SCALE_STEP = 0.1d;
    private Point origin;
    private double previousZoom = zoom;
    private double scrollX = 0d;
    private double scrollY = 0d;

    public GraphPanel(View pView) {
        this.setLayout(null);

        this.view = pView;

        if (view instanceof MapEditorView){
            graph = GameOfGraphs.getGame().getMapEditorController().getGraph();
        }


        this.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {

                double zoomFactor = - SCALE_STEP*e.getPreciseWheelRotation()*zoom;
                zoom = Math.abs(zoom + zoomFactor);

                Dimension d = new Dimension((int)(graph.getWidth()*zoom), (int)(graph.getHeight()*zoom));


                if (d.getWidth() <= 1263) {
                    d.setSize(1263, d.getHeight());
                    zoom = d.getWidth()/graph.getWidth();
                }

                if (d.getHeight() <= 523) {
                    d.setSize(d.getWidth(), 523);
                    zoom = d.getHeight()/graph.getHeight();
                }

                setPreferredSize(d);
                setSize(d);
                revalidate();

                Rectangle visibleRect = getVisibleRect();

                scrollX = e.getX() / previousZoom * zoom - (e.getX() - visibleRect.getX());
                scrollY = e.getY() / previousZoom * zoom - (e.getY() - visibleRect.getY());

                visibleRect.setRect(scrollX, scrollY, visibleRect.getWidth(), visibleRect.getHeight());
                scrollRectToVisible(visibleRect);

                previousZoom = zoom;

                repaint();
            }
        });

        this.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);

                if (origin != null) {
                    int deltaX = origin.x - e.getX();
                    int deltaY = origin.y - e.getY();
                    Rectangle view = getVisibleRect();
                    view.x += deltaX;
                    view.y += deltaY;
                    scrollRectToVisible(view);
                }
            }
        });

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);

                if ((view instanceof MapEditorView && GameOfGraphs.getGame().getMapEditorController().getChooser() == 2 || view instanceof MapEditorView == false) && e.getButton() == 1){
                    origin = new Point(e.getPoint());
                }else {
                    origin = null;
                }
            }
        });
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public void setJScrollPane(JScrollPane jScrollPane) {
        this.jScrollPane = jScrollPane;
    }

    public double getZoom() {
        return zoom;
    }

    @Override
    public void render(Layers layers) {

        Graphics2D graphics2D = layers.first().getGraphics2D();

        graphics2D.scale(zoom, zoom);
        graphics2D.setStroke(new BasicStroke(2));
        Shape oldClip = graphics2D.getClip();
        AffineTransform oldTransform =  graphics2D.getTransform();

        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (view instanceof MapEditorView && GameOfGraphs.getGame().getMapEditorController().getPreviewBackground() != null){
            graphics2D.drawImage(GameOfGraphs.getGame().getMapEditorController().getPreviewBackground(), 0, 0, graph.getWidth(), graph.getHeight(), null);
        }else if (graph.getBackground() != null){
            BufferedImage image = graph.getBackground();

            if (image.getWidth() == 1 && image.getHeight()==1 || !graph.isBackgroundTextured()){
                graphics2D.drawImage(image, 0, 0, graph.getWidth(), graph.getHeight(), null);
            }else if (graph.isBackgroundTextured()) {
                for (int i = 0; i < (Math.ceil((double) graph.getWidth() / (double) image.getWidth()) > 1 ? Math.ceil((double) graph.getWidth() / (double) image.getWidth()) : 1); i++) {
                    for (int j = 0; j < (Math.ceil(((double) graph.getHeight()) / ((double) image.getHeight())) > 1 ? Math.ceil(((double) graph.getHeight()) / ((double) image.getHeight())) : 1); j++) {
                        graphics2D.drawImage(image, i * image.getWidth(), j * image.getHeight(), image.getWidth(), image.getHeight(), null);
                    }
                }
            }
        }

        graphics2D.setClip(oldClip);

        ArrayList<Edge> edgeList = graph.getEdges();
        if (view instanceof MapEditorView && ((MapEditorView) view).isLine()) {
            edgeList.add(new Edge(new String[]{"equals", "equals"}, 0));
        }
        for (Edge edge:edgeList) {
            Vertex vertex1 = null, vertex2 = null;
            if (view instanceof MapEditorView && edge.getVerticesId()[0].equals("equals") && edge.getVerticesId()[1].equals("equals")){
                vertex1 = new Vertex("", ((MapEditorView) view).getLinePos()[0], ((MapEditorView) view).getLinePos()[1], null);
                vertex2 = new Vertex("", ((MapEditorView) view).getLinePos()[2], ((MapEditorView) view).getLinePos()[3], null);
            }else {
                vertex1 = edge.getVerticesId(graph)[0];
                vertex2 = edge.getVerticesId(graph)[1];
            }

            int[] x = new int[4];
            int[] y = new int[4];
            if (vertex1.getY() <= vertex2.getY()){
                y[0] = vertex1.getY()-graph.getThickness()/2;
                y[1] = vertex2.getY()-graph.getThickness()/2;
                y[2] = vertex2.getY()+graph.getThickness()/2;
                y[3] = vertex1.getY()+graph.getThickness()/2;
            }else {
                y[0] = vertex1.getY()+graph.getThickness()/2;
                y[1] = vertex2.getY()+graph.getThickness()/2;
                y[2] = vertex2.getY()-graph.getThickness()/2;
                y[3] = vertex1.getY()-graph.getThickness()/2;
            }
            if (vertex1.getX() >= vertex2.getX()){
                x[0] = vertex1.getX()-graph.getThickness()/2;
                x[1] = vertex2.getX()-graph.getThickness()/2;
                x[2] = vertex2.getX()+graph.getThickness()/2;
                x[3] = vertex1.getX()+graph.getThickness()/2;
            }else{
                x[0] = vertex1.getX()+graph.getThickness()/2;
                x[1] = vertex2.getX()+graph.getThickness()/2;
                x[2] = vertex2.getX()-graph.getThickness()/2;
                x[3] = vertex1.getX()-graph.getThickness()/2;
            }

            graphics2D.setClip(new Polygon(x, y, 4));

            /*Vector vector = new Vector(vertex2.getX() - vertex1.getX(), vertex2.getY() - vertex1.getY());

            Rectangle2D r = new Rectangle2D.Float(vertex1.getX()-graph.getThickness()/2, vertex1.getY(), graph.getThickness(), (float) vector.length());

            AffineTransform affineTransform = new AffineTransform();
            affineTransform.setToRotation(vertex2.getX() - vertex1.getX(), vertex2.getY() - vertex1.getY(), vertex1.getX(), vertex1.getY());
            affineTransform.rotate(Math.toRadians(-90), vertex1.getX(), vertex1.getY());
            graphics2D.setTransform(affineTransform);

            graphics2D.setClip(r);*/

            if (view instanceof MapEditorView && GameOfGraphs.getGame().getMapEditorController().getPreviewEdge() != null) {
                graphics2D.drawImage(GameOfGraphs.getGame().getMapEditorController().getPreviewEdge(), 0, 0, graph.getWidth(), graph.getHeight(), null);

            } else if (graph.getEdgeImage() != null) {
                BufferedImage image = graph.getEdgeImage();
                if (image.getWidth() == 1 && image.getHeight() == 1 || !graph.isEdgeImageTextured()) {
                    graphics2D.drawImage(image, 0, 0, graph.getWidth(), graph.getHeight(), null);

                } else if (graph.isEdgeImageTextured()) {
                    for (int i = 0; i < (Math.ceil((double) this.getWidth() / (double) image.getWidth()) > 1 ? Math.ceil((double) this.getWidth() / (double) image.getWidth()) : 1); i++) {
                        for (int j = 0; j < (Math.ceil(((double) this.getWidth()) / ((double) image.getHeight())) > 1 ? Math.ceil(((double) this.getWidth()) / ((double) image.getHeight())) : 1); j++) {
                            graphics2D.drawImage(image, i * image.getWidth(), j * image.getHeight(), image.getWidth(), image.getHeight(), null);
                        }
                    }
                }
            }

            if (edge.isMark()){
                graphics2D.setColor(Color.RED);
                graphics2D.drawPolyline(x, y, 4);
            }
        }

        graphics2D.setTransform(oldTransform);
        graphics2D.setClip(oldClip);

        ArrayList<Vertex> vertexList = graph.getVertices();
        for (Vertex vertex:vertexList) {
            Ellipse2D ellipse2D = new Ellipse2D.Double(vertex.getX() - graph.getRadius(), vertex.getY() - graph.getRadius(), graph.getRadius() * 2, graph.getRadius() * 2);
            graphics2D.setClip(ellipse2D);


            if (view instanceof MapEditorView && GameOfGraphs.getGame().getMapEditorController().getPreviewVertex() != null) {
                graphics2D.drawImage(GameOfGraphs.getGame().getMapEditorController().getPreviewVertex(), 0, 0, graph.getWidth(), graph.getHeight(), null);
            } else if (graph.getVertexImage() != null) {
                BufferedImage image = graph.getVertexImage();
                if (image.getWidth() == 1 && image.getHeight() == 1 || !graph.isVertexImageTextured()) {
                    graphics2D.drawImage(image, 0, 0, graph.getWidth(), graph.getHeight(), null);
                } else if (graph.isVertexImageTextured()) {
                    for (int i = 0; i < (Math.ceil((double) this.getWidth() / (double) image.getWidth()) > 1 ? Math.ceil((double) this.getWidth() / (double) image.getWidth()) : 1); i++) {
                        for (int j = 0; j < (Math.ceil(((double) this.getHeight()) / ((double) image.getHeight())) > 1 ? Math.ceil(((double) this.getHeight()) / ((double) image.getHeight())) : 1); j++) {
                            graphics2D.drawImage(image, i * image.getWidth(), j * image.getHeight(), image.getWidth(), image.getHeight(), null);
                        }
                    }
                }
            }

            if (vertex.isMarkStart()){
                graphics2D.setColor(Color.BLACK);
                graphics2D.drawOval(vertex.getX() - graph.getRadius(), vertex.getY() - graph.getRadius(), graph.getRadius()*2, graph.getRadius()*2);
            }else if (vertex.isMarkTarget()){
                graphics2D.setColor(Color.RED);
                graphics2D.drawOval(vertex.getX() - graph.getRadius(), vertex.getY() - graph.getRadius(), graph.getRadius()*2, graph.getRadius()*2);
            }
        }


        graphics2D.dispose();


        jScrollPane.getVerticalScrollBar().repaint();
        jScrollPane.getHorizontalScrollBar().repaint();


        if (view instanceof MapEditorView && ((MapEditorView) view).getPropertiesPanel() != null) {
            ((MapEditorView) view).getPropertiesPanel().repaint();
        }

    }

    @Override
    public void update(InputEntry inputEntry, long l) {

    }

    @Override
    public boolean isActive() {
        return false;
    }
}
