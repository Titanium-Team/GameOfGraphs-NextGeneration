package mapEditor;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Button {
    private int x;
    private int y;
    private int width;
    private int height;
    private String text;
    private Color background;
    private Color foreground;

    public Button(int x, int y, int width, int height, Color background, Color foreground, String text) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.background = background;
        this.foreground = foreground;
        this.text = text;
    }

    public void draw(Graphics2D g){
        g.setColor(background);
        g.fillRect(x, y, width, height);

        g.setColor(foreground);
        g.drawRect(x, y, width, height);
        g.drawString(text, x + 5, y + (height / 2 + 5));
    }

    public boolean isPushed(Point p) {
        if (p.getX() >= x && p.getX() <= x + width && p.getY() >= y && p.getY() <= y + height) {
            return true;
        }
        return false;
    }
}
