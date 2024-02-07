package api.game.interfaces;

import api.Structures.java.exceptions.EmptyCollectionException;
import api.game.Flag;
import api.game.Game;
import api.game.algorithm.IAlgorithm;

public interface IBot {

    /**
     * Moves the bot using the assigned algorithm, avoiding collisions with other bots.
     *
     * @return The index where the new position of the bot is assigned.
     */
    int move() throws EmptyCollectionException;

    /**
     * Gets the movement algorithm of the bot.
     *
     * @return The movement algorithm of the bot.
     */
    IAlgorithm getAlgorithm();

    /**
     * Sets the movement algorithm of the bot.
     *
     * @param algorithm The new movement algorithm of the bot.
     */
    void setAlgorithm(IAlgorithm algorithm);

    /**
     * Gets the name of the bot.
     *
     * @return The name of the bot.
     */
    public String getName();

    /**
     * Sets the enemy flag to be reached by the bot.
     *
     * @param enemy The new enemy flag.
     */
    public void setEnemyFlag(Flag enemy);

    /**
     * Gets the number of times the bot has been moved.
     *
     * @return The number of times the bot has been moved.
     */
    public int getCount();

    /**
     * Gets the old position of the bot in the graph.
     *
     * @return The position of the bot in the graph.
     */
    int getLastPosition();
    /**
     * Gets the new position of the bot in the graph.
     *
     * @return The position of the bot in the graph.
     */
    int getCurrentPosition();

}
