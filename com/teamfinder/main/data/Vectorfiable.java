package com.teamfinder.main.data;

import org.la4j.Vector;

/**
 * Created by the-magical-llamicorn on 3/25/17.
 */
public interface Vectorfiable {
    Vector toVec();

    double distance(final Vector that);
}
