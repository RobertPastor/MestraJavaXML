package poi;

import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;

import mestra.CSCI_Allocation;
import mestra.CSCI_SSSFileAllocationList;
import mestra.CSCI_SSSRequirementAllocationList;
import mestra.IdentifierList;
import mestra.MestraBase;
import mestra.MestraFile;
import mestra.MestraFileType;
import mestra.MestraFileType.MestraFileTypeEnum;
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
import configuration.ConfigurationFileBaseReader;
import configuration.MestraStyle;
import configuration.MestraStylesMap;
import configuration.MethodLevelSafety;

/**
 * This class manages writing MESTRA results in an EXCEL sheet
 * @author Pastor
 *
 */
public class MestraOutputSheet extends ExcelSheet {

	/**
	 * Constructor
	 * @param wb the Workbook which will contain the sheet
	 * @param aSheetType
	 * @param sheetName
	 * 
	 */
	public MestraOutputSheet (HSSFWorkbook wb, String sheetName) {

		super(wb,sheetName);
	}

	public void WriteDataDrillMarkers(MestraFiles mestraFiles,
			ConfigurationFileBaseReader aConfiguration) {

		if (mestraFiles.AllFilesSameType(MestraFileType.MestraFileTypeEnum.SSS)) {
			WriteSSSmarkers(mestraFiles,aConfiguration);
		}
		else {
			System.err.println("Mestra Output Sheet: Write Markers --- NOT YET IMPLEMENTED ---");
			System.err.println("Mestra Output Sheet: Write Markers --- NOT YET IMPLEMENTED ---");
			System.err.println("Mestra Output Sheet: Write Markers --- NOT YET IMPLEMENTED ---");
		}

	}

	public void WriteMarkers(MestraFiles mestraFiles,
			ConfigurationFileBaseReader  configuration) {

		if (mestraFiles.AllFilesSameType(MestraFileType.MestraFileTypeEnum.SSS)) {
			WriteSSSmarkers(mestraFiles, configuration);
		}
		else {
			System.err.println("Mestra Output Sheet: Write Markers --- NOT YET IMPLEMENTED ---");
			System.err.println("Mestra Output Sheet: Write Markers --- NOT YET IMPLEMENTED ---");
			System.err.println("Mestra Output Sheet: Write Markers --- NOT YET IMPLEMENTED ---");
		}
	}

	public void WriteMarkers(MestraFile mestraFile,
			ConfigurationFileBaseReader aConfiguration) {

		switch (mestraFile.getFileType().getMestraFileType()) {
		case SSS:
			WriteSSSmarkers(mestraFile, aConfiguration);
			break;
		case SRS:
			WriteSRSmarkers(mestraFile, aConfiguration);
			break;
		case SDD:
			WriteSDDmarkers(mestraFile, aConfiguration);
			break;
		case MF:
			WriteMFmarkers(mestraFile, aConfiguration);
			break;
		case TS:
			WriteTSmarkers(mestraFile, aConfiguration);
			break;
		}
	}

	/**
	 * return number of Items in each collection.
	 * @param mestraFile
	 * @return
	 */
	private int GetNumberOfItems(MestraFile mestraFile) {

		switch (mestraFile.getFileType().getMestraFileType()) {
		case SSS:
			return mestraFile.getMestraSSStags().size();
		case SRS:
			return mestraFile.getMestraSRStags().size();
		case MF:
			return mestraFile.getMestraMFtags().size();
		case TS:
			return mestraFile.getMestraTStags().size();
		default:
			return (int)0;
		}
	}

	private void WriteHeader (MestraFiles mestraFiles,
			ConfigurationFileBaseReader aConfiguration,
			MestraFileTypeEnum FileType) {

		// row are 0 indexed
		HSSFRow row = sheet.createRow(rowIndex++);
		// cell index are 0 indexed. But here we need to start in column 2
		// column is used for a counter
		int cellIndex = 0;

		HSSFCell cell1 = row.createCell(cellIndex++);
		HSSFRichTextString strRT = new HSSFRichTextString("Id");
		cell1.setCellValue(strRT);
		cell1.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightBoldCellStyle));

		HSSFCell cell2 = row.createCell(cellIndex++);
		strRT = new HSSFRichTextString("Programme");
		cell2.setCellValue(strRT);
		cell2.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightBoldCellStyle));

		HSSFCell cell3 = row.createCell(cellIndex++);
		strRT = new HSSFRichTextString("SMFKey");
		cell3.setCellValue(strRT);
		cell3.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightBoldCellStyle));

		HSSFCell cell4 = row.createCell(cellIndex++);
		strRT = new HSSFRichTextString("File");
		cell4.setCellValue(strRT);
		cell4.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightBoldCellStyle));

		MestraStylesMap mestraStyles = aConfiguration.getMestraStyles(FileType);
		Iterator<MestraStyle> Iter1 = mestraStyles.getValues();
		while (Iter1.hasNext()) {
			MestraStyle mestraStyle = Iter1.next();
			String styleHeader = mestraStyle.getMestraStyleHeader();

			HSSFCell cell5 = row.createCell(cellIndex++);
			strRT = new HSSFRichTextString(styleHeader);
			cell5.setCellValue(strRT);
			cell5.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightBoldCellStyle));             
		}
		if (FileType.equals(MestraFileTypeEnum.SSS)) {
			CSCI_SSSFileAllocationList SSS_FileAllocationList = mestraFiles.getSSS_FileAllocationList();
			if (SSS_FileAllocationList != null) {

				Iterator<Map.Entry<String, CSCI_Allocation>> Iter2= SSS_FileAllocationList.iterator();

				while (Iter2.hasNext()) {

					Map.Entry<String, CSCI_Allocation> entry = Iter2.next();
					CSCI_Allocation aCSCI_Allocation = entry.getValue();

					strRT = new HSSFRichTextString(aCSCI_Allocation.getCSCI_Name());
					HSSFCell cell6 = row.createCell(cellIndex++);
					cell6.setCellValue(strRT);
					cell6.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightBoldCellStyle));             
				}
			}
		}
	}

	/**
	 * Write the EXCEL header row.
	 * Mandatory style header => ... all Mestra Base styles => Traceability Style Header
	 * @param mestraFile
	 * @param aConfiguration
	 */
	private void WriteHeader (MestraFile mestraFile, 
			ConfigurationFileBaseReader aConfiguration) {

		// row are 0 indexed
		HSSFRow row = sheet.createRow((short)rowIndex++);
		// cell index are 0 indexed. But here we need to start in column 2
		// column is used for a counter
		int cellIndex = 0;

		HSSFCell cell = row.createCell(cellIndex++);
		HSSFRichTextString strRT = new HSSFRichTextString("Id");
		cell.setCellValue(strRT);
		cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightBoldCellStyle));

		MestraStylesMap mestraStyles = aConfiguration.getMestraStyles(mestraFile.getFileType());

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

		// add the columns specific for each analysed file
		if (mestraFile.IsSSS()) {
			
			for (Map.Entry<String, MestraStyle> entryOne : mestraStyles.getMestraStylesMap().entrySet()) {
				
				MestraStyle mestraStyle = entryOne.getValue(); 
				if (mestraStyle.isTraceability() == true) {
					
					// write the mandatory TRACEABILITY style
					String styleHeader = mestraStyle.getMestraStyleHeader();

					strRT = new HSSFRichTextString(styleHeader);
					try {
						cell = row.createCell(cellIndex++);
						cell.setCellValue(strRT);
						cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightBoldCellStyle));    	
					}
					catch (RuntimeException e) {
						logger.log(Level.SEVERE , "error while setting Cell Value= " +  e.getLocalizedMessage());
					}
					
					CSCI_SSSFileAllocationList SSS_FileAllocationList = mestraFile.getSSS_FileAllocationList();
					// we write in the same order SSS File allocation CSCI 
					if (SSS_FileAllocationList != null) {

						Iterator<Map.Entry<String, CSCI_Allocation>>  Iter2 = SSS_FileAllocationList.iterator();
						while (Iter2.hasNext()) {

							Map.Entry<String, CSCI_Allocation> entryTwo = Iter2.next();
							CSCI_Allocation aCSCI_Allocation = entryTwo.getValue();

							strRT = new HSSFRichTextString(aCSCI_Allocation.getCSCI_Name());
							try {
								cell = row.createCell(cellIndex++);
								cell.setCellValue(strRT);
								cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightBoldCellStyle));    	
							}
							catch (RuntimeException e) {
								logger.log(Level.SEVERE , "error while setting Cell Value= " +  e.getLocalizedMessage());
							}
						}
					}
				}	
			}
		}
		if (mestraFile.IsSRS()) {
			for (Map.Entry<String, MestraStyle> entry : mestraStyles.getMestraStylesMap().entrySet()) {

				MestraStyle mestraStyle = entry.getValue(); 
				if (mestraStyle.isTraceability() == true) {
					// write the mandatory style
					String styleHeader = mestraStyle.getMestraStyleHeader();

					strRT = new HSSFRichTextString(styleHeader);
					try {
						cell = row.createCell(cellIndex++);
						cell.setCellValue(strRT);
						cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightBoldCellStyle));    	
					}
					catch (RuntimeException e) {
						logger.log(Level.SEVERE , "error while setting Cell Value= " +  e.getLocalizedMessage());
					}
				}
			}
		}
	}

	public void WriteNumberOfItems(MestraFile mestraFile) {

		HSSFRow row = sheet.createRow((short)rowIndex++);
		HSSFCell cell = row.createCell(0);
		HSSFRichTextString strRT = new HSSFRichTextString("Number of Items");
		cell.setCellValue(strRT);
		cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightCellStyle)); 

		cell = row.createCell(1);
		cell.setCellValue(GetNumberOfItems(mestraFile));
		cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightCellStyle)); 
	}


	protected int WriteBaseMarkers(MestraBase mestraBase,
			HSSFRow row,
			HSSFCell cell,
			int cellIndex) {

		// write the mestra identifier
		cell = row.createCell(cellIndex++);
		HSSFRichTextString strRTOne = new HSSFRichTextString(mestraBase.getIdentifier());
		cell.setCellValue(strRTOne);
		if (mestraBase.isDuplicated()) {
			// highlight in RED color
			cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightRedBackGroundCellStyle));
		}
		else {
			if (mestraBase.containsIllegalCharacters()) {
				// highlight in yellow
				cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightYellowBackGroundCellStyle));        		
			}
			else {
				cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightCellStyle));        		
			}
		}

		MestraStylesMap mestraStylesMap = mestraBase.getMestraStylesMap();
		/* Now, iterate over the map's contents, sorted by key. */
		for (Map.Entry<String, MestraStyle> entry : mestraStylesMap.getMestraStylesMap().entrySet()) {

			MestraStyle mestraStyle = entry.getValue();
			// first style mandatory and last style traceability
			if ((mestraStyle.isMandatory() == false) && (mestraStyle.isTraceability() ==  false)) {

				if (mestraStyle.getDisplayMestraStyle()) {
					
					if (mestraStyle.hasMethodLevelSafetyAttribute()) {
						cell = row.createCell(cellIndex++);
						HSSFRichTextString strRT = new HSSFRichTextString(mestraBase.getMestraValue(mestraStyle.getMestraStyleHeader()));
						cell.setCellValue(strRT);
						
						MethodLevelSafety methodLevelSafety = mestraBase.getMethodLevelSafety();
						if (methodLevelSafety.isOneLevelNotExisting() || methodLevelSafety.isOneMethodNotExisting() ) {
							cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightYellowBackGroundCellStyle));	        		

						} else {
							cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightCellStyle));
						}
						
					} else {
						// this style must be displayed
						cell = row.createCell(cellIndex++);
						HSSFRichTextString strRT = new HSSFRichTextString(mestraBase.getMestraValue(mestraStyle.getMestraStyleHeader()));
						cell.setCellValue(strRT);
						cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightCellStyle));
					}
				}
			}
		}


		/*		cell = row.createCell((short)cellIndex++);
		strRT = new HSSFRichTextString(mestraBase.getChangeHistory());
		cell.setCellValue(strRT);
		cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightCellStyle));

		cell = row.createCell((short)cellIndex++);
		if (mestraBase.isReleaseRevisionEmpty()) {
			strRT = new HSSFRichTextString("missing revision");
			cell.setCellValue(strRT);
			cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightYellowBackGroundCellStyle));
		}
		else {
			strRT = new HSSFRichTextString(mestraBase.getReleaseRevision());
			cell.setCellValue(strRT);
			if (mestraBase.hasReleaseRevisionMultipleMarkers()) {
				cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightYellowBackGroundCellStyle));	        		
			}
			else {
				cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightCellStyle));        		
			}
		}*/

		/*cell = row.createCell((short)cellIndex++);

		MethodLevelSafety methodLevelSafety = mestraBase.getMethodLevelSafety();
		if (methodLevelSafety.isMethodLevelSafetyEmpty()) { 
			strRT = new HSSFRichTextString("Method Level Safety empty");
			cell.setCellValue(strRT);
			cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightYellowBackGroundCellStyle));
		}
		else {
			strRT = new HSSFRichTextString(mestraBase.getStrMethodLevelSafety());
			cell.setCellValue(strRT);
			if (methodLevelSafety.isOneLevelNotExisting() || 
					methodLevelSafety.isOneMethodNotExisting()) {
				cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightYellowBackGroundCellStyle));
			}
			else {
				cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightCellStyle));
			}
		}*/
		return cellIndex;
	}

	private int WriteBaseMarkers(MestraBase mestraBase,
			HSSFRow row,
			int MarkerIndex) {

		int cellIndex = 0;
		HSSFCell cell = row.createCell(cellIndex++);
		cell.setCellValue(MarkerIndex++);
		cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightCellStyle));

		cellIndex = WriteBaseMarkers(mestraBase, row, cell, cellIndex);
		return cellIndex;
	}

	private String getProgrammeName(String FileName) {
		String ProgrammeName = "";
		if (FileName.indexOf("BEL") > 0) {
			ProgrammeName = "CANAC2";
		}
		if (FileName.indexOf("ECO") > 0) {
			ProgrammeName = "COOPANS";
		}
		if (FileName.indexOf("MAT") > 0) {
			ProgrammeName = "MATIAS"; 
		}
		if (FileName.indexOf("S2K") > 0) {
			ProgrammeName = "S2K2"; 
		}
		if (FileName.indexOf("DAT") > 0) {
			ProgrammeName = "DATMAS"; 
		}
		if (FileName.indexOf("FRS") > 0) {
			ProgrammeName = "FRESH"; 
		}
		return ProgrammeName;
	}

	private String getShortProgrammeName(String FileName) {
		String ShortProgrammeName = "";
		if (FileName.indexOf("BEL") > 0) {
			ShortProgrammeName = "BEL";
		}
		if (FileName.indexOf("ECO") > 0) {
			ShortProgrammeName = "ECO";
		}
		if (FileName.indexOf("MAT") > 0) {
			ShortProgrammeName = "MAT"; 
		}
		if (FileName.indexOf("S2K") > 0) {
			ShortProgrammeName = "S2K"; 
		}
		if (FileName.indexOf("DAT") > 0) {
			ShortProgrammeName = "DAT"; 
		}
		if (FileName.indexOf("FRS") > 0) {
			ShortProgrammeName = "FRS"; 
		}
		return ShortProgrammeName;
	}

	private String extractSMFdigitKey(String FileName) {
		// for DATMAS it can be 3 digits and a letter
		// example Dat055y
		int intThreeDigits = 0;
		String strThreeDigits = "";
		String strDATMASadditionalChar = "";
		for (int i = 0 ; i < FileName.length() ; i++) {
			if (Character.isDigit(FileName.charAt(i))) {
				intThreeDigits++;
				strThreeDigits += FileName.substring(i, i+1);
				if ((intThreeDigits == 3) && (getShortProgrammeName(FileName) == "DAT")) {
					strDATMASadditionalChar = FileName.substring(i+1, i+2);
				}
			}
		}
		if (intThreeDigits == 3) {
			if (getShortProgrammeName(FileName) == "DAT") {
				if (Character.isLetter(strDATMASadditionalChar.charAt(0))) {
					return strThreeDigits+strDATMASadditionalChar;
				}
			}
			return strThreeDigits;
		}
		return "";
	}

	private String getSMFKey(String FileName) {

		String SMFProgKey = getShortProgrammeName(FileName);
		String SMFdigitKey =  extractSMFdigitKey(FileName);
		return SMFProgKey+"-"+SMFdigitKey;
	}

	private int WriteBaseMarkers(MestraBase mestraBase,
			HSSFRow row,
			String FileName,
			int MarkerIndex) {

		int cellIndex = 0;
		HSSFCell cell = null;

		// first column contains an Marker Index which increments		
		cell = row.createCell(cellIndex++);
		cell.setCellValue(MarkerIndex++);
		cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightCellStyle));

		// next column contains the name of the Programme
		cell = row.createCell(cellIndex++);
		HSSFRichTextString strRT = new HSSFRichTextString(getProgrammeName(FileName));
		cell.setCellValue(strRT);
		cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightCellStyle));

		// next column contains the Key SMF id
		cell = row.createCell(cellIndex++);
		strRT = new HSSFRichTextString(getSMFKey(FileName));
		cell.setCellValue(strRT);
		cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightCellStyle));

		// next column contains the File Name
		cell = row.createCell(cellIndex++);
		strRT = new HSSFRichTextString(FileName);
		cell.setCellValue(strRT);
		cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightCellStyle));

		cellIndex = WriteBaseMarkers(mestraBase, row, cell, cellIndex);
		return cellIndex;
	}

	private void WriteSSSmarkers(MestraFiles mestraFiles,
			ConfigurationFileBaseReader  aConfiguration) {

		// write row with headers
		WriteHeader(mestraFiles, aConfiguration, MestraFileType.MestraFileTypeEnum.SSS);

		MestraSSS_Collection mestraSSStags = mestraFiles.getMestraSSStags();
		Iterator<MestraSSS> Iter = mestraSSStags.iterator();

		HSSFRow row = null;
		int MarkerIndex = 1;

		while (Iter.hasNext()) {
			MestraSSS mestraSSS = Iter.next();
			row = sheet.createRow((short)rowIndex++);

			//short cellIndex = WriteBaseMarkers((MestraBase) mestraSSS,row,MarkerIndex++);
			int cellIndex = WriteBaseMarkers((MestraBase) mestraSSS, row, mestraSSS.getShortFileName(), MarkerIndex++);

			HSSFCell cell = row.createCell(cellIndex++);
			CSCI_SSSRequirementAllocationList SSS_ReqAllocationList = mestraSSS.getRequirementAllocationList();

			int NbrAllocatedCSCI = 0;
			if (SSS_ReqAllocationList != null) {
				NbrAllocatedCSCI = SSS_ReqAllocationList.getCSCI_AllocationList().size();           
			}
			cell.setCellValue(NbrAllocatedCSCI);
			if (NbrAllocatedCSCI == 0) {
				cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightYellowBackGroundCellStyle));
			}
			else {
				cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightCellStyle));
			}

			CSCI_SSSFileAllocationList SSS_FileAllocationList = mestraFiles.getSSS_FileAllocationList();

			// we write in the same order SSS File allocation CSCI and Requirement Allocation CSCI
			if ( (SSS_FileAllocationList != null) && (SSS_ReqAllocationList != null) ) {

				Iterator<Map.Entry<String, CSCI_Allocation>> Iter1 = SSS_FileAllocationList.iterator();
				while (Iter1.hasNext()) {

					Map.Entry<String, CSCI_Allocation> entry = Iter1.next();
					CSCI_Allocation aCSCI_Allocation = entry.getValue();

					cell = row.createCell(cellIndex++);
					Iterator<String> Iter2 = SSS_ReqAllocationList.iterator();
					while (Iter2.hasNext()) {
						String strCSCI = Iter2.next();
						if (strCSCI.equalsIgnoreCase(aCSCI_Allocation.getCSCI_Name())) {
							HSSFRichTextString strRT = new HSSFRichTextString(strCSCI);
							cell.setCellValue(strRT);
							// if CSCI name not in the Configuration file then high light
							if (aCSCI_Allocation.isCSCI_NameExisting() == false) {
								cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightYellowBackGroundCellStyle));
							}
							else {
								cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightCellStyle));
							}
						}
					}
				}
			}
		}
	}


	private void WriteSSSmarkers(MestraFile mestraFile,
			ConfigurationFileBaseReader aConfiguration) {

		// write row with headers
		WriteHeader(mestraFile,aConfiguration);

		MestraSSS_Collection mestraSSStags = mestraFile.getMestraSSStags();
		Iterator<MestraSSS> Iter = mestraSSStags.iterator();

		HSSFRow row = null;
		int MarkerIndex = 1;

		while (Iter.hasNext()) {
			MestraSSS mestraSSS = Iter.next();
			//System.out.println("row Index = " + rowIndex);
			row = sheet.createRow((short)rowIndex++);

			// after the counter and the Mestra SSS identifier => write the set of Mestra Base markers in alpha order
			int cellIndex = WriteBaseMarkers((MestraBase) mestraSSS, row, MarkerIndex++);

			HSSFCell cellOne = row.createCell(cellIndex++);
			CSCI_SSSRequirementAllocationList SSS_ReqAllocationList = mestraSSS.getRequirementAllocationList();

			int NbrAllocatedCSCI = 0;
			if (SSS_ReqAllocationList != null) {
				NbrAllocatedCSCI = SSS_ReqAllocationList.getCSCI_AllocationList().size();			
			}
			cellOne.setCellValue(NbrAllocatedCSCI);
			if (NbrAllocatedCSCI == 0) {
				cellOne.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightYellowBackGroundCellStyle));
			}
			else {
				cellOne.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightCellStyle));
			}
			
			CSCI_SSSFileAllocationList SSS_FileAllocationList = mestraFile.getSSS_FileAllocationList();

			// we write in the same order SSS File allocation CSCI and Requirement Allocation CSCI
			if ( (SSS_FileAllocationList != null) && (SSS_ReqAllocationList != null) ) {

				Iterator<Map.Entry<String, CSCI_Allocation>> Iter1 = SSS_FileAllocationList.iterator();
				while (Iter1.hasNext()) {

					Map.Entry<String, CSCI_Allocation> entry = Iter1.next();
					CSCI_Allocation aCSCI_Allocation = entry.getValue();

					try {
						//System.out.println(cellIndex);
						HSSFCell cell = row.createCell(cellIndex++);
						Iterator<String> Iter2 = SSS_ReqAllocationList.iterator();
						while (Iter2.hasNext()) {
							String strCSCI = Iter2.next();
							if (strCSCI.equalsIgnoreCase(aCSCI_Allocation.getCSCI_Name())) {
								HSSFRichTextString strRT = new HSSFRichTextString(strCSCI);
								cell.setCellValue(strRT);
								// if CSCI name not in the Configuration file then high light
								if (aCSCI_Allocation.isCSCI_NameExisting() == false) {
									cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightYellowBackGroundCellStyle));
								}
								else {
									cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightCellStyle));
								}
							}
						}
					}
					catch (RuntimeException e) {
						logger.log(Level.SEVERE , e.getLocalizedMessage());
					}

				}
			}
		}
	}

	private void WriteSRSmarkers(MestraFile mestraFile,
			ConfigurationFileBaseReader aConfiguration) {

		// write row with headers
		WriteHeader(mestraFile,aConfiguration);

		MestraSRS_Collection mestraSRStags = mestraFile.getMestraSRStags();
		Iterator<MestraSRS> Iter = mestraSRStags.iterator();

		HSSFRow row = null;
		int MarkerIndex = 1;

		while (Iter.hasNext()) {
			MestraSRS mestraSRS = Iter.next();
			row = sheet.createRow((short)rowIndex++);

			int cellIndex = WriteBaseMarkers((MestraBase) mestraSRS, row, MarkerIndex++);

			// here we write on one row in contiguous cell the SSS references
			IdentifierList SSS_ReferencesList = mestraSRS.getSSS_ReferencesList();

			if (SSS_ReferencesList.isEmpty()) {
				HSSFCell cell = row.createCell(cellIndex++);
				HSSFRichTextString strRT = new HSSFRichTextString("Empty SSS References list");
				cell.setCellValue(strRT);
				cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightYellowBackGroundCellStyle));				
			}
			else {
				Iterator<String> Iter2 = SSS_ReferencesList.iterator();
				while (Iter2.hasNext()) {
					HSSFCell cell = row.createCell(cellIndex++);
					HSSFRichTextString strRT = new HSSFRichTextString(Iter2.next());
					cell.setCellValue(strRT);
					// if one reference = Req Derived then size = 1
					if ((SSS_ReferencesList.containsReqDerived()) && (SSS_ReferencesList.size()>1)) {
						cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightYellowBackGroundCellStyle));                   	
					}
					else {
						cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightCellStyle));
					}
				}
			}
		}
	}

	private void WriteSDDmarkers(MestraFile mestraFile,ConfigurationFileBaseReader  aConfiguration) {
		// write row with headers
		WriteHeader(mestraFile, aConfiguration);

		MestraSDD_Collection mestraSDDtags = mestraFile.getMestraSDDtags();
		Iterator<MestraSDD> Iter = mestraSDDtags.iterator();

		HSSFRow row = null;
		int MarkerIndex = 1;

		while (Iter.hasNext()) {
			MestraSDD mestraSDD = Iter.next();
			row = this.sheet.createRow((short)rowIndex++);

			int cellIndex = WriteBaseMarkers((MestraBase) mestraSDD, row, MarkerIndex++);

			IdentifierList implementedSRSrequirements = mestraSDD.getImplementedSRSrequirements();
			Iterator<String> Iter2 = implementedSRSrequirements.iterator();
			// the following limit is hard coded in the POI HSSF version 3.0
			final int ExcelMaxColumns = 255;
			/**
			 * 19th November 2009
			 * Cannot create more that 255 Columns in an EXCEL row
			 * added a limitation to ExcelMaxColums
			 */ 
			while (Iter2.hasNext() && (cellIndex < ExcelMaxColumns)) {
				HSSFCell cell = row.createCell(cellIndex++);
				HSSFRichTextString strRT = new HSSFRichTextString(Iter2.next());
				cell.setCellValue(strRT);
				cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightCellStyle));
			}
			/**
			 * 19th November 2009
			 * Cannot create more that 255 Columns in an EXCEL row
			 */ 
			if ((implementedSRSrequirements.size()) > ExcelMaxColumns) {
				row = this.sheet.createRow((short)rowIndex++);
				HSSFCell cell = row.createCell(1);
				HSSFRichTextString strRT = new HSSFRichTextString("Java Mestra cannot write all data: limitations to 255 columns");
				cell.setCellValue(strRT);
			}
		}
	}

	private void WriteTSmarkers(MestraFile mestraFile, ConfigurationFileBaseReader aConfiguration) {

		// write row with headers
		WriteHeader(mestraFile,aConfiguration);

		MestraTS_Collection mestraTStags = mestraFile.getMestraTStags();
		Iterator<MestraTS> Iter = mestraTStags.iterator();

		HSSFRow row = null;
		int MarkerIndex = 1;

		while (Iter.hasNext()) {
			MestraTS mestraTS = Iter.next();
			row = sheet.createRow((short)rowIndex++);

			int cellIndex = WriteBaseMarkers((MestraBase) mestraTS, row, MarkerIndex++);

			IdentifierList testedSRSrequirementsList = mestraTS.getTestedSRSrequirementsList();
			if (testedSRSrequirementsList.isEmpty()) {
				HSSFCell cell = row.createCell(cellIndex++);
				HSSFRichTextString strRT = new HSSFRichTextString("Empty SRS References list");
				cell.setCellValue(strRT);
				cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightYellowBackGroundCellStyle));				
			}
			else {

				Iterator<String> Iter2 = testedSRSrequirementsList.iterator();
				while (Iter2.hasNext()) {
					HSSFCell cell = row.createCell(cellIndex++);
					HSSFRichTextString strRT = new HSSFRichTextString(Iter2.next());
					cell.setCellValue(strRT);
					cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightCellStyle));
				}
			}
		}
	}

	private void WriteMFmarkers(MestraFile mestraFile,
			ConfigurationFileBaseReader  aConfiguration) {

		// write row with headers
		WriteHeader(mestraFile, aConfiguration);

		MestraMF_Collection mestraMFtags = mestraFile.getMestraMFtags();
		Iterator<MestraMF> Iter = mestraMFtags.iterator();

		HSSFRow row = null;
		int MarkerIndex = 1;

		while (Iter.hasNext()) {
			MestraMF mestraMF = Iter.next();
			row = sheet.createRow((short)rowIndex++);

			int cellIndex = WriteBaseMarkers((MestraBase) mestraMF, row, MarkerIndex++);

			HSSFCell cell = row.createCell(cellIndex++);
			HSSFRichTextString strRT = new HSSFRichTextString(mestraMF.getDesignParts());
			cell.setCellValue(strRT);
			cell.setCellStyle(cellStyles.getCellStyle(CellStylesEnum.ArialEightCellStyle));

		}
	}
}
