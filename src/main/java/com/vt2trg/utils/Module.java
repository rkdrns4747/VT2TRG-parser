package com.vt2trg.utils;

import com.vt2trg.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Module {
    public final JavaPlugin plugin = Main.getInstance();
    protected final Plugin triggerReactor = Bukkit.getPluginManager().getPlugin("TriggerReactor");
    protected final Plugin variableTriggers = Bukkit.getPluginManager().getPlugin("VariableTriggers");
    protected final File trgDefaultPath = new File(triggerReactor.getDataFolder().getPath());
}
