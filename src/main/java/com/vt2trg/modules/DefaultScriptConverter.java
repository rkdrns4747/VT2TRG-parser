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
import com.vt2trg.enums.Placeholders;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.material.MaterialData;
import sun.security.x509.AttributeNameEnumeration;

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
                        executor = Executors.valueOf(parts[0].replaceFirst("@", "").toUpperCase());
                    }catch (IllegalArgumentException | NullPointerException ex){
                        executor = Executors.INVALID;
                    }

                    if(executor == Executors.INVALID || executor.isDeprecated()){
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
    private String argumentalConversion(EnhancedMap singleMap){

        return null;
    }
    private Map<Integer, String> argumentalConversion(List<EnhancedMap> partialMap){
        //this part would be much complex
        //only non-functional placeholder can be inside of functional placeholder's argument.
        //hello<placeholder:<placeholder>:hi<placeholder>>abc<placeholder>kor
        //I. split each placeholder and make array of parts like:
        //   [hello, <placeholder:<placeholder>:hi<placeholder>>, abc, <placeholder>, kor] - COMPLETE!
        for(EnhancedMap map : partialMap){
            if(map.getPart().contains("<") && map.getPart().contains(">")){
                //contains placeholder
                String part = map.getPart();
                List<String> splitedParts = new ArrayList<>();
                int index = 0;
                int startedFrom = 0;
                if(!part.substring(0, part.indexOf('<')).equals(""))
                    splitedParts.add(part.substring(0, part.indexOf('<'))); // got 'hello' part

                int highlighter = 0;
                inner:
                for(char partChar : part.substring(part.indexOf('<')).toCharArray()){
                    if(partChar == '<'){
                        highlighter++;
                        if(highlighter == 1){
                            if (index  == 0) {
                                index++;
                                continue;
                            }
                            splitedParts.add(part.substring(part.indexOf('<')).substring(startedFrom, index));
                            startedFrom = index;
                        }

                    }else if(partChar == '>'){
                        highlighter--;
                        if(highlighter == 0){ //now in  <placeholder:<placeholder>:hi<placeholder>>
                            splitedParts.add(part.substring(part.indexOf('<')).substring(startedFrom, index + 1));
                            startedFrom = index + 1;
                        }
                    }
                    index++;
                }
                if(!part.substring(part.indexOf('<')).substring(startedFrom, index).equals(""))
                    splitedParts.add(part.substring(part.indexOf('<')).substring(startedFrom, index));

                index = 0;
                startedFrom = 0;
                Placeholders currentph;
                for(String splitedPart : splitedParts){
                    if(splitedPart.startsWith("<") && splitedPart.endsWith(">")){
                        if(splitedPart.contains(":")){
                            String result = handleFuntionalPH(splitedPart);
                            //handling functional

                        }else{
                            //handling non-functional
                            handleNonFunctionalPH(splitedPart);
                        }
                    }else{
                        //handling general value

                    }
                }

            }else{
                //no placeholders
                Executors.Attribute type = map.getAttribute();
                if(!map.getPart().contains("$")){
                    //no variable. just handling value type.
                }else{
                    String result = variableConversion(map.getPart(), type);
                }

            }
        }
        Map<Integer, String> output = new HashMap<>();
        for(EnhancedMap partial : partialMap){
            output.put(partial.getIndex(), partial.getPart());
        }
        return output;
    }

    private List<String> handleNonFunctionalPH(String splitedPart){

        return null;
    }

    private String handleFuntionalPH(String splitedPart){
        int index = 0;
        String[] placeholderArgs = splitedPart.substring(1, splitedPart.length() - 1).split(":");
        Placeholders currentph = Placeholders.valueOf(placeholderArgs[0].toUpperCase() + "_FUNCTIONAL");
        String[] args = Arrays.copyOfRange(placeholderArgs, 1, placeholderArgs.length);
        List<Placeholders.Attribute> attributes = currentph.getGrammar();
        Map<Integer, String> output = new HashMap<>();
        for(int i = 0; i < args.length; i++){
            String p = args[i];
            Placeholders.Attribute a = attributes.get(i); // Attribute.B
            int highlighter = 0;
            int startedFrom = 0;
            List<String> pR = new ArrayList<>();
            StringBuilder kon = new StringBuilder();
            if(p.contains("<") && p.contains(">")){
                if(p.indexOf("<") != 0)
                    pR.add(p.substring(0, p.indexOf("<")));

                char[] pCA = p.substring(p.indexOf("<")).toCharArray();
                for(char c : pCA){
                    if(c == '<'){
                        highlighter++;
                        if(highlighter == 1){
                            if (index  == 0) {
                                index++;
                                continue;
                            }
                            pR.add(p.substring(p.indexOf('<')).substring(startedFrom, index));
                            startedFrom = index;
                        }

                    }else if(c == '>'){
                        highlighter--;
                        if(highlighter == 0){ //now in  <placeholder:<placeholder>:hi<placeholder>>
                            pR.add(p.substring(p.indexOf('<')).substring(startedFrom, index + 1));
                            startedFrom = index + 1;
                        }
                    }
                    index++;
                }
                if(!p.substring(p.indexOf('<')).substring(startedFrom, index).equals(""))
                    pR.add(p.substring(p.indexOf('<')).substring(startedFrom, index));

                index = 0;
                highlighter = 0;
                startedFrom = 0;
                if(a == Placeholders.Attribute.STRING){
                    for (int z = 0; z < pR.size(); z++) {
                        if (pR.get(z).startsWith("<") && pR.get(z).endsWith(">")) {
                            kon.append(Placeholders.valueOf(pR.get(z).substring(1, pR.get(z).length() - 1).toUpperCase()).getTrgFormat());
                        } else {
                            kon.append("\"").append(pR.get(z)).append("\"");
                        }
                        kon.append(z == pR.size() - 1 ? "" : " + ");
                    }

                }else if(a == Placeholders.Attribute.INT){
                    //TO DO
                }
                String konR = kon.toString();
                output.put(i, konR);
                String result = currentph.getTrgFormat();
                for(int y : output.keySet()) {
                    result = result.replace("$"+y, output.get(i));
                }
                return result;
            }else{
                //just itself(maybe contains variable)
            }
        }
        return null;
    }

    private String variableConversion(String target, Executors.Attribute att){
        //implemention
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
