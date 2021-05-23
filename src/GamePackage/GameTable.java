package GamePackage;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import myException.GameException;

import java.io.*;
import java.util.*;

public class GameTable {
    private IntegerProperty countStone;
    private ObjectProperty<Player> player1;
    private ObjectProperty<Player> player2;
    private ObjectProperty<ArrayList<Circle>> stones;

    private IntegerProperty moveNumber;
    private int choice;


    public GameTable(Player player1, Player player2) {
        Random random = new Random();
        setPlayer1(player1);
        setPlayer2(player2);
        int count = random.nextInt(90) + 10;
        setCountStone(count);
        setStones(new ArrayList<>(count));
        setMoveNumber(0);
        choice = 0;
    }

    //Массив позиций (0 - проигрышная; 1 - выигрышная)
    private ArrayList<Integer> choiceArray(int count) {
        ArrayList<Integer> list = new ArrayList<>(count + 1);
        int[] choice = {1, 2, 3, 4};
        list.add(0);
        for (int i = 0; i <= count;) {
            int t = list.get(i) == 0 ? 1 : 0;
            for (int value : choice) {
                list.add(i + value, t);
            }
            i+=4;
        }
        return list;
    }

    //полуить оптимальный шаг (Для игры с ботом)
    public int getOptimalChoice(int count) {
        if (count >= 1 && count <= 4) return count;
        ArrayList<Integer> list = choiceArray(count);
        if (list.get(count) == 0) list.set(count, 100);
        else  list.set(count, 101);
        int[] choice = {1, 2, 3, 4};
        for (int value : choice) {
            if (list.get(count - value) == 0 && list.get(count - value - 1) == 1) {
                list.set(count - value, 90);
                //System.out.println(list);
                return value;
            }
            if (list.get(count - value) == 1 && list.get(count - value - 1) == 0) {
                list.set(count - value, 91);
                //System.out.println(list);
                return value;
            }
        }
        //System.out.println(list);
        System.out.print("Random ");
        Random random = new Random();
        return random.nextInt(3) + 1;
    }

    //Запись объекта в файл
    public void writeExternal(String fileName) {
        try (FileWriter writer = new FileWriter(fileName, false)) {
            writer.write(this.toString());
            for (Circle stone : this.getStones()) {
                writer.write(String.format(Locale.ROOT, "\n%f %f", stone.getCenterX(), stone.getCenterY()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //чтение объекта из файла
    public void readExternal(String fileName) throws GameException, NumberFormatException{
        try (FileReader reader = new FileReader(fileName)) {
            Scanner scanner = new Scanner(reader);
            String[] s;
            if (scanner.hasNextLine()) {
                s = scanner.nextLine().split(",");
                if (s.length != 11) throw new GameException("Файл с сохранением некорректно составлен!(1)");
            } else throw new GameException("Файл с сохранением пуст");
            ArrayList<Circle> arrayList = new ArrayList<>(100);
            while (scanner.hasNextLine()) {
                String[] s1 = scanner.nextLine().split(" ");
                Circle circle = new Circle(15, Color.DARKGRAY);
                circle.setStroke(Color.BLACK);
                circle.setCenterX(Double.parseDouble(s1[0]));
                circle.setCenterY(Double.parseDouble(s1[1]));
                arrayList.add(circle);
            }
            if (arrayList.size() != Integer.parseInt(s[0]))
                throw new GameException("Файл с сохранением некорректо составлен!(2)" +
                        "\n Введённое число камней не совпадант с количеством координат.");
            if (arrayList.size() == 0) throw new GameException("Игра, в данном сохранении уже окончена!");

            setCountStone(Integer.parseInt(s[0]));
            setPlayer1(new Player(Integer.parseInt(s[1]), s[2], Boolean.parseBoolean(s[3]), Boolean.parseBoolean(s[4])));
            setPlayer2(new Player(Integer.parseInt(s[5]), s[6], Boolean.parseBoolean(s[7]), Boolean.parseBoolean(s[8])));
            setChoice(Integer.parseInt(s[9]));
            setMoveNumber(Integer.parseInt(s[10]));
            setStones(arrayList);
        } catch (IOException ex) {
            System.out.println(ex.getMessage() + " Файл с сохранением некоррекно составлен");
        }
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%d,%d", getCountStone(), getPlayer1(), getPlayer2(), getChoice(),getMoveNumber());
    }


    //Далее идут ТОЛЬКО сеттеры и геттеры
    public int getChoice() {
        return choice;
    }

    public void setChoice(int choice) {
        this.choice = choice;
    }

    public final ArrayList<Circle> getStones() {
        return stonesProperty().get();
    }

    public ObjectProperty<ArrayList<Circle>> stonesProperty() {
        if (stones == null)
            stones = new SimpleObjectProperty<>();
        return stones;
    }

    public final void setStones(ArrayList<Circle> stones) {
        stonesProperty().set(stones);
    }

    public final Player getPlayer1() {
        return player1Property().get();
    }

    public ObjectProperty<Player> player1Property() {
        if (player1 == null)
            player1 = new SimpleObjectProperty<>();
        return player1;
    }

    public final void setPlayer1(Player player1) {
        player1Property().set(player1);
    }

    public final Player getPlayer2() {
        return player2Property().get();
    }

    public ObjectProperty<Player> player2Property() {
        if (player2 == null)
            player2 = new SimpleObjectProperty<>();
        return player2;
    }

    public final void setPlayer2(Player player2) {
        player2Property().set(player2);
    }

    public final int getCountStone() {
        return countStoneProperty().get();
    }

    public IntegerProperty countStoneProperty() {
        if (countStone == null)
            countStone = new SimpleIntegerProperty();
        return countStone;
    }

    public final void setCountStone(int countStone) {
        countStoneProperty().set(countStone);
    }

    public final int getMoveNumber() {
        return moveNumberProperty().get();
    }

    public IntegerProperty moveNumberProperty() {
        if (moveNumber == null)
            moveNumber = new SimpleIntegerProperty();
        return moveNumber;
    }

    public final void setMoveNumber(int moveNumber) {
        moveNumberProperty().set(moveNumber);
    }

    public final void incMoveNumber(){
        setMoveNumber(getMoveNumber() + 1);
    }
}
