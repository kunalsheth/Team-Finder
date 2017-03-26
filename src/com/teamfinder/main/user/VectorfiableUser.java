package com.teamfinder.main.user;

import com.teamfinder.main.data.Vectorfiable;
import org.la4j.Vector;

import java.util.function.ToDoubleBiFunction;

/**
 * Created by the-magical-llamicorn on 3/26/17.
 */
public class VectorfiableUser implements Vectorfiable {
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
