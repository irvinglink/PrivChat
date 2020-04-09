package com.github.irvinglink.PrivChat.Handlers;

import java.util.*;

public class Waiting {

    private final UUID member;
    private UUID owner;
    private static Map<UUID, List<UUID>> waitingList = new HashMap<>();
    private static Map<UUID, UUID> toggleList = new HashMap<>();

    public Waiting(UUID member){
        this.member = member;
    }

    public Waiting(UUID member, UUID owner) {
        this.owner = owner;
        this.member = member;
    }

    public void setWaiting(){

        if (!(isWaiting())) {
            waitingList.put(member, new ArrayList<>());
        }

        waitingList.get(member).add(owner);

    }

    public void remove(){

        if (isWaiting()){

            waitingList.get(member).remove(owner);

            if (waitingList.get(member).isEmpty()){
                waitingList.remove(member);
            }

        }
    }

    public boolean isWaiting(){
        return waitingList.containsKey(member);
    }

    public boolean channelWaiting(){

        if (isWaiting()){
            return waitingList.get(member).contains(owner);
        }

        return false;
    }

    public UUID getToggledChat(){
        return toggleList.get(member);
    }

    public void setToggle(UUID owner){
        toggleList.put(member, owner);
    }

    public void removeToggle() {
        toggleList.remove(member);
    }

    public boolean toggled(){
        return toggleList.containsKey(member);
    }

}
