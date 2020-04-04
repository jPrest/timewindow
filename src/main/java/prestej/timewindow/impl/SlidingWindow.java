package prestej.timewindow.impl;

import prestej.timewindow.core.Point;
import prestej.timewindow.core.Window;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SlidingWindow<T, U> implements Window<T, U> {

    private final Duration duration;
    private Function<NavigableSet<Point<T>>, U> aggregator;
    private Collection<WindowStrategy> windowStrategy = Collections.emptyList();
    private NavigableSet<Point<T>> points = new ConcurrentSkipListSet<>();
    private long eventTime = Long.MIN_VALUE;
    private U currentValue;
    private boolean needRecalculation = true;


    public SlidingWindow(Duration duration, Function<NavigableSet<Point<T>>, U> aggregator) {
        this.duration = duration;
        this.aggregator = aggregator;
    }

    public SlidingWindow(Duration duration, Function<NavigableSet<Point<T>>, U> aggregator, WindowStrategy... windowStrategy) {
        this.duration = duration;
        this.aggregator = aggregator;
        this.windowStrategy = Arrays.asList(windowStrategy);
    }

    @Override
    public void update(Point<T> point) {
        points.add(point);
        progressEventTime(point.getTimestamp());
        needRecalculation = true;
    }

    @Override
    public U getValue() {
        if (needRecalculation) {
            recalculateCurrentValue();
        }
        return currentValue;
    }

    @Override
    public void progressEventTime(long timestamp) {
        eventTime = Math.max(timestamp, eventTime);
        boolean removedPoints = removeOldPoints();
        if (removedPoints) {
            needRecalculation = true;
        }
    }

    private boolean removeOldPoints() {
        Set<Point> toBeRemoved = points.stream()
                // replace with takeWhile() like early abort func
                .filter(point -> point.getTimestamp() < (eventTime - duration.toMillis()))
                .collect(Collectors.toSet());
        return points.removeAll(toBeRemoved);
    }

    private void recalculateCurrentValue() {
        U tmp = this.currentValue;
        currentValue = aggregator.apply(this.points);
        if (currentValue == null && windowStrategy.contains(WindowStrategy.KEEP_CURRENT_VALUE_ON_EMPTY_WINDOW)) {
            this.currentValue = tmp;
        }
    }
}