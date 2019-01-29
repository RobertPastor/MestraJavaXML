package configuration;

import gui.MainGuiApp;
import gui.StatusBar;
import gui.TextAreaDropTarget;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.dnd.DropTarget;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;


public class ConfigurationTabXML extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1894523641979055998L;

	final static Logger logger = Logger.getLogger(ConfigurationTabXML.class.getName()); 

	private MainGuiApp mainGuiApp = null;
	private JButton LoadConfigurationFileButton = null;

	// manage either an XLS or an XML configuration file
	private String configurationXMLFilePath = "";

	private JTextArea textFileDropArea = null;
	private ConfigurationTreeView configurationTreeView = null;
	
	private ConfigurationFileXMLReader aConfiguration = null;

	public ConfigurationTabXML(MainGuiApp mainGuiApp) {

		super(new GridBagLayout());
		//logger.info( "constructor");
		this.mainGuiApp = mainGuiApp;

		// manage either an XLS or an XML configuration file
		this.configurationXMLFilePath = mainGuiApp.getConfigurationFilePath("xml");
		logger.info( this.configurationXMLFilePath );
		CreateConfigurationTab();
	}

	private void CreateConfigurationTab() {

		GridBagConstraints c = new GridBagConstraints();   
		//this.setLayout(new GridLayout(3,1));

		//-----------------------------------------------
		// the text area with the configuration file name
		//-----------------------------------------------
		textFileDropArea = new JTextArea(3, 40);
		textFileDropArea.setLineWrap(true);
		// set the text area with the configuration file path
		textFileDropArea.setText(configurationXMLFilePath);

		JScrollPane scroller = new JScrollPane(textFileDropArea);

		textFileDropArea.setDropTarget(new DropTarget(textFileDropArea, new TextAreaDropTarget(textFileDropArea)));
		textFileDropArea.setFont(new Font("Arial",Font.PLAIN,11));

		TitledBorder title;
		title = BorderFactory.createTitledBorder("Configuration file: drop here from an Explorer");
		scroller.setBorder(title);
		scroller.setPreferredSize(new Dimension(800, 50));

		//-------------------------------------------------
		//the Panel containing the configuration table view
		//-------------------------------------------------
		String panelTitle = "Configuration XML Tree view - click on a node to open or to close it";

		writeDebugMessage("Configuration Tab XML : build the tree view");
		this.configurationTreeView = new ConfigurationTreeView(this.configurationXMLFilePath, panelTitle);
		
		this.configurationTreeView.setPreferredSize(new Dimension(800, 400));

		//=====================================================================
		//the button (and its panel) used to load the configuration file
		//=====================================================================
		this.LoadConfigurationFileButton = new JButton("Load the XML configuration file");
		this.LoadConfigurationFileButton.setPreferredSize(new Dimension(240,26));
		this.LoadConfigurationFileButton.addActionListener(this);
        this.LoadConfigurationFileButton.setBackground(Color.orange);
        this.LoadConfigurationFileButton.setBorderPainted(true);
        this.LoadConfigurationFileButton.setFont(new Font("Arial", Font.BOLD, 12));
        this.LoadConfigurationFileButton.setOpaque(true);


		JPanel jLoadConfigurationButtonPanel = new JPanel();
		title = BorderFactory.createTitledBorder("Use the following button to load the XML configuration file");
		jLoadConfigurationButtonPanel.setBorder(title);
		jLoadConfigurationButtonPanel.add(this.LoadConfigurationFileButton);

		//===============================
		// add the components to the Tab
		//===============================

		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1; 
		c.weighty = 0;
		this.add(scroller,c);

		//================================
		// Add the table view scroll pane to this panel
		//=============================================
		c.gridx = 0; 
		c.gridy = 1;
		c.weightx = 1; 
		c.weighty = 2;
		this.add(configurationTreeView, c);

		//=============================================
		// add the button panel
		//==============================================
		
		c.gridx = 0; 
		c.gridy = 2;
		c.weightx = 1; 
		c.weighty = 0; 

		// Add the new Button panel to this panel.
		this.add(jLoadConfigurationButtonPanel, c);

	}
	
	private void writeDebugMessage(String str) {
		if (this.mainGuiApp != null) {
			this.mainGuiApp.writeDebugMessage(str);
		}
	}
	
/*	private void LoadConfigurationTreeView(){

		writeDebugMessage("Configuration Tab: Load Configuration Tree View");
		StatusBar statusBar = getStatusBar();
		statusBar.startProgressBarRunner();

		// the following task takes time, hence launch a thread
		ConfigurationFileThread configurationFileThread = new ConfigurationFileThread();
		configurationFileThread.start();
		try {
			configurationFileThread.join();
		}
		catch (InterruptedException e) {}
	}*/
	
	private StatusBar getStatusBar () {
		if (this.mainGuiApp != null) {
			return this.mainGuiApp.getStatusBar();    		
		}
		else {
			return null;
		}
	}
	
	private class ConfigurationFileThread extends Thread {
		private Thread configurationFileThread = null;
		//private ConfigurationTab configurationTab = null;

		//ConfigurationFileThread(ConfigurationTab aConfigurationTab){
		ConfigurationFileThread(){
			//configurationTab = aConfigurationTab;
			configurationFileThread = new Thread(this);
		}	

		public void start() {
			configurationFileThread.start();
			writeDebugMessage("Configuration File Thread: thread start");
		}

		public void run() {
			
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			LoadConfigurationFileButton.setEnabled(false);

			writeDebugMessage("Configuration File Thread: run");
			aConfiguration = new ConfigurationFileXMLReader(configurationXMLFilePath, mainGuiApp.getStatusBar());
			if (aConfiguration.isConfigurationOK()) {
				String str = "Configuration file "+configurationXMLFilePath+" analysed correctly!";
				setStatusBarMessage(str);
				if (configurationTreeView != null) {
					writeDebugMessage("reload Configuration Table View");
					configurationTreeView.reload(aConfiguration);
				}
				else {
					writeDebugMessage("Configuration File Thread: Configuration Table View is null");
				}
			}
			else {
				String str = "Configuration file "+configurationXMLFilePath+" NOT analysed correctly!";
				setStatusBarMessage(str);
				aConfiguration = null;
			}

			setCursor(Cursor.getDefaultCursor());
			LoadConfigurationFileButton.setEnabled(true);
		}
	}
	
	private void setStatusBarMessage(String strMessage) {
		if (this.mainGuiApp != null) {
			this.mainGuiApp.getStatusBar().setMessage(strMessage);    		
		}
	}
	
	public MainGuiApp getMainGuiApp() {
		return this.mainGuiApp;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof JButton) {
			logger.log(Level.INFO, "Load configuration file has been pressed");
			ConfigurationFileThread configurationFileThread = new ConfigurationFileThread();
			configurationFileThread.run();
			// need to notify the Mestra Checks and Traceability checks ... classes
			this.mainGuiApp.notifyConfigurationChanges(aConfiguration);
		}
	}
}
