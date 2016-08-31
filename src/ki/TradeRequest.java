package ki;

import field.resource.Resource;
import game.Player;
import graph.Vertex;

import java.util.HashMap;
import java.util.Map;

import static game.GameOfGraphs.getGame;

/**
 * Created by Tim Bolz on 24.06.2016.
 */
public class TradeRequest extends Request {

	private HashMap<Resource,Integer> offeredResources, requestedResources;
	private Vertex place;

	public TradeRequest(Player parent, HashMap<Resource, Integer> offeredResources, HashMap<Resource, Integer> requestedResources,Vertex place,Player recipient) {
		super(parent,recipient);
		this.offeredResources = offeredResources;
		this.requestedResources = requestedResources;
		this.place=place;
	}

	public HashMap<Resource, Integer> getOfferedResources() {
		return offeredResources;
	}

	public HashMap<Resource, Integer> getRequestedResources() {
		return requestedResources;
	}

	public Vertex getPlace() {
		return place;
	}

	@Override
	void decline() {
		if(this.getParent() instanceof KIFraction){
			((KIFraction) this.getParent()).getTrust().put(this.getRecipient(),((KIFraction) this.getParent()).getTrust().get(this.getRecipient())-5);
		}
	}

	@Override
	void accept() {
		Vertex root = getGame().getKiController().getClosestVertex(place,this.getRecipient());
		if(root!=null && getGame().getGraphController().getGraph().getNeighbours(place).contains(root)) {
			HashMap<Resource,Integer> temp1= new HashMap<>();
			HashMap<Resource,Integer> temp2=new HashMap<>();
			boolean possible;
			Depot depot;
			depot=checkResources(root,temp1,requestedResources);
			temp1=depot.getMap();
			possible=depot.isPossible();
			if(possible) {
				depot=checkResources(place,temp2,offeredResources);
				temp2=depot.getMap();
				possible=depot.isPossible();
			}
			if(possible){
				root.getField().getResources().putAll(temp1);
				place.getField().getResources().putAll(temp2);
				if(this.getParent() instanceof KIFraction){
					((KIFraction) this.getParent()).getTrust().put(this.getRecipient(),((KIFraction) this.getParent()).getTrust().get(this.getRecipient())+10);
				}
			}
		}
	}

	/**
	 * überprüft, ob alle Ressourcen für den Handel verfügbar sind und gibt sowohl den neuen Resorcenstand zurück,
	 * als auch ob der Handel zud iesem Zeitpunkt möglich ist.
	 * @param place Der zu überprüfende vertex
	 * @param temp Speicherort der aktualisierten Ressourcen
	 * @param resources Die geforderten Ressourcen
	 * @return aktualisierte Ressourcen und ob der Handel möglich ist
	 */
	private Depot checkResources(Vertex place, HashMap<Resource,Integer> temp, HashMap<Resource,Integer> resources){
		Depot dep;
		boolean result=true;
		HashMap<Resource,Integer> res;
		if(resources.equals(requestedResources)){
			res=offeredResources;
		}else{
			res=requestedResources;
		}
		for (Map.Entry<Resource,Integer> e : place.getField().getResources().entrySet()) {
			if(result){
				if(place.getField().getResources().get(e.getKey())>=resources.get(e.getKey())){
					temp.put(e.getKey(),place.getField().getResources().get(e.getKey())-resources.get(e.getKey())+res.get(e.getKey()));
				}else{
					result=false;
				}
			}
		}
		dep=new Depot(result,temp);
		return dep;
	}
}
