package event.events;

import event.Event;

public class WeatherEvent extends Event {

    //double[AVERAGE_TIME, RAND_START, RAND_END]
    private final static double[] SUN_AVERAGE       = {8, 0, 24}; //40%
    private final static double[] WINDY_AVERAGE     = {3, 0, 18}; //30%
    private final static double[] STORMY_AVERAGE    = {1, 0, 3}; //5%
    private final static double[] RAINY_AVERAGE     = {3, 0, 9}; //24%
    private final static double[] LIGHTNING_AVERAGE = {0.5, 0, 1.5}; //1%

    public WeatherEvent() {
        super("Wetter", "Wettah!", 70);
    }

    @Override
    public void prepare() {}

    @Override
    public void run() {

        int weather = Event.getRandom().nextInt(100);

        if(weather < 40) {
            double time = SUN_AVERAGE[1] + (SUN_AVERAGE[2] - SUN_AVERAGE[1]) * Event.getRandom().nextDouble();
            boolean start = (time > SUN_AVERAGE[0]);
            // Sonne
            // @TODO: Sobald wir Weizen o.ä. als Resource haben, dann könnte man eventuell hier eine Art Wachstums-Boost geben.
        } else if(weather >= 40 && weather < 70) {
            double time = WINDY_AVERAGE[1] + (WINDY_AVERAGE[2] - WINDY_AVERAGE[1]) * Event.getRandom().nextDouble();
            boolean start = (time > WINDY_AVERAGE[0]);
            // Wind
            // @TODO: Wenn wir Weizen haben ö.ä. haben, dann könnte etwas an Ernte verloren gehen.
            // @TODO: Windstärke bestimmen, bei hohen Windstärken könnte auch jemand sterben oder Minen verschüttet werden.
        } else if(weather >= 70 && weather < 94) {
            double time = RAINY_AVERAGE[1] + (RAINY_AVERAGE[2] - RAINY_AVERAGE[1]) * Event.getRandom().nextDouble();
            boolean start = (time > RAINY_AVERAGE[0]);
            // Regen
            // @TODO Mögliche Überschwemmungen.
        } else if(weather >= 94 && weather < 99) {
            double time = STORMY_AVERAGE[1] + (STORMY_AVERAGE[2] - STORMY_AVERAGE[1]) * Event.getRandom().nextDouble();
            boolean start = (time > STORMY_AVERAGE[0]);
            // Sturm
            // @TODO: Mögliche Zerstörung vieler Gebäude.
        } else if(weather == 99) {
            double time = LIGHTNING_AVERAGE[1] + (LIGHTNING_AVERAGE[2] - LIGHTNING_AVERAGE[1]) * Event.getRandom().nextDouble();
            boolean start = (time > LIGHTNING_AVERAGE[0]);
            // Blitze
            // @TODO: Hell yeah!
        }

    }

    @Override
    public void clear() {}

}
