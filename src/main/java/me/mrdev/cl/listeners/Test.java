package me.mrdev.cl.listeners;

import me.mrdev.cl.CafeLevel;
import me.mrdev.cl.events.PlayerLevelUpEvent;
import me.mrdev.cl.profile.ProfileManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Instrument;
import org.bukkit.Note;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.SQLException;

public class Test implements Listener {

    private ProfileManager manager;

    public Test(CafeLevel plugin) {
        manager = plugin.getManager();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) throws SQLException {
        Player player = event.getPlayer();
        if(!manager.hasProfile(player.getUniqueId())) {
            manager.addProfile(player.getUniqueId());
            player.sendMessage("successfully added a profile for you!");
            return;
        }
        player.sendMessage("You already have a profile!");

    }

    @EventHandler
    public void onKill(EntityDamageByEntityEvent event) {
        if(event.getDamager() instanceof Player player && event.getEntity() instanceof Damageable e) {
            if(event.getDamage() >= e.getHealth()) {
                try {
                    manager.addXp(manager.getProfile(player.getUniqueId()), 50);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                player.sendMessage(ChatColor.GREEN + "+50 xp");
            }
        }
    }

    @EventHandler
    public void onLevelup(PlayerLevelUpEvent event) {
        Player player = Bukkit.getPlayer(event.getProfile().getID());
        player.sendMessage("You leveled up to " + event.getLevel());
        player.playNote(player.getLocation(), Instrument.CHIME, Note.flat(1, Note.Tone.A));
    }

}
