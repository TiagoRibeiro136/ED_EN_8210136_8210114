package api.game.algorithm;

import api.Structures.java.exceptions.EmptyCollectionException;
import api.game.Bot;

public interface IAlgorithm {
    /**
     * Calculates a path according to the associated algorithm, if not already calculated,
     * and returns the next index that the bot has to go to.
     *
     * @param currentIndex
     * @param endIndex
     * @param currentBot
     * @return The next index for the bot to go to; if it cannot go anywhere, returns the current index.
     */
    public int getNextPosition(int currentIndex, int endIndex, Bot currentBot) throws EmptyCollectionException;

    /**
     * Updates the bot's position on the map. Updating on the map means updating in the superclass's vertex array.
     *
     * @param currentIndex
     * @param nextIndex
     * @param bot
     */
    public void updateBotPosition(int currentIndex, int nextIndex, Bot bot);

    /**
     * Checks if there is a bot at the given index.
     *
     * @param vertex
     * @return true if there is a bot at this position, false otherwise.
     */
    public boolean hasBot(int vertex);

}
