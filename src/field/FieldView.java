package field;

import connection.Connector;
import de.SweetCode.e.E;
import de.SweetCode.e.input.InputEntry;
import de.SweetCode.e.math.ILocation;
import de.SweetCode.e.rendering.GameScene;
import de.SweetCode.e.rendering.layers.Layers;
import field.buildings.Building;
import field.buildings.Buildings;
import field.recipe.RecipeResource;
import field.resource.Resource;
import field.resource.Resources;
import game.GameOfGraphs;
import game.Player;
import game.sprite.Textures;
import game.ui.Button;
import game.ui.DropDownMenu;
import graph.Graph;
import graph.GraphDrawer;
import graph.Vertex;
import ki.KIFraction;
import ki.RequestView;
import ki.TradeView;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static game.GameOfGraphs.getGame;

/**
 * Created by boeschemeier on 08.06.2016.
 */
public class FieldView extends GameScene{

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private int maxWidthOfResourceName = -1;
    private int maxWidthOfBuildingName = -1;

    //Aktueller Vertex und Field
    private Vertex currentVertex = null;
    private Field currentField = null;

	//boolean für Handel
	private boolean tradeEnabled=false;
	private TradeView tradeView = new TradeView(this, new ILocation(200, 100));

    //Boolean zum Überprüfen, ob gerade Truppen bewegt werden
    private boolean move = false;
    //Boolean bei FreeBuild
    private boolean free = true;
    private List<Vertex> marked = new ArrayList<>();
    //Ein Menu mit Auswahl aller Buildings
    private DropDownMenu<Buildings> buildingDropDownMenu = new DropDownMenu<Buildings>(this, new ILocation(440, 510), new LinkedList<Buildings>() {{
        for( Buildings building : Buildings.values()){
            this.add(building);
        }
    }}, (t, value) -> {});

    //Ein Menu zur Auswahl der Anzahl an Truppen
    private DropDownMenu<Integer> unitDropDownMenu = new DropDownMenu<Integer>(this, new ILocation(220, 530), new LinkedList<Integer>() {{
    }}, (t, value) -> {});

    //Button zum bauen von Gebäuden
    private Button<String> buildButton = new Button<String>(this, "Build", new ILocation(540, 510),(t, value) -> {

        if( Buildings.isBuildable(buildingDropDownMenu.getOption(), this.currentField)){
            Buildings.build(buildingDropDownMenu.getOption(), this.currentField, false);
        }else {
            JOptionPane.showMessageDialog(null, "You can't build this, because: \n" +
                    "-you need at least 1 citizen, or \n" +
                    "-you don't have enough resources, or \n" +
                    "-you have reached the maxmium amount of this type of buildings.");
        }
    });

    //Button zum einmaligen "frei" bauen im ersten Zug
    private Button<String> freeBuildButton = new Button<String>(this, "FreeBuild", new ILocation(580, 510),(t, value) -> {

        Buildings.build(buildingDropDownMenu.getOption(), this.currentField, true);
        this.free = false;

        // Update selectedable units
        if(buildingDropDownMenu.getOption() == Buildings.UNIT) {
            this.unitDropDownMenu.getOptions().clear();
            this.unitDropDownMenu.setOptions(new LinkedList<Integer>() {{

                for (int i = 0; i <= currentField.getUnmovedUnits().size(); i++) {
                    this.add(i);
                }

            }});
        }

    });

    //Handelsbutton, welche freigeschaltet werden, nachdem man das zugehörige Gebäude gebaut hat.
    private Button<String> slaveMarketButton = new Button<String>(this, "Trade", new ILocation(830, 510),(t, value) -> {

        if(this.currentField.getResources().get(Resources.IRON) > 1){
            this.currentField.getResources().put(Resources.IRON, this.currentField.getResources().get(Resources.IRON) -2);
            this.currentField.getResources().put(Resources.POPULATION, this.currentField.getResources().get(Resources.POPULATION) +1);
        } else {
            JOptionPane.showMessageDialog(null, "Trade not possible.");
        }

    });
    private DropDownMenu<Resources> resourceDropDownMenu = new DropDownMenu<Resources>(this, new ILocation(780, 540), new LinkedList<Resources>() {{
        for( Resources resource : Resources.values()){
            if(resource != Resources.POPULATION && resource != Resources.GOLD) {
                this.add(resource);
            }
        }
    }}, (t, value) -> {});
    private Button<String> marketPlaceButton = new Button<String>(this, "Trade", new ILocation(830, 540),(t, value) -> {

        if(this.currentField.getResources().get(Resources.GOLD) > 0){
            this.currentField.getResources().put(Resources.IRON, this.currentField.getResources().get(Resources.IRON) -1);
            this.currentField.getResources().put(resourceDropDownMenu.getOption(),this.currentField.getResources().get(resourceDropDownMenu.getOption()) +1);

        } else {
            JOptionPane.showMessageDialog(null, "Trade not possible.");

        }

    });


    private Button<String> questionButton = new Button<>(this, "?", new ILocation(1235, 10), (ui, value) -> {

        JOptionPane.showMessageDialog(null, null, "Help", 0, new ImageIcon(Textures.HELP_POPUP.getImage()));

    });

    private Button<String> allianceButton = new Button<String>(this,"Request Alliance",new ILocation(900,570),(t,value)->{
        getGame().getCurrentPlayer().requestAlliance(currentField.getPlayer());
    });

	private Button<String> tradeButton = new Button<String>(this,"Trade with Player",new ILocation(830,630),(t, value)->{
		tradeEnabled=true;
		tradeView.setCurrentVertex(currentVertex);
        tradeView.enableAll();
	});

	private boolean requestShown;
	private RequestView requestView = new RequestView(this,new ILocation(400,200));

	private Button<String> requestButton = new Button<String>(this,"Show Requests",new ILocation(830,660),(t, value)->{
		requestShown=true;
		requestView.setRequest(currentField.getPlayer().getRequests().front());
	});

    private Button<String> bazaarButton1 = new Button<String>(this, "Trade", new ILocation(830, 570),(t, value) -> {

        if(this.currentField.getResources().get(Resources.FOOD) > 1){
            this.currentField.getResources().put(Resources.FOOD, this.currentField.getResources().get(Resources.FOOD) -2);
            this.currentField.getResources().put(Resources.STONE,this.currentField.getResources().get(Resources.STONE) +1);

        } else {
            JOptionPane.showMessageDialog(null, "Trade not possible.");

        }

    });
    private Button<String> bazaarButton2 = new Button<String>(this, "Trade", new ILocation(830, 600),(t, value) -> {

        if(this.currentField.getResources().get(Resources.FOOD) > 3){
            this.currentField.getResources().put(Resources.FOOD, this.currentField.getResources().get(Resources.FOOD) -4);
            this.currentField.getResources().put(Resources.IRON,this.currentField.getResources().get(Resources.IRON) +1);

        } else {
            JOptionPane.showMessageDialog(null, "Trade not possible.");

        }

    });


    //Button zum einleiten des nächsten Zuges
    private Button<String> nextTurnButton = new Button<String>(this, "Next Turn", new ILocation(1200, 700),(t, value) -> {
        currentVertex.setMarkStart(false);
        this.currentField = null;
        this.currentVertex = null;
        getGame().nextTurn();
        if (getGame().isFirstTurn()) {
            this.free = true;
        }
    });


    public FieldView(){
        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (Connector.isEnabledMutiplayer()){
                    String player = Connector.getCurrentPlayer();

                    if (!GameOfGraphs.getGame().getCurrentPlayer().getName().equals(player)){
                        for (Player p:GameOfGraphs.getGame().getPlayers()){
                            if (p.getName().equals(player)){
                                GameOfGraphs.getGame().setCurrentPlayer(p);
                                GameOfGraphs.getGame().getGraphController().setGraph(Connector.getGraph(), false);
                            }
                        }
                    }
                }
            }
        },0,500, TimeUnit.MILLISECONDS);

        E.getE().addComponent(unitDropDownMenu);
        //Build
        E.getE().addComponent(buildingDropDownMenu);
        E.getE().addComponent(buildButton);
        E.getE().addComponent(freeBuildButton);
        //Trade
        E.getE().addComponent(slaveMarketButton);
        E.getE().addComponent(marketPlaceButton);
        E.getE().addComponent(resourceDropDownMenu);
        E.getE().addComponent(bazaarButton1);
        E.getE().addComponent(bazaarButton2);
	    //Alliance
	    E.getE().addComponent(allianceButton);


        E.getE().addComponent(nextTurnButton);

        E.getE().addComponent(this.questionButton);

	    E.getE().addComponent(tradeButton);
	    E.getE().addComponent(tradeView);

	    E.getE().addComponent(requestButton);
	    E.getE().addComponent(requestView);

    }

    @Override
    public void render(Layers layers) {

        Graph graph = getGame().getGraphController().getGraph();
        Graphics2D g = layers.first().getGraphics2D();

        if(this.maxWidthOfResourceName == -1) {
            for(Resource resource : Resources.values()) this.maxWidthOfResourceName = Math.max(this.maxWidthOfResourceName, g.getFontMetrics().stringWidth(resource.getName() + ":"));
            for(Building building : Buildings.values()) this.maxWidthOfBuildingName = Math.max(this.maxWidthOfBuildingName, g.getFontMetrics().stringWidth(building.getName() + ":"));
        }

        //Zeichnen des Graphen
        GraphDrawer.drawer(g,graph,"Field");

        g.drawString("Player: " + getGame().getCurrentPlayer().getName(), 50, 50);

        g.setColor(Color.DARK_GRAY);
        g.fillRect(0,500,1280,220);
        g.setColor(Color.BLACK);
        g.drawLine(0,500,1280,500);
        g.drawLine(420, 500, 420, 720);
        g.drawLine(700, 500, 700, 720);
        g.setBackground(Color.WHITE);

	    tradeView.setEnabled(tradeEnabled);
	    requestView.setEnabled(requestShown);

        if(currentField == null) {

            g.drawString("No field selected.", 520, 600);

            this.buildingDropDownMenu.setEnabled(false);
            this.buildButton.setEnabled(false);
            this.unitDropDownMenu.setEnabled(false);
            this.nextTurnButton.setEnabled(false);
            this.marketPlaceButton.setEnabled(false);
            this.resourceDropDownMenu.setEnabled(false);
            this.slaveMarketButton.setEnabled(false);
            this.freeBuildButton.setEnabled(false);
            this.bazaarButton1.setEnabled(false);
            this.bazaarButton2.setEnabled(false);
	        this.allianceButton.setEnabled(false);
	        this.tradeButton.setEnabled(false);
	        this.requestButton.setEnabled(false);

        }else{
            //Zeichnen der Statistiken
            g.setColor(Color.ORANGE);
            g.drawString(String.valueOf("FERTILITY: " + currentField.getFertility()), 20, 520);
            g.setColor(Color.LIGHT_GRAY);
            g.drawString(String.valueOf("MOUNTAIN: " + currentField.getMountains()), 120, 520);
            g.setColor(Color.RED);
            g.drawString("UNITS: " + currentField.getUnits().size() + " (" + currentField.getUnmovedUnits().size() + ")", 220, 520);
            g.setColor(Color.BLUE);
            g.drawString(String.valueOf("SPECIAL: ") + currentField.getLocalResource().getName(), 320, 520);
            g.setColor(Color.GREEN);
            g.drawString(String.valueOf("FOREST: ") + currentField.getForestType(), 320, 540);
            g.setColor(Color.BLACK);
            g.drawString("OWNER: " + this.currentField.getPlayer().getName(), 20, 700);

            g.drawString("ID:" + this.currentVertex.getID(), 1000, 20);

            g.setColor(Color.LIGHT_GRAY);

            //Zeichnen der UIComponents
            boolean active = getGame().getCurrentPlayer().getName().equals(this.currentField.getPlayer().getName());
            this.buildingDropDownMenu.setEnabled(active);
            this.buildButton.setEnabled(active);
            this.unitDropDownMenu.setEnabled(active);
            this.nextTurnButton.setEnabled(active);
            this.marketPlaceButton.setEnabled(false);
            this.resourceDropDownMenu.setEnabled(false);
            this.slaveMarketButton.setEnabled(false);
            this.bazaarButton1.setEnabled(false);
            this.bazaarButton2.setEnabled(false);
            this.freeBuildButton.setEnabled(active && getGame().isFirstTurn() && this.free);
	        this.allianceButton.setEnabled(!active);
	        this.tradeButton.setEnabled(active);
	        this.requestButton.setEnabled(active && !currentField.getPlayer().getRequests().isEmpty());

            //Zeichnen der Trade-Button
            if(this.currentField.getBuildings().get(Buildings.SLAVE_MARKET) > 0) {
                this.slaveMarketButton.setEnabled(true);
                g.drawString("2x Iron → 1 Person", 720, 525);
            }

            if(this.currentField.getBuildings().get(Buildings.MARKETPLACE) > 0) {
                this.marketPlaceButton.setEnabled(true);
                this.resourceDropDownMenu.setEnabled(true);
                g.drawString("1x Gold → ", 720, 555);
            }

            if(this.currentField.getBuildings().get(Buildings.BAZAAR) > 0) {
                this.bazaarButton1.setEnabled(true);
                this.bazaarButton2.setEnabled(true);
                g.drawString("2x Food → 1 Stone", 720, 585);
                g.drawString("4x Food → 1 Iron ", 720, 615);
            }

            g.setColor(Color.BLACK);

            //Zeichnen der Resourcen und Gebäude
            Map<Resources, Integer> resources = currentField.getResources();
            Map<Buildings, Integer> buildings = currentField.getBuildings();
            List<RecipeResource> recipeList = buildingDropDownMenu.getOption().getRecipe().getItemIngredients();

            final int[] y = {0};
            resources.forEach((resource, amount) -> {

                g.drawString(resource.getName() + ":", 20, 540 + y[0] * 20);
                g.drawString(String.valueOf(amount), this.maxWidthOfResourceName + 25, 540 + y[0] * 20);

                for (RecipeResource recipe : recipeList){

                    if(recipe.getResource() == resource){

                        if(recipe.getAmount() <= amount){
                            g.setColor(Color.GREEN);
                        } else {
                            g.setColor(Color.RED);
                        }
                        g.drawString(" -" + recipe.getAmount(), 85, 540 + y[0] * 20);
                        g.setColor(Color.BLACK);
                    }

                }

                y[0]++;
            });


            y[0] = 0;
            buildings.forEach((building, amount) -> {
                g.drawString(building.getName() + ":", 120, 540 + y[0] * 20);
                g.drawString(String.valueOf(amount), this.maxWidthOfBuildingName + 125, 540 + y[0] * 20);
                y[0]++;
            });
        }
    }

    @Override
    public void update(InputEntry inputEntry, long l) {

        GraphDrawer.update(inputEntry,l);
        Graph graph = getGame().getGraphController().getGraph();

        if(GraphDrawer.getHorizontal() == null) {
            return;
        }

        //MouseListener
        inputEntry.getMouseEntries().forEach(entry -> {
            
            if (entry.getPoint().getY() <= 475 && entry.getPoint().getX() <= 1255 && entry.getButton() == 1 && move != true) {
                Vertex vertex = graph.getVertex((int) entry.getPoint().getX(), (int) entry.getPoint().getY());
                if (vertex != null && (vertex.getField().getPlayer() instanceof KIFraction || getGame().getCurrentPlayer().getName().equals(vertex.getField().getPlayer().getName()))) {
                    if(currentVertex != null) {
                        this.currentVertex.setMarkTarget(false);
                    }
                    this.currentVertex = vertex;
                    this.currentVertex.setMarkStart(true);
                    this.currentField = this.currentVertex.getField();
                    this.unitDropDownMenu.setOptions(new LinkedList<Integer>() {{

                        for (int i = 0; i <= currentField.getUnmovedUnits().size(); i++) {
                            this.add(i);
                        }

                    }});
                } else if(!tradeEnabled) {
                    if(currentVertex != null) {
                        this.currentVertex.setMarkTarget(false);
                    }
                    this.currentVertex = null;
                    this.currentField = null;
                }
            } else if(move == true){

                Vertex vertex = graph.getVertex((int) entry.getPoint().getX() + GraphDrawer.getHorizontal().getValue(), (int) entry.getPoint().getY() + GraphDrawer.getVertical().getValue());
                if(vertex != null) {
                    if (vertex.isMarkTarget()) {
                        getGame().getSimulationController().moveUnits(this.currentVertex, vertex, this.unitDropDownMenu.getOption());
                    }
                }
                this.unitDropDownMenu.setSelectedIndex(0);

            }
        });

        // Movement
        if (this.currentField != null && this.currentVertex != null){
            if (this.unitDropDownMenu.getOption() > 0) {
                marked = getGame().getSimulationController().showMovementPossibilities(this.currentVertex);
                move = true;
            } else {
                if(move == true){
                    for (Vertex vertex : marked){
                        vertex.setMarkTarget(false);
                    }
                }
                move = false;
            }
        }


    }

    @Override
    public boolean isActive() {
        return (E.getE().getScreen().getCurrent() == this);
    }

	public void setTradeEnabled(boolean tradeEnabled) {
		this.tradeEnabled = tradeEnabled;
	}

	public void setRequestShown(boolean requestShown) {
		this.requestShown = requestShown;
	}
}
