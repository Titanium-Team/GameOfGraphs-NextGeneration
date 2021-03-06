package field;

import field.buildings.Building;
import field.buildings.Buildings;
import field.resource.Resource;
import field.resource.Resources;
import game.Player;
import graph.Vertex;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by 204g03 on 10.06.2016.
 */
public class FieldController {

    private final static Random random = new Random();

    /**
     * Erstellt ein Field, nach gewünschten Eigenschaften
     * @param player Spieler dem das Feld gehört.
     * @param start Entscheidet ob Starttruppen sich auf dem Feld befinden.
     * @return
     */
    public Field createField(Player player, boolean start){

        int fertility = random.nextInt(5)+1;
        int mountains = random.nextInt(5)+1;
        while(fertility + mountains > 5){
            fertility = random.nextInt(5)+1;
            mountains = random.nextInt(5)+1;
        }
        int forestType = random.nextInt(3)+1;

        Resource[] resources = Resources.getSpecialResources();

        Resource resource = resources[random.nextInt(resources.length)];

        while ((resource.isMineral() && fertility > mountains) || (!resource.isMineral() && fertility < mountains)){
            resource = resources[random.nextInt(resources.length)];
        }

        Field field = new Field(fertility, mountains, player, forestType, (Resources) resource, start);
        field.getResources().put(Resources.POPULATION,random.nextInt(2)+2);

        return field;
    }

    public void run(Player player){

        List<Vertex> vertices = player.getFields();
        for(Vertex vertex : vertices) {

            Map<Buildings, Integer> buildings = vertex.getField().getBuildings();

            for(Map.Entry<Buildings, Integer> entry : buildings.entrySet()){

                if(entry.getValue() > 0) {
                    entry.getKey().production(vertex.getField());
                }
            }

            if(vertex.getField().getResources().get(Resources.POPULATION) > 1) {
                vertex.getField().getResources().put(Resources.FOOD, vertex.getField().getResources().get(Resources.FOOD) + 1 - vertex.getField().getResources().get(Resources.POPULATION ));
            }
            if (vertex.getField().getResources().get(Resources.FOOD) > 9) {
                vertex.getField().getResources().put(Resources.POPULATION, vertex.getField().getResources().get(Resources.POPULATION) + 1);
                vertex.getField().getResources().put(Resources.FOOD, vertex.getField().getResources().get(Resources.FOOD) - 10);
            } else if (vertex.getField().getResources().get(Resources.FOOD) < -9) {
                vertex.getField().getResources().put(Resources.POPULATION, vertex.getField().getResources().get(Resources.POPULATION) - 1);
                vertex.getField().getResources().put(Resources.FOOD, 0);
            }


            vertex.getField().getResources().put(Resources.WOOD, vertex.getField().getResources().get(Resources.WOOD) + random.nextInt(vertex.getField().getForestType() + 1));

        }

    }
}
