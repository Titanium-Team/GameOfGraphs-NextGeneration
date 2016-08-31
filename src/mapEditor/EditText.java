package mapEditor;

import de.SweetCode.e.input.InputEntry;

import java.awt.*;

public class EditText{
    private int width, height;
    private int x, y;

    private String text,hint;
    private boolean onlyNumbers;
    private boolean dot;

    private double cursorBlink;
    private int cursor = 0;

    public EditText(int width, int height, int x, int y, String text, boolean onlyNumbers, boolean dot) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.text = text;
        this.onlyNumbers = onlyNumbers;
        this.dot = dot;
    }

	public EditText(int width, int height, int x, int y,String text, boolean onlyNumbers, boolean dot,String hint) {
		this.width = width;
		this.height = height;
		this.x = x;
		this.y = y;
		this.text = text;
		this.onlyNumbers = onlyNumbers;
		this.dot = dot;
		this.hint=hint;
	}

    public void drawer(Graphics2D g){
        g.drawRect(x, y, width, height);

        g.setClip(new Rectangle(x, y, width, height));
		if(!text.isEmpty() || hint==null) {
			String s1 = text.substring(0, cursor);
			String s2 = text.substring(cursor, text.length());

			String textWCursor;
			if (cursorBlink <= 1) {
				textWCursor = s1 + "|" + s2;
			} else {
				textWCursor = s1 + " " + s2;
			}

			g.drawString(textWCursor, x + 5, y + 15);
		}else{
			g.setColor(Color.GRAY);
			String hintWCursor;
			if (cursorBlink <= 1) {
				hintWCursor =  "|" + hint;
			} else {
				hintWCursor = hint;
			}
			g.drawString(hintWCursor,x+5,y+15);
		}
    }

    public void update(InputEntry inputEntry, long l){
        if (cursorBlink <=2){
            cursorBlink = cursorBlink + 0.005*l;
        }else {
            cursorBlink = 0;
        }
        inputEntry.getKeyEntries().forEach(keyEntry -> {
            if (onlyNumbers){
                if (Character.isDigit(keyEntry.getCharacter())){
                    String s1 = text.substring(0, cursor);
                    String s2 = text.substring(cursor, text.length());
                    text = s1 + keyEntry.getCharacter() + s2;

                    cursor++;
                }
            }else {
                if (Character.isLetterOrDigit(keyEntry.getCharacter())) {
                    String s1 = text.substring(0, cursor);
                    String s2 = text.substring(cursor, text.length());
                    text = s1 + keyEntry.getCharacter() + s2;

                    cursor++;
                }
            }
            if (dot && !text.contains(".")){
                if (keyEntry.getCharacter() == '.'){
                    String s1 = text.substring(0, cursor);
                    String s2 = text.substring(cursor, text.length());
                    text = s1 + keyEntry.getCharacter() + s2;

                    cursor++;
                }
            }

            if (keyEntry.getKeyCode() == 8){
                String s1 = text.substring(0, cursor);
                s1 = (String) s1.subSequence(0, s1.length()-1 != -1 ? s1.length()-1:0);
                String s2 = text.substring(cursor, text.length());

                text = s1 + s2;

                if (cursor != 0) {
                    cursor--;
                }
            }
            if (keyEntry.getKeyCode() == 127){
                String s1 = text.substring(0, cursor);
                String s2 = text.substring(cursor, text.length());
                s2 = (String) s2.subSequence(s2.length() != 0 ? 1:0, s2.length());

                text = s1 + s2;
            }
            if (keyEntry.getKeyCode() == 37 && cursor != 0){
                cursor--;
            }
            if (keyEntry.getKeyCode() == 39 && cursor != text.length()){
                cursor++;
            }
        });
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getText() {
        return text;
    }
}