package game;

import graph.Vertex;
<<<<<<< HEAD
import ki.AllianceRequest;
import ki.Notification;
import ki.Request;
=======
import ki.*;

>>>>>>> a006bf0b2f5cf0bb1148ccff578811ac95e6d392

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Player {

    protected String name;
<<<<<<< HEAD
    protected ArrayList<Player> alliances = new ArrayList<>();
    private ArrayList<Vertex> fields = new ArrayList<>();
    protected Queue<Request> requests = new Queue<>();
=======
    private List<Vertex> fields = new ArrayList<>();
    protected ArrayList<Player> alliances;
    private Random r;
    protected Queue<Request> requests;
>>>>>>> a006bf0b2f5cf0bb1148ccff578811ac95e6d392
    protected ArrayList<Notification> notifications;

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
<<<<<<< HEAD
        return this.fields;
    }

    public boolean isKI() {
        return this.isKI;
=======
        return ((ArrayList<Vertex>) this.fields);
>>>>>>> a006bf0b2f5cf0bb1148ccff578811ac95e6d392
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
