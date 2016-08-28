package mapEditor;

import game.GameOfGraphs;
import game.view.MenuView;
import game.view.View;
import graph.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MapEditorView extends View{

    private static Graph graph;

	private Vertex temp;
	private boolean line;
	private int[] linePos;
	private PropertiesVertex propertiesVertex;
    private PropertiesEdge propertiesEdge;

    private MapEditorController mapEditorController;

    private static JScrollPane jScrollPane;
    private static GraphPanel graphPanel;

	public MapEditorView(MenuView viewMenu) {
		super(viewMenu);

        this.setLayout(null);

        mapEditorController = GameOfGraphs.getGame().getMapEditorController();

        line = false;
        linePos = new int[4];
        graph = GameOfGraphs.getGame().getMapEditorController().getGraph();

        graphPanel = new GraphPanel(this);

        jScrollPane = new JScrollPane(graphPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        this.add(jScrollPane);



        jScrollPane.setVisible(true);
        jScrollPane.setWheelScrollingEnabled(false);
        jScrollPane.setBorder(null);

        jScrollPane.setLocation(0, 0);
        jScrollPane.setSize(new Dimension(1280, 540));

        graphPanel.setJScrollPane(jScrollPane);

        graphPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                if (propertiesVertex != null){
                    propertiesVertex.vertex.setMarkTarget(false);
                    graphPanel.remove(propertiesVertex);
                    propertiesVertex = null;
                }
                if (propertiesEdge != null){
                    propertiesEdge.edge.setMark(false);
                    graphPanel.remove(propertiesEdge);
                    propertiesEdge = null;
                }
                final Vertex vertex = graph.getVertex((int)(e.getX()/graphPanel.getZoom()), (int)(e.getY()/graphPanel.getZoom()));
                if (e.getButton() == 1) {
                    switch (mapEditorController.getChooser()) {
                        case 0:
                            if (vertex != null) {
                                temp = vertex;
                            } else {
                                mapEditorController.vertexAdd((int)(e.getX()/graphPanel.getZoom()), (int)(e.getY()/graphPanel.getZoom()));
                            }
                            break;
                        case 1:
                            if (vertex != null) {
                                line = true;
                                temp = vertex;
                                linePos = new int[]{vertex.getX(), vertex.getY(), vertex.getX(), vertex.getY()};
                            }
                            break;
                    }
                }else if (e.getButton() == 3) {
                    if (vertex != null) {
                        vertex.setMarkTarget(true);

                        propertiesVertex = new PropertiesVertex(vertex);

                        double x = (vertex.getX()*graphPanel.getZoom() + graph.getRadius() + propertiesVertex.getWidth()) > getWidth() ? (vertex.getX()*graphPanel.getZoom()-jScrollPane.getHorizontalScrollBar().getValue() - propertiesVertex.getWidth()) : (vertex.getX()*graphPanel.getZoom());
                        double y = (vertex.getY()*graphPanel.getZoom() + graph.getRadius() + propertiesVertex.getHeight()) > getHeight() ? (vertex.getY()*graphPanel.getZoom()-jScrollPane.getVerticalScrollBar().getValue() - propertiesVertex.getHeight()) : (vertex.getY()*graphPanel.getZoom());

                        propertiesVertex.setLocation((int) x, (int) y);

                        graphPanel.add(propertiesVertex);


                        propertiesVertex.getRemove().addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                graph.removeVertex(vertex);

                                graphPanel.remove(propertiesVertex);
                                propertiesVertex = null;

                                graphPanel.revalidate();
                                graphPanel.repaint();
                            }
                        });
                    } else {
                        final Edge edge = graph.getEdge((int)(e.getX()/graphPanel.getZoom()), (int)(e.getY()/graphPanel.getZoom()), 10);
                        if (edge != null) {
                            edge.setMark(true);
                            propertiesEdge = new PropertiesEdge(edge);

                            double x = (e.getX()-jScrollPane.getHorizontalScrollBar().getValue() + graph.getRadius() + propertiesEdge.getWidth()) > getWidth() ? (e.getX() - propertiesEdge.getWidth()) : (e.getX());
                            double y = (e.getY()-jScrollPane.getVerticalScrollBar().getValue() + graph.getRadius() + propertiesEdge.getHeight()) > getHeight() ? (e.getY() - propertiesEdge.getHeight()) : (e.getY());

                            propertiesEdge.setLocation((int) x, (int) y);
                            graphPanel.add(propertiesEdge);

                            propertiesEdge.getRemove().addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    graph.removeEdge(edge);

                                    graphPanel.remove(propertiesEdge);
                                    propertiesEdge = null;

                                    graphPanel.revalidate();
                                    graphPanel.repaint();
                                }
                            });
                        }
                    }

                    revalidate();
                }
                graphPanel.repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                if (e.getButton()==1) {
                    switch (mapEditorController.getChooser()) {
                        case 0:
                            if (temp != null) {
                                temp = null;
                            }
                            break;
                        case 1:
                            Vertex vertex = graph.getVertex((int)(e.getX()/graphPanel.getZoom()), (int)(e.getY()/graphPanel.getZoom()));
                            if (vertex != null && temp != null) {
                                mapEditorController.edgeAdd(temp.getID(), vertex.getID());
                            }
                            line = false;
                            temp = null;
                            break;
                    }
                }
                graphPanel.repaint();
            }
        });

        graphPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                switch (mapEditorController.getChooser()){
                    case 0:
                        if (temp!=null) {
                            double x = e.getX() / graphPanel.getZoom();
                            double y = e.getY() / graphPanel.getZoom();

                            if (e.getX() - graph.getRadius() - jScrollPane.getHorizontalScrollBar().getValue() < 0) {
                                x = jScrollPane.getHorizontalScrollBar().getValue()/graphPanel.getZoom()+graph.getRadius();
                            }else if (e.getX() + graph.getRadius() - jScrollPane.getHorizontalScrollBar().getValue() > 1263){
                                x = (1263+jScrollPane.getHorizontalScrollBar().getValue())/graphPanel.getZoom()-graph.getRadius();
                            }

                            if (e.getY() - graph.getRadius() - jScrollPane.getVerticalScrollBar().getValue() < 0){
                                y = jScrollPane.getVerticalScrollBar().getValue()/graphPanel.getZoom()+graph.getRadius();
                            }else if (e.getY() + graph.getRadius() - jScrollPane.getVerticalScrollBar().getValue() > 523){
                                y = (523+jScrollPane.getVerticalScrollBar().getValue())/graphPanel.getZoom()-graph.getRadius();
                            }

                            mapEditorController.vertexSetPosition(temp, (int) x, (int) y);
                        }
                        break;
                    case 1:
                        linePos[2] = (int)(e.getX()/graphPanel.getZoom());
                        linePos[3] = (int)(e.getY()/graphPanel.getZoom());
                }
                graphPanel.repaint();
            }
        });
    }

    public static JScrollPane getJScrollPane() {
        return jScrollPane;
    }

    public boolean isLine() {
        return line;
    }

    public int[] getLinePos() {
        return linePos;
    }

    public static void setGraph(Graph pGraph) {
        graph = pGraph;
        graphPanel.repaint();
        graphPanel.setGraph(pGraph);
    }

    public JPanel getPropertiesPanel() {
        if (propertiesVertex != null){
            return propertiesVertex;
        }else if (propertiesEdge != null){
            return propertiesEdge;
        }
        return null;
    }

    private class PropertiesVertex extends JPanel{
        private Vertex vertex;
        private JButton remove;

        private JTextField name = null, population = null, mineLevel = null, resources = null, units = null;
        private JComboBox forestType;

        public PropertiesVertex(Vertex pVertex) {
            this.vertex = pVertex;
            this.setSize(175, 200);

            String[] labels = {"Name: ", "Population: ", "Biom: ", "Gebirge", "Fruchtbarkeit", "Units: "};
            int numPairs = labels.length;

            this.setLayout(new SpringLayout());
            for (int i = 0; i < numPairs; i++) {
                JLabel l = new JLabel(labels[i], JLabel.TRAILING);
                l.setFont(new Font("Arial", Font.BOLD, 10));
                this.add(l);

                if (i <= 2 && i <= 4){
                    JComboBox jComboBox = null;

                    if (i == 2){
                        jComboBox = new JComboBox(new String[]{"Weide: 0 Holz", "Wald: 1-3 Holz", "Dschungel: 3-5 Holz"});
                        forestType = jComboBox;

                        //forestType.setSelectedIndex(vertex.getField().getForestType().name());
                    }else if (i == 3){
                        //TODO:Einstellung
                        jComboBox = new JComboBox(new String[]{"Eisen", "Gold"});
                    }else{
                        jComboBox = new JComboBox(new String[]{"Weizen", "Pferde", "Vieh"});
                    }

                    jComboBox.setFont(new Font("Arial", Font.PLAIN, 10));
                    jComboBox.setSelectedIndex(1);
                    this.add(jComboBox);
                }else {
                    JTextField textField = new JTextField();
                    textField.setFont(new Font("Arial", Font.PLAIN, 10));
                    l.setLabelFor(textField);
                    this.add(textField);
                }
            }

            JComboBox players = new JComboBox(new String[]{"Neutral", "KI", "Player 1", "Player 2"});
            players.setFont(new Font("Arial", Font.PLAIN, 10));
            this.add(players);

            remove = new JButton("Remove");
            remove.setFont(new Font("Arial", Font.BOLD, 10));
            this.add(remove);

            SpringUtilities.makeCompactGrid(this, numPairs+2, 2, 6, 6, 6, 6);


            KeyAdapter onlyNumbers = new KeyAdapter() {
                @Override
                public void keyTyped(KeyEvent e) {
                    super.keyTyped(e);
                    char c = e.getKeyChar();
                    if (!(Character.isDigit(c) || (c==KeyEvent.VK_BACK_SPACE || c==KeyEvent.VK_DELETE))){
                        e.consume();
                    }
                }
            };

            Component[] components = this.getComponents();
            for (Component c:components) {
                if (c instanceof JLabel) {
                    if (((JLabel) c).getText().equals(labels[0])){
                        name = (JTextField) ((JLabel) c).getLabelFor();
                        name.setText(vertex.getID());
                    }else if (((JLabel) c).getText().equals(labels[1])){
                        population = (JTextField) ((JLabel) c).getLabelFor();
                        //TODO:Population
                        //population.setText(String.valueOf(vertex.getField().get()));
                        population.addKeyListener(onlyNumbers);
                    }else if (((JLabel) c).getText().equals(labels[2])){
                        //mineLevel = (JTextField) ((JLabel) c).getLabelFor();
                        //TODO:MineLevel
                     //   mineLevel.setText(String.valueOf(vertex.getField().getMineLevel()));
                       // mineLevel.addKeyListener(onlyNumbers);
                    }else if (((JLabel) c).getText().equals(labels[3])){
                        resources = (JTextField) ((JLabel) c).getLabelFor();
                        //TODO:Resources
                        resources.setText(String.valueOf(vertex.getField().getResources()));
                        resources.addKeyListener(onlyNumbers);
                    }else if (((JLabel) c).getText().equals(labels[4])){
                        units = (JTextField) ((JLabel) c).getLabelFor();
                        //TODO:Resources
                        //units.setText(String.valueOf(vertex.getField().getResources()));
                    }
                }
            }

            name.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!mapEditorController.setId(vertex, ((JTextField) e.getSource()).getText())){
                        name.setBackground(Color.RED);
                    }else {
                        name.setBackground(Color.WHITE);
                    }
                }
            });
            population.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(!mapEditorController.setPopulation(vertex, Integer.parseInt(((JTextField) e.getSource()).getText()))){
                        population.setBackground(Color.RED);
                    }else {
                        population.setBackground(Color.WHITE);
                    }
                }
            });
            mineLevel.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(!mapEditorController.setMineLevel(vertex, Integer.parseInt(((JTextField) e.getSource()).getText()))){
                        mineLevel.setBackground(Color.RED);
                    }else {
                        mineLevel.setBackground(Color.WHITE);
                    }
                }
            });
            resources.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(!mapEditorController.setResources(vertex, Integer.parseInt(((JTextField) e.getSource()).getText()))){
                        resources.setBackground(Color.RED);
                    }else {
                        resources.setBackground(Color.WHITE);
                    }
                }
            });
            units.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(!mapEditorController.setUnits(vertex, Integer.parseInt(((JTextField) e.getSource()).getText()))){
                        units.setBackground(Color.RED);
                    }else {
                        units.setBackground(Color.WHITE);
                    }
                }
            });


            players.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println(mapEditorController.setPlayer(vertex, ((JComboBox) e.getSource()).getSelectedIndex()));
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D graphics2D = (Graphics2D) g;

            graphics2D.setColor(Color.BLACK);
            graphics2D.drawRect(0, 0, this.getWidth()-1, this.getHeight()-1);
        }

        public JButton getRemove() {
            return remove;
        }
    }

    private class PropertiesEdge extends JPanel {
        private Edge edge;
        private JButton remove;

        public PropertiesEdge(Edge pEdge) {
            this.edge = pEdge;
            this.setSize(150, 75);

            String[] labels = {"Gewicht: "};
            int numPairs = labels.length;

            this.setLayout(new SpringLayout());
            for (int i = 0; i < numPairs; i++) {
                JLabel l = new JLabel(labels[i], JLabel.TRAILING);
                l.setFont(new Font("Arial", Font.BOLD, 10));
                this.add(l);

                JTextField textField = new JTextField();
                textField.setFont(new Font("Arial", Font.PLAIN, 10));
                l.setLabelFor(textField);
                this.add(textField);
            }

            JPanel players = new JPanel();
            this.add(players);

            remove = new JButton("Remove");
            remove.setFont(new Font("Arial", Font.BOLD, 10));
            this.add(remove);

            SpringUtilities.makeCompactGrid(this, numPairs + 1, 2, 6, 6, 6, 6);

            KeyAdapter onlyNumbers = new KeyAdapter() {
                @Override
                public void keyTyped(KeyEvent e) {
                    super.keyTyped(e);
                    char c = e.getKeyChar();
                    if (!(Character.isDigit(c) || (c==KeyEvent.VK_BACK_SPACE || c==KeyEvent.VK_DELETE))){
                        e.consume();
                    }
                }
            };

            JTextField weight = null;

            Component[] components = this.getComponents();
            for (Component c:components) {
                if (c instanceof JLabel) {
                    if (((JLabel) c).getText().equals(labels[0])){
                        weight = (JTextField) ((JLabel) c).getLabelFor();
                        weight.setText(String.valueOf(edge.getWeight()));
                        weight.addKeyListener(onlyNumbers);
                    }
                }
            }

            weight.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println(mapEditorController.setEdgeWeight(edge, Double.parseDouble(((JTextField) e.getSource()).getText())));
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D graphics2D = (Graphics2D) g;

            graphics2D.setColor(Color.BLACK);
            graphics2D.drawRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);
        }

        public JButton getRemove() {
            return remove;
        }
    }
}
