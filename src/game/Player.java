package game;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import graph.Vertex;
import ki.AllianceRequest;
import ki.Notification;
import ki.Request;

import java.awt.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Player {

    protected String name;

    protected ArrayList<Player> alliances = new ArrayList<>();
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
    public Player(@JsonProperty("name") String name, @JsonProperty("red")  int red, @JsonProperty("blue")  int blue, @JsonProperty("green")  int green) {
        this.name = name;
        this.color = new Color(red, blue, green);
    }

    public String getName() {
        return this.name;
    }


    @JsonIgnore
    public ArrayList<Vertex> getFields() {

        return GameOfGraphs.getGame().getGraphController().getGraph().getVertices().stream().filter(vertex -> vertex.getField().getPlayer().equals(this)).collect(Collectors.toCollection(ArrayList::new));

    }

    @JsonIgnore
    public boolean isActive() {
        return !(this.getFields().isEmpty());
    }


    /*public boolean requestAlliance(Player p){
        if(p!=null) {
            if(p instanceof KIFraction){
                if(((KIFraction) p).getProperties().contains(Property.DISTRUSTFUL)){
                    return false;
                }else{
                    if(r.nextInt(100)<=((KIFraction) p).getTrust().get(this)){
                        p.getAlliances().add(this);
                        this.alliances.add(p);
                        return true;
                    }
                }
            }else {
                JPopupMenu menu = new JPopupMenu();
                menu.add(new JLabel(this.getName() + " wants to make an alliance with you!"));
                JButton acceptButton=new JButton("Accept");
                JButton denyButton =new JButton("Deny");
                menu.add(acceptButton);
                menu.add(denyButton);
            }
        }
        return false;
    }

    public ArrayList<Player> getAlliances() {
        return alliances;
    }*/


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
}