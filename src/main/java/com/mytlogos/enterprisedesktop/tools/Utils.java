package com.mytlogos.enterprisedesktop.tools;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
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
        List<E> list = new ArrayList<>(collection);
        int steps = 100;
        int minItem = 0;
        int maxItem = minItem + steps;

        do {
            if (maxItem > list.size()) {
                maxItem = list.size();
            }

            List<E> subList = list.subList(minItem, maxItem);

            Boolean result = consumer.apply(subList);

            if (result != null && result) {
                continue;
            } else if (result == null) {
                break;
            }

            minItem = minItem + steps;
            maxItem = minItem + steps;

            if (maxItem > list.size()) {
                maxItem = list.size();
            }
        } while (minItem < list.size() && maxItem <= list.size());
    }
}
