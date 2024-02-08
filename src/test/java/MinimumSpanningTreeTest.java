import api.Map.Map;
import api.Structures.java.exceptions.EmptyCollectionException;
import api.Structures.java.Stacks.LinkedStack.LinkedStack;
import api.Structures.java.exceptions.MapException;
import api.Structures.java.exceptions.UnknownPathException;
import api.game.Bot;
import api.game.Flag;
import api.game.algorithm.MinimumSpanningTree;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MinimumSpanningTreeTest {

    private Flag flag = new Flag(1);
    private MinimumSpanningTree minimumSpanningTree;
    private Map gameMap;

    Bot currentBot = new Bot("Bot 1", minimumSpanningTree, flag);

    @BeforeEach
    void setUp() throws MapException, UnknownPathException {
        // Initialize the game map and the MinimumSpanningTree object
        gameMap = new Map();
        gameMap.importarMapaDeArquivo("Mapa.txt"); // You need to provide proper initialization for the Map object
        minimumSpanningTree = new MinimumSpanningTree(gameMap);
    }

    @Test
    void getNextPosition_validInputs_returnNextIndex() throws EmptyCollectionException {
        // Mocking data
        int currentIndex = 0;
        int endIndex = 5;
        // Call the method under test
        int nextIndex = minimumSpanningTree.getNextPosition(currentIndex, endIndex, currentBot);

        // Assert the result
        assertEquals(2, nextIndex); // Example assertion, replace with appropriate values
    }

    @Test
    void updateBotPosition_validInputs_updateMap() {
        // Mocking data
        int currentIndex = 0;
        int nextIndex = 5;
        // Call the method under test
        minimumSpanningTree.updateBotPosition(currentIndex, nextIndex, currentBot);

        // Assert that the map is updated correctly
        assertTrue(gameMap.getVertices()[nextIndex] instanceof Bot);
        assertNull(gameMap.getVertices()[currentIndex]);
    }




    @Test
    void hasBot_noBotAtGivenIndex_returnFalse() {
        // Mocking data
        int vertexWithoutBot = 10;

        // Call the method under test
        boolean result = minimumSpanningTree.hasBot(vertexWithoutBot);

        // Assert the result
        assertFalse(result);
    }
}
