package mestra;

import gui.StatusBar;

import java.io.IOException;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import mestra.MestraFileType.MestraFileTypeEnum;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

import poi.WordFile;
import configuration.ConfigurationFileBaseReader;

public class MestraFile extends WordFile {

	final static Logger logger = Logger.getLogger(MestraFile.class.getName()); 

	private String Folder = "";
	private String ShortFileName = "";
	private String ShortFileNameWithoutExtension = "";
	private String LongFileName = "";
	private String FileExtension = "";
	private boolean WordExtractionFailed = false;

	// SSS SRS SDD MF TS
	private MestraFileType FileType = null;

	// depending upon the file type: only one of the following collection is used
	private MestraSSS_Collection mestraSSStags = null;
	private MestraSRS_Collection mestraSRStags = null;
	private MestraSDD_Collection mestraSDDtags = null;
	private MestraMF_Collection  mestraMFtags = null;
	private MestraTS_Collection  mestraTStags = null;

	private CSCI_SSSFileAllocationList SSS_FileAllocationList = null;

	public MestraFile() {
		super();
		Folder = "";
		ShortFileName = "";
		ShortFileNameWithoutExtension = "";
		LongFileName = "";
		FileExtension = "";
		WordExtractionFailed = false;

		// default value
		FileType = new MestraFileType(MestraFileTypeEnum.SSS);
		SSS_FileAllocationList = new CSCI_SSSFileAllocationList();
	}

	public MestraFile (String strLongFileName) {
		super(strLongFileName);
		Folder = "";
		ShortFileName = "";
		ShortFileNameWithoutExtension = "";
		FileExtension = "";
		WordExtractionFailed = false;

		// default value
		FileType = new MestraFileType(MestraFileTypeEnum.SSS);
		SSS_FileAllocationList = new CSCI_SSSFileAllocationList();
		setLongFileName(strLongFileName);
	}

	public String getFileExtension() {
		return FileExtension;
	}

	public void setFileExtension(String fileExtension) {
		this.FileExtension = fileExtension;
		if	((FileExtension.equalsIgnoreCase("doc")) || ((FileExtension.equalsIgnoreCase("docx")))) {
			logger.info( "MestraFile: setFileExtension: extension is : " + fileExtension );
			FileExtension = fileExtension;
		}
		/*if (ShortFileName.endsWith("doc") == false) {
			logger.info( "MestraFile: setFileExtension: extension is : "+fileExtension );
			ShortFileName = ShortFileName + "." + FileExtension;
		}*/
	}
	public MestraFileType getFileType() {
		return this.FileType;
	}
	public void setFileType(MestraFileType fileType) {
		this.FileType = fileType;
	}

	public void setFileType (MestraFileTypeEnum fileTypeEnum) {
		this.FileType.setMestraFileType(fileTypeEnum);
	}

	public boolean Is(MestraFileTypeEnum mestraFileType) {
		return (this.FileType.Is(mestraFileType)) ;
	}

	public boolean IsSSS() {
		return (this.FileType.isSSS());
	}

	public boolean IsSRS() {
		return (this.FileType.isSRS());
	}

	public boolean IsSDD() {
		return (this.FileType.isSDD());
	}

	public boolean IsMF() {
		return (this.FileType.isMF());
	}

	public boolean IsTS() {
		return (this.FileType.isTS());
	}

	public String getShortFileName() {
		return this.ShortFileName;
	}

	public void setShortFileName(String shortFileName) {
		// in this case we cannot deduce the folder
		this.ShortFileName = shortFileName;
		if (ShortFileName.endsWith(".doc")) {
			setFileExtension("doc");
			int len = ShortFileName.length();
			ShortFileNameWithoutExtension = ShortFileName.substring(0, len-4);
		}
		else {
			ShortFileNameWithoutExtension = ShortFileName;
			if (FileExtension.length()==0) {
				FileExtension = "doc";
			}
			else {
				if (FileExtension.equalsIgnoreCase("doc")) {
					ShortFileName = ShortFileName + "." + FileExtension;
				}
			}
		}
		assert (Folder.length()>0);
		LongFileName = Folder + ShortFileName;
	}

	/**
	 * @return the strFileType
	 */
	public String getStrFileType() {
		return FileType.getStrMestraFileType();
	}

	/**
	 * @param strFileType the strFileType to set
	 */
	public void setStrFileType(String strFileType) {
		assert (strFileType.length()>0);
		FileType.setStrMestraFileType(strFileType);
	}
	/**
	 * @return the longFileName
	 */
	public String getLongFileName() {
		return LongFileName;
	}

	private String extractFolder(String longFileName) {
		StringTokenizer strTok = new StringTokenizer(longFileName,"\\");
		String folder = "";
		int j = strTok.countTokens();
		for (int i=1 ; i<(j) ; i++) {
			if (i==1) {
				folder = strTok.nextToken();
			}
			else {
				folder += "/"+ strTok.nextToken();
			}
		}
		return folder;
	}

	private String extractShortFileName(String longFileName) {
		StringTokenizer strTok = new StringTokenizer(longFileName,"\\");
		String shortFileName = "";
		int j = strTok.countTokens();
		for (int i=1 ; i <= j ; i++) {
			shortFileName = strTok.nextToken();
		}
		return shortFileName;
	}
	/**
	 * @param longFileName the longFileName to set
	 */
	public boolean setLongFileName(String longFileName) {
		this.LongFileName = longFileName;
		if (this.LongFileName.endsWith(".doc")) {
			setFileExtension("doc");
		} else {
			if (this.LongFileName.endsWith(".docx")) {
				setFileExtension("docx");
			} else {
				return false;
			}
		}

		assert( LongFileName.endsWith(".doc") || LongFileName.endsWith(".docx") );
		this.Folder = extractFolder(longFileName);
		this.ShortFileName = extractShortFileName(longFileName);

		if (this.ShortFileName.endsWith(".doc") || this.ShortFileName.endsWith(".docx")) {
			int len = ShortFileName.length();
			ShortFileNameWithoutExtension = ShortFileName.substring(0, len-4);
		}
		return true;

	}
	/**
	 * @return the folder
	 */
	public String getFolder() {
		return this.Folder;
	}
	/**
	 * @param folder the folder to set
	 */
	public void setFolder(String folder) {
		Folder = folder;
		if (Folder.endsWith("\\")== false) {
			Folder = Folder + "\\";
		}
		if (ShortFileName.length()>0) {
			LongFileName = Folder + ShortFileName;
		}
	}

	private void initSSS_FileAllocationList(ConfigurationFileBaseReader configuration) {

		this.SSS_FileAllocationList.clear();
		Iterator<MestraSSS> Iter = mestraSSStags.iterator();
		while (Iter.hasNext()) {
			MestraSSS mestraSSS = Iter.next();
			this.SSS_FileAllocationList.add(configuration, mestraSSS.getRequirementAllocationList());
		}
	}

	private void ConvertMestraMarkers(ConfigurationFileBaseReader configuration) {

		switch (FileType.getMestraFileType()) {

		case SSS:
			logger.info( "MestraFile : converting markers of a SSS");
			mestraSSStags = new MestraSSS_Collection();
			mestraSSStags.init(configuration, this);
			initSSS_FileAllocationList(configuration);
			break;

		case SRS:
			logger.info( "MestraFile : converting markers of a SRS");
			mestraSRStags = new MestraSRS_Collection();
			mestraSRStags.init(configuration, this);
			break;

		case SDD:
			logger.info( "MestraFile : converting markers of a SDD");
			mestraSDDtags = new MestraSDD_Collection();
			mestraSDDtags.init(configuration,this);
			break;

		case MF:
			logger.info( "MestraFile : converting markers of a MF");

			mestraMFtags = new MestraMF_Collection();
			mestraMFtags.init(configuration, this);
			break;

		case TS:
			logger.info( "MestraFile : converting markers of a TS");

			mestraTStags = new MestraTS_Collection();
			mestraTStags.init(configuration, this);
			break;
		}
	}


	public boolean ExtractAllMestraMarkers(
			final ConfigurationFileBaseReader configuration,
			StatusBar statusBar) {

		// open the word file - docx since 4th August 2016
		// call open method of base class Word file
		XWPFDocument  docx = null;
		try {
			docx = this.open(this.LongFileName);
			assert (configuration != null);
			if ( docx != null ) {
				// update the GUI status bar
				statusBar.setMessage("Extracting Mestra Markers from: " + this.ShortFileName+"...");

				// this collection contains the rough MESTRA markers extracted from the file
				// call a method of the base class - word file
				if (this.ExtractMestraMarkers(docx, configuration.getMestraStyles(FileType), statusBar) == true) {
					logger.info( "MestraFile : ExtractAllMestraMarkers: Markers correctly extracted!");

					ConvertMestraMarkers(configuration);
					logger.info( "MestraFile : ExtractAllMestraMarkers: Markers converted!");

					WordExtractionFailed = false;
					docx.close();
					return true;
				}
				else {
					logger.log(Level.SEVERE, "MestraFile : ExtractAllMestraMarkers: Problem while extracting Markers");
					WordExtractionFailed = true;
					docx.close();
					return false;
				}
			}
			return false;
		}

		catch (Exception e1) {
			
			JOptionPane.showMessageDialog(statusBar.getParent(), "Please ensure that you did provide a word 2007 docx file !!! ");
			// updating the GUI Status Bar
			statusBar.setMessage("Fail to open Word File: " + this.ShortFileName + " localized message = " + e1.getLocalizedMessage());
			WordExtractionFailed = true;
			try {
				docx.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return false;
		}
	}

	public String getShortFileNameWithoutExtension() {
		return this.ShortFileNameWithoutExtension;
	}

	public void setShortFileNameWithoutExtension(
			String _shortFileNameWithoutExtension) {
		this.ShortFileNameWithoutExtension = _shortFileNameWithoutExtension;
	}

	/**
	 * @return the mestraMFtags
	 */
	public MestraMF_Collection getMestraMFtags() {
		return this.mestraMFtags;
	}
	/**
	 * @param mestraMFtags the mestraMFtags to set
	 */
	public void setMestraMFtags(MestraMF_Collection mestraMFtags) {
		this.mestraMFtags = mestraMFtags;
	}
	/**
	 * @return the mestraSRStags
	 */
	public MestraSRS_Collection getMestraSRStags() {
		return this.mestraSRStags;
	}
	/**
	 * @param mestraSRStags the mestraSRStags to set
	 */
	public void setMestraSRStags(MestraSRS_Collection mestraSRStags) {
		this.mestraSRStags = mestraSRStags;
	}
	/**
	 * @return the mestraSSStags
	 */
	public MestraSSS_Collection getMestraSSStags() {
		return this.mestraSSStags;
	}
	/**
	 * @param mestraSSStags the mestraSSStags to set
	 */
	public void setMestraSSStags(MestraSSS_Collection mestraSSStags) {
		this.mestraSSStags = mestraSSStags;
	}
	/**
	 * @return the mestraTStags
	 */
	public MestraTS_Collection getMestraTStags() {
		return this.mestraTStags;
	}
	/**
	 * @param mestraTStags the mestraTStags to set
	 */
	public void setMestraTStags(MestraTS_Collection mestraTStags) {
		this.mestraTStags = mestraTStags;
	}
	/**
	 * @return the SSS_FileAllocationList
	 */
	public CSCI_SSSFileAllocationList getSSS_FileAllocationList() {
		return this.SSS_FileAllocationList;
	}
	/**
	 * @param fileAllocationList the SSS_FileAllocationList to set
	 */
	public void setSSS_FileAllocationList(CSCI_SSSFileAllocationList fileAllocationList) {
		this.SSS_FileAllocationList = fileAllocationList;
	}

	/**
	 * @return the mestraSDDtags
	 */
	public MestraSDD_Collection getMestraSDDtags() {
		return this.mestraSDDtags;
	}

	/**
	 * @param mestraSDDtags the mestraSDDtags to set
	 */
	public void setMestraSDDtags(MestraSDD_Collection mestraSDDtags) {
		this.mestraSDDtags = mestraSDDtags;
	}

	/**
	 * @return the wordExtractionFailed
	 */
	public boolean isWordExtractionFailed() {
		return this.WordExtractionFailed;
	}


}
