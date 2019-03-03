package org.openjump.tools.plugin;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.HashMap;

import javax.swing.JInternalFrame;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openjump.tools.test.TestTools;

import com.vividsolutions.jump.workbench.JUMPWorkbench;
import com.vividsolutions.jump.workbench.model.LayerManager;
import com.vividsolutions.jump.workbench.plugin.PlugIn;

/**
 * @author Benjamin Gudehus
 */
public class UnionByAttributePlugInTest {
    
    //-----------------------------------------------------------------------------------
    // FIELDS.
    //-----------------------------------------------------------------------------------
    
    public static JUMPWorkbench workbench;
    
    //-----------------------------------------------------------------------------------
    // SETUP AND CLEANUP.
    //-----------------------------------------------------------------------------------
    
    @BeforeClass
    public static void beforeClass() throws Exception {
        workbench = TestTools.buildWorkbench(new String[] {"-i18n", "en"});
        // TODO: Wait until frame is visible.
        // TODO: Refactor PlugIns so that a visible frame isn't needed.
        workbench.getFrame().setVisible(true);
    }
    
    @Before
    public void before() {
        //workbench.getFrame().addTaskFrame();
    }
    
    @After
    public void after() {
        for (JInternalFrame frame : workbench.getFrame().getInternalFrames()) {
            workbench.getFrame().removeInternalFrame(frame);
        }
    }
    
    @AfterClass
    public static void afterClass() {
        workbench.getFrame().setVisible(false);
        workbench.getFrame().dispose();
    }
    
    //-----------------------------------------------------------------------------------
    // TEST CASES.
    //-----------------------------------------------------------------------------------

    @Test
    public void testAddedResultLayer() throws Exception {
        // given: "a loaded shapefile fixture"
        TestTools.openFile(new File("share/dissolve.jml"), workbench.getContext());
        
        // and: "plugin with dialog values"
        PlugIn plugin = new UnionByAttributePlugIn();
        LayerManager layerManager = workbench.getContext().getLayerManager();
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("LAYER", layerManager.getLayer("dissolve"));
        parameters.put("ATTRIBUTE", "LABEL");
        parameters.put("IGNORE_EMPTY", false);
        parameters.put("MERGE_LINES", false);
        parameters.put("TOTAL_NUMERIC_FIELDS", false);
        TestTools.configurePlugIn(plugin, parameters, true);
        
        // when: "union by attribute is called"
        TestTools.executePlugIn(plugin, workbench.getContext());
        
        // then: "layer manager contains the source and result layer" 
        assertEquals(2, layerManager.getLayers().size());
        //Thread.sleep(Integer.MAX_VALUE);
    }
    
}