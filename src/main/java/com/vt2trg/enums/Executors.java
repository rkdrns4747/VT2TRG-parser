package com.vt2trg.enums;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum Executors {
    INVALID("INVALID", false),

    QUIET("//@QUIET $0 $1", true, Attribute.STRING, Attribute.INT),
    BROADCAST("#BROADCAST $0", false, Attribute.STRINGS),
    PLAYER("#MESSAGE $0", false, Attribute.STRINGS),
    TELL("player($0).sendMessage($1)", false, Attribute.STRING, Attribute.STRINGS),
    EXIT("#STOP", false),
    PAUSE("#WAIT $0", false, Attribute.INT),
    CALL("#CALL $0", false, Attribute.SEPARATION_COLON),
    COOLDOWN("#COOLDOWN $0", false, Attribute.INT),
    CMD("#CMD $0", false, Attribute.STRINGS),
    CMDOP("#CMDOP $0", false, Attribute.STRINGS),
    CMDCON("#CMDCON $0", false, Attribute.STRINGS),
    TOGGLEBLOCK(Arrays.asList("IF $1.getBlock() != null && $1.getBlock().getType().name() != \"AIR\" ",
                              "    #SETBLOCK \"$0\", $1",
                              "ELSE",
                              "    #SETBLOCK \"AIR\", $1",
                              "ENDIF")
            , false, Attribute.SEPARATION_COLON, Attribute.LOCATION),
    SETBLOCK("#SETBLOCK \"$0\", $1", false, Attribute.SEPARATION_COLON, Attribute.LOCATION),
    GETBLOCK(Arrays.asList("IF $1.getBlock() != null && $1.getBlock().getType().name() != \"AIR\" ",
                           "    $0 = $1.getBlock().name()",
                           "ELSE",
                           "    $0 = \"AIR\"",
                           "ENDIF")
    ,false, Attribute.VARIABLE, Attribute.LOCATION),
    SMOKE(Arrays.asList("IMPORT org.bukkit.Effect",
                        "$1.getWorld().playEffect($1, Effect.SMOKE, $0)")
            , false, Attribute.DOUBLE, Attribute.LOCATION),
    FLAMES(Arrays.asList("IMPORT org.bukkit.Effect",
                         "$1.getWorld().playEffect($1, Effect.MOBSPAWNER_FLAMES, $0)")
            , false, Attribute.DOUBLE, Attribute.LOCATION),
    POOF(Arrays.asList("IMPORT org.bukkit.Effect",
                         "$1.getWorld().playEffect($1, Effect.EXTINGUISH, $0)")
            , false, Attribute.DOUBLE, Attribute.LOCATION),
    TP("#TP $0", false, Attribute.LOCATION),
    LIGHTNING(Arrays.asList("IF $0",
                            "    $1.getWorld().strikeLightning($1)",
                            "ELSE",
                            "    $1.getWorld().strikeLightningEffect($1)",
                            "ENDIF")
            ,false, Attribute.BOOLEAN, Attribute.LOCATION),
    ENTITY(Arrays.asList("FOR i = 0:$1",
                         "    #SPAWN $2 $0",
                         "ENDFOR",
                         "i = null")
            ,false, Attribute.STRING, Attribute.INT, Attribute.LOCATION),
    DROPITEM(Arrays.asList("str = $0",
                           "IF $2 == \"NONE\"",
                           "    #DROPITEM item(str.toUpperCase(), $1), $3",
                           "    str = null",
                           "ELSE",
                           "    IMPORT org.bukkit.enchantments.Enchantment",
                           "    enchMap = map",
                           "    FOR encs = $2.split(\"\\\\,\")",
                           "        encAndLevel = encs.split(\":\")",
                           "        enchMap.put(Enchantment.valueOf(encAndLevel[0]), encAndLevel[1])",
                           "    ENDFOR",
                           "    item =  item(str.toUpperCase(), $1)",
                           "    item.addUnsafeEnchantments(enchMap)",
                           "    #DROPITEM item, $3",
                           "    str = null; enchMap = null; encs = null; item = null;",
                            "ENDIF")
            ,false, Attribute.STRING, Attribute.INT, Attribute.ENCHANTMENTS, Attribute.LOCATION),
    GETENTITYCOUNT(Arrays.asList("count = 0",
                                 "FOR e = player.getNearbyEntites($2, $2, $2)",
                                 "    IF e.getType().name().equalsIgnoreCase($1)",
                                 "        count = count + 1",
                                 "    ENDIF",
                                 "ENDFOR",
                                 "$0 = count",
                                 "count = null; e = null;")
            , false, Attribute.VARIABLE, Attribute.STRING, Attribute.DOUBLE),
    GETLIGHT("$0 = $1.getBlock().getLightLevel()", false, Attribute.VARIABLE, Attribute.LOCATION),
    //in this case, SIGNTEXT's location will be only 'a,b,c,... logic.
    SIGNTEXT("#SIGNEDIT $1, $2, $0", false, Attribute.LOCATION, Attribute.INT, Attribute.STRINGS),
    SOUND("#SOUNDALL $1, $0, 5.0, 5.0", false, Attribute.STRING, Attribute.LOCATION),
    SOUNDEX("#SOUNDALL $3, $0, $1, $2", false, Attribute.STRING, Attribute.DOUBLE, Attribute.DOUBLE, Attribute.LOCATION),
    EXPLOSION("#EXPLOSION $1.getWorld().getName(), $1.getX(), $1.getY(), $1.getZ(), $0", false, Attribute.DOUBLE, Attribute.LOCATION),

    //===========================

    FIREWORK("//@FIREWRK $0 $1 $2", true, Attribute.STRING, Attribute.STRING, Attribute.LOCATION),
    PARTICLE(Arrays.asList("IMPORT org.bukkit.Effect",
                           "IMPORT org.bukkit.Material",
                           "str = $0",
                           "$1.getWorld().playEffect($1, Effect.STEP_SOUND, Material.valueOf(str.toUpperCase()).getId())",
                           "str = null")
            , false, Attribute.STRING, Attribute.LOCATION),
    FALLINGBLOCK("#FALLINGBLOCK $0, $1", false, Attribute.STRING, Attribute.LOCATION),
    SETBLOCKSAFE("#SETBLOCK \"$0\", $1", false, Attribute.SEPARATION_COLON, Attribute.LOCATION),
    WORLDTP("player($0).teleport(location($1, player($0).getX(), player($0).getY(), player($0).getZ(), player($0).getYaw(), player($0).getPitch()))", false, Attribute.STRING, Attribute.STRING),
    CLEARCHAT("#CLEARCHAT player($0)", false, Attribute.STRING),
    FORK("//@FORK", true),
    SETVELOCITY(Arrays.asList("IMPORT org.bukkit.Vector",
                              "player($0).setVelocity(Vector($1, $2, $3))")
            , false, Attribute.STRING, Attribute.INT, Attribute.INT, Attribute.INT),
    OPENINV("//@OPENINV $0", true, Attribute.STRING),
    CLOSEINV("player($0).closeInventory()", false, Attribute.STRING),
    MODIFYPLAYER("//@MODIFYPLAYER $1 $2 $3", true, Attribute.STRING, Attribute.STRING, Attribute.OBJECT),

    //==============================

    SETMOTD("//@SETMOTD $0", true, Attribute.STRINGS),
    SETCANCELLED("//@SETCANCELLED $0 $1", true, Attribute.STRING, Attribute.BOOLEAN),
    WORLDEDIT("//@WORLDEDIT $1 $2", true, Attribute.STRING, Attribute.LOCATION),
    WHILE("WHILE $1 $2 $3", false, Attribute.STRING /* NULLABLE? */, Attribute.OBJECT, Attribute.OPERATOR, Attribute.OBJECT),
    ENDWHILE("ENDWHILE", false),
    LOOP("FOR vt2trgLOOP_count = 0:$0", false, Attribute.INT),
    ENDLOOP("ENDFOR; vt2trgLOOP_count = null", false),

    //==============================


    SETINT("{\"$0\"} = $1", false, Attribute.VARIABLE, Attribute.INT),
    ADDINT("{\"$0\"} = {\"$0\"} + $1", false, Attribute.VARIABLE, Attribute.INT),
    SUBINT("{\"$0\"} = {\"$0\"} - $1", false, Attribute.VARIABLE, Attribute.INT),
    MULINT("{\"$0\"} = {\"$0\"} * $1", false, Attribute.VARIABLE, Attribute.INT),
    DIVINT("{\"$0\"} = {\"$0\"} / $1", false, Attribute.VARIABLE, Attribute.INT),
    GETSTRLEN(Arrays.asList("str = $1",
                            "{\"$0\"} = str.length()",
                            "str = null")
            , false, Attribute.VARIABLE, Attribute.STRING),
    SETSTR("{\"$0\"} = $1", false, Attribute.VARIABLE, Attribute.STRINGS),
    ADDSTR("{\"$0\"} = {\"$0\"} + $1", false, Attribute.VARIABLE, Attribute.STRINGS),
    SETBOOL("{\"$0\"} = $1", false, Attribute.VARIABLE, Attribute.BOOLEAN),
    DELVAR("{\"$0\"} = null", false, Attribute.STRING /* NULLABLE? */, Attribute.VARIABLE);

    //==============================


    private List<String> trgFormat;
    private List<Attribute> grammar;

    public List<String> getTrgFormat()
    {
        return this.trgFormat;
    }
    public List<Attribute> getGrammar(){
        return this.grammar;
    }

    private Executors(String trgFormat, boolean deprecated,  Attribute... args)
    {
        this.grammar = Arrays.asList(args);
        this.trgFormat = Collections.singletonList(trgFormat);
    }
    private Executors(List<String> trgFormat, boolean deprecated,  Attribute... args)
    {
        this.grammar = Arrays.asList(args);
        this.trgFormat = trgFormat;
    }
    public enum Attribute{
        OBJECT, //when return type isn't specified, usually used in deprecated
        STRING, //automatically add " "
        INT, //parse as int
        BOOLEAN, //parse as true/false
        DOUBLE, // parse as double value
        STRINGS, //merged as one string, automatically add " "
        SEPARATION_COLON, //separates with ':', and value will be case by case
        ENCHANTMENTS, //handling vt enchantment serial style, 'Sharp:3,Hello:1'. handled as String. HOWEVER, NONE will be handled as not giving enchantment
        VARIABLE, // handled as GLOBAL variable, $a.b.c -> {"a.b.c"}
        OPERATOR, // operators, like [=/!=/</>/<=/>=]
        LOCATION; // location with string. check https://github.com/lyokofirelyte/VariableTriggers/blob/master/vt/com/github/lyokofirelyte/VariableTriggers/VTParser.java#L1449
        /*
        boolean nullable;
        private Attribute(@Nullable boolean nullable){
            this.nullable = nullable;
        }

        private boolean isNullable(){
            return this.nullable;
        }
     **/
        public static Executors.Attribute fromAliases(char ch){
            switch (ch){
                case 'i':
                    return INT;
                case 's':
                    return STRING;
                case 'b':
                    return BOOLEAN;
                default:
                    return OBJECT;
            }
        }
    }

}
