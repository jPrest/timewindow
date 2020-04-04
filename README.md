
# Time Windows

Simplifies handling and aggregating event time based windows in java.

Example: Calculate the maximum value of sensor readings in a 2 second window
```java
// window with length 2s
// and max() aggregation function for the window
Window<Number, Number> window = new SlidingWindow<>(Duration.ofSeconds(2), WindowAggregator.max());

// add data points to the window
Instant now = Instant.now();
window.update(new Point<>(now, 30.0));
window.getValue(); // returns the aggregated window value - 30

window.update(new Point<>(now.plusSeconds(1), 50.0));
window.getValue(); // returns 50

window.update(new Point<>(now.plusSeconds(5), 20.0));
window.getValue(); // returns 20
```


