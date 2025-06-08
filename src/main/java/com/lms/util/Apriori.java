package com.lms.util;

import java.util.*;

public class Apriori {

    // Giao dịch mẫu
    static List<Set<String>> transactions = Arrays.asList();

    static int minSupport = 2;

    public static void main(String[] args) {
        List<Set<String>> frequentItemsets = apriori(transactions, minSupport);

        System.out.println("Frequent Itemsets:");
        for (Set<String> itemset : frequentItemsets) {
            System.out.println(itemset);
        }
    }

    // Thuật toán Apriori
    public static List<Set<String>> apriori(List<Set<String>> transactions, int minSupport) {
        List<Set<String>> result = new ArrayList<>();
        Map<Set<String>, Integer> supportCount;

        // Bước 1: Tạo itemsets 1 phần tử
        Set<Set<String>> candidates = new HashSet<>();
        for (Set<String> transaction : transactions) {
            for (String item : transaction) {
                candidates.add(new HashSet<>(Collections.singleton(item)));
            }
        }

        while (!candidates.isEmpty()) {
            supportCount = new HashMap<>();
            // Đếm support
            for (Set<String> transaction : transactions) {
                for (Set<String> candidate : candidates) {
                    if (transaction.containsAll(candidate)) {
                        supportCount.put(candidate, supportCount.getOrDefault(candidate, 0) + 1);
                    }
                }
            }

            // Lọc theo minsup
            Set<Set<String>> frequent = new HashSet<>();
            for (Map.Entry<Set<String>, Integer> entry : supportCount.entrySet()) {
                if (entry.getValue() >= minSupport) {
                    frequent.add(entry.getKey());
                    result.add(entry.getKey());
                }
            }

            // Sinh itemsets mới (k + 1)
            candidates = generateCandidates(frequent);
        }

        return result;
    }

    // Sinh các ứng viên (k+1-itemsets) từ tập frequent k-itemsets
    public static Set<Set<String>> generateCandidates(Set<Set<String>> frequentItemsets) {
        Set<Set<String>> candidates = new HashSet<>();
        List<Set<String>> itemsetList = new ArrayList<>(frequentItemsets);

        for (int i = 0; i < itemsetList.size(); i++) {
            for (int j = i + 1; j < itemsetList.size(); j++) {
                Set<String> a = itemsetList.get(i);
                Set<String> b = itemsetList.get(j);

                // Gộp 2 tập có k-1 phần tử giống nhau
                Set<String> union = new HashSet<>(a);
                union.addAll(b);

                if (union.size() == a.size() + 1) {
                    candidates.add(union);
                }
            }
        }

        return candidates;
    }
}
