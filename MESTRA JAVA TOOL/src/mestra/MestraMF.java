package mestra;


import java.util.StringTokenizer;

import configuration.ConfigurationFileBaseReader;
import configuration.MestraStylesMap;

public class MestraMF extends MestraBase {

	private String DesignParts;

	public MestraMF(MestraStylesMap mestraStylesMap) {
		super(mestraStylesMap);
		DesignParts= "";
	}

	public MestraMF(MestraMF mestraMF, ConfigurationFileBaseReader configuration) {
		super((MestraBase)mestraMF, configuration);
		this.DesignParts = mestraMF.DesignParts;       
	}


	public String removeSpaces(String s) {
		StringTokenizer st = new StringTokenizer(s," ",false);
		String t="";
		while (st.hasMoreElements()) t += st.nextElement();
		return t;
	}


	/**
	 * @return the designParts
	 */
	public String getDesignParts() {
		return this.DesignParts;
	}

	/**
	 * @param designParts the designParts to set
	 */
	public void setDesignParts(String designParts) {
		this.DesignParts = this.DesignParts + designParts;
	}


	public boolean IsCovering(String SRS_Identifier) {

		// 11th March 2009
		// specific to MF: 
		// the MF identifier contains a SRS Requirement identifier
		if (this.Identifier.equalsIgnoreCase(SRS_Identifier)) {
			return true;
		}
		return false;
	}

	public Object clone() {

		MestraMF mestraMF = new MestraMF(this, this.configuration);
		return mestraMF;
	}

}


