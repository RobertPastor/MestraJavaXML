package mestra;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * 
 * This class splits and stores: 
 * <br>
 * 1) a list of covered SSS requirements as found inside a SRS Requirement 
 * <br>
 * 2) a list of SRS requirements as found inside a MESTRA marker IMPLEMENTED SRS Requirements of a SDD
 * <br>
 * 3) or inside a MESTRA marker TESTED SRS Requirements of a TS
 * 
 * 
 * @author Pastor
 * @since July 2007
 */
public class IdentifierList {

	private final String StringSeparator = "/";
	private Set<String> mIdentifierList = null;

	public IdentifierList() {
		mIdentifierList = new HashSet<String>();
	}

	/**
	 * @return the implementedSRSrequirements
	 */
	public Set<String> getIdentifierList() {
		return mIdentifierList;
	}

	public String getIdentifierListString() {
		String strIdentifierList = "";
		Iterator<String> Iter = mIdentifierList.iterator();
		while (Iter.hasNext()) {
			if (strIdentifierList.length()==0) {
				strIdentifierList = strIdentifierList + Iter.next();				
			}
			else {
				strIdentifierList = strIdentifierList + StringSeparator + Iter.next();
			}
		}
		return strIdentifierList;
	}

	/**
	 * @param implementedSRSrequirements the implementedSRSrequirements to set
	 */
	public void setSRSrequirements(Set<String> pIdentifierList) {
		this.mIdentifierList = pIdentifierList;
	}

	public Iterator<String> iterator() {
		return mIdentifierList.iterator();
	}

	public boolean isEmpty() {
		return (mIdentifierList.isEmpty());
	}

	private String removeSpaces(String s) {
		StringTokenizer st = new StringTokenizer(s," ",false);
		String t="";
		while (st.hasMoreElements()) {
			t += st.nextElement();
		}
		return t;
	}

	/**
	 * This method splits a String in list elements and adds them to the list
	 * @param str
	 * @return always TRUE if the identifier has been correctly added
	 */
	public boolean add(String strTokenListToSplit) {
		strTokenListToSplit = removeSpaces(strTokenListToSplit);
		StringTokenizer strToken = new StringTokenizer(strTokenListToSplit,StringSeparator);
		while (strToken.hasMoreTokens()) {
			String strSRS = removeSpaces(strToken.nextToken());           
			mIdentifierList.add(strSRS);
		}
		return true;
	}

	public void clear() {
		mIdentifierList.clear();
	}

	public boolean containsReqDerived() {
		if (this.mIdentifierList.size() > 0) {
			Iterator<String> Iter = mIdentifierList.iterator();
			while (Iter.hasNext()) {
				String str = Iter.next();
				if (str.toLowerCase().indexOf("derived") > 0) {
					return true;
				}
			}
		}
		return false;
	}

	public int size() {
		return mIdentifierList.size();
	}
}
