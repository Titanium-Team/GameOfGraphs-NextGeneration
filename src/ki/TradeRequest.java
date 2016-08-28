package ki;

import field.resource.Resource;
import field.resource.Resources;
import game.Player;
import graph.Vertex;

import java.util.HashMap;
import java.util.Map;

import static game.GameOfGraphs.getGame;

/**
 * Created by Tim Bolz on 24.06.2016.
 */
public class TradeRequest extends Request {
	private HashMap<Resources,Integer> offeredResources, requestedResources;
	private Vertex place;
	private Player recipient;

	public TradeRequest(Player parent, HashMap<Resources, Integer> offeredResources, HashMap<Resources, Integer> requestedResources,Vertex place,Player recipient) {
		super(parent,recipient);
		this.offeredResources = offeredResources;
		this.requestedResources = requestedResources;
		this.place=place;
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
			possible=checkResources(root,temp1,requestedResources);
			if(possible) {
				possible=checkResources(place,temp2,offeredResources);
			}
			if(possible){
				root.getField().getResources().putAll(temp1);

				place.getField().getResources().putAll(temp2);
			}
		}
	}

	private boolean checkResources(Vertex place,HashMap<Resources,Integer> temp,HashMap<Resources,Integer> resources){
		boolean result=true;
		HashMap<Resources,Integer> res;
		if(resources.equals(requestedResources)){
			res=offeredResources;
		}else{
			res=requestedResources;
		}
		for (Map.Entry<Resources,Integer> e : place.getField().getResources().entrySet()) {
			if(result){
				if(place.getField().getResources().get(e.getKey())>=resources.get(e.getKey())){
					temp.put(e.getKey(),place.getField().getResources().get(e.getKey())-resources.get(e.getKey())+res.get(e.getKey()));
				}else{
					result=false;
				}
			}
		}
		return result;
	}
}
