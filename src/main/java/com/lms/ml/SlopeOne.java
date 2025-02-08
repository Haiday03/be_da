package com.lms.ml;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

public class SlopeOne {

    private static Map<BookML, Map<BookML, Double>> diff = new HashMap<>();
    private static Map<BookML, Map<BookML, Integer>> freq = new HashMap<>();
    private static Map<UserML, HashMap<BookML, Double>> inputData;
    private static Map<UserML, HashMap<BookML, Double>> outputData = new HashMap<>();

    public static void slopeOne(int numberOfUserMLs) {
        inputData = InputData.initializeData(numberOfUserMLs);
        System.out.println("Slope One - Before the Prediction\n");
        buildDifferencesMatrix(inputData);
        System.out.println("\nSlope One - With Predictions\n");
        predict(inputData);
    }

    /**
     * Based on the available data, calculate the relationships between the
     * items and number of occurences
     *
     * @param data
     *            existing user data and their items' ratings
     */
    private static void buildDifferencesMatrix(Map<UserML, HashMap<BookML, Double>> data) {
        for (HashMap<BookML, Double> user : data.values()) {
            for (Map.Entry<BookML, Double> e : user.entrySet()) {
                if (!diff.containsKey(e.getKey())) {
                    diff.put(e.getKey(), new HashMap<BookML, Double>());
                    freq.put(e.getKey(), new HashMap<BookML, Integer>());
                }
                for (Map.Entry<BookML, Double> e2 : user.entrySet()) {
                    int oldCount = 0;
                    if (freq.get(e.getKey()).containsKey(e2.getKey())) {
                        oldCount = freq.get(e.getKey()).get(e2.getKey()).intValue();
                    }
                    double oldDiff = 0.0;
                    if (diff.get(e.getKey()).containsKey(e2.getKey())) {
                        oldDiff = diff.get(e.getKey()).get(e2.getKey()).doubleValue();
                    }
                    double observedDiff = e.getValue() - e2.getValue();
                    freq.get(e.getKey()).put(e2.getKey(), oldCount + 1);
                    diff.get(e.getKey()).put(e2.getKey(), oldDiff + observedDiff);
                }
            }
        }
        for (BookML j : diff.keySet()) {
            for (BookML i : diff.get(j).keySet()) {
                double oldValue = diff.get(j).get(i).doubleValue();
                int count = freq.get(j).get(i).intValue();
                diff.get(j).put(i, oldValue / count);
            }
        }
        printData(data);
    }

    /**
     * Based on existing data predict all missing ratings. If prediction is not
     * possible, the value will be equal to -1
     *
     * @param data
     *            existing user data and their items' ratings
     */
    private static void predict(Map<UserML, HashMap<BookML, Double>> data) {
        HashMap<BookML, Double> uPred = new HashMap<BookML, Double>();
        HashMap<BookML, Integer> uFreq = new HashMap<BookML, Integer>();
        for (BookML j : diff.keySet()) {
            uFreq.put(j, 0);
            uPred.put(j, 0.0);
        }
        for (Map.Entry<UserML, HashMap<BookML, Double>> e : data.entrySet()) {
            for (BookML j : e.getValue().keySet()) {
                for (BookML k : diff.keySet()) {
                    try {
                        double predictedValue = diff.get(k).get(j).doubleValue() + e.getValue().get(j).doubleValue();
                        double finalValue = predictedValue * freq.get(k).get(j).intValue();
                        uPred.put(k, uPred.get(k) + finalValue);
                        uFreq.put(k, uFreq.get(k) + freq.get(k).get(j).intValue());
                    } catch (NullPointerException e1) {
                    }
                }
            }
            HashMap<BookML, Double> clean = new HashMap<BookML, Double>();
            for (BookML j : uPred.keySet()) {
                if (uFreq.get(j) > 0) {
                    clean.put(j, uPred.get(j).doubleValue() / uFreq.get(j).intValue());
                }
            }
            for (BookML j : InputData.items) {
                if (e.getValue().containsKey(j)) {
                    clean.put(j, e.getValue().get(j));
                } else if (!clean.containsKey(j)) {
                    clean.put(j, -1.0);
                }
            }
            outputData.put(e.getKey(), clean);
        }
        printData(outputData);
    }

    private static void printData(Map<UserML, HashMap<BookML, Double>> data) {
        for (UserML user : data.keySet()) {
            System.out.println(user.getUsername() + ":");
            print(data.get(user));
        }
    }

    private static void print(HashMap<BookML, Double> hashMap) {
        NumberFormat formatter = new DecimalFormat("#0.000");
        for (BookML j : hashMap.keySet()) {
            System.out.println(" " + j.getName() + " --> " + formatter.format(hashMap.get(j).doubleValue()));
        }
    }

}
