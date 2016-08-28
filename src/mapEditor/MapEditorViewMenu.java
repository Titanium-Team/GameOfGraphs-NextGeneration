package mapEditor;

public class MapEditorViewMenu {
    //1280 * 180

    /*
    private JButton vertexAdd, edgeAdd, check, load, save, back, moving;
    private TextureChooser backgroundChooser, vertexChooser, edgeChooser;
    private MapEditorController mapEditorController;
    private Graph graph;

    public MapEditorViewMenu() {
        this.setLayout(new GridLayout(2, 4, 5, 5));
        mapEditorController = GameOfGraphs.getGame().getMapEditorController();
        graph = mapEditorController.getGraph();*/

        /*try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }*/
        /*
        backgroundChooser = new TextureChooser("Background");
        edgeChooser = new TextureChooser("Edge");
        vertexChooser = new TextureChooser("Vertex");

        vertexAdd = new JButton("Knoten Add");
        edgeAdd = new JButton("Kante Add");
        check = new JButton("Prüfen");
        moving = new JButton("Bewegen");
        load = new JButton("Laden");
        save = new JButton("Speichern");

        JPanel other = new JPanel();
        JPanel file = new JPanel();

        other.setLayout(new GridLayout(2, 1, 5, 5));
        other.add(check);
        other.add(moving);

        file.setLayout(new GridLayout(2, 1, 5, 5));
        file.add(load);
        file.add(save);

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

        JPanel settings = new JPanel();
        settings.setLayout(new GridLayout(2, 2, 5, 5));
        settings.setBackground(Color.BLACK);

        final JTextField thickness, radius, width, height;

        thickness = new JTextField();
        radius = new JTextField();
        width = new JTextField();
        height = new JTextField();

        thickness.addKeyListener(onlyNumbers);
        radius.addKeyListener(onlyNumbers);
        width.addKeyListener(onlyNumbers);
        height.addKeyListener(onlyNumbers);

        settings.add(thickness);
        settings.add(radius);
        settings.add(width);
        settings.add(height);

        thickness.setText(String.valueOf(graph.getThickness()));
        radius.setText(String.valueOf(graph.getRadius()));
        width.setText(String.valueOf(graph.getWidth()));
        height.setText(String.valueOf(graph.getHeight()));


        this.add(vertexAdd);
        this.add(edgeAdd);
        this.add(settings);
        this.add(other);

        this.add(vertexChooser);
        this.add(edgeChooser);
        this.add(backgroundChooser);
        this.add(file);

        thickness.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                graph.setThickness(Integer.parseInt(thickness.getText()));
                getRootPane().repaint();
            }
        });
        radius.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                graph.setRadius(Integer.parseInt(radius.getText()));
                getRootPane().repaint();
            }
        });
        width.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                graph.setWidth(Integer.parseInt(width.getText()));
                MapEditorView.getJScrollPane().getViewport().revalidate();
                getRootPane().repaint();
            }
        });
        height.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                graph.setHeight(Integer.parseInt(height.getText()));
                MapEditorView.getJScrollPane().getViewport().revalidate();
                getRootPane().repaint();
            }
        });
        vertexAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mapEditorController.setChooser(0);
            }
        });
        edgeAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mapEditorController.setChooser(1);
            }
        });
        moving.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mapEditorController.setChooser(2);
            }
        });
        check.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mapEditorController.checkGraph();
                getRootPane().repaint();
            }
        });
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!mapEditorController.checkGraph()){
                    int value = JOptionPane.showConfirmDialog(null, "Nicht alle Knoten sind miteinander verbunden, deshalb kann diese Map noch nicht gespielt werden. Willst du die Map trotzdem Speichern?", "Speichern?", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
                    if (value == JOptionPane.YES_OPTION){
                        graph.setChecked(false);
                    }else {
                        return;
                    }
                }else if (!graph.isEmpty()){
                    graph.setChecked(true);
                }

                String name = JOptionPane.showInputDialog(null, "Was soll der Name der Karte sein?", "Name", JOptionPane.QUESTION_MESSAGE);

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

                    getRootPane().repaint();
                }
            }
        });
        load.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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

                        mapEditorController.setGraph(graphTemp);

                        File background = new File(file.getAbsolutePath() + "\\background.png");
                        graphTemp.setBackground(ImageIO.read(background));

                        File vertex = new File(file.getAbsolutePath() + "\\vertexImage.png");
                        graphTemp.setVertexImage(ImageIO.read(vertex));

                        File edge = new File(file.getAbsolutePath() + "\\edgeImage.png");
                        graphTemp.setEdgeImage(ImageIO.read(edge));

                        MapEditorView.setGraph(graphTemp);
                        graph = graphTemp;
                        getRootPane().repaint();

                        MapEditorView.getJScrollPane().getViewport().revalidate();

                        thickness.setText(String.valueOf(graph.getThickness()));
                        radius.setText(String.valueOf(graph.getRadius()));
                        width.setText(String.valueOf(graph.getWidth()));
                        height.setText(String.valueOf(graph.getHeight()));
                        backgroundChooser.setTextured(graph.isBackgroundTextured());
                        vertexChooser.setTextured(graph.isVertexImageTextured());
                        edgeChooser.setTextured(graph.isEdgeImageTextured());

                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }

            }
        });
    }

    private BufferedImage getImage(Color color){
        BufferedImage bufferedImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        bufferedImage.setRGB(0, 0, color.getRGB());
        return bufferedImage;
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D graphics2D = (Graphics2D) g;

        graphics2D.setColor(Color.BLACK);
        graphics2D.fillRect(0, 0, 1280, 180);
    }

    private class TextureChooser extends JPanel{
        private JColorChooser jColorChooser;
        private JCheckBox textured;
        private String whatTexture;

        public TextureChooser(String pWhatTexture) {
            this.whatTexture = pWhatTexture;
            this.setLayout(new GridLayout(2, 4, 5, 5));

            final JButton white = new JButton(""), black = new JButton(), red = new JButton(), green = new JButton(), blue = new JButton(), yellow = new JButton(), colorChooser = new JButton(), wood = new JButton(), sand = new JButton(), paper = new JButton(), leather = new JButton(), bricks = new JButton(), imageChooser = new JButton();
            textured = new JCheckBox();

            white.setBackground(Color.WHITE);
            black.setBackground(Color.BLACK);
            red.setBackground(Color.RED);
            green.setBackground(Color.GREEN);
            blue.setBackground(Color.BLUE);
            yellow.setBackground(Color.YELLOW);

            leather.setIcon(new ImageIcon(new ImageIcon("gfx\\leather.jpg").getImage().getScaledInstance(this.getWidth()/7-10,this.getHeight()/2-10, Image.SCALE_DEFAULT)));
            bricks.setIcon(new ImageIcon(new ImageIcon("gfx\\bricks.jpg").getImage().getScaledInstance(this.getWidth()/7-10,this.getHeight()/2-10, Image.SCALE_DEFAULT)));
            wood.setIcon(new ImageIcon(new ImageIcon("gfx\\wood.jpg").getImage().getScaledInstance(this.getWidth()/7-10,this.getHeight()/2-10, Image.SCALE_DEFAULT)));
            paper.setIcon(new ImageIcon(new ImageIcon("gfx\\paper.jpg").getImage().getScaledInstance(this.getWidth()/7-10,this.getHeight()/2-10, Image.SCALE_DEFAULT)));
            sand.setIcon(new ImageIcon(new ImageIcon("gfx\\sand.jpg").getImage().getScaledInstance(this.getWidth()/7-10,this.getHeight()/2-10, Image.SCALE_DEFAULT)));

            this.add(white);
            this.add(black);
            this.add(red);
            this.add(green);
            this.add(blue);
            this.add(yellow);
            this.add(colorChooser);

            this.add(leather);
            this.add(bricks);
            this.add(wood);
            this.add(paper);
            this.add(sand);
            this.add(imageChooser);
            this.add(textured);


            Component[] components = this.getComponents();
            Dimension dimension = new Dimension(this.getWidth()/7, this.getHeight()/2);
            for (Component component:components){
                component.setPreferredSize(dimension);
                if (component instanceof JButton) {
                    ((JButton) component).setMargin(new Insets(0, 0, 0, 0));
                }
            }

            revalidate();

            final ChangeListener colorChangeListener = new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    switch (whatTexture){
                        case "Background":
                            mapEditorController.setPreviewBackground(getImage(jColorChooser.getColor()));
                            break;
                        case "Vertex":
                            mapEditorController.setPreviewVertex(getImage(jColorChooser.getColor()));
                            break;
                        case "Edge":
                            mapEditorController.setPreviewEdge(getImage(jColorChooser.getColor()));
                            break;
                    }
                    getRootPane().repaint();
                }
            };
            final ActionListener colorOk = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    switch (whatTexture) {
                        case "Background":
                            graph.setBackground(getImage(jColorChooser.getColor()));
                            mapEditorController.setPreviewBackground(null);
                            break;
                        case "Vertex":
                            graph.setVertexImage(getImage(jColorChooser.getColor()));
                            mapEditorController.setPreviewVertex(null);
                            break;
                        case "Edge":
                            graph.setEdgeImage(getImage(jColorChooser.getColor()));
                            mapEditorController.setPreviewEdge(null);
                            break;
                    }
                    getRootPane().repaint();
                }
            };
            final ActionListener colorCancel = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    switch (whatTexture) {
                        case "Background":
                            mapEditorController.setPreviewBackground(null);
                            break;
                        case "Vertex":
                            mapEditorController.setPreviewVertex(null);
                            break;
                        case "Edge":
                            mapEditorController.setPreviewEdge(null);
                            break;
                    }
                    getRootPane().repaint();
                }
            };
            colorChooser.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    jColorChooser = new JColorChooser();
                    jColorChooser.setDragEnabled(true);
                    jColorChooser.getSelectionModel().addChangeListener(colorChangeListener);
                    jColorChooser.setPreviewPanel(new JPanel());

                    JDialog dialog = null;
                    switch (whatTexture) {
                        case "Background":
                            dialog = jColorChooser.createDialog(null, "Suche dir eine Hintergrundfarbe aus.", true, jColorChooser, colorOk, colorCancel);
                            break;
                        case "Vertex":
                            dialog = jColorChooser.createDialog(null, "Suche dir eine Knotenfarbe aus.", true, jColorChooser, colorOk, colorCancel);
                            break;
                        case "Edge":
                            dialog = jColorChooser.createDialog(null, "Suche dir eine Kantenfarbe aus.", true, jColorChooser, colorOk, colorCancel);
                            break;
                    }

                    Component[] cs = dialog.getComponents();
                    for (int j=0; j < cs.length; j++) {
                        JComponent comp = (JComponent) cs[j];
                        Component[] cs2 = comp.getComponents();
                        for (int j2 = 0; j2 < cs2.length; j2++) {
                            JComponent comp2 = (JComponent) cs2[j2];
                            Component[] cs3 = comp2.getComponents();
                            for (int j3 = 0; j3 < cs3.length; j3++) {
                                JComponent comp3 = (JComponent) cs3[j3];
                                Component[] cs4 = comp3.getComponents();
                                for (int j4 = 0; j4 < cs4.length; j4++) {
                                    JComponent comp4 = (JComponent) cs4[j4];
                                    Component[] cs5 = comp4.getComponents();
                                    for (int j5 = 0; j5 < cs5.length; j5++) {
                                        JComponent comp5 = (JComponent) cs5[j5];
                                        if (comp5 instanceof JButton && j5 == 2) {
                                            JButton b_reset = (JButton) comp5;
                                            ActionListener[] actionListeners = b_reset.getActionListeners();
                                            for (ActionListener actionListener:actionListeners){
                                                b_reset.removeActionListener(actionListener);
                                            }
                                            b_reset.addActionListener(colorCancel);
                                        }
                                    }
                                }
                            }
                        }
                    }

                    dialog.pack();
                    dialog.setVisible(true);
                }
            });
            imageChooser.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    final FileNameExtensionFilter fileFilter = new FileNameExtensionFilter(".png, .jpg, .bmp", "png", "jpg", "bmp");
                    JFileChooser jFileChooser = new JFileChooser();

                    jFileChooser.setFileFilter(fileFilter);
                    int i = jFileChooser.showOpenDialog(getParent());
                    if (i == JFileChooser.APPROVE_OPTION) {
                        File file = jFileChooser.getSelectedFile();

                        try {
                            BufferedImage imageIO = ImageIO.read(file);
                            switch (whatTexture) {
                                case "Background":
                                    graph.setBackground(imageIO);
                                    break;
                                case "Vertex":
                                    graph.setVertexImage(imageIO);
                                    break;
                                case "Edge":
                                    graph.setEdgeImage(imageIO);
                                    break;
                            }
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }

                        getRootPane().repaint();
                    }
                }
            });
            white.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    switch (whatTexture) {
                        case "Background":
                            graph.setBackground(getImage(Color.WHITE));
                            break;
                        case "Vertex":
                            graph.setVertexImage(getImage(Color.WHITE));
                            break;
                        case "Edge":
                            graph.setEdgeImage(getImage(Color.WHITE));
                            break;
                    }
                    getRootPane().repaint();
                }
            });
            black.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    switch (whatTexture) {
                        case "Background":
                            graph.setBackground(getImage(Color.BLACK));
                            break;
                        case "Vertex":
                            graph.setVertexImage(getImage(Color.BLACK));
                            break;
                        case "Edge":
                            graph.setEdgeImage(getImage(Color.BLACK));
                            break;
                    }
                    getRootPane().repaint();
                }
            });
            red.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    switch (whatTexture) {
                        case "Background":
                            graph.setBackground(getImage(Color.RED));
                            break;
                        case "Vertex":
                            graph.setVertexImage(getImage(Color.RED));
                            break;
                        case "Edge":
                            graph.setEdgeImage(getImage(Color.RED));
                            break;
                    }
                    getRootPane().repaint();
                }
            });
            green.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    switch (whatTexture) {
                        case "Background":
                            graph.setBackground(getImage(Color.GREEN));
                            break;
                        case "Vertex":
                            graph.setVertexImage(getImage(Color.GREEN));
                            break;
                        case "Edge":
                            graph.setEdgeImage(getImage(Color.GREEN));
                            break;
                    }
                    getRootPane().repaint();
                }
            });
            blue.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    switch (whatTexture) {
                        case "Background":
                            graph.setBackground(getImage(Color.BLUE));
                            break;
                        case "Vertex":
                            graph.setVertexImage(getImage(Color.BLUE));
                            break;
                        case "Edge":
                            graph.setEdgeImage(getImage(Color.BLUE));
                            break;
                    }
                    getRootPane().repaint();
                }
            });
            yellow.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    switch (whatTexture) {
                        case "Background":
                            graph.setBackground(getImage(Color.YELLOW));
                            break;
                        case "Vertex":
                            graph.setVertexImage(getImage(Color.YELLOW));
                            break;
                        case "Edge":
                            graph.setEdgeImage(getImage(Color.YELLOW));
                            break;
                    }
                    getRootPane().repaint();
                }
            });
            bricks.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        switch (whatTexture) {
                            case "Background":
                                graph.setBackground(ImageIO.read(new File("gfx\\bricks.jpg")));
                                break;
                            case "Vertex":
                                graph.setVertexImage(ImageIO.read(new File("gfx\\bricks.jpg")));
                                break;
                            case "Edge":
                                graph.setEdgeImage(ImageIO.read(new File("gfx\\bricks.jpg")));
                                break;
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    getRootPane().repaint();
                }
            });
            leather.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        switch (whatTexture) {
                            case "Background":
                                graph.setBackground(ImageIO.read(new File("gfx\\leather.jpg")));
                                break;
                            case "Vertex":
                                graph.setVertexImage(ImageIO.read(new File("gfx\\leather.jpg")));
                                break;
                            case "Edge":
                                graph.setEdgeImage(ImageIO.read(new File("gfx\\leather.jpg")));
                                break;
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    getRootPane().repaint();
                }
            });
            paper.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        switch (whatTexture) {
                            case "Background":
                                graph.setBackground(ImageIO.read(new File("gfx\\paper.jpg")));
                                break;
                            case "Vertex":
                                graph.setVertexImage(ImageIO.read(new File("gfx\\paper.jpg")));
                                break;
                            case "Edge":
                                graph.setEdgeImage(ImageIO.read(new File("gfx\\paper.jpg")));
                                break;
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    getRootPane().repaint();
                }
            });
            sand.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        switch (whatTexture) {
                            case "Background":
                                graph.setBackground(ImageIO.read(new File("gfx\\sand.jpg")));
                                break;
                            case "Vertex":
                                graph.setVertexImage(ImageIO.read(new File("gfx\\sand.jpg")));
                                break;
                            case "Edge":
                                graph.setEdgeImage(ImageIO.read(new File("gfx\\sand.jpg")));
                                break;
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    getRootPane().repaint();
                }
            });
            wood.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        switch (whatTexture) {
                            case "Background":
                                graph.setBackground(ImageIO.read(new File("gfx\\wood.jpg")));
                                break;
                            case "Vertex":
                                graph.setVertexImage(ImageIO.read(new File("gfx\\wood.jpg")));
                                break;
                            case "Edge":
                                graph.setEdgeImage(ImageIO.read(new File("gfx\\wood.jpg")));
                                break;
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    getRootPane().repaint();
                }
            });
            textured.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    switch (whatTexture) {
                        case "Background":
                            graph.setBackgroundTextured(textured.isSelected());
                            break;
                        case "Vertex":
                            graph.setVertexImageTextured(textured.isSelected());
                            break;
                        case "Edge":
                            graph.setEdgeImageTextured(textured.isSelected());
                            break;
                    }
                    getRootPane().repaint();
                }
            });
        }

        public void setTextured(boolean textured){
            this.textured.setSelected(textured);
        }

        public boolean isTextured(){
            return textured.isSelected();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            g.setColor(Color.WHITE);
            g.drawRect(0, 0, this.getWidth()-1, this.getHeight()-1);
        }
    }*/
}
