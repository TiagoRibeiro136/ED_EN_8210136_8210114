package api.game;

import api.Map.Map;
import api.Structures.java.exceptions.EmptyCollectionException;
import api.Structures.java.exceptions.MapException;
import api.Structures.java.exceptions.UnknownPathException;
import api.game.algorithm.LongestPath;
import api.game.algorithm.MinimumSpanningTree;
import api.game.algorithm.ShortestPath;
import api.game.interfaces.IFlag;
import api.game.interfaces.IGame;
import api.game.interfaces.IPlayer;
import api.util.Random;


import java.util.Locale;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Game implements IGame,Runnable {

    /** The game map. */
    private Map map=new Map();
    /**
     *
     */
    public Scanner scanner = new Scanner(System.in).useLocale(Locale.US);

    /** Player 1 and Player 2 */
    private IPlayer currentPlayer, nextPlayer;

    /** Index of the player whose turn it is. */
    private int currentPlayerIndex;

    /** Winning player. */
    private IPlayer winner;

    /**
     * Constructor of the Game class.
     *
     * @param map     The game map.
     * @param currentPlayer The first player.
     * @param nextPlayer The second player.
     */
    public Game(Map map, IPlayer currentPlayer, IPlayer nextPlayer) {
        this.map = map;
        this.currentPlayer = currentPlayer;
        this.nextPlayer = nextPlayer;
        this.winner = null;

        this.currentPlayerIndex = Random.generateRandomNumber(0, 1);
    }
    public Game(){}

    /**
     * Executes a round of the game for the specified player.
     *
     * @param player The player playing the round.
     * @return The bot that played in the round.
     */
    @Override
    public Bot playRound(IPlayer player) throws EmptyCollectionException {
        Bot currentBot = null;
        int currentBotMoves;
        currentBot = player.getNextBot();
        currentBotMoves = currentBot.getCount();
        currentBot.move();


        // Check if the game has ended
        if (checkEndGame(currentBot, player)) {
            winner = player;
        }

        // Return the bot that played
        return currentBot;
    }

    /**
     * Sets the next player to play.
     *
     * @return The player who will play.
     */
    @Override
    public IPlayer nextTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % 2; // Alternating between player 1 and player 2

        if (currentPlayerIndex == 0) {
            return currentPlayer = nextPlayer;
        } else if (currentPlayerIndex == 1) {
            return currentPlayer = nextPlayer;
        } else {
            throw new RuntimeException("An error occurred while processing the player's turn");
        }
    }

    /**
     * Checks if the game has ended and who the winner is.
     *
     * @return -1 if the game hasn't ended, 0 in case of a draw, 1 if player 1 won, 2 if player 2 won.
     */
    @Override
    public int isGameOver() {
         if (winner == currentPlayer) {
            return 1;
        } else{
            return 2;
        }

    }

    /**
     * Gets the current player's turn.
     *
     * @return The current player.
     */
    @Override
    public IPlayer getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Gets the game map.
     *
     * @return The game map.
     */
    @Override
    public Map getGameMap() {
        return map;
    }

    /**
     * Checks if the game has ended, i.e., if the bot reached the enemy flag and take it to his own base.
     *
     *
     * @param bot    The bot whose position will be checked against the enemy flag.
     * @param player The player to whom the bot belongs.
     * @return true if the bot reached the enemy flag and take it back to hiw own base, false otherwise.
     */
    @Override
    public boolean checkEndGame(Bot bot, IPlayer player) {
        return player.checkEndGame(bot);
    }

    /**
     * Update Flag´s position
     * @param bot
     * @param player
     * @param flag
     * @return
     */
    @Override
    public  boolean updtateFlag(Bot bot, IPlayer player, IFlag flag) {
        return player.updateFlag(bot, flag);
    }

    /**
     * Return enemy´s flag position to their base if opponent bot is in the same position as player1 bot.
     * Return enemy´s flag position to their base if opponent bot is in the same position as player1 bot.
     * @param player1
     * @param player2
     * @throws EmptyCollectionException
     */
    @Override
    public void returnFlag(IPlayer player1, IPlayer player2) throws EmptyCollectionException {
        Flag flaginit1 = player1.getFlag();
        Flag flaginit2 = player2.getFlag();
        Flag flagEnemy1 =player1.getEnemyFlag();
        Flag flagEnemy2 = player2.getEnemyFlag();
        Bot bot1 = player1.getNextBot();
        Bot bot2 = player2.getNextBot();
        if(player1.verifyFlag(bot1,bot2)){
            player1.returnBase(flagEnemy1,flaginit2);
        }else if(player2.verifyFlag(bot2,bot1)){
            player2.returnBase(flagEnemy2,flaginit1);
        }

    }

    /**
     *
     * @param menuOption
     * @throws EmptyCollectionException
     * @throws MapException
     * @throws UnknownPathException
     */
    public void inputGame(int menuOption) throws EmptyCollectionException, MapException, UnknownPathException {
        Player player1 = new Player();
        Player player2 = new Player();
        switch (menuOption) {
            case 1:
                System.out.println("Nome jogador 1: ");
                player1.setName(scanner.next());
                System.out.println("Nome Jogador 2: ");
                player2.setName(scanner.next());
                System.out.println("(" + player1.getName() + ") Escolhe a posicao da Bandeira: ");
                player1.setFlag(new Flag(scanner.nextInt()));
                player2.setEnemyFlag(player1.getFlag());
                System.out.println("(" + player2.getName() + ") Escolhe a posicao da Bandeira: ");
                player2.setFlag(new Flag(scanner.nextInt()));
                player1.setEnemyFlag(player2.getFlag());
                System.out.println("(" + player1.getName() + ") Numero de Bots: ");
                player1.setBots(inputBots(scanner.nextInt(), player1, player2));
                System.out.println("(" + player2.getName() + ") Numero de Bots: ");
                player2.setBots(inputBots(scanner.nextInt(), player2, player1));
                Thread game = new Thread(new Game(map, player1, player2));
                game.start();
                break;
            case 2:
                int nVertices,
                        bidirecional;
                double densidade;
                do {
                    System.out.println("Bidirecional? (1-Sim | 2-Nao) :");
                    bidirecional = scanner.nextInt();
                } while (bidirecional < 1 || bidirecional > 2);
                do {
                    System.out.println("Numero de Vertices(15 - 100): ");
                    nVertices = scanner.nextInt();
                } while (nVertices < 15 || nVertices > 100);
                //do {
                System.out.println("Densidade(0.50 - 0.80): ");
                densidade = scanner.nextDouble();
                //} while (densidade < 0.50 || densidade > 0.80);
                if (bidirecional == 1) {
                    map.gerarMapaAleatorio(nVertices, true, densidade);
                } else {
                    map.gerarMapaAleatorio(nVertices, false, densidade);
                }
                break;
            case 3:
                map.exportarMapaParaArquivo("Mapa.txt");
                break;
            case 4:
                map.importarMapaDeArquivo("Mapa.txt");
                break;
        }
    }

    /**
     *
     * @param nbots
     * @param player
     * @param enemy
     * @return
     */
    public Bot[] inputBots(int nbots, Player player, Player enemy) {
        int option;
        Bot[] bots = new Bot[nbots];
        for (int i = 0; i < nbots; i++) {
            Bot bot = new Bot("[ " + player.getName() + " -> Bot " + (i + 1) + "]", enemy.getFlag());
            do {
                System.out.println("[ " + player.getName() + " -> Bot " + (i + 1) + "] Escolha o Algoritmo do bot");
                System.out.println("| 1 - Caminho mais curto | 2 - Caminho mais longo | 3 - Caminho com menos custo |");
                option = scanner.nextInt();
            } while (option < 1 || option > 3);
            switch (option) {
                case 1:
                    bot.setAlgorithm(new ShortestPath(map));
                    break;
                case 2:
                    bot.setAlgorithm(new LongestPath(map));
                    break;
                case 3:
                    bot.setAlgorithm(new MinimumSpanningTree(map));
                    break;
            }
            bots[i] = bot;
        }
        return bots;
    }


    @Override
    public void run() {
        int round = 1;
        IPlayer currentPlayer = getCurrentPlayer();
        Bot currentBot = null;
        do {
            currentPlayer = nextTurn();
            System.out.println("-------------Ronda " + round + ": -------------");
            try {
                currentBot = playRound(currentPlayer);
            } catch (EmptyCollectionException ex) {
                Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println(currentPlayer.getName() + " tinha o bot " + currentBot.getName() + " no vertice "
                    + (currentBot.getLastPosition() + 1));
            System.out.println(currentPlayer.getName() + " moveu o bot " + currentBot.getName() + " para o vertice "
                    + (currentBot.getLastPosition() + 1) + "\n");

            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            round++;
        } while (isGameOver() == -1);
        showEndGameMessages(currentPlayer);
    }

    private void showEndGameMessages(IPlayer currentPlayer) {
        System.out.println("Fim de jogo!!!");

        switch (isGameOver()) {
            case -1:
                break;
            case 0:
                System.out.println("\n-----Empate------\n");
                break;
            case 1:
            case 2:
                System.out.println("\n--------------WINNER: " + currentPlayer.getName() + " ---------------\n");
                break;

            default:
                System.out.println("Não é suposto vir para aqui");
                break;
        }

    }
}

