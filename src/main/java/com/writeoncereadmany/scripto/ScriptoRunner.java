package com.writeoncereadmany.scripto;

import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.Arrays;

public class ScriptoRunner implements BeforeTestExecutionCallback {

    @Override
    public void beforeTestExecution(ExtensionContext context) throws Exception {
        Requires requirements = context.getRequiredTestMethod().getDeclaredAnnotation(Requires.class);
        if(requirements != null) {
            throw new RuntimeException("Requires " + Arrays.toString(requirements.value()));
        }
    }
}
