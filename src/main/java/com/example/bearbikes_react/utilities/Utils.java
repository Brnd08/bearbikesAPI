package com.example.bearbikes_react.utilities;

import java.util.Random;

public class Utils {
    private static Random randomizer = new Random();
    public static String generateRandomToken(int length){
        StringBuilder randomToken = new StringBuilder();
        for (int i = 0; i < length; i++) {
            randomToken.append((char) randomizer.nextInt(48, 123));
        }
        return randomToken.toString();
    }
}
