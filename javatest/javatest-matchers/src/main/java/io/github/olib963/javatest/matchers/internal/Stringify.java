package io.github.olib963.javatest.matchers.internal;

import io.github.olib963.javatest.matchers.CheckedRunnable;

import java.util.Arrays;

public class Stringify {

    public static String stringify(Object object) {
        if (object == null) {
            return "null";
        } else if(object instanceof CheckedRunnable) {
            return "runnable";
        } else if(object.getClass().isArray()) {
            // Primitive Arrays
            if (object instanceof byte[])
                return Arrays.toString((byte[]) object);
            else if (object instanceof short[])
                return Arrays.toString((short[]) object);
            else if (object instanceof int[])
                return Arrays.toString((int[]) object);
            else if (object instanceof long[])
                return Arrays.toString((long[]) object);
            else if (object instanceof char[])
                return Arrays.toString((char[]) object);
            else if (object instanceof float[])
                return Arrays.toString((float[]) object);
            else if (object instanceof double[])
                return Arrays.toString((double[]) object);
            else if (object instanceof boolean[])
                return Arrays.toString((boolean[]) object);
            else
                // Object Array
                return Arrays.deepToString((Object[])object);
        } else {
            return object.toString();
        }
    }
}
