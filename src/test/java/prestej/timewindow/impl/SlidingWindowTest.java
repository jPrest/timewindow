package prestej.timewindow.impl;

import org.junit.jupiter.api.Test;
import prestej.timewindow.core.Point;
import prestej.timewindow.core.Window;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static java.time.temporal.ChronoUnit.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static prestej.timewindow.impl.WindowAggregator.*;

class SlidingWindowTest {
    @Test
    void testWindow() {
        Window<Number, Number> window = new SlidingWindow<>(Duration.ofMillis(10), max());
        assertNull(window.getValue());

        window.update(p(1, 2.0));
        assertEquals(2.0, window.getValue());
        window.update(p(2, 3.0));
        assertEquals(3.0, window.getValue());
        window.update(p(3, -1.0));
        assertEquals(3.0, window.getValue());

        window.progressEventTime(13);
        assertEquals(-1.0, window.getValue());
        window.progressEventTime(20);
        assertNull(window.getValue());
    }

    @Test
    void testWindow2() {
        // window with length 10s
        // and max() aggregation function
        Window<Number, Number> window = new SlidingWindow<>(Duration.ofSeconds(2), WindowAggregator.max());

        Instant now = Instant.now();
        window.update(new Point<>(now, 30.0));
        window.getValue(); // returns 30

        window.update(new Point<>(now.plusSeconds(1), 50));
        window.getValue(); // returns 50

        window.update(new Point<>(now.plusSeconds(5), 20));
        window.getValue(); // returns 20
    }


    private static <T> Point<T> p(long time, T value) {
        return new Point<>(time, value);
    }
}