package mestra;

import configuration.ConfigurationFileBaseReader;

public class Trace_TS_SRS extends MestraTS {

	private MestraSRS mestraSRS = null;
	private boolean UnknownSRSreference = false;
	
	public Trace_TS_SRS(MestraTS mestraTS, MestraSRS aMestraSRS, ConfigurationFileBaseReader configuration) {
		super(mestraTS, configuration);
		UnknownSRSreference = false;
		if (aMestraSRS == null) {
			mestraSRS = null;
		}
		else {
			mestraSRS = (MestraSRS)aMestraSRS.clone();
		}
	}
	
	public boolean isSRSreferenceUnknow() {
		return UnknownSRSreference;
	}
	
	public void setSRSreferenceUnknown(boolean bool) {
		UnknownSRSreference = bool;
	}
	
	public String getSRSIdentifier() {
		if (mestraSRS == null) {
			return "";
		}
		else {
			return mestraSRS.getIdentifier();
		}
	}
	
	public Trace_TS_SRS getThis() {
		return this;
	}

	/**
	 * @return the mestraSRS
	 */
	public MestraSRS getMestraSRS() {
		return mestraSRS;
	}

	/**
	 * @param mestraSRS the mestraSRS to set
	 */
	public void setMestraSRS(MestraSRS mestraSRS) {
		this.mestraSRS = mestraSRS;
	}
	
}
