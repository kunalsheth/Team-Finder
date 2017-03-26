package com.teamfinder.data;

import org.la4j.Vector;
import org.la4j.vector.DenseVector;

import java.util.*;
import java.util.function.Function;
import java.util.stream.IntStream;

/**
 * Created by the-magical-llamicorn on 3/24/17.
 */
public class FuzzyKMeans<T extends Vectorfiable> implements Function<T, Vector>, Runnable {

    public final int dimensions;
    public final int k;

    protected final Collection<T> data;
    protected final Vector[] centroids;

    protected double residualSumOfSquares;

    public FuzzyKMeans(final Collection<T> data, final int dimensions, final int k) {
        this.data = data;
        this.dimensions = dimensions;
        this.k = k;

        centroids = new Vector[k];
    }

    public void run() {
        initClusters();

        double lastRss = 0;
        while (residualSumOfSquares != lastRss) {
            lastRss = residualSumOfSquares;
            residualSumOfSquares = recalculateClusters();
        }
    }

    public void initClusters() {
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

    public double recalculateClusters() {
        final List<T>[] assignments = IntStream.range(0, k)
                .mapToObj(i -> new ArrayList<T>(data.size() / k))
                .toArray(List[]::new);

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
                .forEach(i -> centroids[i] = assignments[i].stream()
                        .map(T::toVec)
                        .reduce(Vector::add)
                        .get()
                        .divide(assignments[i].size()));

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

    public Vector apply(final T data) {
        final Vector newVector = DenseVector.fromArray(
                Arrays.stream(centroids).parallel()
                        .mapToDouble(data::distance)
                        .toArray()
        );
        return newVector.divide(newVector.norm());
    }
}
