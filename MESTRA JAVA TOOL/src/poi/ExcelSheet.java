package poi;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import mestra.CSCI_Allocation;
import mestra.MestraFile;
import mestra.MestraFiles;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import poi.CellStyles.CellStylesEnum;
import ToolVersion.MestraToolVersion;

/**
 * This is the base class used to create / and fill in
 * an EXCEL output Sheet
 * either for the MESTRA Results, for the Trace-ability Results 
 * or for the Errors and Warning Result sheet
 *  
 * @author Pastor
 * @since July 2007
 *
 */
public class ExcelSheet {

	final static Logger logger = Logger.getLogger(ExcelSheet.class.getName()); 

	String SheetName = "";
	HSSFSheet sheet = null;
	protected int rowIndex = 0;
	protected CellStyles cellStyles = null;


	/**
	 * creates an EXCEL Sheet with the provided sheet name
	 * @param wb the workbook to contain the created sheet
	 * @param sheetName the name of the to be created sheet
	 */
	public ExcelSheet(HSSFWorkbook wb, String _sheetName) {
		// create a sheet with the provided name 
		// in the provided workbook
		// limit to the 31 first characters
		if (_sheetName.length() > 31) {
			this.SheetName = _sheetName.substring(0, 31);
		} else {
			this.SheetName = _sheetName;
		}
		sheet = wb.createSheet(SuppressForbiddenChar(this.SheetName));
		rowIndex = 0 ;
		cellStyles = new CellStyles(wb);
	}

	/**
	 * Write a String in a Cell with a given style
	 * @param text the String text to fill in the cell
	 * @param cell : the cell to contain the string text
	 * @param style : the style to use for the formating of the cell
	 */
	private void Write(String text, HSSFCell cell, HSSFCellStyle style) {

		HSSFRichTextString strRT = new HSSFRichTextString(text);
		cell.setCellValue(strRT);
		cell.setCellStyle(style);

	}

	public void WriteToolVersion () {

		// row are 0 indexed
		HSSFRow row = sheet.createRow((short)rowIndex++);
		HSSFCell cell = row.createCell(0);
		Write("Java Mestra Tool",cell,cellStyles.getCellStyle(CellStylesEnum.ArialEightCellStyle));
//		HSSFRichTextString strRT = new HSSFRichTextString("Java Mestra Tool");
//		cell.setCellValue(strRT);
//		cell.setCellStyle(ArialEightCellStyle);

		MestraToolVersion mestraToolVersion = new MestraToolVersion();
		cell = row.createCell(1);
		HSSFRichTextString strRT = new HSSFRichTextString(mestraToolVersion.getMestraToolVersion());
		cell.setCellValue(strRT);
		cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightCellStyle));

		row = sheet.createRow((short)rowIndex++);
		cell = row.createCell(0);
		strRT = new HSSFRichTextString("Files Analysed");
		cell.setCellValue(strRT);
		cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightCellStyle));

		Date now = new Date();
		String strDate = DateFormat.getDateInstance().format(now);
		cell = row.createCell(1);
		strRT = new HSSFRichTextString(strDate);
		cell.setCellValue(strRT);
		cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightCellStyle));

		String strTime = DateFormat.getTimeInstance().format(now);
		cell = row.createCell(2);
		strRT = new HSSFRichTextString(strTime);
		cell.setCellValue(strRT);
		cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightCellStyle));

	}

	/**
	 *  Writes in the EXCEL output sheet
	 *  the list of file names which have been analyzed
	 * @param mestraFiles
	 */
	public void WriteFilesNames(MestraFiles mestraFiles) {

		ArrayList<MestraFile> mestraFilesArray = mestraFiles.getMestraFiles();
		Iterator<MestraFile> Iter = mestraFilesArray.iterator();
		while (Iter.hasNext()) {
			MestraFile mestraFile = Iter.next();
			WriteFileName(mestraFile);
		}
	}

	public void WriteFileName(MestraFile mestraFile) {

		// Create a row and put some cells in it. 
		// Rows are 0 based.
		HSSFRow row = sheet.createRow((short)rowIndex++);

		// Create a cell and put a value in it.
		int cellIndex = 0;
		HSSFCell cell = row.createCell(cellIndex++);

		HSSFRichTextString strRT = new HSSFRichTextString(mestraFile.getStrFileType());
		cell.setCellValue(strRT);
		cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightCellStyle));

		cell = row.createCell(cellIndex++);
		String FileName = "file:\\" + mestraFile.getLongFileName();
		strRT = new HSSFRichTextString(FileName);
		cell.setCellValue(strRT);
		cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightCellStyle));

		if (mestraFile.isWordExtractionFailed()) {
			cell = row.createCell(cellIndex++);
			String ErrorMessage = "Word Extraction Failed!";
			strRT = new HSSFRichTextString(ErrorMessage);
			cell.setCellValue(strRT);
			cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightRedBackGroundCellStyle));
		}

		//cell = row.createCell((short)cellIndex++);
		//String formulae = "HYPERLINK(file://"+ mestraFile.getLongFileName()+")";
		//cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
		//cell.setCellFormula(formulae);

		// if the file is a SSS we add a row listing all the CSCI impacted by the SSS Requirements
		if	(mestraFile.IsSSS()) {

			row = sheet.createRow((short)rowIndex++);
			cellIndex = 0;
			cell = row.createCell(cellIndex++);
			strRT = new HSSFRichTextString("Impacted CSCI List");
			cell.setCellValue(strRT);
			cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightCellStyle));

			if (mestraFile.getSSS_FileAllocationList() != null) {
				
				Iterator<Map.Entry<String, CSCI_Allocation>> Iter = mestraFile.getSSS_FileAllocationList().iterator();
				while (Iter.hasNext()) {
					try {
						cell = row.createCell(cellIndex++);
						
						Map.Entry<String, CSCI_Allocation> entry = Iter.next();
						CSCI_Allocation aCSCI_Allocation = entry.getValue();

						strRT = new HSSFRichTextString(aCSCI_Allocation.getCSCI_Name());
						cell.setCellValue(strRT);
						// check if the CSCI exists in the configuration file
						if (aCSCI_Allocation.isCSCI_NameExisting() == false) {
							cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightYellowBackGroundCellStyle));
						}
						else {
							cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightCellStyle));
						}
					}
					catch (RuntimeException e) {
						logger.log(Level.SEVERE , e.getLocalizedMessage());
					}
					
				}	
			}

			row = sheet.createRow((short)rowIndex++);
			cellIndex = 0;
			cell = row.createCell(cellIndex++);
			strRT = new HSSFRichTextString("Nbr ref");
			cell.setCellValue(strRT);
			cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightCellStyle));

			if (mestraFile.getSSS_FileAllocationList() != null) {
				
				Iterator<Map.Entry<String, CSCI_Allocation>> Iter = mestraFile.getSSS_FileAllocationList().iterator();
				while (Iter.hasNext()) {

					Map.Entry<String, CSCI_Allocation> entry = Iter.next();
					CSCI_Allocation aCSCI_Allocation = entry.getValue();
					
					try {
						cell = row.createCell(cellIndex++);
						int NbrRef = aCSCI_Allocation.getNbrReferences();
						cell.setCellValue(NbrRef);
						cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightCellStyle));
					}
					catch (RuntimeException e) {
						logger.log(Level.SEVERE , e.getLocalizedMessage());
					}
				}	
			}
		}
	}
	
	private String SuppressForbiddenChar(String inStr) {

		String outStr = inStr;
		// Sheet name cannot be blank, greater than 31 chars, or contain any of /\*?[]	
		// 10 November 2008 : regression from a previous version
		// underscore character is allowed
		char[] toBeSuppressedChars = { '(' , '?' ,
				',' , ';' , ')' , '"' , ':' , '/' , '-' , '[' , ']' , '*' , '\''};
		int index = 0;
		int len = outStr.length() ;
		while (index < len) {
			char c = outStr.charAt(index);
			int sc = 0;
			boolean exitLoop = false;
			while ((sc < toBeSuppressedChars.length) && (exitLoop == false)){
				if (c == toBeSuppressedChars[sc]) {
					outStr = outStr.substring(0, index)+outStr.substring(index+1, len);
					len = outStr.length();
					index = 0;
					exitLoop = true;
				}
				sc++;
			}
			index = index + 1;
		}
		return outStr;
	}
}
