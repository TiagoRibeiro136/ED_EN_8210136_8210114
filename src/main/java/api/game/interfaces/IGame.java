package api.game.interfaces;

import api.Map.Map;
import api.Structures.java.exceptions.EmptyCollectionException;
import api.game.Bot;

public interface IGame {

    /**
     * Executes a round of the game for the specified player.
     *
     * @param player The player who is playing the round.
     * @return The bot that played in the round.
     */
    Bot playRound(IPlayer player) throws EmptyCollectionException;

    /**
     * Sets the next player to play.
     *
     * @return The player who will play.
     */
    IPlayer nextTurn();

    /**
     * Checks if the game has ended and who is the winner.
     *
     * @return -1 if the game is not over, 0 in case of a draw, 1 if player 1 won, 2 if player 2 won.
     */
    int isGameOver();

    /**
     * Gets the current player's turn.
     *
     * @return The current player.
     */
    IPlayer getCurrentPlayer();

    /**
     * Gets the game map.
     *
     * @return The game map.
     */
    Map getGameMap();

    boolean checkEndGame(Bot bot, IPlayer player);

    boolean updtateFlag(Bot bot, IPlayer player, IFlag flag);

    void returnFlag(IPlayer player1, IPlayer player2) throws EmptyCollectionException;
}

