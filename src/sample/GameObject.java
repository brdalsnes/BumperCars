package sample;

import javafx.geometry.Point2D;

public abstract class GameObject {
    private Point2D position;
    private Point2D velocity;

    public GameObject(int x, int y) {
        this.position = new Point2D(x,y);
        this.velocity = new Point2D(0,0);
    }

    public void move(){
        double theta; //Theta uses the same coordinate system as rotation
        if(velocity.getY() > 0){
            theta = Math.toRadians(velocity.angle(1,0));
        }else{
            theta = -Math.toRadians(velocity.angle(1,0));
        }
        double mag = -0.005*velocity.magnitude();
        Point2D drag = new Point2D(mag*Math.cos(theta), mag*Math.sin(theta));
        velocity = velocity.add(drag);
        position = position.add(velocity);
    }

    public void bump(){
        double theta; //Theta uses the same coordinate system as rotation
        if(velocity.getY() > 0){
            theta = Math.toRadians(velocity.angle(1,0));
        }else{
            theta = -Math.toRadians(velocity.angle(1,0));
        }
        double mag = -0.7*velocity.magnitude();
        Point2D force = new Point2D(mag*Math.cos(theta), mag*Math.sin(theta));
        velocity = new Point2D(0, 0);
        velocity = velocity.add(force);
        position = position.add(velocity);
        position = position.add(velocity);
    }

    public Point2D getPosition() {
        return position;
    }

    public Point2D getVelocity() {
        return velocity;
    }

    public void setVelocity(Point2D velocity) {
        this.velocity = velocity;
    }
}
