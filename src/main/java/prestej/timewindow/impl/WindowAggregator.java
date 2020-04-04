package prestej.timewindow.impl;

import prestej.timewindow.core.Point;

import java.util.Comparator;
import java.util.NavigableSet;
import java.util.function.Function;

public class WindowAggregator {
    public static Function<NavigableSet<Point<Number>>, Number> delta() {
        return set -> {
            if (set.isEmpty()) return 0;
            if (set.size() == 1) return set.first().getValue().doubleValue();
            return set.last().getValue().doubleValue() - set.first().getValue().doubleValue();
        };
    }

    public static Function<NavigableSet<Point<Number>>, Number> min() {
        return set -> set.stream().min(Comparator.comparingDouble(p -> p.getValue().doubleValue())).map(Point::getValue).orElse(null);
    }

    public static Function<NavigableSet<Point<Number>>, Number> max() {
        return set -> set.stream().max(Comparator.comparingDouble(p -> p.getValue().doubleValue())).map(Point::getValue).orElse(null);
    }

}
