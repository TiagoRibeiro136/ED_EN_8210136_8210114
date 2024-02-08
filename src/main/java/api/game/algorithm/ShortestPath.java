package api.game.algorithm;

import api.Map.Map;
import api.Structures.java.Interfaces.StackADT;
import api.Structures.java.Stacks.LinkedStack.LinkedStack;
import api.Structures.java.exceptions.EmptyCollectionException;
import api.game.Bot;
import api.game.Flag;

public class ShortestPath implements IAlgorithm{
    private Map map;

    private StackADT<Integer> calculatedPath;

    public ShortestPath(Map map) {
        this.map = map;
        calculatedPath = new LinkedStack<>();
    }

    private boolean dijkstra(int startIndex, int endIndex) throws EmptyCollectionException {
        double[][] adjacencyMatrix = map.getAdjacencyMatrix();
        final int NO_PARENT = -1;
        int numVertices = adjacencyMatrix[0].length;

        double[] shortestDistances = new double[numVertices];

        boolean[] added = new boolean[numVertices];

        for (int vertexIndex = 0; vertexIndex < numVertices; vertexIndex++) {
            shortestDistances[vertexIndex] = Double.POSITIVE_INFINITY;
            added[vertexIndex] = false;
        }

        shortestDistances[startIndex] = 0.0;

        int[] parents = new int[numVertices];

        parents[startIndex] = NO_PARENT;

        for (int i = 1; i < numVertices; i++) {

            int nearestVertex = -1;
            double shortestDistance = Double.POSITIVE_INFINITY;
            for (int vertexIndex = 0; vertexIndex < numVertices; vertexIndex++) {
                if (!added[vertexIndex] && shortestDistances[vertexIndex] < shortestDistance && !hasBot(vertexIndex)) {
                    nearestVertex = vertexIndex;
                    shortestDistance = shortestDistances[vertexIndex];
                }
            }

            if (nearestVertex == -1) {
                return false;
            }

            added[nearestVertex] = true;

            for (int vertexIndex = 0; vertexIndex < numVertices; vertexIndex++) {
                double edgeDistance = adjacencyMatrix[nearestVertex][vertexIndex];

                if (!hasBot(vertexIndex) && edgeDistance > 0
                        && (shortestDistance + edgeDistance) < shortestDistances[vertexIndex]) {
                    parents[vertexIndex] = nearestVertex;
                    shortestDistances[vertexIndex] = shortestDistance + edgeDistance;
                }
            }
        }

        int currentVertexIndex = endIndex;
        while (currentVertexIndex != NO_PARENT) {
            this.calculatedPath.push(currentVertexIndex);
            currentVertexIndex = parents[currentVertexIndex];
        }

        this.calculatedPath.pop();

        return true;
    }

    @Override
    public int getNextPosition(int currentIndex, int endIndex, Bot currentBot) throws EmptyCollectionException {
        if (calculatedPath.isEmpty()) {
            dijkstra(currentIndex, endIndex);
        }

        int nextIndex = currentIndex;
        while (!calculatedPath.isEmpty()) {
            int dequeuedIndex = calculatedPath.pop();

            if (hasBot(dequeuedIndex)) {
                System.out.println("bot " + currentBot.getName() + " tentou ir para o índice " + dequeuedIndex
                        + " mas tem lá um bot, então vai ter de se recalcular o caminho");

                if (!dijkstra(currentIndex, endIndex)) {
                    
                    return currentIndex;
                }
                continue;
            } else {
               
                nextIndex = dequeuedIndex;
                updateBotPosition(currentIndex, nextIndex, currentBot);
                return nextIndex;
            }
        }

        return currentIndex;
    }


    @Override
    public void updateBotPosition(int currentIndex, int nextIndex, Bot bot) {

        if (currentIndex != nextIndex) {
            if (map.getVertices()[currentIndex] instanceof Flag) {
                map.setVertice(nextIndex, bot);
            } else {
                map.setVertice(currentIndex, null);
                map.setVertice(nextIndex, bot);
            }
        }
    }

    @Override
    public boolean hasBot(int vertex) {
        return (map.getVertices()[vertex] != null && map.getVertices()[vertex] instanceof Bot);
    }
}
