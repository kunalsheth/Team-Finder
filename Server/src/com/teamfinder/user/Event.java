package com.teamfinder.user;

import com.teamfinder.data.Location;

public interface Event {

    String organizerEmail();

    String[] attendeesEmails();

    long time();

    Location location();

    Activity type();
}