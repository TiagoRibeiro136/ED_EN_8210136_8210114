package api.util;

/**
 * Class used for generating random numbers.
 */
public class Random {

    /**
     * Generates a random integer within the range [min, max].
     *
     * @param min Minimum value of the range.
     * @param max Maximum value of the range.
     * @return A random integer within the range [min, max].
     */
    public static int generateRandomNumber(long min, long max) {

        // Generates a random number using the appropriate formula
        int randomInt = (int) Math.floor(Math.random() * (max - min + 1) + min);

        return randomInt;
    }
}