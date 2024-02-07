package api.Structures.java.ArrayLists;

import api.Structures.java.Interfaces.ListADT;
import api.Structures.java.exceptions.ElementNotFoundException;
import api.Structures.java.exceptions.EmptyCollectionException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Abstract base class for implementing an ArrayList data structure.
 *
 * @param <T> the type of elements stored in the ArrayList
 */
public abstract class ArrayList<T> implements ListADT<T> {

    protected final int DEFAULT_CAPACITY = 10;
    protected final int EXPAND_BY = 2;
    protected final int ELEMENT_NOT_FOUND = -1;

    protected T[] items;
    protected int rear;
    protected int modCount;

    protected ArrayList() {
        this.items = (T[]) new Object[DEFAULT_CAPACITY];
        this.rear = 0;
        this.modCount = 0;
    }

    @Override
    public T first() {
        return items[0];
    }

    @Override
    public T last() {
        return items[rear];
    }

    @Override
    public boolean isEmpty() {
        return (rear == 0);
    }

    @Override
    public int size() {
        return this.rear;
    }

    protected int find(T element) {
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null && items[i].equals(element)) {
                return i;
            }
        }

        return ELEMENT_NOT_FOUND;
    }

    @Override
    public boolean contains(T target) {
        return (find(target) != ELEMENT_NOT_FOUND);
    }

    @Override
    public T removeFirst() throws EmptyCollectionException  {
        if (isEmpty()) {
            throw new EmptyCollectionException("Collection Empty");
        }

        T result = items[0];

        for (int i = 0; i < items.length - 1; i++) {
            items[i] = items[i + 1];
        }

        rear--;
        modCount++;

        return result;

    }

    @Override
    public T removeLast() throws EmptyCollectionException {

        if (isEmpty()) {
            throw new EmptyCollectionException("Collection Empty");
        }

        T result = items[rear];
        items[rear] = null;

        rear--;
        modCount++;

        return result;
    }

    @Override
    public T remove(T element) throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException("Collection Empty");
        }

        int elementIndex = find(element);

        if (elementIndex == ELEMENT_NOT_FOUND) {
            throw new ElementNotFoundException("Element Not Found");
        }

        T result = items[elementIndex];

        for (int i = elementIndex; i < items.length - 1; i++) {
            items[i] = items[i + 1];
        }

        rear--;
        modCount++;

        return result;
    }

    @Override
    public Iterator<T> iterator() {
        return new MyIterator<T>();
    }

    /**
     * isto estava private, mas o editor pôs protected para usar no add da
     * OrderedArrayList
     */
    protected void expandCapacity() {
        T[] newitems = (T[]) new Object[items.length + EXPAND_BY];

        // passar todos os elementos da queue antiga para a nova
        for (int i = 0; i < items.length; i++) {
            newitems[i] = items[i];
        }

        // fazer com que a antiga queue fique com o tamanho da nova queue
        this.items = newitems;
    }

    private class MyIterator<E> implements Iterator<E> {

        private int current;
        private int expectedModCount;
        private boolean okToRemove;

        public MyIterator() {
            current = 0;
            expectedModCount = modCount;
            okToRemove = false;
        }

        private void checkForConcurrentModification() {
            if (modCount != expectedModCount) {
                throw new java.util.ConcurrentModificationException();
            }
        }

        @Override
        public boolean hasNext() {
            // não sei se é preciso fazer aqui
            checkForConcurrentModification();
            return (current < ArrayList.this.rear);
        }

        @Override
        public E next() {
            // Verifique se o modCount foi alterado durante a iteração
            checkForConcurrentModification();
            if (!hasNext()) {
                try {
                    throw new EmptyCollectionException("Collection Empty");
                } catch (EmptyCollectionException ex) {
                    Logger.getLogger(ArrayList.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            E element = (E) ArrayList.this.items[current];
            current++;
            return element;
        }

    }

}
