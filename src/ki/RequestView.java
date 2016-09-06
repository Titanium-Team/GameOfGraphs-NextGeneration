package ki;

import de.SweetCode.e.E;
import de.SweetCode.e.input.InputEntry;
import de.SweetCode.e.math.ILocation;
import de.SweetCode.e.rendering.GameScene;
import de.SweetCode.e.rendering.layers.Layers;
import field.FieldView;
import game.ui.*;
import game.ui.Button;

import java.awt.*;

/**
 * Created by Tim Bolz on 04.09.2016.
 */
public class RequestView extends UIComponent {
	private ILocation location;
	private Request request;
	private Button<String> accept,decline;

	public RequestView(GameScene gameScene, ILocation location) {
		super(gameScene, (component,value)->{});
		this.location=location;
	}

	@Override
	public void render(Layers layers) {
		Graphics2D g=layers.first().g();

		g.setColor(Color.lightGray);
		g.fillRoundRect(location.getX(),location.getY(),600,200,20,20);
		g.setColor(Color.red);
		g.drawRoundRect(location.getX(),location.getY(),600,200,20,20);
		Font font=g.getFont();
		g.setFont(new Font("Times New Roman",Font.ROMAN_BASELINE,16));
		g.setColor(Color.BLACK);
		g.drawString(String.valueOf(request),location.getX()+10,location.getY()+20);

		try {
			accept.setEnabled(this.isEnabled());
			decline.setEnabled(this.isEnabled());

		}catch (NullPointerException e){

		}

		g.setFont(font);
	}

	@Override
	public void update(InputEntry inputEntry, long l) {

	}

	@Override
	public boolean isActive() {
		return this.getGameScene().isActive()&&this.isEnabled();
	}

	public Request getRequest() {
		return request;
	}

	public void setRequest(Request request) {
		this.request = request;
		accept=new Button<>(this.getGameScene(),"Accept",new ILocation(location.getX()+200,location.getY()+100),(component,value)->{
			this.request.accept();
			((FieldView) this.getGameScene()).setRequestShown(false);
			decline.setEnabled(false);
			accept.setEnabled(false);
			this.request.getRecipient().getRequests().dequeue();
		});
		decline=new Button<>(this.getGameScene(),"Decline",new ILocation(location.getX()+300,location.getY()+100),(component,value)->{
			this.request.decline();
			((FieldView) this.getGameScene()).setRequestShown(false);
			decline.setEnabled(false);
			accept.setEnabled(false);
			this.request.getRecipient().getRequests().dequeue();
		});
		E.getE().addComponent(decline);
		E.getE().addComponent(accept);
		accept.setEnabled(true);
		decline.setEnabled(true);
	}
}
