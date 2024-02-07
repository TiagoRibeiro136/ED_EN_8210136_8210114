package api.game.algorithm;

import api.Map.Map;
import api.Structures.java.Interfaces.StackADT;
import api.Structures.java.Stacks.LinkedStack.LinkedStack;
import api.Structures.java.exceptions.EmptyCollectionException;
import api.game.Bot;
import api.game.Flag;

public class LongestPath implements IAlgorithm{
    /** Game map where the algorithm operates. */
    private Map map;

    /**
     * Stack to store the calculated path. Stores the indices where the bot needs to move to.
     * Since the algorithm returns the shortest path from y to x and not from x to y, a stack is the best way to store the path.
     */
    private StackADT<Integer> calculatedPath;

    /**
     * Constructor that receives the game map.
     *
     * @param map game map
     */
    public LongestPath(Map map) {
        this.map = map;
        calculatedPath = new LinkedStack<Integer>();
    }

    /**
     * Private method to perform a depth-first search (DFS) to find the longest path.
     *
     * @param startVertex starting vertex
     * @param endVertex   vertex to reach
     * @return true if it managed to find a path, false otherwise
     */
    private boolean longestPathDFS(int startVertex, int endVertex) {
        boolean[] visited = new boolean[map.getVertices().length];
        return longestPathDFSUtil(startVertex, endVertex, visited);
    }

    /**
     * Recursive helper function of the {@code longestPathDFS} for depth-first search (DFS).
     *
     * @param currentVertex current vertex
     * @param endVertex     vertex to reach
     * @param visited       vertices already visited
     * @return true if it managed to find a path, false otherwise
     */
    private boolean longestPathDFSUtil(int currentVertex, int endVertex, boolean[] visited) {
        visited[currentVertex] = true;

        // If it reaches the destination vertex, return true to stop the recursion.
        if (currentVertex == endVertex) {
            return true;
        }

        boolean pathFound = false;

        // Iterates over the neighbors of the current vertex.
        for (int neighbor = 0; neighbor < map.getVertices().length; neighbor++) {
            // Checks if the neighbor has not been visited, does not contain a bot, and there is an edge between them.
            if (!visited[neighbor] && !hasBot(neighbor) && map.getAdjacencyMatrix()[currentVertex][neighbor] > 0) {
                // If it finds a path, adds the current vertex to the calculated path stack.
                if (longestPathDFSUtil(neighbor, endVertex, visited)) {
                    calculatedPath.push(neighbor);
                    pathFound = true;
                }
            }
        }

        return pathFound;
    }

    /**
     * Calculates a path according to the associated algorithm, if not already calculated, and returns the next index that the bot has to go to.
     *
     * @param currentIndex current vertex of the bot
     * @param endIndex     vertex to reach
     * @param currentBot   bot that is going to move
     * @return the next index for the bot to go to, if it cannot go anywhere, returns the current index
     */
    @Override
    public int getNextPosition(int currentIndex, int endIndex, Bot currentBot) throws EmptyCollectionException {
        // If the calculated path stack is empty, calculate the longest path.
        if (calculatedPath.isEmpty()) {
            longestPathDFS(currentIndex, endIndex);
        }

        int nextIndex = currentIndex;
        while (!calculatedPath.isEmpty()) {
            int dequeuedIndex = calculatedPath.pop();

            // Checks if the removed vertex contains a bot
            if (hasBot(dequeuedIndex)) {
                System.out.println("bot " + currentBot.getName() + " tried to go to index " + dequeuedIndex
                        + " but there is a bot there, so the path needs to be recalculated");

                // Recalculates the path if the removed vertex contains a bot
                if (!longestPathDFS(currentIndex, endIndex)) {
                    // if it failed to calculate the path it will return the current vertex
                    // break;
                    return currentIndex;
                }
                continue;
            } else {
                // If it does not contain a bot, it will update the bot's position in the array and return where it went
                nextIndex = dequeuedIndex;
                updateBotPosition(currentIndex, nextIndex, currentBot);
                return nextIndex;
            }
        }

        return currentIndex;
    }

    /**
     * Updates the bot's position on the map. Updating on the map means updating in the superclass's vertex array.
     *
     * @param currentIndex current vertex of the bot
     * @param nextIndex    vertex where the bot is going
     * @param bot          bot that is going to move
     */
    @Override
    public void updateBotPosition(int currentIndex, int nextIndex, Bot bot) {
        // If the current index is different from the next index, updates the bot's position on the map.
        if (currentIndex != nextIndex) {

            if (map.getVertices()[currentIndex] instanceof Flag) {
                // if it is on top of the flag, does not set the previous vertex to null to not delete the flag.
                map.setVertice(nextIndex, bot);
            } else {
                map.setVertice(currentIndex, null);
                map.setVertice(nextIndex, bot);
            }
        }
    }

    /**
     * Checks if there is a bot at the given index.
     *
     * @param vertex vertex where the existence of a bot will be checked
     * @return true if there is a bot at this position, false otherwise
     */
    @Override
    public boolean hasBot(int vertex) {
        return (map.getVertices()[vertex] != null && map.getVertices()[vertex] instanceof Bot);
    }

}
