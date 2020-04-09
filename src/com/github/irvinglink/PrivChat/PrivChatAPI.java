package com.github.irvinglink.PrivChat;

import com.github.irvinglink.PrivChat.Handlers.Channel;
import com.github.irvinglink.PrivChat.Handlers.StringReplace;
import com.github.irvinglink.PrivChat.Handlers.Waiting;

import java.util.UUID;

public class PrivChatAPI {

    public static PrivChatAPI instance = new PrivChatAPI();
    
    public MClass getMainClass(){
        return MClass.plugin;
    }

    public Channel channel(UUID owner){
        return new Channel(owner);
    }

    public Waiting waiting(UUID member, UUID owner){
        return new Waiting(member, owner);
    }

    public StringReplace stringReplace(){
        return new StringReplace();
    }

}
