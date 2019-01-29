package gui;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.dnd.DropTarget;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import mestra.MestraFile;
import mestra.MestraFileType;
import poi.ExcelOutputFile;
import configuration.ConfigurationFileBaseReader;

/**
 * 
 *  This tab of the tabbed pane manages the MESTRA analysis of one file.
 *  <p>
 *  Results of an analysis are : 
 *  <p>1) a frame containing a table view with colored (in case of errors) cells
 *  <p>2) an Excel file
 *  
 *  @author Robert Pastor
 *  @since May 2007
 */
public class MestraChecksTab extends JPanel implements ActionListener {
	
	final static Logger logger = Logger.getLogger(MestraChecksTab.class.getName()); 

    private static final long serialVersionUID = -4168217871588386048L;

    private FileTypeComboBox fileTypeComboBox = null;
    //private JFileChooser fc = null;
    //private JDropLabel jDropLabel = null;
	private TextAreaWithBackGroundImage textFileDropArea = null;

    private JButton reloadMestraFileButton = null;
    private JLabel jExcelOutputPathLabel = null;

    private MainGuiApp mainGuiApp = null;

    public ConfigurationFileBaseReader getConfiguration() {
        return this.mainGuiApp.getConfiguration();
    }

    private void writeDebugMessage (String str) {
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

    private class AnalyseFileThread extends Thread
    {
        private Thread analyseFileThread = null;

        private MestraFile mestraFile = null;
        private ConfigurationFileBaseReader configuration = null;
        private MestraChecksTab mestraChecksTab = null;

        AnalyseFileThread(MestraFile aMestraFile,
                ConfigurationFileBaseReader aConfiguration,
                MestraChecksTab aMestraChecksTab) {	
        	
            this.mestraFile = aMestraFile;
            this.configuration = aConfiguration;
            this.mestraChecksTab = aMestraChecksTab;
            this.analyseFileThread = new Thread(this);
        }	

        public void start() {
            this.analyseFileThread.start();
            writeDebugMessage("Analyse File Thread Start");
        }

        public void run()
        {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            reloadMestraFileButton.setEnabled(false);

            writeDebugMessage("Analyse File thread: run method");
            // extract the Markers from the WORD file
            if (mestraFile.ExtractAllMestraMarkers(configuration, mestraChecksTab.getStatusBar())==true) {

                // write a debug message in the Debug Tab
                writeDebugMessage("Analyse File Thread: Markers correctly extracted from: "+mestraFile.getLongFileName());

                // create the output EXCEL file with the results
                ExcelOutputFile outputFileXLS = new ExcelOutputFile();
                boolean bool = outputFileXLS.WriteResults(mestraFile, configuration);
                
                if (bool == true) {
                	// create a new frame for displaying the results
                    MestraChecksResults mestraChecksResults = new MestraChecksResults();
                    mestraChecksResults.viewResults(mestraFile, configuration);
                }

                // update the status bar
                mestraChecksTab.setStatusBarMessage("Analysis done!");
             }
            else {
                writeDebugMessage("Analyse File Thread: Error while extracting Mestra Markers from: "+mestraFile.getLongFileName());
                // update the status bar
                mestraChecksTab.setStatusBarMessage("Analysis failed!");
            }
            
            setCursor(Cursor.getDefaultCursor());
            reloadMestraFileButton.setEnabled(true);
        }
    }

    public void analyseMestraFile(String fileToAnalyse) {

        MestraFile mestraFile = new MestraFile();
        mestraFile.setFileType(new MestraFileType(fileTypeComboBox.getSelectedFileType()));;
        writeDebugMessage("-----------------------");

        if (mestraFile.setLongFileName(fileToAnalyse) == true) {
            writeDebugMessage("MestraFiles: File to analyse: " + mestraFile.getLongFileName());

            if (this.getConfiguration() != null) {

                // launch the MESTRA tags extraction in a thread
                AnalyseFileThread analyseFileThread = new AnalyseFileThread(mestraFile, this.getConfiguration(), this);
                analyseFileThread.start();

            }
            else {
                String str = "Mestra Checks Tab: Cannot analyse: configuration is null";
                writeDebugMessage(str);
                setStatusBarMessage(str);
            }           
        }
        else {
            String str = "JDropLabel: cannot read input file: "+fileToAnalyse;
            writeDebugMessage(str);
            setStatusBarMessage("Cannot read input file: "+fileToAnalyse);
        }
    }

    private void createMestraChecksTab () {

        //---------------------------------------------------------
        // create the combo box to select the file type
        //=========================================================
        this.fileTypeComboBox = new FileTypeComboBox();

        TitledBorder title;
        title = BorderFactory.createTitledBorder("Step 1: Select the file type");
        this.fileTypeComboBox.setBorder(title);

		//-----------------------------------------------
		// the text area with the "to be analyzed" file name
		//-----------------------------------------------
        title = BorderFactory.createTitledBorder("Step 2: Drop the selected file here - WARNING : please provide only *.docx (Word 2007 format) !!! ");
        
		this.textFileDropArea = new TextAreaWithBackGroundImage(3, 40);
		this.textFileDropArea.setLineWrap(true);
		// set the text area with the configuration file path
		this.textFileDropArea.setText("drop me here");
		//========each time the file type is changed => then the Text Area background image needs to be changed
		this.fileTypeComboBox.notifyChangesTo(this.textFileDropArea);
		

		JScrollPane scroller = new JScrollPane(this.textFileDropArea);

		this.textFileDropArea.setDropTarget(new DropTarget(this.textFileDropArea, new TextAreaDropTarget(this.textFileDropArea, this)));
		this.textFileDropArea.setFont(new Font("Arial",Font.PLAIN,11));

		scroller.setBorder(title);
		scroller.setPreferredSize(new Dimension(800, 100));
        
		//--------------------------------------------------------------
        // use button to reload a file / re analyze a MESTRA file
        // -------------------------------------------------------------
        JPanel jButtonPanel = new JPanel();
        title = BorderFactory.createTitledBorder("Step 3: Push the button to run the analysis");
        jButtonPanel.setBorder(title);

        this.reloadMestraFileButton = new JButton("Launch the MESTRA analysis");
        this.reloadMestraFileButton.addActionListener(this);
        this.reloadMestraFileButton.setBackground(Color.orange);
        this.reloadMestraFileButton.setBorderPainted(true);
        this.reloadMestraFileButton.setFont(new Font("Arial", Font.BOLD, 12));
        this.reloadMestraFileButton.setContentAreaFilled(false);
        this.reloadMestraFileButton.setOpaque(true);
        jButtonPanel.add(reloadMestraFileButton);

        //=================================
        // add the file type combo box to the panel
        //=================================
        this.add(fileTypeComboBox, BorderLayout.NORTH);

        //====================================
        // add the scroller with the file path
        //===================================
		this.add(scroller, BorderLayout.CENTER);
		
        //======================================
        // add the button panel
        //======================================
        this.add(jButtonPanel, BorderLayout.SOUTH);

        // this panel is used to configure the directory where the EXCEL output file will be created
        // -----------------------------------------------------------------------------------------
        JPanel jExcelOutputPathPanel = new JPanel();
        title = BorderFactory.createTitledBorder("Step 5: if needed change the path where EXCEL output files are created");
        jExcelOutputPathPanel.setBorder(title);

        if (mainGuiApp != null) {
            jExcelOutputPathLabel = new JLabel(mainGuiApp.getConfigurationFileDirectory());
        }
        else {
            jExcelOutputPathLabel = new JLabel();
        }
        jExcelOutputPathPanel.add(jExcelOutputPathLabel);

        JButton jExcelOutputPathButton = new JButton("Change output path for created EXCEL file");
        jExcelOutputPathPanel.add(jExcelOutputPathButton);

    }

    // Constructor
    public MestraChecksTab(MainGuiApp mainGuiApp) {
        super(new BorderLayout());
        this.mainGuiApp = mainGuiApp;
        createMestraChecksTab();
    }

    public String getSelectedFileType() {
        return this.fileTypeComboBox.getSelectedFileType();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == reloadMestraFileButton) {
            writeDebugMessage("----------------");
            writeDebugMessage("Reload button Pressed");
            analyseMestraFile(this.textFileDropArea.getText());
        }
    }
}

