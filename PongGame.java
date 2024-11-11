import java.util.Random;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

public class PongGame extends Application {
    private static final int width = 800;
    private static final int height = 600;
    private static final int paddle_height = 100;
    private static final int paddle_width = 15;
    private static final double ballR = 15;
    private int ballYSpeed = 1;
    private int ballXSpeed = 1;
    private double playerOneYPos = height / 2;
    private double playerTwoYPos = height / 2;
    private double ballXPos = width / 2;
    private double ballYPos = height / 2;
    private int scoreP1 = 0;
    private int scoreP2 = 0;
    private boolean gameStarted;
    private final int playerOneXPos = 0;
    private final double playerTwoXPos = width - paddle_width;
    private boolean wPressed;
    private boolean sPressed;
    private boolean upPressed;
    private boolean downPressed;
    private Random random;

    public void start(Stage stage) throws Exception {
        Canvas canvas = new Canvas(width, height);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Scene scene = new Scene(new StackPane(canvas));
        stage.setScene(scene);
        stage.setTitle("Pong Game");

        Timeline tl = new Timeline(new KeyFrame(Duration.millis(10), e -> run(gc)));
        tl.setCycleCount(Timeline.INDEFINITE);

        // Tasten um die Paddles zu steuern
        scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case W -> wPressed = true;
                case S -> sPressed = true;
                case UP -> upPressed = true;
                case DOWN -> downPressed = true;
            }
        });

        scene.setOnKeyReleased(e -> {
            switch (e.getCode()) {
                case W -> wPressed = false;
                case S -> sPressed = false;
                case UP -> upPressed = false;
                case DOWN -> downPressed = false;
            }
        });

        //Mausklick um das Spiel zu starten
        canvas.setOnMouseClicked(e ->  gameStarted = true);

        stage.show();
        tl.play();
    }

    private void run(GraphicsContext gc) {
       //Fenster, Score und Paddles zeichnen
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, width, height);

        gc.setFill(Color.WHITE);
        gc.setFont(Font.font(25));
        gc.fillText(scoreP1 + "\t\t\t\t\t\t\t" + scoreP2, width / 2, 100);

        gc.fillRect(playerTwoXPos, playerTwoYPos, paddle_width, paddle_height);
        gc.fillRect(playerOneXPos, playerOneYPos, paddle_width, paddle_height);

      //Verschieben der Rechtecke wenn Taste gedrückt wird

        if (wPressed) playerOneYPos = Math.max(playerOneYPos - 10, 0);
        if (sPressed) playerOneYPos = Math.min(playerOneYPos + 10, height - paddle_height);
        if (upPressed) playerTwoYPos = Math.max(playerTwoYPos - 10, 0);
        if (downPressed) playerTwoYPos = Math.min(playerTwoYPos + 10, height - paddle_height);

        if(gameStarted) {
          //Ball zeichnen und bewegen

            ballXPos+=ballXSpeed;
            ballYPos+=ballYSpeed;

            gc.fillOval(ballXPos, ballYPos, ballR, ballR);


        } else {

            gc.setStroke(Color.WHITE);
            gc.setTextAlign(TextAlignment.CENTER);
            gc.strokeText("Click", width / 2, height / 2);

            //Ballposition zurücksetzen
            ballXPos = width / 2;
            ballYPos = height / 2;

            //Ballgeschwindigkeit und Richtung zurücksetzen
            ballXSpeed = new Random().nextInt(2) == 0 ? 1: -1;
            ballYSpeed = new Random().nextInt(2) == 0 ? 1: -1;
        }

        //Wenn der Ball die oberen und unteren ränder berührt wird richtung umgekehrt
        if(ballYPos > height) ballYSpeed *=-1;
        if(ballYPos < 0) ballYSpeed *=-1;

        //Wenn Spieler 1 den Ball nicht trifft bekommt Spieler 2 ein Punkt
        if(ballXPos < playerOneXPos - paddle_width) {
            scoreP2++;
            gameStarted = false;
        }

        //Wenn Spieler 2 den Ball nicht trifft bekommt Spieler 1 einen Punkt
        if(ballXPos > playerTwoXPos + paddle_width) {
            scoreP1++;
            gameStarted = false;
        }

        //Wird der Ball mit dem Paddle berührt, wird die Geschwindigkeit erhöht und der Ball in die andere Richtung zurückgeschossen
        if(((ballXPos < playerOneXPos + paddle_width) && ballYPos >= playerOneYPos && ballYPos <= playerOneYPos + paddle_height)) {
            ballYSpeed += 1 * Math.signum(ballYSpeed);
            ballXSpeed += 1 * Math.signum(ballXSpeed);
            ballXSpeed *= -1;
            ballXSpeed += random.nextDouble() * 0.5;
            ballYSpeed *= -1;
        }
        if (((ballXPos + ballR > playerTwoXPos) && ballYPos >= playerTwoYPos && ballYPos <= playerTwoYPos + paddle_height)){
            ballYSpeed += 1 * Math.signum(ballYSpeed);
            ballXSpeed += 1 * Math.signum(ballXSpeed);
            ballXSpeed *= -1;
            ballXSpeed += random.nextDouble() * 0.5;
            ballYSpeed *= -1;

        }

    }

    //Pong Game starten
    public static void main(String[] args) {
        launch(args);
    }
}
