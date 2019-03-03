package org.openjump.tools.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.vividsolutions.jump.workbench.model.Layer;

/**
 * @author Benjamin Gudehus
 */
public class DialogParametersTest {

    // TODO: Test that methods gracefully complain about invalid calls.
    // TODO: Test nonexistent hash keys.
    
    @Test
    public void testGetText() {
        // expect: "text value"
        DialogParameters parameters = new DialogParameters();
        parameters.putField("text", "value");
        assertEquals("value", parameters.getText("text"));
    }
    
    @Test
    public void testGetBoolean() {
        // expect: "boolean value"
        DialogParameters parameters = new DialogParameters();
        parameters.putField("boolean", false);
        assertEquals(false, parameters.getBoolean("boolean"));
    }
    
    @Test
    public void testGetDouble() {
        // expect: "double value"
        DialogParameters parameters = new DialogParameters();
        parameters.putField("double", 3.14);
        assertEquals(3.14, parameters.getDouble("double"), 0.01);
    }
    
    @Test
    public void testGetInteger() {
        // expect: "integer value"
        DialogParameters parameters = new DialogParameters();
        parameters.putField("integer", 42);
        assertEquals(42, parameters.getInteger("integer"));
    }
    
    @Test
    public void testGetLayer() {
        // expect: "layer object"
        DialogParameters parameters = new DialogParameters();
        Layer layer = new Layer();
        parameters.putField("layer", layer);
        assertEquals(layer, parameters.getLayer("layer"));
    }

    @Test(expected=UnsupportedOperationException.class)
    public void testSetVisible() {
        // expect: set visible throws an exception.
        DialogParameters parameters = new DialogParameters();
        parameters.setVisible(true);
    }
    
}