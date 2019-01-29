package gui;

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
import javax.swing.border.TitledBorder;

import mestra.MestraFileType.MestraFileTypeEnum;
import mestra.MestraFiles;
import mestra.MestraScenario;
import configuration.ConfigurationFileBaseReader;

/**
 * 
 *
 * this class aims to manage trace-ability runs / exercises.<br>
 *
 * The aim of this class is to manage the Trace-ability Tab.
 * @author Pastor Robert
 * @since May 2007
 *
 */

public class TraceabilityTab extends JPanel implements ActionListener {

	final static Logger logger = Logger.getLogger(TraceabilityTab.class.getName()); 

	private static final long serialVersionUID = 3447472934661646436L;

	// the COMBO containing the different file types 
	// used by pairs in a trace-ability run
	TraceabilityTypeComboBox traceabilityTypeComboBox = null;

	// the COMBO containing the different CSCI names used for a trace-ability run
	TraceabilityCSCIComboBox traceabilityCSCIComboBox = null;
	// 16th July 2008: need to store the CSCI and restore it each time the tab is changed
	String storedTraceabilityCSCI = "";

	// the configuration file
	private ConfigurationFileBaseReader aConfiguration = null;

	private JDropTablePanel jUpStreamDropTable = null;
	private JDropTablePanel jDownStreamDropTable = null;

	//private JButton FileChooserButton = null;
	private JButton RunTraceabilityButton = null;

	private MainGuiApp mainGuiApp = null;
	private MestraFiles mestraFiles = null;
	//private JLabel jExcelOutputFileDirectory = null;

	public MestraFiles getMestraFiles() {
		return this.mestraFiles;
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

	private void createTraceabilityTab() {

		logger.info( "Traceability Tab: create Tab");
		GridBagConstraints c = new GridBagConstraints();

		c.fill=GridBagConstraints.BOTH;
		//this.setLayout(new GridLayout(5,1));

		//==================================================================================
		TitledBorder title;
		title = BorderFactory.createTitledBorder("Step 1: Select the type of traceability");

		traceabilityTypeComboBox = new TraceabilityTypeComboBox(this);
		traceabilityTypeComboBox.setBorder(title);

		//===============================================================================================
		title = BorderFactory.createTitledBorder("Step 2: Select the CSCI - for SSS-SRS Traceability only");

		traceabilityCSCIComboBox = new TraceabilityCSCIComboBox(this);
		traceabilityCSCIComboBox.setBorder(title);

		//===============================================================================================

		jUpStreamDropTable = new JDropTablePanel("Step 3: Drop here the Upstream files - Warning  - only .docx (word 2007 format) are expected !!! ",
				getUpStreamFileType());
		jUpStreamDropTable.setDropTarget(new DropTarget(jUpStreamDropTable, jUpStreamDropTable) );
		jUpStreamDropTable.setPreferredSize(new Dimension(800, 200));
		jUpStreamDropTable.setOpaque(false);
		
		//===============================================================================================

		jDownStreamDropTable = new JDropTablePanel("Step 4: Drop here the Downstream files - Warning  - only .docx (word 2007 format) are expected !!! ",
				getDownStreamFileType());
		jDownStreamDropTable.setDropTarget(new DropTarget(jDownStreamDropTable, jDownStreamDropTable) );
		jDownStreamDropTable.setPreferredSize(new Dimension(800, 200));
		jDownStreamDropTable.setOpaque(false);
		
		//===============================================================================================
		// need to tell the Traceability Type combo box how to notify the changes to the drop table panels.
		traceabilityTypeComboBox.notifyChangesTo(jUpStreamDropTable, jDownStreamDropTable); 

		//===============================================================================================

		this.RunTraceabilityButton = new JButton("Launch the Traceability analysis");
		this.RunTraceabilityButton.setPreferredSize(new Dimension(250,26));
		this.RunTraceabilityButton.addActionListener(this);
        this.RunTraceabilityButton.setBackground(Color.orange);
        this.RunTraceabilityButton.setBorderPainted(true);
        this.RunTraceabilityButton.setFont(new Font("Arial", Font.BOLD, 12));
        this.RunTraceabilityButton.setContentAreaFilled(false);
        this.RunTraceabilityButton.setOpaque(true);


		JPanel jRunButtonPanel = new JPanel();
		title = BorderFactory.createTitledBorder("Step 5: Use the following button to run a traceability exercice");
		jRunButtonPanel.setBorder(title);
		jRunButtonPanel.add(RunTraceabilityButton);
		jRunButtonPanel.setPreferredSize(new Dimension(800,50));

		//==========================================
		// add the different components to the panel
		//==========================================

		c.gridx=0;
		c.gridy=0;
		c.weightx=1; 
		c.weighty=0; 
		add(traceabilityTypeComboBox, c);

		//===========================

		c.gridx=0; 
		c.gridy=1;
		c.weightx=1;
		c.weighty=0;
		add(traceabilityCSCIComboBox, c);

		//===========================

		c.gridx=0; 
		c.gridy=2;
		c.weightx=1;
		c.weighty=2;
		add(jUpStreamDropTable, c);

		//============================

		c.gridx=0; 
		c.gridy=3;
		c.weightx=1;
		c.weighty=2;
		add(jDownStreamDropTable, c);

		//===========================

		c.gridx=0; 
		c.gridy=4;
		c.weightx=1;
		c.weighty=0;
		add(jRunButtonPanel,c);

	}

	/**
	 * Constructor
	 */ 
	public TraceabilityTab(MainGuiApp mainGuiApp) {

		super(new GridBagLayout());
		this.mainGuiApp = mainGuiApp;
		this.aConfiguration = mainGuiApp.getConfiguration();
		createTraceabilityTab();

		// store the trace-ability CSCI in order to restore it later after a TAB change
		this.storedTraceabilityCSCI = this.traceabilityCSCIComboBox.getSelectedTraceabilityCSCI();

	}

	private MestraFileTypeEnum getDownStreamFileType() {
		MestraScenario mestraScenario = traceabilityTypeComboBox.getMestraScenario();
		return mestraScenario.getMestraFileTypeDownStream();
	}

	private MestraFileTypeEnum getUpStreamFileType() {
		MestraScenario mestraScenario = traceabilityTypeComboBox.getMestraScenario();
		return mestraScenario.getMestraFileTypeUpStream();
	}

	/**
	 * launch the traceability analysis.
	 */
	private void launchTraceabilityAnalysis() {

		// store the trace-ability CSCI in order to restore it later after a TAB change
		this.storedTraceabilityCSCI = this.traceabilityCSCIComboBox.getSelectedTraceabilityCSCI();

		this.mestraFiles = new MestraFiles();

		String strMestraScenario = traceabilityTypeComboBox.getSelectedTraceabilityType();
		logger.info( "Traceability Tab: current scenario is: " + strMestraScenario);

		MestraScenario mestraScenario = new MestraScenario(strMestraScenario);
		this.mestraFiles.setMestraScenario(mestraScenario);

		// ensure that there is at least one item in each drop table
		if ((jUpStreamDropTable.getRowCount() > 0) && (jDownStreamDropTable.getRowCount() > 0)) {

			this.mestraFiles.addAll(jUpStreamDropTable.getMestraFileTableList());
			this.mestraFiles.addAll(jDownStreamDropTable.getMestraFileTableList());            

			// we launch the trace-ability exercise in a thread
			TraceabilityRunThread traceabilityRunThread = new TraceabilityRunThread(this.mestraFiles,
					this.getConfiguration(),
					traceabilityCSCIComboBox.getSelectedTraceabilityCSCI(),
					this);
			traceabilityRunThread.start();
		}
		else {
			logger.log(Level.SEVERE , "Need at least one Upstream and one DownStream file to run a traceability!");
			setStatusBarMessage("Need at least one Upstream and one DownStream file to run a traceability!");
		}
	}

	/**
	 * This method is call when the traceability button is pressed.
	 * 
	 */
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == RunTraceabilityButton) {
			logger.info( "Run Traceability Button pressed in Traceability tab");
			launchTraceabilityAnalysis();
		}
	}

	/**
	 * This method is called by the Main each time the Traceability Tab is pressed.
	 */
	public void reload() {
		//logger.info( "Traceability Tab: reload");
		this.aConfiguration = this.getConfiguration();
				
		this.traceabilityCSCIComboBox.reload();
		this.traceabilityCSCIComboBox.setSelectedTraceabilityCSCI(this.storedTraceabilityCSCI);
		this.repaint();
		
	}

	/**
	 * triggered each time the kind of traceability is modified.
	 * @param strTraceabilityType
	 */
	public void updateTraceabilityType(String strTraceabilityType) {
		logger.info( "Traceability Tab: update Traceability Type: "+strTraceabilityType);
		if (strTraceabilityType.equalsIgnoreCase("SSS_SRS") ) {
			// the CSCI selection is only applicable for a SSS to SRS traceability exercise
			this.traceabilityCSCIComboBox.setEnabled(true);
		} else {
			this.traceabilityCSCIComboBox.setEnabled(false);
		}
		// we need to change the Mestra File type stored in the Upstream and Down Stream tables
		MestraScenario mestraScenario = traceabilityTypeComboBox.getMestraScenario();
		jUpStreamDropTable.setMestraFileType(mestraScenario.getMestraFileTypeUpStream());
		jDownStreamDropTable.setMestraFileType(mestraScenario.getMestraFileTypeDownStream());

	}

	public ConfigurationFileBaseReader getConfiguration() {
		if (mainGuiApp != null) {
			return mainGuiApp.getConfiguration();
		} else {
			return null;
		}
	}

	private class TraceabilityRunThread extends Thread
	{
		private Thread analyseFileThread = null;

		private MestraFiles mestraFiles = null;
		private ConfigurationFileBaseReader configuration = null;
		private TraceabilityTab traceabilityTab = null;
		private String traceabilityCSCI = "";

		public TraceabilityRunThread(MestraFiles aMestraFiles,
				ConfigurationFileBaseReader configurationFileBaseReader,
				String TraceabilityCSCI,
				TraceabilityTab aTraceabilityTab)
		{	
			this.mestraFiles = aMestraFiles;
			this.configuration = configurationFileBaseReader;
			this.traceabilityTab = aTraceabilityTab;
			this.traceabilityCSCI = TraceabilityCSCI;
			analyseFileThread = new Thread(this);

		}	

		public void start() {
			analyseFileThread.start();
		}

		public void run()
		{
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			RunTraceabilityButton.setEnabled(false);

			logger.info( "Traceability run thread: run method");

			boolean bool = mestraFiles.RunMestraScenario(mestraFiles.getMestraScenario(), 
					configuration, 
					traceabilityCSCI,
					traceabilityTab.getStatusBar());
			if (bool) {
				// display the 2 frames with the trace-ability results
				TraceabilityResults mestraTraceabilityResults = new TraceabilityResults();
				mestraTraceabilityResults.viewResults(mestraFiles, configuration);				
			}

			// update the status bar
			traceabilityTab.getStatusBar().setMessage("Traceability run finished!");
		
			// changes shape of the Waiting cursor to default
			setCursor(Cursor.getDefaultCursor());
			RunTraceabilityButton.setEnabled(true);
		}
	}

	public ConfigurationFileBaseReader getAConfiguration() {
		return this.aConfiguration;
	}
}
