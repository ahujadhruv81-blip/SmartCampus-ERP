package com.smartcampus.erp.util;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

// Rubric: Collections & Generics (Generic Utility Methods)
public final class CollectionUtils {

    private CollectionUtils() {
    }

    // Generic method to filter any List based on a Predicate
    public static <T> List<T> filter(List<T> items, Predicate<T> predicate) {
        if (items == null) {
            return Collections.emptyList();
        }
        List<T> result = new ArrayList<>();
        for (T item : items) {
            if (predicate.test(item)) {
                result.add(item);
            }
        }
        return result;
    }

    // Generic method to extract unique elements into a Set
    public static <T, R> Set<R> mapToSet(List<T> items, Function<T, R> mapper) {
        if (items == null) {
            return Collections.emptySet();
        }
        Set<R> set = new LinkedHashSet<>();
        for (T item : items) {
            R mapped = mapper.apply(item);
            if (mapped != null) {
                set.add(mapped);
            }
        }
        return set;
    }

    // Generic method to group items into a Map
    public static <T, K> Map<K, List<T>> groupBy(List<T> items, Function<T, K> classifier) {
        if (items == null) {
            return Collections.emptyMap();
        }
        Map<K, List<T>> map = new LinkedHashMap<>();
        for (T item : items) {
            K key = classifier.apply(item);
            map.computeIfAbsent(key, k -> new ArrayList<>()).add(item);
        }
        return map;
    }
}
