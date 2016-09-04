package game;

import connection.Connector;
import de.SweetCode.e.E;
import de.SweetCode.e.input.InputEntry;
import de.SweetCode.e.math.ILocation;
import de.SweetCode.e.rendering.GameScene;
import de.SweetCode.e.rendering.layers.Layers;
import field.FieldView;
import game.ui.Button;
import game.ui.DropDownMenu;
import graph.Graph;

import java.awt.event.KeyEvent;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

public class MapSelectView extends GameScene {

    private final static Map<String, Graph> maps = new LinkedHashMap<>();

    private boolean drawSinglePlayer = true;
    private int time = 0;

    private DropDownMenu<String> gameModeMenu = new DropDownMenu<>(this, new ILocation(640, 300), new LinkedList<String>() {{
        this.add("Single-player");
        this.add("Multi-player");
    }}, (c, t) -> this.drawSinglePlayer = t.equals("Single-player"));

    private DropDownMenu<String> dropDownMenuS = new DropDownMenu<>(this, new ILocation(400,400), new LinkedList<>(), (c, t) -> {

        GameOfGraphs.getGame().getGraphController().setGraph(maps.get(t));

    });

    private Button<String> loadMapButtonS = new Button<>(this, "Load Map", new ILocation(400, 380), (c, t) -> {

        Object[] graph = GameOfGraphs.getGame().getGraphController().load();

        if(!(graph == null)) {
            this.maps.put(String.valueOf(graph[1]), (Graph) graph[0]);
            this.dropDownMenuS.getOptions().add(String.valueOf(graph[1]));
        }

    });

    private DropDownMenu<Player> playerDropDownM = new DropDownMenu<>(this, new ILocation(500,400), new LinkedList<>(), (c, t) -> {


    });


    private Button<String> playButton = new Button<>(this, "Play", new ILocation(400, 340), (c, t) -> E.getE().show(FieldView.class));

    public MapSelectView() {
        E.getE().addComponent(gameModeMenu);

        E.getE().addComponent(dropDownMenuS);
        E.getE().addComponent(loadMapButtonS);

        E.getE().addComponent(playerDropDownM);

        E.getE().addComponent(playButton);
    }

    @Override
    public void render(Layers layers) {}

    @Override
    public void update(InputEntry inputEntry, long l) {

        this.time += l;
        this.dropDownMenuS.setEnabled(this.drawSinglePlayer);
        this.loadMapButtonS.setEnabled(this.drawSinglePlayer);

        this.playerDropDownM.setEnabled(!(this.drawSinglePlayer));

        inputEntry.getKeyEntries().forEach(e -> {

            if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                E.getE().show(MenuView.class);
            }

        });

        if(this.time >= 5000) {
            this.playerDropDownM.setOptions(new LinkedList<Player>() {{

                if(!(Connector.unusedPlayers() == null)) {
                    this.addAll(Connector.unusedPlayers());
                }

            }});
            this.time = 0;
        }

    }

    @Override
    public boolean isActive() {
        return E.getE().getScreen().getCurrent() == this;
    }

}
