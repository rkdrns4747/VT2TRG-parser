/*******************************************************************************
 *     Copyright (C) 2020 Dr_Romantic
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package com.vt2trg.utils;
import com.vt2trg.interfaces.IModule;
import com.vt2trg.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class ClickTriggerModule extends Module implements IModule {
    private YamlConfiguration fromYaml = new YamlConfiguration();
    private File to;
    public ClickTriggerModule() throws IOException, InvalidConfigurationException {
        fromYaml.load(new File(super.variableTriggers.getDataFolder().getPath() +"/ClickTriggers.yml"));
        to = new File(super.trgDefaultPath.getPath() +"/ClickTrigger");
    }
    public ClickTriggerModule(String fromPath, String toPath) throws IOException, InvalidConfigurationException {
        fromYaml.load(new File(fromPath));
        to = new File(toPath);
    }

    @Override
    public void transferAll() throws NullPointerException, ClassCastException{
        ConfigurationSection section = (ConfigurationSection) fromYaml;
        int coolDown;
        List<String> script;
        for(String w : section.getKeys(false)){
            for(String c : Objects.requireNonNull(section.getConfigurationSection(w)).getKeys(false)){
                coolDown = section.getInt(w+"."+c+".CoolDown");
                script = (List<String>) section.getList(w+"."+c+".Script");
            }


        }

    }

    @Override
    public void transferPart(Object... args) {
        String partType = ((String) args[0]).toLowerCase();

        switch (partType) {
            case "world":
                String worldName = (String) args[1];

                //TODO - implement world sectional convert
            case "location":
                Location loc;
                try {
                    loc = (Location) args[1];
                } catch (ClassCastException e){
                    e.printStackTrace();
                }
                //TODO - implement world-coordinate sectional convert
            case "coordinates":
                int x,y,z;
                try{
                    x = (int) args[1];
                    y = (int) args[2];
                    z = (int) args[3];
                    //TODO - implement coordinate sectional convert
                }catch (ClassCastException e){
                    e.printStackTrace();
                }

        }
    }
}
