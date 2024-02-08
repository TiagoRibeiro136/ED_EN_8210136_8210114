package api.game.interfaces;

import api.Structures.java.exceptions.EmptyCollectionException;
import api.game.Bot;
import api.game.Flag;
import api.game.Player;

public interface IPlayer {

    /**
     * Gets the name of the player.
     *
     * @return The name of the player.
     */
    String getName();

    /**
     * Gets the player's flag.
     *
     * @return The player's flag.
     */
    Flag getFlag();

    /**
     * Gets the enemy player's flag.
     *
     * @return The enemy player's flag.
     */
    Flag getEnemyFlag();

    /**
     *
     * @param bot
     * @return true if the bot reached his own base with the enemy´s flag.
     */
    boolean conquerFlag(Bot bot);

    boolean updateFlag(Bot bot, IFlag flag);

    boolean verifyFlag(Bot bot, Player player) throws EmptyCollectionException;

    void returnBase(IFlag flag, int position);

    /**
     * Checks if the game has ended, i.e., if the bot reached the enemy flag and
     * take it to the base.
     *
     * @param bot The bot whose position will be checked against the enemy flag.
     * @return true if the bot reached the enemy flag, false otherwise.
     */
    boolean checkEndGame(Bot bot);

    /**
     * Gets the next bot in the queue.
     *
     * @return The next bot in the queue.
     */
    Bot getNextBot() throws EmptyCollectionException;

    boolean isStuckCountReached();

    void decrementStuckCount();

    void incrementStuckCount();
    
    int getStuckCount();
            
            /**
     * Sets the name of the player.
     *
     * @param name The new name of the player.
     */

    void setName(String name);

}
