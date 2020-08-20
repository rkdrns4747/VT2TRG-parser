package com.vt2trg.utils;

import com.vt2trg.enums.VtTriggerType;
import com.vt2trg.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class FileUtil {
    protected static final Plugin TRIGGER_REACTOR = Bukkit.getPluginManager().getPlugin("TriggerReactor");
    protected static final Plugin VARIABLE_TRIGGERS = Bukkit.getPluginManager().getPlugin("VariableTriggers");
    public static File getTrgDataFolder(){
        if(!Main.isTrgAvailable())
            return null;

        return TRIGGER_REACTOR.getDataFolder();
    }

    public static File getVtDataFolder(){
        if(!Main.isVtAvailable())
            return null;

        return VARIABLE_TRIGGERS.getDataFolder();
    }

    public static File getVtScript(VtTriggerType type){
        String defaultPath = FileUtil.getVtDataFolder().getPath();
        return new File(defaultPath +"/"+ type.name().substring(0, 1).toUpperCase() + type.name().toLowerCase().substring(1) +"Triggers.yml");
    }
}
