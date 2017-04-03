package com.teamfinder.main.recommender;

import com.teamfinder.main.data.Database;
import com.teamfinder.main.data.SearchEngine;
import com.teamfinder.main.user.Activity;
import com.teamfinder.main.user.User;
import com.teamfinder.main.user.VectorfiableUser;

import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by the-magical-llamicorn on 3/25/17.
 */
public class Recommender {

    protected static final int numberOfIntrestCombinations = 3;

    public static final int numberOfActivities = Activity.values().length;

    protected static final SearchEngine<VectorfiableUser> locationSearchEngine;

    static {
        System.out.println("Creating Location Clusters");
        locationSearchEngine = new SearchEngine<>(
                Database.getUsers()
                        .map(RecommenderHelper::locationVectorfiableUser)
                        .collect(Collectors.toSet()),
                3,
                Math.max(Database.getUserCollection().size() / 100, 3)
        );
    }

    // this is really jank, but it works
    public static Stream<User> recommend(final User user, final int count) {
        final VectorfiableUser userLocation = RecommenderHelper.locationVectorfiableUser(user);
        return locationSearchEngine.apply(userLocation)
                .map(v -> Database.getUser(v.email()))
                .filter(Objects::nonNull)
                .filter(u -> !user.beef().contains(u.email()))
                .limit(count * 16)
                .sorted(Comparator.comparingDouble(u ->
                        RecommenderHelper.intrestsVectorfiableUser(u)
                                .distance(RecommenderHelper.intrestsVectorfiableUser(user).toVec())
                ))
                .limit(count * 8)
                .sorted(Comparator.comparingDouble(u -> Math.pow(user.age() - u.age(), 2)))
                .limit(count * 4)
                .sorted(Comparator.comparingDouble(
                        u -> RecommenderHelper.locationVectorfiableUser(u).distance(userLocation.toVec()))
                )
                .limit(count * 2)
                .sorted(Comparator.comparingDouble(u -> -u.personality()))
                .limit(count * 1);
    }

}
