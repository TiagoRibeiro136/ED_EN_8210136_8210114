import api.Map.Map;
import api.Structures.java.exceptions.EmptyCollectionException;
import api.Structures.java.exceptions.MapException;
import api.Structures.java.exceptions.UnknownPathException;
import api.game.Bot;
import api.game.Flag;
import api.game.algorithm.ShortestPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ShortestPathTest {

    private Flag flag = new Flag(1);
    private ShortestPath shortestPath;
    private Map gameMap;
    private Bot currentBot;

    @BeforeEach
    void setUp() throws MapException, UnknownPathException {
        // Initialize the game map
        gameMap = new Map();
        gameMap.importarMapaDeArquivo("Mapa.txt");
        shortestPath = new ShortestPath(gameMap);
        currentBot = new Bot("Bot 1", shortestPath, flag, false);
    }

    @Test
    void getNextPosition_ValidInputs_ReturnNextIndex() throws EmptyCollectionException {
        // Mocking data
        int currentIndex = 0;
        int endIndex = 5;

        // Call the method under test

        int nextIndex = shortestPath.getNextPosition(currentIndex, endIndex, currentBot);

        // Assert the result
        assertEquals(13, nextIndex);
    }

    @Test
    void updateBotPosition_ValidInputs_UpdateMap() {
        // Mocking data
        int currentIndex = 0;
        int nextIndex = 5;

        // Call the method under test
        shortestPath.updateBotPosition(currentIndex, nextIndex, currentBot);

        // Assert that the map is updated correctly
        assertTrue(gameMap.getVertices()[nextIndex] instanceof Bot);
        assertNull(gameMap.getVertices()[currentIndex]);
    }

    @Test
    void hasBot_NoBotAtGivenIndex_ReturnFalse() {
        // Mocking data
        int vertexWithoutBot = 10;

        // Call the method under test
        boolean result = shortestPath.hasBot(vertexWithoutBot);

        // Assert the result
        assertFalse(result);
    }
}

