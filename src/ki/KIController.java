package ki;

import field.buildings.Buildings;
import field.recipe.Recipe;
import field.recipe.RecipeResource;
import field.resource.Resources;
import game.GameOfGraphs;
import game.Player;
import game.Queue;
import graph.Graph;
import graph.List;
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
                KIFraction rebelPlayer=new KIFraction("independent");
                for(int i=0;i<v1.getField().getResources().get(Resources.POPULATION)/2;i++){
                    rebels.add(new simulation.Unit(rebelPlayer));
                }
                getGame().getSimulationController().fight(v1,rebels);
                p.getNotifications().add(new Rebellion(!p.equals(v1.getField().getPlayer()),v1));
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
                                List<Vertex> list = getGame().getSimulationController().giveListOfVerticesToFollow(v1,tempGoal);
                                list.toFirst();
                                while(list.hasAccess()){
                                    if(pathFree &&archEnemy!=null){
                                        if(!current.equals(list.getContent().getField().getPlayer()) && !archEnemy.equals(list.getContent().getField().getPlayer())){
                                            pathFree=false;
                                        }
                                    }
                                    list.next();
                                }
                                if(pathFree)goal=tempGoal;
                            }
                            if(goal!=null) {
                                getGame().getSimulationController().moveUnits(v1, goal, amount);
                            }
                        }else{
                            this.buildIfBuildable(Buildings.UNIT,v1);
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
                    fields = current.getFields();
                    for (Vertex v : fields) {
                        if (v1.getField().getResources().get(Resources.FOOD) < v1.getField().getResources().get(Resources.POPULATION)) {
                            if (v1.getField().getBuildings().get(Buildings.FARM) > 0) {
                                this.buildIfBuildable(Buildings.BUTCHER, v1);
                                this.buildIfBuildable(Buildings.WINDMILL, v1);
                            } else {
                                this.buildIfBuildable(Buildings.FARM, v1);
                            }
                        }
                        if (current.getProperties().contains(Property.AGGRESSIVE)) {
                            if (!current.getProperties().contains(Property.STRATEGIC)) {
                                if (v.getField().getUnits().size() > 3 && r.nextBoolean()) {
                                    ArrayList<Vertex> neighbours = getGame().getGraphController().getGraph().getNeighbours(v);
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
                                    this.buildIfBuildable(Buildings.UNIT, v);
                                    this.buildIfBuildable(Buildings.MINE, v);
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
                                current.requestAlliance(partner);
                            }
                        }
                        if (current.getProperties().contains(Property.ECONOMIC)) {
                            //TODO:trade

                        }
                    }
                    allFields.removeAll(fields);
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
                                    req.accept();
                                }else{
                                    req.decline();
                                }
                            }else{
                                req.decline();
                            }
                        }else if(req instanceof TradeRequest){
                            //TODO: specify offers, trade
                        }else if(req instanceof HelpRequest){
                            if(current.getAlliances().contains(req.getParent())){
                                if(current.getTrust().get(req.getParent())>50+((HelpRequest) req).getAmountOfUnits()){
                                    req.accept();
                                }else{
                                    req.decline();
                                }
                            }else{
                                req.decline();
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
                                                        p.addRequest(new HelpRequest(current,5,root,p));
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }else{
                                    if(current.getFields().size()==1){
                                        getGame().getPlayers().remove(current);
                                        current.setName("independent");
                                    }else if(current.getFields().isEmpty()){
                                        for(Player p:getGame().getPlayers()){
                                            if(p instanceof KIFraction){
                                                ((KIFraction) p).getTrust().remove(current);
                                                for(Notifications n:p.getNotifications()){

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
                                        current = null;
                                    } else{
                                        reclaim(current,place);
                                    }
                                }
                            }else{
                                if(!current.isFraction() && i.isFightWon()){
                                    int x=0;
                                    current.setName("Player " + Arrays.toString(Character.toChars(65 + x + getGame().getPlayers().size())));
                                    for (Player p : getGame().getPlayers()) {
                                        if (p.getName().equals(current.getName())){
                                            x++;
                                            current.setName("Player " + Arrays.toString(Character.toChars(65 + x + getGame().getPlayers().size())));
                                        }
                                    }
                                    getGame().getPlayers().add(current);
                                }
                            }
                            if(current.equals(place.getField().getPlayer())){
                                this.buildIfBuildable(Buildings.UNIT,place);
                            }

                        }else if(i instanceof Rebellion){
                            Vertex place = ((Rebellion) i).getPlace();
                            if(((Rebellion) i).isSuccessful()){
                                if(current.getFields().size()==1){
                                    getGame().getPlayers().remove(current);
                                    current.setName("independent");
                                }else{
                                    this.reclaim(current,place);
                                }
                            }else{
                                this.buildIfBuildable(Buildings.UNIT,place);
                            }
                        }
                    }
                }
            }
        }
    }

    private void buildIfBuildable(Buildings b,Vertex v){
        if(Buildings.isBuildable(b,v.getField())){
            Buildings.build(b,v.getField());
        }else{
            Recipe recipe=b.getRecipe();
            for(RecipeResource rRes:recipe.getItemIngredients()){
                if(v.getField().getResources().get(rRes.getResource())<rRes.getAmount()){
                    ((KIFraction) v.getField().getPlayer()).getGoals().put(rRes.getResource(),rRes.getAmount()-v.getField().getResources().get(rRes.getResource()));
                }
            }
        }
    }

    /**
     * Ein Gegenangriff wird vom nächsten Vertex gestartet
     * @param current der aktuelle Spieler
     * @param place der zu zurückerobernde Vertex
     */
    private void reclaim(KIFraction current, Vertex place) {
        Vertex v = this.getClosestVertex(place,current);
        if(v.getField().getUnits().size()>3) {
            getGame().getSimulationController().moveUnits(v, place, v.getField().getUnits().size() - 3);
        }
    }

    /**
     * gibt den nächsten Vertex eines Spielers mithilfe der Breitensuche zurück
     * @param start der UrsprungsVertex
     * @param p der Spieler
     * @return den nächsten Vertex
     */
    public  Vertex getClosestVertex(Vertex start, Player p) {
        return getClosestVertex(null, start, getGame().getGraphController().getGraph(),p);
    }

    /**
     * s.o.
     * @param q wird für Breitensuche benötigt
     * @param start der StartVertex
     * @param graph der Graph
     * @param p der Spieler
     * @return den nächsten Vertex
     */
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