package poi;

import gui.StatusBar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import configuration.ConfigurationFileBaseReader;

public class ExcelFile extends ConfigurationFileBaseReader {

	final static Logger logger = Logger.getLogger(ExcelFile.class.getName()); 

	private FileInputStream fin = null;
	private POIFSFileSystem poifsFile = null;
	private HSSFWorkbook workBook = null;
	
	public ExcelFile(File file) {
		super(file);
		this.fin = null;
		this.configurationFilePath = file.getAbsolutePath();
		setExcelFileDirectory();
	}

	// Constructor
	public ExcelFile (String aExcelFilePath) {
		super(aExcelFilePath);
		this.fin = null;
	}

	public ExcelFile() {
		super();
		this.fin = null;
		this.configurationFilePath = "";
	}

	public void setExcelFilePath(String excelFilePath) {
		this.configurationFilePath = excelFilePath;
	}

	/**
	 * Opens an Excel File
	 * 
	 * @param args
	 */
	public HSSFWorkbook open(StatusBar statusBar) {

		if (statusBar != null) {
			statusBar.setMessage("Configuration: Open Excel file: " + this.configurationFilePath);
		}

		try {
			fin = new FileInputStream(this.configurationFilePath);
			statusBar.setMessage("Configuration: FileInputStream: " + this.fin.getFD().toString());

			poifsFile = new POIFSFileSystem(fin);
			statusBar.setMessage("Configuration: POIFSFileSystem: " + poifsFile.getShortDescription());

			workBook = new HSSFWorkbook( poifsFile , false);
			statusBar.setMessage("Configuration: HSSFWorkbook: "+workBook.toString());
			statusBar.setMessage("Configuration: opening configuration file OK!");
		}
		catch (FileNotFoundException e1) {
			//String strMessage = "Configuration file: " + ExcelFilePath + " File Not Found Exception " + e1.toString();
			String strMessage = "Configuration file: " + this.configurationFilePath + " File Not Found Exception " ;
			statusBar.setMessage(strMessage);

			JOptionPane.showMessageDialog(null, strMessage , "File Not Found Exception", JOptionPane.ERROR_MESSAGE);
			logger.log(Level.SEVERE , "Configuration: FileInputStream does not exist: "+ this.configurationFilePath + " error " + e1.getMessage());
			
			
			return null;
		}
		catch (IOException e2) {
			String strMessage = "Configuration file: " + this.configurationFilePath + " IO Exception raised " + e2.toString();
			statusBar.setMessage(strMessage);

			JOptionPane.showMessageDialog(null, strMessage , "IO Exception while opening configuration file", JOptionPane.ERROR_MESSAGE);
			logger.log(Level.SEVERE , "Configuration: IOException raised: " + e2.getMessage());

			return null;
		}
		catch (SecurityException e3) {
			String strMessage = "Configuration file: " + this.configurationFilePath + " Security Exception raised " + e3.toString();
			statusBar.setMessage(strMessage);

			JOptionPane.showMessageDialog(null, strMessage , "IO Exception while opening configuration file", JOptionPane.ERROR_MESSAGE);

			logger.log(Level.SEVERE , "Configuration: IOException raised: " + e3.getMessage());
			return null;

		}
		catch (IllegalArgumentException e4) {
			String strMessage = "Configuration file: " + this.configurationFilePath + " Illegal Argument Exception raised (not an EXCEL File!!!) " + e4.toString();
			statusBar.setMessage(strMessage);

			JOptionPane.showMessageDialog(null, strMessage , "Illegal Argument Exception while opening configuration file", JOptionPane.ERROR_MESSAGE);

			logger.log(Level.SEVERE , "Configuration: IOException raised: " + e4.getMessage());
			return null;
		}
		return workBook;
	}

	public HSSFWorkbook open(){

		logger.log(Level.INFO , "ExcelFile: Open Excel file: " + this.configurationFilePath);
		try {
			this.fin = new FileInputStream( this.configurationFilePath );
			poifsFile = new POIFSFileSystem(fin);
			workBook = new HSSFWorkbook( poifsFile , true);
		}
		catch (FileNotFoundException e1) {

			logger.log(Level.SEVERE , " FileInputStream does not exist: "+ this.configurationFilePath + " error " + e1.getMessage());
			return null;
		}
		catch (IOException e2) {

			logger.log(Level.SEVERE , "ExcelFile: IOException raised: " + e2.getMessage());
			return null;
		}
		return workBook;
	}

	/**
	 * Closes the EXCEL File
	 * @return true or false
	 */
	public boolean close() {
		try {
			if (this.fin != null) {
				this.fin.close();
			}
			if (this.workBook != null) {
				this.workBook.close();
			}
			if (this.poifsFile != null) {
				this.poifsFile.close();
			}
		}
		catch (IOException e) {
			return false;
		}
		return true;
	}

	public HSSFSheet createSheet(HSSFWorkbook wb, String SheetName) {
		// create a sheet with the provided name 
		// in the provided workbook
		return (wb.createSheet(SheetName));
	}

	public boolean createOutputExcelFile(String ExcelFilePath, HSSFWorkbook wb) {
		FileOutputStream fileOut = null;

		try {
			fileOut = new FileOutputStream(ExcelFilePath);
		}
		catch (FileNotFoundException e1) {
			return false;
		}
		try {
			wb.write(fileOut);
			fileOut.close();
		}
		catch (IOException e2) {
			try {
				fileOut.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return false;
		}
		return true;
	}


}
