package game.view.views;

import game.GameOfGraphs;
import game.loading.Loadable;
import game.view.View;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class LoadingView extends View {

    private String value = ".";
    private int count = 0;

    private final Timer timer = new Timer();

    public LoadingView() {
        super(new DefaultMenu());

        this.timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Thread.currentThread().setName("Loading Task");

                count++;
                if(count == 10) {
                    if (value.length() > 2) {
                        value = ".";
                    } else {
                        value += ".";
                    }
                    count = 0;
                }

                repaint();

                // Der TimeTask wird beendet, sobald dieser nicht mehr benoetigt wird.
                if(GameOfGraphs.getGame().getLoadingManager().isDone()) {
                    if(!(this.cancel())) {
                        // @Watch: Das sollte eigentlich nie passieren, falls doch muss man schauen wie
                        // man das loest.
                        throw new RuntimeException("Loading Task didn't stop running.");
                    }
                }

            }
        }, 0, 50);
    }

    @Override
    public void paintComponent(Graphics graphic) {

        Graphics2D g = (Graphics2D) graphic;
        super.paintComponent(g);

        // Draw Loading Text
        g.setFont(new Font("Arial", Font.BOLD, 50));
        String text = "Loading" + this.value;

        g.drawString(text, 515,  193);

        // Draw Loadable Text
        Loadable current = GameOfGraphs.getGame().getLoadingManager().getCurrent();
        g.setFont(new Font("Arial", Font.BOLD, 25));
        FontMetrics fontMetrics = g.getFontMetrics();

        if(!(current == null)) {
            g.drawString(current.getName(), (1280 - fontMetrics.stringWidth(current.getName())) / 2,  ((720 - fontMetrics.getHeight()) / 2) - fontMetrics.getAscent());
        }

    }

}
