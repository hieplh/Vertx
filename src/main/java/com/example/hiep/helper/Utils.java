package com.example.hiep.helper;

import static com.example.hiep.MainVerticle.mUnicodeASCII;

import io.vertx.ext.web.RoutingContext;

public class Utils {

    public static String removeDuplicate(RoutingContext request) {
        String duplicated = null;
        String[] arr = request.normalizedPath().split("/");
        StringBuilder sb = new StringBuilder();
        for (String s : arr) {
            if (!s.equals(duplicated)) {
                sb.append(s);
                duplicated = s;
            }
        }
        return sb.toString();
    }

    public static String convertUnicodeToASCII(String s) {
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            sb.append(mUnicodeASCII.getOrDefault(Character.toString(c), Character.toString(c)).toLowerCase());
        }
        return sb.toString();
    }

    public static String getShortenName(String s, boolean isUpperCase) {
        s = convertUnicodeToASCII(s);
        
        String[] arr;
        if (!isUpperCase) {
            arr = s.toLowerCase().split(" ");
        } else {
            arr = s.split(" ");
        }

        StringBuilder sb = new StringBuilder();
        if (isUpperCase) {
            sb.append(arr[arr.length - 1].substring(0, 1).toUpperCase()).append(arr[arr.length - 1].substring(1));
        } else {
            sb.append(arr[arr.length - 1].substring(0, 1)).append(arr[arr.length - 1].substring(1));
        }

        for (int i = 0; i < arr.length - 1; i++) {
            if (isUpperCase) {
                sb.append(arr[i].substring(0, 1).toUpperCase());
            } else {
                sb.append(arr[i].substring(0, 1));
            }
        }

        return sb.toString();
    }

    public static String getShortenNameUnicode(String s, boolean isUpperCase) {
        String[] arr;
        if (!isUpperCase) {
            arr = s.toLowerCase().split(" ");
        } else {
            arr = s.split(" ");
        }

        StringBuilder sb = new StringBuilder();
        if (isUpperCase) {
            sb.append(arr[arr.length - 1].substring(0, 1).toUpperCase()).append(arr[arr.length - 1].substring(1));
        } else {
            sb.append(arr[arr.length - 1].substring(0, 1)).append(arr[arr.length - 1].substring(1));
        }

        for (int i = 0; i < arr.length - 1; i++) {
            if (isUpperCase) {
                sb.append(arr[i].substring(0, 1).toUpperCase());
            } else {
                sb.append(arr[i].substring(0, 1));
            }
        }

        return sb.toString();
    }
}
