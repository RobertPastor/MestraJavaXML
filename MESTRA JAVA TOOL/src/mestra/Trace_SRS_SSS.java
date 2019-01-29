package mestra;

import configuration.ConfigurationFileBaseReader;

/*
 * class created June 2007
 * Author : Robert Pastor
 * This class traces a SRS Requirement to one SSS Requirement
 * it is a Downstream to Upstream trace
 */

public class Trace_SRS_SSS extends MestraSRS {

	private MestraSSS mestraSSS = null;
	private boolean UnknownSSSreference = false;
	
	public boolean isSSSreferenceUnknown() {
		return UnknownSSSreference;
	}
	
	public void setSSSreferenceUnknown(boolean bool) {
		UnknownSSSreference = bool;
	}
	
	public Trace_SRS_SSS (MestraSRS mestraSRS,MestraSSS aMestraSSS, ConfigurationFileBaseReader configuration) {
		super(mestraSRS, configuration);
		UnknownSSSreference = false;
		if (aMestraSSS == null) {
			mestraSSS = null;
		}
		else {
			mestraSSS = (MestraSSS)aMestraSSS.clone();
		}
	}	
	
	public String getSSSIdentifier () {
		if (mestraSSS == null) {
			return "";
		}
		else {
			return mestraSSS.getIdentifier();
		}
	}
	
	public Trace_SRS_SSS getThis() {
		return this;
	}

	/**
	 * @return the mestraSSS
	 */
	public MestraSSS getMestraSSS() {
		return mestraSSS;
	}

	/**
	 * @param mestraSSS the mestraSSS to set
	 */
	public void setMestraSSS(MestraSSS mestraSSS) {
		this.mestraSSS = mestraSSS;
	}
}
