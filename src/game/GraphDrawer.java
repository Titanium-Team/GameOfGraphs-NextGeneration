package game;

import de.SweetCode.e.E;
import de.SweetCode.e.input.InputEntry;
import graph.Edge;
import graph.Graph;
import graph.Vertex;
import mapEditor.MapEditorView;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class GraphDrawer {

    private static double zoom = 1.0;
    private static final double SCALE_STEP = 0.1d;
    private static Graph graph;
    private static double previousZoom = zoom;
    private static double scrollX = 0d;
    private static double scrollY = 0d;

    private static Scrollbar horizontal;
    private static Scrollbar vertical;

    public static void drawer(Graphics2D g, Graph graph, String whoAreYou){
        GraphDrawer.graph = graph;
        AffineTransform veryoldTransform = g.getTransform();
        g.scale(zoom, zoom);
        if (horizontal != null && vertical != null) {
            g.translate(-horizontal.getValue(), -vertical.getValue());
        }
        g.setStroke(new BasicStroke(2));
        Shape oldClip = g.getClip();
        AffineTransform oldTransform = g.getTransform();

        g.setClip(null);

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

        g.setTransform(oldTransform);
        g.setClip(oldClip);

        ArrayList<Edge> edgeList = graph.getEdges();
        if (whoAreYou.equals("MapEditor") && MapEditorView.getDragEdge() != null ) {
            edgeList.add(new Edge(new String[]{"equals", "equals"}, 0));
        }
        for (Edge edge:edgeList) {
            Vertex vertex1 = null, vertex2 = null;
            if (whoAreYou.equals("MapEditor") && edge.getVerticesId()[0].equals("equals") && edge.getVerticesId()[1].equals("equals")){
                vertex1 = MapEditorView.getDragEdge()[0];
                vertex2 = MapEditorView.getDragEdge()[1];
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

            // Weight
            Shape tmp = g.getClip();

            g.setClip(0, 0, E.getE().getSettings().getWidth(), E.getE().getSettings().getHeight());
            g.setColor(Color.BLACK);
            int startX = vertex1.getX(), startY = vertex1.getY();
            int endX = vertex2.getX(), endY = vertex2.getY();

            int weightX = (startX + endX) / 2;
            int weightY = (startY + endY) / 2;

            Font copyCat = g.getFont();

            double angle = Math.toDegrees(
                    Math.atan2(endY - startY, endX - startX)
            );

            if(angle < -90 || angle > 90) {
                angle += 180;
            }

            AffineTransform affineTransform = new AffineTransform();
            affineTransform.rotate(Math.toRadians(angle), 0, 0);
            Font rotatedFont = g.getFont().deriveFont(affineTransform);
            g.setFont(rotatedFont);
            g.drawString(String.format("%.2f", edge.getWeight()), weightX, weightY);
            g.setFont(copyCat);

            g.setClip(tmp);

        }

        g.setTransform(oldTransform);
        g.setClip(oldClip);

        ArrayList<Vertex> vertexList = graph.getVertices();
        for (Vertex vertex:vertexList) {
            if(vertex.getField().getPlayer() == GameOfGraphs.getGame().getPlayers().get(0)){
                g.setColor(Color.RED);
            } else if (vertex.getField().getPlayer() == GameOfGraphs.getGame().getPlayers().get(1)){
                g.setColor(Color.CYAN);
            }
            Ellipse2D ellipse2D = new Ellipse2D.Double(vertex.getX() - graph.getRadius(), vertex.getY() - graph.getRadius(), graph.getRadius() * 2, graph.getRadius() * 2);
            g.setClip(ellipse2D);


            if (whoAreYou.equals("MapEditor") && MapEditorView.getPreviewVertex() != null) {
                g.drawImage(MapEditorView.getPreviewVertex(), 0, 0, graph.getWidth(), graph.getHeight(), null);
            } else if (graph.getVertexImage() != null) {
                BufferedImage image = graph.getVertexImage();
                if (image.getWidth() == 1 && image.getHeight() == 1 || !graph.isVertexImageTextured()) {
                    g.fill(ellipse2D);
                    //g.drawImage(image, 0, 0, graph.getWidth(), graph.getHeight(), null);
                } else if (graph.isVertexImageTextured()) {
                    for (int i = 0; i < (Math.ceil((double) 1280 / (double) image.getWidth()) > 1 ? Math.ceil((double) 1280 / (double) image.getWidth()) : 1); i++) {
                        for (int j = 0; j < (Math.ceil(((double) 500) / ((double) image.getHeight())) > 1 ? Math.ceil(((double) 500) / ((double) image.getHeight())) : 1); j++) {
                            g.drawImage(image, i * image.getWidth(), j * image.getHeight(), image.getWidth(), image.getHeight(), null);
                        }
                    }
                }
            }
            g.setColor(Color.BLACK);

            g.setStroke(new BasicStroke(5));
            if (vertex.isMarkStart()){
                g.setColor(Color.ORANGE);
                g.drawOval(vertex.getX() - graph.getRadius(), vertex.getY() - graph.getRadius(), graph.getRadius()*2, graph.getRadius()*2);
            }else if (vertex.isMarkTarget()){
                g.setColor(Color.GREEN);
                g.drawOval(vertex.getX() - graph.getRadius(), vertex.getY() - graph.getRadius(), graph.getRadius()*2, graph.getRadius()*2);
            }
            g.setStroke(new BasicStroke(1));
            g.setColor(Color.BLACK);
        }

        g.setTransform(veryoldTransform);
        g.setClip(null);

        if (horizontal != null){
            horizontal.drawer(g);
        }
        if (vertical != null){
            vertical.drawer(g);
        }
    }

    public static void update(InputEntry inputEntry, long l){
        if(graph == null){
            return;
        }
        if (horizontal == null){
            horizontal = new Scrollbar(false, 1280-25, 25, 0, 475, 0, graph.getWidth()-1280, 25);
        }
        horizontal.update(inputEntry, l);

        if (vertical == null){
            vertical = new Scrollbar(true, 25, 500-25, 1280-25, 0, 0, graph.getHeight()-500, 25);
        }
        vertical.update(inputEntry, l);

        inputEntry.getMouseWheelEntries().forEach(mouseWheelEntry -> {

            double zoomFactor = -SCALE_STEP * mouseWheelEntry.getPreciseWheelRotation() * zoom;
            zoom = Math.abs(zoom + zoomFactor);

            Dimension d = new Dimension((int) (graph.getWidth() * zoom), (int) (graph.getHeight() * zoom));

            if (d.getWidth() <= 1280 - 25) {
                d.setSize(1280 - 25, d.getHeight());
                zoom = d.getWidth() / graph.getWidth();
            }

            if (d.getHeight() <= 500 - 25) {
                d.setSize(d.getWidth(), 500 - 25);
                zoom = d.getHeight() / graph.getHeight();
            }

            // scroll
            scrollX = (mouseWheelEntry.getPoint().getX() - horizontal.getValue()) / previousZoom * zoom - (mouseWheelEntry.getPoint().getX() - horizontal.getValue() * 2);
            scrollY = (mouseWheelEntry.getPoint().getY() - vertical.getValue()) / previousZoom * zoom - (mouseWheelEntry.getPoint().getY() - vertical.getValue() * 2);
           // horizontal.setEnd((int) (horizontal.getEnd() * SCALE_STEP));
          //  vertical.setEnd((int) (vertical.getEnd()/previousZoom*zoom));

            // zoom
            horizontal.setEnd((int) (d.getWidth()));
            vertical.setEnd(3000);

            previousZoom = zoom;

            // scroll
            horizontal.setValue((int) scrollX);
            vertical.setValue((int) scrollY);


        });

    }

    public static Scrollbar getHorizontal() {
        return horizontal;
    }

    public static Scrollbar getVertical() {
        return vertical;
    }

    public static double getZoom() {
        return zoom;
    }

    public static class Scrollbar{
        private boolean vertical;
        private int width;
        private int height;
        private int x;
        private int y;
        private int start;
        private int end;
        private int step;

        private int value;

        private boolean dragged = false;
        private int distance;

        private double factor;

        public Scrollbar(boolean vertical, int width, int height, int x, int y, int start, int end, int step) {
            this.vertical = vertical;
            this.width = width;
            this.height = height;
            this.x = x;
            this.y = y;
            this.start = start;
            this.end = end;
            this.step = step;

            if (vertical){
                factor = ((double) (height - width * 2 - 50) / (double) (end - start));
            }else {
                factor = ((double) (width - height * 2 - 50) / (double) (end - start));
            }
        }

        public void drawer(Graphics2D g){
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(x, y, width, height);


            g.setColor(Color.DARK_GRAY);
            if (vertical){
                g.fillRect(x, y, width, width);
                g.fillRect(x, height-width, width, width);
            }else {
                g.fillRect(x, y, height, height);
                g.fillRect(width-height, y, height, height);
            }

            g.setColor(Color.GRAY);
            if (vertical){
                g.fillRect(x, (int) (value*factor) + width, width, 50);
            }else {
                g.fillRect((int) (value*factor) + height, y, 50, height);
            }


        }

        public void update(InputEntry inputEntry, long l){
            //System.out.println(value + "  " + end + "   " + zoom);

            inputEntry.getMouseEntries().forEach(mouseEntry -> {
                if (vertical){
                    if (mouseEntry.getPoint().getX() >= x && mouseEntry.getPoint().getX() <= x + width && mouseEntry.getPoint().getY() >= (int) (value * factor) + width && mouseEntry.getPoint().getY() <= (int) (value * factor) + width + 50) {
                        if (mouseEntry.getButton() == 1) {
                            dragged = true;
                            distance = (int) (mouseEntry.getPoint().getY() / factor - value - width);
                        }
                    }

                    if (mouseEntry.getPoint().getX() >= x && mouseEntry.getPoint().getX() <= x + width && mouseEntry.getPoint().getY() >= y && mouseEntry.getPoint().getY() <= y + width) {
                        value = value - step;}
                    if (mouseEntry.getPoint().getX() >= x && mouseEntry.getPoint().getX() <= x + width && mouseEntry.getPoint().getY() >= y + height - width && mouseEntry.getPoint().getY() <= y + height) {
                        value = value + step;
                    }
                }else {
                    if (mouseEntry.getPoint().getX() >= (int) (value * factor) + height && mouseEntry.getPoint().getX() <= (int) (value * factor) + height + 50 && mouseEntry.getPoint().getY() >= y && mouseEntry.getPoint().getY() <= y + height) {
                        if (mouseEntry.getButton() == 1) {
                            dragged = true;
                            distance = (int) (mouseEntry.getPoint().getX() / factor - value - height);
                        }
                    }

                    if (mouseEntry.getPoint().getX() >= x && mouseEntry.getPoint().getX() <= x + height && mouseEntry.getPoint().getY() >= y && mouseEntry.getPoint().getY() <= y + height) {
                        value = value - step;}
                    if (mouseEntry.getPoint().getX() >= x + width - height && mouseEntry.getPoint().getX() <= x + width && mouseEntry.getPoint().getY() >= y && mouseEntry.getPoint().getY() <= y + height) {
                        value = value + step;
                    }
                }
            });
            inputEntry.getMouseReleasedQueue().forEach(mouseEntry -> {
                dragged = false;
            });

            inputEntry.getMouseDraggedEntries().forEach(mouseEntry -> {
                if (dragged){
                    if (vertical){
                        value = (int) ((mouseEntry.getPoint().getY() / factor - width - distance));
                    }else {
                        value = (int) ((mouseEntry.getPoint().getX() / factor - height - distance));
                    }
                }
            });

            if (value < start){
                value = start;
            }
            if (value > end){
                value = end;
            }
        }

        public int getValue() {
            return value;
        }

        public void setEnd(int end) {
            this.end = end;

            if (vertical){
                factor = ((double) (height - width * 2 - 50) / (double) (end - start));
            }else {
                factor = ((double) (width - height * 2 - 50) / (double) (end - start));
            }
        }

        public int getEnd() {
            return end;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }
}
