package mestra;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;

public class ImplementedSRSrequirements {

	private Set<String> implementedSRSrequirements = null;
	
	public ImplementedSRSrequirements() {
		implementedSRSrequirements = new HashSet<String>();
	}

	/**
	 * @return the implementedSRSrequirements
	 */
	public Set<String> getImplementedSRSrequirements() {
		return implementedSRSrequirements;
	}

	/**
	 * @param implementedSRSrequirements the implementedSRSrequirements to set
	 */
	public void setImplementedSRSrequirements(Set<String> implementedSRSrequirements) {
		this.implementedSRSrequirements = implementedSRSrequirements;
	}
	
	public Iterator<String> iterator() {
		return implementedSRSrequirements.iterator();
	}
	
	private String removeSpaces(String s) {
		StringTokenizer st = new StringTokenizer(s," ",false);
		String t="";
		while (st.hasMoreElements()) t += st.nextElement();
		return t;
	}
	
	public void add(String str) {
        StringTokenizer strToken = new StringTokenizer(str,"/");
        while (strToken.hasMoreTokens()) {
            String strSRS = removeSpaces(strToken.nextToken());           
            implementedSRSrequirements.add(strSRS);
        }
		
	}
}
