package com.teamfinder.recommender;

import com.teamfinder.data.Database;
import com.teamfinder.data.Location;
import com.teamfinder.data.SearchEngine;
import com.teamfinder.data.Vectorfiable;
import com.teamfinder.user.Activity;
import com.teamfinder.user.User;
import org.la4j.Vector;

import java.util.function.ToDoubleBiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by the-magical-llamicorn on 3/25/17.
 */
public class Recommender {

    protected static final Location triangulationPoint1 = Location.newLocationDegrees(0, 0);
    protected static final Location triangulationPoint2 = Location.newLocationDegrees(-90, 0);
    protected static final Location triangulationPoint3 = Location.newLocationDegrees(90, 0);

    protected static final int numberOfIntrestCombinations = 3;

    public static final int numberOfActivities = Activity.values().length;

    protected static final SearchEngine<Vectorfiable> locationClusters, interestClusters;

    static {
        System.out.println("Creating Location Clusters");
        locationClusters = new SearchEngine<>(
                Database.getUsers()
                        .map(Recommender::locationVectorfiableUser)
                        .collect(Collectors.toSet()),
                3,
                Database.getUserCollection().size() / 100
        );

        System.out.println("Creating Interest Clusters");
        interestClusters = new SearchEngine<>(
                Database.getUsers()
                        .map(Recommender::intrestsVectorfiableUser)
                        .collect(Collectors.toSet()),
                numberOfActivities,
                (int) (factorial(numberOfActivities) / factorial(numberOfActivities - numberOfIntrestCombinations))
        );
    }

    protected static Stream<User> recommend(final User user) {
        locationClusters.apply(locationVectorfiableUser(user));
    }

    protected static long factorial(final int n) {
        return IntStream.range(2, n).reduce(1, (a, b) -> a * b);
    }

    protected static Vector locationToVector(final Location location) {
        return Vector.fromArray(new double[]{
                location.distance(triangulationPoint1),
                location.distance(triangulationPoint2),
                location.distance(triangulationPoint3)
        });
    }

    protected static double cosineSimilarity(final Vector v1, final Vector v2) {
        return v1.hadamardProduct(v2).sum() / (v1.norm() * v2.norm());
    }

    protected static double euclideanDistance(final Vector v1, final Vector v2) {
        final Vector delta = v1.subtract(v2);
        return Math.sqrt(delta.hadamardProduct(delta).sum());
    }

    protected static VectorfiableUser locationVectorfiableUser(final User u) {
        return new VectorfiableUser(
                u.email(),
                u.interestVector(),
                Recommender::euclideanDistance
        );
    }

    protected static VectorfiableUser intrestsVectorfiableUser(final User u) {
        return new VectorfiableUser(
                u.email(),
                u.interestVector(),
                Recommender::euclideanDistance
        );
    }

    protected static class VectorfiableUser implements Vectorfiable {
        final String email;
        final Vector vector;
        final ToDoubleBiFunction<Vector, Vector> distance;

        public VectorfiableUser(final String email, final Vector vector, final ToDoubleBiFunction<Vector, Vector> distance) {
            this.email = email;
            this.vector = vector;
            this.distance = distance;
        }

        public String email() {
            return email;
        }

        public Vector toVec() {
            return vector;
        }

        public double distance(final Vector that) {
            return distance.applyAsDouble(this.vector, that);
        }
    }
}
