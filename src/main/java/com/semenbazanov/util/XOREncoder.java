package com.semenbazanov.util;

public class XOREncoder {
    public static String encrypt (String str, String key){
        String res = "";
        for (int i = 0; i < str.length(); i++) {
            res += Character.toString(key.charAt(i % key.length()) ^ str.charAt(i));
        }
        return res;
    }
}
