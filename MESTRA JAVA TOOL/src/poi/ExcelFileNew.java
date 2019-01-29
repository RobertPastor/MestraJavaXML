package poi;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class ExcelFileNew {

	final static Logger logger = Logger.getLogger(ExcelFileNew.class.getName()); 

	private FileInputStream fin = null;
	private POIFSFileSystem poifsFile = null;
	private HSSFWorkbook workBook = null;

	/**
	 * @param args
	 */

	public HSSFWorkbook OpenExcelFile(String ExcelFilePath){


		logger.info( "ExcelFile: Open Excel file: " + ExcelFilePath);
		try {
			this.fin = new FileInputStream( ExcelFilePath );
			this.poifsFile = new POIFSFileSystem(fin);
			this.workBook = new HSSFWorkbook( poifsFile , true);
		}
		catch (FileNotFoundException e1) {
			logger.log(Level.SEVERE , "ExcelFile: FileInputStream does not exist: "+ ExcelFilePath + " error " + e1.getMessage());
			return null;
		}
		catch (IOException e2) {
			logger.log(Level.SEVERE , "ExcelFile: IOException raised: " + e2.getMessage());
			return null;
		}
		return workBook;
	}

	public boolean CloseExcelFile() {
		try {
			if (this.fin != null) {
				this.fin.close();
			}
			if (this.workBook!= null) {
				this.workBook.close();
			}
			if (this.poifsFile != null) {
				this.poifsFile.close();
			}
		}
		catch (IOException e) {return false;}
		return true;
	}

	public HSSFSheet CreateSheet(HSSFWorkbook wb, String SheetName) {
		// create a sheet with the provided name 
		// in the provided workbook
		return(wb.createSheet(SheetName));
	}

	public boolean CreateOutputExcelFile(String ExcelFilePath,HSSFWorkbook wb) {
		FileOutputStream fileOut = null;

		try {
			fileOut = new FileOutputStream(ExcelFilePath);
		}
		catch (FileNotFoundException e) {
			return false;
		}
		try {
			wb.write(fileOut);
		}
		catch (IOException e) {
			try {
				fileOut.close();
			} catch (IOException e1) {
				e.printStackTrace();
			}
			return false;
		}
		try {
			fileOut.close();
		} 
		catch (IOException e2) {
			return false;
		}
		return true;
	}
}
