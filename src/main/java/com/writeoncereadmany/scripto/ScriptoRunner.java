package com.writeoncereadmany.scripto;

import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class ScriptoRunner implements BeforeTestExecutionCallback {

    @Override
    public void beforeTestExecution(ExtensionContext context) throws Exception {
        Method testMethod = context.getRequiredTestMethod();
        Class testClass = context.getRequiredTestClass();
        Object testInstance = context.getRequiredTestInstance();

        Requires requirements = testMethod.getDeclaredAnnotation(Requires.class);
        if(requirements != null) {
            List<Method> setup = getRequirements(testClass, requirements.value());
            for(Method method : setup) {
                method.invoke(testInstance);
            }
        }
    }

    private List<Method> getRequirements(Class<?> context, String... requirements) {
        return Stream.of(requirements).map(req -> getRequirement(context, req)).collect(toList());
    }

    private Method getRequirement(Class<?> testClass, String requirement) {
        return Stream
            .of(testClass.getMethods())
            .filter(provides(requirement))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Could not find method providing " + requirement));
    }

    private Predicate<Method> provides(String requirement) {
        return method -> {
            Provides provides = method.getDeclaredAnnotation(Provides.class);
            if(provides != null) {
                return Stream.of(provides.value()).anyMatch(requirement::equals);
            } else {
                return false;
            }
        };
    }
}
