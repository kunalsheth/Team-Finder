package com.teamfinder.main.recommender;

import com.teamfinder.main.data.Database;
import com.teamfinder.main.data.SearchEngine;
import com.teamfinder.main.user.Activity;
import com.teamfinder.main.user.User;
import com.teamfinder.main.user.VectorfiableUser;

import java.util.Comparator;
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
                Database.getUserCollection().size() / 100
        );
    }

    public static Stream<String> recommend(final User user, final int count) {
        return locationSearchEngine.apply(RecommenderHelper.locationVectorfiableUser(user))
//                .filter(v -> !user.beef().contains(v.email()))
//                .limit(count)
//                .sorted(Comparator.comparingDouble(v ->
//                        RecommenderHelper.intrestsVectorfiableUser(Database.getUser(v.email()))
//                                .distance(RecommenderHelper.intrestsVectorfiableUser(user).toVec())
//                ))
                .map(v -> v.email());
    }

}
