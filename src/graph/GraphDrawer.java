package graph;

import de.SweetCode.e.E;
import de.SweetCode.e.input.InputEntry;
import mapEditor.MapEditor;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class GraphDrawer {


    private static Graph graph;

    private static Scrollbar horizontal;
    private static Scrollbar vertical;

    public static void drawer(Graphics2D g, Graph graph, String whoAreYou){
        GraphDrawer.graph = graph;
        AffineTransform veryoldTransform = g.getTransform();
        if (horizontal != null && vertical != null) {
            g.translate(-horizontal.getValue(), -vertical.getValue());
        }
        g.setStroke(new BasicStroke(2));
        Shape oldClip = g.getClip();
        AffineTransform oldTransform = g.getTransform();

        if (graph.getBackground() != null){
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
        if (whoAreYou.equals("MapEditor") && MapEditor.getDragEdge() != null ) {
            edgeList.add(new Edge(new String[]{"equals", "equals"}, 0));
        }
        for (Edge edge:edgeList) {
            Vertex vertex1 = null, vertex2 = null;
            if (whoAreYou.equals("MapEditor") && edge.getVerticesId()[0].equals("equals") && edge.getVerticesId()[1].equals("equals")){
                vertex1 = MapEditor.getDragEdge()[0];
                vertex2 = MapEditor.getDragEdge()[1];
                if (vertex1 == null){
                    break;
                }
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

            if (graph.getEdgeImage() != null) {
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
            Ellipse2D ellipse2D = new Ellipse2D.Double(vertex.getX() - graph.getRadius(), vertex.getY() - graph.getRadius(), graph.getRadius() * 2, graph.getRadius() * 2);
            g.setClip(ellipse2D);


            if (graph.getVertexImage() != null) {
                BufferedImage image = getImage(vertex.getField().getPlayer().getColor());
                if (image.getWidth() == 1 && image.getHeight() == 1 || !graph.isVertexImageTextured()) {
                    g.fill(ellipse2D);
                    g.drawImage(image, 0, 0, graph.getWidth(), graph.getHeight(), null);
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
            }else if (vertex.isMark()){
                g.setColor(new Color(139, 69, 19));
                g.drawOval(vertex.getX() - graph.getRadius(), vertex.getY() - graph.getRadius(), graph.getRadius()*2, graph.getRadius()*2);
            }
            g.setStroke(new BasicStroke(1));
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
            horizontal = new Scrollbar(false, 1280-25, 25, 0, 475, 0, graph.getWidth()-1280+25, 25);
        }
        horizontal.update(inputEntry, l);

        if (vertical == null){
            vertical = new Scrollbar(true, 25, 500-25, 1280-25, 0, 0, graph.getHeight()-500+25, 25);
        }
        vertical.update(inputEntry, l);
    }

    public static BufferedImage getImage(Color color){
        BufferedImage bufferedImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        bufferedImage.setRGB(0, 0, color.getRGB());
        return bufferedImage;
    }

    public static Scrollbar getHorizontal() {
        return horizontal;
    }

    public static Scrollbar getVertical() {
        return vertical;
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
            //System.out.println(value/zoom + "  " + end/zoom + "   " + graph.getWidth() + "   " + zoom);

            inputEntry.getMouseEntries().forEach(mouseEntry -> {
                if (vertical){
                    if (mouseEntry.getPoint().getX() >= x && mouseEntry.getPoint().getX() <= x + width && mouseEntry.getPoint().getY() >= (int) (value * factor) + width && mouseEntry.getPoint().getY() <= (int) (value * factor) + width + 50) {
                        if (mouseEntry.getButton() == 1) {
                            dragged = true;
                            distance = (int) (mouseEntry.getPoint().getY() / factor - value);
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
                            distance = (int) (mouseEntry.getPoint().getX() / factor - value);
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
                        value = (int) ((mouseEntry.getPoint().getY() / factor - distance));
                    }else {
                        value = (int) ((mouseEntry.getPoint().getX() / factor - distance));
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
