package api.Map.Interface;

import api.Structures.java.exceptions.EmptyCollectionException;
import api.Structures.java.exceptions.MapException;
import api.Structures.java.exceptions.UnknownPathException;
import api.exceptions.InvalidMapException;
import api.game.interfaces.IFlag;

import java.io.FileNotFoundException;

public interface MapADT {
    /**
     *
     * @param numVertices
     * @param bidirecional
     * @param densidade
     * @throws EmptyCollectionException
     */
    public void gerarMapaAleatorio(int numVertices, boolean bidirecional, double densidade) throws EmptyCollectionException;

    /**
     *
     * @param nomeArquivo
     * @throws MapException
     * @throws UnknownPathException
     */
    public void importarMapaDeArquivo(String nomeArquivo) throws MapException, UnknownPathException;

    /**
     *
     * @param nomeArquivo
     */
    public void exportarMapaParaArquivo(String nomeArquivo);

    /**
     *
     * @return
     */
    public String getMap();
}
