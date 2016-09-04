package ki;

import field.resource.Resource;
import field.resource.Resources;
import game.Player;
import graph.Vertex;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static game.GameOfGraphs.getGame;

/**
 * Created by Tim Bolz on 24.06.2016.
 */
public class TradeRequest extends Request {

	private HashMap<Resources,Integer> offeredResources, requestedResources;
	private Vertex place;

	public TradeRequest(Player parent, HashMap<Resources, Integer> offeredResources, HashMap<Resources, Integer> requestedResources, Vertex place, Player recipient) {
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
			((KIFraction) this.getParent()).getTrust().put(this.getRecipient(),((KIFraction) this.getParent()).getTrust().get(this.getRecipient())-5);
		}
	}

	@Override
	void accept() {
		Vertex root = null;
		for (Vertex v : getGame().getGraphController().getGraph().getNeighbours(place)) {
			if (root==null && v.getField().getPlayer().equals(getRecipient())) {
				root=v;
			}
		}
		if(root!=null && getGame().getGraphController().getGraph().getNeighbours(place).contains(root)) {
			HashMap<Resources,Integer> temp1= new HashMap<>();
			HashMap<Resources,Integer> temp2=new HashMap<>();
			boolean possible;
			Depot depot;
			depot=checkResources(root,temp1,requestedResources);
			temp1=depot.getMap();
			possible=depot.isPossible();
			if(possible) {
				depot = checkResources(place, temp2, offeredResources);
				temp2 = depot.getMap();
				possible = depot.isPossible();
				if (possible) {
					root.getField().getResources().putAll(temp1);
					place.getField().getResources().putAll(temp2);
					if (this.getParent() instanceof KIFraction) {
						((KIFraction) this.getParent()).getTrust().put(this.getRecipient(), ((KIFraction) this.getParent()).getTrust().get(this.getRecipient()) + 10);
					}
				}
			}
			if(!possible){
				JOptionPane.showMessageDialog(null,"Trade failed");
			}else{
				JOptionPane.showMessageDialog(null,"Trade successful");
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
	private Depot checkResources(Vertex place, HashMap<Resources,Integer> temp, HashMap<Resources,Integer> resources){
		Depot dep;
		boolean result=true;
		HashMap<Resources,Integer> res;
		if(resources.equals(requestedResources)){
			res=offeredResources;
		}else{
			res=requestedResources;
		}
		for (Map.Entry<Resources,Integer> e : res.entrySet()) {
			if(result){
				temp.put(e.getKey(),place.getField().getResources().get(e.getKey())+res.get(e.getKey()));
			}
		}
		for (Map.Entry<Resources,Integer> e : resources.entrySet()) {
			if(result && place.getField().getResources().get(e.getKey())>=resources.get(e.getKey())){
				temp.put(e.getKey(),place.getField().getResources().get(e.getKey())-resources.get(e.getKey()));
			}
		}

		dep=new Depot(result,temp);
		return dep;
	}

	@Override
	public String toString() {
		String result =getParent().toString() + " wants to trade with you.He offers you ";
		Set<Map.Entry<Resources,Integer>> set=offeredResources.entrySet();
		for (Map.Entry<Resources, Integer>e:set){
			result+=e.getValue() + " " + e.getKey().toString();
			result+=",";
		}
		if(result.lastIndexOf(',')==result.length()-1){
			result=result.substring(0,result.length()-1) +" ";
		}
		result+="and wants ";
		set=requestedResources.entrySet();
		for (Map.Entry<Resources, Integer>e:set){
			result+=e.getValue() + " " +  e.getKey().toString();
			result+=",";
		}
		if(result.lastIndexOf(',')==result.length()-1){
			result=result.substring(0,result.length()-1) +" ";
		}
		result+="in return.";
		return result;
	}
}
