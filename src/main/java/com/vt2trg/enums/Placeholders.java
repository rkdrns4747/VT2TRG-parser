package com.vt2trg.enums;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum Placeholders {
    THIS("\"<this-DEPRECATED>\"", true, Attribute.STRING),
    PLAYERNAME("$playername", false),
    ITEMID("player.getInventory().getItemInHand().getTypeId()", false),
    PLAYERLOC("player.getLocation().getBlockX() + \",\" + player.getLocation().getBlockY() + \",\" + player.getLocation().getBlockZ()", false),
    PLAYERLOC_FUNCTIONAL("player($0).getLocation().get$0()", false, Attribute.STRING), //uppercase on initial
    TRIGGERLOC("block.getLocation().getBlockX() + \",\" + block.getLocation().getBlockY() + \",\" + block.getLocation().getBlockZ()", false),
    TRIGGERLOC_FUNCTIONAL("block.getLocation().get$0()", false, Attribute.STRING), //uppercase on initial
    HEALTH("$health", false),
    HEALTH_FUNCTIONAL("player($0).getHealth()", false, Attribute.STRING),
    WORLDNAME("$worldname", false),
    BIOME("$biome", false),
    GAMEMODE("$gamemode", false),
    ISSPRINTING("$issprinting", false),
    ISSNEAKING("$issneaking", false),
    HELDITEMNAME("$helditemname", false),
    HELDITEMDISPLAYNAME("$helditemdisplayname", false),
    PLAYERSUFFIX("$suffix", false),
    PLAYERLISTNAME("player.getPlayerListName()", false),
    PLAYERDISPLAYNAME("player.getDisplayName()", false),
    PLAYERUUID("$playeruuid", false),
    HOLDINGITEM_FUNCTIONAL("(player.getItemInHand() == null && player($0).getItemInHand().getType().name().equalsIgnoreCase($1))", false, Attribute.STRING, Attribute.STRING),
    EVAL_FUNCTIONAL("$0", false, Attribute.STRING_WITH_PERCENT_VARIABLE_VARIABLE),
    FOOD_FUNCTIONAL("player($0).getFoodLevel()", false, Attribute.STRING),
    SATURATION_FUNCTIONAL("player($0).getSaturation()", false, Attribute.STRING),
    UUID("player($0).getUniqueId()", false, Attribute.STRING),
    SN_FUNCTIONAL("\"<sn-DEPRECATED>\"", true),
    ONLINEPLAYERAMOUNT("$onlineplayers", false),
    SIGNTEXT_FUNCTIONAL("location($0).getBlock().getState().getLine($1)", false, Attribute.LOCATION, Attribute.INT),
    SYSTEMTIME("\"<systemtime-DEPRECATED>\"", true),
    MAXHEALTH_FUNCTIONAL("player($0).getMaxHealth()", false, Attribute.STRING),
    ISOP_FUNCTIONAL("player($0).isOp()", false, Attribute.STRING),
    CONTAINS_FUNCTIONAL("matches($0, $1)", false, Attribute.STRING, Attribute.STRING),
    RAMDOM_FUNCTIONAL("random($0, $1)", false, Attribute.INT, Attribute.INT),
    ARRAYSIZE("listOf({$0}).get(0).size()", false, Attribute.VARIABLE);

    //from page 17


    private String trgFormat;
    private List<Placeholders.Attribute> grammar;
    public List<Placeholders.Attribute> getGrammar(){
        return this.grammar;
    }

    private Placeholders(String trgFormat, boolean deprecated, Attribute... args)
    {
        this.grammar = Arrays.asList(args);
        this.trgFormat = trgFormat;
    }
    public enum Attribute{
        STRING,
        STRING_WITH_PERCENT_VARIABLE_VARIABLE,
        VARIABLE,
        LOCATION,
        INT,
        OBJECT,
    }
}
