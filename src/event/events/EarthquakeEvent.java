package event.events;

import event.Event;
import game.GameOfGraphs;
import graph.Vertex;

import java.util.ArrayList;

public class EarthquakeEvent extends Event {

    private final ArrayList<Vertex> visited = new ArrayList<>();

    public EarthquakeEvent() {
        super("Erdbeben", "Boom!", 25);
    }

    @Override
    public void prepare() {}

    @Override
    public void run() {

        ArrayList<Vertex> vertices = GameOfGraphs.getGame().getCurrentPlayer().getFields();

        if(vertices.isEmpty()) {
            return;
        }

        Vertex epicenter = vertices.get(Event.getRandom().nextInt(vertices.size()));

        int effected = Event.getRandom().nextInt(5);
        this.earthquake(epicenter, effected);


    }

    private void earthquake(Vertex vertex, final int effected) {

        /*List<Vertex> vertices = game.GameOfGraphs.getGame().getGraphController().getGraph().getNeighbours(vertex);
        vertices.toFirst();

        while (vertices.hasAccess() && this.visited.size() <= effected) {

            if(this.visited.contains(vertices.getContent())) {
                continue;
            }

            this.earthquake(vertices.getContent(), effected);
        }*/

    }

    @Override
    public void clear() {
        this.visited.clear();
    }
}
