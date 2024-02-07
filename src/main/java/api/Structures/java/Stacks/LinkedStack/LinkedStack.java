package api.Structures.java.Stacks.LinkedStack;

import api.Structures.java.Interfaces.StackADT;
import api.Structures.java.exceptions.EmptyCollectionException;
import api.Structures.java.exceptions.NullException;

/**
 * LinkedStack represents a stack implemented using a linked structure.
 *
 * @param <T> the type of elements stored in the stack
 */
public class LinkedStack<T> implements StackADT<T> {
    /**
     * Size of the Stack
     */
    private int size;

    /**
     * Especify the top of the stack
     */
    private LinearNode<T> top;

    /**
     * Constructor to create an empty stack.
     */
    public LinkedStack() {
        this.top = null;
        this.size = 0;
    }

    /**
     * Constructor to create a stack with a specified top and size.
     *
     * @param top  The top of the stack.
     * @param size The size of the stack.
     */
    public LinkedStack(LinearNode<T> top, int size) {
        this.top = top;
        this.size = size;
    }

    /**
     * Pushes an element onto the stack.
     *
     * @param element The element to push onto the stack.
     */
    @Override
    public void push(T element) {
        LinearNode<T> newNode = new LinearNode<>(element);
        newNode.setNext(this.top);

        this.top = newNode;

        this.size++;
    }

    /**
     * Removes the element at the top of this stack and
     * returns a reference to it.
     * Throws an EmptyCollectionException if the stack is empty.
     *
     * @return T element removed from top of stack
     * @throws EmptyCollectionException if a pop
     *                                  is attempted on empty stack
     */
    @Override
    public T pop() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException("The stack is empty.");
        }

        T result = this.top.getElement();

        this.top = this.top.getNext();

        this.size--;

        return result;
    }

    /**
     * Returns a reference to the element at the top of this stack.
     * The element is not removed from the stack.
     * Throws an EmptyCollectionException if the stack is empty.
     *
     * @return T element on top of stack
     * @throws EmptyCollectionException if a
     *                                  peek is attempted on empty stack
     */
    @Override
    public T peek() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException("The stack is empty.");
        }

        return this.top.getElement();
    }

    /**
     * Check the emptiness of the stack
     *
     * @return true if the stack is empty, false otherwise
     */
    @Override
    public boolean isEmpty() {
        return (this.top == null);
    }

    /**
     * Returns the number of elements in the stack.
     *
     * @return the number of elements in the stack
     */
    @Override
    public int size() {
        return this.size;
    }

    @Override
    public String toString() {
        String text = "";

        for (LinearNode<T> current = this.top; current != null; current = current.getNext()) {
            text += current.getElement() + "\n";
        }

        text += '\n';

        return text;
    }

    /**
     * Get the current size of the stack
     *
     * @return size of the Stack
     */
    public int getSize() {
        return size;
    }
}
