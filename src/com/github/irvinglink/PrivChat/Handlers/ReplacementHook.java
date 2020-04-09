package com.github.irvinglink.PrivChat.Handlers;

import org.bukkit.OfflinePlayer;

public class ReplacementHook {

    public String replaceHook(OfflinePlayer executor, String target, String next) {

        if (executor != null) {

            switch (next) {

                case "name":
                    return executor.getName();

                case "target":
                    if (target != null){
                        return target;
                    }
            }

        }

        return null;
    }

    public String replaceHook(OfflinePlayer executor, OfflinePlayer target, String next) {

        if (executor != null) {

            switch (next) {

                case "name":
                    return executor.getName();

                case "target":
                    if (target != null){
                        return target.getName();
                    }
            }

        }

        return null;
    }
}
