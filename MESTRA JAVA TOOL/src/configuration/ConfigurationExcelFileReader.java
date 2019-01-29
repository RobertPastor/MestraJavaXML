package configuration;

import gui.StatusBar;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import poi.ExcelFile;
import FolderBrowser.FolderBrowser;

/**
 * This class reads an EXCEL configuration file.
 * @author PASTOR Robert
 * @since July 2016
 *
 */
public class ConfigurationExcelFileReader extends ExcelFile {

	final static Logger logger = Logger.getLogger(ConfigurationExcelFileReader.class.getName()); 

    private String ExcelConfigurationFilePath = "";
    private boolean ConfigurationOK = false;
    private StatusBar statusBar = null;
    
	private KnownCSCIs known_CSCIs = null;
	
	private HSSFWorkbook workBook = null;
	private HSSFSheet workSheet = null;
    
	public ConfigurationExcelFileReader() {
		
		super();
		this.ConfigurationOK = false;

		logger.info("constructor");
		this.setExcelConfigurationFilePath(getMestraConfigurationFilePath());
		if (checkConfigurationFileExists()) {
			super.setExcelFilePath(this.ExcelConfigurationFilePath);
			this.ConfigurationOK = true;
			ConstructorHelper();
		}
	}
	
	private void ConstructorHelper () {

		ConfigurationOK = false;
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
				logger.log(Level.SEVERE , "Configuration: Sheet with name: " + sheetName + " does not exist");
				ConfigurationOK = false;
			}
			else {
				logger.info( "Configuration: Sheet with name: " + sheetName + " exists");
				ReadExcelRows(workSheet, statusBar);
				ConfigurationOK = true;
			}    
		}
		try {
			this.workBook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// close the EXCEL file
		close();
	}
	
	public void ReadExcelRows(HSSFSheet sheet,StatusBar statusBar) {

		// set Maximum size of the progress bar
		if (statusBar != null) {
			statusBar.setProgressBarMaxValue(sheet.getLastRowNum());
		}

		// Iterate over each row in the sheet
		Iterator<Row> rows = sheet.rowIterator();
		boolean LastRow = false;
		int RowIndex = 0;

		while( rows.hasNext() && LastRow == false ) {           
			// let the status bar progress
			if (statusBar != null) {
				statusBar.setProgressBarValue(RowIndex++);
			}

			HSSFRow row = (HSSFRow) rows.next();
			LastRow = ReadOneExcelRow(row);

		}
		if (statusBar != null) {
			statusBar.setProgressBarValue(0);
		}
	}
	
	
	private boolean ReadOneExcelRow(HSSFRow row) {

		boolean LastRow = false;

		Iterator<Cell> cells = row.cellIterator();
		if (cells.hasNext()){
			HSSFCell cell = (HSSFCell) cells.next();
			if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
				if  (cell.getCellType() != HSSFCell.CELL_TYPE_BLANK) {
					if (cell.getRichStringCellValue().getString().equalsIgnoreCase("CSCI names")) {
						ReadCSCI_Names(row);
					}
					else {
						ReadCellsInRow(row);
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
	
	private void ReadCellsInRow(HSSFRow row) {

		// Iterate over each cell in the row and print out the cell's content
		Iterator<Cell> cells = row.cellIterator();
		boolean LastColumn = false;
		int columnIndex = 0;
		while( cells.hasNext() && LastColumn == false ) {
			HSSFCell cell = (HSSFCell) cells.next();

			if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
				String CellContent = cell.getRichStringCellValue().getString();
				//logger.info( " Cell content " + CellContent );
				if (columnIndex == 0) {
					logger.info(" it is the name of the MESTRA style " + CellContent);
					columnIndex ++;
				}
				
			}
		}
	}
	
	private void ReadCSCI_Names(HSSFRow row) {

		known_CSCIs = new KnownCSCIs();
		// Iterate over each cell in the row and print out the cell's content
		Iterator<Cell> cells = row.cellIterator();
		boolean LastColumn = false;
		while( cells.hasNext() && LastColumn == false ) {
			HSSFCell cell = (HSSFCell) cells.next();
			if (cell.getCellType()==HSSFCell.CELL_TYPE_STRING) {
				if (cell.getRichStringCellValue().getString().equalsIgnoreCase("CSCI names")== false) {
					known_CSCIs.add(cell.getRichStringCellValue().getString());
				}
			}
			else {
				LastColumn = true;
			}
		}
	}
	
    private boolean checkConfigurationFileExists() {

        //File file = getFile(ExcelConfigurationFilePath);
        File file = new File(ExcelConfigurationFilePath);
        if (file.exists() == false) {
            String msg = "file: " + ExcelConfigurationFilePath + " does not exist";
            logger.log(Level.SEVERE , msg);
            return false;
/*            JOptionPane.showMessageDialog(null, "file: "+ExcelConfigurationFilePath+" does not exist \n please use Configuration file button", 
                    "Error Configuration file not found", JOptionPane.ERROR_MESSAGE);
*/        }
        else {
            this.ExcelConfigurationFilePath = file.getAbsolutePath();
            logger.info("configuration File path = " + this.ExcelConfigurationFilePath);
            return true;
        }
    }


	private String getMestraToolRootDirectory() {
		String root = "";
		File[] roots = File.listRoots();
		for(int i = 0 ; i < roots.length ; i++) {
			File f = roots[i];
			String name = f.toString();
			logger.info("Platform roots: "+name);
			File mestraToolDir = new File(name+"Mestra Tool");
			if (mestraToolDir.exists()) {
				logger.info(mestraToolDir.getAbsolutePath()+".....exists!!");
				// we return the first one found
				return mestraToolDir.getAbsolutePath();
			}
		}
		return root;
	}

	private String getMestraConfigurationFilePath() {
		/**
		 * 
		 * new method is to search a file with a specific pattern in the file name
		 */
		String MestraConfigurationFilePath = "";
		String rootPath = getMestraToolRootDirectory();

		FolderBrowser folderBrowser = new FolderBrowser(rootPath);
		String[] fileExtensions = {"xls", "xml"};
		File[] files = folderBrowser.GetFilesInFolder(fileExtensions , false);
		for (int i=0 ; i < files.length ; i++) {
			File file = files[i];
			if (file.getName().indexOf("Mestra_Tool_V2")==0 ) {
				logger.info( file.getName() );
				MestraConfigurationFilePath = file.getAbsolutePath();
				logger.info("Mestra Configuration file found: "+file.getAbsolutePath());
			}
		}
		return MestraConfigurationFilePath;
	}


	public String getExcelConfigurationFilePath() {
		return ExcelConfigurationFilePath;
	}


	public void setExcelConfigurationFilePath(String excelConfigurationFilePath) {
		ExcelConfigurationFilePath = excelConfigurationFilePath;
	}

	public boolean isConfigurationOK() {
		return ConfigurationOK;
	}

	public void setConfigurationOK(boolean configurationOK) {
		ConfigurationOK = configurationOK;
	}
}
