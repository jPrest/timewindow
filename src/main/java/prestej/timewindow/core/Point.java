package prestej.timewindow.core;

import java.time.Instant;

public class Point<T> implements Comparable<Point<T>> {
    private long timestamp;
    private T value;

    public Point(long timestamp, T value) {
        this.timestamp = timestamp;
        this.value = value;
    }

    public Point(Instant timestamp, T value) {
        this.timestamp = timestamp.toEpochMilli();
        this.value = value;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public T getValue() {
        return value;
    }

    @Override
    public int compareTo(Point o) {
        int compare = Long.compare(this.timestamp, o.timestamp);
        if(compare == 0 && this.getValue() instanceof Comparable && o.getValue() instanceof Comparable) {
            // compare by value if timestamp is the same
            return ((Comparable) this.getValue()).compareTo(o.getValue());
        }
        return compare;
    }
}