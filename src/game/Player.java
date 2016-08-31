package game;

import graph.Vertex;
import ki.AllianceRequest;
import ki.Notification;
import ki.Request;
import java.util.ArrayList;

public class Player {

    protected String name;
    protected ArrayList<Player> alliances = new ArrayList<>();
    private ArrayList<Vertex> fields = new ArrayList<>();
    private boolean isKI = false;
    protected Queue<Request> requests = new Queue<>();
    protected ArrayList<Notification> notifications;



    public Player(String name, boolean isKI) {
        this.name = name;
        this.isKI = isKI;
    }

    public String getName() {
        return this.name;
    }

    public ArrayList<Vertex> getFields() {
        return this.fields;
    }

    public boolean isKI() {
        return this.isKI;
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
}