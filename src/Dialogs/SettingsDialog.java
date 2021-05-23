package Dialogs;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SettingsDialog {
    private Stage stage;
    private Scene scene;
    private ComboBox<String> endOfGame;
    private ComboBox<String> gameType;
    private ObservableList<String> optEndGame;
    private ObservableList<String> optGameType;
    private GridPane gridPane;
    private Label endLabel;
    private Label gameTypeLabel;
    private Button buttonOk;
    private boolean endFlag;
    private boolean botFlag;
    private StartGameDialog startGameDialog;


    private void createPane(){
        gridPane.add(endLabel,0,0);
        gridPane.add(endOfGame,1,0);
        gridPane.add(gameTypeLabel,0,1);
        gridPane.add(gameType,1,1);
        GridPane.setHalignment(buttonOk, HPos.RIGHT);
        gridPane.add(buttonOk,1,2);
    }

    public SettingsDialog(StartGameDialog startGameDialog) {
        this.startGameDialog = startGameDialog;
        stage = new Stage();
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Settings");

        this.endFlag = true;
        this.botFlag = false;

        gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        endLabel = new Label("Конец игры");
        gameTypeLabel = new Label("Тип игры");

        createEndGameComboBox();
        createGameTypeComboBox();
        createButtons();
        createPane();

        scene = new Scene(gridPane, 500, 400);
        stage.setScene(scene);
        stage.showAndWait();
    }

    public void showScene(){
        stage.setScene(scene);
        stage.showAndWait();
    }

    private void createEndGameComboBox(){
        optEndGame = FXCollections.observableArrayList("Игрок забирает последний камень",
                "Игрок оставляет противнику последний камень");
        endOfGame = new ComboBox<>(optEndGame);
        endOfGame.setValue(endFlag ? optEndGame.get(0) : optEndGame.get(1));

    }

    private void createGameTypeComboBox(){
        optGameType = FXCollections.observableArrayList("два игрока", "игрок с компьютером");
        gameType = new ComboBox<>(optGameType);
        gameType.setValue(botFlag ? optGameType.get(1) : optGameType.get(0));
    }

    private void createButtons(){
        buttonOk = new Button("Apply");
        buttonOk.setOnAction(event -> {
            stage.close();
            startGameDialog.setMoveFlag(endOfGame.getValue().equals(optEndGame.get(0)));
            startGameDialog.setBotFlag(gameType.getValue().equals(optGameType.get(1)));
        });
    }

    @Override
    public String toString() {
        return String.format("%s\n%s",endOfGame.getValue(),gameType.getValue());
    }
}

