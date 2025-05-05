import java.util.LinkedList;
import java.util.Objects;

public class JacobHashMap<T extends CharSequence> {

    private LinkedList<T>[] data;   // buckets
    private int size;               // stored elements

    private static final int DEFAULT_CAPACITY = 8;
    private static final double MAX_LOAD = 0.75;

    public JacobHashMap() {
        data = new LinkedList[DEFAULT_CAPACITY];
        for (int i = 0; i < data.length; i++) data[i] = new LinkedList<>();
    }

    private int dumbHash(String s) {
        return (s == null ? 0 : s.length()) % data.length;
    }

    public void add(T value) {
        Objects.requireNonNull(value);
        int idx = dumbHash(value.toString());
        if (!data[idx].contains(value)) {
            data[idx].add(value);
            size++;
            if ((double) size / data.length > MAX_LOAD) resize(data.length * 2);
        }
    }

    public boolean contains(T value) {
        if (value == null) return false;
        return data[dumbHash(value.toString())].contains(value);
    }

    public int size()      { return size; }
    public int capacity()  { return data.length; }

    public void resize(int newCap) {
        LinkedList<T>[] old = data;
        data = new LinkedList[newCap];
        for (int i = 0; i < newCap; i++) data[i] = new LinkedList<>();
        size = 0;
        for (LinkedList<T> bucket : old)
            for (T e : bucket) add(e);
    }
}
