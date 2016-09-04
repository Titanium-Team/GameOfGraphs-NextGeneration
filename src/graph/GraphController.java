
package graph;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.smile.SmileFactory;
import game.GameOfGraphs;
import game.Player;
import game.Queue;
import ki.KIFraction;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class GraphController {
    private Graph graph;

    public GraphController() {
        Random r = new Random();

        graph = new Graph();

        KIFraction fraction1 = new KIFraction("Independent", new Color(232, 77, 91));
        GameOfGraphs.getGame().getPlayers().add(fraction1);

        /*Vertex a = new Vertex(r.nextInt(10000000) + "", r.nextInt(1195)+40, r.nextInt(395)+40, GameOfGraphs.getGame().getFieldController().createField(GameOfGraphs.getGame().getPlayers().get(0),true));
        Vertex c = new Vertex(r.nextInt(10000000) + "", r.nextInt(1195)+40, r.nextInt(395)+40, GameOfGraphs.getGame().getFieldController().createField(GameOfGraphs.getGame().getPlayers().get(1),true));

        Vertex b = new Vertex(r.nextInt(10000000) + "", r.nextInt(1195)+40, r.nextInt(395)+40, GameOfGraphs.getGame().getFieldController().createField(fraction1 ,true));
        KIFraction fraction2 = new KIFraction("KIplayer 1");
        Vertex d = new Vertex(r.nextInt(10000000) + "", r.nextInt(1195)+40, r.nextInt(395)+40, GameOfGraphs.getGame().getFieldController().createField(fraction2,true));
        Vertex e = new Vertex(r.nextInt(10000000) + "", r.nextInt(1195)+40, r.nextInt(395)+40, GameOfGraphs.getGame().getFieldController().createField(fraction2,true));

        graph.addVertex(a);
        graph.addVertex(b);
        graph.addVertex(c);
        graph.addVertex(d);
        graph.addVertex(e);

        graph.addEdge(new Edge(new String[] { a.getID(), b.getID() }, 50));
        graph.addEdge(new Edge(new String[] { b.getID(), c.getID() }, 50));
        graph.addEdge(new Edge(new String[] { c.getID(), d.getID() }, 50));
        graph.addEdge(new Edge(new String[] { d.getID(), a.getID() }, 50));
        graph.addEdge(new Edge(new String[]{e.getID(),a.getID()},38));
        graph.addEdge(new Edge(new String[]{e.getID(),c.getID()},38));



        GameOfGraphs.getGame().getPlayers().add(fraction2);
        /*for (int i = 0; i < 15; i++){
            graph.addVertex(new Vertex(r.nextInt(10000000) + "", r.nextInt(1195)+40, r.nextInt(395)+40, GameOfGraphs.getGame().getFieldController().createField(GameOfGraphs.getGame().getPlayers().get(2),false)));
        }
        for (int j = 0; j < 12; j++){
            graph.addEdge(new Edge(new String[] {graph.getVertices().get(r.nextInt(graph.getVertices().size())).getID(), graph.getVertices().get(r.nextInt(graph.getVertices().size())).getID()}, r.nextInt(50)+1));
        }*/

        graph.setWidth(1280);
        graph.setHeight(500);
    }

    public Graph getGraph() {
        return graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;

        ArrayList<Player> players = new ArrayList<>();

        boolean add = true;

        ArrayList<Vertex> vertices = graph.getVertices();
        for (Vertex v:vertices){
            for (Player temp : players) {
                if (v.getField().getPlayer().getName().equals(temp.getName())) {
                    add = false;
                    break;
                }
            }

            if (add){
                players.add(v.getField().getPlayer());
            }
            add = true;
        }

        GameOfGraphs.getGame().setPlayers(players);
        GameOfGraphs.getGame().nextTurn();
    }

    public void save(Graph graph) {
        if (GameOfGraphs.getGame().getPlayers().size() < 2) {
            int value = JOptionPane.showConfirmDialog(null, "You need at leat 2 Players, thats why you can't play this map. Do you still want to save it?", "Save?", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
            if (value == JOptionPane.YES_OPTION) {
                graph.setChecked(false);
            } else {
                return;
            }
        }
        if (!checkGraph()){
            int value = JOptionPane.showConfirmDialog(null, "Not all vertecies are joined, thats why you can't play this map. Do you still want to save it?", "Save?", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
            if (value == JOptionPane.YES_OPTION){
                graph.setChecked(false);
            }else {
                return;
            }
        }else if (!graph.isEmpty()){
            graph.setChecked(true);
        }

        String name = JOptionPane.showInputDialog(null, "How should the map be called?", "Name", JOptionPane.QUESTION_MESSAGE);
        if (name == null){
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);

        int i = fileChooser.showSaveDialog(null);

        if (i!=JFileChooser.CANCEL_OPTION) {
            File file = new File(fileChooser.getSelectedFile().getAbsolutePath() + "\\" + name);
            try {
                file.mkdir();

                File graphFile = new File(file.getAbsolutePath() + "\\graph.gog");
                ObjectMapper mapper = new ObjectMapper(new SmileFactory());
                mapper.configure(SerializationFeature.INDENT_OUTPUT, true);

                mapper.writeValue(graphFile, graph);

            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    public boolean checkGraph() {
        Queue<Vertex> biggestGroup = new Queue<>();
        int biggest = 0;

        Set<Player> players = new HashSet<>();

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

            if(!(vertex.getField().getPlayer() == null)) {
                players.add(vertex.getField().getPlayer());
            }

        }

        if(!(GameOfGraphs.getGame().getPlayers().size() == players.size())) {
            return false;
        }

        graph.setAllVertexTargetMark(true);
        while (!biggestGroup.isEmpty()){
            biggestGroup.front().setMarkTarget(false);
            biggestGroup.dequeue();
        }

        if (!(graph.allVertexTargetMark(false))) {
            return false;
        }

        return true;
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

    public Object[] load() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);

        int i = fileChooser.showOpenDialog(null);

        if (i!=JFileChooser.CANCEL_OPTION) {
            File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
            try {
                File graphFile = new File(file.getAbsolutePath() + "\\graph.gog");
                ObjectMapper mapper = new ObjectMapper(new SmileFactory());
                Graph graphTemp = mapper.readValue(graphFile, Graph.class);

                setGraph(graphTemp);

                /*thickness.setText(String.valueOf(graph.getThickness()));
                radius.setText(String.valueOf(graph.getRadius()));
                width.setText(String.valueOf(graph.getWidth()));
                height.setText(String.valueOf(graph.getHeight()));
                backgroundChooser.setTextured(graph.isBackgroundTextured());
                vertexChooser.setTextured(graph.isVertexImageTextured());
                edgeChooser.setTextured(graph.isEdgeImageTextured());*/

                return new Object[] {
                    graph,
                    file.getName()
                };
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }
}
