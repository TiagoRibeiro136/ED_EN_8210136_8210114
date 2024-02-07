package api.Map;

import api.Map.Interface.MapADT;
import api.Structures.java.Graphs.Network;
import api.Structures.java.exceptions.EmptyCollectionException;
import api.Structures.java.exceptions.MapException;
import api.Structures.java.exceptions.UnknownPathException;
import api.game.Bot;
import api.game.interfaces.IFlag;

import java.io.*;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

public class Map extends Network<IFlag> implements MapADT {

    /**
     *
     */
    private final int MIN_DISTANCE = 1;
    /**
     *
     */
    private final int MAX_DISTANCE = 15;
    /**
     *
     */
    private String mapName;

    /**
     *
     */
    public Map() {
        super();
        this.mapName = "MapDefault";
    }

    /**
     *
     * @param mapName
     */
    public Map(String mapName) {
        super();
        this.mapName = mapName;
    }

    /**
     *
     * @return
     */
    public String getMapName() {
        return mapName;
    }

    /**
     *
     * @param mapName
     */
    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    /**
     *
     * @return
     */
    @Override
    public String getMap() {
        String map = "";
        for (int i = 0; i < this.numVertices; i++) {
            map += this.vertices[i] + " ";
        }
        return map + "\n";
    }

    /**
     *
     * @param numVertices
     */
    private void novoGrafo(int numVertices) {
        this.vertices = new IFlag[numVertices];
        this.adjMatrix = new double[numVertices][numVertices];
        this.numVertices = numVertices;
    }


    /**
     *
     * @param numVertices
     * @param bidirecional
     * @param densidade
     * @throws EmptyCollectionException
     */
    @Override
    //gerar mapa aleatorio
    public void gerarMapaAleatorio(int numVertices, boolean bidirecional, double densidade) throws EmptyCollectionException {

        if (densidade < 0 || densidade > 1) {
            throw new IllegalArgumentException("Densidade fora do intrevalo de 0 e 1");
        }
        do {
            novoGrafo(numVertices);
            double totalArestas = calcularTotalArestas(numVertices, bidirecional, densidade);
            Random random = new Random();
            int gerarArestas = 0;

            while (gerarArestas < totalArestas) {
                int i = random.nextInt(numVertices);
                int j = random.nextInt(numVertices);

                if (i != j && this.adjMatrix[i][j] == 0) {
                    int distance = random.nextInt(MAX_DISTANCE) + MIN_DISTANCE;
                    this.adjMatrix[i][j] = distance;

                    if (bidirecional) {
                        this.adjMatrix[j][i] = distance;
                    }
                    gerarArestas++;
                }
            }
        } while (!isConnected());
        System.out.println();
    }

    /**
     *
     * @param numVertices
     * @param bidirecional
     * @param densidade
     * @return
     */
    private double calcularTotalArestas(int numVertices, boolean bidirecional, double densidade) {
        double totalArestas;
        if (bidirecional) {
            totalArestas = (numVertices * (numVertices - 1)) * (densidade / 2);
        } else {
            totalArestas = (numVertices * (numVertices - 1)) * densidade;
            totalArestas += numVertices * densidade;
        }
        return totalArestas;
    }

    /***
     *
     * @param nomeArquivo
     */
    //exportar mapa gerado
    @Override
    public void exportarMapaParaArquivo(String nomeArquivo) {
        try ( BufferedWriter writer = new BufferedWriter(new FileWriter(nomeArquivo))) {
            for (int i = 0; i < adjMatrix.length; i++) {
                for (int j = 0; j < adjMatrix.length; j++) {
                    writer.write(String.valueOf(adjMatrix[i][j]));
                    writer.write(" ");
                }
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param nomeArquivo
     * @throws MapException
     * @throws UnknownPathException
     */
    // importar mapa
    @Override
    public void importarMapaDeArquivo(String nomeArquivo) throws MapException, UnknownPathException {
        try ( BufferedReader reader = new BufferedReader(new FileReader(nomeArquivo))) {

            String line;
            int i = 0;

            line = reader.readLine();
            novoGrafo(line.split(" ").length);

            do {
                String[] values = line.split(" ");
                if (adjMatrix == null || i >= adjMatrix.length) {
                    throw new MapException("A matriz não foi criada corretamente.");
                }

                if (values.length != adjMatrix[i].length) {
                    throw new MapException("Número incorreto de elementos na linha " + (i + 1));
                }

                if (i >= adjMatrix.length) {
                    throw new MapException("Ficheiro demasiado longo");
                }

                for (int j = 0; j < adjMatrix[i].length; j++) {
                    try {
                        adjMatrix[i][j] = Double.parseDouble(values[j]);
                    } catch (NumberFormatException e) {
                        throw new MapException(
                                "Valor numérico inválido no ficheiro | Linha " + (i + 1) + ", Coluna "
                                        + (j + 1));
                    }
                }

                i++;
            } while ((line = reader.readLine()) != null);

            if (i < adjMatrix.length) {
                throw new UnknownPathException("Ficheiro demasiado curto");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String imprimirMapa() {
        StringBuilder result = new StringBuilder();

        // Adicione informações sobre os vértices
        result.append("Vertices: ");
        for (int i = 0; i < numVertices; i++) {
            result.append(vertices[i]).append(" ");
        }
        result.append("\n");

        // Adicione informações sobre as arestas e pesos
        result.append("Edges and Weights:\n");
        for (int i = 0; i < numVertices; i++) {
            for (int j = i + 1; j < numVertices; j++) {
                if (adjMatrix[i][j] < Double.POSITIVE_INFINITY) {
                    result.append(vertices[i]).append(" -- ").append(vertices[j])
                            .append(" (Weight: ").append(adjMatrix[i][j]).append(")\n");
                }
            }
        }

        return result.toString();
    }

    /**
     *
     * @return
     */
    public double[][] getAdjacencyMatrix() {

        return adjMatrix;
    }

    /**
     *
     * @return
     */
    public IFlag[] getVertices() {
        return this.vertices;
    }

    /**
     *
     * @param index
     * @param entety
     */
    public void setVertice(int index, IFlag entety) {
        vertices[index] = entety ;
    }
}

