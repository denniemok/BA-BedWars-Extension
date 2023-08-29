package com.battleasya.Cmd;

import com.battleasya.Util.General;
import com.battleasya.BWExtension;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

public class Booster implements CommandExecutor {

    private final BWExtension plugin;

    public Booster(BWExtension plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        if (args.length == 0) {
            printHelp(sender);
            return true;
        }

        if (args.length == 1) {

            if (args[0].equalsIgnoreCase("status")) {

                if (plugin.boosterInAction) {
                    long secondsLeft = 3600 - ( System.currentTimeMillis() / 1000 - plugin.timeStamp / 1000 );
                    General.sendMessage(sender, "&8[&3Booster&8] &aSponsor: " + plugin.activatedBy);
                    General.sendMessage(sender, "&8[&3Booster&8] &aDuration: " + secondsLeft + " seconds left");
                    General.sendMessage(sender, "&8[&3Booster&8] &aModifier: " + plugin.expModifier);
                } else {
                    General.sendMessage(sender, "&8[&3Booster&8] &cThere is currently no active booster.");
                }

                return true;

            }

            if (args[0].equalsIgnoreCase("activate")) {

                if(!sender.hasPermission("booster.use")) {
                    General.sendMessage(sender, "&8[&3Booster&8] &cPurchase Royale Membership To Unlock This Feature!");
                    return true;
                }

                if (plugin.consumedList.containsKey(sender.getName())) {
                    General.sendMessage(sender, "&8[&3Booster&8] &cYou can only do so once per day!");
                    return true;
                }

                if (plugin.boosterInAction) {
                    General.sendMessage(sender, "&8[&3Booster&8] &cThere is already an active booster!");
                } else {
                    activateBooster(sender, !sender.hasPermission("booster.admin"));
                }

                return true;

            }

        }

        printHelp(sender);
        return true;

    }

    public void printHelp(CommandSender sender) {
        General.sendMessage(sender, "");
        General.sendMessage(sender, "&8[&3Booster&8] &7Plugin made by &aKaytlynJay&7.");
        General.sendMessage(sender, "&8[&3Booster&8] &c/booster status");
        General.sendMessage(sender, "&8[&3Booster&8] &c/booster activate");
        General.sendMessage(sender, "");
    }

    public void activateBooster(CommandSender sender, Boolean consumption) {

        plugin.boosterInAction = true;
        plugin.activatedBy = sender.getName();
        plugin.timeStamp = System.currentTimeMillis();

        if (consumption) {
            plugin.consumedList.put(sender.getName(), 1);
        }

        (new BukkitRunnable() {
            public void run() {
                plugin.boosterInAction = false;
            }
        }).runTaskLater(plugin, 20L * 3600);

        General.sendMessage(sender, "&8[&3Booster&8] &aBooster Activated [1 Hour].");

        General.broadcastMessage("");
        General.broadcastMessage("&7&m---»--*-------------------------------------*--«---");
        General.broadcastMessage("  &6" + sender.getName() + "&e is now boosting your exp gain for an hour!");
        General.broadcastMessage("");
        General.broadcastMessage("  &7Purchase &fRoyale Membership &7to activate daily boosters!");
        General.broadcastMessage("&7&m---»--*-------------------------------------*--«---");
        General.broadcastMessage("");

    }

}