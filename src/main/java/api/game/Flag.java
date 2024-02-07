package api.game;

import api.game.interfaces.IFlag;

public class Flag implements IFlag {
    /**
     * Index of the flag in the graph.
     */
    private int index;

    /**
     * Constructor of the Flag class.
     * We pass the number of vertices, so we need to convert it to the index.
     *
     * @param vertex The initial vertex of the flag in the graph.
     */
    public Flag(int vertex) {
        this.index = vertex - 1;
    }

    /**
     * Gets the index of the flag in the graph.
     *
     * @return The index of the flag.
     */
    @Override
    public int getIndex() {
        return index;
    }

    /**
     * Sets the index of the flag in the graph.
     *
     * @param index The new index of the flag.
     */
    @Override
    public void setIndex(int index) {
        this.index = index;
    }
}
