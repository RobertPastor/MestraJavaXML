package poi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import mestra.MestraFile;
import mestra.MestraFiles;
import mestra.MestraScenario;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import poi.TraceabilityOutputSheet.TraceabilitySheetType;
import configuration.ConfigurationFileBaseReader;

/**
 * This class manages an EXCEL output file
 * @author PASTOR Robert
 *
 */
public class ExcelOutputFile {

	final static Logger logger = Logger.getLogger(ExcelOutputFile.class.getName()); 

	private String OutputFileXLSpath = "";	
	private HSSFWorkbook wb = null;
	private FileOutputStream fileOut = null;
	
	public ExcelOutputFile () {
		OutputFileXLSpath = "";
	}

	private String ComputeTraceabilitySheetName(MestraFiles mestraFiles,
			TraceabilitySheetType sheetType) {

		String SheetName = "Trace_";
		MestraScenario mestraScenario = mestraFiles.getMestraScenario();

		switch (mestraScenario.getMestraScenarioEnum()) {
		case trace_SSS_SRS:
			switch (sheetType) {
			case UpDown:
				SheetName = SheetName + "SSS_SRS_" + mestraFiles.getTraceability_CSCI();
				break;
			case DownUp:
				SheetName = SheetName + "SRS_SSS_" + mestraFiles.getTraceability_CSCI();
				break;			
			}
			break;
		case trace_SRS_MF:
			switch (sheetType) {
			case UpDown:
				SheetName = SheetName + "SRS_MF";
				break;
			case DownUp:
				SheetName = SheetName + "MF_SRS";
				break;
			}	
			break;
		case trace_SRS_SDD:
			switch (sheetType) {
			case UpDown:
				SheetName = SheetName + "SRS_SDD";
				break;
			case DownUp:
				SheetName = SheetName + "SDD_SRS";
				break;
			}	
			break;

		case trace_SRS_TS:
			switch (sheetType) {
			case UpDown:
				SheetName = SheetName + "SRS_TS";
				break;
			case DownUp:
				SheetName = SheetName + "TS_SRS";
				break;
			}	
			break;

		case trace_All:
			SheetName = "trace_All";
			break;

		default:
			logger.log(Level.SEVERE , "ERROR in OutputFileXL: ComputeTraceabilitySheetName: should not achieve here!");
			break;
		}
		logger.info( "Excel Output File: Sheet Name: "+SheetName); 
		return SheetName;
	}


	public boolean WriteTraceabilityResults(MestraFiles mestraFiles,
			ConfigurationFileBaseReader aConfiguration) {

		// Create a New workbook
		wb = new HSSFWorkbook();
		
		//=========================================
		// create a ReadMe Sheet
		MestraOutputSheet readMeSheet = new MestraOutputSheet(wb, "ReadMe");
		// write tool version
		readMeSheet.WriteToolVersion();
		// write the files name in the sheet 
		readMeSheet.WriteFilesNames(mestraFiles);

		// we build two other sheets : 
		// ========================================
		// one from upstream towards downstream
		// the other one from downstream towards upstream
		String SheetNameUpDown = ComputeTraceabilitySheetName(mestraFiles, TraceabilitySheetType.UpDown);
		// create a sheet with the short filename
		TraceabilityOutputSheet outputSheetUpDown = new TraceabilityOutputSheet(wb, TraceabilitySheetType.UpDown, SheetNameUpDown);
		// write trace-ability results from Up to Down
		outputSheetUpDown.WriteTraceabilityMarkers(mestraFiles, aConfiguration);
		
		// ========================================
		String SheetNameDownUp = ComputeTraceabilitySheetName(mestraFiles, TraceabilitySheetType.DownUp);
		TraceabilityOutputSheet outputSheetDownUp = new TraceabilityOutputSheet(wb, TraceabilitySheetType.DownUp, SheetNameDownUp);

		// write trace-ability results from Down to Up
		outputSheetDownUp.WriteTraceabilityMarkers(mestraFiles, aConfiguration);

		// as of 4th August 2008: result file created in the folder
		// where sits the 1st provided files in the analysis
		OutputFileXLSpath = ComputeTraceabilityFileName(mestraFiles);
		return (CreateOutputExcelFile(OutputFileXLSpath, wb));

	}

	public boolean WriteMultipleMestraFilesResults(MestraFiles mestraFiles,
			ConfigurationFileBaseReader aConfiguration) {

		// Create a New workbook
		wb = new HSSFWorkbook();
		String SheetName = "Multiple Mestra Files";
		String DataDrillSheetName = "DataDrillSheet";

		// create a sheet with the short filename
		MestraOutputSheet outputSheetXL = new MestraOutputSheet(wb,SheetName);
		MestraOutputSheet dataDrillOutputSheetXL = new MestraOutputSheet(wb,DataDrillSheetName);

		// write tool version
		outputSheetXL.WriteToolVersion();
		// we do not write the Tool Version in the DataDrill Output Sheet
		// as this sheet is loaded into DataDrill

		// write the file names in the sheet on the first rows
		outputSheetXL.WriteFilesNames(mestraFiles);

		outputSheetXL.WriteMarkers(mestraFiles, aConfiguration);
		dataDrillOutputSheetXL.WriteDataDrillMarkers(mestraFiles, aConfiguration);

		// write the errors
		ErrorWarningSheetXL errorSheetXL = new ErrorWarningSheetXL(wb,"errors");

		errorSheetXL.WriteToolVersion();
		errorSheetXL.WriteFilesNames(mestraFiles);
		errorSheetXL.WriteErrorsWarnings(mestraFiles);

		// 4th August 2008 : traceability results are provided in the folder 
		// where sits the first file of the Mestra Files set
		OutputFileXLSpath = ComputeTraceabilityFileName(mestraFiles);
		return (CreateOutputExcelFile(OutputFileXLSpath, wb));
	}

	public boolean WriteResults(MestraFile mestraFile, 
			ConfigurationFileBaseReader configuration) {

		// Create a New workbook
		wb = new HSSFWorkbook();
		String SheetName = mestraFile.getShortFileName();

		//=========================================
		// create a ReadMe Sheet
		MestraOutputSheet readMeSheet = new MestraOutputSheet(wb,	"ReadMe");
		// write tool version
		readMeSheet.WriteToolVersion();
		// write the file name in the sheet on the first row
		readMeSheet.WriteFileName(mestraFile);
		// write number of items found
		readMeSheet.WriteNumberOfItems(mestraFile);

		//=====================================================
		// create a sheet with the short filename
		MestraOutputSheet outputSheetXL = new MestraOutputSheet(wb,	SheetName);

		// write the markers
		outputSheetXL.WriteMarkers(mestraFile,configuration);

		//=====================================================
		// write the errors
		ErrorWarningSheetXL errorSheetXL = new ErrorWarningSheetXL(wb,"errors");
		errorSheetXL.WriteErrorsWarnings(mestraFile);

		// 25 July 2008 : output file is created in the folder where the source Word Doc is located
		//OutputFileXLSpath = ComputeXLOutputFileName(aConfiguration,mestraFile);
		OutputFileXLSpath = ComputeXLOutputFileName(mestraFile);

		logger.info ( "OutputFileXL: compute XLS File Name: "+OutputFileXLSpath);
		return (CreateOutputExcelFile(OutputFileXLSpath,wb));
	}

	private String getDirectory (String AnalysedWordFilePath) {

		File fileDir = null;
		File file = null;
		String strParentDirFilePath = "/";
		try {

			file = new File(AnalysedWordFilePath);
			if (file.exists()) {
				fileDir = file.getParentFile();
				if (fileDir.isDirectory()) {
					logger.info( "Excel File: Directory: "+fileDir.getCanonicalPath());
					strParentDirFilePath = fileDir.getCanonicalPath();
				}
			}
		}
		catch (IOException e1) {
			return strParentDirFilePath;
		}
		catch (NullPointerException e) {
			return strParentDirFilePath;
		}
		return strParentDirFilePath;
	}

	private static String getCurrentDateTime() {

		Calendar cal = Calendar.getInstance(TimeZone.getDefault()); 
		String DATE_FORMAT = "dd_MMM_yyyy_HH_mm_ss"; 
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(DATE_FORMAT);
		sdf.setTimeZone(TimeZone.getDefault()); 
		String currentTime = sdf.format(cal.getTime());
		return currentTime;

	}

	private String ComputeTraceabilityFileName(MestraFiles mestraFiles) {

		String XLOutputFileName = "d:"+File.separator;
		if (mestraFiles.size()>0) {

			String WordFilePath = mestraFiles.first().getLongFileName();
			XLOutputFileName = getDirectory(WordFilePath);
			logger.info( "Excel Output File: directory file path: "+XLOutputFileName);

			XLOutputFileName = XLOutputFileName + File.separator + "Java Mestra Tool_";
			XLOutputFileName = XLOutputFileName + ComputeTraceabilitySheetName(mestraFiles,TraceabilitySheetType.UpDown);
			XLOutputFileName = XLOutputFileName + "_" + getCurrentDateTime();

			XLOutputFileName = XLOutputFileName + ".xls";
		}
		return XLOutputFileName;
	}

	private String ComputeXLOutputFileName(MestraFile analysedFileName) {

		if (analysedFileName.getLongFileName().length()>0) {

			String WordFilePath = analysedFileName.getLongFileName();
			String XLOutputFileName = getDirectory(WordFilePath);
			logger.info( "Excel File: directory file path: "+XLOutputFileName);

			if (WordFilePath.endsWith(".doc")) {

				logger.info( "Output EXCEL File: file name ends with .doc");
				XLOutputFileName = XLOutputFileName + File.separator + "Java Mestra Tool_";
				XLOutputFileName = XLOutputFileName + analysedFileName.getShortFileNameWithoutExtension();

				XLOutputFileName = XLOutputFileName + "_" + getCurrentDateTime();
				XLOutputFileName = XLOutputFileName + ".xls";
			}
			else {
				XLOutputFileName = XLOutputFileName + File.separator + "Java Mestra Tool_";
				XLOutputFileName = XLOutputFileName + analysedFileName.getShortFileNameWithoutExtension();

				XLOutputFileName = XLOutputFileName + "_" + getCurrentDateTime();
				XLOutputFileName = XLOutputFileName + ".xls";
			}
			return XLOutputFileName;
		}
		else {
			return "D:/";
		}
	}


	/**
	 * This method creates an EXCEL File.
	 * @param ExcelFilePath
	 * @param wb
	 * @return
	 */

	private boolean CreateOutputExcelFile(String ExcelFilePath,HSSFWorkbook wb) {

		logger.info( "OutputExcelFile: ExcelFilePath: "+ExcelFilePath);
		
		try {
			fileOut = new FileOutputStream(ExcelFilePath);
		}
		catch (FileNotFoundException e1) {
			logger.log(Level.SEVERE , "OutputsXL: ERROR while creating result file: " + ExcelFilePath + " " + e1.getMessage());
			return false;
		}
		try {
			wb.write(fileOut);
			fileOut.close();
			logger.info( "OutputsXL: result file: " + ExcelFilePath + " created.... end...");
		}
		catch (IOException e2) {
			logger.log(Level.SEVERE , "OutputsXL: ERROR while writing result file: " + ExcelFilePath + " " + e2.getMessage());
			return false;
		}
		// throw a Modal Dialog to inform about creation of the EXCEL result file

		JOptionPane.showMessageDialog(null, 
				"file: "+ExcelFilePath+" created correctly!", 
				"Mestra Result EXCEL File created!", JOptionPane.INFORMATION_MESSAGE);

		return true;
	}

	/**
	 * @return the outputFileXLSpath
	 */
	public String getOutputFileXLSpath() {
		return OutputFileXLSpath;
	}


	/*   

    In Excel a comment is a kind of a text shape, so inserting a comment is very similar to placing a text box in a worksheet: 

        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("Cell comments in POI HSSF");

        // Create the drawing patriarch. This is the top level container for all shapes including cell comments.
        HSSFPatriarch patr = sheet.createDrawingPatriarch();

        //create a cell in row 3
        HSSFCell cell1 = sheet.createRow(3).createCell((short)1);
        cell1.setCellValue(new HSSFRichTextString("Hello, World"));

        //anchor defines size and position of the comment in worksheet
        HSSFComment comment1 = patr.createComment(new HSSFClientAnchor(0, 0, 0, 0, (short)4, 2, (short) 6, 5));

         // set text in the comment
        comment1.setString(new HSSFRichTextString("We can set comments in POI"));

        //set comment author.
        //you can see it in the status bar when moving mouse over the commented cell
        comment1.setAuthor("Apache Software Foundation");

        // The first way to assign comment to a cell is via HSSFCell.setCellComment method
        cell1.setCellComment(comment1);

        //create another cell in row 6
        HSSFCell cell2 = sheet.createRow(6).createCell((short)1);
        cell2.setCellValue(36.6);


        HSSFComment comment2 = patr.createComment(new HSSFClientAnchor(0, 0, 0, 0, (short)4, 8, (short) 6, 11));
        //modify background color of the comment
        comment2.setFillColor(204, 236, 255);

        HSSFRichTextString string = new HSSFRichTextString("Normal body temperature");

        //apply custom font to the text in the comment
        HSSFFont font = wb.createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short)10);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setColor(HSSFColor.RED.index);
        string.applyFont(font);

        comment2.setString(string);
        //by default comments are hidden. This one is always visible.
        comment2.setVisible(true); 

        comment2.setAuthor("Bill Gates");

        /**
	 * The second way to assign comment to a cell is to implicitly specify its row and column.
	 * Note, it is possible to set row and column of a non-existing cell.
	 * It works, the comment is visible.
	 */
	/*
        comment2.setRow(6);
        comment2.setColumn((short)1);

        FileOutputStream out = new FileOutputStream("poi_comment.xls");
        wb.write(out);
        out.close();
            Reading cell comments 

        HSSFCell cell = sheet.get(3).getColumn((short)1);
        HSSFComment comment = cell.getCellComment(); 
        if (comment != null) {
          HSSFRichTextString str = comment.getString();
          String author = comment.getAuthor();
        }
	 */

}
