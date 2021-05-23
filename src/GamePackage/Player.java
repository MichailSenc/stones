package GamePackage;

import javafx.beans.property.*;

import java.io.Serializable;

public class Player {
    private IntegerProperty countStones; //Колиество взятых камней
    private StringProperty name;         //Имя игрока
    private BooleanProperty move;        //Ходит ли ишрок в данный момент?
    private boolean bot;                 //Является ли игрок ботом?

    public Player(int countStones, String name) {
        setCountStones(countStones);
        setName(name);
    }

    public Player(int countStones, String name, boolean move, boolean bot) {
        setCountStones(countStones);
        setName(name);
        setMove(move);
        setBot(bot);
    }

    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s",getCountStones(), getName(), isMove(),isBot());
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) return false;
        if (this == object) return true;
        if (!(object instanceof Player)) return false;
        Player z = (Player) object;
        if (!this.getName().equals(z.getName())) return false;
        if (!(this.getCountStones() == z.getCountStones())) return false;
        return this.isMove() == z.isMove();
    }

    //Далее идут ТОЛЬКО сеттеры и геттеры

    public boolean isBot() {
        return bot;
    }

    public void setBot(boolean bot) {
        this.bot = bot;
    }

    public final boolean isMove() {
        return moveProperty().get();
    }

    public BooleanProperty moveProperty() {
        if (move == null)
            move = new SimpleBooleanProperty();
        return move;
    }

    public void setMove(boolean move) {
        moveProperty().set(move);
    }

    public final String getName() {
        return nameProperty().get();
    }

    public StringProperty nameProperty() {
        if (name == null)
            name = new SimpleStringProperty();
        return name;
    }

    public final void setName(String name) {
        nameProperty().set(name);
    }

    public final int getCountStones() {
        return countStonesProperty().get();
    }

    public IntegerProperty countStonesProperty() {
        if (countStones == null)
            countStones = new SimpleIntegerProperty();
        return countStones;
    }

    public final void setCountStones(int countStones) {
        countStonesProperty().set(countStones);
    }

    public final void reverseMovie(){
        setMove(!isMove());
    }
}
