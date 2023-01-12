package me.mrdev.cl.placeholders;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.mrdev.cl.CafeLevel;
import me.mrdev.cl.profile.PlayerProfile;
import me.mrdev.cl.profile.ProfileManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CafeExpansion extends PlaceholderExpansion {

    private CafeLevel plugin;
    private ProfileManager manager;

    public CafeExpansion(CafeLevel plugin) {
        this.plugin = plugin;
        manager = plugin.getManager();

    }


    @Override
    public @NotNull String getIdentifier() {
        return "cxp";
    }

    @Override
    public @NotNull String getAuthor() {
        return plugin.getDescription().getAuthors().get(0);
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        if(player == null) return "ERROR";
        PlayerProfile profile = manager.getProfile(player.getUniqueId());
        if(profile == null) return "PROFILE_NOT_FOUND";
        return switch (params) {
            case "level" -> String.valueOf(profile.getLevel());
            case "level_progressbar" -> manager.getProgressBar(profile.getLevel() , profile.getXp());
            case "level_progress"  -> "Next level progress " + manager.getProgressPercentage(profile.getLevel(), profile.getXp()) + "%";
            case "level_xpamount" -> profile.getXp() + "xp";
            case "level_neededxp" -> manager.getNextRequiredXp(profile.getLevel(), profile.getXp()) + "xp";
            default -> "UNKNOWN";
        };
    }
}
