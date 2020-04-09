package com.github.irvinglink.PrivChat.Listeners;

import com.github.irvinglink.PrivChat.Handlers.Channel;
import com.github.irvinglink.PrivChat.Handlers.Chat;
import com.github.irvinglink.PrivChat.MClass;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.UUID;

public class AsyncChat implements Listener {

    private final MClass plugin;
    private final Chat chat;

    public AsyncChat(MClass plugin) {
        this.plugin = plugin;
        this.chat = plugin.getChat();
    }

    @EventHandler
    public void asyncChat(AsyncPlayerChatEvent event) {

        UUID uuid = event.getPlayer().getUniqueId();

        if (Channel.isInChat(uuid)) {

            Channel channel = Channel.getChat(uuid);

            assert channel != null;
            channel.sendMessage(event.getPlayer(), event.getMessage(), plugin);

            event.setCancelled(true);

        }
    }
}
