package com.lms.ml;

import java.util.*;

import lombok.Data;

@Data
public class InputData {

    protected static List<BookML> items = Arrays.asList(new BookML("Candy"), new BookML("Drink"), new BookML("Soda"), new BookML("Popcorn"), new BookML("Snacks"));


    public static Map<UserML, HashMap<BookML, Double>> initializeData(int numberOfUsers) {
        Map<UserML, HashMap<BookML, Double>> data = new HashMap<>();
        HashMap<BookML, Double> newUser;
        Set<BookML> newRecommendationSet;
        for (int i = 0; i < numberOfUsers; i++) {
            newUser = new HashMap<BookML, Double>();
            newRecommendationSet = new HashSet<>();
            for (int j = 0; j < 3; j++) {
                newRecommendationSet.add(items.get((int) (Math.random() * 5)));
            }
            for (BookML item : newRecommendationSet) {
                newUser.put(item, Math.random());
            }
            data.put(new UserML("User " + i), newUser);
        }
        return data;
    }

}