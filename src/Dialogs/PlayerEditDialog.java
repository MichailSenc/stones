package Dialogs;

import GamePackage.GameTable;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PlayerEditDialog {

    private TextField nameEdit1;
    private TextField nameEdit2;
    private GameTable gameTable;
    private Stage dialog;
    private GridPane gridPane;
    private Button buttonOk;


    private void createPane(){
        gridPane.add(new Label("Player 1:"),0,0);
        gridPane.add(new Label("Player 2:"),0,1);

        nameEdit1 = new TextField("player1");
        nameEdit2 = new TextField("player2");

        nameEdit1.setTextFormatter(new TextFormatter<>(change ->
                (change.getControlNewText().matches("[a-zA-Zа-яА-Я0-9 ]*")) ? change : null));
        nameEdit2.setTextFormatter(new TextFormatter<>(change ->
                (change.getControlNewText().matches("[a-zA-Zа-яА-Я0-9 ]*")) ? change : null));

        gridPane.add(nameEdit1,1,0);
        gridPane.add(nameEdit2,1,1);
        GridPane.setHalignment(buttonOk, HPos.RIGHT);
        gridPane.add(buttonOk,1,2);
    }

    public void createButton(){
        buttonOk = new Button("Apply");
        buttonOk.setOnAction((event) -> {
            dialog.close();
            gameTable.getPlayer1().setName(nameEdit1.getText().equals("") ? "Player1" : nameEdit1.getText());
            gameTable.getPlayer2().setName(nameEdit2.getText().equals("") ? "Player2" : nameEdit2.getText());
        });
    }


    public PlayerEditDialog(GameTable gameTable) {
        this.gameTable = gameTable;
        dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setResizable(false);
        dialog.setTitle("Edit players names");
        gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        createButton();
        createPane();

        Scene scene = new Scene(gridPane, 500,400);
        dialog.setScene(scene);
        dialog.showAndWait();
    }
}
