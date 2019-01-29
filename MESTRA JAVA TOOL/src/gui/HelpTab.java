package gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.TitledBorder;


/**
 * this class will display a Panel with the help.
 * @author t0007330
 *
 */
public class HelpTab extends JPanel {
	
	private String fillTraceabilityHelp()  {
		
		String text = "<html>";
		text += "<head>";
		text += "<title>title</title>";
		text += "<style type=\"text/css\">";
		text += "<!--";
		text += "h1{color:red; text-align:center; font-family:Arial, sans-serif;}";
		text += "h2{color:red; text-align:center; font-family:Arial, sans-serif;}";
		text += "p{color:black; text-align:left; font-size:14pt; letter-spacing;1px; font-family:arial, sans-serif; margin-left:10;  margin-right:10;}";
		text += "normal{color:black; font-family:Arial, sans-serif; font-size:14pt;}";
		text += "-->";
		text += "</style>";
		text += "</head>";
		
		text += "<body>";
		text += "<h1>==============================</h1>";
		text += "<h1>Perform a Traceability Analysis</h1>";
		text += "<h1>==============================</h1>";
		text += "<p>This tool allows to analyse a set of upstream and downstream related MESTRA tags contained in Word docx format(starting in Office 2007) files.</p>";
		text += "<p>The result of an analysis is composed of :</p>";
		text += "<ul>";
		text += "<li> two frames with the links between the upstream and the downstream MESTRA tags found in the documents.</li>";
		text += "<li> an EXCEL file containing two sheets with the links between the upstream and the downstream MESTRA Tags found in the documents.</li>";
		text += "</ul>";
		text += "<p><b>Note</b>: it is recommended to checks each Mestra file before starting a traceability analysis.</p>";
		text += "<br>";
		text += "<p>SSS : System Software Specifications</p>";
		text += "<p>SRS : Software Requirements Specifications</p>";
		text += "<p>SDD : Software Design Document</p>";
		text += "<p>MF : Modification Form</p>";
		text += "<p>TS : Test Sheet</p>";
		text += "<br>";
		text += "In order to perform a traceability analysis, the user needs to provide two sets of files:";
		text += "<ul>";
		text += "<li>a set of upstream files</li>";
		text += "<li>a set of downstream files</li>";
		text += "</ul>";

		text += "<h1>================================</h1>";
		text += "<h1>SSS versus SRS </h1>";
		text += "<h1>================================</h1>";
		text += "<p>If the type of the traceability run is: SSS versus SRS then two frames are generated : </p>";
		text += "<p>1) a frame named SSS_SRS_XXX where XXX is the name of the selected CSCI.</p>";
		text += "<p>2) a frame named SRS_SSS_XXX where XXX is the name of the selected CSCI.</pS>";
		text += "<br>";
		text += "<p><u><b>The SSS_SRS_XXX frame shows : </b></u></p>";
		text += "<p>1) the name of the SSS (or SMF) upstream analysed file.</p>";
		text += "<p>2) the name of the SSS requirement from the upstream analysed file.</p>";
		text += "<p>3) The list of the CSCI(s) to which the SSS Requirement is allocated.</p>";
		text += "<p>4) The name of the SRS requirement containing a reference to the SSS requirement.</p>";
		
		text += "<p><b>Note</b>: If one SSS Requirement (allocated to the traceability CSCI) is covered by several SRS Requirements (from the downstreams documents), then the frame SSS_SRS will show one row (record) for each pair SSS Requirement - SRS Requirement.</p>";
		text += "<p><b>Note</b>: If one SSS Requirement (allocated to the traceability CSCI) is not covered by any SRS Requirement (from the downstreams documents), then the frame SSS_SRS will show one RED empty cell - SRS covering Requirement - meaning NOT COVERED.</p>";
		
		text += "<p><u><b>The SRS_SSS_XXX frame shows : </b></u></p>";
		text += "<p>1) the name of the SRS (or SCN-SRS) downstream analysed file.</p>";
		text += "<p>2) the name of the SRS (or SCN-SRS) downstream requirement.</p>";
		text += "<p>2) the name of the SSS (or SMF) upstream requirement covered by this SRS Requirement.</p>";

		text += "<p><b>Note</b>: If one SRS Requirement is covering several SSS Requirements (from the upstream documents), then the frame SRS_SSS will show one row (record) for each pair SRS Requirement - covered SSS Requirement.</p>";

		text += "<p><b>Note</b>: If one SRS Requirement is covering a SSS Requirement that is not existing in the upstreams SSS document(s), then the frame SRS_SSS will show one YELLOW cell- in the covered SSS Requirement column - with the NOT EXISTING SSS Requirement.</p>";
		
		text += "<h1>==============================</h1>";
		text += "<h1>SRS versus Test Sheets</h1>";
		text += "<h1>==============================</h1>";
		text += "When the SRS versus TS kind of traceability is selected then the selection of the traceability CSCI is disabled.<br>";
		text += "<b>the SRS_TS frame shows: </b><br>";
		text += "<ul>";
		text += "<li>the name of the SRS upstream analysed file.</li>";
		text += "<li>the name of the SRS upstream Requirement.</li>";
		text += "<li>the name of one of the Test Sheet covering the SRS upstream Requirement.</li>";
		text += "</ul>";
		text += "<b>the TS_SRS frame shows: </b><br> ";
		text += "<ul>";
		text += "<li>the name of the TS downstream analysed file</li>";
		text += "<li>the name of the TS identifier</li>";
		text += "<li>the name of one of the SRS Requirement covered by the Test Sheet</li>";
		text += "</ul>";

		text += "<h1>==============================</h1>";
		text += "<h1>SRS versus SDD</h1>";
		text += "<h1>==============================</h1>";
		text += "When the SRS versus SDD kind of traceability is selected then the selection of the traceability CSCI is disabled.";

		text += "<p>If the type of the traceability run is: SRS versus SDD then two frames are generated : </p>";

		
		text += "<h1>==============================</h1>";
		text += "<h1>SRS versus Modification Form</h1>";
		text += "<h1>==============================</h1>";
		text += "When the SRS versus MF kind of traceability is selected then the selection of the traceability CSCI is disabled.";

		
		text += "</body>";
		text += "</html>";
		
		return text;
		
	}
	
	private String fillXmlConfigurationHelp() {
		
		String text = "<html>";
		text += "<head>";
		text += "<TITLE>title</TITLE>";
		text += "<STYLE TYPE=\"text/css\">";
		text += "<!--";
		text += "H1{color:#FF6342; text-align:center; font-family:Tahoma, sans-serif;}";
		text += "H2{color:#FF9473; font-family:Tahoma, sans-serif;}";
		text += "P{color:navy; font-size:12pt;  font-weight:600; letter-spacing;1px;  font-family:arial, sans-serif; margin-left:10; margin-right:10; }";
		text += "normal{font-family:Arial, sans-serif; font-size:12pt;}";

		text += "-->";
		text += "</STYLE>";
		text += "</HEAD>";
		
		text += "<body>";
		
		text += "<h1>==============================</h1>";
		text += "<h1>Configure the analysis</h1>";
		text += "<h1>==============================</h1>";
		text += "This help explains how to use the configuration file.";
		text += "The configuration file allows to:";
		text += "<ul>";

		text += "<li> define the MESTRA tags and the related WORD styles.</li> ";
		text += "<br>";
		text += "<li> define the method IADT , the level of the analysis and the SAFETY tag.</li>";
		text += "<br>";
		text += "<li> define the names of the CSCIs that will be used for the SSS versus SRS traceability verifications.</li>";
		text += "</ul>";
		text += "<b>Note</b>:The XML Configuration file needs to start with the STRING pattern : <b><u>Mestra_Tool_V3</u></b><br>";
		text += "<br>";
		text += "<b>Note</b>: It is possible to open the XML configuration file, update it and click on the load button to reload the new configuration";
		

		text += "<h1>==============================</h1>";
		text += "<h1>Define the MESTRA tags and the related WORD styles</h1>";
		text += "<h1>==============================</h1>";
		text += "For each document, SSS, SRS, SDD, MF and TS, the user may define : <br>";
		text += "<ul>";
		text += "<li> a set of MESTRA tags (logical names).</li>";
		text += "<li> a set of corresponding WORD styles (example : M:T:PUID:reqt).</li>";
		text += "</ul>";
		
		text += "<b>Note:</b> The first MESTRA tag is the identifier with specific checks attached to it such as no duplicates, no SLASH character, etc. <br>";
		text += "<b>Note:</b> Each MESTRA tag may be displayed in the frame and in the EXCEL output (display='true') or hidden. <br>";

		text += "<h1>==============================</h1>";
		text += "<h1>Define the method, the level and the SAFETY tags</h1>";
		text += "<h1>==============================</h1>";
		text += "In order to check Level Method SAFETY tags, the XML configuration file shall define a tag with an attribute methodLevelSafety='true'.<br>";


		text += "<h1>==============================</h1>";
		text += "<h1>Define the traceability CSCIs</h1>";
		text += "<h1>==============================</h1>";
		text += "The traceability CSCIs are CSCIs name that are used during a SSS versus SRS traceability.<br>";
		text += "The traceability CSCI defines the <b>subset</b> of SSS Requirements that are allocated to the CSCI and that will be considered in the traceability as those to be covered by the SRS Requirements.";
		
		text += "<b>Note</b>: Traceability CSCI are names without any consideration of upper or lower case.";
		text += "<b>Note</b>: SSS or SMF Requirements are allocated to a set of CSCI separated by a SLASH.";
		text += "<b>Note</b>: If the configuration file contains several times the same CSCI, the duplicated CSCI names will appear in the Tree View but these duplicates will not impact the analysis.";

		text += "</body>";
		text += "</html>";
		
		return text;
		
	}


	private String fillMestraChecksHelp() {
		
		String text = "<html>";
		text += "<head>";
		text += "<TITLE>title</TITLE>";
		text += "<STYLE TYPE=\"text/css\">";
		text += "<!--";
		text += "H1{color:#FF6342; text-align:center; font-family:Tahoma, sans-serif;}";
		text += "H2{color:#FF9473; font-family:Tahoma, sans-serif;}";
		text += "P{color:navy; font-size:10pt;   font-weight:600; letter-spacing;1px;   font-family:arial, sans-serif; margin-left:10; margin-right:100; }";
		text += "normal{font-family:Arial, sans-serif; font-size:10pt;}";

		text += "-->";
		text += "</STYLE>";
		text += "</HEAD>";
		
		text += "<body>";
		
		text += "<h1>==============================</h1>";
		text += "<h1>Analyse Word (docx) files containing MESTRA Tags</h1>";
		text += "<h1>==============================</h1>";
		text += "This tool allows to extract and analyse MESTRA tags contained in WORD docx format(starting from Office 2007) files.";
		text += "<br>";
		text += "The result of an analysis is composed of :<br>";
		text += "1) a frame with a table view containing the MESTRA Tags found in the document<br>";
		text += "2) an EXCEL file containing the MESTRA Tags found in the document<br>";
				
		text += "<br>";
		text += "In the table view frame, the user may click on a header to sort the content of the table, according to the content of the sorted column.<br>";
		text += "<br>";
		text += "In order to launch an analysis, the user needs :";
		text += "<ul>";
		text += "<li>first to select the type of the file, hence the type of MESTRA styles that expected to be found in the file</li>";
		text += "<li>second to open a folder where the file to analyse is located, to click on the file, maintain the mouse button down, drag and drop it in the file path area</li>";
		text += "</ul>";

		text += "<h2>==============================</h2>";
		text += "<h2>Requirement Identifiers</h2>";
		text += "<h2>==============================</h2>";
		text += "If several requirements are identical (after suppression of leading and trailing spaces) these requirements are highlighted in RED.<br>";
		text += "If a requirement contains a SLASH , it is highlighted in YELLOW.<br>";
		text += "If a requirement starts with a DASH or ends with a DASH , it is highlighted in YELLOW.<br>";
		text += "<br>";

		text += "<h2>==============================</h2>";
		text += "<h2>Requirement Allocation</h2>";
		text += "<h2>==============================</h2>";
		
		text += "Note: the requirement allocation tag is specific to the MESTRA SSS Requirement.<br>";
		text += "If a SSS requirement is allocated to a CSCI that is not defined in the XML configuration, the Allocation tag is highlighted in YELLOW.<br>";
		text += "If a SSS requirement is not allocated to any CSCI, the 'empty' Allocation tag is highlighted in YELLOW.<br>";

		text += "<br>";
		text += "If a SRS requirement is marked as 'Req_derived' , and it is also covering another SSS Requirement, the Allocation tag is highlighted in YELLOW.<br>";

		text += "<h2>==============================</h2>";
		text += "<h2>Method Level Safety</h2>";
		text += "<h2>==============================</h2>";
		text += "The standard Methods defined in the XML configuration file are: I for Inspection, A for Analysis, D for Demonstration, T for tests.<br>";
		text += "If, for a requirement, the provided Method is not defined in the XML configuration file, then the <b>Method Level Safety</b> cell is highlighted in YELLOW.<br>";
		text += "<br>";

		text += "The standard Levels defined in the XML configuration file are : System , Integration, etc.<br>";
		text += "If the provided Level is not defined in the XML configuration file, then the Method Level Safety cell is highlighted in YELLOW.<br>";
		text += "<br>";
		text += "If the SAFETY tag is not spelled correctly, then the Method Level Safety cell is highlighted in YELLOW.<br>";


		text += "</body>";
		text += "</html>";
		
		return text;
	}

	public HelpTab(String kindOfHelp) {

		super(new GridBagLayout());
		//this.setLayout(new GridLayout(2,1));

		// the Panel containing the Text Area
		// -----------------------------------
		JTextPane jTextPane = new JTextPane();
		jTextPane.setContentType( "text/html" );
		jTextPane.setEditable( false );
		
		if (kindOfHelp.toLowerCase().contains("traceability")) {
			jTextPane.setText(fillTraceabilityHelp());
		} else {
			if (kindOfHelp.toLowerCase().contains("configuration")) {
				jTextPane.setText(fillXmlConfigurationHelp());
			} else {
				jTextPane.setText(fillMestraChecksHelp());
			}
		}
		// set the scroll lift to the top
		jTextPane.setCaretPosition(0);
		
		TitledBorder title;
		title = BorderFactory.createTitledBorder("Help Tab: the following text area contains the Help...");
		
		JScrollPane scroller = new JScrollPane(jTextPane);
		scroller.setBorder(title);
		scroller.setPreferredSize(new Dimension(800, 300));

		GridBagConstraints c = new GridBagConstraints();   
		c.fill = GridBagConstraints.BOTH;
		c.gridx=0;
		c.gridy=0;
		c.weightx=1;
		c.weighty=5;

		this.add(scroller, c);
		
	}

}
