package api.game;

import api.Structures.java.Interfaces.QueueADT;
import api.Structures.java.Queues.LinkedQueue.LinkedQueue;
import api.Structures.java.exceptions.EmptyCollectionException;
import api.game.interfaces.IFlag;
import api.game.interfaces.IPlayer;


public class Player implements IPlayer {

    /** Player name. */
    private String name;

    /** Bots Queue that defines who´s the next bot playing. */
    private QueueADT<Bot> bots;

    /** Player´s flag. */
    private Flag flag;

    /** Enemy´s flag. */
    private Flag enemyFlag;

    public Player(String name, Flag flag, Flag enemyFlag, Bot[] bots) {
        this.name = name;
        this.bots = new LinkedQueue<>();
        this.flag = flag;
        this.enemyFlag = enemyFlag;
        assignBot(bots);
        enqueueBots(bots);

    }
    public Player(){}
    /**
     * Assigns the initial position to all player's bots based on the flag's position.
     * At the beginning of the game, all bots should be located at the same position as their player's flag.
     *
     * @param bots The array of bots to be positioned.
     * @return The array of bots with initial positions assigned.
     */
    private Bot[] assignBot(Bot[] bots) {
        int flagPosition = flag.getIndex();

        for (Bot bot : bots) {
            bot.setPosition(flagPosition);
        }

        return bots;
    }

    /**
     * Enqueues the player's associated bots into the queue.
     *
     * @param bots The array of bots associated with the player.
     */
    private void enqueueBots(Bot[] bots) {
        for (Bot bot : bots) {
            this.bots.enqueue(bot);
        }
    }

    /**
     * Gets the name of the player.
     *
     * @return The name of the player.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Gets the player's flag.
     *
     * @return The player's flag.
     */
    @Override
    public Flag getFlag() {
        return flag;
    }

    /**
     * Gets the enemy player's flag.
     *
     * @return The enemy player's flag.
     */
    @Override
    public Flag getEnemyFlag() {
        return enemyFlag;
    }

    /**
     * Associate enemy flag with the bot who conquer it
     * @param bot
     * @return
     */
    @Override
    public boolean conquerFlag(Bot bot) {
        int enemyFlagPosition = enemyFlag.getIndex();
        bot.Hasflag(bot,true);
        return bot.getCurrentPosition() == enemyFlagPosition;
    }

    /**
     * Update the flag position
     * @param bot
     * @param flag
     * @return
     */
    @Override
    public boolean updateFlag(Bot bot, IFlag flag){
        int botPos = bot.getCurrentPosition();
        enemyFlag.setIndex(botPos) ;
        return true;
    }

    /**
     * Verifies if bot who contains the enemy flag is in the same position as the enemy bot
     * @param bot
     * @param bot2
     * @return
     */
    @Override
    public boolean verifyFlag(Bot bot, Bot bot2){
        int botpos1 = bot.getCurrentPosition();
        int botpos2 = bot.getCurrentPosition();
        if(bot.Hasflag(bot,true)||botpos1==botpos2){
            bot.Hasflag(bot,false);
        }
        return true;
    }

    /**
     * Return the flag to the origin base
     * @param flag
     * @param position
     */
    @Override
    public void returnBase(IFlag flag, Flag position){
        flag.setIndex(position.getIndex());
    }



    /**
     * Checks if the game has ended, i.e., if the bot reached the enemy flag.
     * The game ends when one of the bots reaches the opponent's field and come back to his own base.
     *
     * @param bot The bot whose position will be checked against the enemy flag.
     * @return true if the bot reached the enemy flag and come back to his own base, false otherwise.
     */
    @Override
    public boolean checkEndGame(Bot bot) {
        if (conquerFlag(bot) && bot.getCurrentPosition() == flag.getIndex()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Gets the next bot in the queue.
     *
     * @return The next bot in the queue.
     */
    @Override
    public Bot getNextBot() throws EmptyCollectionException {
        Bot currentBot = this.bots.dequeue();

        this.bots.enqueue(currentBot);

        return currentBot;
    }

    /**
     * Sets the name of the player.
     *
     * @param name The new name of the player.
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }
    public void setBots(Bot[] bots) {
        this.bots = new LinkedQueue<>();
        enqueueBots(bots);
    }
    public void setFlag(Flag flag) {
        this.flag = flag;
    }
    public void setEnemyFlag(Flag flag) {
        this.enemyFlag = flag;
    }

}
