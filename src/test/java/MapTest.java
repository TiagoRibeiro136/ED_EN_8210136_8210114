import api.Map.Map;
import api.Structures.java.exceptions.EmptyCollectionException;
import api.Structures.java.exceptions.MapException;
import api.Structures.java.exceptions.UnknownPathException;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

public class MapTest {



    @Test
    void gerarMapaAleatorio_ValidInputs_MapGenerated() throws EmptyCollectionException {
        Map map = new Map();
        assertDoesNotThrow(() -> map.gerarMapaAleatorio(5, true, 0.5));
    }

    @Test
    void exportarMapaParaArquivo_ValidInputs_FileCreated() throws IOException, EmptyCollectionException {
        Map map = new Map();
        map.gerarMapaAleatorio(5, true, 0.5);
        String nomeArquivo = "mapa_teste.txt";
        assertDoesNotThrow(() -> map.exportarMapaParaArquivo(nomeArquivo));
        assertTrue(new File(nomeArquivo).exists());
    }

    @Test
    void importarMapaDeArquivo_ValidInputs_MapImported() throws IOException, MapException, UnknownPathException, EmptyCollectionException {
        Map map = new Map();
        map.gerarMapaAleatorio(50, true, 0.5);
        String nomeArquivo = "mapa_teste.txt";
        map.exportarMapaParaArquivo(nomeArquivo);

        Map importedMap = new Map();
        assertDoesNotThrow(() -> importedMap.importarMapaDeArquivo(nomeArquivo));
        assertEquals(map.imprimirMapa(), importedMap.imprimirMapa());
    }

    @Test
    void imprimirMapa_ValidInputs_MapPrinted() throws EmptyCollectionException {
        Map map = new Map();
        map.gerarMapaAleatorio(50, true, 0.5);
        assertDoesNotThrow(() -> map.imprimirMapa());
    }

    @Test
    void getAdjacencyMatrix_ValidInputs_ReturnMatrix() throws EmptyCollectionException {
        Map map = new Map();
        map.gerarMapaAleatorio(50, true, 0.5);
        assertNotNull(map.getAdjacencyMatrix());
    }

    @Test
    void getVertices_ValidInputs_ReturnVerticesArray() throws EmptyCollectionException {
        Map map = new Map();
        map.gerarMapaAleatorio(50, true, 0.5);
        assertNotNull(map.getVertices());
    }

}
