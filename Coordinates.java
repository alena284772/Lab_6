package Vehicle;

import java.io.Serializable;

/**
 * The class responsible for the correctness of the entered coordinates for Vehicle
 */
public class Coordinates implements Serializable {
    /**
     * X field value must be greater than -815
     */
    private double x;
    private static final long serialVersionUID = 11L;
    /**
     * Y field value must be greater than -815
     */
    private long y;

    public void setX(double x) throws CoordinateException {
        if (x<-815){
            throw new CoordinateException("The value of X must be greater than -815, and the value of Y must be greater than -774");
        } else
        {this.x = x;
        }
    }

    public void setY(long y) throws CoordinateException {
        if (y<-774){
            throw new CoordinateException("The value of X must be greater than -815, and the value of Y must be greater than -774");
        } else {
            this.y = y; }
    }

    public double getX() {
        return x;
    }

    public long getY() {
        return y;
    }
}
