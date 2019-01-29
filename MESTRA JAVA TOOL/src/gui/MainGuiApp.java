package gui;

/*
 * Robert Pastor :
 * May 2007
 * This class manages the Main GUI of the Java MESTRA Tool
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.io.File;
import java.net.URLDecoder;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import XGridBagLayout.CellStyle;
import configuration.ConfigurationFileBaseReader;
import configuration.ConfigurationFilesFactory;
import configuration.ConfigurationTabXML;

/**
 * Build the main Graphical User Interface.
 * @author t0007330
 * @since June 2007
 *
 */
public class MainGuiApp extends JPanel implements ChangeListener {

	final static Logger logger = Logger.getLogger(MainGuiApp.class.getName()); 

	private static final long serialVersionUID = 2915222862992446115L;

	private ConfigurationFilesFactory configurationFilesFactory = null;

	// private  ConfigurationFileXLSReader aConfiguration = null;
	private  StatusBar statusBar = null;

	private String mestraRootFolderName = "";

	// the following objects are composing the main tabs of the GUI
	private  ConfigurationTabXML configurationTabXML = null;

	private  MestraChecksTab mestraChecksTab = null;
	private  TraceabilityTab traceabilityTab = null;
	private  DebugTab debugTab = null;

	//private  MultipleMestraChecksTab multipleMestraChecksTab = null;

	// cell styles used for the XGridBag Layout
	private CellStyle[] cellStyles = null;

	public CellStyle[] getCellStyles() {
		return this.cellStyles;
	}


	public StatusBar getStatusBar() {
		return this.statusBar;
	}

	public void writeDebugMessage(String str) {
		logger.info ( str );
		if (this.debugTab != null) {
			this.debugTab.appendDebugMessage(str);
			this.debugTab.appendDebugMessage("\n");
		}
	}

	public static File getFile(String name) {

		java.net.URL base = MainGuiApp.class.getResource(".");
		try{
			return new File(URLDecoder.decode(base.getPath(),"UTF-8"), name); 
		}
		catch(java.io.UnsupportedEncodingException uee){
			logger.log( Level.SEVERE , "MainGuiApp getFile : Unable to decode file path ", uee);
			throw new RuntimeException("MainGuiApp getFile : Unable to decode file path ", uee);
		}
	}

	private CellStyle[] createGridBagCellStyles() {

		// Create the inserts (insets)
		Insets cellInsets0 = new Insets(12, 2, 2, 2);
		Insets cellInsets1 = new Insets(2, 2, 2, 2);
		Insets cellInsets2 = new Insets(2, 12, 2, 2);

		// Create the cell styles
		CellStyle[] cellStyles = { 
				// title style
				new CellStyle(0.0, 0.0,
						GridBagConstraints.FIRST_LINE_START, 
						GridBagConstraints.BOTH, cellInsets0, 2, 0) ,

						// fixed Style
						new CellStyle(0.0, 0.0,
								GridBagConstraints.LINE_START, 
								GridBagConstraints.NONE, cellInsets1, 2, 4)	,	

								// fixed style 2
								new CellStyle(0.0, 0.0,
										GridBagConstraints.LINE_START, 
										GridBagConstraints.NONE, cellInsets2, 2, 4),

										// variable Horizontal Style
										new CellStyle(0.0, 0.0,
												GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL, cellInsets1, 2, 0),

												// variable Horizontal Vertical Size
												new CellStyle(1.0, 1.0,
														GridBagConstraints.FIRST_LINE_START, 
														GridBagConstraints.BOTH, cellInsets1, 2, 0)        

		};
		return cellStyles;
	}

	
	public MainGuiApp() {

		//super(new GridLayout(2,1));
		super(new BorderLayout());

		// create the cell styles
		this.cellStyles = createGridBagCellStyles();

		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addChangeListener(this);

		this.statusBar = new StatusBar(this);

		this.mestraRootFolderName = "Mestra Tool";
		this.configurationFilesFactory = new ConfigurationFilesFactory(mestraRootFolderName);

		if (this.configurationFilesFactory.isV3_XML()) {
			
			//POI default logger
			System.setProperty("org.apache.poi.util.POILogger", "org.apache.poi.util.NullLogger" );

			logger.info( " ==================>>> it is a V3 XML configuration !!! ");

			this.configurationTabXML = new ConfigurationTabXML(this);
			this.configurationTabXML.setOpaque(true);

			//=============================================
			// add the configuration tab to the TABBED panel
			//=============================================
			tabbedPane.addTab("<html><center>XML<br>Configuration</center></html>", createImageIcon("xml.png"), configurationTabXML, "Manage Configuration file");
			
			//
			// add the XML Configuration HELP tab
			//===================================
			
			HelpTab configurationHelpTab = new HelpTab("configuration");
			configurationHelpTab.setBackground(Color.yellow);
			configurationHelpTab.setOpaque(true);
			
			tabbedPane.addTab("<html><center>Configuration<br>Help</center></html>", createImageIcon("question-mark.png"), configurationHelpTab, "help");

			//=============================================
			// add the Mestra Checks tab to the TABBED panel
			//=============================================

			this.mestraChecksTab = new MestraChecksTab(this);
			this.mestraChecksTab.setOpaque(true); //content panes must be opaque

			tabbedPane.addTab("<html><center>Mestra<br>Checks</center></html>", createImageIcon("checks.png"), mestraChecksTab, "Analyse one MESTRA file");
			
			/**============================================
			 * add the Help Tab
			 =============================================*/
			HelpTab mestraChecksHelpTab = new HelpTab("mestra checks");
			mestraChecksHelpTab.setBackground(Color.yellow);
			mestraChecksHelpTab.setOpaque(true);
			
			tabbedPane.addTab("<html><center>Mestra<br>Checks<br>Help</center></html>", createImageIcon("question-mark.png"), mestraChecksHelpTab, "help");
			
			//=============================================
			// add the trace-ability tab to the TABBED panel
			//=============================================
			this.traceabilityTab = new TraceabilityTab(this);
			this.traceabilityTab.setOpaque(true); //content panes must be opaque

			tabbedPane.addTab("<html><center>Traceability<br>Checks</center></html>", createImageIcon("cycle.png"), traceabilityTab, "Run Traceability");
			//tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);
			
			/**============================================
			 * add the traceability Help Tab
			 =============================================*/
			HelpTab traceabilityHelpTab = new HelpTab("traceability");
			mestraChecksHelpTab.setBackground(Color.yellow);
			traceabilityHelpTab.setOpaque(true);
			
			tabbedPane.addTab("<html><center>Traceability<br>Help</center></html>", createImageIcon("question-mark.png"), traceabilityHelpTab, "help");


		} else {
			String str = "Configuration File not found!";
			this.statusBar.setMessage(str);
			logger.log(Level.SEVERE , "Configuration file not found !!!");
		}

		//=============================================
		//add multiple MESTRA files check 
		//=============================================
		//this.multipleMestraChecksTab = new MultipleMestraChecksTab(this);
		//this.multipleMestraChecksTab.setOpaque(true);

		//tabbedPane.addTab("Multiples Mestra Checks", null, multipleMestraChecksTab, "Analyse MESTRA files");
		//tabbedPane.setMnemonicAt(3, KeyEvent.VK_3);

		//=============================================
		// add the debug tab
		//=============================================
		this.debugTab = new DebugTab();
		this.debugTab.setOpaque(true); 

		tabbedPane.addTab("<html><center>Debug<br>Messages</center></html>", null, debugTab, "Debug helper");
		//tabbedPane.setMnemonicAt(4, KeyEvent.VK_4);


		// The following line enables to use scrolling tabs
		//=================================================
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

		//Add the tabbed pane to this panel.
		JPanel jPanelTabbedPane = new JPanel();
		jPanelTabbedPane.add(tabbedPane);

		this.add(tabbedPane, BorderLayout.CENTER);
		//g.add(tabbedPane, cellStyles[3], 0, 0);

		this.add(this.statusBar, BorderLayout.SOUTH);
		//g.add(this.statusBar, cellStyles[1], 1, 0);

		if (this.configurationFilesFactory.checkXMLConfigurationFileExists() == false) {
			this.statusBar.setMessage("XML configuration file not found !!!");
			this.writeDebugMessage("XML configuration file not found !!!");
		}

		logger.info ( "MainGuiApp: end of main!");
	}


	/**
	 *      
	 *  Returns an ImageIcon, or null if the path was invalid. 
	 */
	protected  ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = MainGuiApp.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			String str = "Main Gui App: Couldn't find Image Icon file: " + path;
			logger.log(Level.SEVERE, str);
			writeDebugMessage(str);
			return null;
		}
	}

	public void stateChanged(ChangeEvent e) {

		//writeDebugMessage("----------------------------");
		//logger.info( "Main Gui Application: state Changed: " + e.toString());
		//writeDebugMessage(e.getSource().toString()) ;
		//writeDebugMessage("----------------------------");
		if (this.traceabilityTab != null) {
			//logger.info( "Main Gui Application: traceability Tab reload");
			this.traceabilityTab.reload();			
		}
	}

	/**
	 * fileExtension = "xls" or "xml"
	 * @param fileExtension
	 * @return
	 */
	public String getConfigurationFilePath(String fileExtension) {
		logger.info( "file Extension = " + fileExtension);
		String configurationFilePath = this.configurationFilesFactory.getConfigurationFilePath(fileExtension);
		logger.info( "configuration file path = "+ configurationFilePath);
		return configurationFilePath;
	}

	public String getConfigurationFileDirectory() {
		return this.configurationFilesFactory.getMestraToolRootDirectory(this.mestraRootFolderName);
	}

	public ConfigurationFileBaseReader getConfiguration() {
		return this.configurationFilesFactory.getConfigurationBaseReader();
	}
	
	public void notifyConfigurationChanges(ConfigurationFileBaseReader configurationFileBaseReader) {
		logger.log(Level.INFO, "notify configuration changes");
		this.configurationFilesFactory.seConfigurationBaseReader(configurationFileBaseReader);
		this.traceabilityTab.reload();
	}
	
}


