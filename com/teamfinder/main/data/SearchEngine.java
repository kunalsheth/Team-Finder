package com.teamfinder.main.data;

import org.la4j.Vector;
import org.la4j.vector.DenseVector;

import java.util.*;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by the-magical-llamicorn on 3/25/17.
 */
public class SearchEngine<T extends Vectorfiable> implements Function<T, Stream<T>> {

    public final int dimensions;
    public final int k;

    protected final Collection<T> data;
    protected final Vector[] centroids;
    protected List<T>[] assignments;

    protected double residualSumOfSquares = 0;

    public SearchEngine(final Collection<T> data, final int dimensions, final int k) {
        this.data = data;
        this.dimensions = dimensions;
        this.k = k;

        centroids = new Vector[k];
        assignments = IntStream.range(0, k)
                .mapToObj(i -> new ArrayList<T>(data.size() / k))
                .toArray(List[]::new);

        initClusters();

        double lastRss = residualSumOfSquares - 1;
        while (residualSumOfSquares != lastRss) {
            lastRss = residualSumOfSquares;
            residualSumOfSquares = recalculateClusters();
        }
    }

    public synchronized void initClusters() {
        final Vector min = DenseVector.constant(dimensions, Double.MAX_VALUE);
        final Vector max = DenseVector.constant(dimensions, Double.MIN_VALUE);

        data.forEach(t ->
                IntStream.range(0, dimensions).forEach(i -> {
                    final double value = t.toVec().get(i);
                    if (value < min.get(i)) min.set(i, value);
                    else if (value > max.get(i)) max.set(i, value);
                })
        );

        final Random random = new Random();
        IntStream.range(0, k).parallel().forEach(i -> {
            final double[] v = random.doubles(dimensions).toArray();
            IntStream.range(0, dimensions).forEach(j -> {
                v[j] *= max.get(j) - min.get(j);
                v[j] += min.get(j);
            });
            centroids[i] = Vector.fromArray(v);
        });
    }

    public synchronized double recalculateClusters() {
        Arrays.stream(assignments).forEach(List::clear);

        data.parallelStream()
                .forEach(v -> {
                    final int clusterIndex = IntStream.range(0, k)
                            .boxed()
                            .min(Comparator.comparingDouble(
                                    i -> v.distance(centroids[i])
                            ))
                            .get();
                    assignments[clusterIndex].add(v);
                });

        IntStream.range(0, k).parallel()
                .forEach(i -> {
                    final Vector lastCentroid = centroids[i];
                    if (assignments[i].size() > 0) centroids[i] = assignments[i].stream()
                            .map(T::toVec)
                            .reduce(Vector::add)
                            .orElse(lastCentroid)
                            .divide(assignments[i].size());
                });

        return IntStream.range(0, k).parallel()
                .boxed()
                .flatMapToDouble(i -> assignments[i].stream()
                        .mapToDouble(v -> v.distance(centroids[i]))
                )
                .map(d -> Math.pow(d, 2))
                .sum();
    }

    public double getResidualSumOfSquares() {
        return residualSumOfSquares;
    }

    public synchronized Stream<T> apply(final T data) {
        return IntStream.range(0, k).parallel()
                .boxed()
                .sorted(Comparator.comparingDouble(i -> data.distance(centroids[i])))
                .flatMap(i -> assignments[i].stream())
                .sorted(Comparator.comparingDouble(i -> data.distance(i.toVec())));
    }
}
