package connection;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import game.GameOfGraphs;
import game.Player;
import graph.Graph;
import graph.Vertex;
import ki.KIFraction;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class Connector {
    private static final String url = "jdbc:mysql://dk-developer.ddns.net:3306/GameOfGraphs";
    private static final String user = "GameOfGraphs";
    private static final String password = "game";

    private static int gameId = -1;
    private static int playerId;

    private static boolean host;

    private static Statement setup(){
        try {
            Connection connection = DriverManager.getConnection(url, user, password);

            Statement statement = connection.createStatement();

            return statement;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

     public static boolean joinGame(Player p){
         Statement statement = setup();

         String player = null;
         ObjectMapper mapper = new ObjectMapper();
         try {
             player = mapper.writeValueAsString(p);
         } catch (JsonProcessingException e) {
             e.printStackTrace();
         }

         try {
             ResultSet resultSet = statement.executeQuery("SELECT * FROM Games WHERE start=0");

             if (resultSet.next()) {
                 gameId = resultSet.getInt("id");
                 GameOfGraphs.getGame().getGraphController().setGraph(getGraph());

                 statement.executeUpdate("UPDATE Player SET used=1");

                 resultSet = statement.executeQuery("SELECT * FROM Player WHERE player='" + player + "'");
                 resultSet.next();
                 playerId = resultSet.getInt("id");

                 host = false;

                 return true;
             }
         } catch (SQLException e) {
             e.printStackTrace();
         }
         return false;
     }

    public static void createGame(Graph g, Player p){
        host = true;

        Statement statement = setup();

        String graph = null;
        String player = null;

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        try {
            graph = mapper.writeValueAsString(g);
            player = mapper.writeValueAsString(p);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        try {
            statement.executeUpdate("INSERT INTO Games (map, turn, start) VALUES ('"+ graph +"', '" + p.getName() +"', 0)");

            ResultSet resultSet = statement.executeQuery("SELECT  * FROM Games WHERE map='" + graph + "'");
            resultSet.next();

            gameId = resultSet.getInt("id");

            statement.executeUpdate("INSERT INTO Player (player, gameId, ki, used) VALUES ('"+ player +"', " + gameId + ", 0, 0)");

            resultSet = statement.executeQuery("SELECT  * FROM Player WHERE player='" + player + "'");
            resultSet.next();

            playerId = resultSet.getInt("id");

            nextTurn(p.getName());

            ArrayList<Player> players = new ArrayList<>();
            players.add(p);

            boolean add = true;

            ArrayList<Vertex> vertices = g.getVertices();
            for (Vertex v:vertices){
                if (v.getField().getPlayer() instanceof KIFraction) {
                    for (Player temp : players) {
                        if (v.getField().getPlayer().getName().equals(temp.getName())) {
                            add = false;
                            break;
                        }
                    }

                    if (add){
                        players.add(v.getField().getPlayer());

                        try {
                            String tempP = mapper.writeValueAsString(v.getField().getPlayer());

                            statement.executeUpdate("INSERT INTO Player (player, gameId, ki, used) VALUES ('"+ tempP +"', " + gameId + "1, 1)");
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }
                    }
                }
                add = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean gameReady(){
        LinkedList<Player> players = GameOfGraphs.getGame().getPlayers();
        int countPlayer = 0;

        Statement statement = setup();

        try {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Player WHERE gameId=" + gameId);

            while (resultSet.next()){
                countPlayer++;
            }

            if (players.size() == countPlayer){
                statement.executeUpdate("UPDATE Games SET start=1 WHERE id=" + gameId);

                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean gameStarted(){
        Statement statement = setup();

        try {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Games WHERE id=" + gameId);

            resultSet.next();
            return resultSet.getBoolean("start");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Graph getGraph(){
        Statement statement = setup();

        try {
            ResultSet resultSet = statement.executeQuery("SELECT  * FROM Games WHERE id=" + gameId);

            if (resultSet.next()) {
                String graph = resultSet.getString("map");

                ObjectMapper mapper = new ObjectMapper();
                try {
                    Graph g = mapper.readValue(graph, Graph.class);

                    return g;
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static ArrayList<Player> unusedPlayers(){
        Statement statement = setup();

        try {
            ResultSet resultSet = statement.executeQuery("SELECT  * FROM Player WHERE used=0");

            ArrayList<Player> players = new ArrayList<>();
            if (resultSet.next()) {
                ObjectMapper mapper = new ObjectMapper();
                try {
                    players.add(mapper.readValue(resultSet.getString("player"), Player.class));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return players;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void nextTurn(String name){
        Statement statement = setup();

        try {
            statement.executeUpdate("UPDATE Games SET turn='" + name + "' WHERE id=" + gameId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean isHost() {
        if (gameId == -1){
            Statement statement = setup();

            try {
                ResultSet resultSet = statement.executeQuery("SELECT * FROM Games WHERE start=0");

                gameId = resultSet.getInt("id");

                if (resultSet.next()) {
                    host = false;
                }else {
                    host = true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        }
        return host;

    }
}
