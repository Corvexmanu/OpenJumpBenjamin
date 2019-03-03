package org.openjump.tools.test;

import java.lang.reflect.Field;

/**
 * @author Benjamin Gudehus
 */
public final class ReflectionUtils {
    
    //-----------------------------------------------------------------------------------
    // CONSTRUCTORS.
    //-----------------------------------------------------------------------------------
    
    private ReflectionUtils() {
        throw new UnsupportedOperationException();
    }
    
    //-----------------------------------------------------------------------------------
    // STATIC METHODS.
    //-----------------------------------------------------------------------------------

    public static Object privateField(Object obj, String name) 
            throws Exception {
        Field field = obj.getClass().getDeclaredField(name);
        field.setAccessible(true);
        return field.get(obj);
    }
    
    public static void privateField(Object obj, String name, Object value) 
            throws Exception {
        Field field = obj.getClass().getDeclaredField(name);
        field.setAccessible(true);
        field.set(obj, value);
    }
    
    public static Object privateStaticField(Class<?> cls, String name) 
            throws Exception {
        Field field = cls.getDeclaredField(name);
        field.setAccessible(true);
        return field.get(cls);
    }
    
    public static void privateStaticField(Class<?> cls, String name, Object value) 
            throws Exception {
        Field field = cls.getDeclaredField(name);
        field.setAccessible(true);
        field.set(cls, value);
    }
    
}