package com.teamfinder.main.user;

import com.teamfinder.main.data.Location;
import org.la4j.Vector;

import java.io.Serializable;
import java.util.Set;

/**
 * Created by the-magical-llamicorn on 3/25/17.
 */
public interface User extends Serializable {

    String email();

    String name();

    int age();

    double personality();

    Location location();

    // corresponds to Activity.values()
    Vector interestVector();

    // social conflicts with other users
    Set<String> beef();

    long phoneNumber();
}
