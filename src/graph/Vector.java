package graph;

/**
 * Created by bolz on 04.04.2016.
 */


public class Vector {
    /**
     * @Attributes :
     * x,y :Koordinaten des Punktes, um dessen Ortsvektor es sich handelt
     */
    private double x,y;

    /**
     * Setzt die Koordinaten
     */
    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * veraendert den Vektor zum Einheitsvektor
     */
    public void normalize(){
        double r=1/this.length();
        x=  x * r;
        y=y*r;
    }

    /**
     *
     * @return Gibt die Länge des Vektors zurueck
     */
    public double length(){
        return Math.sqrt(x*x+y*y);
    }

    /**
     * Getter
     */
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }
}
