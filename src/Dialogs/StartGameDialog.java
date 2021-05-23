package Dialogs;

import GamePackage.GameTable;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import myException.GameException;

public class StartGameDialog {
    private Stage dialog;
    private GridPane root;
    private SettingsDialog settingsDialog;
    private Button buttonStart;
    private Button buttonDownload;
    private Button buttonSettings;
    private Button buttonExit;
    private boolean flagOK;
    private boolean moveFlag;
    private boolean botFlag;
    private boolean downLoadFlag;
    private GameTable gameTable;

    private void createPane(){
        root.add(buttonStart,0,0);
        root.add(buttonDownload,0,1);
        root.add(buttonSettings,0,2);
        root.add(buttonExit,0,3);
    }

    public StartGameDialog() {
        dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setResizable(false);
        dialog.setTitle("New Game");
        root = new GridPane();
        root.setAlignment(Pos.CENTER);
        root.setVgap(10);
        root.setHgap(10);

        flagOK = false;
        downLoadFlag = false;

        createButtons();
        createPane();

        Scene scene = new Scene(root, 500,400);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    public void createButtons(){
        buttonStart = new Button("Start new game");
        buttonStart.setMinWidth(100);
        buttonStart.setOnAction((ActionEvent event) -> {
            if (settingsDialog == null){
                moveFlag = true;
                botFlag = false;
            }
            flagOK = true;
            dialog.close();
        });

        buttonSettings = new Button("Settings");
        buttonSettings.setMinWidth(100);
        buttonSettings.setOnAction((ActionEvent event) -> {
            if (settingsDialog == null)
                settingsDialog = new SettingsDialog(this);
            else {
                settingsDialog.showScene();
            }
        });

        buttonDownload = new Button("Download game");
        buttonDownload.setMinWidth(100);
        buttonDownload.setOnAction((ActionEvent event) -> {
            try {
                gameTable = new GameTable(null, null);
                gameTable.readExternal("src//Resources//save//saves.txt");
                message();
                flagOK = true;
                downLoadFlag = true;
                dialog.close();
                //viewGameTable.setGameTable(gameTable);
                System.out.println("Download Game!");
            } catch (GameException | NumberFormatException e ) {
                errorMessage(e.getMessage());
                System.out.println(e.getMessage());
            }
        });

        buttonExit = new Button("Exit");
        buttonExit.setMinWidth(100);
        buttonExit.setOnAction((ActionEvent event) -> {
            flagOK = false;
            dialog.close();
        });
    }

    private void message(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Download");
        alert.setContentText("the game was successfully downloaded");
        alert.showAndWait();
    }

    private void errorMessage(String s){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error!");
        alert.setContentText(s);
        alert.showAndWait();
    }

    public boolean isDownLoadFlag() {
        return downLoadFlag;
    }

    public void setDownLoadFlag(boolean downLoadFlag) {
        this.downLoadFlag = downLoadFlag;
    }

    public boolean isFlagOK() {
        return flagOK;
    }

    public boolean isMoveFlag() {
        return moveFlag;
    }

    public boolean isBotFlag() {
        return botFlag;
    }

    public void setFlagOK(boolean flagOK) {
        this.flagOK = flagOK;
    }

    public void setMoveFlag(boolean moveFlag) {
        this.moveFlag = moveFlag;
    }

    public void setBotFlag(boolean botFlag) {
        this.botFlag = botFlag;
    }

    public GameTable getGameTable() {
        return gameTable;
    }
}

