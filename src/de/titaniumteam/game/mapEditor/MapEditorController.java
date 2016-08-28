package mapEditor;

import field.Field;
import game.Queue;
import graph.*;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class MapEditorController {
    private Graph graph;
    private int chooser;
    private BufferedImage previewBackground, previewVertex, previewEdge;

    public MapEditorController() {
        graph = new Graph();
        chooser = 2;
    }

    public boolean vertexAdd(int x, int y) {
        int id = 0;
        ArrayList<Vertex> vertexList = graph.getVertices();
        for (Vertex vertex:vertexList){
            try {
                if (Integer.valueOf(vertex.getID()) > id) {
                    id = Integer.valueOf(vertex.getID());
                }
            }catch (NumberFormatException e){
            }
        }

        graph.addVertex(new Vertex(String.valueOf(id + 1), x, y, new Field(0, 0, null, null, null)));
        return true;
    }

    public boolean vertexSetPosition(Vertex pVertex, int x, int y) {
        ArrayList<Vertex> vertexNear = graph.getVertexList(pVertex, x, y, graph.getRadius() * 3);
        if (vertexNear.isEmpty()) {
            pVertex.setPosition(x, y);
            return true;
        }

        int iTemp = 0, xTemp = 0, yTemp = 0;
        for (Vertex vertex:vertexNear){
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


        Vertex vertex1 = graph.getVertex(pVertex, x, y, graph.getRadius() * 3);
        Vector vector1 = new Vector(vertex1.getX() - vector.getX(), vertex1.getY() - vector.getY());

        vector1.setX(vertex1.getX() - vector1.getX() + vertex1.getX());
        vector1.setY(vertex1.getY() - vector1.getY() + vertex1.getY());

        pVertex.setPosition((int) vector1.getX(), (int) vector1.getY());
        return false;
    }

    public boolean setId(Vertex pVertex, String id) {
        ArrayList<Vertex> vertexList = graph.getVertices();
        for (Vertex vertex:vertexList){
            if (vertex.getID().equalsIgnoreCase(id) && vertex != pVertex) {
                return false;
            }
        }
        pVertex.setId(id);
        return true;
    }

    public boolean setPopulation(Vertex vertex, int i) {
        if (i >= 0) {
            //TODO:setPopulation
            //vertex.getField().setPopulation(i);
            return true;
        }
        return false;
    }

    public boolean setMineLevel(Vertex vertex, int i) {
        if (i >= 0) {
            //TODO:setMineLevel
          //  vertex.getField().setMineLevel(i);
            return true;
        }
        return false;
    }

    public boolean setResources(Vertex vertex, int i) {
        if (i >= 0) {
            //TODO:setResources
          //  vertex.getField().setResources(i);
            return true;
        }
        return false;
    }

    public boolean setUnits(Vertex vertex, int i) {
        //TODO:Set Units
        return false;
    }

    public boolean setPlayer(Vertex vertex, int s) {
        //TODO:Set Player
        //vertex.getField().setPlayer();
        return true;
    }

    public void edgeAdd(String vertexId1, String vertexId2) {
        graph.addEdge(new Edge(new String[]{vertexId1, vertexId2}, 0));
    }

    public boolean checkGraph() {
        Queue<Vertex> biggestGroup = new Queue<>();
        int biggest = 0;

        ArrayList<Vertex> vertexList = graph.getVertices();
        for (Vertex vertex:graph.getVertices()){
            Queue<Vertex> vertexQueue = checkGraph(null, new Queue<Vertex>(), vertex);
            Queue<Vertex> tempGroup = checkGraph(null, new Queue<Vertex>(), vertex);
            int temp = 0;

            while (!vertexQueue.isEmpty()) {
                temp++;
                vertexList.remove(vertexQueue.front());
                vertexQueue.dequeue();
            }
            if (temp > biggest){
                biggest = temp;
                biggestGroup = tempGroup;
            }
        }

        graph.setAllVertexTargetMark(true);
        while (!biggestGroup.isEmpty()){
            biggestGroup.front().setMarkTarget(false);
            biggestGroup.dequeue();
        }
        if (graph.allVertexTargetMark(false)){
            return true;
        }else {
            return false;
        }
    }

    private Queue<Vertex> checkGraph(Queue<Vertex> q, Queue<Vertex> queue, Vertex start) {
        if (start != null && graph.getVertex(start.getID()) != null) {
            if (q == null) {
                graph.setAllVertexStartMark(false);
                q = new Queue<Vertex>();
                q.enqueue(start);
                start.setMarkStart(true);
                queue.enqueue(start);
            }
            if (!q.isEmpty()) {
                ArrayList<Vertex> neighbours = this.graph.getNeighbours(start);
                for (Vertex vertex:neighbours) {
                    if (!vertex.isMarkStart()) {
                        q.enqueue(vertex);
                        queue.enqueue(vertex);
                        vertex.setMarkStart(true);
                    }
                }
                q.dequeue();
                checkGraph(q, queue, q.front());
            }
        }
        return queue;
    }

    public boolean setEdgeWeight(Edge edge, double d) {
        if (d > 0) {
            edge.setWeight(d);
            return true;
        }
        return false;
    }

    public int getChooser() {
        return chooser;
    }

    public void setChooser(int chooser) {
        this.chooser = chooser;
    }

    public Graph getGraph() {
        return graph;
    }


    public BufferedImage getPreviewBackground() {
        return previewBackground;
    }

    public void setPreviewBackground(BufferedImage previewBackground) {
        if (previewBackground == null || previewBackground.getRGB(0,0) != graph.getVertexImage().getRGB(0,0) && previewBackground.getRGB(0,0) != graph.getEdgeImage().getRGB(0,0)) {
            this.previewBackground = previewBackground;
        }
    }

    public BufferedImage getPreviewVertex() {
        return previewVertex;
    }

    public void setPreviewVertex(BufferedImage previewVertex) {
        if (previewVertex == null || previewVertex.getRGB(0,0) != graph.getBackground().getRGB(0,0) && previewVertex .getRGB(0,0) != graph.getEdgeImage().getRGB(0,0)) {
            this.previewVertex = previewVertex;
        }
    }

    public BufferedImage getPreviewEdge() {
        return previewEdge;
    }

    public void setPreviewEdge(BufferedImage previewEdge) {
        if (previewEdge == null || previewEdge.getRGB(0,0) != graph.getBackground().getRGB(0,0) && previewEdge.getRGB(0,0) != graph.getVertexImage().getRGB(0,0)) {
            this.previewEdge = previewEdge;
        }
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }
}
