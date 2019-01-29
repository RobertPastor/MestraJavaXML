package configuration;

/*
 * Class created April 2007
 * Author : Robert Pastor
 * 
 * this class implements the configuration tab
 * This class uses the MESTRA Configuration class to read the configuration sheet
 * 
 */


import gui.FileChooserFileFilter;
import gui.MainGuiApp;
import gui.StatusBar;
import gui.TextAreaDropTarget;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.dnd.DropTarget;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

/**
 * An SWING tab based upon JPanel.
 * @author t0007330 - Robert PASTOR
 * @since June 2007
 *
 */
public class ConfigurationTabExcel extends JPanel implements ActionListener {
	
	final static Logger logger = Logger.getLogger(ConfigurationTabExcel.class.getName()); 

	private static final long serialVersionUID = 6961437019971464773L;

	private JButton FileChooserButton =null;
	private JButton LoadConfigurationFileButton = null;

	// manage either an XLS or an XML configuration file
	private String configurationFilePath = "";
	private ConfigurationFileXLSReader aConfiguration = null;

	private JFrame jFCframe = null;

	private JTextArea textFileDropArea = null;
	private ConfigurationTableView configurationTableView = null;

	private MainGuiApp mainGuiApp = null;
	
	public ConfigurationTabExcel(MainGuiApp mainGuiApp) {

		super(new GridBagLayout());
		logger.info( "constructor" );
		this.mainGuiApp = mainGuiApp;

		// manage either an XLS or an XML configuration file
		this.configurationFilePath = mainGuiApp.getConfigurationFilePath("xls");
		CreateConfigurationTab();
	}

	public ConfigurationFileXLSReader GetConfiguration() {
		return this.aConfiguration;
	}

	private void writeDebugMessage(String str) {
		if (this.mainGuiApp != null) {
			this.mainGuiApp.writeDebugMessage(str);
		}
	}

	private void setStatusBarMessage(String strMessage) {
		if (this.mainGuiApp != null) {
			this.mainGuiApp.getStatusBar().setMessage(strMessage);    		
		}
	}

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
			aConfiguration = new ConfigurationFileXLSReader(configurationFilePath, mainGuiApp.getStatusBar());
			if (aConfiguration.isConfigurationOK()) {
				String str = "Configuration file "+configurationFilePath+" analysed correctly!";
				setStatusBarMessage(str);
				if (configurationTableView != null) {
					writeDebugMessage("reload Configuration Table View");
					configurationTableView.reload(aConfiguration);
				}
				else {
					writeDebugMessage("Configuration File Thread: Configuration Table View is null");
				}
			}
			else {
				String str = "Configuration file "+configurationFilePath+" NOT analysed correctly!";
				setStatusBarMessage(str);
				aConfiguration = null;
			}

			setCursor(Cursor.getDefaultCursor());
			LoadConfigurationFileButton.setEnabled(true);
		}
	}

	private void LoadConfigurationSheet(){

		writeDebugMessage("Configuration Tab: Load Configuration Sheet");
		StatusBar statusBar = getStatusBar();
		statusBar.startProgressBarRunner();

		// the following task takes time, hence launch a thread
		//ConfigurationFileThread configurationFileThread = new ConfigurationFileThread(this);
		ConfigurationFileThread configurationFileThread = new ConfigurationFileThread();
		configurationFileThread.start();
		try {
			configurationFileThread.join();
		}
		catch (InterruptedException e) {}
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
		textFileDropArea.setText(configurationFilePath);

		JScrollPane scroller = new JScrollPane(textFileDropArea);

		textFileDropArea.setDropTarget(new DropTarget(textFileDropArea,new TextAreaDropTarget(textFileDropArea)));
		textFileDropArea.setFont(new Font("Arial",Font.PLAIN,11));

		TitledBorder title;
		title = BorderFactory.createTitledBorder("Configuration file: drop here from an Explorer");
		scroller.setBorder(title);
		scroller.setPreferredSize(new Dimension(800, 50));

		//============================================================
		//the file chooser button
		//============================================================
		//FileChooserButton = new JButton("Change Configuration File");
		//FileChooserButton.setPreferredSize(new Dimension(240,26));
		//FileChooserButton.addActionListener(this);

		//JPanel jButtonPanel = new JPanel();
		//title = BorderFactory.createTitledBorder("Use the following button to launch a file chooser and change the configuration file");
		//jButtonPanel.setBorder(title);
		//jButtonPanel.add(FileChooserButton);

		//add(jButtonPanel, c);

		//-------------------------------------------------
		//the Panel containing the configuration table view
		//-------------------------------------------------

		String panelTitle = "Configuration Table view";

		if (aConfiguration != null) {
			writeDebugMessage("Configuration Tab: aConfiguration is not null");
			configurationTableView = new ConfigurationTableView(aConfiguration,panelTitle);
		}
		else {
			writeDebugMessage("Configuration Tab: aConfiguration is null");
			configurationTableView = new ConfigurationTableView(null,panelTitle);
		}
		configurationTableView.setPreferredSize(new Dimension(800, 400));

		//=====================================================================
		//the button (and its panel) used to load the configuration file
		//=====================================================================
		LoadConfigurationFileButton = new JButton("Load the configuration file");
		LoadConfigurationFileButton.setPreferredSize(new Dimension(240,26));
		LoadConfigurationFileButton.addActionListener(this);

		JPanel jLoadConfigurationButtonPanel = new JPanel();
		title = BorderFactory.createTitledBorder("Use the following button to load the configuration file");
		jLoadConfigurationButtonPanel.setBorder(title);
		jLoadConfigurationButtonPanel.add(LoadConfigurationFileButton);

		//===============================
		// add the components to the Tab
		//===============================

		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1; 
		c.weighty = 0;
		add(scroller,c);

		//================================
		// Add the table view scroll pane to this panel
		//=============================================
		c.gridx = 0; 
		c.gridy = 1;
		c.weightx = 1; 
		c.weighty = 2;
		add(configurationTableView,c);

		//=============================================
		// add the button panel
		//==============================================
		
		c.gridx = 0; 
		c.gridy = 2;
		c.weightx = 1; 
		c.weighty = 0; 

		// Add the new Button panel to this panel.
		add(jLoadConfigurationButtonPanel,c);

		if (aConfiguration == null) {
			LoadConfigurationSheet();
		}
	}



	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == FileChooserButton) {
			writeDebugMessage("File Chooser Button pressed in Configuration tab");
			// construct a frame to display the file chooser
			jFCframe = new JFrame("Configuration file chooser");
			jFCframe.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

			JFileChooser fc = new JFileChooser(configurationFilePath);
			fc.setMultiSelectionEnabled(false);
			fc.setDragEnabled(true);
			fc.setControlButtonsAreShown(false);
			//((Component)fc).setPreferredSize(new Dimension(600, 400));

			FileChooserFileFilter filter = new FileChooserFileFilter(new String("xls"), "Microsoft EXCEL documents");
			fc.addChoosableFileFilter(filter);
			fc.setFileFilter(filter);

			// Applet specific bug with Java 1.4 
			// need to call getContentPane before adding the file chooser
			jFCframe.getContentPane().add(fc);
			jFCframe.pack();
			jFCframe.setVisible(true);
		}
		else {
			if (e.getSource() == LoadConfigurationFileButton) {
				// get the update file name from the upper text area
				writeDebugMessage("LoadConfigurationFileButton pressed in Configuration tab");
				configurationFilePath = textFileDropArea.getText();
				if (configurationFilePath.length() > 0) {
					// load the corresponding configuration sheet
					LoadConfigurationSheet();     
				}
			}
		}
	}
}


