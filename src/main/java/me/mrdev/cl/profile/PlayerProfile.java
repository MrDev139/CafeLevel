package me.mrdev.cl.profile;

import me.mrdev.cl.events.PlayerLevelUpEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerProfile {

    private UUID id;
    private double xp;
    private int level;

    public PlayerProfile(UUID id, int level, double xp) {
        this.id = id;
        this.xp = xp;
        this.level = level;
    }

    public UUID getID() {
        return id;
    }

    public int getLevel() {
        return level;
    }

    public double getXp() {
        return xp;
    }

    public void addXp(double xp) {
        this.xp += xp;
    }

    public void setXp(double xp) {
        this.xp = xp;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void addLevel(int level) {
        this.level += level;
    }

}
