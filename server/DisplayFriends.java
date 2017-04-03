package com.teamfinder.main.server;

import com.teamfinder.main.recommender.Recommender;
import com.teamfinder.main.user.Activity;
import com.teamfinder.main.user.User;

import java.util.Comparator;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class DisplayFriends {

    public static String generateFriendsTable(final Stream<User> users) {
        return friendsTablePrefix
                + users
                .map(DisplayFriends::friendsTableContent)
                .reduce((a, b) -> a + b)
                + friendsTableSuffix;
    }

    protected static final String friendsTablePrefix =
            "<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <title>Friends</title>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "<table style=\"width:100%\">\n" +
                    "    <tr>\n" +
                    "        <th>Name</th>\n" +
                    "        <th>Age</th>\n" +
                    "        <th>Email</th>\n" +
                    "        <th>Phone Number</th>\n" +
                    "        <th>Location</th>\n" +
                    "        <th>Personal Interests</th>\n" +
                    "    </tr>";

    protected static final String friendsTableContent(final User user) {
        final double[] interestVector = user.interestVector().toDenseVector().toArray();
        return "<tr>\n" +
                "        <td>" + user.name() + "</td>\n" +
                "        <td>" + user.age() + "</td>\n" +
                "        <td>" + user.email() + "</td>\n" +
                "        <td>" + user.phoneNumber() + "</td>\n" +
                "        <td><a href=\"https://www.google.com/maps/preview/@" + Math.toDegrees(user.location().latitude) + "," + Math.toDegrees(user.location().longitude) + ",10z\">Location</a></td>\n" +
                "        <td>"
                + IntStream.range(0, Recommender.numberOfActivities)
                .boxed()
                .sorted(Comparator.comparingDouble(i -> interestVector[i]))
                .map(i -> Activity.values()[i].toString())
                .limit(3)
                .reduce((a, b) -> a + " " + b)
                + "</td>\n" +
                "    </tr>";
    }

    protected static final String friendsTableSuffix =
            "</table>\n" +
                    "</body>\n" +
                    "<style>\n" +
                    "    table, th, td {\n" +
                    "        border: 1px solid black;\n" +
                    "    }\n" +
                    "</style>\n" +
                    "</html>";
}
