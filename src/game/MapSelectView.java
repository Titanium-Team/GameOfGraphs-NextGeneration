package game;

import connection.Connector;
import de.SweetCode.e.E;
import de.SweetCode.e.input.InputEntry;
import de.SweetCode.e.math.ILocation;
import de.SweetCode.e.rendering.GameScene;
import de.SweetCode.e.rendering.layers.Layers;
import de.SweetCode.e.utils.StringUtils;
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

    private DropDownMenu<String> gameModeMenu = new DropDownMenu<>(this, new ILocation(100, 100), new LinkedList<String>() {{
        this.add("Single-player");
        this.add("Multi-player");
    }}, (c, t) -> this.drawSinglePlayer = t.equals("Single-player"));

    private DropDownMenu<String> selectedMapMenu = new DropDownMenu<>(this, new ILocation(400, 400), new LinkedList<>(), (c, t) -> {

        if(maps.get(t).isChecked()) {
            GameOfGraphs.getGame().getGraphController().setGraph(maps.get(t));
        }

    });

    private Button<String> loadMapButtonS = new Button<>(this, "Load Map", new ILocation(400, 380), (c, t) -> {

        Object[] graph = GameOfGraphs.getGame().getGraphController().load();

        if(!(graph == null)) {
            this.maps.put(String.valueOf(graph[1]), (Graph) graph[0]);
            this.selectedMapMenu.getOptions().add(String.valueOf(graph[1]));
        }

    });

    private DropDownMenu<Player> playerDropDownM = new DropDownMenu<>(this, new ILocation(500,400), new LinkedList<Player>() {{


        if(!(Connector.unusedPlayers() == null)) {
            this.addAll(Connector.unusedPlayers());
        }

    }}, (c, t) -> {});

    private Button<String> createGameButton = new Button<>(this, "Create Game", new ILocation(700, 400), (c, t) -> {
        Connector.createGame(maps.get(this.selectedMapMenu.getOption()));

        this.playerDropDownM.setOptions(new LinkedList<Player>() {{

            if(!(Connector.unusedPlayers() == null)) {
                this.addAll(Connector.unusedPlayers());
            }

        }});
        this.time = 0;
    });

    private Button<String> joinGameButton = new Button<>(this, "Join Game", new ILocation(700, 420), (c, t) -> {
        Connector.joinGame(this.playerDropDownM.getOption());
    });


    private Button<String> playButton = new Button<>(this, "Play", new ILocation(700, 440), (c, t) -> E.getE().show(FieldView.class));

    public MapSelectView() {
        E.getE().addComponent(gameModeMenu);

        E.getE().addComponent(selectedMapMenu);
        E.getE().addComponent(loadMapButtonS);

        E.getE().addComponent(createGameButton);
        E.getE().addComponent(joinGameButton);

        E.getE().addComponent(playerDropDownM);

        E.getE().addComponent(playButton);

        this.createGameButton.setEnabled(Connector.isHost());
    }

    @Override
    public void render(Layers layers) {}

    @Override
    public void update(InputEntry inputEntry, long delta) {

        this.time += delta;

        inputEntry.getKeyEntries().forEach(e -> {

            if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                E.getE().show(MenuView.class);
            }

        });

        if(this.time >= 5000) {

            if(Connector.gameReady()) {
                GameOfGraphs.getGame().getGraphController().setGraph(Connector.getGraph());
                E.getE().show(FieldView.class);
                return;
            }

            this.playerDropDownM.setOptions(new LinkedList<Player>() {{

                System.out.println(StringUtils.join(Connector.unusedPlayers(), ", "));
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
