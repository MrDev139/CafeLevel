package me.mrdev.cl.events;

import me.mrdev.cl.profile.PlayerProfile;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerLevelUpEvent extends Event {

    private static final HandlerList handlerList = new HandlerList();
    private PlayerProfile profile;
    private int level;

    public PlayerLevelUpEvent(PlayerProfile profile) {
        this.profile = profile;
        level = profile.getLevel();
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public PlayerProfile getProfile() {
        return profile;
    }

    public int getLevel() {
        return level;
    }

}
