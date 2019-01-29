package mestra;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;

/*
 * July 2007
 * this class splits and stores a list of SRS requirements as found
 * inside a MESTRA marquer IMPLEMENTED SRS Requirements of a SDD
 * or inside a MESTRA marquer TESTED SRS Requirements of a TS
 * 
 */
public class SRSrequirementsList {

	private final String StringSeparator = "/";
	private Set<String> SRSrequirementsList = null;
	
	public SRSrequirementsList() {
		SRSrequirementsList = new HashSet<String>();
	}

	/**
	 * @return the implementedSRSrequirements
	 */
	public Set<String> getImplementedSRSrequirements() {
		return SRSrequirementsList;
	}

	/**
	 * @param implementedSRSrequirements the implementedSRSrequirements to set
	 */
	public void setSRSrequirements(Set<String> SRSrequirements) {
		this.SRSrequirementsList = SRSrequirements;
	}
	
	public Iterator<String> iterator() {
		return SRSrequirementsList.iterator();
	}
	
	private String removeSpaces(String s) {
		StringTokenizer st = new StringTokenizer(s," ",false);
		String t="";
		while (st.hasMoreElements()) t += st.nextElement();
		return t;
	}
	
	public void add(String str) {
        StringTokenizer strToken = new StringTokenizer(str,StringSeparator);
        while (strToken.hasMoreTokens()) {
            String strSRS = removeSpaces(strToken.nextToken());           
            SRSrequirementsList.add(strSRS);
        }
	}
}
