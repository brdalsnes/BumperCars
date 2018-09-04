package sample;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Controller {

    
    protected final static double MAX_ACCELERATION = 0.1;
    protected final static double MAX_TURN = 1;
    private final static int START_X = 300;

    private int leftGoal = 0;
    private int rightGoal = 0;

    private Set<KeyCode> keysPressed = new HashSet<>();

    private Car player = new Car(-START_X, 0, 0, 0, 0, Color.GREEN);
    private Car opponent = new Car(START_X, 0, 0, 0, 180, Color.MAROON);
    private Ball ball = new Ball(0, 0);

    private List<Rectangle> horizontalBarriers = new ArrayList<>();
    private List<VBox> verticalBarriers = new ArrayList<>();
    private List<Rectangle> sideBarriers = new ArrayList<>();

    @FXML
    private Label score;
    @FXML
    private StackPane field;
    @FXML
    private Rectangle h1;
    @FXML
    private Rectangle h2;
    @FXML
    private VBox v1;
    @FXML
    private VBox v2;
    @FXML
    private Rectangle s1;
    @FXML
    private Rectangle s2;
    @FXML
    private Rectangle s3;
    @FXML
    private Rectangle s4;

    @FXML
    public void initialize(){

        //Sets views
        field.getChildren().addAll(player.getView(), opponent.getView(), ball.getView());
        horizontalBarriers.add(h1);
        horizontalBarriers.add(h2);
        verticalBarriers.add(v1);
        verticalBarriers.add(v2);
        sideBarriers.add(s1);
        sideBarriers.add(s2);
        sideBarriers.add(s3);
        sideBarriers.add(s4);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                //Registers what keys are pressed
                registerKeys();

                //Handles movement based on velocity and resistance
                handleMovement(player);
                handleMovement(opponent);
                handleMovement(ball);

                //Checks intersecting views
                checkBumpWall(player, true);
                checkBumpWall(opponent, true);
                checkBumpWall(ball, false);
                checkBumpCarBall(player, ball);
                checkBumpCarBall(opponent, ball);
                checkBumpCarCar(player, opponent);
                checkGoal();

                //Opponent is steered towards ball, player, or in position between ball and own goal
                opponent.steerToPoint(new Point2D(ball.getView().getTranslateX(), ball.getView().getTranslateY()), 0.00015, 0.0025); //Normal
                //opponent.steerToPoint(new Point2D(player.getView().getTranslateX(), player.getView().getTranslateY()), 0.00015, 0.001); //Aggressive
                //opponent.steerToPoint(new Point2D(0.5*(ball.getView().getTranslateX() + 0.5*field.getWidth()), 0.5*ball.getView().getTranslateY()), 0.0001, 0.001); //Defensive
            }
        };
        timer.start();
    }

    @FXML
    private void handleKeyPressed(KeyEvent e){
        keysPressed.add(e.getCode());
    }

    @FXML
    private void handleKeyReleased(KeyEvent e){
        keysPressed.remove(e.getCode());
    }

    private void registerKeys(){
        if(keysPressed.contains(KeyCode.UP) && keysPressed.contains(KeyCode.LEFT)){
            player.accelerate(MAX_ACCELERATION);
            player.rotate(-MAX_TURN);
        }
        else if(keysPressed.contains(KeyCode.UP) && keysPressed.contains(KeyCode.RIGHT)){
            player.accelerate(MAX_ACCELERATION);
            player.rotate(MAX_TURN);
        }
        else if(keysPressed.contains(KeyCode.DOWN) && keysPressed.contains(KeyCode.LEFT)){
            player.accelerate(-0.5*MAX_ACCELERATION);
            player.rotate(-MAX_TURN);
        }
        else if(keysPressed.contains(KeyCode.DOWN) && keysPressed.contains(KeyCode.RIGHT)){
            player.accelerate(-0.5*MAX_ACCELERATION);
            player.rotate(MAX_TURN);
        }
        else if(keysPressed.contains(KeyCode.UP)){
            player.accelerate(MAX_ACCELERATION);
        }
        else if(keysPressed.contains(KeyCode.DOWN)){
            player.accelerate(-0.5*MAX_ACCELERATION);
        }
        else if(keysPressed.contains(KeyCode.LEFT)){
            player.rotate(-MAX_TURN);
        }
        else if(keysPressed.contains(KeyCode.RIGHT)){
            player.rotate(MAX_TURN);
        }
    }

    private void handleMovement(GameObject object){
        if(object.getVelocity().magnitude() > 0.1){
            object.move();
        }
    }

    private void checkBumpWall(GameObject object, boolean bumpGoal){
        for(Rectangle b : horizontalBarriers){
            if(b.intersects(b.sceneToLocal(object.getView().localToScene(object.getView().getBoundsInLocal())))){
                object.bumpWall(1);
            }
        }
        if(bumpGoal){
            for(VBox b : verticalBarriers){
                if(b.intersects(b.sceneToLocal(object.getView().localToScene(object.getView().getBoundsInLocal())))){
                    object.bumpWall(-1);
                }
            }
        }else{
            for(Rectangle b : sideBarriers){
                if(b.intersects(b.sceneToLocal(object.getView().localToScene(object.getView().getBoundsInLocal())))){
                    object.bumpWall(-1);
                }
            }
        }
    }

    private void checkBumpCarBall(Car car, Ball ball){
        if(car.getView().intersects(car.getView().sceneToLocal(ball.getView().localToScene(ball.getView().getBoundsInLocal())))){
            ball.bumpCar(car);
        }
    }

    private void checkBumpCarCar(Car carA, Car carB){
        if(carA.getView().intersects(carA.getView().sceneToLocal(carB.getView().localToScene(carB.getView().getBoundsInLocal())))){
            Point2D vA = carA.getVelocity();
            Point2D vB = carB.getVelocity();
            carA.bumpCar(vB);
            carB.bumpCar(vA);
        }
    }

    private void checkGoal(){
        //Checks if the entire ball is outside the field to register goal
        if(Math.abs(ball.getView().getTranslateX()) > ((field.getWidth()/2) + 2*ball.getRadius())){
            System.out.println("GOAL!!!!");
            if(ball.getView().getTranslateX() < 0){
                leftGoal++;
            }else{
                rightGoal++;
            }
            player.reset(-START_X, 0, 0, 0, 0);
            opponent.reset(START_X, 0, 0, 0, 180);
            ball.reset(0, 0);
            score.setText(rightGoal + " - " + leftGoal);
            try {
                Thread.sleep(2000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }

        }
    }

}
