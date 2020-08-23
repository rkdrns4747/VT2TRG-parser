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
    QUIET("//@QUIET $0 $1", true, Attribute.STRING, Attribute.INT),
    BROADCAST("#BROADCAST $0", false, Attribute.STRING),
    PLAYER("#MESSAGE $0 $1", false, Attribute.STRING),
    TELL("player($0).sendMessage($1)", false, Attribute.STRING, Attribute.STRING),
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

    FIREWORK("", false, Attribute.STRING, Attribute.STRING, Attribute.LOCATION),
    PARTICLE("", false, Attribute.STRING, Attribute.LOCATION),
    FALLINGBLOCK("", false, Attribute.STRING, Attribute.LOCATION),
    SETBLOCKSAFE("", false, Attribute.SEPARATION_COLON, Attribute.LOCATION),
    WORLDTP("", false, Attribute.STRING, Attribute.STRING, Attribute.LOCATION),
    CLEARCHAT("#CLEARCHAT player($0)", false, Attribute.STRING),
    FORK("//@FORK", true),
    SETVELOCITY("", false, Attribute.STRING, Attribute.INT, Attribute.INT, Attribute.INT),
    OPENINV("//@OPENINV $0", true, Attribute.STRING),
    CLOSEINV("player($0).closeInventory()", false, Attribute.STRING),

    //TODO - implementation of the least

/*  @FIREWORK [색] [모양] <장소>
    해당 장소에 폭죽이 터지도록 합니다.
    만일 장소를 입력하지 않을 경우, 명령어를 친 뒤, 폭죽이 터질 위치를 뼈를 든 채로 우 클릭
    하여야 합니다.
            [색] : red, blue, aqua, black, fuchsia, gray, green, lime, brawoon, navy, olive, orange, purple, silver, teal, white, yellow, random
            [모양] : ball(구형) ball_large(큰 구형) burst(반짝이) creeper(크리퍼) star(별모양) random(랜덤)

    @PARTICLE [물질(Material name)] <장소>
    해당 장소에 입자가 생기도록 합니다.
    만일 장소를 입력하지 않을 경우, 명령어를 친 뒤, 입자가 생길 위치를 뼈를 든 채로 우 클릭
    하여야 합니다.

    @FALLINGBLOCK [종류(Material name)] <장소>
    해당 장소에 떨어지는 블록이 생기도록 합니다.
    만일 장소를 입력하지 않을 경우, 명령어를 친 뒤, 떨어질 블록이 생길 위치를 뼈를 든 채로
    우 클릭 하여야 합니다.

    @SETBLOCKSAFE [블럭ID] <장소>
    스피갓 버킷을 사용할 때, SETBLOCK 대용으로 쓰는 스크립트입니다. @SETBLOCK과 같습니다.
    만일 장소를 입력하지 않을 경우, 명령어를 친 뒤, 블록이 생길 위치를 뼈를 든 채로
    우 클릭 하여야 합니다.

    @WORLDTP [닉네임] [월드 이름]
    해당 플레이어를 해당 월드로 이동 시킵니다.

    @SETVELOCITY [닉네임] [X좌표] [Y좌표] [Z좌표]
    해당 플레이어를 X,Y,Z 방향에 적어놓은 힘만큼 날려버립니다. */

    MODIFYPLAYER("//@MODIFYPLAYER $1 $2 $3", true, Attribute.STRING, Attribute.STRING, Attribute.OBJECT);

    //==============================

/*  @SETMOTD [motd]
    MOTD를 설정합니다.

    @SETCANCELLED [이벤트] <true/false>
    해당 이벤트를 true를 입력할시 취소시키고, false를 입력할 시 취소시키지 않습니다.

    @WORLDEDIT [SETPOS1/SETPOS2] <장소>
    월드에딧 구역1 혹은 구역2를 지정합니다.

    @LOOP [숫자]
    지정된 숫자만큼 @ENDLOOP 까지 루프

    @ENDLOOP
    루프종료문 */

    //==============================

/*  @SETINT [변수명] [숫자] : 해당 변수에 해당 숫자를 저장한다.

    @ADDINT [변수명] [숫자] : 해당 변수에 값을 더한다.

    @SUBINT [변수명] [숫자] : 해당 변수에 값을 뺀다.

    @MULINT [변수명] [숫자] : 해당 변수에 값을 곱한다.

    @DIVINT [변수명] [숫자] : 해당 변수에 값을 나눈다.

    @GETSTRLEN [변수명] [문자] : 해당 변수에 문자의 길이를 숫자로 변환시켜 저장한다.

    @SETSTR [변수명] [문자] : 해당 변수에 문자를 저장한다.

    @ADDSTR [변수명] [문자] 해당 변수에 문자열을 추가한다.

    @SETBOOL [변수명] [true/false] : 해당 변수에 이진수 값을 적용시킨다.

    @DELVAR [i/s/b] [변수명] : 해당 변수를 제거한다. */

    //==============================
/*  @IF [i/s/b] [변수명/문자열/정수/True/False] [=/!=/</>/<=/>=] [변수명/문자열/정수/True/False]
        '만약 [i/s/b]의 직업을 가진 [변수값(좌)]가 [변수값(우)]와의 [부등호]가 일치할 때, 다음 구문으로 넘어간다.'

    @ELSE
        'IF의 값에 일치하지 않는 경우 실행되는 블록'

    @AND [i/s/b] [변수명/문자열/정수/True/False] [=/!=/</>/<=/>=] [변수명/문자열/정수/True/False]
        '&&' 의미

    @OR [i/s/b] [변수명/문자열/정수/True/False] [=/!=/</>/<=/>=] [변수명/문자열/정수/True/False]
        '||' 의미

    @ENDIF
        '조건블록 종료'

    @WHILE [i/s/b] [값] [=/!=/</>/<=/>=] [값] :
        [정수/문자열/이진수]인 [값]이 [=/!=/</>/<=/>=] [값]이 될 때 까지
        @WHILE 에서부터 @ENDWHILE 까지의 구문을 반복합니다.

    @ENDWHILE : WHILE문의 끝 지점을 의미합니다.
        반드시 WHILE의 개수만큼 ENDWHILE이 나와야 합니다.

    @SWITCH [i/s/b] [변수]
        IF와 비슷하지만, 해당 변수 속에 @CASE안의 것이 있는가 확인합니다.
        IF문처럼, 끝에는 @ENDSWITCH가 반드시 써 져야 합니다.

    @CASE [값]
        @SWITCH의 표적이 된 변수 속에 @CASE안에 있는 단어가 들어 있다면, 해당 CASE 구문을 실행시킵니다.

    @ENDSWITCH
        SWITCH 종료문 */

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
    }
}
