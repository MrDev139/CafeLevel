package me.mrdev.cl.commands;

import me.mrdev.cl.CafeLevel;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.*;

public class CafeCmd implements CommandExecutor {

    private CafeLevel plugin;

    public CafeCmd(CafeLevel plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length >= 1) {
            String subcmd = args[0];
            switch (subcmd) {
                case "help" -> {
                    sender.sendMessage("current subcommands:" ,"/cxp add(x,xp)","/cxp remove(p,xp)","/cxp set(p,xp)","/cxp leaderboard");
                }
                case "leaderboard" -> {
                    sender.sendMessage("Leaderboard: ");
                    LinkedHashMap<UUID, Double> map = plugin.getManager().getLeaderBoard();
                    ArrayList<UUID> sortedIDs = new ArrayList<>(map.keySet());
                    Collections.reverse(sortedIDs);
                    sortedIDs.forEach(u -> {
                        sender.sendMessage(plugin.getServer().getOfflinePlayer(u) + " | " + map.get(u) + "xp");
                    });
                }
                case "add" -> {
                    if(args.length != 3) {
                        sender.sendMessage("wrong arguments, use /̣̣cxp add <player> <xp>");
                        break;
                    } //cxp 0 1 2
                    Player player = plugin.getServer().getPlayer(args[1]);
                    if(player == null) {
                        sender.sendMessage("player's not online");
                        break;
                    }
                    if(!isDouble(args[2])) {
                        sender.sendMessage("The xp amount needs to be a decimal number!");
                    }
                    double amount = Double.parseDouble(args[2]);
                    try {
                        plugin.getManager().addXp(plugin.getManager().getProfile(player.getUniqueId()), amount);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    sender.sendMessage("Successfully added " + amount + "xp to " + player.getName());
                }
                case "remove" -> {
                    if(args.length != 3) {
                        sender.sendMessage("wrong arguments, use /̣̣cxp remove <player> <xp>");
                        break;
                    } //cxp 0 1 2
                    Player player = plugin.getServer().getPlayer(args[1]);
                    if(player == null) {
                        sender.sendMessage("player's not online");
                        break;
                    }
                    if(!isDouble(args[2])) {
                        sender.sendMessage("The xp amount needs to be a decimal number!");
                    }
                    double amount = Double.parseDouble(args[2]);
                    try {
                        plugin.getManager().removeXp(plugin.getManager().getProfile(player.getUniqueId()), amount);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    sender.sendMessage("Successfully removed " + amount + "xp of " + player.getName());
                }
                case "set" -> {
                    if(args.length != 3) {
                        sender.sendMessage("wrong arguments, use /̣̣cxp set <player> <xp>");
                        break;
                    } //cxp 0 1 2
                    Player player = plugin.getServer().getPlayer(args[1]);
                    if(player == null) {
                        sender.sendMessage("player's not online");
                        break;
                    }
                    if(!isDouble(args[2])) {
                        sender.sendMessage("The xp amount needs to be a decimal number!");
                    }
                    double amount = Double.parseDouble(args[2]);
                    try {
                        plugin.getManager().setXp(plugin.getManager().getProfile(player.getUniqueId()), amount);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    sender.sendMessage("Successfully set " + player.getName() + "'s xp to " + amount);
                }
                default -> sender.sendMessage("Unknown subcommand type /cxp help for more information");
            }
            return true;
        }
        sender.sendMessage("use /cxp help for more information");
        return true;
    }

    private boolean isDouble(String input) {
        try {
            Double.parseDouble(input);
        }catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

}
