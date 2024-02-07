package api.Structures.java.exceptions;

/**
 * extend RunTimeException para não ser obrigado a dizer na função throws
 * ElementNotFoundException
 */

public class ConcurrentModificationException extends RuntimeException {
    public ConcurrentModificationException() {
        super();
    }

    public ConcurrentModificationException(String text) {
        super(text);
    }
}