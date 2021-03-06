package graph;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.SweetCode.e.math.ILocation;
import de.SweetCode.e.utils.ToString.ToStringBuilder;
import field.Field;

/**
 * <p>
 * Materialien zu den zentralen NRW-Abiturpruefungen im Fach Informatik ab 2018
 * </p>
 * <p>
 * Klasse Vertex
 * </p>
 * <p>
 * Die Klasse Vertex stellt einen einzelnen Knoten eines Graphen dar. Jedes Objekt 
 * dieser Klasse verfuegt ueber eine im Graphen eindeutige ID als String und kann diese 
 * ID zurueckliefern. Darueber hinaus kann eine Markierung gesetzt und abgefragt werden.
 * </p>
 * 
 * @author Qualitaets- und UnterstuetzungsAgentur - Landesinstitut fuer Schule, Materialien zum schulinternen Lehrplan Informatik SII
 * @version Oktober 2015
 */
public class Vertex implements Comparable<Vertex> {

  private Vertex previous;
  private double minDistance;

  //Einmalige ID des Knotens und Markierung
  private String id;
  private boolean markStart;
  private boolean markTarget;
  private boolean mark;
  private Field field;
  private int x, y;

  private Vertex parent;
  private double dist;

  @JsonCreator
  public Vertex(@JsonProperty("id") String pID , @JsonProperty("x") int x, @JsonProperty("y") int y, @JsonProperty("field") Field pField){
    id = pID;
    markStart = false;
    markTarget = false;
    mark = false;
    this.x = x;
    this.y = y;
    field = pField;
  }
  
  /**
  * Die Anfrage liefert die ID des Knotens als String.
  */
  public String getID(){
    return new String(id);
  }

  public boolean isMarkTarget() {
    return markTarget;
  }

  public void setMarkTarget(boolean markTarget) {
    markStart = false;
    this.markTarget = markTarget;
  }

  public boolean isMarkStart() {
    return markStart;
  }

  public void setMarkStart(boolean markStart) {
    markTarget = false;
    this.markStart = markStart;
  }

  public boolean isMark() {
    return mark;
  }

  public void setMark(boolean mark) {
    this.mark = mark;
  }

  public Field getField() {
    return field;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public void setPosition(int x, int y){
      this.x = x;
      this.y = y;
  }

  public void setId(String id) {
    this.id = id;
  }


  public Vertex getParent() {
    return parent;
  }

  public void setParent(Vertex parent) {
    this.parent = parent;
  }

  public double getDist() {
    return dist;
  }

  public void setDist(double dist) {
    this.dist = dist;
  }

  public void setField(Field field) {
    this.field = field;
  }

  public Vertex getPrevious() {
    return previous;
  }

  public void setPrevious(Vertex previous) {
    this.previous = previous;
  }

  public void setMinDistance(double minDistance) {
    this.minDistance = minDistance;
  }

  public double getMinDistance() {
    return this.minDistance;
  }

  @Override
  public int compareTo(Vertex o) {
    return Double.compare(o.getMinDistance(), this.getMinDistance());
  }

  @Override
  public String toString() {
    return ToStringBuilder.create(this)
            .append("id", this.id)
            .append("location", new ILocation(this.x, this.y))
            .append("minDistance", this.minDistance)
            .append("markStart", this.markStart)
            .append("markTarget", this.markTarget)
            .append("mark", this.mark)
            .append("field", this.field)
            .append("parent", this.parent)
          .build();
  }

}
