package event;

import java.util.Random;

public abstract class Event {

    private final static Random random = new Random();

    private final String name;
    private final String description;
    private final double weight;

    public Event(String name, String description, double weight) {
        this.name = name;
        this.description = description;
        this.weight = weight;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public double getWeight() {
        return this.weight;
    }

    public static Random getRandom() {
        return random;
    }

    public abstract void prepare();

    public abstract void run();

    public abstract void clear();

}
