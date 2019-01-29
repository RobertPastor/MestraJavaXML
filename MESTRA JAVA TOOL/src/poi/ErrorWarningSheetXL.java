package poi;

import java.util.Iterator;

import mestra.MestraBase;
import mestra.MestraFile;
import mestra.MestraFiles;
import mestra.MestraMF;
import mestra.MestraMF_Collection;
import mestra.MestraSDD;
import mestra.MestraSDD_Collection;
import mestra.MestraSRS;
import mestra.MestraSRS_Collection;
import mestra.MestraSSS;
import mestra.MestraSSS_Collection;
import mestra.MestraTS;
import mestra.MestraTS_Collection;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import poi.CellStyles.CellStylesEnum;

/**
 * 
 * The aim of this class is to create an Error and Warning sheet
 * next to the MESTRA results or next to the trace-ability results
 * 
 * @author Pastor
 * @since July 2007
 */

public class ErrorWarningSheetXL extends ExcelSheet {

	public ErrorWarningSheetXL(HSSFWorkbook wb, String sheetName) {
		super(wb,sheetName);

	}
	
	private void WriteHeader() {
		
		// row are 0 indexed
		HSSFRow row = sheet.createRow((short)rowIndex++);
		// cell index are 0 indexed. But here we need to start in column 2
		// column is used for a counter
		int cellIndex = 0;

		HSSFCell cellId = row.createCell(cellIndex++);
		HSSFRichTextString strRTId = new HSSFRichTextString("Id");
		cellId.setCellValue(strRTId);
		cellId.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightBoldCellStyle));
		
		HSSFCell cellFileName = row.createCell(cellIndex++);
		HSSFRichTextString strRTFileName = new HSSFRichTextString("FileName");
		cellFileName.setCellValue(strRTFileName);
		cellFileName.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightBoldCellStyle));
		
		HSSFCell cellItem = row.createCell(cellIndex++);
		HSSFRichTextString strRTItem = new HSSFRichTextString("Item Identifier");
		cellItem.setCellValue(strRTItem);
		cellItem.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightBoldCellStyle));
		
		HSSFCell cellErrorWarning = row.createCell(cellIndex++);
		HSSFRichTextString strRTErrorWarning = new HSSFRichTextString("Error Warning");
		cellErrorWarning.setCellValue(strRTErrorWarning);
		cellErrorWarning.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightBoldCellStyle));
		
	}
	
	private void WriteOneSetOfErrorsWarnings(MestraBase mestraBase,
			int MarkerIndex) {
		
		HSSFRow row = null;
		row = sheet.createRow((short)rowIndex++);
		
		int cellIndex = 0;
		
		HSSFCell cell = null;
		cell = row.createCell(cellIndex++);
		cell.setCellValue(MarkerIndex);
		cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightCellStyle));

		cell = row.createCell(cellIndex++);
		HSSFRichTextString strRT = new HSSFRichTextString(mestraBase.getShortFileName());
		cell.setCellValue(strRT);
		cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightCellStyle));

		cell = row.createCell(cellIndex++);
		strRT = new HSSFRichTextString(mestraBase.getIdentifier());
		cell.setCellValue(strRT);
		cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightCellStyle));
		
		boolean FirstRow = false;
		// fill in the Errors and Warnings
		mestraBase.computeWarningsErrors();
		
		Iterator<String> Iter1 = mestraBase.ErrorsIterator();
		while (Iter1.hasNext()) {
			String strError = Iter1.next();
			if (FirstRow == false) {
				FirstRow = true;
			}
			else {
				row = sheet.createRow((short)rowIndex++);
			}
			cell = row.createCell(cellIndex);
			strRT = new HSSFRichTextString(strError);
			cell.setCellValue(strRT);
			cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightCellStyle));
		}
		
		Iterator<String> Iter2 = mestraBase.WarningsIterator();
		while (Iter2.hasNext()) {
			String strWarning = Iter2.next();
			if (FirstRow == false) {
				FirstRow = true;
			}
			else {
				row = sheet.createRow((short)rowIndex++);
			}
			cell = row.createCell(cellIndex);
			strRT = new HSSFRichTextString(strWarning);
			cell.setCellValue(strRT);
			cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightCellStyle));
		}
	}
	
	public void WriteErrorsWarnings(MestraFiles mestraFiles) {
		
		int MarkerIndex = 1;
	
		Iterator<MestraFile> Iter = mestraFiles.iterator();
        if (Iter.hasNext()) {
        	MestraFile mestraFile = Iter.next();
        	switch (mestraFile.getFileType().getMestraFileType()) {
    		case SSS:
    			// here mestraFiles.get... instead of mestraFile.get...
    			MestraSSS_Collection mestraSSStags = mestraFiles.getMestraSSStags();
    			Iterator<MestraSSS> Iter1 = mestraSSStags.iterator();
    			while (Iter1.hasNext()) {
    				MestraBase mestraBase = Iter1.next();
    				WriteOneSetOfErrorsWarnings(mestraBase,MarkerIndex++);
    			}
    			break;
			default:
				break;
        	}
        }
	}
	
	public void WriteErrorsWarnings(MestraFile mestraFile) {
		
		int MarkerIndex = 1;
		// write the header of the Errors and Warnings results
		WriteHeader();
		
		switch (mestraFile.getFileType().getMestraFileType()) {
		case SSS:
			MestraSSS_Collection mestraSSStags = mestraFile.getMestraSSStags();
			Iterator<MestraSSS> Iter1 = mestraSSStags.iterator();
			while (Iter1.hasNext()) {
				MestraBase mestraBase = Iter1.next();
				WriteOneSetOfErrorsWarnings(mestraBase, MarkerIndex++);
			}
			break;
			
		case SRS:
			MestraSRS_Collection mestraSRStags = mestraFile.getMestraSRStags();
			Iterator<MestraSRS> Iter2 = mestraSRStags.iterator();
			while (Iter2.hasNext()) {
				MestraBase mestraBase = Iter2.next();
				WriteOneSetOfErrorsWarnings(mestraBase, MarkerIndex++);
			}
			break;
			
		case SDD:
			MestraSDD_Collection mestraSDDtags = mestraFile.getMestraSDDtags();
			Iterator<MestraSDD> Iter3 = mestraSDDtags.iterator();
			while (Iter3.hasNext()) {
				MestraBase mestraBase = Iter3.next();
				WriteOneSetOfErrorsWarnings(mestraBase,MarkerIndex++);
			}
			break;
			
		case MF:
			MestraMF_Collection mestraMFtags = mestraFile.getMestraMFtags();
			Iterator<MestraMF> Iter4 = mestraMFtags.iterator();
			while (Iter4.hasNext()) {
				MestraBase mestraBase = Iter4.next();
				WriteOneSetOfErrorsWarnings(mestraBase,MarkerIndex++);
			}
			break;
			
		case TS:
			MestraTS_Collection mestraTStags = mestraFile.getMestraTStags();
			Iterator<MestraTS> Iter5 = mestraTStags.iterator();
			while (Iter5.hasNext()) {
				MestraBase mestraBase = Iter5.next();
				WriteOneSetOfErrorsWarnings(mestraBase,MarkerIndex++);
			}
			break;
		}
	}
}
