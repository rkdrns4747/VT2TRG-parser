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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
                continue;
            } else {
                //If starts with @
                String argOutput = "";
                String operator_str = str.substring(1, !str.contains(" ") ? str.length() : str.indexOf(" ") - 1).toUpperCase();
                if(!Executors.exists(operator_str)){
                    //If Executor does not exist
                    newScript.add("//" + str);
                    index++;
                    continue;
                }
                Executors operator = Executors.valueOf(operator_str);
                List<Executors.Attribute> grammar = operator.getGrammar();
                if(grammar.contains(Executors.Attribute.STRINGS)){
                    //if contains STRINGS
                    HashMap<Integer, Executors.Attribute> solidGrammar = new HashMap<>();
                    for(int i = 0; i < grammar.indexOf(Executors.Attribute.STRINGS); i++){
                        solidGrammar.put(i, grammar.get(i));
                    }
                    String[] solidParameters = str.substring(!str.contains(" ") ? str.length() : str.indexOf(" ") + 1, grammar.indexOf(Executors.Attribute.STRINGS)).split(" "); //solid part
                    if(solidGrammar.size() != solidParameters.length){
                        //solid part's parameter amount is not matched
                        newScript.add("//" + str);
                        index++;
                        continue;
                    }
                    //interpret solid part first
                    for(Map.Entry<Integer, Executors.Attribute> entry : solidGrammar.entrySet()){
                        nonSpacedInterpretation(solidGrammar.get(entry.getKey()), solidParameters[entry.getKey()]);
                    }
                }else if(grammar.contains(Executors.Attribute.LOCATION) && operator != Executors.SIGNTEXT /* it already treated on STRINGS */){
                    //TODO - LOCATION treatment
                }else{
                    List<String> at_all = new ArrayList<>();
                    //TODO - He said I can get paid but still there was no
                             return at_all;
                }
                /*
                String[] parts = str.split(" ");
                if(parts[0])
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
                        argOutput = "#IF " + result.get(1) + " " + operator + " " + result.get(3);

                    }else {
                        //none ISB ed
                        argOutput = argOutput;
                    }
                    newScript.add(argOutput);
                } else if (parts[0].equals("@SWITCH")) {
                 
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
        */
            }
            index++;
        }
        return newScript;
    }

    private String nonSpacedInterpretation(Executors.Attribute grammar, String part){
        //     < , > , : , $ , .
        // if the attribute is kind of value,
        // < is start of placeholder
        // > is end of placeholder
        // : is start of a placeholder's parameter
        // $ is start of a variable
        // . is variable separator
        //
        // HOWEVER, this is also flexible based on grammar. So it's better to check grammar first.
        // ALERT, @SIGNTEXT contains location ONE THE FIRST OF parameter, which means its logic is limited to 'x,y,z'
        // location grammar also can be similar with STRINGS if it is located one the last of parameters... so should be exceptionally treated like STRINGS.

        char[] seq = part.toCharArray();
        //TODO - implement non-spaced interpretation.
        for (char c : seq) {

        }

        return "a";
    }


    private Map<Integer, String> argumentalConversion(List<EnhancedMap> partialMap){
        //this part would be much complex
        //only non-functional placeholder can be inside of functional placeholder's argument.
        //hello<placeholder:<placeholder>:hi<placeholder>>abc<placeholder>kor
        //I. split each placeholder and make array of parts like:
        //   [hello, <placeholder:<placeholder>:hi<placeholder>>, abc, <placeholder>, kor] - COMPLETE!
        for(EnhancedMap map : partialMap){
            //variable conversion should be first.
            String part = map.getPart();
            if(part.contains("$")){
                //on this case, we have some available situation
                // $<placeholder>.abc
                // <var:$abc.def>
                // $abc.def
                // $abc.<var:$abc>
                if(part.startsWith("$")){
                    String varpart = part.substring(1);
                    map.setPart("{" + varpart + "}"); // {abc.<var:$abc>}
                }

//                if(part.matches("[<var:[a-zA-Z$.<>:]>]*")){
//                    Pattern pattern = Pattern.compile("[<var:[a-zA-Z$.<>:]>]");
//                    Matcher matcher = pattern.matcher(part);
//                    StringBuilder sb = new StringBuilder();
//                    while(matcher.find())
//                    {
//                        sb.append(matcher.group());
//                    }
//                    System.out.println(sb);
//                }
            }
            if(map.getPart().contains("<") && map.getPart().contains(">")){
                //contains placeholder

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

    private String handleNonFunctionalPH(String splitedPart){
        Placeholders ph = Placeholders.valueOf(splitedPart.substring(1, splitedPart.length() - 1).toUpperCase());
        return ph.getTrgFormat();
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
        private void setPart(String part) {this.part = part;}
    }
}
