package sample;

import javafx.geometry.Point2D;
import javafx.scene.shape.Shape;


public class GameObject {
    private Shape view;
    private Point2D velocity;


    public void move(){
        //Theta uses the same coordinate system as rotation
        double theta;
        if(velocity.getY() > 0){
            theta = Math.toRadians(velocity.angle(1,0));
        }else{
            theta = -Math.toRadians(velocity.angle(1,0));
        }

        double mag = -0.005*velocity.magnitude();
        Point2D drag = new Point2D(mag*Math.cos(theta), mag*Math.sin(theta));
        velocity = velocity.add(drag);
        view.setTranslateX(view.getTranslateX()+ velocity.getX());
        view.setTranslateY(view.getTranslateY()+ velocity.getY());
    }

    public void bumpWall(int dir){
        velocity = new Point2D(dir*velocity.getX(), -dir*velocity.getY());
    }

    public void setView(Shape view) {
        this.view = view;
    }

    public Shape getView() {
        return view;
    }

    public void setVelocity(Point2D velocity) {
        this.velocity = velocity;
    }

    public Point2D getVelocity() {
        return velocity;
    }

    public void reset(int x, int y, double vx, double vy){
        view.setTranslateX(x);
        view.setTranslateY(y);
        velocity = new Point2D(vx, vy);
    }
}
