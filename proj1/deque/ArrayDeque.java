package deque;

public class ArrayDeque<T> {
    private T[] items;
    private int size;
    private int capacity;
    private int first;
    private int last;
    private final int INITIAL_CAPACITY = 8;

    public ArrayDeque() {
        capacity = INITIAL_CAPACITY;
        items = (T[]) new Object[capacity];
        size = 0;
        first = capacity - 1;
        last = 0;
    }

    public void addFirst(T item) {
        if (size == capacity) {
            resize(capacity * 2);
        }
        items[first] = item;
        first = (first - 1) % capacity;
        size++;
    }

    public void addLast(T item) {
        if (size == capacity) {
            resize(capacity * 2);
        }
        items[last] = item;
        last = (last + 1) % capacity;
        size++;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        for (int i = (first + 1) % capacity; i < last; i = (i + 1) % capacity) {
            System.out.print(items[i] + " ");
        }
        System.out.println();
    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        if (capacity >= INITIAL_CAPACITY * 2 && (size - 1) * 4 == capacity) {
            resize(capacity / 2);
        }
        first = (first + 1) % capacity;
        T item = items[first];
        items[first] = null;
        size--;
        return item;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }
        if (capacity >= 16 && (size - 1) * 4 == capacity) {
            resize(capacity / 2);
        }
        last = (last - 1) % capacity;
        T item = items[last];
        items[last] = null;
        size--;
        return item;
    }

    public T get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        return items[(first + 1 + index) % capacity];
    }

    private void resize(int length) {
        T[] newArr = (T[]) new Object[length];
        int j = 0;
        for (int i = (first + 1) % capacity; i < last; i = (i + 1) % capacity) {
            newArr[j] = items[i];
            j++;
        }
        items = newArr;
        capacity = length;
        first = capacity - 1;
        last = j;
    }
}