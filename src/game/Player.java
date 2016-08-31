package game;

import graph.Vertex;
import ki.*;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Player {

    protected String name;
    private List<Vertex> fields = new ArrayList<>();
    protected ArrayList<Player> alliances;
    private Random r;
    protected Queue<Request> requests;
    protected ArrayList<Notifications> notifications;

    public Player(String name) {
        this.name = name;
        alliances=new ArrayList<Player>();
        r=new Random();

        requests=new Queue<Request>();

    }

    public String getName() {
        return this.name;
    }

    public ArrayList<Vertex> getFields() {
        return ((ArrayList<Vertex>) this.fields);
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


    public boolean requestAlliance(Player p){
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
                p.addRequest(new AllianceRequest(this,p));
            }
        }
        return false;
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

    public ArrayList<Notifications> getNotifications() {
        return notifications;
    }

    public void setRequests(Queue<Request> requests) {
        this.requests = requests;
    }
}
