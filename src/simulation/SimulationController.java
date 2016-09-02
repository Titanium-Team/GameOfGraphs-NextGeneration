package simulation;

import de.SweetCode.e.E;
import game.GameOfGraphs;
import game.Player;
import graph.Edge;
import graph.Graph;
import graph.List;
import graph.Vertex;
import ki.AttackNotification;

import javax.swing.*;
import java.util.ArrayList;

public class SimulationController {

    private Graph graph = GameOfGraphs.getGame().getGraphController().getGraph();
    private Player currentPlayer;

    public SimulationController(Player player){
        this.currentPlayer = player;
    }

    //TODO: If-Verzweigung überprüfen ob nötig. (If not player vertex)


    public void run(Player currentPlayer){

        this.currentPlayer = currentPlayer;

        if (graph == null){
            graph = GameOfGraphs.getGame().getGraphController().getGraph();
        }


        /*
        Units des currentPlayer werden auf "nicht bewegt" gesetzt, damit
        sie in der Runde vom Spieler überhaupt bewegt werden können.
         */

        ArrayList<Vertex> allVertices = graph.getVertices();
        ArrayList<Vertex> vertices = new ArrayList<>();
        for (Vertex vertex: allVertices) {
            if (vertex.getField().getPlayer() == currentPlayer){
                vertices.add(vertex);
            }
        }
        for (Vertex vertex: vertices) {
            ArrayList<Unit> units = vertex.getField().getUnits();
            for (Unit unit: units) {
                unit.setMoved(false);
            }
        }
    }


    //TODO: Klären ob die Methode benötigt wird.

    /*
    public void createUnit(Vertex vertex) {
        if (currentPlayer == vertex.getField().getPlayer()){
            vertex.getField().getUnits().add(new Unit(vertex.getField().getPlayer()));
        }else{
            JOptionPane.showMessageDialog(null,"Nicht dein Field!","Fehler",JOptionPane.ERROR_MESSAGE);
            System.out.println("Not your field!");
        }
    }
    */

    /**
     * Eine Methode um festzustellen, welche Units von welchem Feld abgezogen werden.
     *
     * @param fromVertex
     * Der Vertex, von dem die Units abgezigen werden.
     * @param amountUnits
     * Wieviele Units abgezogen werden sollen.
     * @return
     * Erstell eine ArrayList die in anderen Methoden zu simulierung von der Bewegung benutzt wird.
     */

    private ArrayList<Unit> getListOfUnitsToMove(Vertex fromVertex, Vertex current, Vertex target , int amountUnits){
        /**
         * Return eine Liste mit Units, die einem Feld
         * abgezogen werden um einem anderen Field zu adden.
         **/
        if (currentPlayer == fromVertex.getField().getPlayer()) {
            if (fromVertex.getField().getUnmovedUnits().size() >= amountUnits) {
                ArrayList<Unit> fromFieldUnits = fromVertex.getField().getUnits();
                ArrayList<Unit> toFieldUnits = new ArrayList<>();
                for (int i = 0; i < amountUnits; i++) {
                    if(current == target) {
                        fromFieldUnits.get(i).setMoved(true);
                    }
                    toFieldUnits.add(fromFieldUnits.get(i));
                }

                for(int i = amountUnits - 1; i >= 0; i--){
                    fromFieldUnits.remove(i);

                }

                return toFieldUnits;
            }
        }else{
            System.out.println("Not your field!");
        }
        return null;
    }

    /**
     * Die Hauptmethode, die die Bewegung der Truppen simuliert.
     * Dabei werden jeweils immer zwei Vertices angegeben, zwischen denen
     * eine Edge existiert, die von den Units dann abgegangen wird.
     *
     *
     * @param start
     * Der Knoten von dem die Truppen losgehen.
     * @param end
     * Der Knoten an dem sie ankommen sollen.
     * @param amountUnits
     * Wieviele Units jeweils gehen sollen.
     */


    public void moveUnits(Vertex start, Vertex end, int amountUnits) {
        if (currentPlayer == start.getField().getPlayer()) {
            List<Vertex> path = giveListOfVerticesToFollow(start, end);
            if (!isMovementPossible(path)) {
                JOptionPane.showMessageDialog(null, "Das Zielfeld ist zuweit weg oder ungültig! Bitte wähle einen nähres Feld (unter 50 km)");
            } else {
                if (path != null) {
                    path.toFirst();
                    while (path.hasAccess() && path.getNext() != null) {
                        ArrayList<Unit> units = getListOfUnitsToMove(path.getContent(), path.getNext(), end ,amountUnits);
                        assert units != null;
                        if (path.getNext().getField().getPlayer() != currentPlayer && !path.getNext().getField().getUnits().isEmpty()) {
                            fight(start, path.getNext(), units);
                            /*if (units.get(0) != null) {
                                JOptionPane.showMessageDialog(null, "Deine Truppen haben gewonnen!");
                            } else if (path.getNext().getField().getUnits().get(0) != null) {
                                JOptionPane.showMessageDialog(null, "Die Truppen des Gegners haben gewonnen!");
                            } else {
                                JOptionPane.showMessageDialog(null, "Keine der beiden Truppen hat gewonnen!");
                            }*/
                        } else {
                            path.getNext().getField().setPlayer(currentPlayer);
                        }
                        addUnitsToField(path.getNext(), units);
                        path.next();
                    }
                }
            }
        }
    }

    /**
     * Einfache Methode die eine ArrayList von Units einem Field hinzufügt.
     *
     * @param vertex
     * Das Field dem die Units hinzugefügt werden sollen.
     * @param units
     * Die ArrayList der Units die hinzugefügt werden soll.
     */

    private void addUnitsToField(Vertex vertex, ArrayList<Unit> units){
        if (currentPlayer == vertex.getField().getPlayer()) {
            vertex.getField().getUnits().addAll(units);
        }
    }

    public ArrayList<Vertex> showMovementPossibilities(Vertex vertex){
        ArrayList<Vertex> newList = new ArrayList<>();
        return showMovementPossibilities(vertex, 0, newList);
    }

    private ArrayList<Vertex> showMovementPossibilities(Vertex vertex, double weight, ArrayList<Vertex> markedVertices){
        if (weight <= 50) {
            ArrayList<Vertex> neighbours = graph.getNeighbours(vertex);
            if (neighbours != null) {
                for (int i = 0; i < neighbours.size(); i++) {
                    showMovementPossibilities(neighbours.get(i), graph.getEdge(vertex, neighbours.get(i)).getWeight() + weight, markedVertices);
                }
            }
            vertex.setMarkTarget(true);
            markedVertices.add(vertex);
        }
        return markedVertices;
    }


    private boolean isMovementPossible(List<Vertex> vertexList){
        double weight = 0.0;
        vertexList.toFirst();
        while (vertexList.hasAccess() && vertexList.getNext() != null){
            Edge e = graph.getEdge(vertexList.getContent(), vertexList.getNext());
            if (e != null){
                weight += e.getWeight();
            }else {
                return false;
            }
            if (weight > 50){
                return false;
            }
            vertexList.next();
        }
        return true;
    }

    /**
     * Die Methode gibt eine Liste wieder, die später bei moveUnits()
     * abgearbeitet wird.
     * Dabei wird der
     * Dijkstra-Algorithmus
     * benutzt, um den kürzesten Weg zwischen zwei Vertices zu finden.
     *
     *
     * @param start
     * Der Vertex, von dem gestartet wird.
     * @param destination
     * Der Vertex, der erreicht werden soll.
     * @return
     * Eine Liste von Vertices, die den kürzesten Weg zwischen dem Start Vertex und dem Destination Vertex entsprechen.
     */


    //TODO: Dijkstra in der Präsi zeigen!

    public List<Vertex> giveListOfVerticesToFollow(Vertex start, Vertex destination) {
        List<Vertex> path = new List<>();

        ArrayList<Vertex> unvisitedArrayList = graph.getVertices();
        List<Vertex> unvisited = new List<>();
        for (Vertex vertex: unvisitedArrayList) {
            unvisited.append(vertex);
        }
        path.append(start);
        /**
         * Eine Liste wird erstellt, die die unbesuchten Knoten darstellt.
         * Die Entfernungen der unbesuchten Knoten wird auf unendlich gesetzt.
         * Die des Startknotens auf 0.
         */

        unvisited.toFirst();
        while(unvisited.hasAccess()) {
            Vertex v = unvisited.getContent();
            if (v != start) {
                v.setDist(Double.MAX_VALUE);
                v.setParent(null);
            }else{
                v.setDist(0);
                v.setParent(null);
            }
            unvisited.next();
        }


        while(!unvisited.isEmpty()){
            Vertex v0 = null;

            unvisited.toFirst();
            while(unvisited.hasAccess()){
                Vertex v = unvisited.getContent();
                if (v0 == null || v.getDist() < v0.getDist()) {
                    v0 = v;
                }

                unvisited.next();
            }

            if (v0 == destination) {
                Vertex v = v0;

                do {
                    assert v != null;
                    //path.toFirst();
                    path.append(v);

                    /*
                    Der letzte Vertex des path könnte ein Vertex des Gegners sein, damit dieser angepeilt
                    werden kann für einen Angriff.
                    Danach wird der path zurückgegeben damit die nicht einfach überlaufen werden.
                     */
                    if (v.getField().getPlayer() != currentPlayer){
                        return path;
                    }

                    v = v.getParent();
                } while(v != null);
            }

            //v0 wird entfernt
            unvisited.toFirst();
            while(unvisited.hasAccess()){
                Vertex v = unvisited.getContent();
                if (v == v0) {
                    unvisited.remove();
                }

                unvisited.next();
            }

            // durch alle Nachbarn durchgehen
            unvisited.toFirst();
            while(unvisited.hasAccess()) {
                Vertex v = unvisited.getContent();
                Edge e = graph.getEdge(v0, v);

                if (e != null) {
                    assert v0 != null;
                    double dist0 = v0.getDist() + graph.getEdge(v0, v).getWeight();
                    if (dist0 < v.getDist()) {
                        v.setDist(dist0);
                        v.setParent(v0);
                    }
                }

                unvisited.next();
            }
        }

        return path;
    }

    /**
     * Erste einfach Simulation eines Kampfes zwischen zwei Soldatentrupps.
     *
     * @param vertex
     * Der Vertex, der angegriffen wird.
     * Aus dem Vertex wird die ArrayList der Units genommen,
     * die den Vertex verteidigen.
     * @param attackingUnits
     * Die ArrayList der Units, die den Angriff leiten.
     */

    //TODO: Verbesserung des Kampf-Systems
    public void fight(Vertex origin, Vertex vertex, ArrayList<Unit> attackingUnits){

        if(attackingUnits.isEmpty()) {
            //throw new IllegalArgumentException("WHY DO YOU ATTACK WITH NO UNITS AT ALL. THAT IS NOT HOW IT WORKS. DAMIT IT A-ARON.");
            return;
        }

        ArrayList<Unit> defendingUnits = vertex.getField().getUnits();

        boolean attackWin = false;
        int aU, dU;
        aU = attackingUnits.size();
        dU = defendingUnits.size();

        /*while(aU != 0 && dU != 0){
            Random rn = new Random();

            int aURoll = rn.nextInt(aU) + 1;
            int dUROLL = rn.nextInt(dU) + 1;
            if (aURoll > dUROLL){
                dU--;
            }else{
                aU--;
            }
        }*/

        if(dU == 0) {
            attackWin = true;
        } else {
            attackWin = E.getE().getRandom(false).nextInt(aU) > E.getE().getRandom(false).nextInt(dU);
        }

        Player defender = vertex.getField().getPlayer();
        Player attacker = attackingUnits.get(0).getPlayer();

        if (attackWin){

            vertex.getField().getUnits().clear();
            vertex.getField().setPlayer(attacker);

            attacker.getNotifications().add(new AttackNotification(defender, vertex, false, true));
            defender.getNotifications().add(new AttackNotification(attacker, vertex, true, false));
        }else{
            if(!(origin == null)) {
                origin.getField().getUnits().removeAll(attackingUnits);
            }
            defender.getNotifications().add(new AttackNotification(attacker, vertex, true, true));
            attacker.getNotifications().add(new AttackNotification(defender, vertex, false, false));
        }

    }

}
