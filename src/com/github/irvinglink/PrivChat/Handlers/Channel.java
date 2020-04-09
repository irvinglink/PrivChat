package com.github.irvinglink.PrivChat.Handlers;

import com.github.irvinglink.PrivChat.MClass;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class Channel extends Chat {

    private static Map<UUID, List<UUID>> channels = new HashMap<>();
    private final UUID owner;

    public Channel(UUID owner) {
        this.owner = owner;
    }

    public void delete() {

        if (exists()) {
            getChannels().remove(owner);
        }

    }

    public void add(UUID uuid) {

        if (!(exists())) {

            List<UUID> uuidList = new ArrayList<>();

            uuidList.add(owner);

            getChannels().put(owner, uuidList);

        }

        if (exists()) {

            if (!(containsPlayer(uuid))) {

                getMembers().add(uuid);

            }
        }

        Waiting waiting = new Waiting(uuid, owner);

        if (waiting.isWaiting()) waiting.remove();

    }

    public void remove(UUID uuid) {

        if (exists() && containsPlayer(uuid)) {

            getMembers().remove(uuid);
            
        }

    }

    public boolean containsPlayer(UUID uuid) {

        if (exists()) {
            return getMembers().contains(uuid);
        }

        return false;

    }

    public boolean exists() {
        return channels.containsKey(owner);
    }

    public void broadcastMessage(String text) {

        if (exists()) {

            for (UUID uuid : getMembers()) {

                Player target = Bukkit.getPlayer(uuid);

                if (target != null) {

                    target.sendMessage(setString(text).color());

                }

            }

        }
    }

    public void sendMessage(Player player, String message, MClass plugin) {

        if (exists()) {

            for (UUID uuid : getMembers()) {

                Player target = Bukkit.getPlayer(uuid);

                if (target != null) {

                    target.sendMessage(setString(plugin.getConfig().getString("format").replaceAll("%message%", message).
                            replaceAll("%player_name%", player.getName())).color());

                }

            }

        }
    }

    public UUID getOwner() {
        return this.owner;
    }

    public List<UUID> getMembers() {

        if (exists()) {

            return getChannels().get(owner);

        }

        return null;
    }

    public static Set<UUID> getOwners() {

        if (!(getChannels().isEmpty())) {

            return getChannels().keySet();
        }

        return null;
    }

    public static Map<UUID, List<UUID>> getChannels() {
        return channels;
    }

    public static Channel getChat(UUID uuid) {

        if (getOwners().contains(uuid)) return new Channel(uuid);

        else {

            for (UUID owner : getOwners()) {

                Channel channel = new Channel(owner);

                if (channel.containsPlayer(uuid)) return new Channel(owner);
            }

        }

        return null;
    }

    public static boolean isInChat(UUID uuid) {

        for (UUID owner : getChannels().keySet()) {

            for (UUID member : getChannels().get(owner)) {

                if (member.equals(uuid)) return true;

            }

        }

        return false;
    }

}
