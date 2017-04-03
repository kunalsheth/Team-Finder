package com.teamfinder.main.user;

import com.teamfinder.main.data.Location;

import java.io.Serializable;

public interface Event extends Serializable {

    String organizerEmail();

    String[] attendeesEmails();

    long time();

    Location location();

    Activity type();
}