package mestra;

import configuration.ConfigurationFileBaseReader;

/**
 * 
 * Manages a trace between a SRS and a SDD.
 * @author Pastor
 *
 */
public class Trace_SRS_SDD extends MestraSRS {

    private MestraSDD mestraSDD = null;

    public Trace_SRS_SDD(MestraSRS mestraSRS, MestraSDD aMestraSDD, ConfigurationFileBaseReader configuration) {

        super(mestraSRS, configuration);
        if (aMestraSDD == null) {
            mestraSDD = null;
        }
        else {
            mestraSDD = (MestraSDD)aMestraSDD.clone();
        }
    }

    public Trace_SRS_SDD getThis() {
        return this;
    }

	/**
	 * @return the mestraSDD
	 */
	public MestraSDD getMestraSDD() {
		return mestraSDD;
	}

	/**
	 * @param mestraSDD the mestraSDD to set
	 */
	public void setMestraSDD(MestraSDD mestraSDD) {
		this.mestraSDD = mestraSDD;
	}

	public String getSDDIdentifier() {
		if (mestraSDD == null) {
			return "";
		}
		else {
			return mestraSDD.getIdentifier();
		}
	}

}
