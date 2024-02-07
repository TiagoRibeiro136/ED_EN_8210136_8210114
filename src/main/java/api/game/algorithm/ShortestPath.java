package api.game.algorithm;

import api.Map.Map;
import api.Structures.java.Interfaces.StackADT;
import api.Structures.java.Stacks.LinkedStack.LinkedStack;
import api.Structures.java.exceptions.EmptyCollectionException;
import api.game.Bot;
import api.game.Flag;

public class ShortestPath implements IAlgorithm {
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
    public ShortestPath(Map map) {
        this.map = map;
        calculatedPath = new LinkedStack<>();
    }

    /**
     * Applies Dijkstra's algorithm to find the shortest path from the specified start vertex to the provided destination vertex
     * in the graph represented by the adjacency matrix of the {@code GameMap}.
     *
     * @param startIndex the index of the start vertex.
     * @param endIndex   the index of the destination vertex.
     * @return {@code true} if a valid path is found, {@code false} otherwise.
     */
    private boolean dijkstra(int startIndex, int endIndex) throws EmptyCollectionException {
        double[][] adjacencyMatrix = map.getAdjacencyMatrix();
        final int NO_PARENT = -1;
        int numVertices = adjacencyMatrix[0].length;

        // shortestDistances[i] will contain the shortest distance from startIndex to i
        double[] shortestDistances = new double[numVertices];

        // added[i] will be true if vertex i is included in the shortest path tree
        // or if the shortest distance from startIndex to i is finalized
        boolean[] added = new boolean[numVertices];

        // initialize all distances to INFINITY and added[] to false
        for (int vertexIndex = 0; vertexIndex < numVertices; vertexIndex++) {
            shortestDistances[vertexIndex] = Double.POSITIVE_INFINITY;
            added[vertexIndex] = false;
        }

        // Distance from the start vertex to itself is always 0
        shortestDistances[startIndex] = 0.0;

        // Array of parents to store the shortest path
        int[] parents = new int[numVertices];

        // The start vertex has no parent
        parents[startIndex] = NO_PARENT;

        // Find the shortest path to all vertices
        for (int i = 1; i < numVertices; i++) {

            // Choose the vertex with the minimum distance
            // from the set of vertices not yet processed.
            int nearestVertex = -1;
            double shortestDistance = Double.POSITIVE_INFINITY;
            for (int vertexIndex = 0; vertexIndex < numVertices; vertexIndex++) {
                if (!added[vertexIndex] && shortestDistances[vertexIndex] < shortestDistance && !hasBot(vertexIndex)) {
                    // if the vertex has not been added, the path to it is shorter than the
                    // shortest stored path and there is no bot there, save this vertex and
                    // the distance to it
                    nearestVertex = vertexIndex;
                    shortestDistance = shortestDistances[vertexIndex];
                }
            }

            // if nearestVertex remains -1, then it couldn't find a path
            if (nearestVertex == -1) {
                return false;
            }

            // Mark the chosen vertex as processed
            added[nearestVertex] = true;

            // Update the distance values of
            // the adjacent vertices of the
            // chosen vertex.
            for (int vertexIndex = 0; vertexIndex < numVertices; vertexIndex++) {
                double edgeDistance = adjacencyMatrix[nearestVertex][vertexIndex];

                // if there is no bot and edgeDistance > 0
                if (!hasBot(vertexIndex) && edgeDistance > 0
                        && (shortestDistance + edgeDistance) < shortestDistances[vertexIndex]) {
                    parents[vertexIndex] = nearestVertex;
                    shortestDistances[vertexIndex] = shortestDistance + edgeDistance;
                }
            }
        }

        // Build the path from endIndex to startIndex it contains the indices of the
        // vertices, not the content of the vertex.
        // Since it uses addToFront the path is built from startIndex to the
        // endIndex
        int currentVertexIndex = endIndex;
        while (currentVertexIndex != NO_PARENT) {
            this.calculatedPath.push(currentVertexIndex);
            currentVertexIndex = parents[currentVertexIndex];
        }

        // remove the index where the bot is from the path
        this.calculatedPath.pop();

        return true;
    }

    /**
     * Calculate a path according to the associated algorithm, if not already calculated, and returns the next index that the bot has to go to.
     *
     * @param currentIndex current vertex of the bot
     * @param endIndex     vertex to reach
     * @param currentBot   bot that is going to move
     * @return the next index for the bot to go to, if it cannot go anywhere, returns the current index
     */
    @Override
    public int getNextPosition(int currentIndex, int endIndex, Bot currentBot) throws EmptyCollectionException {
        if (calculatedPath.isEmpty()) {
            // If the calculated path is empty, calculate the path
            dijkstra(currentIndex, endIndex);
        }

        int nextIndex = currentIndex;
        while (!calculatedPath.isEmpty()) {
            int dequeuedIndex = calculatedPath.pop();

            // Check if the removed vertex contains a bot
            if (hasBot(dequeuedIndex)) {
                System.out.println("bot " + currentBot.getName() + " tried to go to index " + dequeuedIndex
                        + " but there is a bot there, so the path needs to be recalculated");

                // Recalculate the path if the removed vertex contains a bot
                if (!dijkstra(currentIndex, endIndex)) {
                    // if it failed to calculate the path it will return the current vertex
                    // break;
                    return currentIndex;
                }
                continue;
            } else {
                // If it does not contain a bot, update the bot's position in the array and return where it went
                nextIndex = dequeuedIndex;
                updateBotPosition(currentIndex, nextIndex, currentBot);
                return nextIndex;
            }
        }

        // If there are no more available movements, return the current index
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
     * @param vertex vertex where to check for the existence of a bot
     * @return true if there is a bot at that position, false otherwise
     */
    @Override
    public boolean hasBot(int vertex) {

        // if the vertex is not null, and what is there is a bot and not a flag, then return True
        return (map.getVertices()[vertex] != null && map.getVertices()[vertex] instanceof Bot);
    }
}
