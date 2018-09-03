package sample;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Rectangle;
import java.util.HashSet;
import java.util.Set;

public class Controller {

    private Set<KeyCode> keysPressed = new HashSet<>();

    private Car player = new Car(-300, 0, 0);
    private Car opponent = new Car(300, 0, 180);

    @FXML
    private Rectangle playerView;
    @FXML
    private Rectangle opponentView;
    @FXML
    private BorderPane border;

    @FXML
    public void initialize(){
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {

                //Handles pressed keys
                if(keysPressed.contains(KeyCode.UP) && keysPressed.contains(KeyCode.LEFT)){
                    player.accelerate(0.1);
                    player.rotate(-1);
                }
                else if(keysPressed.contains(KeyCode.UP) && keysPressed.contains(KeyCode.RIGHT)){
                    player.accelerate(0.1);
                    player.rotate(1);
                }
                else if(keysPressed.contains(KeyCode.UP)){
                    player.accelerate(0.1);
                }
                else if(keysPressed.contains(KeyCode.DOWN)){
                    player.accelerate(-0.05);
                }
                else if(keysPressed.contains(KeyCode.LEFT)){
                    player.rotate(-1);
                }
                else if(keysPressed.contains(KeyCode.RIGHT)){
                    player.rotate(1);
                }

                //Handles dynamics
                if(player.getVelocity().magnitude() > 0.1){
                    player.move();
                }

                //Handles interaction between views
                if(border.getRight().intersects(border.getRight().sceneToLocal(playerView.localToScene(playerView.getBoundsInLocal())))){
                    player.bump();
                }

                updateView();
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

    private void updateView(){
        playerView.setTranslateX(player.getPosition().getX());
        playerView.setTranslateY(player.getPosition().getY());
        playerView.setRotate(player.getRotation());
    }
}
