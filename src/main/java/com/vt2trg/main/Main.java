package com.vt2trg.main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;

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
            if(isTrgAvailable() || isVtAvailable()){
                Bukkit.getConsoleSender().sendMessage(warningPrefix+"§cVariableTrigger or TriggerReactor could be found on this server!\n");
                Bukkit.getConsoleSender().sendMessage("TriggerReactor: "+ (trgAvailablity ? ChatColor.GREEN +"true\n" : Color.fromRGB(255, 5, 5) + "false\n"));
                Bukkit.getConsoleSender().sendMessage("VariableTriggers: " + (vtAvailabilty ? ChatColor.GREEN +"true\n" : ChatColor.RED + "false\n"));
                Bukkit.getPluginManager().disablePlugin(this);
            }else{
                Bukkit.getConsoleSender().sendMessage(defaultPrefix + ChatColor.GREEN +"Plugin successfully loaded.");
            }
        }, 60L);
    }
    /**
     * get instance of Main class of this plugin
     *
     * @return instance of Main class
     */
    public static JavaPlugin getInstance(){

        return instance;
    }
    /**
     * get availability of TriggerReactor plugin
     *
     * @return true if TriggerReactor is in server, or else, returns false
     */
    public static boolean isTrgAvailable(){
        return Bukkit.getPluginManager().getPlugin("TriggerReactor") != null;
    }
    /**
     * get availability of VariableTriggers plugin
     *
     * @return true if VariableTriggers is in server, or else, returns false
     */
    public static boolean isVtAvailable(){
        return Bukkit.getPluginManager().getPlugin("VariableTriggers") != null;
    }
    /**
     * get TriggerReactor's version as int
     *
     * For example, if it's version is 3.0.5, this method
     * will return 305 as int
     *
     * @return version of the plugin as int.
     */
    public static int getTrgVersion(){
        return Integer.parseInt(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("TriggerReactor")).getDescription().getVersion().replace(".", ""));
    }
}
