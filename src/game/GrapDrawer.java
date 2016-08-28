package game;

import graph.Edge;
import graph.Graph;
import graph.Vertex;

import java.awt.*;
import java.util.ArrayList;

public class GrapDrawer {
    public static void drawer(Graphics2D g, Graph graph){

        ArrayList<Vertex> vertices = graph.getVertices();
        for (Vertex v:vertices){
            g.fillOval(v.getX() - graph.getRadius()/2, v.getY() - graph.getRadius()/2, graph.getRadius()*2, graph.getRadius()*2);
        }

        ArrayList<Edge> edges = graph.getEdges();
        for (Edge e:edges){
            Vertex[] edgeVertex = e.getVerticesId(graph);
            g.drawLine(edgeVertex[0].getX(), edgeVertex[0].getY(), edgeVertex[1].getX(), edgeVertex[1].getY());
        }
    }
}
