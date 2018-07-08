package de.dzmitry_lamaka.timingsdk.sdk;

import java.lang.reflect.Field;

public class ReflectionHelpers {

    public static void setField(final Object object, final String fieldName, final Object fieldNewValue) {
        try {
            traverseClassHierarchy(object.getClass(), NoSuchFieldException.class, new InsideTraversal<Void>() {
                @Override
                public Void run(Class<?> traversalClass) throws Exception {
                    Field field = traversalClass.getDeclaredField(fieldName);
                    field.setAccessible(true);
                    field.set(object, fieldNewValue);
                    return null;
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static <R, E extends Exception> R traverseClassHierarchy(Class<?> targetClass, Class<? extends E> exceptionClass, InsideTraversal<R> insideTraversal) throws Exception {
        Class<?> hierarchyTraversalClass = targetClass;
        while (true) {
            try {
                return insideTraversal.run(hierarchyTraversalClass);
            } catch (Exception e) {
                if (!exceptionClass.isInstance(e)) {
                    throw e;
                }
                hierarchyTraversalClass = hierarchyTraversalClass.getSuperclass();
                if (hierarchyTraversalClass == null) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private interface InsideTraversal<R> {
        R run(Class<?> traversalClass) throws Exception;
    }
}
