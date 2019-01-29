package poi;

import gui.StatusBar;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import mestra.MestraMarker;
import mestra.MestraMarkers;

import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.ICell;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFStyle;
import org.apache.poi.xwpf.usermodel.XWPFStyles;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyle;

import configuration.MestraStyle;
import configuration.MestraStylesMap;


public class WordFile {

	private static final Logger logger = Logger.getLogger(WordFile.class.getName()); 

	private String filePath = "";
	private Map<String, String> wordFileStyles = null;

	MestraMarkers mestraMarkers = null;
	private XWPFDocument docx = null; 

	public WordFile() {
		this.filePath = "";
		this.mestraMarkers = new MestraMarkers();
	}

	public WordFile(String filePath) {
		this.filePath = filePath;
		this.mestraMarkers = new MestraMarkers();	
	}

	private void buildWordFileStylesMap(XWPFDocument document) {
		this.wordFileStyles = new TreeMap<String, String>();
		try {

			XWPFStyles xwpfStyles = document.getStyles();
			Iterator<XWPFStyle> xwpfStylesIterator = xwpfStyles.iterator();
			while (xwpfStylesIterator.hasNext()) {
				XWPFStyle xwpfStyle = xwpfStylesIterator.next();

				CTStyle ctStyle = xwpfStyle.getCTStyle();
				if (xwpfStyle.getStyleId() != null) {
					this.wordFileStyles.put(xwpfStyle.getStyleId(), ctStyle.getName().getVal());
				}
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getLocalizedMessage());
		}
	}

	public XWPFDocument open(String filePath) {
		this.filePath = filePath;
		//HWPFDocument doc = null;
		try {
			docx = new XWPFDocument (new FileInputStream (filePath));
			// Robert - 26 September 2016
			docx.setTrackRevisions(false);
			buildWordFileStylesMap(docx);
		}
		catch (FileNotFoundException e1) {
			logger.log(Level.SEVERE , "WordFile: FileInputStream does not exist: " + filePath + " error " + e1.getMessage());
			return null;
		}
		catch (IOException e2) {
			logger.log(Level.SEVERE ,  "WordFile: IOException raised: " + e2.getMessage());

			return null;
		}
		catch (ArrayIndexOutOfBoundsException e3) {
			logger.log(Level.SEVERE , "WordFile: ArrayIndexOutOfBoundsException raised: " + e3.getMessage());
			
		}
		// 16 September 2008
		catch (IllegalArgumentException e4) {
			logger.log(Level.SEVERE , "WordFile: IllegalArgumentException raised: " + e4.getMessage());
					
		}
			
		return docx;
	}
	
	
	private void analyseParagraph(XWPFParagraph xwpfParagraph, MestraStylesMap mestraStyles) {
		
		if (xwpfParagraph.getStyleID() != null) {
			//logger.info( "Style = " + xwpfParagraph.getStyle()+ " style id = " + xwpfParagraph.getStyleID() + " real style = " + this.wordFileStyles.get(xwpfParagraph.getStyleID()) );
			//logger.info( " text " + xwpfParagraph.getText());

			String wordFileStyle = this.wordFileStyles.get(xwpfParagraph.getStyleID());
			if (mestraStyles.hasStyle(wordFileStyle)) {
				// we have found a style that is in the XML configuration file
				MestraMarker mestraMarker = new MestraMarker();
				// set the style
				mestraMarker.setMestraStyle(new MestraStyle(wordFileStyle, mestraStyles.getMestraStyleHeader(wordFileStyle)));
				// set the text
				String strMarker = "";
				for (XWPFRun xwpfRun : xwpfParagraph.getRuns()) {
					if ( (xwpfRun.isDoubleStrikeThrough() == true) || (xwpfRun.isStrikeThrough() == true) ) {
						logger.info( xwpfParagraph.getText() );
					} else {
						if (xwpfRun.getText(0) != null) {
							strMarker += xwpfRun.getText(0);
						}
					}
				}
				if (wordFileStyle.equalsIgnoreCase(mestraStyles.getMainMandatoryStyle()) == true ) {
					// suppress SPACES BLANK if main style
					//logger.log(Level.INFO,"==========Main style found: "+strMarker);
					strMarker = Filter(strMarker,true);
				}
				else {
					strMarker = Filter(strMarker,false);
				}
				// set the content of the Mestra Marker
				if (strMarker.length()>0) {
					mestraMarker.setMestraMarkerValue(strMarker);
					// we did found an expected Mestra Marker
					this.mestraMarkers.add(mestraMarker);
				}
			}
		}
		
	}


	public boolean ExtractMestraMarkers(XWPFDocument document, 
			MestraStylesMap mestraStyles,
			StatusBar statusBar) {

		// clear the existing markers - if any
		this.mestraMarkers.clear();
		try {
			if (statusBar != null) {
				statusBar.setProgressBarMaxValue( document.getParagraphs().size());
			}
			if (mestraStyles != null) {
				int nbParagraph = 1;
				
				for (IBodyElement bodyElement : document.getBodyElements()) {

					if (statusBar != null) {
						statusBar.setProgressBarValue(nbParagraph);
						nbParagraph++;
					}

					if (bodyElement instanceof XWPFParagraph) { 
						XWPFParagraph xwpfParagraph = (XWPFParagraph) bodyElement;
						analyseParagraph(xwpfParagraph, mestraStyles);
						
					}  else if (bodyElement instanceof XWPFTable) {
						XWPFTable xwpfTable = (XWPFTable) bodyElement;
						for (XWPFTableRow xwpfTableRow : xwpfTable.getRows()) {
							for (ICell xwpfTableCell : xwpfTableRow.getTableICells()) {
								if (xwpfTableCell instanceof XWPFTableCell) {
									for (XWPFParagraph xwpfParagraph : ((XWPFTableCell) xwpfTableCell).getParagraphs()) {
										
										analyseParagraph(xwpfParagraph, mestraStyles);

									}
								}
							}
						}
					}
				}
				
				statusBar.setProgressBarValue(0);
				return true;
			}

		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getLocalizedMessage());
		}
		return false;
	}


	public MestraMarkers getMestraMarkers () {
		return this.mestraMarkers;
	}

	private String Filter(String str,boolean SuppressSpaces){

		// added 1st Febuary 2008
		// Robert Pastor
		str = convertDash173toDash45(str);

		int len = str.length();
		if (len > 0){
			while ((str.length() > 0) && ((str.endsWith("\b")) || 
					(str.endsWith("\t")) ||
					(str.endsWith("\n")) ||
					(str.endsWith("\r")) ||
					(str.endsWith("\f")) ||
					((int)str.charAt(len-1)==7 ) ||
					((int)str.charAt(len-1)==8 ) ||
					((int)str.charAt(len-1)==9 ) ||
					((int)str.charAt(len-1)==10 ) ||
					((int)str.charAt(len-1)==11 ) ||
					((int)str.charAt(len-1)==12 ) ||
					((int)str.charAt(len-1)==13 )) ) {
				str = str.substring(0, len-1);
				// need to compute again the length in the while loop
				len = str.length();
			}
			while ((str.length() > 0) && ((str.startsWith("\b")) ||
					(str.startsWith("\t")) ||
					(str.startsWith("\n")) ||
					(str.startsWith("\r")) ||
					(str.startsWith("\f")) ||
					((int)str.charAt(0)==7 ) ||
					((int)str.charAt(0)==8 ) ||
					((int)str.charAt(0)==9 ) ||
					((int)str.charAt(0)==10 ) ||
					((int)str.charAt(0)==11 ) ||
					((int)str.charAt(0)==12 ) ||
					((int)str.charAt(0)==13 )) ) {
				str = str.substring(1);
			}	
			if (SuppressSpaces == true) {
				str = str.trim();
				str = str.toUpperCase();
			}
		}
		return str;
	}

	/**	 
	 * * special Long Dash Characters generate a non genuine error
	 */
	private static String convertDash173toDash45(String source) {

		String char173 = new Character((char)8211).toString();
		StringTokenizer st = new StringTokenizer(source,char173,false);
		String t="";
		while (st.hasMoreElements()) {
			if (t.length() == 0) {
				t = t + st.nextElement();
			}
			else {
				t = t + new Character((char)45).toString() + st.nextElement();
			}
		}
		return t;
	}

	public String getFilePath() {
		return this.filePath;
	}

	public Map<String, String> getWordFileStyles() {
		return this.wordFileStyles;
	}

}
