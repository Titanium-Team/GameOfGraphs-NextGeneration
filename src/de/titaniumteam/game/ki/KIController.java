package ki;

import field.buildings.Buildings;
import field.resource.Resources;
import game.GameOfGraphs;
import game.Membership;
import game.Player;
import game.Queue;
import graph.Graph;
import graph.Vertex;

import simulation.Unit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static game.GameOfGraphs.*;

/**
 * Created by Tim on 08.06.2016.
 */
public class KIController {
	private Random r;
	private String[] names = {""};

	public KIController() {
		this.r = new Random();
	}

	/**
	 * steuert den gesamten Ablauf der KI
	 * @param allFields
	 * Alle Felder
	 */
	public void run(ArrayList<Vertex> allFields){
		KIFraction current;
		ArrayList<Vertex> fields;
		for(Vertex v1:allFields){
			if(v1.getField().getResources().get(Resources.FOOD)<v1.getField().getResources().get(Resources.POPULATION) && r.nextInt(100)<50){
				Player p = v1.getField().getPlayer();
				ArrayList<Unit> rebels = new ArrayList<Unit>();
				for(int i=0;i<v1.getField().getResources().get(Resources.POPULATION)/2;i++){
					//TODO:change to player
					//rebels.add(new Unit(Membership.UNKNOWN));
				}
				//getGame().getSimulationController().fight(v1,rebels);
				p.getNotifications().add(new Rebellion(!p.equals(v1),v1));
			}
			if(v1.getField().getPlayer() instanceof KIFraction ){
				current = ((KIFraction) v1.getField().getPlayer());
				if(!current.isFraction()) {
					if(!current.getProperties().contains(Property.PEACEFUL)){
						int minUnits=Integer.MAX_VALUE;
						if(current.getProperties().contains(Property.AGGRESSIVE)) {
							if (current.getProperties().contains(Property.STRATEGIC)) {
								minUnits = 5;
							}else{
								minUnits=3;
							}
						}else if(r.nextInt(3)+1==1){
							minUnits=4;
						}
						if(v1.getField().getUnits().size()>=minUnits){
							Vertex goal = null;
							int amount=minUnits;
							ArrayList<Vertex> neighbors= getGame().getGraphController().getGraph().getNeighbours(v1);
							if(current.getProperties().contains(Property.AGGRESSIVE)) {
								amount = v1.getField().getUnits().size();
							}
							for(Vertex v:neighbors){
								if(!current.equals(v.getField().getPlayer())){
									goal=v;
								}
							}
							if(current.getProperties().contains(Property.STRATEGIC)){
								boolean pathFree=true;
								int min=Integer.MAX_VALUE;
								Player archEnemy=null;
								Vertex tempGoal;
								for(Player p:getGame().getPlayers()){
									if(current.getTrust().get(p)<min){
										archEnemy=p;
										min=current.getTrust().get(p);
									}
								}
								tempGoal=this.getClosestVertex(v1,archEnemy);
								//ArrayList<Vertex> list = getGame().getSimulationController().giveListOfVerticesToFollow(v1,tempGoal);
								//for (Vertex vertex : list){
								//	if(pathFree &&archEnemy!=null){
								//		if(!current.equals(vertex.getField().getPlayer()) && !archEnemy.equals(vertex.getField().getPlayer())){
								//			pathFree=false;
								//		}
								//	}
								//}
								if(pathFree)goal=tempGoal;
							}
							if(goal!=null) {
								getGame().getSimulationController().moveUnits(v1, goal, amount);
							}
						}else{
							getGame().getSimulationController().createUnit(v1);
						}
					}
				}else if(((KIFraction)(v1.getField().getPlayer())).isFraction()){
					if(r.nextInt(100)>=current.getDevelopingChance()){
						ArrayList<Property> options = Property.getMissingProperties(current.getProperties());
						if(!options.isEmpty()) {
							int chance = r.nextInt(options.size());
							current.addProperty(Property.values()[chance]);
							current.setDevelopingChance(r.nextInt(5));
						}
					}
					if (current.getProperties().contains(Property.AGGRESSIVE)) {
						if (!current.getProperties().contains(Property.STRATEGIC)) {
							fields = current.getFields();
							for (Vertex v : fields) {
								if (v.getField().getUnits().size() > 3 && r.nextBoolean()) {
									ArrayList<Vertex> neighbours = getGame().getGraphController().getGraph().getNeighbours(v);
									Vertex attackedVertex=null;
									for(Vertex vertex:neighbours){
										if(!current.equals(vertex.getField().getPlayer())){
											attackedVertex=vertex;
										}
									}
									if(attackedVertex!=null) {
										getGame().getSimulationController().moveUnits(v1, attackedVertex, v1.getField().getUnits().size() );
									}
									
								} else {
									getGame().getSimulationController().createUnit(v1);
									Buildings.build(Buildings.MINE,v.getField());
								}
							}
						}
					}
					if (current.getProperties().contains(Property.DIPLOMATIC)) {
						int max = 0;
						Player partner = null;
						for (Player p : GameOfGraphs.getGame().getPlayers()) {
							if (p.getFields().size() > max) {
								partner = p;
								max = partner.getFields().size();
							}
						}

						if (partner != null) {
							if (current.getProperties().contains(Property.PEACEFUL)) {
								if (partner instanceof KIFraction) {
									if (r.nextInt(100) < ((KIFraction) partner).getTrust().get(current)) {
										int resources = 0;
                                        /*Vertex goal=current.getFields().get(0);
                                        for(Vertex vertex:current.getFields()){
                                            resources+=vertex.getField().getResources();
                                            if(vertex.getField().getResources()>goal.getField().getResources()){
                                                goal=vertex;
                                            }

                                        }
                                        if(((KIFraction) partner).getProperties().contains(Property.GREEDY)){
                                            resources=resources/10;
                                            if(goal.getField().getResources()>=resources){
                                                goal.getField().setResources(goal.getField().getResources());
                                            }
                                        }
                                        current.addAlly(partner);
                                        */
									}
								}

							} else {
								current.requestAlliance(partner);
							}
						}
					}
					if (current.getProperties().contains(Property.ECONOMIC)) {
						//TODO:trade
					}
					if (current.getProperties().contains(Property.STRATEGIC)) {
						if (current.getProperties().contains(Property.PEACEFUL)) {
							//TODO: produce just as much units as needed to defend
						}
						//TODO: scout enemies, level up mines, if strong enemies → level up defense
					}
					while(!current.getRequests().isEmpty()){
						Request req = current.getRequests().front();
						if(req instanceof AllianceRequest){
							if(!current.getProperties().contains(Property.DISTRUSTFUL)){
								int trust = current.getTrust().get(req.getParent());
								if(current.getProperties().contains(Property.DIPLOMATIC)){
									if(trust<=90){
										trust+=10;
									}else{
										trust=100;
									}
								}
								if(r.nextInt(100)<trust){
									current.addAlly(req.getParent());
									req.getParent().getAlliances().add(current);
									current.getTrust().put(req.getParent(),75);
								}
							}
						}else if(req instanceof TradeRequest){
							//TODO: specify offers, trade
						}else if(req instanceof HelpRequest){
							if(current.getAlliances().contains(req.getParent())){
								if(current.getTrust().get(req.getParent())>50+((HelpRequest) req).getAmountOfUnits()){
									ArrayList<Vertex> origin = new ArrayList<>();
									ArrayList<Vertex> neighbors = getGame().getGraphController().getGraph().getNeighbours(((HelpRequest) req).getPlace());
									int amount = ((HelpRequest) req).getAmountOfUnits();
									for (Vertex v : neighbors) {
										if (current.equals(v.getField().getPlayer())) {
											if(amount>0) {
												origin.add(v);
												amount-=v.getField().getUnmovedUnits().size()-3;
											}
										}
									}
									amount = ((HelpRequest) req).getAmountOfUnits();
									for (Vertex v : origin) {
										if(amount>0) {
											if (v.getField().getUnmovedUnits().size() + 3 >= amount) {
												getGame().getSimulationController().moveUnits(v, ((HelpRequest) req).getPlace(), amount);
												amount = 0;
											} else {
												getGame().getSimulationController().moveUnits(v, ((HelpRequest) req).getPlace(), v.getField().getUnmovedUnits().size() - 3);
												amount -= ((HelpRequest) req).getPlace().getField().getUnmovedUnits().size() - 3;
											}
										}
									}
								}
							}
						}
						current.getRequests().dequeue();
					}

					for(Notifications i:current.getNotifications()){
						if(i instanceof Attack){
							Vertex place = ((Attack) i).getPlace();
							Player opponent = ((Attack) i).getOpponent();
							if(((Attack) i).isDefense()){
								current.getTrust().put(opponent,0);
								if(i.isFightWon()){
									if(!current.getAlliances().isEmpty()){
										Vertex root=this.getClosestVertex(place,opponent);
										if(root!=null) {
											for (Vertex v : getGame().getGraphController().getGraph().getNeighbours(root)) {
												for(Player p:current.getAlliances()) {
													if (v.getField().getPlayer().equals(p)){
														p.addRequest(new HelpRequest(current,5,root));
													}
												}
											}
										}
									}
								}else{
									reclaim(current,place);
								}
							}else{
								if(!current.isFraction() && i.isFightWon()){
									current.setName("Player " + Arrays.toString(Character.toChars(65 + getGame().getPlayers().size())));
								}
							}
							if(current.equals(place.getField().getPlayer())){
								getGame().getSimulationController().createUnit(place);
							}

						}else if(i instanceof Rebellion){
							Vertex place = ((Rebellion) i).getPlace();
							if(((Rebellion) i).isSuccessful()){
								if(current.getFields().size()==1){
									current.setName("independent");
								}else{
									this.reclaim(current,place);
								}
							}else{
								getGame().getSimulationController().createUnit(place);
							}
						}
					}
				}
			}
		}
	}


	private void reclaim(KIFraction current, Vertex place) {
		Vertex v = this.getClosestVertex(place,current);
		if(v.getField().getUnits().size()>3) {
			getGame().getSimulationController().moveUnits(v, place, v.getField().getUnits().size() - 3);
		}
	}

	public  Vertex getClosestVertex(Vertex start, Player p) {
		return getClosestVertex(null, start, getGame().getGraphController().getGraph(),p);
	}

	private Vertex getClosestVertex(Queue<Vertex> q,  Vertex start, Graph graph, Player p) {
		if(!p.getFields().isEmpty()) {
			if (start != null && graph.getVertex(start.getID()) != null) {
				if (q == null) {
					graph.setAllVertexMark(false);
					q = new Queue<Vertex>();
					q.enqueue(start);
					start.setMark(true);
				}
				if (!q.isEmpty()) {
					if (!p.equals(start.getField().getPlayer())) {
						ArrayList<Vertex> neighbours = graph.getNeighbours(start);
						for (Vertex v : neighbours) {
							if (!v.isMark()) {
								q.enqueue(v);
								v.setMark(true);
								if (p.equals(start.getField().getPlayer()))
									return start;
							}
						}
						q.dequeue();
						getClosestVertex(q, q.front(), graph, p);
					}
				}
			}
			return start;
		}
		return null;
	}
}