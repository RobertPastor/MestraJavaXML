package ToolVersion;


	/**
	 * this class provides the tool version written in the output EXCEL file
	 * it contains also a TODO list
	 * 
	 * list of known problems:
	 * ======================
	 * 
	 * in the trace-ability tab, if the file name - file path is too long 
	 * there is no horizontal scroll bar to see the complete file path in the different tables showing the
	 * the upstream and downstream files
	 * 
	 * in the output EXCEL file, the file path at the top should be an hyper link 
	 * in order to be able to click on it and open directly the file
	 * 
	 * there should be a mean (a button associated to a folder chooser) 
	 * to select the path where EXCEL results files are generated.
	 * This output path could also be configurable in the configuration file
	 * 
	 */ 
public class MestraToolVersion {
	

	private MestraChanges[] MestraToolVersion = { new MestraChanges("initial Version","V0.0.1 dated 26 July 2007") ,
			
			new MestraChanges("Directory where results files (MESTRA and trace-ability) are created\n"+
			" is now obtained from the path of the configuration file",
			"V0.0.2 dated 31 July 2007"),
			
			new MestraChanges("Applet specific bug with Java 1.4 need to call getContentPane.add for each frame",
			"V0.0.3 dated 1 August 2007"),
	
			new MestraChanges("Trace-ability SSS SRS : add Requirement title in EXCEL output",
			"V0.0.4 dated 2 August 2007"),
			
			new MestraChanges("Fix Sorting in the Table View, Errors and Warnings management",
			"V0.0.5 dated 10 September 2007"),
			
			new MestraChanges("Regression in the Splash window and null displayed in the Table View",
			"V0.0.6 dated 11 September 2007"),
			
			new MestraChanges("List of SSS Requirements in SRS Table View can now be sorted",
			"V0.0.7 dated 18 September 2007"),
	
			new MestraChanges("Regression in IdentifierList method and causing failure of trace-ability runs",
			"V0.0.8 dated 20 September 2007"),
			
			new MestraChanges("Changes to MestraTS in order to deal with SRS Requirements links on several lines",
			"V0.0.9 dated 21 September 2007"),
			
			new MestraChanges("Add headers to the EXCEL trace-ability outputs",
			"V0.0.10 dated 01 October 2007"),
			
			new MestraChanges("More checks added in SRS such as Requirement Derived alone",
			"V0.0.11 dated 16 October 2007"),
			
			new MestraChanges("in a TS, if no references to SRS requirements then error in Excel results\n"+
			" in the table view, wrong back ground color when cell is selected\n"+
			" in the trace-ability Excel results, column headers are not in bold style\n"+
			" fixed SRS to SDD trace-ability",
			"V0.0.12 dated 5 November 2007"),
			
			new MestraChanges("added Multiple MESTRA Files tab",
			"V0.0.13 dated 12 November 2007"),
			
			new MestraChanges("Catch WordFile.Open ArrayIndexOutOfBoundsException\n"+
			" fixed header row with CSCI Names in EXCEL output for Multiple MESTRA files analysis",
	        "V0.0.14 dated 23 November 2007"),
	        
	        new MestraChanges("multiple Mestra files in table view",
	        "V0.0.15 dated 04 December 2007"),
	        
	        new MestraChanges("WriteTraceabilityHeader : last column header in XLS out is wrong in case of a traceability",
	        "V0.0.16 dated 11 January 2008"),
	        
	        new MestraChanges("Long Dash Unicode 8211 changed to short Dash (code=45) in Wordfile\n"+
	        " header of traceability results in EXCEL : added file in first column",
	        "V0.0.17 dated 01 February 2008"),
	        
	        new MestraChanges("Added for D. Bornant  Safety True / False columns in SRS to SSS traces",
	        "V0.0.18 dated 21 March 2008"),
	        
	        new MestraChanges("added D. Bornant : at the end of a SRS -> SSS trace-ability table we add the Req_Derived list\n"+
	        " modified Multiple Mestra interface\n"+
	        " generation of a specific DataDrill EXCEL output sheet",
	        "V0.0.19 dated 23 May 2008") ,
	        
	        new MestraChanges("Main Gui App: Mestra Tool Input & Output Root Directory no more necessarily on D:",
	        "V0.0.20 dated 29 May 2008"),
	        
	        new MestraChanges("In all the main GUI tabs, file choosers have been suppressed",
	        "V0.0.21 dated 11 July 2008"), 
	        
	        new MestraChanges("The Traceability CSCI for SSS SRS exercice is memorised and restored each time the tab is changed",
	        "V0.0.22 dated 15 July 2008"), 
	        
	        new MestraChanges("File names with special chars such as [ cannot create a result sheet",
	        "V0.0.23 dated 23 July 2008"),
	        
	        new MestraChanges("EXCEL result file are created where the WORD file is located",
	        "V0.0.24 dated 25 July 2008"),
	        
	        new MestraChanges("Simplified GUI - File Chooser suppressed in all tabs",
	        "V0.0.25 dated 04 August 2008"),
	        
	        new MestraChanges("Catch Illegal Argument Exception in WordFile",
	        "V0.0.26 dated 16 September 2008"),
	        
	        new MestraChanges("added unknown references from SDD to SRS",
	        "V0.0.27 dated 3rd October 2008"),
	        
	        new MestraChanges("Underscore is allowed in Excel Result sheet name",
	        "V0.0.28 dated 10th November 2008"),
	        
	        new MestraChanges("SCN-SRS versus MF Traceability implementation",
	        "V0.0.29 dated 11th March 2009"),
	        
	        new MestraChanges("Suppress tab cr lf in SRS references to SSS requirements",
	        "V0.0.30 dated 6th October 2009"),
	        
	        new MestraChanges("Cannot create more than 255 columns in an EXCEL row",
	        "V0.0.31 dated 19th November 2009"),
	        
	        new MestraChanges("Mestra Configuration is first XLS file found in C:/Mestra Tool or D:/Mestra Tool with a file name having \"Mestra_Tool_V2\" in the file name",
	        "V0.0.32 dated 27th November 2009"),

	        new MestraChanges("POI List Tables : getLevel add a check on lst null pointer",
	        "V0.0.33 dated 15th December 2010"),
	        
	        new MestraChanges("POI StyleSheet : erroneous check on index array cause out of bounds: ParagraphProperties getParagraphStyle",
	        "V0.0.34 dated 26th January 2011"),
	        
	        new MestraChanges("in SRS SDD traceability add Safety tag of SRS Requirements",
	        "V0.0.35 dated 6th April 2012"),
	        
	        new MestraChanges("class CSCI_SSSFileAllocationList SortedMap implement as a TreeMap",
	        "V0.0.36 dated 8th June 2016"),

	        new MestraChanges("new XML configuration file - manage a set of extended mestra TAGs",
	        "V1.0.01 dated 15th July 2016"),
	        
	        new MestraChanges("correct size of status bar message area",
	        "V1.0.02 dated 2nd August 2016"),
	        
	        new MestraChanges("Mestra EXCEL output with Errors and Warnings",
	        "V1.0.03 dated 3rd August 2016"),
	        
	        new MestraChanges("Read only docx - 2007 XML format - files up to now",
	        "V1.1.0 dated 4th August 2016"),
	        
	        new MestraChanges("Traceability features up and running again",
	        "V1.1.1 dated 5th September 2016"),
	        
	        new MestraChanges("Memory leaks",
	        "V1.1.2 dated 5th September 2016"),
	        
	        new MestraChanges("Mestra Style value - now adding value to existing ones",
	        "V1.1.3 dated 11th January 2017"),

	        new MestraChanges("Reload the XML configuration file",
	        "V1.1.4 dated 31st January 2017"),
	        
	        new MestraChanges("Traceability panels with background image",
	        "V1.1.5 dated 7th February 2017"),
	        
	        new MestraChanges("Extract Mestra Markers from a Docx Table cell.",
	        "V1.2.0 dated 9th May 2017"),
	        
	        new MestraChanges("bug if traceability tag found in SRS or SSS before any other.",
	        "V1.2.1 dated 2nd June 2017"),
	        
	        new MestraChanges("Title content is duplicated.",
	    	"V1.2.2 dated 8th January 2019"),
	        
	        };
	
	
	public String getMestraToolVersion() {
		return MestraToolVersion[this.MestraToolVersion.length-1].getVersion();
	}
	
	public String getMestraToolRationale() {
		return MestraToolVersion[this.MestraToolVersion.length-1].getRationale();
	}
}
