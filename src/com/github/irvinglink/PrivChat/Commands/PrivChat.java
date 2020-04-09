package com.github.irvinglink.PrivChat.Commands;

import com.github.irvinglink.PrivChat.Handlers.*;
import com.github.irvinglink.PrivChat.MClass;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PrivChat implements CommandExecutor {

    private final MClass plugin;
    private final Chat chat;
    private final FileConfiguration lang;

    public PrivChat(MClass plugin) {

        this.plugin = plugin;

        this.chat = plugin.getChat();

        this.lang = plugin.getLang();

    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {

            Player player = (Player) sender;

            UUID uuid = player.getUniqueId();

            if (args.length == 0 || args[0].equalsIgnoreCase("help")) {

                player.sendMessage(chat.getPrefix() + chat.setString("&e/privchat invite &b<player> &aInvite a player to chat&7.").color());

                player.sendMessage(chat.getPrefix() + chat.setString("&e/privchat accept &b<player> &aAccept a player invitation&7.").color());

                player.sendMessage(chat.getPrefix() + chat.setString("&e/privchat leave &aLeave from the chat&7.").color());

                player.sendMessage(chat.getPrefix() + chat.setString("&e/privchat kick &b<player> &aKick a player from the chat&7.").color());

                player.sendMessage(chat.getPrefix() + chat.setString("&e/privchat toggle &aToggle chat&7.").color());

                player.sendMessage(chat.getPrefix() + chat.setString("&e/privchat delete &aDelete the chat&7.").color());

                if (player.hasPermission("PrivChat.Admin")) {

                    player.sendMessage(chat.getPrefix() + chat.setString("&e/privchat silentjoin &aJoin to a chat in silent mode&7.").color());

                    player.sendMessage(chat.getPrefix() + chat.setString("&e/privchat reload &aReload the config file&7.").color());

                }


                return true;
            }

            if (args[0].equalsIgnoreCase("invite")) {

                if (args.length == 2) {

                    String targetName = args[1];

                    Player target = Bukkit.getPlayer(targetName);

                    if (target != null) {

                        if (!(target.getUniqueId().equals(uuid))) {

                            UUID member = target.getUniqueId();

                            Waiting waiting = new Waiting(member, uuid);

                            waiting.setWaiting();

                            player.sendMessage(chat.getPrefix() + StringReplace.replace(player, target, lang.getString("Sent_Invitation"), true));

                            target.sendMessage(chat.getPrefix() + StringReplace.replace(player, target, lang.getString("Invitation"), true));

                        } else
                            player.sendMessage(chat.getPrefix() + StringReplace.replace(player, target, lang.getString("No_Self_Invitation"), true));

                    } else
                        player.sendMessage(chat.getPrefix() + StringReplace.replace(player, targetName, lang.getString("Player_Not_Online"), true));

                } else
                    player.sendMessage(chat.getPrefix() + chat.setString("&cSyntax Error. &b/privchat invite &a<player>").color());

                return true;
            }

            if (args[0].equalsIgnoreCase("accept")) {

                if (args.length == 2) {

                    String channelOwnerName = args[1];

                    Player channelOwner = Bukkit.getPlayer(channelOwnerName);

                    if (channelOwner != null) {

                        UUID channelOwnerUUID = channelOwner.getUniqueId();

                        Waiting waiting = new Waiting(uuid, channelOwnerUUID);

                        if (!(Channel.isInChat(uuid))){

                            if (waiting.isWaiting()) {

                                if (waiting.channelWaiting()) {

                                    Channel channel = new Channel(channelOwnerUUID);

                                    channel.add(uuid);

                                    channel.broadcastMessage(chat.getPrefix() + StringReplace.replace(player, channelOwnerName, lang.getString("Chat_Join_Chat"), true));

                                } else
                                    player.sendMessage(chat.getPrefix() + StringReplace.replace(player, channelOwner, lang.getString("No_Invitation_From"), true));

                            } else
                                player.sendMessage(chat.getPrefix() + StringReplace.replace(player, channelOwnerName, lang.getString("No_Invitations"), true));

                        } else
                            player.sendMessage(chat.getPrefix() + StringReplace.replace(player, channelOwner, lang.getString("Player_In_Chat"), true));

                    } else
                        player.sendMessage(chat.getPrefix() + StringReplace.replace(player, channelOwnerName, lang.getString("Player_Not_Online"), true));

                } else
                    player.sendMessage(chat.getPrefix() + chat.setString("&cSyntax Error. &b/privchat accept &a<player>").color());

                return true;
            }

            if (args[0].equalsIgnoreCase("leave")) {

                if (args.length == 1) {

                    if (Channel.isInChat(uuid)) {

                        if (!(Channel.getChat(uuid).getOwner().equals(uuid))) {
                            Channel channel = Channel.getChat(uuid);

                            assert channel != null;
                            channel.remove(uuid);

                            channel.broadcastMessage(chat.getPrefix() + StringReplace.replace(player, (OfflinePlayer) null, lang.getString("Chat_Leave_Chat"), true));

                            player.sendMessage(chat.getPrefix() + StringReplace.replace(player, (OfflinePlayer) null, lang.getString("Player_Leave"), true));

                        } else
                            player.sendMessage(chat.getPrefix() + StringReplace.replace(player, (OfflinePlayer) null, lang.getString("Owner_Leave"), true));


                    }

                } else
                    player.sendMessage(chat.getPrefix() + chat.setString("&cSyntax Error. &b/privchat leave").color());

                return true;
            }

            if (args[0].equalsIgnoreCase("toggle")) {

                if (args.length == 1) {

                    Waiting waiting = new Waiting(uuid);

                    if (Channel.isInChat(uuid)) {

                        Channel channel = Channel.getChat(uuid);

                        assert channel != null;
                        waiting.setToggle(channel.getOwner());

                        channel.remove(uuid);

                        player.sendMessage(chat.getPrefix() + StringReplace.replace(player, (OfflinePlayer) null, lang.getString("Disable_Chat"), true));

                    } else if (waiting.toggled()) {

                        Channel channel = new Channel(waiting.getToggledChat());

                        channel.add(uuid);

                        player.sendMessage(chat.getPrefix() + StringReplace.replace(player, (OfflinePlayer) null, lang.getString("Enable_Chat"), true));

                        waiting.removeToggle();

                    } else
                        player.sendMessage(chat.getPrefix() + StringReplace.replace(player, (OfflinePlayer) null, lang.getString("No_Chat"), true));

                    return true;

                } else
                    player.sendMessage(chat.getPrefix() + chat.setString("&cSyntax Error. &b/privchat toggle").color());

            }

            if (args[0].equalsIgnoreCase("delete")) {

                if (args.length == 1) {

                    if (Channel.isInChat(uuid)) {

                        Channel channel = Channel.getChat(uuid);

                        assert channel != null;
                        channel.broadcastMessage(chat.getPrefix() + StringReplace.replace(player, (OfflinePlayer) null, lang.getString("No_Chat"), true));

                        channel.delete();

                    } else
                        player.sendMessage(chat.getPrefix() + StringReplace.replace(player, (OfflinePlayer) null, lang.getString("No_Chat"), true));

                } else
                    player.sendMessage(chat.getPrefix() + chat.setString("&cSyntax Error. &b/privchat delete").color());

                return true;
            }
            if (args[0].equalsIgnoreCase("kick")) {

                if (args.length == 2) {

                    if (Channel.isInChat(uuid)) {

                        Channel channel = Channel.getChat(uuid);

                        assert channel != null;
                        if (channel.getOwner().equals(uuid)) {

                            String targetName = args[1];
                            Player target = Bukkit.getPlayer(targetName);

                            if (target != null) {

                                if (!(uuid.equals(target.getUniqueId()))) {

                                    if (channel.containsPlayer(uuid)) {

                                        channel.remove(target.getUniqueId());

                                        channel.broadcastMessage(chat.getPrefix() + StringReplace.replace(player, targetName, lang.getString("Player_Kick"), true));

                                    } else
                                        player.sendMessage(chat.getPrefix() + StringReplace.replace(player, targetName, lang.getString("Player_Not_In_Chat"), true));

                                } else
                                    player.sendMessage(chat.getPrefix() + StringReplace.replace(player, targetName, lang.getString("No_Self_Kick"), true));

                            } else
                                player.sendMessage(chat.getPrefix() + StringReplace.replace(player, targetName, lang.getString("Player_Not_Online"), true));

                        } else
                            player.sendMessage(chat.getPrefix() + StringReplace.replace(player, (OfflinePlayer) null, lang.getString("No_Chat_Owner"), true));

                    } else
                        player.sendMessage(chat.getPrefix() + StringReplace.replace(player, (OfflinePlayer) null, lang.getString("No_Chat"), true));

                } else
                    player.sendMessage(chat.getPrefix() + chat.setString("&cSyntax Error. &b/privchat kick &a<player>").color());

                return true;
            }

            if (args[0].equalsIgnoreCase("admin") && player.hasPermission("PrivChat.Admin")) {

                if (args[1].equalsIgnoreCase("silentjoin")) {

                    if (args.length == 3) {

                        String channelOwnerName = args[2];

                        Player target = Bukkit.getPlayer(channelOwnerName);

                        if (target != null) {

                            UUID channelOwnerUUID = target.getUniqueId();

                            Channel channel = new Channel(channelOwnerUUID);

                            channel.add(uuid);

                            player.sendMessage(chat.getPrefix() + StringReplace.replace(player, channelOwnerName, lang.getString("Silent_Mode_Join"), true));

                        } else
                            player.sendMessage(chat.getPrefix() + StringReplace.replace(player, channelOwnerName, lang.getString("Player_Not_Online"), true));

                    } else
                        player.sendMessage(chat.getPrefix() + chat.setString("&cSyntax Error. &b/privchat admin silentjoin &a<player>").color());

                    return true;
                }

                if (player.hasPermission("PrivChat.Admin.Reload") && args[1].equalsIgnoreCase("reload")) {

                    if (args.length == 2) {

                        plugin.reloadConfig();

                        player.sendMessage(plugin.getChat().setString(plugin.getPrefix() + "&aConfig successfully reloaded!").color());

                    } else
                        player.sendMessage(chat.getPrefix() + chat.setString("&cSyntax Error. &b/privchat admin reload").color());

                    return true;

                }

                return true;

            } else
                player.sendMessage(chat.getPrefix() + StringReplace.replace(player, (String) null, lang.getString("Unknown_Chat"), true));

        }

        return false;
    }
}
