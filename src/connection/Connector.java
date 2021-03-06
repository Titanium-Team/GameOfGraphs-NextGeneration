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


    private static int gameId=-1;

    private static int playerId;
    private static Player myPlayer;

    private static boolean host;

    private static boolean enabledMutiplayer = false;

    private static Connection connection = null;

    private static Statement setup(){
        try {

            if(connection == null) {
                connection = DriverManager.getConnection(url, user, password);
            }

            Statement statement = connection.createStatement();

            return statement;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

     public static boolean joinGame(Player p){
         myPlayer = p;
         Statement statement = setup();

         String player = null;
         ObjectMapper mapper = new ObjectMapper();
         mapper.configure(SerializationFeature.INDENT_OUTPUT, true);

         try {
             player = mapper.writeValueAsString(p);
         } catch (JsonProcessingException e) {
             e.printStackTrace();
         }

         try {
             ResultSet resultSet = statement.executeQuery("SELECT * FROM Games WHERE start=0");

             if (resultSet.next()) {
                 gameId = resultSet.getInt("id");
                 GameOfGraphs.getGame().getGraphController().setGraph(getGraph(), true);

                 statement.executeUpdate("UPDATE Player SET used=1 WHERE  player='" + player + "'");

                 resultSet = statement.executeQuery("SELECT * FROM Player WHERE player='" + player + "'");
                 resultSet.first();
                 playerId = resultSet.getInt("id");

                 host = false;

                 return true;
             }
         } catch (SQLException e) {
             e.printStackTrace();
         }
         return false;
     }

    public static void createGame(Graph g){

        if(g == null) {
            return;
        }

        host = true;

        Statement statement = setup();

        String graph = null;

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        try {
            graph = mapper.writeValueAsString(g);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        try {
            statement.executeUpdate("INSERT INTO Games (map, start) VALUES ('"+ graph +"', 0)");

            ResultSet resultSet = statement.executeQuery("SELECT  * FROM Games WHERE map='" + graph + "'");
            resultSet.next();

            gameId = resultSet.getInt("id");

            LinkedList<Player> players = GameOfGraphs.getGame().getPlayers();
            for (Player p:players){
                String tempP = mapper.writeValueAsString(p);
                int ki = p instanceof KIFraction ? 1 : 0;
                statement.executeUpdate("INSERT INTO Player (player, gameId, ki, used) VALUES ('"+ tempP +"', " + gameId + ", " + ki + ", " + ki + ")");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public static int gameReady(){
        LinkedList<Player> players = GameOfGraphs.getGame().getPlayers();
        int countPlayer = 0;

        Statement statement = setup();

        try {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Player WHERE gameId=" + gameId + " AND used=1");

            while (resultSet.next()){
                countPlayer++;
            }

            if (players.size() == countPlayer){
                statement.executeUpdate("UPDATE Games SET start=1 WHERE id=" + gameId);
            }

            return (players.size() == 0 ? -1 : players.size())-countPlayer;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public static boolean gameStarted(){
        Statement statement = setup();

        try {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Games WHERE id=" + gameId);


            if (resultSet.first()) {
                return resultSet.getBoolean("start");
            }
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
            while (resultSet.next()) {
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

    public static void nextTurn(String name, Graph g){
        String graph = null;

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        try {
            graph = mapper.writeValueAsString(g);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        Statement statement = setup();

        try {
            statement.executeUpdate("UPDATE Games SET turn='" + name + "', map='" + graph + "' , newTurn=1 WHERE id=" + gameId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void nextTurn(String name){
        Statement statement = setup();

        try {
            statement.executeUpdate("UPDATE Games SET turn='" + name +"' , newTurn=1 WHERE id=" + gameId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean isHost() {
        if (gameId == -1){
            Statement statement = setup();

            try {
                ResultSet resultSet = statement.executeQuery("SELECT * FROM Games WHERE start=0");

                if (resultSet.next()){
                    gameId = resultSet.getInt("id");
                    return host = false;
                }else {
                    return host = true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        }
        return host;

    }

    public static String getCurrentPlayer(){
        Statement statement = setup();

        try {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Games WHERE id=" + gameId);

            resultSet.next();

            return resultSet.getString("turn");

        } catch (SQLException e) {
        }

        return null;
    }

    public static boolean getNewCurrentPlayer(){
        Statement statement = setup();

        try {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Games WHERE id=" + gameId);

            resultSet.next();

            return resultSet.getBoolean("newTurn");

        } catch (SQLException e) {
        }

        return false;
    }

    public static void setNewCurrentPlayer(boolean newCurrentPlayer){
        Statement statement = setup();

        try {
            statement.executeUpdate("UPDATE Games SET newTurn=" + newCurrentPlayer +" WHERE id=" + gameId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteGame(){
        Statement statement = setup();

        try {
            statement.executeUpdate("DELETE FROM Games WHERE id=" + gameId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void setActive(Player p, boolean active){
        Statement statement = setup();

        String player = null;
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);

        try {
            player = mapper.writeValueAsString(p);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        try {
            statement.executeUpdate("UPDATE Player SET active=" + active + " WHERE  player='" + player + "'");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean isActive(Player p){
        Statement statement = setup();

        String player = null;
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);

        try {
            player = mapper.writeValueAsString(p);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        try {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Player WHERE player='" + player + "'");

            if (resultSet.next()) {
                return resultSet.getBoolean("active");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isEnabledMutiplayer() {
        return enabledMutiplayer;
    }

    public static void setEnabledMutiplayer(boolean enabledMutiplayer) {
        Connector.enabledMutiplayer = enabledMutiplayer;
    }

    public static Player getMyPlayer() {
        return myPlayer;
    }

    public static void setHost(boolean host) {
        Connector.host = host;
    }
}
