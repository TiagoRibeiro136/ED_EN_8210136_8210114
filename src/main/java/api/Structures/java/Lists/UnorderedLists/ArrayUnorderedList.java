package api.Structures.java.Lists.UnorderedLists;

import api.Structures.java.Interfaces.UnorderedListADT;
import api.Structures.java.ArrayLists.ArrayList;
import api.Structures.java.exceptions.ElementNotFoundException;
import api.Structures.java.exceptions.EmptyCollectionException;

public class ArrayUnorderedList<T> extends ArrayList<T> implements UnorderedListADT<T> {

    public ArrayUnorderedList() {
        super();
    }

    @Override
    public void addToFront(T element) {
        if (rear == items.length) {
            expandCapacity();
        }
        for (int i = rear; i > 0; i--) {
            items[i] = items[i - 1];
        }
        items[0] = element;
        rear++;
        modCount++;
    }

    @Override
    public void addToRear(T element) {
        if (rear == items.length) {
            expandCapacity();
        }

        items[rear] = element;

        rear++;
        modCount++;
    }

    @Override
    public void addAfter(T element, T target) throws EmptyCollectionException {
        if (rear == items.length) {
            expandCapacity();
        }

        int targetIndex = find(target);
        if (targetIndex == ELEMENT_NOT_FOUND) {
            throw new ElementNotFoundException("ELEMENT NOT FOUND");
        }

        for (int i = rear; i > targetIndex; i--) {
            items[i] = items[i - 1];
        }

        items[targetIndex + 1] = element;

        rear++;
        modCount++;
    }

    public T removeIndex(int index) throws ElementNotFoundException {
        if (index < 0 || index >= rear) {
            throw new ElementNotFoundException("Index out of bounds: " + index);
        }

        T removedElement = items[index];

        // Desloca os elementos após a posição especificada uma posição para frente
        for (int i = index; i < rear - 1; i++) {
            items[i] = items[i + 1];
        }

        items[rear - 1] = null; // Remove a referência ao último elemento duplicado
        rear--;
        modCount++;

        return removedElement;
    }
}
