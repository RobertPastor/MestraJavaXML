package mestra;

import configuration.ConfigurationFileBaseReader;

public class Trace_SDD_SRS extends MestraSDD {

    private MestraSRS mestraSRS = null;
    private boolean UnknownSRSreference = false;

    public Trace_SDD_SRS(MestraSDD mestraSDD, MestraSRS aMestraSRS, ConfigurationFileBaseReader configuration) {

        super(mestraSDD, configuration);
        UnknownSRSreference = false;
        if (aMestraSRS == null) {
            mestraSRS = null;
        }
        else {
            mestraSRS = (MestraSRS)aMestraSRS.clone();
        }
    }

    public void setSRSreferenceUnknown(boolean bool) {
        UnknownSRSreference = bool;
    }

    public Trace_SDD_SRS getThis() {
        return this;
    }

	/**
	 * @return the mestraSRS
	 */
	public MestraSRS getMestraSRS() {
		return this.mestraSRS;
	}

	/**
	 * @param mestraSRS the mestraSRS to set
	 */
	public void setMestraSRS(MestraSRS mestraSRS) {
		this.mestraSRS = mestraSRS;
	}

	/**
	 * @return the unknownSRSreference
	 */
	public boolean isSRSreferenceUnknown() {
		return this.UnknownSRSreference;
	}

	/**
	 * @param unknownSRSreference the unknownSRSreference to set
	 */
	public void setUnknownSRSreference(boolean _unknownSRSreference) {
		this.UnknownSRSreference = _unknownSRSreference;
	}

	public String getSRSIdentifier() {
		if (this.mestraSRS == null) {
			return "";
		}
		else {
			return this.mestraSRS.getIdentifier();
		}
	}
}
