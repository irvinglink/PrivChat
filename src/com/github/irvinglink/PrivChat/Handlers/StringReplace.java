package com.github.irvinglink.PrivChat.Handlers;

import org.bukkit.OfflinePlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringReplace {

    private static Map<String, ReplacementHook> hookMap = new HashMap<>();
    private static Pattern pattern = Pattern.compile("[%]([^%]+)[%]");

    public void register() {

        hookMap.put("player", new ReplacementHook());

    }

    public static String replace(OfflinePlayer player, String target, String text, boolean color) {

        if (text == null) return null;

        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {

            String var = matcher.group(1);
            int index = var.indexOf("_");

            if (index <= 0 || index >= var.length()) continue;

            String identifier = var.substring(0, index);
            String next = var.substring(index + 1);

            if (hookMap.containsKey(identifier)) {


                String value = hookMap.get(identifier).replaceHook(player, target, next);


                if (value != null)
                    text = text.replaceAll(Pattern.quote(matcher.group()), Matcher.quoteReplacement(value));

            }
        }

        return color ? new Chat(text).color() : text;
    }

    public static String replace(OfflinePlayer player, OfflinePlayer target, String text, boolean color) {

        if (text == null) return null;

        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {

            String var = matcher.group(1);
            int index = var.indexOf("_");

            if (index <= 0 || index >= var.length()) continue;

            String identifier = var.substring(0, index);
            String next = var.substring(index + 1);

            if (hookMap.containsKey(identifier)) {


                String value = hookMap.get(identifier).replaceHook(player, target, next);


                if (value != null)
                    text = text.replaceAll(Pattern.quote(matcher.group()), Matcher.quoteReplacement(value));

            }
        }

        return color ? new Chat(text).color() : text;
    }
}
