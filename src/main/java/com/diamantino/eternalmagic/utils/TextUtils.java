package com.diamantino.eternalmagic.utils;

public class TextUtils {
    public static String formatNumberWithPrefix(double number) {
        String[] suffixes = new String[]{"", "K", "M", "B", "T", "P", "E", "Z", "Y"};
        int suffixIndex = 0;
        double formattedNumber = number;

        while (formattedNumber >= 1000 && suffixIndex < suffixes.length - 1) {
            formattedNumber /= 1000;
            suffixIndex++;
        }

        return String.format("%.1f", formattedNumber) + " " + suffixes[suffixIndex];
    }

    public static String removeUnderscoresAndCapitalize(String str) {
        String[] words = str.split("_");
        StringBuilder result = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                String capitalizedWord = word.substring(0, 1).toUpperCase() + word.substring(1);
                result.append(capitalizedWord);
            }
        }

        return result.toString();
    }

    public static String removeBeforeLastSlash(String str) {
        int slashIndex = str.lastIndexOf("/");
        if (slashIndex != -1) {
            return str.substring(slashIndex + 1);
        }
        return str;
    }
}
