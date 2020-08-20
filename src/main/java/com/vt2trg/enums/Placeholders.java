package com.vt2trg.enums;

import java.util.Arrays;
import java.util.List;

public enum Placeholders {
    THIS("this", ""),
    PLAYERNAME("playername", "player.getName()"),
    ITEMID("itemid", "player.getInventory().getItemInHand().getTypeId()", Attribute.STRING);

    private String trgChanged;
    private List<Placeholders.Attribute> grammar;

    public String getAsTrg()
    {
        return this.trgChanged;
    }
    public List<Placeholders.Attribute> getGrammar(){
        return this.grammar;
    }

    private Placeholders(String vtPlaceholder,String trgChanged, Placeholders.Attribute... args)
    {
        this.grammar = Arrays.asList(args);
        this.trgChanged = trgChanged;
    }
    public enum Attribute{
        STRING;
    }
}
