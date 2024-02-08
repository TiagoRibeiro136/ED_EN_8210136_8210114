package api.game;

import api.Structures.java.exceptions.EmptyCollectionException;
import api.game.algorithm.IAlgorithm;
import api.game.interfaces.IBot;
import api.game.interfaces.IFlag;

public class Bot implements IBot, IFlag {

    /**
     * bot name
     */
    private String name;
    /**
     * bot last index on graph
     */
    private int lastPosition;
    /**
     * bot index on graph
     */
    private int currentPosition;

    /**
     * bot algorithm
     */
    private IAlgorithm algorithm;
    /**
     * bot enemy flag
     */
    private Flag enemy;

    /**
     *
     */
    private int count;
    private boolean hasflag;

    /**
     *
     * @param name
     * @param algorithm
     * @param enemy
     */
    public Bot(String name, IAlgorithm algorithm, Flag enemy) {
        this.name = name;
        this.algorithm = algorithm;
        this.enemy = enemy;
        this.hasflag = false;
        this.count = 0;
    }

    public Bot(String name, Flag enemy) {
        this.name = name;
        this.enemy = enemy;
        this.hasflag = false;
    }

    public Bot() {
    }

    /**
     * move bot using chosed algorithm.
     *
     * @return new bot position;
     */
    @Override
    public int move() throws EmptyCollectionException {
        if (algorithm != null) {
            int newPosition = algorithm.getNextPosition(currentPosition, enemy.getIndex(), this);
            if (newPosition != currentPosition) {
                count++;
                setPosition(newPosition);
            }
            return currentPosition;
        }
        return -1;
    }

    /**
     * Get new bot algorithm .
     *
     * @return algorithm.
     */
    @Override
    public IAlgorithm getAlgorithm() {
        return algorithm;
    }

    /**
     * Choose new bot algorithm.
     *
     * @param algorithm The new movement algorithm of the bot.
     */
    @Override
    public void setAlgorithm(IAlgorithm algorithm) {
        this.algorithm = algorithm;

    }

    /**
     * get bot name.
     *
     * @return bot name.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * set enemy flag.
     *
     * @param enemy The new enemy flag.
     */
    @Override
    public void setEnemyFlag(Flag enemy) {
        this.enemy = enemy;

    }

    /**
     * Get number of times that bot moves.
     *
     * @return number of bot movement.
     */
    @Override
    public int getCount() {
        return count;
    }

    /**
     * get last bot position.
     *
     * @return last bot position.
     */
    @Override
    public int getLastPosition() {
        return lastPosition;
    }

    /**
     * Bot position on graph;
     *
     * @param currentPosition .
     */
    public void setPosition(int currentPosition) {
        if (count == 0) {
            this.lastPosition = currentPosition;
            this.currentPosition = currentPosition;
        } else {
            this.lastPosition = this.currentPosition;
            this.currentPosition = currentPosition;
        }
    }

    /**
     * get bot current position on graph.
     *
     * @return bot current position on graph.
     */
    @Override
    public int getCurrentPosition() {
        return currentPosition;
    }

    public int VerifyFlag() {
        if (!hasflag) {
            return 0;
        } else {
            return 1;
        }
    }

    public boolean Hasflag(Bot bot) {
        if (bot.currentPosition == enemy.getIndex()) {
            bot.hasflag = true;
            return true;
        }
        return false;
    }

    /**
     * @return
     */
    @Override
    public int getIndex() {
        return currentPosition;
    }

    /**
     * @param index The new position of the entity.
     */
    @Override
    public void setIndex(int index) {
        if (count == 0) {
            this.lastPosition = index;
            this.currentPosition = index;
        } else {
            this.lastPosition = this.currentPosition;
            this.currentPosition = index;
        }

    }
}
