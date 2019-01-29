package mestra;

import java.util.StringTokenizer;


public class BlankRemover
{

	/* remove leading whitespace */
	public static String ltrim(String source) {
		return source.replaceAll("^\\s+", "");
	}

	/* remove trailing whitespace */
	public static String rtrim(String source) {
		return source.replaceAll("\\s+$", "");
	}

	/* replace multiple whitespaces between words with single blank */
	public static String itrim(String source) {
		return source.replaceAll("\\b\\s{2,}\\b", " ");
	}

	/* remove all superfluous whitespaces in source string */
	public static String trim(String source) {
		return itrim(ltrim(rtrim(source)));
	}

	public static String lrtrim(String source){
		return ltrim(rtrim(source));
	}

	public static String spaceRemover(String source) {

		StringTokenizer st = new StringTokenizer(source," ",false);
		String t="";
		while (st.hasMoreElements()) t += st.nextElement();
		return t;
	}


public static void main(String[] args){
	String oldStr =
		">     <0-1-2-3-4-5-6-7-8-9-----0-1-2-3-4-5-6-7-8-9>   <";
	System.out.println(oldStr);
	String newStr = oldStr.replaceAll("-", " ");
	System.out.println(newStr);
	System.out.println(ltrim(newStr));
	System.out.println(rtrim(newStr));
	System.out.println(itrim(newStr));
	System.out.println(lrtrim(newStr));
	System.out.println(spaceRemover(newStr));
	
}


}

