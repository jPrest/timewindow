package prestej.timewindow.core;

public interface Window<T, U> {

    void update(Point<T> point);

    U getValue();

    void progressEventTime(long timestamp);
}
