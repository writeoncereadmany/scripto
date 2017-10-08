package com.writeoncereadmany.scripto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(ScriptoRunner.class)
public class SequentialSetupTest {

    int x = 4;

    @Test
    @Provides("x is 5")
    public void foo() {
        x = 5;
    }

    @Test
    @Requires("x is 5")
    public void bar() {
        assertEquals(x, 5);
    }
}
