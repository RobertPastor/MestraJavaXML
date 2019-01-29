package poi;

import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import mestra.IdentifierList;
import mestra.MestraBase;
import mestra.MestraFileType;
import mestra.MestraFiles;
import mestra.MestraMF;
import mestra.MestraMF_Collection;
import mestra.MestraSDD;
import mestra.MestraSDD_Collection;
import mestra.MestraSRS;
import mestra.MestraSRS_Collection;
import mestra.MestraSSS;
import mestra.MestraSSS_Collection;
import mestra.MestraScenario;
import mestra.MestraTS;
import mestra.MestraTS_Collection;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import poi.CellStyles.CellStylesEnum;
import configuration.ConfigurationFileBaseReader;
import configuration.MestraStyle;
import configuration.MestraStylesMap;

/**
 * This class manages Trace-ability outputs 
 * @author Robert Pastor
 * @since September 2007
 */

public class TraceabilityOutputSheet extends MestraOutputSheet {

	final static Logger logger = Logger.getLogger(TraceabilityOutputSheet.class.getName()); 

	public enum TraceabilitySheetType { UpDown, DownUp  }
	private TraceabilitySheetType sheetType = null;

	/**
	 * 
	 * @param wb
	 * @param aSheetType
	 * @param sheetName
	 */
	public TraceabilityOutputSheet (HSSFWorkbook wb,
			TraceabilitySheetType aSheetType,
			String sheetName) {

		super(wb,sheetName);
		this.sheetType = aSheetType;
	}

	/**
	 * Writes in the EXCEL sheet the headers of the columns in bold style
	 * @param configuration
	 * @param mestraFileTypeEnum
	 */
	private void WriteTraceabilityHeader(ConfigurationFileBaseReader configuration,
			MestraFiles mestraFiles,
			MestraFileType.MestraFileTypeEnum mestraFileTypeEnum) {

		// row are 0 indexed
		HSSFRow row = sheet.createRow((short)rowIndex++);

		// cell index are 0 indexed. But here we need to start in column 2
		// column 1 is used for the file name (specific to a traceability exercise)
		int cellIndex = 0;
		HSSFCell cell = null;
		cell = row.createCell(cellIndex++);

		HSSFRichTextString strRT = null;
		strRT = new HSSFRichTextString("File");
		cell.setCellValue(strRT);
		cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightBoldCellStyle));      

		MestraStylesMap mestraStyles = configuration.getMestraStyles(mestraFileTypeEnum);

		// write only the Mestra Mandatory style header. 
		for (Map.Entry<String, MestraStyle> entry : mestraStyles.getMestraStylesMap().entrySet()) {

			MestraStyle mestraStyle = entry.getValue(); 
			if (mestraStyle.isMandatory() == true) {
				// write the mandatory style
				String styleHeader = mestraStyle.getMestraStyleHeader();

				cell = row.createCell(cellIndex++);
				strRT = new HSSFRichTextString(styleHeader);
				cell.setCellValue(strRT);
				cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightBoldCellStyle));
			}
		}

		// write all other Mestra Base styles in alpha order
		for (Map.Entry<String, MestraStyle> entry : mestraStyles.getMestraStylesMap().entrySet()) {

			MestraStyle mestraStyle = entry.getValue();
			// mandatory style in first column and traceability in last column
			if ((mestraStyle.isMandatory() == false) && (mestraStyle.isTraceability() == false)){
				// write all style headers except the first and the last
				// check is the data has to be displayed
				if (mestraStyle.getDisplayMestraStyle()) {

					String styleHeader = mestraStyle.getMestraStyleHeader();

					cell = row.createCell(cellIndex++);
					strRT = new HSSFRichTextString(styleHeader);
					cell.setCellValue(strRT);
					cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightBoldCellStyle)); 
				}
			}
		}

		MestraScenario mestraScenario = mestraFiles.getMestraScenario();

		// 21 March 2008
		// specific add on for Dominique Bornant
		if (mestraScenario.getMestraScenarioEnum() == MestraScenario.MestraScenarioEnum.trace_SSS_SRS) {
			if (mestraFileTypeEnum == MestraFileType.MestraFileTypeEnum.SRS) {
				cell = row.createCell(cellIndex++);
				strRT = new HSSFRichTextString("SRS Safety");
				cell.setCellValue(strRT);
				cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightBoldCellStyle));      

				cell = row.createCell(cellIndex++);
				strRT = new HSSFRichTextString("Test Method");
				cell.setCellValue(strRT);
				cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightBoldCellStyle));      

			}
		}

		cell = row.createCell(cellIndex++);

		switch (mestraScenario.getMestraScenarioEnum()) {
		case trace_SSS_SRS: 
			if (mestraFileTypeEnum == MestraFileType.MestraFileTypeEnum.SSS) {
				
				strRT = new HSSFRichTextString("Allocation");
				cell.setCellValue(strRT);
				cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightBoldCellStyle));
				
				cell = row.createCell(cellIndex++);
				strRT = new HSSFRichTextString("SRS Coverage");
				cell.setCellValue(strRT);
				cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightBoldCellStyle));
				
				cell = row.createCell(cellIndex++);
				strRT = new HSSFRichTextString("SRS Req Title");
				cell.setCellValue(strRT);
				cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightBoldCellStyle));
				

			} else {
				// file type is SRS hence we start with the SSS Req that is covered
				strRT = new HSSFRichTextString("SSS Covered");
				cell.setCellValue(strRT);
				cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightBoldCellStyle));

				cell = row.createCell(cellIndex++);
				strRT = new HSSFRichTextString("SSS Safety");
				cell.setCellValue(strRT);
				cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightBoldCellStyle));      

			}
			break;
		case trace_SRS_MF:
			if (mestraFileTypeEnum == MestraFileType.MestraFileTypeEnum.SRS) {
				strRT = new HSSFRichTextString("MF Coverage");
				cell.setCellValue(strRT);
				cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightBoldCellStyle));

			}
			else {
				strRT = new HSSFRichTextString("SRS Covered");
				cell.setCellValue(strRT);
				cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightBoldCellStyle));

			}
			break;
		case trace_SRS_TS:
			if (mestraFileTypeEnum == MestraFileType.MestraFileTypeEnum.SRS) {
				strRT = new HSSFRichTextString("TS Coverage");
				cell.setCellValue(strRT);
				cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightBoldCellStyle));

			}
			else {
				strRT = new HSSFRichTextString("SRS Covered");
				cell.setCellValue(strRT);
				cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightBoldCellStyle));

			}
			break;
		case trace_SRS_SDD:
			if (mestraFileTypeEnum == MestraFileType.MestraFileTypeEnum.SRS) {
				strRT = new HSSFRichTextString("SDD Coverage");
				cell.setCellValue(strRT);
				cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightBoldCellStyle));

				cell = row.createCell(cellIndex++);
				strRT = new HSSFRichTextString("SDD Title");
				cell.setCellValue(strRT);
				cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightBoldCellStyle));      

			}
			else {
				strRT = new HSSFRichTextString("SRS Covered");
				cell.setCellValue(strRT);
				cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightBoldCellStyle));

				cell = row.createCell(cellIndex++);
				strRT = new HSSFRichTextString("SRS Requirement Title");
				cell.setCellValue(strRT);
				cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightBoldCellStyle));      

			}
			break;
		default:
			break;
		}


		// 06 April 2012
		// specific add on for Frederic Leblanc
		if (mestraScenario.getMestraScenarioEnum() == MestraScenario.MestraScenarioEnum.trace_SRS_SDD) {
			if (mestraFileTypeEnum == MestraFileType.MestraFileTypeEnum.SRS) {
				cell = row.createCell(cellIndex++);
				strRT = new HSSFRichTextString("Safety");
				cell.setCellValue(strRT);
				cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightBoldCellStyle));      

				cell = row.createCell(cellIndex++);
				strRT = new HSSFRichTextString("Test Method");
				cell.setCellValue(strRT);
				cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightBoldCellStyle));      

			}
		}
	}

	public void WriteTraceabilityMarkers(MestraFiles mestraFiles,
			ConfigurationFileBaseReader configuration) {

		MestraScenario mestraScenario = mestraFiles.getMestraScenario();

		switch (mestraScenario.getMestraScenarioEnum()) {
		case trace_SSS_SRS:
			switch (sheetType) {
			case UpDown:
				WriteTraceabilitySSS_SRS(mestraFiles, configuration);
				break;
			case DownUp:
				WriteTraceabilitySRS_SSS(mestraFiles,configuration);
				break;
			}
			break;

		case trace_SRS_MF:
			switch (sheetType) {
			case UpDown:
				WriteTraceabilitySRS_MF(mestraFiles,configuration);
				break;
			case DownUp:
				WriteTraceabilityMF_SRS(mestraFiles,configuration);
				break;
			}
			break;

		case trace_SRS_TS:
			switch (sheetType) {
			case UpDown:
				WriteTraceabilitySRS_TS(mestraFiles,configuration);
				break;
			case DownUp:
				WriteTraceabilityTS_SRS(mestraFiles,configuration);
				break;
			}
			break;

		case trace_SRS_SDD:
			switch (sheetType) {
			case UpDown:
				WriteTraceabilitySRS_SDD(mestraFiles,configuration);
				break;
			case DownUp:
				WriteTraceabilitySDD_SRS(mestraFiles,configuration);
				break;
			}
			break;
		default:
			break;          
		}
	}


	private void WriteTraceabilityMF_SRS(MestraFiles mestraFiles,
			ConfigurationFileBaseReader configuration) {

		HSSFRow row = null;
		HSSFRichTextString strRT;
		HSSFCell cell = null;

		// write the header for the trace-ability column results
		WriteTraceabilityHeader(configuration,mestraFiles,
				MestraFileType.MestraFileTypeEnum.MF);

		MestraMF_Collection mestraMF_Collection = mestraFiles.getMestraMFtags();
		Iterator<MestraMF> Iter1 = mestraMF_Collection.iterator();
		while (Iter1.hasNext()) {

			MestraMF mestraMF = Iter1.next();
			MestraSRS_Collection mestraSRS_Collection = mestraFiles.getMestraSRStags();
			Iterator<MestraSRS> Iter2 = mestraSRS_Collection.iterator();

			boolean found = false;

			while (Iter2.hasNext()) {
				MestraSRS mestraSRS = Iter2.next();

				if (mestraMF.IsCovering(mestraSRS.getIdentifier())) {

					found = true;

					row = sheet.createRow((short)rowIndex++);
					int cellIndex = WriteBaseMarkers((MestraBase) mestraMF, row, mestraMF.getShortFileName());

					cell = row.createCell(cellIndex++);
					strRT = new HSSFRichTextString(mestraSRS.getIdentifier());
					cell.setCellValue(strRT);
					cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightCellStyle));
				}
			}
			if (found == false) {
				row = sheet.createRow((short)rowIndex++);
				int cellIndex = WriteBaseMarkers((MestraBase) mestraMF,row,mestraMF.getShortFileName());

				cell = row.createCell(cellIndex++);
				strRT = new HSSFRichTextString("unknown SRS Requirement");
				cell.setCellValue(strRT);
				cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightYellowBackGroundCellStyle));
			}
		}
	}

	private void WriteTraceabilitySRS_MF(MestraFiles mestraFiles,
			ConfigurationFileBaseReader configuration) {

		HSSFRow row = null;
		HSSFRichTextString strRT;
		HSSFCell cell = null;

		// write the header of the results
		WriteTraceabilityHeader(configuration,mestraFiles,
				MestraFileType.MestraFileTypeEnum.SRS);

		MestraSRS_Collection mestraSRS_Collection = mestraFiles.getMestraSRStags();
		Iterator<MestraSRS> Iter1 = mestraSRS_Collection.iterator();
		while (Iter1.hasNext()) {

			MestraSRS mestraSRS = Iter1.next();
			// we state first that the Requirement is not covered
			mestraSRS.setCovered(false);

			MestraMF_Collection mestraMF_Collection = mestraFiles.getMestraMFtags();
			Iterator<MestraMF> Iter2 = mestraMF_Collection.iterator();

			while (Iter2.hasNext()) {

				MestraMF mestraMF = Iter2.next();
				if (mestraMF.IsCovering(mestraSRS.getIdentifier())) {

					// set this SRS Requirement as covered
					mestraSRS.setCovered(true);
					row = sheet.createRow((short)rowIndex++);

					int cellIndex = WriteBaseMarkers((MestraBase) mestraSRS, row, mestraSRS.getShortFileName());

					cell = row.createCell(cellIndex++);
					strRT = new HSSFRichTextString(mestraMF.getIdentifier());
					cell.setCellValue(strRT);
					cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightCellStyle));
				}
			}
			if ( mestraSRS.isCovered() == false  ) {

				row = sheet.createRow((short)rowIndex++);
				int cellIndex = WriteBaseMarkers((MestraBase) mestraSRS, row, mestraSRS.getShortFileName());

				cell = row.createCell(cellIndex++);
				strRT = new HSSFRichTextString("SRS is not covered");
				cell.setCellValue(strRT);
				cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightRedBackGroundCellStyle));
			}
		}
	}

	private void WriteTraceabilitySDD_SRS(MestraFiles mestraFiles,
			ConfigurationFileBaseReader configuration) {

		HSSFRow row = null;
		HSSFCell cell = null;
		HSSFRichTextString strRT;

		// Write the Header
		WriteTraceabilityHeader(configuration,mestraFiles,
				MestraFileType.MestraFileTypeEnum.SDD);

		MestraSDD_Collection mestraSDD_Collection = mestraFiles.getMestraSDDtags();
		Iterator<MestraSDD> Iter1 = mestraSDD_Collection.iterator();
		while (Iter1.hasNext()) {

			MestraSDD mestraSDD = Iter1.next();
			logger.info( "WriteTraceabilitySDD_SRS: "+mestraSDD.getIdentifier());

			MestraSRS_Collection mestraSRS_Collection = mestraFiles.getMestraSRStags();
			Iterator<MestraSRS> Iter2 = mestraSRS_Collection.iterator();

			while (Iter2.hasNext()) {
				MestraSRS mestraSRS = Iter2.next();
				if (mestraSDD.IsCovering(mestraSRS.getIdentifier())) {

					row = sheet.createRow((short)rowIndex++);
					int cellIndex = WriteBaseMarkers((MestraBase) mestraSDD, row, mestraSDD.getShortFileName());

					cell = row.createCell(cellIndex++);
					strRT = new HSSFRichTextString(mestraSRS.getIdentifier());
					cell.setCellValue(strRT);
					cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightCellStyle));

					cell = row.createCell(cellIndex++);
					strRT = new HSSFRichTextString(mestraSRS.getTitle());
					cell.setCellValue(strRT);
					cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightCellStyle));
				}
			}
		}

		// we list here all unknown references from SDD to SRS
		rowIndex++;
		row = sheet.createRow((short)rowIndex++);
		cell = row.createCell(0);
		strRT = new HSSFRichTextString("Unknown links from SDD to SRS");
		cell.setCellValue(strRT);
		cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightYellowBackGroundCellStyle));

		mestraSDD_Collection = mestraFiles.getMestraSDDtags();
		Iter1 = mestraSDD_Collection.iterator();
		while (Iter1.hasNext()) {
			MestraSDD mestraSDD = Iter1.next();
			IdentifierList SRS_ReferenceList = mestraSDD.getImplementedSRSrequirements();

			Iterator<String> Iter4 = SRS_ReferenceList.iterator();
			while (Iter4.hasNext()) {
				String SRS_Reference = Iter4.next();

				if (mestraFiles.isSRSreferenceKnown(SRS_Reference) == false) {

					row = sheet.createRow((short)rowIndex++);

					cell = row.createCell(0);
					strRT = new HSSFRichTextString(mestraSDD.getIdentifier());
					cell.setCellValue(strRT);
					cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightCellStyle));

					cell = row.createCell(1);
					strRT = new HSSFRichTextString(SRS_Reference);
					cell.setCellValue(strRT);
					cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightCellStyle));
				}
			}
		}
	}

	private void WriteTraceabilitySSS_SRS(MestraFiles mestraFiles,
			ConfigurationFileBaseReader configuration) {

		HSSFRow row = null;
		HSSFCell cell = null;
		HSSFRichTextString strRT;

		// Write the Header
		WriteTraceabilityHeader(configuration, mestraFiles, MestraFileType.MestraFileTypeEnum.SSS);

		MestraSSS_Collection mestraSSS_Collection = mestraFiles.getMestraSSStags();
		Iterator<MestraSSS> Iter1 = mestraSSS_Collection.iterator();
		while (Iter1.hasNext()) {

			MestraSSS mestraSSS = Iter1.next();
			// we state first that the SSS Requirement is not covered
			mestraSSS.setCovered(false);

			MestraSRS_Collection mestraSRS_Collection = mestraFiles.getMestraSRStags();
			Iterator<MestraSRS> Iter2 = mestraSRS_Collection.iterator();

			while (Iter2.hasNext()) {

				MestraSRS mestraSRS = Iter2.next();
				if (mestraSRS.IsCovering(mestraSSS.getIdentifier())) {

					// set this SSS Requirement as covered
					mestraSSS.setCovered(true);
					row = sheet.createRow((short)rowIndex++);

					int cellIndex = WriteBaseMarkers((MestraBase) mestraSSS, row, mestraSSS.getShortFileName());

					cell = row.createCell(cellIndex++);
					// write the names of the CSCIs to which this SSS Req is allocated
					strRT = new HSSFRichTextString(mestraSSS.getRequirementAllocation());
					cell.setCellValue(strRT);
					if ( (mestraSSS.isAllocated(mestraFiles.getTraceability_CSCI())) || 
							( (mestraSSS.isCPOH()) && (mestraFiles.getTraceability_CSCI().equalsIgnoreCase("ODS")))) {

						cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightCellStyle));
					}
					else {
						// This SSS requirement is not allocated to the trace-ability CSCI
						cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightYellowBackGroundCellStyle));
					}

					cell = row.createCell(cellIndex++);
					// this is a SRS req covering the SSS Req
					strRT = new HSSFRichTextString(mestraSRS.getIdentifier());
					cell.setCellValue(strRT);
					cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightCellStyle));

					cell = row.createCell(cellIndex++);
					if (mestraSRS.getTitle().length() == 0) {
						strRT = new HSSFRichTextString("missing title");
						cell.setCellValue(strRT);
						cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightYellowBackGroundCellStyle));   
					}
					else {  
						strRT = new HSSFRichTextString(mestraSRS.getTitle());
						cell.setCellValue(strRT);
						cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightCellStyle));
					}
				}
			}

			if  (mestraSSS.isCovered() == false ) {
				if ( (mestraSSS.isAllocated(mestraFiles.getTraceability_CSCI())) || 
						( (mestraSSS.isCPOH()) && (mestraFiles.getTraceability_CSCI().equalsIgnoreCase("ODS")))) {

					row = sheet.createRow((short)rowIndex++);
					int cellIndex = WriteBaseMarkers((MestraBase) mestraSSS, row, mestraSSS.getShortFileName());

					cell = row.createCell(cellIndex++);
					strRT = new HSSFRichTextString(mestraSSS.getRequirementAllocation());
					cell.setCellValue(strRT);
					cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightCellStyle));

					cell = row.createCell(cellIndex++);
					strRT = new HSSFRichTextString("SSS is not covered");
					cell.setCellValue(strRT);
					cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightRedBackGroundCellStyle));
				}
			}
		}
	}

	private void WriteTraceabilitySRS_SDD(MestraFiles mestraFiles,
			ConfigurationFileBaseReader configuration) {

		HSSFRow row = null;
		HSSFCell cell = null;
		HSSFRichTextString strRT;

		// Write the Header
		WriteTraceabilityHeader(configuration,mestraFiles,
				MestraFileType.MestraFileTypeEnum.SRS);

		MestraSRS_Collection mestraSRS_Collection = mestraFiles.getMestraSRStags();
		Iterator<MestraSRS> Iter1 = mestraSRS_Collection.iterator();
		while (Iter1.hasNext()) {

			MestraSRS mestraSRS = Iter1.next();
			mestraSRS.setCovered(false);

			MestraSDD_Collection mestraSDD_Collection = mestraFiles.getMestraSDDtags();
			Iterator<MestraSDD> Iter2 = mestraSDD_Collection.iterator();

			// iterate through all SDD component covering a SRS requirement
			while (Iter2.hasNext()) {
				MestraSDD mestraSDD = Iter2.next();
				if (mestraSDD.IsCovering(mestraSRS.getIdentifier())) {
					mestraSRS.setCovered(true);

					row = sheet.createRow((short)rowIndex++);
					int cellIndex = WriteBaseMarkers((MestraBase) mestraSRS,row,mestraSRS.getShortFileName());

					cell = row.createCell(cellIndex++);
					strRT = new HSSFRichTextString(mestraSDD.getIdentifier());
					cell.setCellValue(strRT);
					cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightCellStyle));

					cell = row.createCell(cellIndex++);
					strRT = new HSSFRichTextString(mestraSDD.getTitle());
					cell.setCellValue(strRT);
					cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightCellStyle));

					// specific add on for Frederic Leblanc 06 April 2012
					cell = row.createCell(cellIndex++);
					if (mestraSRS.getMethodLevelSafety().isSafety()) {
						strRT = new HSSFRichTextString("True");
					}
					else {
						strRT = new HSSFRichTextString("False");
					}
					cell.setCellValue(strRT);
					cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightCellStyle));

					cell = row.createCell(cellIndex++);
					strRT = new HSSFRichTextString(mestraSRS.getMethodLevelSafety().getMethodLevelStr());
					cell.setCellValue(strRT);
					cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightCellStyle));

					// end of specific add on 06 April 2012

				}
			}
			if (mestraSRS.isCovered() == false) {

				row = sheet.createRow((short)rowIndex++);
				int cellIndex = WriteBaseMarkers((MestraBase) mestraSRS, row, mestraSRS.getShortFileName());

				cell = row.createCell(cellIndex++);
				strRT = new HSSFRichTextString("SRS is not covered");
				cell.setCellValue(strRT);
				cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightRedBackGroundCellStyle));
			}
		}
	}

	private void WriteTraceabilitySRS_SSS_UnknownLinks (MestraFiles mestraFiles,ConfigurationFileBaseReader configuration) {

		HSSFRow row = null;
		HSSFCell cell = null;
		HSSFRichTextString strRT;

		// need to write all the links from SRS to SSS where the SSS Requirement is unknown
		rowIndex++;
		row = sheet.createRow((short)rowIndex++);
		cell = row.createCell(0);
		strRT = new HSSFRichTextString("Unknown links from SRS to SSS");
		cell.setCellValue(strRT);
		cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightCellStyle));

		// we list here all unknown references from SRS to SSS
		MestraSRS_Collection mestraSRS_Collection = mestraFiles.getMestraSRStags();

		//mestraSRS_Collection = mestraFiles.getMestraSRStags();
		Iterator<MestraSRS> Iter = mestraSRS_Collection.iterator();

		while (Iter.hasNext()) {
			MestraSRS mestraSRS = Iter.next();

			IdentifierList SSS_ReferenceList = mestraSRS.getSSS_ReferencesList();
			Iterator<String> Iter4 = SSS_ReferenceList.iterator();
			while (Iter4.hasNext()) {
				String SSS_Reference = Iter4.next();

				if (mestraFiles.isSSSreferenceKnown(SSS_Reference) == false) {

					row = sheet.createRow((short)rowIndex++);
					int cellIndex = WriteBaseMarkers((MestraBase) mestraSRS, row, mestraSRS.getShortFileName());

					cell = row.createCell(cellIndex++);
					strRT = new HSSFRichTextString(SSS_Reference);
					cell.setCellValue(strRT);
					cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightYellowBackGroundCellStyle));
				}
			}
		}
	}

	private void WriteTraceabilitySRS_SSS_SRSreqDerived (MestraFiles mestraFiles,ConfigurationFileBaseReader configuration) {

		HSSFRow row = null;
		HSSFCell cell = null;
		HSSFRichTextString strRT;

		// need to write all the SRS  Requirement that are REQ_DERIVED
		//rowIndex++;
		//row = sheet.createRow((short)rowIndex++);
		//cell = row.createCell((short)0);
		//strRT = new HSSFRichTextString("SRS Req Derived");
		//cell.setCellValue(strRT);
		//cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightCellStyle));

		// we list here all unknown references from SRS to SSS
		MestraSRS_Collection mestraSRS_Collection = mestraFiles.getMestraSRStags();

		//mestraSRS_Collection = mestraFiles.getMestraSRStags();
		Iterator<MestraSRS> Iter = mestraSRS_Collection.iterator();

		while (Iter.hasNext()) {
			MestraSRS mestraSRS = Iter.next();
			if (mestraSRS.isReqDerived()) {
				row = sheet.createRow((short)rowIndex++);
				int cellIndex = WriteBaseMarkers((MestraBase) mestraSRS, row, mestraSRS.getShortFileName());

				// specific add on for Dominique Bornant 21 March 2008
				cell = row.createCell(cellIndex++);
				if (mestraSRS.getMethodLevelSafety().isSafety()) {
					strRT = new HSSFRichTextString("True");
				}
				else {
					strRT = new HSSFRichTextString("False");
				}
				cell.setCellValue(strRT);
				cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightCellStyle));

				cell = row.createCell(cellIndex++);
				strRT = new HSSFRichTextString(mestraSRS.getMethodLevelSafety().getMethodLevelStr());
				cell.setCellValue(strRT);
				cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightCellStyle));

				// end of specific add on 21 March 2008

				cell = row.createCell(cellIndex++);
				strRT = new HSSFRichTextString("Req_Derived");
				cell.setCellValue(strRT);
				cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightCellStyle));
			}
		}
	}


	private void WriteTraceabilitySRS_SSS(MestraFiles mestraFiles,
			ConfigurationFileBaseReader configuration) {

		HSSFRow row = null;
		HSSFCell cell = null;
		HSSFRichTextString strRT;

		// write the headers
		WriteTraceabilityHeader(configuration, mestraFiles, MestraFileType.MestraFileTypeEnum.SRS);

		MestraSRS_Collection mestraSRS_Collection = mestraFiles.getMestraSRStags();
		Iterator<MestraSRS> Iter1 = mestraSRS_Collection.iterator();
		while (Iter1.hasNext()) {

			MestraSRS mestraSRS = Iter1.next();

			MestraSSS_Collection mestraSSS_Collection = mestraFiles.getMestraSSStags();
			Iterator<MestraSSS> Iter2 = mestraSSS_Collection.iterator();

			while (Iter2.hasNext()) {

				MestraSSS mestraSSS = Iter2.next();
				if (mestraSRS.IsCovering(mestraSSS.getIdentifier())) {

					row = sheet.createRow((short)rowIndex++);
					int cellIndex = WriteBaseMarkers((MestraBase) mestraSRS, row, mestraSRS.getShortFileName());

					// specific add on for Dominique Bornant 21 March 2008
					cell = row.createCell(cellIndex++);
					if (mestraSRS.getMethodLevelSafety().isSafety()) {
						strRT = new HSSFRichTextString("True");
					}
					else {
						strRT = new HSSFRichTextString("False");
					}
					cell.setCellValue(strRT);
					cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightCellStyle));

					cell = row.createCell(cellIndex++);
					strRT = new HSSFRichTextString(mestraSRS.getMethodLevelSafety().getMethodLevelStr());
					cell.setCellValue(strRT);
					cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightCellStyle));

					// end of specific add on 21 March 2008

					cell = row.createCell(cellIndex++);
					strRT = new HSSFRichTextString(mestraSSS.getIdentifier());
					cell.setCellValue(strRT);
					cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightCellStyle));

					// specific add on for D. Bornant 21 March 2008
					cell = row.createCell(cellIndex++);
					if (mestraSSS.getMethodLevelSafety().isSafety()) {
						strRT = new HSSFRichTextString("True");
					}
					else {
						strRT = new HSSFRichTextString("False");
					}
					cell.setCellValue(strRT);
					cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightCellStyle));

				}
			}
		}
		WriteTraceabilitySRS_SSS_SRSreqDerived (mestraFiles,configuration);
		WriteTraceabilitySRS_SSS_UnknownLinks (mestraFiles,configuration);

	}

	private void WriteTraceabilityTS_SRS(MestraFiles mestraFiles,
			ConfigurationFileBaseReader configuration) {

		HSSFRow row = null;
		HSSFCell cell = null;
		HSSFRichTextString strRT;

		// Write the column Header
		WriteTraceabilityHeader(configuration,mestraFiles,
				MestraFileType.MestraFileTypeEnum.TS);

		MestraTS_Collection mestraTS_Collection = mestraFiles.getMestraTStags();
		Iterator<MestraTS> Iter1 = mestraTS_Collection.iterator();
		while (Iter1.hasNext()) {

			MestraTS mestraTS = Iter1.next();

			MestraSRS_Collection mestraSRS_Collection = mestraFiles.getMestraSRStags();
			Iterator<MestraSRS> Iter2 = mestraSRS_Collection.iterator();

			while (Iter2.hasNext()) {

				MestraSRS mestraSRS = Iter2.next();

				if (mestraTS.IsCovering(mestraSRS.getIdentifier())) {

					row = sheet.createRow((short)rowIndex++);
					int cellIndex = WriteBaseMarkers((MestraBase) mestraTS, row, mestraTS.getShortFileName());

					cell = row.createCell(cellIndex++);
					strRT = new HSSFRichTextString(mestraSRS.getIdentifier());
					cell.setCellValue(strRT);
					cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightCellStyle));

					cell = row.createCell(cellIndex++);
					strRT = new HSSFRichTextString(mestraSRS.getTitle());
					cell.setCellValue(strRT);
					cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightCellStyle));
				}
			}       
		}

		rowIndex++;
		row = sheet.createRow((short)rowIndex++);
		cell = row.createCell(0);
		strRT = new HSSFRichTextString("Unknown links from TS to SRS");
		cell.setCellValue(strRT);
		cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightCellStyle));

		// we list here all unknown references from TS to SRS
		// we will browse through the TS Markers
		mestraTS_Collection = mestraFiles.getMestraTStags();
		Iterator<MestraTS> Iter3 = mestraTS_Collection.iterator();

		while (Iter3.hasNext()) {
			MestraTS mestraTS = Iter3.next();

			IdentifierList SRS_ReferenceList = mestraTS.getTestedSRSrequirementsList();
			Iterator<String> Iter4 = SRS_ReferenceList.iterator();
			while (Iter4.hasNext()) {
				String SRS_Reference = Iter4.next();

				if (mestraFiles.isSRSreferenceKnown(SRS_Reference) == false) {

					row = sheet.createRow((short)rowIndex++);
					int cellIndex = WriteBaseMarkers((MestraBase) mestraTS, row, mestraTS.getShortFileName());

					cell = row.createCell(cellIndex++);
					strRT = new HSSFRichTextString(SRS_Reference);
					cell.setCellValue(strRT);
					cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightYellowBackGroundCellStyle));
				}
			}
		}
	}

	private void WriteTraceabilitySRS_TS(MestraFiles mestraFiles,
			ConfigurationFileBaseReader configuration) {

		HSSFRow row = null;
		HSSFCell cell = null;
		HSSFRichTextString strRT;

		// write the Header
		WriteTraceabilityHeader(configuration, mestraFiles, MestraFileType.MestraFileTypeEnum.SRS);

		MestraSRS_Collection mestraSRS_Collection = mestraFiles.getMestraSRStags();
		Iterator<MestraSRS> Iter1 = mestraSRS_Collection.iterator();
		while (Iter1.hasNext()) {

			MestraSRS mestraSRS = Iter1.next();
			mestraSRS.setCovered(false);

			MestraTS_Collection mestraTS_Collection = mestraFiles.getMestraTStags();
			Iterator<MestraTS> Iter2 = mestraTS_Collection.iterator();

			while (Iter2.hasNext()) {
				MestraTS mestraTS = Iter2.next();
				if (mestraTS.IsCovering(mestraSRS.getIdentifier())) {
					mestraSRS.setCovered(true);

					row = sheet.createRow((short)rowIndex++);
					int cellIndex = WriteBaseMarkers((MestraBase) mestraSRS, row, mestraSRS.getShortFileName());

					cell = row.createCell(cellIndex++);
					strRT = new HSSFRichTextString(mestraTS.getIdentifier());
					cell.setCellValue(strRT);
					cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightCellStyle));

					cell = row.createCell(cellIndex++);
					strRT = new HSSFRichTextString(mestraTS.getTitle());
					cell.setCellValue(strRT);
					cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightCellStyle));
				}
			}
			if (mestraSRS.isCovered() == false) {

				row = sheet.createRow((short)rowIndex++);
				int cellIndex = WriteBaseMarkers((MestraBase) mestraSRS, row, mestraSRS.getShortFileName());

				cell = row.createCell(cellIndex++);
				strRT = new HSSFRichTextString("SRS is not covered");
				cell.setCellValue(strRT);
				cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightRedBackGroundCellStyle));
			}
		}   
	}

	private int WriteBaseMarkers(MestraBase mestraBase, HSSFRow row, String FileName) {

		int cellIndex = 0;
		HSSFCell cell = row.createCell(cellIndex++);
		HSSFRichTextString strRT = new HSSFRichTextString(FileName);
		cell.setCellValue(strRT);
		cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightCellStyle));

		cellIndex = WriteBaseMarkers(mestraBase, row, cell, cellIndex);
		return cellIndex;
	}

}
