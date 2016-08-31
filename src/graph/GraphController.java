
package graph;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.smile.SmileFactory;
import field.Field;

import game.GameOfGraphs;
import game.Queue;
import mapEditor.MapEditorView;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class GraphController {
    private Graph graph;

    public GraphController() {
        Random r = new Random();

        graph = new Graph();

        for (int i = 0; i < 15; i++){
            graph.addVertex(new Vertex(r.nextInt(10000000) + "", r.nextInt(1195)+40, r.nextInt(395)+40, GameOfGraphs.getGame().getFieldController().createField(null,true)));
        }
        for (int j = 0; j < 7; j++){
            graph.addEdge(new Edge(new String[] {graph.getVertices().get(r.nextInt(graph.getVertices().size())).getID(), graph.getVertices().get(r.nextInt(graph.getVertices().size())).getID()}, r.nextInt(50)+1));
        }

        graph.setWidth(1280);
        graph.setHeight(500);
    }

    public Graph getGraph() {
        return graph;
    }



    public void save(Graph graph) {
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
                mapper.writeValue(graphFile, graph);

                File background = new File(file.getAbsolutePath() + "\\background.png");
                ImageIO.write(graph.getBackground(), "png", background);

                File vertex = new File(file.getAbsolutePath() + "\\vertexImage.png");
                ImageIO.write(graph.getVertexImage(), "png", vertex);

                File edge = new File(file.getAbsolutePath() + "\\edgeImage.png");
                ImageIO.write(graph.getEdgeImage(), "png", edge);

            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
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

    public Graph load() {
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

                File background = new File(file.getAbsolutePath() + "\\background.png");
                graphTemp.setBackground(ImageIO.read(background));

                File vertex = new File(file.getAbsolutePath() + "\\vertexImage.png");
                graphTemp.setVertexImage(ImageIO.read(vertex));

                File edge = new File(file.getAbsolutePath() + "\\edgeImage.png");
                graphTemp.setEdgeImage(ImageIO.read(edge));

                graph = graphTemp;

                /*thickness.setText(String.valueOf(graph.getThickness()));
                radius.setText(String.valueOf(graph.getRadius()));
                width.setText(String.valueOf(graph.getWidth()));
                height.setText(String.valueOf(graph.getHeight()));
                backgroundChooser.setTextured(graph.isBackgroundTextured());
                vertexChooser.setTextured(graph.isVertexImageTextured());
                edgeChooser.setTextured(graph.isEdgeImageTextured());*/

                return graphTemp;
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }
}
