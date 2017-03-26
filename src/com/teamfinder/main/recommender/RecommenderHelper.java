package com.teamfinder.main.recommender;

import com.teamfinder.main.data.Location;
import com.teamfinder.main.user.User;
import com.teamfinder.main.user.VectorfiableUser;
import org.la4j.Vector;

/**
 * Created by the-magical-llamicorn on 3/26/17.
 */
public class RecommenderHelper {

    protected static final Location triangulationPoint1 = Location.newLocationDegrees(0, 0);
    protected static final Location triangulationPoint2 = Location.newLocationDegrees(-90, 0);
    protected static final Location triangulationPoint3 = Location.newLocationDegrees(90, 0);

    protected static double cosineSimilarity(final Vector v1, final Vector v2) {
        return v1.hadamardProduct(v2).sum() / (v1.norm() * v2.norm());
    }

    protected static double euclideanDistance(final Vector v1, final Vector v2) {
        final Vector delta = v1.subtract(v2);
        return Math.sqrt(delta.hadamardProduct(delta).sum());
    }

    protected static Vector locationToVector(final Location location) {
        return Vector.fromArray(new double[]{
                location.distance(triangulationPoint1),
                location.distance(triangulationPoint2),
                location.distance(triangulationPoint3)
        });
    }

    protected static VectorfiableUser locationVectorfiableUser(final User u) {
        return new VectorfiableUser(
                u.email(),
                locationToVector(u.location()),
                RecommenderHelper::euclideanDistance
        );
    }

    protected static VectorfiableUser intrestsVectorfiableUser(final User u) {
        return new VectorfiableUser(
                u.email(),
                u.interestVector(),
                RecommenderHelper::euclideanDistance
        );
    }
}
