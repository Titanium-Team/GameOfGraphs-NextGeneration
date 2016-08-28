package graph;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import game.GameOfGraphs;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * <p>
 * Materialien zu den zentralen NRW-Abiturpruefungen im Fach Informatik ab 2018
 * </p>
 * <p>
 * Klasse Graph
 * </p>
 * <p>
 * Die Klasse Graph stellt einen ungerichteten, kantengewichteten Graphen dar. Es koennen 
 * Knoten- und Kantenobjekte hinzugefuegt und entfernt, flache Kopien der Knoten- und Kantenlisten 
 * des Graphen angefragt und Markierungen von Knoten und Kanten gesetzt und ueberprueft werden.
 * Des Weiteren kann eine Liste der Nachbarn eines bestimmten Knoten, eine Liste der inzidenten 
 * Kanten eines bestimmten Knoten und die Kante von einem bestimmten Knoten zu einem 
 * anderen bestimmten Knoten angefragt werden. Abgesehen davon kann abgefragt werden, welches 
 * Knotenobjekt zu einer bestimmten ID gehoert und ob der Graph leer ist.
 * </p>
 * 
 * @author Qualitaets- und UnterstuetzungsAgentur - Landesinstitut fuer Schule, Materialien zum schulinternen Lehrplan Informatik SII
 * @version Oktober 2015
 */
public class Graph{

  private ArrayList<Vertex> vertices;
  private ArrayList<Edge> edges;

    private int radius;
    private int thickness;
    private int width, height;

    private boolean checked = false;

    @JsonIgnore
    private BufferedImage background;
    private boolean backgroundTextured = false;

    @JsonIgnore
    private BufferedImage vertexImage;
    private boolean vertexImageTextured = false;

    @JsonIgnore
    private BufferedImage edgeImage;
    private boolean edgeImageTextured = false;

  /**
   * Ein Objekt vom Typ Graph wird erstellt. Der von diesem Objekt 
   * repraesentierte Graph ist leer.
   */

  @JsonCreator
  public Graph(){
    //Leere Listen fuer Knoten und Kanten erstellen.
    vertices = new ArrayList<Vertex>();
    edges = new ArrayList<Edge>();
      this.radius = 25;
      this.thickness = 5;
      this.width = 1280;
      this.height = 540;

      background = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
      background.setRGB(0, 0, Color.WHITE.getRGB());

      vertexImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
      vertexImage.setRGB(0, 0, Color.BLACK.getRGB());

      edgeImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
      edgeImage.setRGB(0, 0, Color.BLUE.getRGB());
  }

  /**
   * Die Anfrage liefert eine neue Liste aller Knotenobjekte vom Typ graph.List<Vertex>.
   */
  public ArrayList<Vertex> getVertices(){
    return (ArrayList<Vertex>) vertices.clone();
  }

  /**
   * Die Anfrage liefert eine neue Liste aller Kantenobjekte vom Typ graph.List<Edge>.
   */
  public ArrayList<Edge> getEdges(){
    return (ArrayList<Edge>) edges.clone();
  }

  /**
   * Die Anfrage liefert das Knotenobjekt mit pID als ID. Ist ein solchen Knotenobjekt nicht im Graphen enthalten,
   * wird null zurueckgeliefert.
   */
  public Vertex getVertex(String pID){
    //Vertex-Objekt mit pID als ID suchen.
    Vertex result = null;
   for (Vertex vertex:vertices){
      if (vertex.getID().equals(pID)){
          result = vertex;
          break;
      }
    }
    //Objekt zurueckliefern.
    return result;
  }

  /**
   * Der Auftrag fuegt den Knoten pVertex in den Graphen ein, sofern es noch keinen
   * Knoten mit demselben ID-Eintrag wie pVertex im Graphen gibt und pVertex eine ID ungleich null hat. 
   * Ansonsten passiert nichts.
   */
  public boolean addVertex(Vertex pVertex){
    //Pruefen, ob der Vertex existiert und eine ID hat.
    if (pVertex != null && pVertex.getID() != null) {
      //Pruefen, ob nicht schon ein Vertex mit gleicher ID enthalten ist.
      boolean freeID = true;
      for (Vertex vertex:vertices){
        if (vertex.getID().equals(pVertex.getID())){
            return false;
        }
      }

      //Wenn die ID noch frei ist, den Vertex einfuegen, sonst nichts tun.
      if (freeID) {
          Vertex vertex = getVertex(pVertex.getX(), pVertex.getY(), radius*3);
          if (vertex==null){
              vertices.add(pVertex);
              return true;
          }
      }
    }
    return false;
  }

  /**
   * Der Auftrag fuegt die Kante pEdge in den Graphen ein, sofern beide durch die Kante verbundenen Knoten
   * im Graphen enthalten sind, nicht identisch sind und noch keine Kante zwischen den Knoten existiert. Ansonsten passiert nichts.
   */
  public void addEdge(Edge pEdge){ 
    //Pruefen, ob pEdge exisitert.
    if (pEdge != null){  
      Vertex[] vertexPair = pEdge.getVerticesId(this);
      
      //Einfuegekriterien pruefen.
      if (vertexPair[0] != null && vertexPair[1] != null && 
      this.getVertex(vertexPair[0].getID()) == vertexPair[0] && 
      this.getVertex(vertexPair[1].getID()) == vertexPair[1] &&
      this.getEdge(vertexPair[0], vertexPair[1]) == null && 
      vertexPair[0] != vertexPair[1]){
        //Kante einfuegen.
        edges.add(pEdge);
      }
    }
  }

  /**
   * Der Auftrag entfernt den Knoten pVertex aus dem Graphen und loescht alle Kanten, die mit ihm inzident sind.
   * Ist der Knoten pVertex nicht im Graphen enthalten, passiert nichts.
   */
  public void removeVertex(Vertex pVertex){
    //Inzidente Kanten entfernen.
    for (Edge edge:getEdges()){
      Vertex[] akt = edge.getVerticesId(this);
      if (akt[0] == pVertex || akt[1] == pVertex){
        edges.remove(edge);
      }
    }

    //Knoten entfernen
    vertices.remove(pVertex);
  }

  /**
   * Der Auftrag entfernt die Kante pEdge aus dem Graphen. Ist die Kante pEdge nicht 
   * im Graphen enthalten, passiert nichts.
   */
  public void removeEdge(Edge pEdge){
    //Kante aus Kantenliste des Graphen entfernen.
    edges.remove(pEdge);
  }

  /**
   * Der Auftrag setzt die Markierungen aller Kanten des Graphen auf pMark.
   */
  public void setAllEdgeMarks(boolean pMark){
    for (Edge edge:edges){
      edge.setMark(pMark);
    }
  }

    public void setAllVertexStartMark(boolean pMark){
        for (Vertex vertex:vertices){
            vertex.setMarkStart(pMark);
        }
    }

    public void setAllVertexTargetMark(boolean pMark){
        for (Vertex vertex:vertices){
            vertex.setMarkTarget(pMark);
        }
    }

    public void setAllVertexMark(boolean pMark){
        for (Vertex vertex:vertices){
            vertex.setMark(pMark);
        }
    }

  /**
   * Die Anfrage liefert true, wenn alle Kanten des Graphen mit true markiert sind, ansonsten false.
   */
  public boolean allEdgesMarked(boolean pMark){
    boolean result = true;
    for (Edge edge:edges){
      if (edge.isMark() != pMark){
        result = false;
      }
    }
    return result;
  }

    public boolean allVertexStartMark(boolean pMark){
        boolean result = true;
        for (Vertex vertex:vertices){
            if (vertex.isMarkStart() != pMark){
                result = false;
            }
        }
        return result;
    }

    public boolean allVertexTargetMark(boolean pMark){
        boolean result = true;
        for (Vertex vertex:vertices){
            if (vertex.isMarkTarget() != pMark){
                result = false;
            }
        }
        return result;
    }

    public boolean allVertexMark(boolean pMark){
        boolean result = true;
        for (Vertex vertex:vertices){
            if (vertex.isMark() != pMark){
                result = false;
            }
        }
        return result;
    }

  /**
   * Die Anfrage liefert alle Nachbarn des Knotens pVertex als neue Liste vom Typ graph.List<Vertex>. Hat der Knoten
   * pVertex keine Nachbarn in diesem Graphen oder ist gar nicht in diesem Graphen enthalten, so 
   * wird eine leere Liste zurueckgeliefert.
   */
  public ArrayList<Vertex> getNeighbours(Vertex pVertex){
    ArrayList<Vertex> result = new ArrayList<>();
    
    //Alle Kanten durchlaufen.
    for (Edge edge:edges){
      
      //Wenn ein Knoten der Kante pVertex ist, den anderen als Nachbarn in die Ergebnisliste einfuegen.
      Vertex[] vertexPair = edge.getVerticesId(this);
      if (vertexPair[0] == pVertex) {
        result.add(vertexPair[1]);
      } else { 
        if (vertexPair[1] == pVertex){
          result.add(vertexPair[0]);
        }
      }
    }    
    return result;
  }

  /**
   * Die Anfrage liefert eine neue Liste alle inzidenten Kanten zum Knoten pVertex. Hat der Knoten
   * pVertex keine inzidenten Kanten in diesem Graphen oder ist gar nicht in diesem Graphen enthalten, so 
   * wird eine leere Liste zurueckgeliefert.
   */
  public ArrayList<Edge> getEdges(Vertex pVertex){
    ArrayList<Edge> result = new ArrayList<Edge>();
    
    //Alle Kanten durchlaufen.
    for (Edge edge:edges){
      
      //Wenn ein Knoten der Kante pVertex ist, dann Kante als inzidente Kante in die Ergebnisliste einfuegen.
      Vertex[] vertexPair = edge.getVerticesId(this);
      if (vertexPair[0] == pVertex) {
        result.add(edge);
      } else{
        if (vertexPair[1] == pVertex){
          result.add(edge);
        }
      }
    }    
    return result;
  }

  /**
   * Die Anfrage liefert die Kante, welche die Knoten pVertex und pAnotherVertex verbindet, 
   * als Objekt vom Typ Edge. Ist der Knoten pVertex oder der Knoten pAnotherVertex nicht 
   * im Graphen enthalten oder gibt es keine Kante, die beide Knoten verbindet, so wird null 
   * zurueckgeliefert.
   */
  public Edge getEdge(Vertex pVertex, Vertex pAnotherVertex){
    Edge result = null;
    
    //Kanten durchsuchen, solange keine passende gefunden wurde.
    for (Edge edge:edges){
      
      //Pruefen, ob die Kante pVertex und pAnotherVertex verbindet.
      Vertex[] vertexPair = edge.getVerticesId(this);
      if ((vertexPair[0] == pVertex && vertexPair[1] == pAnotherVertex) ||
      (vertexPair[0] == pAnotherVertex && vertexPair[1] == pVertex)) {
        //Kante als Ergebnis merken.
        result = edge;
          break;
      }
    }    
    return result;
  }

  /**
   * Die Anfrage liefert true, wenn der Graph keine Knoten enthaelt, ansonsten false.
   */
  @JsonIgnore
  public boolean isEmpty(){
    return vertices.isEmpty();
  } 

  public Vertex getVertex(int x, int y){
    ArrayList<Vertex> vertexList = getVertices();
    for (Vertex vertex:vertexList){
      Vector vector = new Vector(vertex.getX() - x, vertex.getY() - y);
      if (vector.length() <= getRadius()){
        return vertex;
      }
    }
    return null;
  }

    public Vertex getVertex(int x, int y, int distance){
        ArrayList<Vertex> vertexList = getVertices();
        for (Vertex vertex:vertexList){
            Vector vector = new Vector(vertex.getX() - x, vertex.getY() - y);
            if (vector.length() <= distance){
                return vertex;
            }
        }
        return null;
    }

    public ArrayList<Vertex> getVertexList(Vertex pVertex, int x, int y, int distance){
        ArrayList<Vertex> returnList = new ArrayList<>();
        ArrayList<Vertex> vertexList = getVertices();
        for (Vertex vertex:vertexList){
            if (vertex!=pVertex) {
                Vector vector = new Vector(vertex.getX() - x, vertex.getY() - y);
                if (vector.length() <= distance) {
                    returnList.add(vertex);
                }
            }
        }
        return returnList;
    }

    public Vertex getVertex(Vertex pVertex, int x, int y, int distance){
        double nearest = distance+1;
        Vertex nearestVert = null;
        ArrayList<Vertex> vertexList = getVertices();
        for (Vertex vertex:vertexList){
            if (vertex != pVertex) {
                Vector vector = new Vector(vertex.getX() - x, vertex.getY() - y);
                if (vector.length() <= distance && vector.length() < nearest) {
                    nearest = vector.length();
                    nearestVert = vertex;
                }
            }
        }
        return nearestVert;
    }

    public Edge getEdge(int x, int y, int distance){
        double i = distance;
        Edge edge = null;

      ArrayList<Edge> edgeList = getEdges();
      for (Edge edgeT:edgeList){
          Vertex[] vertices = edgeT.getVerticesId(this);

          Vector vectorA = new Vector(vertices[0].getX(), vertices[0].getY());
          Vector vectorAB = new Vector(vertices[1].getX() - vertices[0].getX(), vertices[1].getY() - vertices[0].getY());
          Vector vectorAC = new Vector(x - vectorA.getX(), y - vectorA.getY());

          double temp = Math.abs((vectorAB.getX() * vectorAC.getY() - vectorAB.getY() * vectorAC.getX()) / vectorAB.length());

          if (temp < i && x >= Math.min(vertices[0].getX(), vertices[1].getX()) - distance && x <= Math.max(vertices[0].getX(), vertices[1].getX()) + distance && y >= Math.min(vertices[0].getY(), vertices[1].getY()) - distance && y <= Math.max(vertices[0].getY(), vertices[1].getY()) + distance){
              i = temp;
              edge = edgeT;
          }

      }
        return edge;
    }

    public void setVertices(ArrayList<Vertex> vertices) {
        this.vertices = vertices;
    }

    public void setEdges(ArrayList<Edge> edges) {
        this.edges = edges;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public BufferedImage getBackground() {
        return background;
    }

    public void setBackground(BufferedImage background) {
        if (background.getRGB(0,0) != vertexImage.getRGB(0,0) && background.getRGB(0,0) != edgeImage.getRGB(0,0)) {
            this.background = background;
        }
    }

    public boolean isBackgroundTextured() {
        return backgroundTextured;
    }

    public void setBackgroundTextured(boolean backgroundTextured) {
        this.backgroundTextured = backgroundTextured;
    }

    public BufferedImage getVertexImage() {
        return vertexImage;
    }

    public void setVertexImage(BufferedImage vertexImage) {
        if (vertexImage.getRGB(0,0) != background.getRGB(0,0) && vertexImage.getRGB(0,0) != edgeImage.getRGB(0,0)) {
            this.vertexImage = vertexImage;
        }
    }

    public boolean isVertexImageTextured() {
        return vertexImageTextured;
    }

    public void setVertexImageTextured(boolean vertexImageTextured) {
        this.vertexImageTextured = vertexImageTextured;
    }

    public BufferedImage getEdgeImage() {
        return edgeImage;
    }

    public void setEdgeImage(BufferedImage edgeImage) {
        if (edgeImage.getRGB(0,0) != background.getRGB(0,0) && edgeImage.getRGB(0,0) != vertexImage.getRGB(0,0)) {
            this.edgeImage = edgeImage;
        }
    }

    public boolean isEdgeImageTextured() {
        return edgeImageTextured;
    }

    public void setEdgeImageTextured(boolean edgeImageTextured) {
        this.edgeImageTextured = edgeImageTextured;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getThickness() {
        return thickness;
    }

    public void setThickness(int thickness) {
        this.thickness = thickness;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}

