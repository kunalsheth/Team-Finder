package com.teamfinder.main.server;

import com.teamfinder.main.data.Location;
import com.teamfinder.main.recommender.Recommender;
import com.teamfinder.main.user.Activity;
import com.teamfinder.main.user.User;
import org.la4j.Vector;

import java.util.Collections;
import java.util.Set;

import static spark.Spark.get;
import static spark.Spark.post;

/**
 * Created by the-magical-llamicorn on 3/26/17.
 */
public class Server {

    public static void main(final String[] args) {
        get("/", (request, response) -> Index.generateIndex());
        post("/", (request, response) -> {
            final User user = new User() {
                protected final String name = request.queryParams("name");
                protected final String email = request.queryParams("email");
                protected final int age = Integer.parseInt(request.queryParams("age"));
                protected final double latitude = Double.parseDouble(request.queryParams("latitude"));
                protected final double longitude = Double.parseDouble(request.queryParams("longitude"));
                protected final Location location = Location.newLocationDegrees(latitude, longitude);
                protected final long phone = Long.parseLong(request.queryParams("phone").replaceAll("[()-]", ""));

                public String email() {
                    return email;
                }

                public String name() {
                    return name;
                }

                public int age() {
                    return age;
                }

                public double personality() {
                    return 0.5;
                }

                public Location location() {
                    return location;
                }

                public Vector interestVector() {
                    return Vector.zero(Activity.values().length);
                }

                public Set<String> beef() {
                    return Collections.emptySet();
                }

                public long phoneNumber() {
                    return phone;
                }
            };
            request.session(true).attribute("email", user.email());
            return DisplayFriends.generateFriendsTable(Recommender.recommend(user, 100));
        });
    }
}
