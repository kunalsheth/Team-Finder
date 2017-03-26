package com.teamfinder.test;

import com.google.gson.Gson;
import com.teamfinder.main.data.Database;
import com.teamfinder.main.data.Location;
import com.teamfinder.main.user.Activity;
import com.teamfinder.main.user.User;
import org.la4j.Vector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

/**
 * Created by the-magical-llamicorn on 3/26/17.
 */

public class Test {

    protected static final List<String> names = new LinkedList<>();
    protected static final int[] ages = new int[]{15, 16, 16, 17, 17, 17, 18, 18, 18, 18, 19, 19, 19, 20, 20, 21};

    static {
        try {
            new BufferedReader(new FileReader(new File("test-names.txt"))).lines()
                    .distinct()
                    .forEach(names::add);
        } catch (FileNotFoundException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static void main(final String[] args) {
        for (int i = 0; i < 1000; i++) {
            System.out.println("HERE");
            Database.add(new User() {
                final Location location = Location.newLocationDegrees((Math.random() * -180) + 90, (Math.random() * -360) + 180);
                final double personality = Math.random();
                final String name = names.get((int) (Math.random() * names.size()));

                public String email() {
                    return name.replace("\\s+", "_") + "@teamfinder.com";
                }

                public String name() {
                    return name;
                }

                public int age() {
                    return ages[(int) (Math.random() * ages.length)];
                }

                public double personality() {
                    return personality;
                }

                public Location location() {
                    return location;
                }

                public Vector interestVector() {
                    return Vector.fromArray(new Random().doubles().limit(Activity.values().length).toArray());
                }

                public Set<String> beef() {
                    return Collections.emptySet();
                }

                public long phoneNumber() {
                    return (long) ((Math.random() * (999_999_9999L - 100_000_0000L)) + 100_000_0000L);
                }
            });
        }

        final User ourUser = Database.getUsers().findAny().get();
        System.out.println(
                new Gson().toJson(ourUser)
        );
        System.out.println(
                new Gson().toJson(Database.getUser(ourUser.email()))
        );
    }
}
