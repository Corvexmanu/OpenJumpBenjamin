package org.openjump.tools.test;

import static org.openjump.tools.test.ReflectionUtils.privateField;
import static org.openjump.tools.test.ReflectionUtils.privateStaticField;

import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.openjump.OpenJumpConfiguration;
import org.openjump.core.ui.plugin.file.OpenFilePlugIn;

import com.vividsolutions.jump.coordsys.CoordinateSystem;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.io.datasource.Connection;
import com.vividsolutions.jump.io.datasource.DataSource;
import com.vividsolutions.jump.io.datasource.DataSourceQuery;
import com.vividsolutions.jump.io.datasource.StandardReaderWriterFileDataSource.Shapefile;
import com.vividsolutions.jump.task.DummyTaskMonitor;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.util.LangUtil;
import com.vividsolutions.jump.util.commandline.CommandLine;
import com.vividsolutions.jump.util.commandline.OptionSpec;
import com.vividsolutions.jump.workbench.JUMPConfiguration;
import com.vividsolutions.jump.workbench.JUMPWorkbench;
import com.vividsolutions.jump.workbench.Setup;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.plugin.PlugIn;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.plugin.ThreadedPlugIn;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.SplashPanel;
import com.vividsolutions.jump.workbench.ui.SplashWindow;

/**
 * @author Benjamin Gudehus
 */

public final class TestTools {
	
	public Shapefile dataSourceClass = null;
	public static CoordinateSystem coordinateSystem = CoordinateSystem.UNSPECIFIED;
    
    //-----------------------------------------------------------------------------------
    // CONSTRUCTORS.
    //-----------------------------------------------------------------------------------
    
    private TestTools() {
        throw new UnsupportedOperationException();
    }
    

    //-----------------------------------------------------------------------------------
    // MAIN METHOD.
    //-----------------------------------------------------------------------------------
    
    public static void main(String[] args) throws Exception {
    	
    	
    	String path1 = ".\\sample\\dataset1.shp";
    	String path2 = ".\\sample\\dataset1.shp";    	
    	final File file1 = new File(path1);
    	final File file2 = new File(path2);    	
    	final JUMPWorkbench workbench = TestTools.buildWorkbench(args);
        workbench.getFrame().addWindowListener(new WindowAdapter() {
            public void windowOpened(WindowEvent event) {
                TestTools.openFile(new File(path1), workbench.getContext());
            }
        });        
        workbench.getFrame().addWindowListener(new WindowAdapter() {
            public void windowOpened(WindowEvent event) {
                TestTools.openFile(new File(path2), workbench.getContext());
            }
        });
    	
        
    	/*
    	final File file1 = new File(args[0]);
    	final File file2 = new File(args[1]);    	
        final JUMPWorkbench workbench = TestTools.buildWorkbench(args);
        workbench.getFrame().addWindowListener(new WindowAdapter() {
            public void windowOpened(WindowEvent event) {
                TestTools.openFile(new File(args[0]), workbench.getContext());
            }
        });        
        workbench.getFrame().addWindowListener(new WindowAdapter() {
            public void windowOpened(WindowEvent event) {
                TestTools.openFile(new File(args[1]), workbench.getContext());
            }
        });*/
        
        
        
        //Become the graphical interface invisible.
        workbench.getFrame().setVisible(true);
        
        //DataSource
        DataSource dataSource = getDataSource();                

        //Datasource query.		
      	DataSourceQuery dataSourceQuery1 = getDataSourceQuery(dataSource, coordinateSystem, file1);
      	DataSourceQuery dataSourceQuery2 = getDataSourceQuery(dataSource, coordinateSystem, file2);
              
        // feature collection.      
      	FeatureCollection featureCollection1 = getFeatureCollection(dataSourceQuery1);
      	FeatureCollection featureCollection2 = getFeatureCollection(dataSourceQuery2);
      		
      	//Print Results
      	printResults(featureCollection1);
      	printResults(featureCollection2);
    }
    
    //-----------------------------------------------------------------------------------
    // STATIC METHODS.
    //-----------------------------------------------------------------------------------
    
    /**
     * Builds a new Workbench with WorkbenchFrame and WorkbenchContext.
     * 
     * @see JUMPWorkbench#main
     * @return JUMPWorkbench
     */
    public static JUMPWorkbench buildWorkbench(String[] args) throws Exception {
    	
        // Configure a SplashPanel.
        String title = "OpenJUMP";
        SplashPanel splashPanel = new SplashPanel(JUMPWorkbench.splashImage(), title);
        SplashWindow splashWindow = new SplashWindow(splashPanel);
        splashWindow.setVisible(true);

        // Create a new Workbench with WorkbenchFrame and WorkbenchContext.
        TaskMonitor monitor = new DummyTaskMonitor();
        Setup setup = new JUMPConfiguration();
        
        char c = '-';
        final CommandLine commandLine2 = new CommandLine(c);
        OptionSpec optSpec = new OptionSpec("name", "value");
        System.out.println("The command line is " + commandLine2.getOption("name"));
        
        privateStaticField(JUMPWorkbench.class, "commandLine", new CommandLine());
        
        
        
        File tryingDirectory = new File("./lib/ext");
        System.out.println("The directory is = " + tryingDirectory);
        if (tryingDirectory.exists()) {
        	 System.out.println("Exist");
        }else
        {
        	System.out.println("Does not Exist");
        }
        
        JUMPWorkbench workbench = new JUMPWorkbench(title, args, splashWindow, monitor);
        
        // Setup Workbench.
        setup.setup(workbench.getContext());
        OpenJumpConfiguration.postExtensionInitialization(workbench.getContext());
        return workbench;
    }
    
    /**
     * Opens a geometric fixture in the task panel.
     * 
     * @param file File
     * @param context Context.
     */
    public static void openFile(File file, WorkbenchContext context) {
        OpenFilePlugIn filePlugin = new OpenFilePlugIn(context, file);
        filePlugin.actionPerformed(new ActionEvent(filePlugin, 0, ""));
    }
    
    public static void installPlugIn(PlugIn plugin, WorkbenchContext context) 
            throws Exception {
        PlugInContext plugInContext = context.createPlugInContext();
        plugin.initialize(plugInContext);
    }
    
    /**
     * Configures execution parameters for {@link PlugIn} using its instance fields.
     * 
     * @param plugin Plugin.
     * @param parameters Execution parameters.
     */
    public static void configurePlugIn(PlugIn plugin, Map<String, Object> parameters)
            throws Exception {
        for (String key : parameters.keySet()) {
            privateField(plugin, key, parameters.get(key));
        }
    }

    /**
     * Configures execution parameters for {@link PlugIn} using a new user dialog.
     * 
     * @param plugin Plugin.
     * @param parameters Execution parameters.
     * @param retrieveFieldNamesFromPlugIn Retrieve {@link I18N} string names.
     */
    public static void configurePlugIn(PlugIn plugin, Map<String, Object> parameters, 
            boolean retrieveFieldNamesFromPlugIn) throws Exception {
        DialogParameters dialogParameters = new DialogParameters();
        for (String key : parameters.keySet()) {
            Object fieldValue = parameters.get(key);
            String fieldName = key;
            if (retrieveFieldNamesFromPlugIn) {
                fieldName = (String) privateStaticField(plugin.getClass(), fieldName);
            }
            dialogParameters.putField(fieldName, fieldValue);
        }
        // TODO: Throw specific exception if plugin has no field "dialog".
        privateField(plugin, "dialog", dialogParameters);
    }
    
    /**
     * Executes operations of the {@link Plugin}.
     * 
     * @param plugin Plugin.
     * @param context Context.
     * @see com.vividsolutions.jump.workbench.plugin.AbstractPlugIn#toActionListener
     */
    public static void executePlugIn(PlugIn plugin, WorkbenchContext context) 
            throws Exception {
        TaskMonitor taskMonitor = new DummyTaskMonitor();
        PlugInContext plugInContext = context.createPlugInContext();
        // TODO: Start UndoableEditReceiver (see AbstractPlugIn.toActionListener).
        //AbstractPlugIn.toActionListener(plugin, context, taskMonitorManager);
        if (plugin instanceof ThreadedPlugIn) {
            // TODO: Wait until plugin has finished.
            ((ThreadedPlugIn) plugin).run(taskMonitor, plugInContext);
        }
        else {
            String message = "Please use PlugIn.execute(context) directly.";
            throw new IllegalArgumentException(message);
        }
    }
    
    public static DataSource getDataSource() {		
        Shapefile dataSourceClass = new Shapefile();
		DataSource dataSource = (DataSource) LangUtil.newInstance(dataSourceClass.getClass()); 
		
		return dataSource;
	}
	
	public static DataSourceQuery getDataSourceQuery(DataSource dataSource, CoordinateSystem coordinateSystem, File file ) {	
		Map<String, Object> options = new HashMap<String, Object>();
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put(DataSource.FILE_KEY, file.getPath());
        properties.put(DataSource.COORDINATE_SYSTEM_KEY, coordinateSystem.getName());
        properties.putAll(options);
        dataSource.setProperties(properties);       
        String layerName = GUIUtil.nameWithoutExtension(file) ;
        DataSourceQuery dataSourceQuery = new DataSourceQuery(dataSource, null, layerName); 
        
		return dataSourceQuery;
	}
    
    public static FeatureCollection getFeatureCollection(DataSourceQuery dataSourceQuery) {
		Connection connection = dataSourceQuery.getDataSource().getConnection();
        ArrayList queryExceptions  = new ArrayList();
        TaskMonitor taskMonitor =  null; 
        FeatureCollection featureCollection  = connection.executeQuery(dataSourceQuery.getQuery(), queryExceptions, taskMonitor);
		return featureCollection;
	}
    
    public static void printResults(FeatureCollection featureCollection) {
		System.out.println(featureCollection);
        System.out.println(featureCollection.getFeatures().size());
        System.out.println("(" + featureCollection.getEnvelope().getMaxX() + "," + featureCollection.getEnvelope().getMaxY() + ")");
        System.out.println("(" + featureCollection.getEnvelope().getMinX() + "," + featureCollection.getEnvelope().getMinY() + ")");
	}
        
    
}