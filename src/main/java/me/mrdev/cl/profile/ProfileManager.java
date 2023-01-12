package me.mrdev.cl.profile;

import me.mrdev.cl.CafeLevel;
import me.mrdev.cl.events.PlayerLevelUpEvent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class ProfileManager {

    private final ArrayList<PlayerProfile> profiles;
    private ProfileDataManager manager;
    private CafeLevel plugin;

    public ProfileManager(CafeLevel plugin) throws SQLException, ClassNotFoundException {
        this.plugin = plugin;
        manager = new ProfileDataManager(plugin);
        manager.loadDB();
        plugin.getLogger().info("Connected to the database!");
        plugin.getLogger().info("Now loading profiles...");
        profiles = manager.loadProfiles();
        if(profiles.isEmpty()) {
            plugin.getLogger().info("no profiles found!");
        }else {
            plugin.getLogger().info("Successfully loaded profiles:");
            profiles.forEach(p -> plugin.getLogger().info(p.getID().toString() + " | lvl: " + p.getLevel() + " | xp: " + p.getXp()));
        }
    }

    public PlayerProfile getProfile(UUID uuid) {
        return profiles.stream().filter(p -> uuid.equals(p.getID())).findFirst().orElse(null);
    }

    public boolean hasProfile(UUID uuid) {
        return profiles.stream().anyMatch(p -> p.getID().equals(uuid));
    }

    public void addProfile(UUID uuid) throws SQLException {
        if(hasProfile(uuid)) return;
        PlayerProfile profile = new PlayerProfile(uuid , 0 , 0);
        profiles.add(profile);
        manager.addProfile(profile);
    }

    public void addXp(PlayerProfile profile, double xp) throws SQLException {
        if(profile == null) return;
        profile.addXp(xp);
        if(profile.getLevel() == 0 && profile.getXp() < getRequiredXp(1)) {
            return;
        }
        if(profile.getXp() >= getRequiredXp(profile.getLevel())) {
            profile.setLevel(getXpToLevel(profile.getXp()));
            plugin.getServer().getPluginManager().callEvent(new PlayerLevelUpEvent(profile));
        }
        manager.updateProfile(profile);
    }

    public void setXp(PlayerProfile profile, double xp) throws SQLException {
        if(profile == null) return;
        profile.setXp(xp);
        if(profile.getLevel() == 0 && profile.getXp() < getRequiredXp(1)) {
            return;
        }
         if(xp > getRequiredXp(profile.getLevel())) {
            plugin.getServer().getPluginManager().callEvent(new PlayerLevelUpEvent(profile));
        }
        profile.setLevel(profile.getXp() < getRequiredXp(1) ? 0 : getXpToLevel(profile.getXp()));
        manager.updateProfile(profile);
    }

    public void removeXp(PlayerProfile profile, double xp) throws SQLException {
        if(profile == null) return;
        profile.addXp(-xp);
        if(profile.getLevel() == 0 && profile.getXp() < getRequiredXp(1)) {
            return;
        }
        if(profile.getXp() < getRequiredXp(profile.getLevel())) {
            profile.setLevel(profile.getXp() < getRequiredXp(1) ? 0 : getXpToLevel(profile.getXp()));
        }
        manager.updateProfile(profile);
    }

    public void setLevel(PlayerProfile profile , int level) {
        if(profile == null) return;
        if(profile.getLevel() < level) {
            plugin.getServer().getPluginManager().callEvent(new PlayerLevelUpEvent(profile));
        }
        profile.setLevel(level);
        profile.setXp(getLevelToXp(level));
    }

    public int getXpToLevel(double xp) {
        return (int)(xp - 100) / 20;
    }

    public double getLevelToXp(int level) {
        return 20 * level + 100;
    }

    public double getRequiredXp(int level) {
        if(level == 0) return 0;
        return getLevelToXp(level);
    }

    public double getNextRequiredXp(int level , double xp) {
        plugin.getLogger().info("required xp for levelup " + level);
        return getLevelToXp(level + 1) - xp; //get the required xp to levelup
    }

    public double getProgressPercentage(int level, double xp) {
        return 100 - getNextRequiredXp(level, xp) * 10 / 2;
    }

    public String getProgressBar(int level, double xp) {
        String progressbar = "[|||||||||||||||||||||||||||||||||||||||]";
        StringBuilder builder = new StringBuilder();
        int amount = (int) (getProgressPercentage(level, xp) / 100 * progressbar.length() - 2);
        for (int i = 0; i < progressbar.length() ; i++) {
            char c = progressbar.charAt(i);
            if(i == 0 || i == progressbar.length() - 1) {
                builder.append(ChatColor.RESET + (String.valueOf(c)));
                continue;
            }
            if(i <= amount) {
                builder.append(ChatColor.GREEN + String.valueOf(c));
                continue;
            }
            builder.append(ChatColor.RED + String.valueOf(c));
        }
        return builder.toString();
    }

    public LinkedHashMap<UUID, Double> getLeaderBoard() {
        LinkedHashMap<UUID, Double> map = new LinkedHashMap<>();
        profiles.forEach(p -> map.put(p.getID(), p.getXp()));
        return map.entrySet().stream().sorted(Comparator.comparingDouble(Map.Entry::getValue)).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a,b)->b, LinkedHashMap::new));
    }


}
