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
    private List<String> newScript = new ArrayList<>();
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
            } else {
                String argOutput = "";
                String[] parts = str.split(" ");
                if (parts[0].matches("@IF|@OR|@AND")) {
                    String[] args = (String[]) Arrays.asList(Arrays.copyOfRange(parts, 1, parts.length)).toArray();
                    String operator;
                    List<EnhancedMap> partialMap = new ArrayList<EnhancedMap>();
                    if(args[0].matches("[isb]")) {
                        partialMap.add(new EnhancedMap(1, Executors.Attribute.fromAliases(args[0].charAt(0)), args[1]));
                        partialMap.add(new EnhancedMap(3, Executors.Attribute.fromAliases(args[0].charAt(0)), args[3]));
                        Map<Integer, String> result = argumentalConversion(partialMap);

                        if(args[2].matches("=|==")){
                            operator = "==";
                        }else if(args[2].matches("<|>|!=|<=|>=")){
                            operator = args[2];
                        }else{
                            operator = "UNKNOWN";
                        }
                        /*
                        switch (args[2]){
                            case "=": case "==":
                                operator = "==";
                            case "<": case ">": case "!=": case "<=": case ">=":
                                operator = args[2];
                            default:
                                operator = "";
                        }

                        does not work for some reason :(
                         */
                        argOutput = "#IF " + result.get(1) + " " + operator + " " + result.get(3);

                    }else {
                        //none ISB ed
                        argOutput = argOutput;
                    }
                    newScript.add(argOutput);
                } else if (parts[0].equals("@SWITCH")) {
/*
                    //CATCH FIRST CASE - TODO
                    String[] args = (String[]) Arrays.asList(Arrays.copyOfRange(parts, 1, parts.length)).toArray();
                    if(args[0].matches("[isb]")) {
                        String operator = "";
                        String result = argumentalConversion(args[1], Executors.Attribute.fromAliases(args[0].charAt(0)));
                        //String argOutput = result.get(1) + " " + operator + " " + result.get(3);
                    }
*/
                } else {
                    Executors executor;
                    List<EnhancedMap> partialMap = new ArrayList<EnhancedMap>();
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
                    int strings = grammar.indexOf(Executors.Attribute.STRINGS);
                    for(int i = 1; i < parts.length; i++){
                        if(strings == (i - 1)){
                            String appended = "";
                            for(int k = i; k < parts.length; k++){
                                appended = appended + parts[k] + (k == (parts.length - 1) ? "" : " ");
                            }
                            partialMap.add(new EnhancedMap(i, Executors.Attribute.STRINGS, appended));
                            break;
                        }
                        partialMap.add(new EnhancedMap(i, grammar.get(i - 1), parts[i]));
                    }
                    Map<Integer, String> result = argumentalConversion(partialMap);
                    List<String> generalFormat = executor.getTrgFormat();
                    String temp = "";
                    List<String> finalResult = new ArrayList<>();
                    for(String formatLine : generalFormat){
                        for(int i : result.keySet()){
                            temp = formatLine.replaceAll("\\$"+(i - 1), result.get(i));
                        }
                        finalResult.add(temp);
                    }
                    newScript.addAll(finalResult);
                }
            }
            index++;
        }
        return newScript;
    }
    private String argumentalConversion(String arg, Executors.Attribute type){

        return null;
    }
    private Map<Integer, String> argumentalConversion(List<EnhancedMap> partialMap){
        Map<Integer, String> output = new HashMap<>();
        for(EnhancedMap partial : partialMap){
            output.put(partial.getIndex(), partial.getPart());
        }
        return output;
    }

    /*
    private List<String> interpretGeneralScript(Map<Executors.Attribute, String> map, String[] whole){
        Executors.Attribute currentGrammar = (Executors.Attribute) map.keySet().toArray()[0];
        String part = map.get(currentGrammar);
        switch (currentGrammar){
            case OBJECT:

            case STRING:

            case BOOLEAN:

            case DOUBLE:

            case STRINGS:

            case SEPARATION_COLON:

            case ENCHANTMENTS:

            case VARIABLE:

            case LOCATION:

            default:

        }
        return null;
    }
    */


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
