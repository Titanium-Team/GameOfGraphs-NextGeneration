package ki;

import de.SweetCode.e.E;
import de.SweetCode.e.input.InputEntry;
import de.SweetCode.e.math.ILocation;
import de.SweetCode.e.rendering.GameScene;
import de.SweetCode.e.rendering.layers.Layer;
import field.resource.Resource;
import field.resource.Resources;
import game.Player;
import game.ui.DropDownMenu;
import graph.Vertex;
import mapEditor.Button;
import mapEditor.EditText;

import java.awt.*;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.LinkedList;

import static game.GameOfGraphs.getGame;

/**
 * Created by Tim Bolz on 31.08.2016.
 */
public class TradeView  {

	private static ArrayList<EditText> textFields=new ArrayList<>();
	private static boolean firstTime=true;
	private static EditText enabled;
	private static int height= Resources.values().length*50 +100;
	private static Button cancel,submit;
	private static Button pressed;
	private static DropDownMenu<Player> players;

	public static void drawer(GameScene gameScene, int x, int y, Graphics2D g, Player currentPlayer, Vertex currentVertex, Layer layer){
		g.setColor(Color.WHITE);
		int width = 200;
		g.fillRoundRect(x,y, width,height,10,10);
		g.setColor(Color.BLACK);
		Font font=g.getFont();
		g.setFont(new Font("Arial",Font.BOLD,12));
		g.drawString("TRADE",x+50,y+15);
		g.setFont(font);
		g.drawRoundRect(x,y, width,height,10,10);
		if(cancel!=null)cancel.draw(g);
		if (submit != null)submit.draw(g);
		if(players!=null && players.getOption()!=null){
			//players.handleDraw(layer);
		}
		if(firstTime) {
			cancel=new Button(x+55,y+height-20,45,15,Color.white,Color.black,"Cancel");
			submit=new Button(x+105,y+height-20,45,15,Color.white,Color.black,"Submit");
			players= new DropDownMenu<Player>(gameScene,new ILocation(x+75,y+height-40),new LinkedList<Player>(){{
				for(Vertex v:getGame().getGraphController().getGraph().getNeighbours(currentVertex)){
					if(!v.getField().getPlayer().equals(currentPlayer)){
						if(v.getField().getPlayer() instanceof KIFraction){
							if(((KIFraction) v.getField().getPlayer()).isFraction()){
								this.add(v.getField().getPlayer());
							}
						}else{
							this.add(v.getField().getPlayer());
						}
					}
				}
			}},(t, value)->{});
			E.getE().addComponent(players);
			for (int i = 0; i < Resources.values().length; i++) {
				g.setColor(Color.BLACK);
				g.drawString(Resources.values()[i].getName() + ":", x + 10, y + 50 * i + 60);
				if (textFields.size()<Resources.values().length*2) {
					EditText wanted = new EditText(45, 15, x + 55, y + 50 * i + 47, "", true, false, "wanted");
					EditText offered = new EditText(45, 15, x + 105, y + 50 * i + 47, "", true, false, "offered");

					textFields.add(wanted);
					textFields.add(offered);
				}
				for (EditText txt:textFields) {
					txt.drawer(g);
				}
			}
			firstTime=false;
		}else{
			for (int i = 0; i < Resources.values().length; i++) {
				g.setColor(Color.BLACK);
				g.drawString(Resources.values()[i].getName() + ":", x + 10, y + 50 * i + 60);
			}
			for(EditText txt:textFields){
				g.setColor(Color.black);
				txt.drawer(g);
			}
		}

	}
	public static void update(InputEntry inputEntry, long l){
		try{
			if (textFields != null) {
				for (EditText e : textFields) {
					inputEntry.getMouseEntries().forEach(mouseEntry -> {
						if (mouseEntry.getPoint().getX() >= e.getX() && mouseEntry.getPoint().getX() <= e.getX() + e.getWidth() && mouseEntry.getPoint().getY() >= e.getY() && mouseEntry.getPoint().getY() <= e.getY() + e.getHeight()) {
							enabled = e;
						}else if(cancel.isPushed(mouseEntry.getPoint())){
							pressed=cancel;
						}else if(submit.isPushed(mouseEntry.getPoint())){
							pressed=submit;
						}
					});
				}
			}
			if (enabled != null) {
				enabled.update(inputEntry, l);
			}
			if(pressed!=null){
				performAction(pressed);
				pressed=null;
			}
		}catch(ConcurrentModificationException c){
			enabled = null;
		}
	}

	private static void performAction(Button pressed) {
		if(pressed.equals(cancel)){
			//TODO: hide
		}else{
			try {
				HashMap<Resource, Integer> offered = new HashMap<>(), wanted = new HashMap<>();
				EditText t1, t2;
				Resources res;
				for (int i = 0; i < textFields.size(); i = i + 2) {
					res=Resources.values()[i/2];
					t1 = textFields.get(i);
					t2 = textFields.get(i + 1);
					if (t1.getText().isEmpty()) {
						if (!t2.getText().isEmpty()) {
							offered.put(res, new Integer(t2.getText()));
						}
					} else if (t2.getText().isEmpty()) {
						wanted.put(res, new Integer(t1.getText()));
					} else {
						int w = new Integer(t1.getText()), o = new Integer(t2.getText());
						if (w>o){
							wanted.put(res,w-o);
						}else if(o>w){
							offered.put(res,o-w);
						}
					}
					t1.setText("");
					t2.setText("");
				}

			}catch(NumberFormatException n){
				System.out.println("WTF?!");
			}
		}
	}
}
