package game;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.SweetCode.e.E;
import de.SweetCode.e.input.InputEntry;
import de.SweetCode.e.math.ILocation;
import de.SweetCode.e.rendering.GameScene;
import de.SweetCode.e.rendering.layers.Layers;
import field.FieldView;
import game.ui.Button;
import game.ui.DropDownMenu;
import graph.Graph;
import graph.Vertex;
import ki.KIFraction;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

public class MapSelectView extends GameScene {

    private final static Map<String, Graph> maps = new LinkedHashMap<>();

    private DropDownMenu<String> dropDownMenu = new DropDownMenu<>(this, new ILocation(400,400), new LinkedList<>(), (c, t) -> {

        GameOfGraphs.getGame().getGraphController().setGraph(maps.get(t));

    });

    private Button<String> loadMapButton = new Button<>(this, "Load Map", new ILocation(400, 380), (c, t) -> {

        Object[] graph = GameOfGraphs.getGame().getGraphController().load();

        if(!(graph == null)) {
            this.maps.put(String.valueOf(graph[1]), (Graph) graph[0]);
            this.dropDownMenu.getOptions().add(String.valueOf(graph[1]));
        }

    });

    private Button<String> playButton = new Button<>(this, "Play", new ILocation(400, 340), (c, t) -> {

        E.getE().show(FieldView.class);

    });

    public MapSelectView() {
        E.getE().addComponent(dropDownMenu);
        E.getE().addComponent(loadMapButton);
        E.getE().addComponent(playButton);
    }

    @Override
    public void render(Layers layers) {}

    @Override
    public void update(InputEntry inputEntry, long l) {

    }

    @Override
    public boolean isActive() {
        return E.getE().getScreen().getCurrent() == this;
    }

}
