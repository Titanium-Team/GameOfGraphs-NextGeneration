package mapEditor;

import de.SweetCode.e.input.InputEntry;
import de.SweetCode.e.input.MouseEntry;
import de.SweetCode.e.rendering.GameScene;
import de.SweetCode.e.rendering.layers.Layers;
import game.GraphDrawer;
import graph.Graph;
import graph.GraphPanel;
import graph.Vertex;

import java.util.stream.Stream;

public class MapEditorView extends GameScene{
    private static Graph graph;

    private PropertiesVertex propertiesVertex;
    private PropertiesEdge propertiesEdge;

    @Override
    public void render(Layers layers) {
        GraphDrawer.drawer(layers.first().getGraphics2D(), graph, "MapEditor");
    }

    @Override
    public void update(InputEntry inputEntry, long l) {
        inputEntry.getMouseEntries().forEachOrdered(mouseEntry -> {
            if (mouseEntry.getButton() == 1){
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
            }
            final Vertex vertex = graph.getVertex((int)(mouseEntry.getPoint().getX()/graphPanel.getZoom()), (int)(mouseEntry.getPoint().getX()/graphPanel.getZoom()));
        });
    }

    @Override
    public boolean isActive() {
        return false;
    }
}
