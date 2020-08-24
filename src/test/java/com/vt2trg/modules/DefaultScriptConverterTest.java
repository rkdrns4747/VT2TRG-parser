package com.vt2trg.modules;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

public class DefaultScriptConverterTest {

/*
    @Test
    public void testRandom() throws Exception {
        List<String> testString = Arrays.asList(
                "@PLAYER Hello sir",
                "@PAUSE 3",
                "@CMDOP spawn",
                "@PLAYER now in spawn!!"
        );
        DefaultScriptConverter DSC = new DefaultScriptConverter(testString);

        Assert.assertTrue(isEqual(Arrays.asList(
                "#MESSAGE \"Hello sir\"",
                "#WAIT 3",
                "#CMDOP \"spawn\"",
                "#MESSAGE \"now in spawn!!\""
        ), DSC.convert()));
    }
**/

    private boolean isEqual(List<String> l1, List<String> l2){
        l1.removeAll(l2);
        return l1.size() == 0;
    }
}
