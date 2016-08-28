package event;

import event.events.EarthquakeEvent;

import java.util.*;

public class EventManager {

    private final static Random random = new Random();

    private final NavigableMap<Double, Event> events = new TreeMap<>();
    private double totalWeight = 0;

    {

        EarthquakeEvent earthquakeEvent = new EarthquakeEvent();
        events.put(earthquakeEvent.getWeight(), earthquakeEvent);

    }

    public EventManager() {}

    public void run() {

        Event event = this.events.ceilingEntry(random.nextDouble() * this.totalWeight).getValue();
        event.prepare();
        event.run();
        event.clear();

    }

}
