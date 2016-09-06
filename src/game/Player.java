package game;

import com.fasterxml.jackson.annotation.*;
import graph.Vertex;
import ki.AllianceRequest;
import ki.KIFraction;
import ki.Notification;
import ki.Request;

import java.awt.*;
import java.util.ArrayList;

import static game.GameOfGraphs.getGame;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({@JsonSubTypes.Type(value = KIFraction.class)})
public class Player {

    protected String name;

    @JsonIgnore
    protected ArrayList<Player> alliances = new ArrayList<>();
    @JsonIgnore
    private ArrayList<Notification> notifications = new ArrayList<>();

    @JsonIgnore
    protected Queue<Request> requests = new Queue<>();

    @JsonIgnore
    private Color color;


    public Player(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    @JsonCreator
    public Player(@JsonProperty("name") String name, @JsonProperty("red")  int red, @JsonProperty("green")  int green, @JsonProperty("blue")  int blue) {
        this.name = name;
        this.color = new Color(red, green, blue);
    }

    public String getName() {
        return this.name;
    }


    @JsonIgnore
    public ArrayList<Vertex> getFields() {

        //funktioniert  nicht/stackoverflow
        //return GameOfGraphs.getGame().getGraphController().getGraph().getVertices().stream().filter(vertex -> vertex.getField().getPlayer().equals(vertex.getField().getPlayer())).collect(Collectors.toCollection(ArrayList::new));
        ArrayList<Vertex> vertices = new ArrayList<Vertex>();
        for(Vertex v: getGame().getGraphController().getGraph().getVertices()){
            if(v.getField().getPlayer().equals(this)){
                vertices.add(v);
            }
        }
        return vertices;
    }

    @JsonIgnore
    public boolean isActive() {
        return !(this.getFields().isEmpty());
    }


    public void requestAlliance(Player p){
        if(p!=null) {
            p.addRequest(new AllianceRequest(this,p));
        }
    }

    public ArrayList<Player> getAlliances() {
        return alliances;
    }

    public void addRequest(Request request){
        this.requests.enqueue(request);
    }

    public Queue<Request> getRequests() {
        return requests;
    }

    public ArrayList<Notification> getNotifications() {
        return notifications;
    }

    public void setRequests(Queue<Request> requests) {
        this.requests = requests;
    }

    @Override
    public String toString() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    public int getRed(){
        return color.getRed();
    }

    public int getBlue(){
        return color.getBlue();
    }

    public int getGreen(){
        return color.getGreen();
    }

    public void setColor(Color color) {
        this.color = color;
    }
}