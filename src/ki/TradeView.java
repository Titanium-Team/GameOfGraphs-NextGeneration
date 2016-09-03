package ki;

import de.SweetCode.e.E;
import de.SweetCode.e.input.InputEntry;
import de.SweetCode.e.math.IBoundingBox;
import de.SweetCode.e.math.ILocation;
import de.SweetCode.e.rendering.GameScene;
import de.SweetCode.e.rendering.layers.Layers;
import field.FieldView;
import field.resource.Resources;
import game.Player;
import game.ui.DropDownMenu;
import game.ui.UIComponent;
import graph.Vertex;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import static game.GameOfGraphs.getGame;

/**
 * Created by Tim Bolz on 31.08.2016.
 */
public class TradeView extends UIComponent {

	private ArrayList<game.ui.EditText<String>> textFields=new ArrayList<>();
	private boolean firstTime=true;
	private int height= Resources.values().length*50 +100;
	private game.ui.Button<String> cancel,submit;
	private DropDownMenu<Player> players;
	private Vertex currentVertex;
	private GameScene gameScene;

	private ILocation location;

	public TradeView(GameScene gameScene, ILocation location){
		super(gameScene, (component, value) -> {});
		this.location = location;
		this.gameScene = gameScene;

		for (int i = 0; i < Resources.values().length; i++) {
			if (textFields.size()<Resources.values().length*2) {
				game.ui.EditText<String> wanted = new game.ui.EditText<String>(gameScene, "", new IBoundingBox(new ILocation(location.getX()+55, location.getY()+50*i+47), new ILocation(location.getX()+45+55, location.getY()+15+50*i+47)), true, false, "Wanted", (component, value) -> {});
				game.ui.EditText<String> offered = new game.ui.EditText<String>(gameScene, "", new IBoundingBox(new ILocation(location.getX()+105, location.getY()+50*i+47), new ILocation(location.getX()+45+105, location.getY()+15+50*i+47)), true, false, "Offered", (component, value) -> {});

				textFields.add(wanted);
				textFields.add(offered);
			}

		}

		cancel = new game.ui.Button<>(gameScene, "Cancel", new ILocation(location.getX()+55,location.getY()+height-20), (component, value) -> {
			performAction();

			((FieldView) gameScene).setTradeEnabled(false);
		});
		submit = new game.ui.Button<>(gameScene, "Submit", new ILocation(location.getX()+105,location.getY()+height-20), (component, value) -> {
			performAction();

			HashMap<Resources, Integer> offered = new HashMap<>(), wanted = new HashMap<>();
			game.ui.EditText<String> t1, t2;
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
			Player p= players.getOption();
			TradeRequest tr=new TradeRequest(getGame().getCurrentPlayer(),offered,wanted,currentVertex,p);
			p.addRequest(tr);
			System.out.println(tr);
			((FieldView) gameScene).setTradeEnabled(false);
		});


	}

	@Override
	public void update(InputEntry inputEntry, long l){
	}

	@Override
	public boolean isActive() {
		return this.getGameScene().isActive() && this.isEnabled();
	}

	private void performAction() {
		for (game.ui.EditText<String> e: textFields){
			e.setEnabled(false);
		}
		players.setEnabled(false);
		submit.setEnabled(false);
		cancel.setEnabled(false);
	}

	@Override
	public void render(Layers layers) {


		Graphics2D g = layers.first().getGraphics2D();

		g.setColor(Color.WHITE);
		int width = 200;
		g.fillRoundRect(location.getX(),location.getY(), width,height,10,10);
		g.setColor(Color.BLACK);
		Font font=g.getFont();
		g.setFont(new Font("Arial",Font.BOLD,12));
		g.drawString("TRADE",location.getX()+50,location.getY()+15);
		g.setFont(font);
		g.drawRoundRect(location.getX(),location.getY(), width,height,10,10);

		if(firstTime) {
			for (game.ui.EditText<String> e: textFields){
				E.getE().addComponent(e);
				e.setTextColor(Color.black);
				e.setLineColor(Color.BLACK);
			}
			E.getE().addComponent(cancel);
			E.getE().addComponent(submit);

			firstTime=false;
		}else{
			for (int i = 0; i < Resources.values().length; i++) {
				g.setColor(Color.BLACK);
				g.drawString(Resources.values()[i].getName() + ":", location.getX() + 10, location.getY() + 50 * i + 60);
			}
		}
	}

	public void setCurrentVertex(Vertex currentVertex) {
		this.currentVertex = currentVertex;

		players= new DropDownMenu<Player>(gameScene,new ILocation(location.getX()+75,location.getY()+height-50),new LinkedList<Player>(){{
			for(Vertex v:getGame().getGraphController().getGraph().getNeighbours(currentVertex)){
				if(!v.getField().getPlayer().equals(getGame().getCurrentPlayer())){
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
		players.setEnabled(true);

		for (game.ui.EditText<String> e: textFields){
			e.setEnabled(true);
		}

		cancel.setEnabled(true);
		submit.setEnabled(true);
	}
}
