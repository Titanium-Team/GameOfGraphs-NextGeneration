package game.ui;

import de.SweetCode.e.input.InputEntry;
import de.SweetCode.e.math.IBoundingBox;
import de.SweetCode.e.math.ILocation;
import de.SweetCode.e.math.Location;
import de.SweetCode.e.rendering.GameScene;
import de.SweetCode.e.rendering.layers.Layers;

import java.awt.*;

public class EditText<T> extends UIComponent<T>{

    private T text;
    private final IBoundingBox boundingBox;

    private String hint;
    private boolean onlyNumbers;
    private boolean dot;

    private double cursorBlink;
    private int cursor = 0;

    private int number = -1;

    private Color lineColor = Color.BLACK;
    private Color textColor = Color.WHITE;

    private boolean clicked = false;

    public EditText(GameScene gameScene, T text, IBoundingBox boundingBox, boolean onlyNumbers, boolean dot, Trigger<T> trigger) {
        super(gameScene, trigger);
        this.text = text;
        this.boundingBox = boundingBox;
        this.onlyNumbers = onlyNumbers;
        this.dot = dot;
        cursor = text.toString().length();
    }

    public EditText(GameScene gameScene, T text, IBoundingBox boundingBox, boolean onlyNumbers, boolean dot, String hint, Trigger<T> trigger) {
        super(gameScene, trigger);
        this.text = text;
        this.boundingBox = boundingBox;
        this.onlyNumbers = onlyNumbers;
        this.dot = dot;
        this.hint=hint;
        cursor = text.toString().length();
    }

    public EditText(GameScene gameScene, T text, IBoundingBox boundingBox, boolean onlyNumbers, boolean dot, int number, Trigger<T> trigger) {
        super(gameScene, trigger);
        this.text = text;
        this.boundingBox = boundingBox;
        this.onlyNumbers = onlyNumbers;
        this.dot = dot;
        this.number = number;
        cursor = text.toString().length();
    }

    @Override
    public void render(Layers layers) {
        Graphics2D g = layers.first().getGraphics2D();

        g.setColor(lineColor);
        g.drawRect(boundingBox.getMin().getX(), boundingBox.getMin().getY(), boundingBox.getWidth(), boundingBox.getHeight());
        g.setClip(boundingBox.getMin().getX(), boundingBox.getMin().getY(), boundingBox.getWidth(), boundingBox.getHeight());
        if(!text.toString().isEmpty() || hint==null) {
            String s1 = text.toString().substring(0, cursor);
            String s2 = text.toString().substring(cursor, text.toString().length());

            String textWCursor;
            if (cursorBlink <= 1) {
                textWCursor = s1 + "|" + s2;
            } else {
                textWCursor = s1 + " " + s2;
            }

            g.setColor(textColor);
            g.drawString(textWCursor, boundingBox.getMin().getX() + 5, boundingBox.getMin().getY() + 15);
        }else{
            g.setColor(Color.GRAY);
            g.drawString(hint,boundingBox.getMin().getX()+5, boundingBox.getMin().getY()+15);
        }
        g.setClip(null);
    }

    @Override
    public void update(InputEntry inputEntry, long l) {
        inputEntry.getMouseEntries().forEach(mouseEntry -> {
            if (boundingBox.contains(new Location(mouseEntry.getPoint()))){
                clicked = true;
            }else {
                clicked = false;
            }
        });

        if (clicked){
            if (cursorBlink <=2){
                cursorBlink = cursorBlink + 0.005*l;
            }else {
                cursorBlink = 0;
            }
            inputEntry.getKeyEntries().forEach(keyEntry -> {
                if (number == -1 || text.toString().length() < number) {
                    if (onlyNumbers) {
                        if (Character.isDigit(keyEntry.getCharacter())) {
                            String s1 = text.toString().substring(0, cursor);
                            String s2 = text.toString().substring(cursor, text.toString().length());
                            text = (T) (s1 + keyEntry.getCharacter() + s2);

                            cursor++;
                        }
                    } else {
                        if (Character.isLetterOrDigit(keyEntry.getCharacter())) {
                            String s1 = text.toString().substring(0, cursor);
                            String s2 = text.toString().substring(cursor, text.toString().length());
                            text = (T) (s1 + keyEntry.getCharacter() + s2);

                            cursor++;
                        }
                    }
                    if (dot && !text.toString().contains(".")) {
                        if (keyEntry.getCharacter() == '.') {
                            String s1 = text.toString().substring(0, cursor);
                            String s2 = text.toString().substring(cursor, text.toString().length());
                            text = (T) (s1 + keyEntry.getCharacter() + s2);

                            cursor++;
                        }
                    }
                }

                if (keyEntry.getKeyCode() == 8){
                    String s1 = text.toString().substring(0, cursor);
                    s1 = (String) s1.subSequence(0, s1.length()-1 != -1 ? s1.length()-1:0);
                    String s2 = text.toString().substring(cursor, text.toString().length());

                    text = (T) (s1 + s2);

                    if (cursor != 0) {
                        cursor--;
                    }
                }
                if (keyEntry.getKeyCode() == 127){
                    String s1 = text.toString().substring(0, cursor);
                    String s2 = text.toString().substring(cursor, text.toString().length());
                    s2 = (String) s2.subSequence(s2.length() != 0 ? 1:0, s2.length());

                    text = (T) (s1 + s2);
                }
                if (keyEntry.getKeyCode() == 37 && cursor != 0){
                    cursor--;
                }
                if (keyEntry.getKeyCode() == 39 && cursor != text.toString().length()){
                    cursor++;
                }
            });

            getTrigger().call(this, text);
        }
    }

    @Override
    public boolean isActive() {
        return this.getGameScene().isActive() && this.isEnabled();
    }

    public T getText() {
        return text;
    }

    public void setText(T text) {
        this.text = text;
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }

    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }
}
