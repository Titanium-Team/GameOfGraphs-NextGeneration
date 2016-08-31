
package graph;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.smile.SmileFactory;
import field.Field;

import game.GameOfGraphs;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class GraphController {
    private Graph graph;

    public GraphController() {
        graph = new Graph();
    }

    public Graph getGraph() {
        return graph;
    }

    public Graph newGraph(){
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

                return graph;


            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }
}
