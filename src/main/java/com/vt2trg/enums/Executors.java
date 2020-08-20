package com.vt2trg.enums;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum Executors {

   PLAYER("#MESSAGE", Attribute.STRING);

    private String trgExecutor;
    private List<Attribute> grammar;

    public String getExecutorAsTrg()
    {
        return this.trgExecutor;
    }
    public List<Attribute> getGrammar(){
        return this.grammar;
    }

    private Executors(String trgExecutor, Attribute... args)
    {
        this.grammar = Arrays.asList(args);
        this.trgExecutor = trgExecutor;
    }
    public enum Attribute{
        STRING;
    }
}
