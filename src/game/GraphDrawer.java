package game;

import graph.Edge;
import graph.Graph;
import graph.Vertex;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class GraphDrawer {

    private static double zoom = 1.0;
    private final double SCALE_STEP = 0.1d;
    private Point origin;
    private double previousZoom = zoom;
    private double scrollX = 0d;
    private double scrollY = 0d;

    public static void drawer(Graphics2D g, Graph graph, String whoAreYou){
        g.scale(zoom, zoom);
        g.setStroke(new BasicStroke(2));
        Shape oldClip = g.getClip();
        AffineTransform oldTransform =  g.getTransform();

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (whoAreYou.equals("MapEditor") && MapEditorView.getPreviewBackground() != null){
            g.drawImage(MapEditorView.getPreviewBackground(), 0, 0, graph.getWidth(), graph.getHeight(), null);
        }else if (graph.getBackground() != null){
            BufferedImage image = graph.getBackground();

            if (image.getWidth() == 1 && image.getHeight()==1 || !graph.isBackgroundTextured()){
                g.drawImage(image, 0, 0, graph.getWidth(), graph.getHeight(), null);
            }else if (graph.isBackgroundTextured()) {
                for (int i = 0; i < (Math.ceil((double) graph.getWidth() / (double) image.getWidth()) > 1 ? Math.ceil((double) graph.getWidth() / (double) image.getWidth()) : 1); i++) {
                    for (int j = 0; j < (Math.ceil(((double) graph.getHeight()) / ((double) image.getHeight())) > 1 ? Math.ceil(((double) graph.getHeight()) / ((double) image.getHeight())) : 1); j++) {
                        g.drawImage(image, i * image.getWidth(), j * image.getHeight(), image.getWidth(), image.getHeight(), null);
                    }
                }
            }
        }

        g.setClip(oldClip);

        ArrayList<Edge> edgeList = graph.getEdges();
        if (whoAreYou.equals("MapEditor") && MapEditorView.isDragEdge() ) {
            edgeList.add(new Edge(new String[]{"equals", "equals"}, 0));
        }
        for (Edge edge:edgeList) {
            Vertex vertex1 = null, vertex2 = null;
            if (whoAreYou.equals("MapEditor") && edge.getVerticesId()[0].equals("equals") && edge.getVerticesId()[1].equals("equals")){
                vertex1 = MapEditorView.getDragEdgePos()[0];
                vertex2 = MapEditorView.getDragEdgePos()[1];
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

            g.setClip(new Polygon(x, y, 4));

            if (whoAreYou.equals("MapEditor") && MapEditorView.getPreviewEdge() != null) {
                g.drawImage(MapEditorView.getPreviewEdge(), 0, 0, graph.getWidth(), graph.getHeight(), null);

            } else if (graph.getEdgeImage() != null) {
                BufferedImage image = graph.getEdgeImage();
                if (image.getWidth() == 1 && image.getHeight() == 1 || !graph.isEdgeImageTextured()) {
                    g.drawImage(image, 0, 0, graph.getWidth(), graph.getHeight(), null);

                } else if (graph.isEdgeImageTextured()) {
                    for (int i = 0; i < (Math.ceil((double) 1280 / (double) image.getWidth()) > 1 ? Math.ceil((double) 1280 / (double) image.getWidth()) : 1); i++) {
                        for (int j = 0; j < (Math.ceil(((double) 500) / ((double) image.getHeight())) > 1 ? Math.ceil(((double) 500) / ((double) image.getHeight())) : 1); j++) {
                            g.drawImage(image, i * image.getWidth(), j * image.getHeight(), image.getWidth(), image.getHeight(), null);
                        }
                    }
                }
            }

            if (edge.isMark()){
                g.setColor(Color.RED);
                g.drawPolyline(x, y, 4);
            }
        }

        g.setTransform(oldTransform);
        g.setClip(oldClip);

        ArrayList<Vertex> vertexList = graph.getVertices();
        for (Vertex vertex:vertexList) {
            Ellipse2D ellipse2D = new Ellipse2D.Double(vertex.getX() - graph.getRadius(), vertex.getY() - graph.getRadius(), graph.getRadius() * 2, graph.getRadius() * 2);
            g.setClip(ellipse2D);


            if (whoAreYou.equals("MapEditor") && MapEditorView.getPreviewVertex() != null) {
                g.drawImage(MapEditorView.getPreviewVertex(), 0, 0, graph.getWidth(), graph.getHeight(), null);
            } else if (graph.getVertexImage() != null) {
                BufferedImage image = graph.getVertexImage();
                if (image.getWidth() == 1 && image.getHeight() == 1 || !graph.isVertexImageTextured()) {
                    g.drawImage(image, 0, 0, graph.getWidth(), graph.getHeight(), null);
                } else if (graph.isVertexImageTextured()) {
                    for (int i = 0; i < (Math.ceil((double) 1280 / (double) image.getWidth()) > 1 ? Math.ceil((double) 1280 / (double) image.getWidth()) : 1); i++) {
                        for (int j = 0; j < (Math.ceil(((double) 500) / ((double) image.getHeight())) > 1 ? Math.ceil(((double) 500) / ((double) image.getHeight())) : 1); j++) {
                            g.drawImage(image, i * image.getWidth(), j * image.getHeight(), image.getWidth(), image.getHeight(), null);
                        }
                    }
                }
            }

            if (vertex.isMarkStart()){
                g.setColor(Color.BLACK);
                g.drawOval(vertex.getX() - graph.getRadius(), vertex.getY() - graph.getRadius(), graph.getRadius()*2, graph.getRadius()*2);
            }else if (vertex.isMarkTarget()){
                g.setColor(Color.RED);
                g.drawOval(vertex.getX() - graph.getRadius(), vertex.getY() - graph.getRadius(), graph.getRadius()*2, graph.getRadius()*2);
            }
        }

        g.dispose();

        //jScrollPane.getVerticalScrollBar().repaint();
        //jScrollPane.getHorizontalScrollBar().repaint();


        //if (view instanceof MapEditorView && ((MapEditorView) view).getPropertiesPanel() != null) {
       //     ((MapEditorView) view).getPropertiesPanel().repaint();
        //}
    }

    public static double getZoom() {
        return zoom;
    }
}
