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

import java.util.*;

public class DefaultScriptConverter{
    private List<String> script;
    private List<String> newScript;
    private Map<Integer, IfBuilder> tempIfBuilder;
    private SwitchBuilder tempSwitchBuilder;
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
                if (parts[0].equals("@IF")) {
                    IfBuilder ifBuilder = new IfBuilder(index, script.subList(index, script.size()), Arrays.asList(Arrays.copyOfRange(parts, 1, parts.length)));
                    Map<String, Object> result = ifBuilder.build();
                    //List<String> result = this.handleIfProcess(parts[0].replace("@", ""), Arrays.asList(Arrays.copyOfRange(parts, 1, parts.length)));
                } else if (parts[0].equals("@SWITCH")) {
                    SwitchBuilder switchBuilder = new SwitchBuilder(index, script.subList(index, script.size()), Arrays.asList(Arrays.copyOfRange(parts, 1, parts.length)));
                    Map<String, Object> result = switchBuilder.build();
                    //append result to newScript

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
                        partial(partialMap.get(now), parts);
                    }
                }
            }
        }
        return null;
    }

    private List<String> partial(Map<Executors.Attribute, String> map, String[] whole){
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
/*
    public List<String> convertPlaceholder(String placeholder, String parent){
        // <hello:<hi:<yes>>> layering (X)
        // <hello:<yes>:<hi>> multiplicity
        // <hello:<yes>gogo> string-placeholder form
        String temp = placeholder.substring(1, placeholder.length() - 1);
        String partial;
        if(temp.contains("<")
                && temp.contains(">")
                && !(temp.charAt(temp.indexOf("<") + 1) == ' ')
                && !(temp.charAt(temp.lastIndexOf(">") - 1) == ' ')){
            String[] inside = temp.split(":");

            convertPlaceholder(placeholder.substring(placeholder.indexOf("<"), placeholder.lastIndexOf(">") + 1), inside[0]);
        }else{
            partial = temp;
        }

        return null;
    }*/

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

/*@IF [i/s/b] [변수명/문자열/정수/True/False] [=/!=/</>/<=/>=] [변수명/문자열/정수/True/False]
        '만약 [i/s/b]의 직업을 가진 [변수값(좌)]가 [변수값(우)]와의 [부등호]가 일치할 때, 다음 구문으로 넘어간다.'

    @ELSE
        'IF의 값에 일치하지 않는 경우 실행되는 블록'

    @AND [i/s/b] [변수명/문자열/정수/True/False] [=/!=/</>/<=/>=] [변수명/문자열/정수/True/False]
        '&&' 의미

    @OR [i/s/b] [변수명/문자열/정수/True/False] [=/!=/</>/<=/>=] [변수명/문자열/정수/True/False]
        '||' 의미

    @ENDIF
        '조건블록 종료'*/

    /*
     * Handles one super parent block.
     * Most VT has multi-layered @IF statement, just like TR.
     * HOWEVER, VT does not handle AND and OR with &&, ||, but with separated executors, @AND and @OR.
     * In other words, there's no way to interpret @IF, @AND, @OR, @ELSE, @ENDIF line by line.
     * And also, as I said, @IF block can contain more than two @IFs. What this means is that IfBuilder would be `recursive Object`.
     * variables:
     *  - ifCount: one @IF block can have multiple @IF statements. So, this variable initialized as 0 at first,
     *             and whenever it meets @IF, the value will increase 1, which is contrary to decreasing 1 whenever it meets @ENDIF.
     *             If its value reaches 0, then this IfBuilder will return the whole interpreted @IF block.
     *  - leastCode: codes after starting @IF block. As soon as current IfBuilder interpretation ends(ifCount reaches 0),
     *               leastCode should be trimmed to being have lines after end of if block(@ENDIF).
     *  - resultMap: map that has result String list, startIndex, lastIndex, leastCode.
     *
     */
    private class IfBuilder {
        private int startIndex;
        private List<String> leastCode;
        private Map<String, Object> resultMap = new HashMap<>();
        private IfBuilder(int currentIndex, List<String> least, List<String> args){
            this.startIndex = currentIndex;
            this.leastCode = least; //will contain also its line itself

        }

        private Map<String, Object> build(){

            return null;
        }
    }

    /*
    @SWITCH [i/s/b] [변수]
        IF와 비슷하지만, 해당 변수 속에 @CASE안의 것이 있는가 확인합니다.
        IF문처럼, 끝에는 @ENDSWITCH가 반드시 써 져야 합니다.

    @CASE [값]
        @SWITCH의 표적이 된 변수 속에 @CASE안에 있는 단어가 들어 있다면, 해당 CASE 구문을 실행시킵니다.

    @ENDSWITCH
        SWITCH 종료문 */
    private class SwitchBuilder {
        private int ifCount;
        private int startIndex;
        private List<String> leastCode;
        private SwitchBuilder(int currentIndex, List<String> least, List<String> args){
            this.startIndex = currentIndex;
            this.leastCode = least; //will contain also its line itself
        }

        private Map<String, Object> build(){

            return null;
        }
    }
}
