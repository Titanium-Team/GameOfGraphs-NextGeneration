package simulation;

import graph.Vertex;

import java.util.LinkedList;

public class Path {

    private final LinkedList<Vertex> path;

    public Path(LinkedList<Vertex> path) {
        this.path = path;
    }

    public LinkedList<Vertex> getPath() {
        return this.path;
    }

}

