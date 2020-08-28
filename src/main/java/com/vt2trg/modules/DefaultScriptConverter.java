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
package com.vt2trg.modules;
import com.vt2trg.enums.Executors;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.material.MaterialData;

import javax.print.DocFlavor;
import java.util.*;

public class DefaultScriptConverter{
    private List<String> script;
    private List<String> newScript;
    private int ifCount = 0;
    public DefaultScriptConverter(List<String> script){
        this.script = script;
    }

    public List<String> convert(){

        int index = 0;
        while(index < script.size()) {
            String str = script.get(index);
            if (!str.startsWith("@")) {
                newScript.add("//" + str);
                index++;
                continue;
            } else {
                String[] parts = str.split(" ");
                if (parts[0].matches("@IF | @OR | @AND")) {
                    String[] args = (String[]) Arrays.asList(Arrays.copyOfRange(parts, 1, parts.length)).toArray();
                    String operator = "";
                    List<EnhancedMap> partialMap = new ArrayList<EnhancedMap>();
                    if(args[0].matches("[isb]")) {
                        partialMap.add(new EnhancedMap(1, Executors.Attribute.fromAliases(args[0].charAt(0)), args[1]));
                        partialMap.add(new EnhancedMap(3, Executors.Attribute.fromAliases(args[0].charAt(0)), args[3]));
                        Map<Integer, String> result = argumentalConversion(partialMap);
                        switch (args[2]){
                            case "=": case "==":
                                operator = "==";
                            case "<": case ">": case "!=": case "<=": case ">=":
                                operator = args[2];
                        }
                        String argOutput = result.get(1) + " " + operator + " " + result.get(3);
                    }
                } else if (parts[0].equals("@SWITCH")) {

                    //CATCH FIRST CASE
                    String[] args = (String[]) Arrays.asList(Arrays.copyOfRange(parts, 1, parts.length)).toArray();
                    if(args[0].matches("[isb]")) {
                        String operator = "";
                        String result = argumentalConversion(args[1], Executors.Attribute.fromAliases(args[0].charAt(0)));
                        //String argOutput = result.get(1) + " " + operator + " " + result.get(3);
                    }

                } else {
                    Executors executor;
                    Map<Integer, Map<Executors.Attribute, String>> partialMap = new HashMap<>();
                    try{
                        executor = Executors.valueOf(parts[0].replaceFirst("@", ""));
                    }catch (IllegalArgumentException | NullPointerException ex){
                        executor = Executors.INVALID;
                    }

                    if(executor == Executors.INVALID){
                        newScript.add("//" + script.get(index));
                        index++;
                        continue;
                    }
                    List<Executors.Attribute> grammar = executor.getGrammar();
                    for(int i = 1; i < parts.length; i++){
                        Map<Executors.Attribute, String> temp = new HashMap<>();
                        temp.put(grammar.get(i - 1), parts[i]);
                        partialMap.put(i, temp);
                        temp.clear();
                    }
                    for (int now : partialMap.keySet()) {
                        interpretGeneralScript(partialMap.get(now), parts);
                    }
                }
            }
        }
        return null;
    }
    private String argumentalConversion(String arg, Executors.Attribute type){
        //TODO - implemtion of conversion
        return null;
    }
    private Map<Integer, String> argumentalConversion(List<EnhancedMap> partialMap){
        //TODO - implemtion of conversion
        return null;
    }

    private List<String> interpretGeneralScript(Map<Executors.Attribute, String> map, String[] whole){
        Executors.Attribute currentGrammar = (Executors.Attribute) map.keySet().toArray()[0];
        String part = map.get(currentGrammar);
        switch (currentGrammar){
            case OBJECT:
                //TODO
            case STRING:
                //TODO
            case BOOLEAN:
                //TODO
            case DOUBLE:
                //TODO
            case STRINGS:
                //TODO
            case SEPARATION_COLON:
                //TODO
            case ENCHANTMENTS:
                //TODO
            case VARIABLE:
                //TODO
            case LOCATION:
                //TODO
        }
        return null;
    }


    @SuppressWarnings("deprecation")
    public static String convertMaterial(String value) {
        String[] sep = value.split(":");
        int id = Integer.parseInt(sep[0]);
        byte data = Byte.parseByte(sep[1]);
        for(Material i : EnumSet.allOf(Material.class)){
            if(i.getId() == id)
                return Bukkit.getUnsafe().fromLegacy(new MaterialData(i, data)).name();
        }
        return null;
    }

    public class EnhancedMap{
        private int index;
        private Executors.Attribute attribute;
        private String part;

        EnhancedMap(int index, Executors.Attribute attribute, String part){
            this.index = index;
            this.attribute = attribute;
            this.part = part;
        }

        private int getIndex(){
            return  index;
        }
        private Executors.Attribute getAttribute(){
            return attribute;
        }

        private String getPart(){
            return part;
        }
    }
}
