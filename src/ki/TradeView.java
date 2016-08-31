package ki;

import de.SweetCode.e.input.InputEntry;
import de.SweetCode.e.rendering.GameScene;
import de.SweetCode.e.rendering.layers.Layers;
import field.resource.Resources;
import game.Player;
import graph.Vertex;
import mapEditor.EditText;

import java.awt.*;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import static game.GameOfGraphs.getGame;

/**
 * Created by Tim Bolz on 31.08.2016.
 */
public class TradeView  {

	private static ArrayList<EditText> textFields;
	private static EditText enabled;

	public static void drawer( int x,int y,Graphics2D g, Player currentPlayer, Vertex currentVertex){
		textFields=new ArrayList<>();
		g.setColor(Color.WHITE);
		int height=Resources.values().length*50 +50;
		int width=200;
		g.fillRoundRect(x,y,width,height,10,10);
		g.setColor(Color.BLACK);
		g.drawRoundRect(x,y,width,height,10,10);

		for (int i=0;i<Resources.values().length;i++) {
			g.setColor(Color.BLACK);
			g.drawString(Resources.values()[i].getName() +":", x + 10, y + 50 * i + 50);
			EditText wanted=new EditText(35,15,x+50,y+50*i+47,"",true,false,"wanted"),offered=new EditText(35,15,x+100,y+50*i+47,"",true,false,"offered");
			textFields.add(wanted);
			textFields.add(offered);
			wanted.drawer(g);
			g.setColor(Color.BLACK);
			offered.drawer(g);
		}



	}
	public static void update(InputEntry inputEntry, long l){
		try{
			if (textFields != null) {
				for (EditText e : textFields) {
					inputEntry.getMouseEntries().forEach(mouseEntry -> {
						if (mouseEntry.getPoint().getX() >= e.getX() && mouseEntry.getPoint().getX() <= e.getX() + e.getWidth() && mouseEntry.getPoint().getY() >= e.getY() && mouseEntry.getPoint().getY() <= e.getY() + e.getHeight()) {
							enabled = e;
						}
					});
					if (enabled != null) {
						enabled.update(inputEntry, l);
					}
				}
			}
		}catch(ConcurrentModificationException c){
			enabled = null;
		}
	}
}
