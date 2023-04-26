package deque;

public class LinkedListDeque<T> {

    private class ItemNode {
        public T item;
        public ItemNode next;
        public ItemNode previous;

        public ItemNode(T i, ItemNode n, ItemNode p) {
            item = i;
            next = n;
            previous = p;
        }
    }

    private final ItemNode sentinel;
    private int size;

    public LinkedListDeque() {
        sentinel = new ItemNode(null, null, null);
        size = 0;
    }

    private ItemNode initialNode(T item) {
        ItemNode n = new ItemNode(item, null, null);
        n.next = n;
        n.previous = n;
        size++;
        return n;
    }

    private ItemNode regularNode(T item) {
        ItemNode first = sentinel.next;
        ItemNode last = first.previous;
        ItemNode n = new ItemNode(item, first, last);
        last.next = n;
        first.previous = n;
        size++;
        return n;
    }

    public void addFirst(T item) {
        if (size == 0) {
            sentinel.next = initialNode(item);
        } else {
            sentinel.next = regularNode(item);
        }
    }

    public void addLast(T item) {
        if (size == 0) {
            sentinel.next = initialNode(item);
        } else {
            regularNode(item);
        }
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        ItemNode p = sentinel.next;
        for (int i = 0; i < size; i++) {
            System.out.print(p.item + " ");
            p = p.next;
        }
        System.out.println();
    }

    private void makeNewRing(ItemNode first, ItemNode last) {
        last.next = first;
        first.previous = last;
        size--;
        if (size == 0) {
            sentinel.next.next = null;
            sentinel.next.previous = null;
            sentinel.next = null;
        } else {
            sentinel.next = first;
        }
    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        T item = sentinel.next.item;
        makeNewRing(sentinel.next.next, sentinel.next.previous);
        return item;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }
        T item = sentinel.next.previous.item;
        makeNewRing(sentinel.next, sentinel.next.previous.previous);
        return item;
    }

    public T get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        ItemNode p = sentinel.next;
        for (int i = 0; i < index; i++) {
            p = p.next;
        }
        return p.item;
    }

    private T getRecursive(ItemNode p, int index) {
        if (index == 0) {
            return p.item;
        }
        return getRecursive(p.next, index - 1);
    }

    public T getRecursive(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        return getRecursive(sentinel.next, index);
    }
}