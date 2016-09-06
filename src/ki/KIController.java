package ki;

import field.buildings.Buildings;
import field.recipe.Recipe;
import field.recipe.RecipeResource;
import field.resource.Resources;
import game.GameOfGraphs;
import game.Player;
import game.Queue;
import graph.Vertex;
import jdk.nashorn.internal.scripts.JO;
import simulation.Unit;

import javax.swing.*;
import java.awt.*;
import java.util.*;

import static field.resource.Resources.*;
import static game.GameOfGraphs.getGame;

import game.Queue;

/**
 * Created by Tim on 08.06.2016.
 */
public class KIController {
	private Random r;
	private ArrayList<Vertex> allFields;


	public KIController() {
		this.r = new Random();
	}

	/**
	 * steuert den gesamten Ablauf der KI,indem zun?chst durch alle Felder und dann durch die der KI-Fraktionen iteriert wird.
	 * Dabei werden die Abl?ufe auf diesen Feldern anhand diverser Eigenschaften gesteuert und sowohl Benachrichtigungen als auch Anfragen bearbeitet;
	 */
	public void run(Player currentPlayer){

		KIFraction current;
		ArrayList<Vertex> fields;

		if(!currentPlayer.isActive()){
			for(Notification n:currentPlayer.getNotifications()){
				JOptionPane.showMessageDialog(null,n.getDisplayMessage());
			}
			getGame().nextTurn();
		}
		allFields=currentPlayer.getFields();
		for(Vertex v1:allFields) {
			//RebellionNotification
			if (v1.getField().getResources().get(Resources.FOOD) < 0 && r.nextInt(100) < 50 && v1.getField().getResources().get(POPULATION) > 1) {
				ArrayList<Unit> rebels = new ArrayList<>();
				KIFraction rebelPlayer = new KIFraction("Independent", new Color(232, 77, 91));
				for (int i = 0; i < v1.getField().getResources().get(POPULATION) / 2; i++) {
					rebels.add(new Unit(rebelPlayer));
				}
				v1.getField().getResources().put(POPULATION, v1.getField().getResources().get(POPULATION) - rebels.size());
				getGame().getSimulationController().fight(null, v1, rebels);
				if(rebelPlayer.isActive())getGame().getPlayers().add(rebelPlayer);
				if(currentPlayer.isActive()) {
					currentPlayer.getNotifications().add(new RebellionNotification(!currentPlayer.equals(v1.getField().getPlayer()), v1));
					Notification toRemove = null;
					for (Notification n : currentPlayer.getNotifications()) {
						if (n instanceof AttackNotification) {
							if (((AttackNotification) n).getOpponent().equals(rebelPlayer)) {
								toRemove = n;
							}
						}
					}
					currentPlayer.getNotifications().remove(toRemove);
				}else{
					if(!(currentPlayer instanceof KIFraction))JOptionPane.showMessageDialog(null,new RebellionNotification(!currentPlayer.equals(v1.getField().getPlayer()), v1).getDisplayMessage());
					getGame().nextTurn();
				}
			}

			if (currentPlayer instanceof KIFraction && getGame().getPlayers().contains(currentPlayer)) {
				//check goals
				current = (KIFraction)currentPlayer;
				getGame().getSimulationController().run(current);
				if (current.getGoals().containsKey(v1)) {
					Iterator<Map.Entry<Resources, Integer>> aIterator = current.getGoals().get(v1).entrySet().iterator();
					while (aIterator.hasNext()) {

						Map.Entry<Resources, Integer> entry = aIterator.next();

						if (v1.getField().getResources().get(entry.getKey()) >= entry.getValue()) {
							aIterator.remove();
						}
					}
				} else {
					current.getGoals().putIfAbsent(v1, new HashMap<Resources, Integer>());
				}

				if (v1.getField().getBuildings().get(Buildings.FARM)==0 || v1.getField().getResources().get(Resources.FOOD) < v1.getField().getResources().get(POPULATION) || current.getGoals().containsKey(v1) && current.getGoals().get(v1).containsKey(FOOD)) {
					this.buildIfBuildable(Buildings.FARM, v1);
				}
				if(current.getGoals().get(v1).containsKey(IRON) && v1.getField().getLocalResource().equals(IRON) || current.getGoals().get(v1).containsKey(STONE)){
					this.buildIfBuildable(Buildings.MINE,v1);
				}

				if (!current.isFraction()) {
					if(!current.getName().equals("Independent")){
						current.setName("Independent");
						current.setColor(new Color(232, 77, 91));
					}
					if (!current.getProperties().contains(Property.PEACEFUL)) {
						int minUnits = Integer.MAX_VALUE;
						if (current.getProperties().contains(Property.AGGRESSIVE)) {
							if (current.getProperties().contains(Property.STRATEGIC)) {
								minUnits = 4;
							} else {
								minUnits = 2;
							}
						} else if (r.nextInt(3) == 1) {
							minUnits = 3;
						}
						if (minUnits<Integer.MAX_VALUE && v1.getField().getUnits().size() >= minUnits+1) {
							Vertex goal = null;
							int amount = minUnits;
							ArrayList<Vertex> neighbors = getGame().getGraphController().getGraph().getNeighbours(v1);
							if (current.getProperties().contains(Property.AGGRESSIVE)) {
								amount = v1.getField().getUnits().size()-2;
							}
							for (Vertex v : neighbors) {
								if (!current.equals(v.getField().getPlayer())) {
									goal = v;
								}
							}
							if (goal != null) {
								getGame().getSimulationController().moveUnits(v1, goal, amount);
							}
						} else if(minUnits<Integer.MAX_VALUE) {
							if(v1.getField().getLocalResource().equals(IRON)) {
								this.buildIfBuildable(Buildings.UNIT, v1);
							}
						}
					}
				} else if (current.isFraction()) {
					if(current.getName().equalsIgnoreCase("independent")){
						int x = 1;
						current.setName("KIPlayer " + x);
						current.setColor(new Color(r.nextInt(255),r.nextInt(255),r.nextInt(255)));
						for (Player p : getGame().getPlayers()) {
							if (p.getName().equals(current.getName())) {
								x++;
								current.setName("KIPlayer " + x);
							}
						}
					}
					if (r.nextInt(100) <= current.getDevelopingChance()) {
						ArrayList<Property> options = Property.getMissingProperties(current.getProperties());
						if (!options.isEmpty()) {
							int chance = r.nextInt(options.size());
							current.addProperty(Property.values()[chance]);
							current.setDevelopingChance(r.nextInt(5*currentPlayer.getFields().size()));
						}
					}
					if (current.getProperties().contains(Property.AGGRESSIVE)) {
						if (!current.getProperties().contains(Property.STRATEGIC)) {
							if (v1.getField().getUnits().size() > 3 && r.nextBoolean()) {
								ArrayList<Vertex> neighbours = getGame().getGraphController().getGraph().getNeighbours(v1);
								Vertex attackedVertex = null;
								for (Vertex vertex : neighbours) {
									if (!current.equals(vertex.getField().getPlayer())) {
										attackedVertex = vertex;
									}
								}
								if (attackedVertex != null) {
									getGame().getSimulationController().moveUnits(v1, attackedVertex, v1.getField().getUnits().size());
								}
							} else {
								this.buildIfBuildable(Buildings.UNIT, v1);
								this.buildIfBuildable(Buildings.MINE, v1);
							}
						}
					}
					if (current.getProperties().contains(Property.DIPLOMATIC)) {
						int max = 0;
						Player partner = null;
						for (Player p : GameOfGraphs.getGame().getPlayers()) {
							if (!(partner instanceof KIFraction) || ((KIFraction) partner).isFraction()) {
								if (p.getFields().size() > max) {
									partner = p;
									max = partner.getFields().size();
								}
							}
						}

						if (partner != null) {
							current.requestAlliance(partner);
						}
					}
					current.getProperties().add(Property.ECONOMIC);
					if (current.getProperties().contains(Property.ECONOMIC)) {
						Player partner = GameOfGraphs.getGame().getPlayers().get(0);
						int maxTrust = -1;
						for (Vertex vertex : getGame().getGraphController().getGraph().getNeighbours(v1)) {
							if (!vertex.getField().getPlayer().equals(current) && current.getTrust().containsKey(vertex.getField().getPlayer()) && current.getTrust().get(vertex.getField().getPlayer()) > maxTrust) {
								if (!(partner instanceof KIFraction) || ((KIFraction) partner).isFraction()) {
									partner = vertex.getField().getPlayer();
									maxTrust = current.getTrust().get(vertex.getField().getPlayer());
								}
							}
						}
						if (partner != null) {
							HashMap<Resources, Integer> goals, wanted = new HashMap<>(), offered = new HashMap<>(), res;
							goals = current.getGoals().get(v1);
							int greed = r.nextInt(goals.entrySet().size() + 1);
							for (Map.Entry<Resources, Integer> e : goals.entrySet()) {
								if (greed > 0) {
									wanted.put(e.getKey(), e.getValue() - v1.getField().getResources().get(e.getKey()));
									greed--;
								}
							}
							res = ((HashMap<Resources, Integer>) v1.getField().getResources());
							int generosity = r.nextInt(res.entrySet().size() - goals.entrySet().size()) + 1;
							for (Map.Entry<Resources, Integer> e : res.entrySet()) {
								if (generosity > 0 && (!goals.containsKey(e.getKey()) || goals.get(e.getKey()) > 0) && e.getValue() > 1) {
									offered.put(e.getKey(), r.nextInt(e.getValue() / 2));
								}
							}
							partner.getRequests().enqueue(new TradeRequest(current, offered, wanted, v1, partner));
						}
					}
					while (!current.getRequests().isEmpty()) {
						Request req = current.getRequests().front();
						if (req instanceof AllianceRequest) {
							if (!current.getProperties().contains(Property.DISTRUSTFUL)) {
								int trust = (current.getTrust().containsKey(req.getParent()) ? current.getTrust().get(req.getParent()) : 1);
								if (current.getProperties().contains(Property.DIPLOMATIC)) {
									if (trust <= 90) {
										trust += 10;
									} else {
										trust = 100;
									}
								}
								if (r.nextInt(100) < trust) {
									req.accept();
								} else {
									req.decline();
								}
							} else {
								req.decline();
							}
						} else if (req instanceof TradeRequest) {
							HashMap<Resources, Integer> offRes = ((TradeRequest) req).getOfferedResources();
							HashMap<Resources, Integer> reqRes = ((TradeRequest) req).getRequestedResources();
							Vertex place = ((TradeRequest) req).getPlace();
							Vertex root= ((TradeRequest) req).getRoot();
							HashMap<Resources, Integer> res = current.getGoals().get(root);
							if(res!=null) {
								boolean isGoal = false;
								int minTrust = 60;
								for (Map.Entry<Resources, Integer> e : reqRes.entrySet()) {
									if (!isGoal) {
										if (res != null && res.containsKey(e.getKey())) {
											isGoal = true;
										}
									}
								}

								if (!isGoal) {
									for (Map.Entry<Resources, Integer> e : offRes.entrySet()) {
										if (res.containsKey(e.getKey())) {
											minTrust -= 10;
											if (e.getValue() >= res.get(e.getKey())) {
												minTrust -= 5;
											}
										}else if(e.getValue()<=0){
											minTrust=Integer.MAX_VALUE;

										}
									}
									if (current.getTrust().get(req.getParent()) >= r.nextInt(minTrust) + 1) {
										req.accept();
									} else {
										req.decline();
									}
								} else {
									req.decline();
								}
							}else{
								current.getGoals().put(v1,new HashMap<>());
							}
						} else if (req instanceof HelpRequest) {
							if (current.getAlliances().contains(req.getParent())) {
								if (current.getTrust().get(req.getParent()) > 50 + ((HelpRequest) req).getAmountOfUnits()) {
									req.accept();
								} else {
									req.decline();
								}
							} else {
								req.decline();
							}
						}
						current.getRequests().dequeue();
					}

					for (Notification n : current.getNotifications()) {
						if (current != null) {
							if (n instanceof AttackNotification) {
								AttackNotification attackNotification = (AttackNotification) n;
								Vertex place = attackNotification.getPlace();
								Player opponent = attackNotification.getOpponent();
								if (attackNotification.isDefense()) {
									current.getTrust().put(opponent, 0);
									if (attackNotification.isFightWon()) {
										if (!current.getAlliances().isEmpty()) {
											Vertex root = null;
											for(Vertex v:getGame().getGraphController().getGraph().getNeighbours(v1)){
												if(v.getField().getPlayer().equals(((AttackNotification) n).getOpponent())){
													root=v;
												}
											}
											if (root != null) {
												for (Vertex v : getGame().getGraphController().getGraph().getNeighbours(root)) {
													for (Player p : current.getAlliances()) {
														if (v.getField().getPlayer().equals(p)) {
															p.addRequest(new HelpRequest(current, 5, root, p));
														}
													}
												}
											}
										}
									} else {
										current.getGoals().remove(attackNotification.getPlace());
										if (current.getFields().size() == 1) {
											current.setName("independent");
										} else if (current.getFields().isEmpty()) {
											getGame().nextTurn();
										} else {
											reclaim(current, place);
										}
									}
								} else {
									if (!current.isFraction() && attackNotification.isFightWon()) {
										int x = 1;
										current.setName("KIPlayer " + x);
										current.setColor(new Color(r.nextInt(255),r.nextInt(255),r.nextInt(255)));
										for (Player p : getGame().getPlayers()) {
											if (p.getName().equals(current.getName())) {
												x++;
												current.setName("KIPlayer " + x);
											}
										}
									}
								}
								if (current != null && current.equals(place.getField().getPlayer())) {
									this.buildIfBuildable(Buildings.UNIT, place);
								}

							} else if (n instanceof RebellionNotification) {
								Vertex place = ((RebellionNotification) n).getPlace();
								if (((RebellionNotification) n).isSuccessful()) {
									if (current.getFields().size() == 1) {
										current.setName("independent");
									} else if (current.getFields().isEmpty()) {
										getGame().nextTurn();
									} else {
										this.reclaim(current, place);
									}
								} else {
									this.buildIfBuildable(Buildings.UNIT, place);
								}
							}
						}
					}
				}
			}
		}
	}

	public void deletePlayer(Player current){
		if(current.getFields().isEmpty()){
			for(Player p:getGame().getPlayers()){
				if(p instanceof KIFraction){
					((KIFraction) p).getTrust().remove(current);
				}
				for(Notification n:p.getNotifications()){
					if(n instanceof AttackNotification){
						if(((AttackNotification) n).getOpponent().equals(current)){
							((AttackNotification) n).setOpponent(null);
						}
					}
				}
				p.getAlliances().remove(current);
				Queue<Request> r=new Queue<>();
				while(!p.getRequests().isEmpty()){
					if(!p.getRequests().front().getParent().equals(current)){
						r.enqueue(p.getRequests().front());
					}
					p.getRequests().dequeue();
				}
				p.setRequests(r);
			}
		}
	}

	private void buildIfBuildable(Buildings b,Vertex v){
		if(Buildings.isBuildable(b,v.getField())){
			Buildings.build(b,v.getField(), false);
		}else{
			Recipe recipe=b.getRecipe();
			for(RecipeResource rRes:recipe.getItemIngredients()){
				if(v.getField().getResources().get(rRes.getResource())<rRes.getAmount()){
					try {
						if (!((KIFraction) v.getField().getPlayer()).getGoals().get(v).containsKey(rRes) || ((KIFraction) v.getField().getPlayer()).getGoals().get(v).get(rRes) < rRes.getAmount()) {
							((KIFraction) v.getField().getPlayer()).getGoals().get(v).put(rRes.getResource(), rRes.getAmount());
						}

					}catch(NullPointerException n){}
				}
			}
		}
	}


	/**
	 * Ein Gegenangriff wird vom n?chsten Vertex gestartet
	 * @param current der aktuelle Spieler
	 * @param place der zu zur?ckerobernde Vertex
	 */
	private void reclaim(KIFraction current, Vertex place) {
		Vertex v = null;
		for(Vertex v2:getGame().getGraphController().getGraph().getNeighbours(place)){
			if(v2.getField().getPlayer().equals(place.getField().getPlayer())){
				v=v2;
			}
		}

		if(v!=null && v.getField().getUnits().size()>3) {
			getGame().getSimulationController().moveUnits(v, place, v.getField().getUnits().size() - 3);
		}
	}



}
