package GamePackage;

import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;
import java.util.Random;

public class ViewGameTable {
    private GameTable gameTable;
    private AnchorPane root;
    private Font font;
    private Point2D delta;
    private boolean isRotate;
    private Label player1;
    private Label player2;
    private Label score1;
    private Label score2;
    private Label totalScore;
    private Player thisPlayer;

    //создат панель и разместить камни
    private AnchorPane createAnchorPane() {
        AnchorPane pane = new AnchorPane();
        pane.setStyle("-fx-font-size: 22");

        GridPane gridPane = createGridPane();
        AnchorPane.setTopAnchor(gridPane,1.0);
        AnchorPane.setRightAnchor(gridPane,40.0);
        pane.getChildren().add(gridPane);

        ArrayList<Circle> stones = gameTable.getStones();

        for (int i = 0; i < gameTable.getCountStone(); i++) {
            Circle circle = createStone();
            pane.getChildren().add(circle);
            stones.add(circle);
        }
        return pane;
    }

    //Панель, показывает состояние игры (На экране располагается в Правом-Верхнем углу)
    private GridPane createGridPane(){
        player1 = new Label(gameTable.getPlayer1().getName());
        player1.setTextFill(gameTable.getPlayer1().isMove() ? Color.RED : Color.BLACK);
        player2 = new Label(gameTable.getPlayer2().getName());
        player2.setTextFill(gameTable.getPlayer2().isMove() ? Color.GREEN : Color.BLACK);
        thisPlayer = gameTable.getPlayer1().isMove() ? gameTable.getPlayer1() : gameTable.getPlayer2();
        score1 = new Label(Integer.toString(gameTable.getPlayer1().getCountStones()));
        score1.setTextFill(gameTable.getPlayer1().isMove() ? Color.RED : Color.BLACK);
        score2 = new Label(Integer.toString(gameTable.getPlayer2().getCountStones()));
        score2.setTextFill(gameTable.getPlayer2().isMove() ? Color.GREEN : Color.BLACK);
        totalScore = new Label(Integer.toString(gameTable.getCountStone()));
        player1.setFont(font);
        player2.setFont(font);
        score1.setFont(font);
        score2.setFont(font);
        totalScore.setFont(font);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.add(totalScore,1,0);
        gridPane.add(player1,0,1);
        gridPane.add(score1,1,1);
        gridPane.add(player2,0,2);
        gridPane.add(score2,1,2);
        return gridPane;
    }

    //Создать 1 объект - камень
    private Circle createStone() {
        double radius = 15;
        Random random = new Random();
        Circle circle = new Circle(radius,Color.DARKGRAY);
        circle.setStroke(Color.BLACK);
        double maxXCord = 700.0;
        double minXCord = 50.0;
        double posX = minXCord + maxXCord * random.nextDouble();
        double minYCord = 100.0;
        double maxYCord = 550.0;
        double posY = minYCord + maxYCord * random.nextDouble();
        circle.setCenterX(posX);
        circle.setCenterY(posY);
        circle.setFocusTraversable(true);

        addEventHandler(circle);

        return circle;
    }

    //Добавить EventHandler для камня
    private void addEventHandler(Circle circle) {
        Player player1 = gameTable.getPlayer1();
        Player player2 = gameTable.getPlayer2();
        circle.setOnMouseEntered((MouseEvent me) -> {
            circle.setFill(Color.AQUA);
            circle.requestFocus();
        });

        circle.setOnMouseExited((MouseEvent me) -> circle.setFill(Color.DARKGRAY));

        circle.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent mouseEvent) -> {
            circle.toFront();
            delta = new Point2D((mouseEvent.getSceneX() - circle.getLayoutX()),
                    (mouseEvent.getSceneY() - circle.getLayoutY()));
            if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                isRotate = true;
            }
        });

        circle.addEventHandler(MouseEvent.MOUSE_RELEASED, (MouseEvent mouseEvent) -> isRotate = false);

        circle.addEventHandler(MouseEvent.MOUSE_DRAGGED, (MouseEvent mouseEvent) -> {
            if (isRotate) {
                double xCord = mouseEvent.getSceneX() - circle.getLayoutX();
                double yCord = mouseEvent.getSceneY() - circle.getLayoutY();
                double angle = Math.acos(xCord / Math.sqrt(xCord * xCord + yCord * yCord));
                if (yCord < 0) {
                    angle = Math.PI - angle;
                }
                circle.setRotate(Math.toDegrees(angle));
            } else {
                double xCord = mouseEvent.getSceneX() - delta.getX();
                double yCord = mouseEvent.getSceneY() - delta.getY();
                circle.setLayoutX(xCord);
                circle.setLayoutY(yCord);
            }
        });

        circle.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
            if (mouseEvent.getClickCount() == 2 && !thisPlayer.isBot()) {
                root.getChildren().remove(circle);
                thisPlayer.setCountStones(thisPlayer.getCountStones() + 1);
                gameTable.setCountStone(gameTable.getCountStone() - 1);
                System.out.println(gameTable.getCountStone());
                gameTable.getStones().remove(circle);
                gameTable.incMoveNumber();
            }
        });

        circle.addEventHandler(KeyEvent.KEY_PRESSED, (KeyEvent keyEvent) -> {
            if (keyEvent.getCode() == KeyCode.ENTER && !thisPlayer.isBot()) {
                root.getChildren().remove(circle);
                thisPlayer.setCountStones(thisPlayer.getCountStones() + 1);
                gameTable.setCountStone(gameTable.getCountStone() - 1);
                System.out.println(gameTable.getCountStone());
                gameTable.getStones().remove(circle);
                gameTable.incMoveNumber();
            }
            if (keyEvent.getCode() == KeyCode.BACK_SPACE && gameTable.getMoveNumber() != 0) {
                if (gameTable.getCountStone() == 1) {
                    if (gameTable.getChoice() == 1) {
                        message();
                        Platform.exit();
                        System.exit(1);
                    }
                }
                System.out.println("----");
                gameTable.setMoveNumber(0);
                player1.reverseMovie();
                player2.reverseMovie();
            }
        });
    }

    public ViewGameTable(GameTable gameTable){
        this.gameTable = gameTable;
        font = Font.font("Tahoma", FontWeight.NORMAL, 20);
        root = createAnchorPane();
        addListeners();
        root.setPrefSize(800,800);
    }

    private void addListeners() {
        gameTable.countStoneProperty().addListener((observableValue, number, t1) -> {
            totalScore.setText(Integer.toString(gameTable.getCountStone()));
            if (gameTable.getCountStone() == 0) {
                if (gameTable.getChoice() != 0)
                    thisPlayer = thisPlayer == gameTable.getPlayer1() ? gameTable.getPlayer2() : gameTable.getPlayer1();
                message();
                Platform.exit();
                System.exit(1);
            }
        });
        gameTable.getPlayer1().moveProperty().addListener((observableValue, aBoolean, t1) -> {
            player1.setTextFill(gameTable.getPlayer1().isMove() ? Color.RED : Color.BLACK);
            score1.setTextFill(gameTable.getPlayer1().isMove() ? Color.RED : Color.BLACK);
            if (gameTable.getPlayer1().isMove()) {
                thisPlayer = gameTable.getPlayer1();
            }

        });
        gameTable.getPlayer2().moveProperty().addListener((observableValue, aBoolean, t1) -> {
            player2.setTextFill(gameTable.getPlayer2().isMove() ? Color.GREEN : Color.BLACK);
            score2.setTextFill(gameTable.getPlayer2().isMove() ? Color.GREEN : Color.BLACK);
            if (gameTable.getPlayer2().isMove()) {
                thisPlayer = gameTable.getPlayer2();
                //--------------------------Игра против бота------------------------//
                if (thisPlayer.isBot()) {
                    ArrayList<Circle> list = gameTable.getStones();
                    int count = gameTable.getOptimalChoice(gameTable.getCountStone() - gameTable.getChoice());
                    System.out.printf("БОТ ВЗЯЛ %d КАМНЯ!\n", count);
                    for (int i = 0; i < count; i++) {
                        Circle circle = list.get(0);
                        root.getChildren().remove(circle);
                        thisPlayer.setCountStones(thisPlayer.getCountStones() + 1);
                        gameTable.setCountStone(gameTable.getCountStone() - 1);
                        list.remove(circle);
                        gameTable.incMoveNumber();
                        System.out.println(gameTable.getCountStone());
                    }
                    if (count != 4) {
                        if (gameTable.getCountStone() == 1) {
                            if (gameTable.getChoice() == 1) {
                                message();
                                Platform.exit();
                            }
                        }
                        gameTable.setMoveNumber(0);
                        gameTable.getPlayer1().reverseMovie();
                        gameTable.getPlayer2().reverseMovie();
                    }
                }
            }
            //-----------------------------------------------------------------------//
        });
        gameTable.getPlayer1().nameProperty().addListener((observableValue, s, t1) ->
                player1.setText(gameTable.getPlayer1().getName()));
        gameTable.getPlayer2().nameProperty().addListener((observableValue, s, t1) ->
                player2.setText(gameTable.getPlayer1().getName()));
        gameTable.getPlayer1().countStonesProperty().addListener((observableValue, number, t1) ->
                score1.setText(Integer.toString(gameTable.getPlayer1().getCountStones())));
        gameTable.getPlayer2().countStonesProperty().addListener((observableValue, number, t1) ->
                score2.setText(Integer.toString(gameTable.getPlayer2().getCountStones())));
        gameTable.moveNumberProperty().addListener((observableValue, number, t1) -> {
            if (gameTable.getMoveNumber() == 4) {
                System.out.println("----");
                gameTable.setMoveNumber(0);
                gameTable.getPlayer1().reverseMovie();
                gameTable.getPlayer2().reverseMovie();
            }
        });
    }

    private void message(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Victory!");
        alert.setContentText(String.format("Player \"%s\" win!", thisPlayer.getName()));
        alert.showAndWait();
    }


    public void setGameTable(GameTable gameTable) {
        this.gameTable = gameTable;
        root.getChildren().clear();
        GridPane gridPane = createGridPane();
        AnchorPane.setTopAnchor(gridPane, 1.0);
        AnchorPane.setRightAnchor(gridPane, 40.0);
        root.getChildren().add(gridPane);
        ArrayList<Circle> stones = gameTable.getStones();
        if (stones.size() == 0)
            for (int i = 0; i < gameTable.getCountStone(); i++) {
                Circle circle = createStone();
                root.getChildren().add(circle);
                stones.add(circle);
            }
        else
            for (Circle stone : stones) {
                stone.setFocusTraversable(true);
                addEventHandler(stone);
                root.getChildren().add(stone);
            }
        player1.setText(gameTable.getPlayer1().getName());
        player2.setText(gameTable.getPlayer2().getName());
        addListeners();
    }

    public AnchorPane getPane(){
        return root;
    }
}
