package GamePackage;

import Dialogs.PlayerEditDialog;
import Dialogs.StartGameDialog;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import myException.GameException;

public class Main extends Application {

    private MenuBar menuBar;
    private GameTable gameTable;
    private ViewGameTable viewGameTable;
    private StartGameDialog startGameDialog;

    private void createFileMenu() {
        Menu fileMenu = new Menu("_Menu");
        MenuItem newGame = new MenuItem("New Game");
        newGame.setAccelerator(KeyCombination.keyCombination("shortcut+N"));
        MenuItem save = new MenuItem("Save");
        save.setAccelerator(KeyCombination.keyCombination("shortcut+S"));
        MenuItem downloadGame = new MenuItem("Download game");
        downloadGame.setAccelerator(KeyCombination.keyCombination("shortcut+D"));
        MenuItem exit = new MenuItem("Exit");
        exit.setAccelerator(KeyCombination.keyCombination("shortcut+E"));
        fileMenu.getItems().addAll(newGame, save, downloadGame, new SeparatorMenuItem(), exit);


        newGame.setOnAction((ActionEvent event) -> {
            startGameDialog = new StartGameDialog();
            gameTable = new GameTable(new Player(0, "player1", true, false),
                    new Player(0, "player2", false, startGameDialog.isBotFlag()));
            gameTable.setChoice(startGameDialog.isMoveFlag() ? 0 : 1);
            new PlayerEditDialog(gameTable);
            viewGameTable.setGameTable(gameTable);
            System.out.println("New Game!");
        });

        save.setOnAction((ActionEvent event) -> {
            gameTable.writeExternal("src//Resources//save//saves.txt");
            message("the game has been saved","Save");
        });

        downloadGame.setOnAction((ActionEvent event) -> {
            try {
                gameTable.readExternal("src//Resources//save//saves.txt");
                message("the game was successfully downloaded","Download");
                viewGameTable.setGameTable(gameTable);
                System.out.println("Download Game!");
            } catch (GameException | NumberFormatException e) {
                errorMessage(e.getMessage());
                System.out.println(e.getMessage());
            }
        });

        exit.setOnAction((ActionEvent event) -> Platform.exit());

        menuBar.getMenus().addAll(fileMenu);
    }

    private void createHelpMenu() {
        Menu helpMenu = new Menu("_Help!");
        MenuItem information = new MenuItem("Information");
        information.setAccelerator(KeyCombination.keyCombination("shortcut+I"));
        helpMenu.getItems().addAll(information);

        String text = "УПРАВЛЕНИЕ:\n" +
                "Взять камень (2 способа)\n" +
                "    1) Двойной щелчок мыши по камню.\n" +
                "    2) Enter - взять рандомный камень.\n\n" +
                "Передать ход (Если взят хотябы 1 камень)\n" +
                "    1) Back Space\n" +
                "       В случае если игрок взял 4 камня, то ход передаётся\n" +
                "       другому игроку автоматически.\n\n" +
                "Для того чтобы узнать кто ходит, в правом врехнем углу показывается информация об игре.\n" +
                "Активный игрок подсвечивается красным или зелёным цветом.\n\n" +
                "В меню есть следующие Пункты:\n" +
                "    1) New Game - начать новую игру.\n" +
                "       Комбинация: Ctrl + N\n" +
                "    2) Save - перезаписывает текущее сохранение.\n" +
                "       Комбинация: Ctrl + S\n" +
                "    3) Download game - загрузка сохраения. Сохранение\n" +
                "       должно быть правильно составлено, в противном\n" +
                "       случае программа выдаст сообщение об ошибке.\n" +
                "       Комбинация: Ctrl + D\n" +
                "    4) Exit - выход из игры.\n" +
                "       Комбинация: Ctrl + E\n\n" +
                "Приятной игры!";

        information.setOnAction((ActionEvent event) -> message(text,"Help"));
        menuBar.getMenus().addAll(helpMenu);
    }

    private void message(String text, String title){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(text);
        alert.showAndWait();
    }

    private void errorMessage(String s){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error!");
        alert.setContentText(s);
        alert.showAndWait();
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Game");
        HBox root = new HBox(10);
        root.setAlignment(Pos.BOTTOM_CENTER);
        BorderPane borderPane = new BorderPane();
        menuBar = new MenuBar();
        createFileMenu();
        createHelpMenu();
        borderPane.setTop(menuBar);

        startGameDialog = new StartGameDialog();

        if (!startGameDialog.isFlagOK()) return;

        if (startGameDialog.isDownLoadFlag()){
            gameTable = startGameDialog.getGameTable();
        }
        else {
            Player player1 = new Player(0, "player1", true, false);
            Player player2 = new Player(0, "player2", false, startGameDialog.isBotFlag());
            gameTable = new GameTable(player1, player2);
            gameTable.setChoice(startGameDialog.isMoveFlag() ? 0 : 1);
            new PlayerEditDialog(gameTable);
        }

        viewGameTable = new ViewGameTable(gameTable);
        root.getChildren().add(viewGameTable.getPane());
        borderPane.setCenter(root);

        Scene scene = new Scene(borderPane, 800, 800, false);
        stage.setScene(scene);
        stage.show();
    }
}