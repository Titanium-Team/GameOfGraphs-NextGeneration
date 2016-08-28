package ki;

import field.resource.Resources;
import game.Player;
import graph.Vertex;

import java.util.HashMap;

import static game.GameOfGraphs.getGame;

/**
 * Created by Tim Bolz on 24.06.2016.
 */
public class TradeRequest extends Request {
	private HashMap<Resources,Integer> offeredResources, requestedResources;
	private Vertex place;
	private Player recipient;

	public TradeRequest(Player parent, HashMap<Resources, Integer> offeredResources, HashMap<Resources, Integer> requestedResources,Vertex place,Player recipient) {
		super(parent);
		this.offeredResources = offeredResources;
		this.requestedResources = requestedResources;
		this.place=place;
		this.recipient=recipient;
	}

	public HashMap<Resources, Integer> getOfferedResources() {
		return offeredResources;
	}

	public HashMap<Resources, Integer> getRequestedResources() {
		return requestedResources;
	}

	public Vertex getPlace() {
		return place;
	}

	@Override
	void decline() {
		if(this.getParent() instanceof KIFraction){
			((KIFraction) this.getParent()).getTrust().put(recipient,((KIFraction) this.getParent()).getTrust().get(recipient)-5);
		}
	}

	@Override
	void accept() {
		Vertex root = getGame().getKiController().getClosestVertex(place,recipient);
		if(root!=null && getGame().getGraphController().getGraph().getNeighbours(place).contains(root)) {
			HashMap<Resources,Integer> temp1= new HashMap<>();
			HashMap<Resources,Integer> temp2=new HashMap<>();
			boolean possible=true;
			/*for (Map.Entry<Resource,Integer> e : root.getField().getResources().entrySet()) {
				if(possible){
					if(root.getField().getResources().containsKey(res) && root.getField().getResources().get(res)>=requestedResources.get(res)){
						temp1.put(res,root.getField().getResources().get(res)-requestedResources.get(res));
					}else{
						possible=false;
					}
					if(place.getField().getResources().containsKey(res) && place.getField().getResources().get(res)>=offeredResources.get(res))
				}
			}
			if(possible){

			}*/
		}
	}
}
