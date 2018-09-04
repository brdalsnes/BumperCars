package sample;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Ball extends GameObject{

    private double radius;

    public Ball(int x, int y){
        this.radius = 10;
        Circle view = new Circle(radius);
        view.setFill(Color.BLACK);
        super.setView(view);
        this.reset(x, y);
    }

    public void bumpCar(Car car){
        if(car.getVelocity().magnitude() > 0) {
            super.setVelocity(car.getVelocity().multiply(2));
        }else{
            super.setVelocity(super.getVelocity().multiply(-1));
        }
    }

    public double getRadius() {
        return radius;
    }

    public void reset(int x, int y) {
        //Ball starts with random velocity in y direction
        super.reset(x, y, 0, Math.random()*20 -10);
    }
}
