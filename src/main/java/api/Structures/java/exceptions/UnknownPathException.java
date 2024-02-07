package api.Structures.java.exceptions;

/**
 * The {@code UnknownPathException} class is an exception that indicates
 * an attempt to handle an unknown or undefined path.
 * This exception is typically thrown when an operation expects a valid path,
 * but the provided path is not recognized or does not exist, leading to an error.
 *
 * @author Gil Leão
 * @version 1.0
 * @since 2023-11-30
 */
public class UnknownPathException extends Exception {

  
    public UnknownPathException() {
        super();
    }

 
    public UnknownPathException(String message) {
        super(message);
    }
}
