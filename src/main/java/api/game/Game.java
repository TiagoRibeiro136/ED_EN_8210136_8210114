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

public class Game implements IGame, Runnable {

    /**
     * The game map.
     */
    private Map map = new Map();
    /**
     *
     */
    public Scanner scanner = new Scanner(System.in).useLocale(Locale.US);

    /**
     * Player 1 and Player 2
     */
    private IPlayer currentPlayer, nextPlayer;

    /**
     * Index of the player whose turn it is.
     */
    private int currentPlayerIndex;

    /**
     * Winning player.
     */
    private IPlayer winner;

    /**
     * Constructor of the Game class.
     *
     * @param map The game map.
     * @param currentPlayer The first player.
     * @param nextPlayer The second player.
     */
    public Game(Map map, Player currentPlayer, Player nextPlayer) {
        this.map = map;
        this.currentPlayer = currentPlayer;
        this.nextPlayer = nextPlayer;
        this.winner = null;
        this.map.setVertice(currentPlayer.getEnemyFlag().getIndex(), currentPlayer.getEnemyFlag());
        this.map.setVertice(nextPlayer.getEnemyFlag().getIndex(), nextPlayer.getEnemyFlag());
        this.currentPlayerIndex = Random.generateRandomNumber(0, 1);
    }

    public Game() {
    }

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
        if (currentBotMoves == currentBot.getCount()) {
            player.incrementStuckCount();
        } else {
            player.decrementStuckCount();
        }
        /*
        if (player.verifyFlag(currentBot, enemy) == true) {
            returnFlag(player);
        }
         */
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

        IPlayer playerTurn = null;

        if (currentPlayerIndex == 0) {
            playerTurn = currentPlayer;
        } else if (currentPlayerIndex == 1) {
            playerTurn = nextPlayer;
        } else {
            throw new RuntimeException("Ocurreu um erro ao processar a vez do jogador");
        }

        return playerTurn;
    }

    /**
     * Checks if the game has ended and who the winner is.
     *
     * @return -1 if the game hasn't ended, 0 in case of a draw, 1 if player 1
     * won, 2 if player 2 won.
     */
    @Override
    public int isGameOver() {
        if (currentPlayer.isStuckCountReached() && nextPlayer.isStuckCountReached()) {
            return 0;
        } else if (winner == currentPlayer) {
            return 1;
        } else if (winner == nextPlayer) {
            return 2;
        }
        return -1;
    }

    /**
     * Gets the current player's turn.
     *
     * @return The current player.
     */
    @Override
    public IPlayer getCurrentPlayer() {
        IPlayer playerTurn = null;

        if (currentPlayerIndex == 0) {
            playerTurn = currentPlayer;
        } else if (currentPlayerIndex == 1) {
            playerTurn = nextPlayer;
        } else {
            throw new RuntimeException("Ocurreu um erro ao processar a vez do jogador");
        }

        return playerTurn;
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
     * Checks if the game has ended, i.e., if the bot reached the enemy flag.
     * Topic 8 - The game ends when one of the bots reaches the opponent's
     * field.
     *
     * @param bot The bot whose position will be checked against the enemy flag.
     * @param player The player to whom the bot belongs.
     * @return true if the bot reached the enemy flag, false otherwise.
     */
    @Override
    public boolean checkEndGame(Bot bot, IPlayer player) {
        return player.checkEndGame(bot);
    }

    /**
     * Update Flag´s position
     *
     * @param bot
     * @param player
     * @param flag
     * @return
     */
    @Override
    public boolean updateFlag(Bot bot, IPlayer player, IFlag flag) {
        return player.updateFlag(bot, flag);
    }

    /**
     * Return enemy´s flag position to their base if opponent bot is in the same
     * position as player1 bot. Return enemy´s flag position to their base if
     * opponent bot is in the same position as player1 bot.
     *
     * @param player1
     * @param player2
     * @throws EmptyCollectionException
     */
    @Override
    public void returnFlag(IPlayer player) throws EmptyCollectionException {
        player.returnBase(player.getEnemyFlag(), player.getEnemyFlag().getIndex());
    }

    public void inputMenu(int menuOption) throws EmptyCollectionException, MapException, UnknownPathException {
        Scanner scanner = new Scanner(System.in).useLocale(Locale.US);
        switch (menuOption) {
            case 1:
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
            case 2:
                map.importarMapaDeArquivo("Mapa.txt");
                break;
        }
    }

    public void inputGame(int menuOption) throws EmptyCollectionException, MapException, UnknownPathException {
        String p1Name, p2Name;
        Flag p1, p2, enemyP1, enemyP2;

        switch (menuOption) {
            case 1:
                System.out.println("Nome jogador 1: ");
                p1Name = scanner.next();
                System.out.println("Nome Jogador 2: ");
                p2Name = scanner.next();
                System.out.println("(" + p1Name + ") Escolhe a posicao da Bandeira: ");
                p1 = new Flag(scanner.nextInt());
                enemyP2 = p1;
                System.out.println("(" + p2Name + ") Escolhe a posicao da Bandeira: ");
                p2 = new Flag(scanner.nextInt());
                enemyP1 = p2;
                System.out.println("(" + p1Name + ") Numero de Bots: ");
                Bot[] p1bots = inputBots(scanner.nextInt(), p1Name, enemyP1);
                Player formatedPlayer1 = new Player(p1Name, p1, enemyP1, p1bots);
                System.out.println("(" + p2Name + ") Numero de Bots: ");
                Bot[] p2bots = inputBots(scanner.nextInt(), p2Name, enemyP2);
                Player formatedPlayer2 = new Player(p2Name, p2, enemyP2, p2bots);
                Thread game = new Thread(new Game(map, formatedPlayer1, formatedPlayer2));
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

    public Bot[] inputBots(int nbots, String name, Flag enemy) {
        int option;
        Bot[] bots = new Bot[nbots];
        for (int i = 0; i < nbots; i++) {
            Bot bot = new Bot();
            do {
                System.out.println("[ " + name + " -> Bot " + (i + 1) + "] Escolha o Algoritmo do bot");
                System.out.println("| 1 - Caminho mais curto | 2 - Caminho mais longo | 3 - Caminho com menos custo |");
                option = scanner.nextInt();
            } while (option < 1 || option > 3);
            switch (option) {
                case 1:
                    bot = new Bot("[ " + name + " -> Bot " + (i + 1) + "]", new ShortestPath(map), enemy);
                    break;
                case 2:
                    bot = new Bot("[ " + name + " -> Bot " + (i + 1) + "]", new LongestPath(map), enemy);
                    break;
                case 3:
                    bot = new Bot("[ " + name + " -> Bot " + (i + 1) + "]", new MinimumSpanningTree(map), enemy);
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
            System.out.println("[" + currentPlayer.getName() + "] -> (" + currentBot.getName() + ") no vertice "
                    + (currentBot.getLastPosition() + 1));
            System.out.println("[" + currentPlayer.getName() + "] -> (" + currentBot.getName() + ") para o vertice "
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
        System.out.println("Fim de jogo!");

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
                System.out.println("");
                break;
        }

    }
}
