package test;

public class TestSuppressChar {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String inStr = "test /PB Saut de page[.doc";
		System.out.println(inStr);
		String outStr = SuppressForbiddenChar(inStr);
		System.out.println(outStr);
	}

	private static String SuppressForbiddenChar(String inStr) {

		String outStr = inStr;
		// Sheet name cannot be blank, greater than 31 chars, or contain any of /\*?[]		
		char[] toBeSuppressedChars = { '(' , '?' ,
				',' , ';' , ')' , '"' , ':' , '/' , '-' , '_' , '[' , ']' , '*' , '\''};
		int index = 0;
		int len = outStr.length() ;
		while (index < len) {
			char c = outStr.charAt(index);
			int sc = 0;
			boolean exitLoop = false;
			while ((sc < toBeSuppressedChars.length) && (exitLoop == false)){
				if (c == toBeSuppressedChars[sc]) {
					outStr = outStr.substring(0, index)+outStr.substring(index+1, len);
					len = outStr.length();
					index = 0;
					exitLoop = true;
				}
				sc++;
			}
			index = index + 1;
		}
		return outStr;
	}

	
}
