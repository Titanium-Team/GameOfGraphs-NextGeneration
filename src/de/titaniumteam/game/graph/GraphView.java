package graph;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.smile.SmileFactory;
import game.GameOfGraphs;
import game.view.MenuView;
import game.view.View;
import mapEditor.MapEditorView;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GraphView extends View{
    private Graph graph;

    private JScrollPane jScrollPane;
    private GraphPanel graphPanel;

    public GraphView(MenuView viewMenu) {
        super(viewMenu);

        this.setLayout(null);

        Graph temp = GameOfGraphs.getGame().getGraphController().newGraph();
        if (temp != null) {
            graph = temp;

            graphPanel = new GraphPanel(this);
            graphPanel.setGraph(graph);
            jScrollPane = new JScrollPane(graphPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
            this.add(jScrollPane);

            jScrollPane.setVisible(true);
            jScrollPane.setWheelScrollingEnabled(false);
            jScrollPane.setBorder(null);
            jScrollPane.setLocation(0, 0);
            jScrollPane.setSize(new Dimension(1280, 540));
            graphPanel.setJScrollPane(jScrollPane);


            jScrollPane.revalidate();
            repaint();
        }
    }
}
