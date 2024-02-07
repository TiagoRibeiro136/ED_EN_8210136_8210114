package api.Structures.java.Graphs;

import java.util.Iterator;

import api.Structures.java.Heaps.LinkedHeap;
import api.Structures.java.Interfaces.HeapADT;
import api.Structures.java.Interfaces.NetworkADT;
import api.Structures.java.Interfaces.UnorderedListADT;
import api.Structures.java.Lists.UnorderedLists.ArrayUnorderedList;
import api.Structures.java.PriorityQueue.PriorityQueue;
import api.Structures.java.Queues.LinkedQueue.LinkedQueue;
import api.Structures.java.Stacks.LinkedStack.LinkedStack;
import api.Structures.java.exceptions.EmptyCollectionException;
import api.Structures.java.exceptions.UnknownPathException;

public class Network<T> extends MatrixGraph<T> implements NetworkADT<T> {
    protected double[][] adjMatrix;

    public Network() {
        numVertices = 0;
        this.adjMatrix = new double[this.DEFAULT_CAPACITY][this.DEFAULT_CAPACITY];
        this.vertices = (T[]) (new Object[DEFAULT_CAPACITY]);
    }

    @Override
    public void addVertex(T vertex) {
        if (super.size() == super.vertices.length) {
            this.expandCapacity();
        }

        super.vertices[super.size()] = vertex;

        for (int i = 0; i <= this.size(); i++) {
            this.adjMatrix[numVertices][i] = Double.POSITIVE_INFINITY;
            this.adjMatrix[i][numVertices] = Double.POSITIVE_INFINITY;
        }
        super.numVertices++;
    }

    @Override
    public void addEdge(T vertex1, T vertex2, double weight) {
        this.addEdge(super.getIndex(vertex1), super.getIndex(vertex2), weight);
    }

    @Override
    public void addEdge(T vertex1, T vertex2) {
        this.addEdge(super.getIndex(vertex1), super.getIndex(vertex2), 0);
    }

    private void addEdge(int index1, int index2, double weight) {
        if (super.indexIsValid(index1) && super.indexIsValid(index2)) {
            this.adjMatrix[index1][index2] = weight;
            this.adjMatrix[index2][index1] = weight;
        }
    }
    @Override
    public void removeEdge(T vertex1, T vertex2) {
        this.removeEdge(super.getIndex(vertex1), super.getIndex(vertex2));
    }

    public void removeEdge(int index1, int index2) {
        if (super.indexIsValid(index1) && super.indexIsValid(index2)) {
            this.adjMatrix[index1][index2] = Double.POSITIVE_INFINITY;
            this.adjMatrix[index2][index1] = Double.POSITIVE_INFINITY;
        }
    }

    @Override
    public void removeVertex(T vertex) {
        this.removeVertex(super.getIndex(vertex));
    }

    public void removeVertex(int index) {
        if (this.indexIsValid(index)) {
            this.numVertices--;

            for (int i = index; i < super.size(); i++) {
                this.vertices[i] = this.vertices[i + 1];
            }

            for (int i = index; i < super.size(); i++) {
                for (int j = 0; j <= super.size(); j++) {
                    this.adjMatrix[i][j] = this.adjMatrix[i + 1][j];
                }
            }

            for (int i = index; i < super.size(); i++) {
                for (int j = 0; j < super.size(); j++) {
                    this.adjMatrix[j][i] = this.adjMatrix[j][i + 1];
                }
            }
        }
    }

    public Iterator<T> iteratorBFS(T startVertex) throws EmptyCollectionException {
        return this.iteratorBFS(super.getIndex(startVertex));
    }

    public Iterator<T> iteratorBFS(int startIndex) throws EmptyCollectionException {
        Integer x;
        LinkedQueue<Integer> traversalQueue = new LinkedQueue<>();
        ArrayUnorderedList<T> resultList = new ArrayUnorderedList<>();

        if (!this.indexIsValid(startIndex)) {
            return resultList.iterator();
        }
        boolean[] visited = new boolean[numVertices];
        for (int i = 0; i < numVertices; i++) {
            visited[i] = false;
        }

        traversalQueue.enqueue(new Integer(startIndex));
        visited[startIndex] = true;

        while (!traversalQueue.isEmpty()) {
            x = traversalQueue.dequeue();
            resultList.addToRear(this.vertices[x.intValue()]);
            /**
             * Find all vertices adjacent to x that have not been visited and
             * queue them up
             */
            for (int i = 0; i < numVertices; i++) {
                if ((this.adjMatrix[x.intValue()][i] < Double.POSITIVE_INFINITY) && !visited[i]) {
                    traversalQueue.enqueue(new Integer(i));
                    visited[i] = true;
                }
            }
        }
        return resultList.iterator();
    }

    public Iterator<T> iteratorDFS(T startVertex) throws EmptyCollectionException {
        return this.iteratorDFS(super.getIndex(startVertex));
    }

    public Iterator<T> iteratorDFS(int startIndex) throws EmptyCollectionException {
        Integer x;
        boolean found;
        LinkedStack<Integer> traversalStack = new LinkedStack<Integer>();
        ArrayUnorderedList<T> resultList = new ArrayUnorderedList<T>();
        boolean[] visited = new boolean[numVertices];

        if (!indexIsValid(startIndex)) {
            return resultList.iterator();
        }

        for (int i = 0; i < numVertices; i++) {
            visited[i] = false;
        }
        traversalStack.push(new Integer(startIndex));
        resultList.addToRear(vertices[startIndex]);
        visited[startIndex] = true;

        while (!traversalStack.isEmpty()) {

            x = traversalStack.peek();

            found = false;
            /**
             * Find a vertex adjacent to x that has not been visited and push it
             * on the stack
             */
            for (int i = 0; (i < numVertices) && !found; i++) {
                if ((adjMatrix[x.intValue()][i] < Double.POSITIVE_INFINITY) && !visited[i]) {
                    traversalStack.push(new Integer(i));
                    resultList.addToRear(vertices[i]);
                    visited[i] = true;
                    found = true;
                }
            }
            if (!found && !traversalStack.isEmpty()) {
                traversalStack.pop();
            }
        }
        return resultList.iterator();
    }

    protected int[] getEdgeWithWeightOf(double weight, boolean[] visited) {
        int[] edge = new int[2];
        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                if ((adjMatrix[i][j] == weight) && (visited[i] ^ visited[j])) {
                    edge[0] = i;
                    edge[1] = j;
                    return edge;
                }
            }
        }

        // Will only get to here if a valid edge is not found
        edge[0] = -1;
        edge[1] = -1;
        return edge;
    }

    public void setEdgeWeight(T firstVertex, T secondVertex, double weight) {
        if (weight < 0.0D) {
            throw new IllegalArgumentException("The weight cannot be under the default.");
        }

        int first = this.getIndex(firstVertex);
        int second = this.getIndex(secondVertex);

        if (secondVertex.equals("exterior") || firstVertex.equals("exterior") || secondVertex.equals("entrada") || firstVertex.equals("entrada")) {
            this.adjMatrix[first][second] = 0;
            this.adjMatrix[second][first] = 0;
        } else {
            this.adjMatrix[first][second] = weight;
        }

    }

    public double getEdgeWeight(T firstVertex, T secondVertex) {
        int first = this.getIndex(firstVertex);
        int second = this.getIndex(secondVertex);

        return this.adjMatrix[first][second];
    }

    public double shortestPathWeight(T vertex1, T vertex2) throws EmptyCollectionException {
        return shortestPathWeight(getIndex(vertex1), getIndex(vertex2));

    }

    public double shortestPathWeight(int vertex1, int vertex2) {
        double result = 0;
        if (!indexIsValid(vertex1) || !indexIsValid(vertex2)) {
            return Double.POSITIVE_INFINITY;
        }

        int index1, index2;
        Iterator<Integer> it = iteratorShortestPathIndices(vertex1,
                vertex2);

        if (it.hasNext()) {
            index1 = ((Integer) it.next()).intValue();
        } else {
            return Double.POSITIVE_INFINITY;
        }

        while (it.hasNext()) {
            index2 = (it.next()).intValue();
            result += adjMatrix[index1][index2];
            index1 = index2;
        }

        return result;
    }

    @Override
    public Iterator<T> iteratorShortestPath(T vertex1, T vertex2) {
        return iteratorShortestPath(getIndex(vertex1), getIndex(vertex2));
    }

    public Iterator<T> iteratorShortestPath(int vertex1, int vertex2) {
        ArrayUnorderedList<T> templist = new ArrayUnorderedList<>();
        if (!indexIsValid(vertex1) || !indexIsValid(vertex2)) {
            return templist.iterator();
        }

        Iterator<Integer> it = iteratorShortestPathIndices(vertex1,
                vertex2);
        while (it.hasNext()) {
            templist.addToRear(vertices[(it.next()).intValue()]);
        }
        return templist.iterator();
    }

    protected Iterator<Integer> iteratorShortestPathIndices(int vertex1, int vertex2) {
        int index;
        double weight;
        int[] predecessor = new int[numVertices];
        HeapADT<Double> traversalMinHeap = new LinkedHeap<>();
        ArrayUnorderedList<Integer> resultList = new ArrayUnorderedList<>();
        LinkedStack<Integer> stack = new LinkedStack<>();

        int[] pathIndex = new int[numVertices];
        double[] pathWeight = new double[numVertices];
        for (int i = 0; i < numVertices; i++) {
            pathWeight[i] = Double.POSITIVE_INFINITY;
        }

        boolean[] visited = new boolean[numVertices];
        for (int i = 0; i < numVertices; i++) {
            visited[i] = false;
        }

        if (!indexIsValid(vertex1) || !indexIsValid(vertex2)
                || (vertex1 == vertex2) || isEmpty()) {
            return resultList.iterator();
        }

        pathWeight[vertex1] = 0;
        predecessor[vertex1] = -1;
        visited[vertex1] = true;
        weight = 0;

        /**
         * Update the pathWeight for each vertex except the startVertex. Notice
         * that all vertices not adjacent to the startVertex will have a
         * pathWeight of infinity for now.
         */
        for (int i = 0; i < numVertices; i++) {
            if (!visited[i]) {
                pathWeight[i] = pathWeight[vertex1]
                        + adjMatrix[vertex1][i];
                predecessor[i] = vertex1;
                traversalMinHeap.addElement(pathWeight[i]);
            }
        }

        do {
            try {
                weight = traversalMinHeap.removeMin();
                traversalMinHeap = new LinkedHeap<>();
                if (weight == Double.POSITIVE_INFINITY) // no possible path
                {
                    return resultList.iterator();
                } else {
                    index = getIndexOfAdjVertexWithWeightOf(visited, pathWeight, weight);
                    visited[index] = true;
                }

                /**
                 * Update the pathWeight for each vertex that has has not been
                 * visited and is adjacent to the last vertex that was visited.
                 * Also, add each unvisited vertex to the heap.
                 */
                for (int i = 0; i < numVertices; i++) {
                    if (!visited[i]) {
                        if ((adjMatrix[index][i] < Double.POSITIVE_INFINITY)
                                && (pathWeight[index] + adjMatrix[index][i]) < pathWeight[i]) {
                            pathWeight[i] = pathWeight[index] + adjMatrix[index][i];
                            predecessor[i] = index;
                        }
                        traversalMinHeap.addElement(pathWeight[i]);
                    }
                }
            } catch (EmptyCollectionException ignored) {
            }
        } while (!traversalMinHeap.isEmpty() && !visited[vertex2]);

        index = vertex2;
        stack.push(index);
        do {
            index = predecessor[index];
            stack.push(index);
        } while (index != vertex1);

        while (!stack.isEmpty()) {
            try {
                resultList.addToRear((stack.pop()));
            } catch (EmptyCollectionException ignored) {
            }
        }

        return resultList.iterator();
    }

    protected int getIndexOfAdjVertexWithWeightOf(boolean[] visited, double[] pathWeight, double weight) {
        for (int i = 0; i < numVertices; i++) {
            if ((pathWeight[i] == weight) && !visited[i]) {
                for (int j = 0; j < numVertices; j++) {
                    if ((adjMatrix[i][j] < Double.POSITIVE_INFINITY)
                            && visited[j]) {
                        return i;
                    }
                }
            }
        }

        return -1;
    }

    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }

    @Override
    public boolean isConnected() throws EmptyCollectionException {
        if (this.isEmpty()) {
            return false;
        }

        Iterator<T> it = this.iteratorBFS(0);
        int count = 0;

        while (it.hasNext()) {
            it.next();
            count++;
        }

        return (count == this.size());
    }

    @Override
    public int size() {
        return this.numVertices;
    }

    public Network mstNetwork() throws EmptyCollectionException {
        int x, y;
        int index;
        double weight;
        int[] edge = new int[2];
        LinkedHeap<Double> minHeap = new LinkedHeap<>();
        Network<T> resultGraph = new Network<>();
        if (isEmpty() || !isConnected()) {
            return resultGraph;
        }
        resultGraph.adjMatrix = new double[numVertices][numVertices];
        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                resultGraph.adjMatrix[i][j] = Double.POSITIVE_INFINITY;
            }
        }
        resultGraph.vertices = (T[]) (new Object[numVertices]);
        boolean[] visited = new boolean[numVertices];
        for (int i = 0; i < numVertices; i++) {
            visited[i] = false;
        }

        edge[0] = 0;
        resultGraph.vertices[0] = this.vertices[0];
        resultGraph.numVertices++;
        visited[0] = true;

        // Add all edges, which are adjacent to the starting vertex, to the heap
        for (int i = 0; i < numVertices; i++) {
            minHeap.addElement(adjMatrix[0][i]);
        }
        while ((resultGraph.size() < this.size()) && !minHeap.isEmpty()) {
            // Get the edge with the smallest weight that has exactly one vertex already in the resultGraph
            do {
                try {
                    weight = minHeap.removeMin();
                    edge = getEdgeWithWeightOf(weight, visited);
                } catch (EmptyCollectionException e) {
                    System.out.println(e.getMessage());
                }
            } while (!indexIsValid(edge[0]) || !indexIsValid(edge[1]));
            x = edge[0];
            y = edge[1];
            if (!visited[x]) {
                index = x;
            } else {
                index = y;
            }
            // Add the new edge and vertex to the resultGraph
            resultGraph.vertices[index] = this.vertices[index];
            visited[index] = true;
            resultGraph.numVertices++;
            resultGraph.adjMatrix[x][y] = this.adjMatrix[x][y];
            resultGraph.adjMatrix[y][x] = this.adjMatrix[y][x];

            // Add all edges, that are adjacent to the newly added vertex, to the heap
            for (int i = 0; i < numVertices; i++) {
                if (!visited[i] && (this.adjMatrix[i][index] < Double.POSITIVE_INFINITY)) {
                    edge[0] = index;
                    edge[1] = i;
                    minHeap.addElement(adjMatrix[index][i]);
                }
            }
        }
        return resultGraph;
    }

    protected void expandCapacity() {
        T[] largerVertices = (T[]) (new Object[vertices.length * 2]);
        double[][] largerAdjMatrix
                = new double[vertices.length * 2][vertices.length * 2];

        for (int i = 0; i < numVertices; i++) {
            System.arraycopy(adjMatrix[i], 0, largerAdjMatrix[i], 0, numVertices);
            largerVertices[i] = vertices[i];
        }

        vertices = largerVertices;
        adjMatrix = largerAdjMatrix;
    }

}
