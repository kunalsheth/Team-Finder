package com.teamfinder.test;

import com.google.gson.Gson;
import com.teamfinder.main.data.Database;
import com.teamfinder.main.data.Location;
import com.teamfinder.main.recommender.Recommender;
import com.teamfinder.main.user.Activity;
import com.teamfinder.main.user.User;
import org.la4j.Vector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.stream.LongStream;

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
        System.out.println("Number Of Names " + names.size());
    }

    public static void main(final String[] args) {
        System.out.println("DB Size " + Database.getUserCollection().size());
        LongStream.range(0, 1000).parallel().forEach(i -> {
            Database.add(new User() {
                final Location location = Math.random() > 0.5 ? Location.newLocationDegrees(((Math.random() * -18) + 9 + 37.548706), (Math.random() * -36) + 18 + -122.059283) : Location.newLocationDegrees(((Math.random() * -0.018) + 0.09 + 37.7989733), (Math.random() * -0.036) + 0.018 + -122.4836963);
                final double personality = Math.random();
                final String name = names.get((int) (Math.random() * names.size())) + " " + names.get((int) (Math.random() * names.size())) + " " + names.get((int) (Math.random() * names.size()));

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
        });

        System.out.println("DB Size " + Database.getUserCollection().size());

        final User ourUser = Database.getUsers().findAny().get();
        System.out.println(
                "our user" + new Gson().toJson(ourUser)
        );
        System.out.println(
                "db getUser email" + new Gson().toJson(Database.getUser(ourUser.email()))
        );
        Recommender.recommend(ourUser, 10).map(o -> new Gson().toJson(o)).forEach(System.out::println);

    }
}
