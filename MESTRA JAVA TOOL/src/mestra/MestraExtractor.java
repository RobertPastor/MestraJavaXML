package mestra;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.model.StyleDescription;
import org.apache.poi.hwpf.model.StyleSheet;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Section;

import configuration.MestraStyle;
import configuration.MestraStylesMap;

public class MestraExtractor {

	private static MestraMarker ExtractOneMestraMarker(Paragraph paragraph,
			StyleSheet styleSheet,MestraStylesMap mestraStyles) {

		MestraMarker mestraMarker = new MestraMarker();
		short styleIndex = paragraph.getStyleIndex();
		StyleDescription styleDescription = styleSheet.getStyleDescription(styleIndex);
		String paragraphStyle = styleDescription.getName();

		Iterator<MestraStyle> Iter = mestraStyles.getValues();
		while (Iter.hasNext()){
			MestraStyle mestraStyle = Iter.next();
			String strStyle = mestraStyle.getMestraStyle();
			mestraMarker.setMestraStyle(new MestraStyle(strStyle,""));
			if (paragraphStyle.equalsIgnoreCase(strStyle)){
				String strMarker = "";
				for (int z = 0; z < paragraph.numCharacterRuns(); z++) {
					// character run
					CharacterRun run = paragraph.getCharacterRun(z);
					if ( run.isStrikeThrough() == false) {
						strMarker = strMarker + run.text();
					}
				}
				if (strStyle.equalsIgnoreCase(mestraStyles.getMainMandatoryStyle())) {
					mestraMarker.setMestraMarkerValue(Filter(strMarker,true));
				}
				else {
					mestraMarker.setMestraMarkerValue(strMarker);
				}
			}
		}
		return mestraMarker;
	}


	public static Set<MestraMarker> ExtractMestraMarkers(HWPFDocument doc,MestraStylesMap mestraStyles){

		Set<MestraMarker> mestraMarkers = new HashSet<MestraMarker>();
		
		try
		{
			Range r = doc.getRange();

			StyleSheet styleSheet = doc.getStyleSheet();
			for (int x = 0; x < r.numSections(); x++)
			{
				Section s = r.getSection(x);
				for (int y = 0; y < s.numParagraphs(); y++)
				{
					Paragraph paragraph = s.getParagraph(y);
					MestraMarker mestraMarker = ExtractOneMestraMarker( paragraph,styleSheet,mestraStyles);
					mestraMarkers.add(mestraMarker);
				}
			}
		}
		catch (Throwable t)
		{
			t.printStackTrace();
		}
		return mestraMarkers;
	}
	private static String Filter(String str,boolean SuppressSpaces){
		if (str.length()> 0){
			if ((str.endsWith("\b")) || 
					(str.endsWith("\t")) ||
					(str.endsWith("\n")) ||
					(str.endsWith("\r")) ||
					(str.endsWith("\f")) ) {
				int len = str.length();
				str = str.substring(0, len-1);
			}
			if (SuppressSpaces==true) {
				str = str.replaceAll(" ", "");
			}
			int len = str.length();
			if (len > 2) {
				if ((int)str.charAt(len-1)==7 ) {
					str = str.substring(0, len-1);
				}
			}
		}
		return str;
	}
}
