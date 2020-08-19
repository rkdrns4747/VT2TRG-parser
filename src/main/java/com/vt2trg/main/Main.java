package com.vt2trg.main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Main extends JavaPlugin {
    private static Main instance;
    private static boolean vtAvailabilty;
    private static boolean trgAvailablity;
    public static final String defaultPrefix = "§a《§3VT2TRG§a》§f ";
    public static final String warningPrefix = "§4[WARNING]§f ";
    public void main(String[] args){
    }
    public void onEnable(){
        Bukkit.getScheduler().runTaskLater(this, () -> {
            vtAvailabilty = (Bukkit.getPluginManager().getPlugin("VariableTriggers") != null);
            trgAvailablity = (Bukkit.getPluginManager().getPlugin("TriggerReactor") != null);
            if(!trgAvailablity || !vtAvailabilty){
                Bukkit.getConsoleSender().sendMessage(warningPrefix+"§cVariableTrigger or TriggerReactor could be found on this server!\n");
                Bukkit.getConsoleSender().sendMessage("TriggerReactor: "+ (trgAvailablity ? ChatColor.GREEN +"true\n" : Color.fromRGB(255, 5, 5) + "false\n"));
                Bukkit.getConsoleSender().sendMessage("VariableTriggers: " + (vtAvailabilty ? ChatColor.GREEN +"true\n" : ChatColor.RED + "false\n"));
                Bukkit.getPluginManager().disablePlugin(this);
            }else{
                Bukkit.getConsoleSender().sendMessage(defaultPrefix + ChatColor.GREEN +"Plugin successfully loaded.");
            }
        }, 60L);
    }
    public static JavaPlugin getInstance(){

        return instance;
    }
}
