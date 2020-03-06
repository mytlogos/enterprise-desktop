package com.mytlogos.enterprisedesktop.tools;

import com.mytlogos.enterprisedesktop.background.sqlite.life.Observer;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Utils {
    public static BiFunction FIRST_ONLY = (o, o2) -> o;
    public static BiFunction SECOND_ONLY = (o, o2) -> o2;

    public static String getDomain(String url) {
        String host = URI.create(url).getHost();
        if (host == null) {
            return null;
        }
        Matcher matcher = Pattern.compile("(www\\.)?(.+?)/?").matcher(host);

        String domain;
        if (matcher.matches()) {
            domain = matcher.group(2);
            int index = domain.indexOf("/");

            if (index >= 0) {
                domain = domain.substring(0, index);
            }
        } else {
            domain = host;
        }
        return domain;
    }

    public static String externalUserTypeToName(int type) {
        switch (type) {
            case 0:
                return "NovelUpdates";
            default:
                throw new IllegalArgumentException("unknown type");
        }
    }

    public static <E> void doPartitioned(Collection<E> collection, FunctionEx<List<E>, Boolean> consumer) throws Exception {
        doPartitioned(100, collection, consumer);
    }

    public static <E> void doPartitioned(int steps, Collection<E> collection, FunctionEx<List<E>, Boolean> consumer) throws Exception {
        List<E> list = new ArrayList<>(collection);
        int minItem = 0;
        int maxItem = minItem + steps;

        do {
            if (maxItem > list.size()) {
                maxItem = list.size();
            }

            List<E> subList = list.subList(minItem, maxItem);

            Boolean result = consumer.apply(subList);

            if (result == null || result) {
                break;
            }

            minItem = minItem + steps;
            maxItem = minItem + steps;

            if (maxItem > list.size()) {
                maxItem = list.size();
            }
        } while (minItem < list.size() && maxItem <= list.size());
    }

    public static <E> Observer<E> emptyObserver() {
        return e -> {
        };
    }

    public static <E> void doPartitionedAsync(Collection<E> collection, FunctionEx<List<E>, Boolean> consumer) throws Exception {
        doPartitioned(100, collection, consumer);
    }

    public static <E> void doPartitionedAsync(int steps, Collection<E> collection, ConsumerEx<List<E>> consumer) throws Exception {
        List<E> list = new ArrayList<>(collection);
        int minItem = 0;
        int maxItem = minItem + steps;

        List<CompletableFuture<Void>> futures = new ArrayList<>((collection.size() / steps) + 1);

        do {
            if (maxItem > list.size()) {
                maxItem = list.size();
            }

            List<E> subList = list.subList(minItem, maxItem);

            futures.add(CompletableFuture.runAsync(() -> consumer.accept(subList)));

            minItem = minItem + steps;
            maxItem = minItem + steps;

            if (maxItem > list.size()) {
                maxItem = list.size();
            }
        } while (minItem < list.size() && maxItem <= list.size());
        finishAll(futures).get();
    }

    public static <T> CompletableFuture<List<T>> finishAll(Collection<CompletableFuture<T>> futuresList) {
        CompletableFuture<Void> allFuturesResult = CompletableFuture.allOf(futuresList.toArray(new CompletableFuture[0]));
        return allFuturesResult.thenApply(v -> futuresList.stream().
                map(CompletableFuture::join).
                collect(Collectors.toList())
        );
    }

    public static int countChar(String s, char c) {
        int count = 0;

        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == c) {
                count++;
            }
        }

        return count;
    }
}
