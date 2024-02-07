package api.game.interfaces;

public interface IFlag {
    /**
     * Gets the current index in the array of vertices.
     *
     * @return The current index in the array of vertices.
     */
    int getIndex();

    /**
     * Sets the current index in the array of vertices.
     * The position is the index where the entity is in the graph's array of vertices.
     *
     * @param index The new position of the entity.
     */
    void setIndex(int index);

}
