package api.Structures.java.Queues.LinkedQueue;

import api.Structures.java.Interfaces.QueueADT;
import api.Structures.java.exceptions.EmptyCollectionException;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * LinkedQueue represents a linked implementation of a queue.
 *
 * @param <T> the type of elements in the queue
 */
public class LinkedQueue<T> implements QueueADT<T>, Iterable<T> {
    private int size = 0;
    private LinearNode<T> front, rear;
    private int modCount;

    /**
     * Adds one element to the rear of this queue.
     * 
     * @param element the element to be added to
     *                the rear of this queue
     */
    @Override
    public void enqueue(T element) {
        LinearNode<T> newNode = new LinearNode<>(element);

        if (isEmpty()) {
            this.front = newNode;
            this.rear = newNode;
        } else {
            this.rear.setNext(newNode);
            this.rear = newNode;
        }

        this.size++;
        this.modCount++;
    }

    /**
     * Removes and returns the element at the front of
     * this queue.
     * 
     * @return the element at the front of this queue
     */
    @Override
    public T dequeue() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException("Empty Queue");
        }

        T element = this.front.getElement();

        LinearNode<T> newNode = this.front.getNext();
        this.front.setNext(null);
        this.front = newNode;

        this.size--;
        this.modCount++;

        return element;
    }

    /**
     * Returns without removing the element at the front of
     * this queue.
     * 
     * @return the first element in this queue
     */
    @Override
    public T first() {
        return this.front.getElement();
    }

    /**
     * Returns true if this queue contains no elements.
     *
     * @return true if this queue is empty
     */
    @Override
    public boolean isEmpty() {
        return (front == null);
    }

    /**
     * Returns the number of elements in this queue.
     *
     * @return the integer representation of the size
     *         of this queue
     */
    @Override
    public int size() {
        return this.size;
    }

    /**
     * Returns a string representation of this queue.
     * 
     * @return the string representation of this queue
     */
    public String toString() {
        String text = "";

        for (LinearNode<T> currentNode = front; currentNode != null; currentNode = currentNode.getNext()) {
            text += currentNode.getElement() + "\n";
        }

        text += '\n';

        return text;
    }

    /**
     * Retorna um iterator para a fila.
     *
     * @return um iterator para a fila
     */
    @Override
    public Iterator<T> iterator() {
        return new LinkedQueueIterator();
    }

    /**
     * Classe interna para o iterator da fila.
     */
    private class LinkedQueueIterator implements Iterator<T> {
        private LinearNode<T> current;
        private int expectedModCount;

        /**
         * Construtor do iterator.
         */
        public LinkedQueueIterator() {
            this.current = front;
            expectedModCount = modCount;
        }

        private void checkForConcurrentModification() {
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
        }

        /**
         * Verifica se há um próximo elemento na fila.
         *
         * @return true se houver um próximo elemento, false caso contrário
         */
        @Override
        public boolean hasNext() {
            checkForConcurrentModification();
            return (current != null);
        }

        /**
         * Retorna o próximo elemento na fila.
         *
         * @return o próximo elemento na fila
         * @throws EmptyCollectionException se a fila estiver vazia
         */
        @Override
        public T next() {
            if (!hasNext()) {
                try {
                    throw new EmptyCollectionException("No more elements in the queue");
                } catch (EmptyCollectionException ex) {
                    Logger.getLogger(LinkedQueue.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            T element = current.getElement();
            current = current.getNext();
            return element;
        }
    }

}
