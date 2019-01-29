package gui;

import java.awt.Cursor;
import java.awt.Dimension;
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
import javax.swing.border.TitledBorder;

import configuration.ConfigurationFileBaseReader;
import mestra.MestraFileType;
import mestra.MestraFiles;
import mestra.MestraScenario;
import mestra.MestraFileType.MestraFileTypeEnum;

/**
 * This class manages a TAB in order to allow MESTRA analysis of several files of the same type.
 * @author Robert Pastor
 * @since November 2007 
 *
 */

public class MultipleMestraChecksTab extends JPanel implements ActionListener {

	final static Logger logger = Logger.getLogger(MultipleMestraChecksTab.class.getName()); 

	private static final long serialVersionUID = -1897604103129424807L;

	private FileTypeComboBox fileTypeComboBox = null;

	private JButton MestraAnalysisButton = null;
	private JDropTablePanel jDropTablePanel = null;

	private MainGuiApp mainGuiApp = null;
	// the configuration object
	private ConfigurationFileBaseReader configuration = null;

	public ConfigurationFileBaseReader getConfiguration() {
		if (mainGuiApp != null) {
			return mainGuiApp.getConfiguration();
		}
		else {
			return null;
		}
	}

	private StatusBar getStatusBar () {
		if (mainGuiApp != null) {
			return mainGuiApp.getStatusBar();           
		}
		else {
			return null;
		}
	}

	private void writeDebugMessage (String str) {
		if (mainGuiApp != null) {
			mainGuiApp.writeDebugMessage(str);
		}
	}

	private void setStatusBarMessage(String strMessage) {
		if (mainGuiApp != null) {
			mainGuiApp.getStatusBar().setMessage(strMessage);           
		}
	}

	private class MultipleFilesMestraAnalysisThread extends Thread
	{
		private Thread analysisFilesThread = null;

		private MestraFiles mestraFiles = null;
		private ConfigurationFileBaseReader configuration = null;
		private MultipleMestraChecksTab multipleMestraChecksTab = null;

		public MultipleFilesMestraAnalysisThread(MestraFiles aMestraFiles,
				ConfigurationFileBaseReader aConfiguration,
				MultipleMestraChecksTab aMultipleMestraChecksTab) {
			
			this.mestraFiles = aMestraFiles;
			this.configuration = aConfiguration;
			this.multipleMestraChecksTab = aMultipleMestraChecksTab;
			this.analysisFilesThread = new Thread(this);
		}   

		public void start() {
			this.analysisFilesThread.start();
		}

		public void run()
		{
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			MestraAnalysisButton.setEnabled(false);

			logger.info("Multiple File Analysis run thread: run method");

			MestraScenario mestraScenario = new MestraScenario(MestraScenario.MestraScenarioEnum.trace_All);
			this.mestraFiles.setMestraScenario(mestraScenario);

			// launch extraction of Mestra Markers from all the input files
			boolean bool = mestraFiles.RunMestraScenario(mestraFiles.getMestraScenario(), 
					configuration,
					"",
					this.multipleMestraChecksTab.getStatusBar());

			if (bool == true) {
				// display a frame with the Multiple MESTRA Files analysis results
				MultipleMestraResults multipleMestraResults = new MultipleMestraResults();
				multipleMestraResults.viewResults(mestraFiles, configuration);
			}
			
			// update the status bar
			this.multipleMestraChecksTab.getStatusBar().setMessage("Multiple File Analysis run finished!");			

			setCursor(Cursor.getDefaultCursor());
			MestraAnalysisButton.setEnabled(true);
		}
	}

	private void launchMestraFilesAnalysis() {

		MestraFiles mestraFiles = new MestraFiles();

		if (jDropTablePanel.getRowCount() > 0)  {

			mestraFiles.addAll(jDropTablePanel.getMestraFileTableList());

			// we launch the Multiple file MESTRA Analysis exercise in a thread
			MultipleFilesMestraAnalysisThread mestraAnalysisThread = new MultipleFilesMestraAnalysisThread(mestraFiles,
					this.getConfiguration(),
					this);
			mestraAnalysisThread.start();
		}
		else {
			logger.log(Level.SEVERE, "Need at least one file to run an analysis!");
			setStatusBarMessage("Need at least one file to run an analysis!");
		}
	}

	public void actionPerformed(ActionEvent e) {
		//
		if (e.getSource() == MestraAnalysisButton) {
			//writeDebugMessage("----------------Mestra Analysis button Pressed");
			launchMestraFilesAnalysis();
		}
	}

	private MestraFileTypeEnum getFileType() {
		String strFileType = fileTypeComboBox.getSelectedFileType();
		return (new MestraFileType(strFileType).getMestraFileType());
	}

	private void createMultipleMestraChecksTab() {

		GridBagConstraints c = new GridBagConstraints();
		//this.setLayout(new GridLayout(3,1));

		c.fill=GridBagConstraints.BOTH;

		//==============================================
		TitledBorder title;
		title = BorderFactory.createTitledBorder("Step 1: Select the file type");

		fileTypeComboBox = new FileTypeComboBox();
		fileTypeComboBox.setBorder(title);

		//===============================================================================================
		jDropTablePanel = new JDropTablePanel("Step 2: Drop here the Mestra files", getFileType());
		jDropTablePanel.setDropTarget(new DropTarget(jDropTablePanel, jDropTablePanel) );
		JScrollPane scrollerPane = new JScrollPane(jDropTablePanel);
		scrollerPane.setPreferredSize(new Dimension(800, 100));

		
		//==============================================================
		// use this button to reload a file / re analyze the MESTRA files
		// =============================================================
		JPanel jButtonPanel = new JPanel();
		title = BorderFactory.createTitledBorder("Step 3: Push the button to run the analysis");
		jButtonPanel.setBorder(title);

		MestraAnalysisButton = new JButton("Run MESTRA analysis");
		MestraAnalysisButton.addActionListener(this);     
		jButtonPanel.add(MestraAnalysisButton);

		//===========================================
		// add the component to the tabbed pane
		c.gridx=0;
		c.gridy=0;
		c.weightx=1; 
		c.weighty=0; 
		add(fileTypeComboBox,c);
		
		//=================================================
		c.gridx=0; 
		c.gridy=1;
		c.weightx=1;
		c.weighty=3;
		add(scrollerPane,c);
		
		//=================================================

		c.gridx=0; 
		c.gridy=2;
		c.weightx=1;
		c.weighty=0; 
		add(jButtonPanel,c);

	}

	//	Constructor
	public MultipleMestraChecksTab(MainGuiApp mainGuiApp) {
		super(new GridBagLayout());
		this.mainGuiApp = mainGuiApp;
		createMultipleMestraChecksTab();
	}

	public ConfigurationFileBaseReader getaConfiguration() {
		return this.configuration;
	}

	public void setaConfiguration(ConfigurationFileBaseReader aConfiguration) {
		this.configuration = aConfiguration;
	}

}
