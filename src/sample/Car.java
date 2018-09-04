package sample;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class Car extends GameObject{

    private Rectangle view;

    public Car(int x, int y, int vx, int vy, double rotation, Color color){
        view = new Rectangle(80, 40);
        view.setFill(color);
        view.setArcWidth(20);
        view.setArcHeight(20);
        super.setView(view);
        this.reset(x, y, vx, vy, rotation);
    }

    public void steerToPoint(Point2D point, double accelerationConstant, double turnConstant){
        //Distance vector from car to point
        Point2D distVector = point.subtract(new Point2D(view.getTranslateX(), view.getTranslateY()));

        //Sign of cross product is dependent on which side the velocity vector is compared to the distance vector
        double turn = -turnConstant*distVector.crossProduct(getVelocity()).getZ();
        if(turn > Controller.MAX_TURN) { //Limited to player limit
            turn = Controller.MAX_TURN;
        }
        rotate(turn);

        //Acceleration dependent on distance to point
        double acceleration = accelerationConstant*distVector.magnitude();
        if(acceleration > Controller.MAX_ACCELERATION){ //Limited to player limit
            acceleration = Controller.MAX_ACCELERATION;
        }
        accelerate(acceleration);

    }

    public void accelerate(double mag){
        Point2D acceleration = new Point2D(mag*Math.cos(Math.toRadians(view.getRotate())), mag*Math.sin(Math.toRadians(view.getRotate())));
        super.setVelocity(super.getVelocity().add(acceleration));
    }

    public void rotate(double turn){
        view.setRotate(view.getRotate() + turn);
    }

    public void bumpCar(Point2D obstacleVelocity){
        super.setVelocity(obstacleVelocity.multiply(1));
    }

    //Overload
    public void reset(int x, int y, double vx, double vy, double rotation) {
        super.reset(x, y, vx, vy);
        view.setRotate(rotation);
    }

    public Point2D getVelocity() {
        return super.getVelocity();
    }

    public Rectangle getView() {
        return view;
    }
}


