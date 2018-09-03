package sample;

import javafx.geometry.Point2D;

public class Car extends GameObject{
    private double rotation;

    public Car(int x, int y, double rotation){
        super(x, y);
        this.rotation = rotation;
    }

    public void accelerate(double mag){
        Point2D acceleration = new Point2D(mag*Math.cos(Math.toRadians(rotation)), mag*Math.sin(Math.toRadians(rotation)));
        setVelocity(getVelocity().add(acceleration));
    }

    public void rotate(double turn){
        rotation += turn;
        if(rotation > 180){
            rotation = -179;
        }
        if(rotation <= -180){
            rotation = 180;
        }
    }

    public double getRotation() {
        return rotation;
    }
}


