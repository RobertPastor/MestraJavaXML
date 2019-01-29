package mestra;


import java.util.StringTokenizer;

/**
 * 
 * This is the Base Class for MESTRA Component.
 * This class is dedicated to display colored cells
 * in a the Table View for the User Interface
 * 
 * @since: June 2007
 * @author: Robert Pastor
 */


//TODO add filter to modify some WRONG underscore or Dash in correct dash


public class MestraIdentifier extends ErrorWarning implements Cloneable {

	public static final int MestraStyleIdentifier = 0;
	public static final String strSeparator = "/";

	private int UniqueId = 0;
	protected String Identifier = "";
	private boolean Duplicated = false;

	/**
	 * Constructor
	 * 
	 */
	public MestraIdentifier() {
		super();
		UniqueId = 0;
		Identifier = "";
		Duplicated = false;
	}

	public MestraIdentifier(MestraIdentifier mestraIdentifier) {
		super();
		UniqueId = mestraIdentifier.UniqueId;
		setIdentifier (mestraIdentifier.Identifier);
		setDuplicated (mestraIdentifier.Duplicated);
	}

	public MestraIdentifier(String strIdentifier, boolean duplicated) {
		super();
		setIdentifier (strIdentifier);
		setDuplicated (duplicated);
	}

	/**
	 * @return a clone of this object
	 */
	public Object clone() throws CloneNotSupportedException  {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			throw new Error("This should not occur since we implement Cloneable");
		}
	}

	/**
	 * @return the identifier
	 */
	public String getIdentifier() {
		return this.Identifier;
	}

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

	/**
	private String tabRemoverold(String source) {
		
		String t=source;
		// Remove the tab characters. Replace with new line characters.
		Pattern nLineChar = Pattern.compile("\t");
		Matcher mnLine = nLineChar.matcher(t);
		while (mnLine.find()) {
			t = mnLine.replaceAll("");
		}
		nLineChar = Pattern.compile("\n");
		mnLine = nLineChar.matcher(t);
		while (mnLine.find()) {
			t = mnLine.replaceAll("");
		}
		nLineChar = Pattern.compile("\r");
		mnLine = nLineChar.matcher(t);
		while (mnLine.find()) {
			t = mnLine.replaceAll("");
		}
		nLineChar = Pattern.compile("\f");
		mnLine = nLineChar.matcher(t);
		while (mnLine.find()) {
			t = mnLine.replaceAll("");
		}
		return t;
	}
	**/

	private String tabRemover(String source) {
		
		StringTokenizer st = new StringTokenizer(source,"\t",false);
		String t="";
		while (st.hasMoreElements()) t += st.nextElement();
		return t;
	}

	private String spaceRemover(String source) {

		StringTokenizer st = new StringTokenizer(source," ",false);
		String t="";
		while (st.hasMoreElements()) t += st.nextElement();
		return t;
	}

	/**
	 * @param identifier : the identifier to set
	 */
	public void setIdentifier(String identifier) {
		// this can be called several times
		Identifier = Identifier + identifier;
		Identifier = spaceRemover(Identifier);
		Identifier = tabRemover(Identifier);
		Identifier = convertDash173toDash45(Identifier);
	}

	/**
	 * @return the duplicated
	 */
	public boolean isDuplicated() {
		return this.Duplicated;
	}


	/**
	 * @param duplicated the duplicated to set
	 */
	public void setDuplicated(boolean _duplicated) {
		this.Duplicated = _duplicated;
	}

	public boolean isDuplicated(String identifier) {
		identifier = spaceRemover(identifier);
		if (this.Identifier.equalsIgnoreCase(identifier)) {
			return true;
		}
		return false;
	}

	/**
	 * Returns true if the MESTRA identifier contains a SLASH or starts or ends with a DASH.
	 * @return
	 */
	public boolean containsIllegalCharacters() {
		if ((this.Identifier.contains("/")) || (this.Identifier.startsWith("-")) || (this.Identifier.endsWith("-"))) {
			return true;
		}
		return false;
	}

	/**
	 * @return the uniqueId as an Integer
	 */
	public int getUniqueId() {
		return this.UniqueId;
	}

	/**
	 * Returns the Identifier Unique Id as a String
	 * @return UniqueId converted from Integer to String
	 */
	public String getStrUniqueId() {
		return (String.valueOf(this.UniqueId));
	}
	/**
	 * @param uniqueId the uniqueId to set
	 */
	public void setUniqueId(int _uniqueId) {
		this.UniqueId = _uniqueId;
	}

	public void computeWarningsErrors() {
		if (containsIllegalCharacters()) {
			addError("Identifier contains illegal character!");
		}
		if (Duplicated) {
			addError("Identifier is duplicated!");
		}
	}
}
