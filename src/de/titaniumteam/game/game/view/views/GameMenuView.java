package game.view.views;

import game.GameOfGraphs;
import game.view.View;
import graph.GraphView;
import mapEditor.MapEditorView;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class GameMenuView extends View {

    private int selectedOption = 0;
    private final Map<String, Class<? extends View>> options = new LinkedHashMap<>();

    {
        this.options.put("Play", GraphView.class);
        this.options.put("Map Editor", MapEditorView.class);
        this.options.put("Exit", null);
    }


    public GameMenuView() {
        super(new DefaultMenu());
    }


    @Override
    public void paintComponent(Graphics graphic) {
        Graphics2D g = (Graphics2D) graphic;
        super.paintComponent(g);

        //draw menu
        int x = 0;
        for(Map.Entry<String, Class<? extends View>> entry : this.options.entrySet()) {

            Image image = GameOfGraphs.getGame().getTextBuilder().toImage(entry.getKey(), (x == this.selectedOption ? 15 : 10));
            g.drawImage(image, 640 - image.getWidth(null) / 2, 150 + x * 35, null);
            x++;

        }

    }

    /*private void setup() {
        this.add(this.playButton);
        this.add(this.mapEditorButton);
        this.add(this.exitButton);

        this.playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        this.mapEditorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GameOfGraphs.getGame().getViewManager().switchView(GraphView.class);
            }
        });

        this.exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }*/

}
