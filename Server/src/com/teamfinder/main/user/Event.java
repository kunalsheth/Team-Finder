package com.teamfinder.main.user;

import com.teamfinder.main.data.Location;

public interface Event {

    String organizerEmail();

    String[] attendeesEmails();

    long time();

    Location location();

    Activity type();
}