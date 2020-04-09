package com.github.irvinglink.PrivChat.Handlers;

import org.bukkit.ChatColor;

public class Chat {

    private String textToTranslate;

    public Chat(){}

    public Chat(String textToTranslate){
        this.textToTranslate = textToTranslate;
    }

    public Chat setString(String textToTranslate){
        return new Chat(textToTranslate);
    }

    public String getString(){
        return textToTranslate;
    }

    public String color(){
        return ChatColor.translateAlternateColorCodes('&', textToTranslate);
    }

    public String getPrefix(){
        return setString("&7[&6Priv&bChat&7] &a").color();
    }

}
