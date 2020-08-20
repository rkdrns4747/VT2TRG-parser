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

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DefaultScriptConverter{
    public List<String> script;
    public DefaultScriptConverter(List<String> script){
        this.script = script;
    }
    public List<String> convert(){
        String testString = "@PLAYER";
        String a = Executors.valueOf(testString.substring(1)).getExecutorAsTrg();
        //TODO - implement vt -> trg script converter
        return null;
    }

    public String convert(int line){
        String str = script.get(line);
        if(!str.startsWith("@"))
            return null;
        else {
            Executors currentEx = Executors.valueOf(!str.contains(" ") ? str.substring(1,str.indexOf(" ")) : str.substring(1));
            List<Executors.Attribute> grammar = currentEx.getGrammar();
        }

        return null;
    }

    public String convertPlaceholder(String placeholder, String parent){
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
    }
}
