
package api.Structures.java.Interfaces;

import api.Structures.java.exceptions.*;

public interface MapADT {
    public void gerarMapaAleatorio(int numVertices, boolean bidirecional, double densidade) throws EmptyCollectionException;
    
    public void importarMapaDeArquivo(String nomeArquivo) throws MapException, UnknownPathException;
    
    public void exportarMapaParaArquivo(String nomeArquivo);


}
