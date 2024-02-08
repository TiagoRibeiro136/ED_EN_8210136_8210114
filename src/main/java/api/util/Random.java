package api.util;

/**
 * Classe utilizada para geração de números aleatórios.
 */
public class Random {

    /**
     * Gera um número inteiro aleatório no intervalo [min, max].
     *
     * @param min Valor mínimo do intervalo.
     * @param max Valor máximo do intervalo.
     * @return Um número inteiro aleatório no intervalo [min, max].
     */
    public static int generateRandomNumber(long min, long max) {

        // Gera um número aleatório usando a fórmula apropriada
        int randomInt = (int) Math.floor(Math.random() * (max - min + 1) + min);

        return randomInt;
    }
}