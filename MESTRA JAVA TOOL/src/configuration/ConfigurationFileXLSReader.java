package configuration;

import gui.StatusBar;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import poi.ExcelFile;

public class ConfigurationFileXLSReader extends ExcelFile {

	final static Logger logger = Logger.getLogger(ConfigurationFileXLSReader.class.getName()); 

	private StatusBar statusBar = null;
	private boolean ConfigurationOK = false;

	private MestraStylesMap mestraSSSStylesMap = null;

	private MestraStylesMap mestraSRSStylesMap = null;
	private MestraStylesMap mestraSDDStylesMap = null;
	private MestraStylesMap mestraMFStylesMap = null;
	private MestraStylesMap mestraTSStylesMap = null;
	
	private HSSFWorkbook workBook = null;
	private HSSFSheet workSheet = null;

	/**
	 * The following constructor is used to test Classes using Configuration
	 * without needing to use a dummy StatusBar
	 * 
	 * @param ExcelFilePath
	 */
	public ConfigurationFileXLSReader(String ExcelFilePath) {
		super(ExcelFilePath);
		ConstructorHelper (); 
	}

	public ConfigurationFileXLSReader(File excelFile) {

		super(excelFile);
		ConstructorHelper (); 
	}

	// Constructor
	public ConfigurationFileXLSReader(String ExcelFilePath, StatusBar statusBar) {
		super(ExcelFilePath);
		this.statusBar = statusBar;
		ConstructorHelper (); 
	}


	private void ConstructorHelper () {

		this.ConfigurationOK = false;
		if (statusBar != null) {
			workBook = open(statusBar);
		}
		else {
			workBook = open();
		}

		if (workBook != null) {
			String sheetName = "Config";
			workSheet = workBook.getSheet(sheetName) ;
			if (workSheet == null) {
				logger.info( "Configuration: Sheet with name: " + sheetName + " does not exist");
				this.ConfigurationOK = false;
			}
			else {
				//logger.info( "Configuration: Sheet with name: " + sheetName + " exists");
				ReadExcelRows(workSheet, statusBar);
				this.ConfigurationOK = true;
			}    
		}
		try {
			this.workBook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		close();
	}


	private boolean ReadOneExcelRow(HSSFRow row, int rowIndex) {

		boolean LastRow = false;

		Iterator<Cell> cells = row.cellIterator();
		if (cells.hasNext()){
			HSSFCell cell = (HSSFCell) cells.next();
			if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
				if  (cell.getCellType() != HSSFCell.CELL_TYPE_BLANK) {
					if (cell.getRichStringCellValue().getString().equalsIgnoreCase("CSCI names")) {
						//logger.info ( "Configuration: Row starting with CSCI Names");
						ReadCSCI_Names(row);
					}
					else {
						ReadCellsInRow(row, rowIndex);
					}
				}
				else {
					LastRow = true;
				}
			}
			else {
				LastRow = true;
			}
		}
		return LastRow;
	}

	public void ReadExcelRows(HSSFSheet sheet, StatusBar statusBar) {

		// set Maximum size of the progress bar
		if (statusBar != null) {
			statusBar.setProgressBarMaxValue(sheet.getLastRowNum());
		}

		// Iterate over each row in the sheet
		Iterator<Row> rows = sheet.rowIterator();
		boolean LastRow = false;
		int rowIndex = 0;

		while( rows.hasNext() && LastRow == false ) {           

			HSSFRow row = (HSSFRow) rows.next();
			//logger.info( "Configuration: Row: " + row.getRowNum() );
			LastRow = ReadOneExcelRow(row, rowIndex);

			// let the status bar progress
			if (statusBar != null) {
				statusBar.setProgressBarValue(rowIndex);
			}
			// increment rowIndex ... (if rowIndex == 0 then mandatory first Mestra Tag !!!)
			rowIndex = rowIndex + 1;
		}
		if (statusBar != null) {
			statusBar.setProgressBarValue(0);
		}
	}

	public void ReadExcelRows(HSSFSheet sheet) {

		// Iterate over each row in the sheet
		Iterator<Row> rows = sheet.rowIterator();
		boolean LastRow = false;
		int rowIndex = 0;

		while( rows.hasNext() && LastRow == false ) {           
			HSSFRow row = (HSSFRow) rows.next();
			//logger.info( "Configuration: Row: " + row.getRowNum() );
			LastRow = ReadOneExcelRow(row, rowIndex);
			rowIndex = rowIndex + 1;

		}    
	}

	private void ReadCSCI_Names(HSSFRow row) {

		this.known_CSCIs = new KnownCSCIs();
		// Iterate over each cell in the row and print out the cell's content
		Iterator<Cell> cells = row.cellIterator();
		boolean LastColumn = false;
		while( cells.hasNext() && LastColumn == false ) {
			HSSFCell cell = (HSSFCell) cells.next();
			if (cell.getCellType()==HSSFCell.CELL_TYPE_STRING) {
				if (cell.getRichStringCellValue().getString().equalsIgnoreCase("CSCI names")== false) {
					//logger.info( "Configuration: Read CSCI Names: "+cell.getRichStringCellValue().getString());
					this.known_CSCIs.add(cell.getRichStringCellValue().getString());
				}
			}
			else {
				LastColumn = true;
			}
		}
	}

	private void ReadCellsInRow(HSSFRow row, int rowIndex) {

		// Iterate over each cell in the row and print out the cell's content
		Iterator<Cell> cells = row.cellIterator();
		boolean LastColumn = false;
		while( cells.hasNext() && LastColumn == false ) {
			HSSFCell cell = (HSSFCell) cells.next();

			if (cell.getCellType()==HSSFCell.CELL_TYPE_STRING) {
				String CellContent = cell.getRichStringCellValue().getString();
				//logger.info(  "Configuration: Cell: " + cell.getCellNum()+ " : " + CellContent);

				if (CellContent.equalsIgnoreCase("SCN_SSS") || CellContent.equalsIgnoreCase("SSS")) {
					//logger.info(  "Configuration: Row starting with SSS or SCN_SSS" );
					if (this.configurationMainMap.hasSSSMap() == false){

						this.mestraSSSStylesMap = new MestraStylesMap();
						this.configurationMainMap.createMap("SSS", this.mestraSSSStylesMap);

					}
					InitMestraStyles(row, this.mestraSSSStylesMap, "SSS", rowIndex);
				}
				if (CellContent.equalsIgnoreCase("SCN_SRS") || CellContent.equalsIgnoreCase("SRS")) {
					//logger.info( "Configuration: Row starting with SCN SRS or SRS" );
					if (this.configurationMainMap.hasSRSMap() == false){

						this.mestraSRSStylesMap = new MestraStylesMap();
						this.configurationMainMap.createMap("SRS", this.mestraSRSStylesMap);

					}
					InitMestraStyles(row, this.mestraSRSStylesMap, "SRS", rowIndex);
				}
				if (CellContent.equalsIgnoreCase("SDD")) {
					//logger.info( "Configuration: Row starting with SDD" );
					if (this.configurationMainMap.hasSDDMap() == false){

						this.mestraSDDStylesMap = new MestraStylesMap();
						this.configurationMainMap.createMap("SDD", this.mestraSDDStylesMap);

					}
					InitMestraStyles(row, this.mestraSDDStylesMap, "SDD", rowIndex);
				}
				if (CellContent.equalsIgnoreCase("MF")) {
					//logger.info( "Configuration: Row starting with MF" );
					if (this.configurationMainMap.hasMFMap() == false){

						this.mestraMFStylesMap = new MestraStylesMap();
						this.configurationMainMap.createMap("MF", this.mestraMFStylesMap);

					}
					InitMestraStyles(row, this.mestraMFStylesMap, "MF", rowIndex);
				}
				if (CellContent.equalsIgnoreCase("TS")) {
					//logger.info( "Configuration: Row starting with TS" );
					if (this.configurationMainMap.hasTSMap() == false){

						this.mestraTSStylesMap = new MestraStylesMap();
						this.configurationMainMap.createMap("TS", this.mestraTSStylesMap);

					}
					InitMestraStyles(row, this.mestraTSStylesMap ,"TS", rowIndex);
				}  

				if (CellContent.equalsIgnoreCase("IADT")) {
					//logger.info( "Configuration: row starting with IADT");
					if (methodLevelSafety == null) {
						methodLevelSafety = new MethodLevelSafety();
					}
					InitMethod(row,methodLevelSafety);
				}

				if (CellContent.equalsIgnoreCase("Level")) {
					//logger.info ( "Configuration: row starting with Level");
					if (methodLevelSafety == null) {
						methodLevelSafety = new MethodLevelSafety();
					}
					InitLevel(row,methodLevelSafety);
				}
			}
			else {
				LastColumn = true;
			}
		}
	}

	private void InitMethod(HSSFRow row,MethodLevelSafety methodLevelSafety) {

		// Iterate over each cell in the row and print out the cell's content
		Iterator<Cell> cells = row.cellIterator();
		boolean LastColumn = false;
		while( cells.hasNext() && LastColumn == false ) {
			HSSFCell cell = (HSSFCell) cells.next();
			if (cell.getCellType()==HSSFCell.CELL_TYPE_STRING) {
				methodLevelSafety.addMethod(cell.getRichStringCellValue().getString());
			}
			else {
				LastColumn = true;
			}
		}
	}

	private void InitLevel(HSSFRow row,MethodLevelSafety methodLevelSafety) {

		// Iterate over each cell in the row and print out the cell's content
		Iterator<Cell> cells = row.cellIterator();
		boolean LastColumn = false;
		while( cells.hasNext() && LastColumn == false ) {
			HSSFCell cell = (HSSFCell) cells.next();
			if (cell.getCellType()==HSSFCell.CELL_TYPE_STRING) {
				methodLevelSafety.addLevel(cell.getRichStringCellValue().getString());
			}
			else {
				LastColumn = true;
			}
		}
	}

	private void InitMestraStyles(HSSFRow row, MestraStylesMap mestraStyles, String mestraFileType, int rowIndex) {

		String mestraStyleHeader = "";
		String mestraStyle = "";
		String strDisplay = "";

		Map<String , Boolean> attributesMap = new TreeMap<String, Boolean>();
		// rowIndex = 0 name of the file + version - rowIndex = 1 CSCI names => rowIndex = 2 start of mestra styles
		if (rowIndex == 2) {
			attributesMap.put("mandatory", true);
		} else {
			attributesMap.put("mandatory", false);
		}

		// Iterate over each cell in the row and print out the cell's content
		Iterator<Cell> cells = row.cellIterator();
		boolean LastColumn = false;
		while( cells.hasNext() && LastColumn == false ) {
			HSSFCell cell = (HSSFCell) cells.next();

			if (cell.getCellType()==HSSFCell.CELL_TYPE_STRING) {
				// getCellNum starts vaue = 0 - hence if value = 1 it is the second column
				switch (cell.getColumnIndex()) {
				case 1 : 
					mestraStyleHeader = cell.getRichStringCellValue().getString(); 
					//logger.info ( " mestra style header = " + mestraStyleHeader);
					break;

				case 2 : 
					mestraStyle =  cell.getRichStringCellValue().getString();
					//logger.info ( " mestra style  = " + mestraStyle);
					break;

				case 3 : 
					strDisplay = cell.getRichStringCellValue().getString();;
					if (strDisplay.equalsIgnoreCase("mandatory")) {
						//logger.info ( " display = mandatory ");
						attributesMap.put("display", true);

					} else {
						attributesMap.put("display", false);
					}
					break;
				}
			}
			else {
				LastColumn = true;
			}
		}
		mestraStyles.add(new MestraStyle(mestraFileType, mestraStyle, mestraStyleHeader, attributesMap, rowIndex));
	}


	/**
	 * @param methodLevelSafety the methodLevelSafety to set
	 */
	public void setMethodLevelSafety(MethodLevelSafety methodLevelSafety) {
		this.methodLevelSafety = methodLevelSafety;
	}

	/**
	 * @return the configurationOK
	 */
	public boolean isConfigurationOK() {
		return this.ConfigurationOK;
	}


}