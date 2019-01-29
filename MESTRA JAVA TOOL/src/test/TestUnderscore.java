package test;

import java.util.StringTokenizer;

public class TestUnderscore {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		for (int i=0 ; i < 255 ; i++) {
			System.out.println(i+"..."+new Character((char)i));
		}
		System.out.println("----");
		System.out.println(new Character((char)95));
		System.out.println(new Character((char)173));
		System.out.println(new Character((char)45));
		
		System.out.println("----");
		String str1 = "TTT-AAAA"+new Character((char)173)+"BBB";
		System.out.println(str1);
		String str2 = "TTT-AAAA-BBB";
		System.out.println(str2);
		if (str1.equalsIgnoreCase(str2)) {
			System.out.println("strings are equal");
		}
		else System.out.println("Strings are NOT equal!");

		String str3 = convertDash173toDash45(str1);
		System.out.println(str2);
		System.out.println(str3);
		if (str2.equalsIgnoreCase(str3)) {
			System.out.println("strings are NOW equal");
		}
		else System.out.println("Strings are NOT equal!");
	}
	
	// TODO implement the following
	private static String convertDash173toDash45(String source) {
		
		String char173 = new Character((char)173).toString();
		StringTokenizer st = new StringTokenizer(source,char173,false);
		String t="";
		while (st.hasMoreElements()) {
			if (t.length() == 0) {
				t = t + st.nextElement();
			}
			else {
				t = t + new Character((char)45).toString() + st.nextElement();
			}
		}
		return t;
	}

	/*
	private String spaceRemover(String source) {

		StringTokenizer st = new StringTokenizer(source," ",false);
		String t="";
		while (st.hasMoreElements()) t += st.nextElement();
		return t;
	}
	*/

}
