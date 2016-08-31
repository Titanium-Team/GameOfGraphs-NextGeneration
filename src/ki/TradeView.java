package ki;

import de.SweetCode.e.input.InputEntry;
import de.SweetCode.e.rendering.GameScene;
import de.SweetCode.e.rendering.layers.Layers;
import field.resource.Resources;
import game.Player;
import graph.Vertex;
import mapEditor.EditText;

import java.awt.*;

import static game.GameOfGraphs.getGame;

/**
 * Created by Tim Bolz on 31.08.2016.
 */
public class TradeView  {

	public static void drawer( int x,int y,Graphics2D g, Player currentPlayer, Vertex currentVertex){
		g.setColor(Color.WHITE);
		int height=Resources.values().length*50 +50;
		int width=200;
		g.fillRoundRect(x,y,width,height,10,10);
		g.setColor(Color.BLACK);
		g.drawRoundRect(x,y,width,height,10,10);

		for (int i=0;i<Resources.values().length;i++) {
			g.drawString(Resources.values()[i].getName() +":", x + 10, y + 50 * i + 50);
			EditText wanted=new EditText(35,15,x+50,y+50*i+50,"",true,false,"wanted"),offered=new EditText(35,15,x+50,y+50*i+50,"",true,false,"offered");
			wanted.drawer(g);
			offered.drawer(g);
		}
	}
}
