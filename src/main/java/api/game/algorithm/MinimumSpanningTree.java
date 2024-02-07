package api.game.algorithm;

import api.Map.Map;
import api.Structures.java.Interfaces.StackADT;
import api.Structures.java.Stacks.LinkedStack.LinkedStack;
import api.Structures.java.exceptions.EmptyCollectionException;
import api.game.Bot;
import api.game.algorithm.IAlgorithm;

/**
 * Implementation of Minimum Spanning Tree algorithm.
 */
public class MinimumSpanningTree implements IAlgorithm {
    /** Game map where the algorithm operates. */
    private Map map;

    /**
     * Stack to store the calculated path. Stores the indices where the bot needs to move to.
     * Since the algorithm returns the minimum spanning tree, a stack is used to store the path.
     */
    private StackADT<Integer> calculatedPath;

    /**
     * Constructor that receives the game map.
     *
     * @param map game map
     */
    public MinimumSpanningTree(Map map) {
        this.map = map;
        calculatedPath = new LinkedStack<>();
    }

    /**
     * Performs the Minimum Spanning Tree algorithm.
     *
     * @param startIndex starting vertex
     * @param endIndex   vertex to reach
     * @return true if it managed to find a path, false otherwise
     */
    private boolean minimumSpanningTree(int startIndex, int endIndex) {
        boolean[] visited = new boolean[map.getVertices().length];
        return minimumSpanningTreeUtil(startIndex, endIndex, visited);
    }

    /**
     * Recursive helper function of the Minimum Spanning Tree algorithm.
     *
     * @param currentVertex current vertex
     * @param endVertex     vertex to reach
     * @param visited       vertices already visited
     * @return true if it managed to find a path, false otherwise
     */
    private boolean minimumSpanningTreeUtil(int currentVertex, int endVertex, boolean[] visited) {
        visited[currentVertex] = true;

        // If it reaches the destination vertex, return true to stop the recursion.
        if (currentVertex == endVertex) {
            return true;
        }

        boolean pathFound = false;

        // Iterates over the neighbors of the current vertex.
        for (int neighbor = 0; neighbor < map.getVertices().length; neighbor++) {
            // Checks if the neighbor has not been visited and there is an edge between them.
            if (!visited[neighbor] && map.getAdjacencyMatrix()[currentVertex][neighbor] > 0) {
                // If it finds a path, adds the current vertex to the calculated path stack.
                if (minimumSpanningTreeUtil(neighbor, endVertex, visited)) {
                    calculatedPath.push(neighbor);
                    pathFound = true;
                }
            }
        }

        return pathFound;
    }

    /**
     * Calculates the next position for the bot to move based on the Minimum Spanning Tree.
     *
     * @param currentIndex current vertex of the bot
     * @param endIndex     vertex to reach
     * @param currentBot   bot that is going to move
     * @return the next index for the bot to go to, if it cannot go anywhere, returns the current index
     * @throws EmptyCollectionException if the calculated path stack is empty
     */
    @Override
    public int getNextPosition(int currentIndex, int endIndex, Bot currentBot) throws EmptyCollectionException {
        // If the calculated path stack is empty, calculate the minimum spanning tree.
        if (calculatedPath.isEmpty()) {
            minimumSpanningTree(currentIndex, endIndex);
        }

        int nextIndex = currentIndex;
        while (!calculatedPath.isEmpty()) {
            int dequeuedIndex = calculatedPath.pop();

            // If the dequeued index is not the current index, update bot position and return next index.
            if (dequeuedIndex != currentIndex) {
                updateBotPosition(currentIndex, dequeuedIndex, currentBot);
                nextIndex = dequeuedIndex;
                break;
            }
        }

        return nextIndex;
    }

    /**
     * Updates the bot's position on the map.
     *
     * @param currentIndex current vertex of the bot
     * @param nextIndex    vertex where the bot is going
     * @param bot          bot that is going to move
     */
    @Override
    public void updateBotPosition(int currentIndex, int nextIndex, Bot bot) {
        // Update bot position on the map.
        map.setVertice(currentIndex, null);
        map.setVertice(nextIndex, bot);
    }

    /**
     * Checks if there is a bot at the given index.
     *
     * @param vertex vertex where the existence of a bot will be checked
     * @return true if there is a bot at this position, false otherwise
     */
    @Override
    public boolean hasBot(int vertex) {
        // Tópico 7 - Excetuando na localização das bandeiras, um bot não se pode
        // movimentar para uma posição em que esteja outro bot.

        // se o vértice for diferente de null, e o que estiver lá for um bot e não uma
        // flag, ent retorna True
        return (map.getVertices()[vertex] != null && map.getVertices()[vertex] instanceof Bot);
    }
}
