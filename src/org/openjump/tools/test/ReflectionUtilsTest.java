package org.openjump.tools.test;

import static org.junit.Assert.assertEquals;
import static org.openjump.tools.test.ReflectionUtils.privateField;
import static org.openjump.tools.test.ReflectionUtils.privateStaticField;

import org.junit.Test;

/**
 * @author Benjamin Gudehus
 */
public class ReflectionUtilsTest {

    //-----------------------------------------------------------------------------------
    // TEST CASES.
    //-----------------------------------------------------------------------------------
    
    @Test
    public void testGetPrivateField() throws Exception {
        // expect: "private field"
        PrivateClass object = new PrivateClass();
        assertEquals("foo", privateField(object, "privateField"));
    }
    
    @Test(expected=NoSuchFieldException.class)
    public void testGetPrivateFieldException() throws Exception {
        // expect: "private field"
        PrivateClass object = new PrivateClass();
        privateField(object, "invalid");
    }
    
    @Test
    public void testSetPrivateField() throws Exception {
        // expect: "private field"
        PrivateClass object = new PrivateClass();
        privateField(object, "privateField", "bar");
        assertEquals("bar", privateField(object, "privateField"));
    }
    
    @Test
    public void testGetPrivateStaticField() throws Exception {
        // expect: "private static field"
        Class<PrivateClass> cls = PrivateClass.class;
        assertEquals("foo", privateStaticField(cls, "privateStaticField"));
    }
    
    @Test(expected=NoSuchFieldException.class)
    public void testGetPrivateStaticFieldException() throws Exception {
        // expect: "private static field"
        Class<PrivateClass> cls = PrivateClass.class;
        privateStaticField(cls, "invalid");
    }
    
    @Test
    public void testSetPrivateStaticField() throws Exception {
        // expect: "private static field"
        Class<PrivateClass> cls = PrivateClass.class;
        privateStaticField(cls, "privateStaticField", "bar");
        assertEquals("bar", privateStaticField(cls, "privateStaticField"));
    }
    
    //-----------------------------------------------------------------------------------
    // TEST FIXTURES.
    //-----------------------------------------------------------------------------------
    
    public static class PrivateClass {
        @SuppressWarnings("unused")
        private String privateField = "foo";
        
        @SuppressWarnings("unused")
        private static String privateStaticField = "foo";
    }
    
}