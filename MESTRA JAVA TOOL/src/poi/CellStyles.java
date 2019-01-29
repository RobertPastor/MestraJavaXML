package poi;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

/**
 * This class manages the different kind of cell styles to be used
 * <br> when filling an EXCEL sheet
 * @author PASTOR Robert
 * @since June 2007
 *
 */
public class CellStyles {

	/**
	 * Defines the different kind of Cell Styles to be used
	 * when filling an EXCEL sheet
	 */
	public enum CellStylesEnum { 
		ArialEightCellStyle, 
		ArialEightBoldCellStyle,
		ArialEightYellowBackGroundCellStyle,
		ArialEightRedBackGroundCellStyle }

	private HSSFCellStyle arialEightCellStyle = null ;
	private HSSFCellStyle arialEightBoldCellStyle = null ;
	private HSSFCellStyle arialEightYellowBackGroundCellStyle = null ;
	private HSSFCellStyle arialEightRedBackGroundCellStyle = null ;

	/**
	 * Constructor creates the "to be used" Cell Styles
	 */
	public CellStyles (HSSFWorkbook wb) {

		arialEightCellStyle = CreateArialEightSizedFont(wb,false,false,new HSSFColor.WHITE());
		arialEightBoldCellStyle = CreateArialEightSizedFont(wb,true,false,new HSSFColor.WHITE());
		arialEightYellowBackGroundCellStyle = CreateArialEightSizedFont(wb,false,true,new HSSFColor.YELLOW());
		arialEightRedBackGroundCellStyle = CreateArialEightSizedFont(wb,false,true,new HSSFColor.RED());

	}

	/**
	 * Returns the selected cell style
	 * @param cellStyleEnum
	 * @return
	 */
	public HSSFCellStyle getCellStyle(CellStylesEnum cellStylesEnum) {
		switch (cellStylesEnum) {
		case ArialEightCellStyle:
			return arialEightCellStyle;
		case ArialEightBoldCellStyle:
			return arialEightBoldCellStyle;
		case ArialEightYellowBackGroundCellStyle:
			return arialEightYellowBackGroundCellStyle;
		case ArialEightRedBackGroundCellStyle:
			return arialEightRedBackGroundCellStyle;
		default :
			return arialEightCellStyle;
		}
	}

	/**
	 * creates each Cell Style
	 * @param wb ; the workbook where to use the cell style
	 * @param Bold : the cell will be "BOLDED"
	 * @param BackGround : there will be a background color
	 * @param color : the background color
	 * @return
	 */
	private HSSFCellStyle CreateArialEightSizedFont(HSSFWorkbook wb,
			boolean Bold,
			boolean BackGround,
			HSSFColor color) {

		// Create a new font and alter it.
		HSSFFont font = wb.createFont();
		font.setFontHeightInPoints((short)8);
		font.setFontName("arial");
		if (Bold == true) {
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		}
		font.setItalic(false);
		font.setStrikeout(false);

		// Fonts are set into a style so create a new one to use.
		HSSFCellStyle style = wb.createCellStyle();

		if (BackGround == true) {
			style.setFillBackgroundColor(color.getIndex());
			style.setFillForegroundColor(color.getIndex());
			style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			font.setItalic(true);
		}
		style.setFont(font);
		return style;
	}


}
