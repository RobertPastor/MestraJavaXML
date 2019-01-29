package mestra;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;

import configuration.ConfigurationFileBaseReader;
import configuration.MestraStyle;
import configuration.MestraStylesMap;

public class MestraSRS_Collection {
	
	final static Logger logger = Logger.getLogger(MestraSRS_Collection.class.getName()); 

	ArrayList<MestraSRS> mestraSRS_Collection = null;
    
	public MestraSRS_Collection() {
		mestraSRS_Collection = new ArrayList<MestraSRS>();
	}
	
	public Iterator<MestraSRS> iterator() {
		return this.mestraSRS_Collection.iterator();
	}
	
	public int size() {
		return this.mestraSRS_Collection.size();
	}
	
	private void checkDuplicates(MestraSRS mestraSRS) {
		Iterator<MestraSRS> Iter = this.mestraSRS_Collection.iterator();
		while (Iter.hasNext()) {
			MestraSRS aMestraSRS = Iter.next();
			if (aMestraSRS.getIdentifier().equalsIgnoreCase(mestraSRS.getIdentifier())) {
				mestraSRS.setDuplicated(true);
				aMestraSRS.setDuplicated(true);
			}
		}
	}

	public boolean add(MestraSRS mestraSRS) {
		checkDuplicates(mestraSRS);
		return this.mestraSRS_Collection.add(mestraSRS);
	}
	
	public boolean addAll(MestraSRS_Collection aMestraSRS_Collection) {
		if (aMestraSRS_Collection != null) {
			Iterator<MestraSRS> Iter = aMestraSRS_Collection.iterator();
			while (Iter.hasNext()) {
				// WARNING: need to call this.add (and not collection.add) because we want 
				// to call Check Duplicates and identify duplicates
				this.add(Iter.next());
			}
			return (true);
		}
		return false;
	}

	/**
	 * The configuration contains the data as extracted from the configuration file.
	 * The configuration file decides what has to be shown from the extracted markers.
	 * @param configuration
	 * @param mestraFile
	 */
	public void init(ConfigurationFileBaseReader configuration, MestraFile mestraFile) {
		
		MestraSRS mestraSRStag = null;
		MestraStylesMap mestraStyles = configuration.getMestraStyles(mestraFile.getFileType());
		
		int UniqueId = 1;
		
		Iterator<MestraMarker> Iter = mestraFile.getMestraMarkers().iterator();
		while (Iter.hasNext()){
			MestraMarker mestraMarker = Iter.next();

			String mestraStyleHeader = mestraMarker.getMestraStyle().getMestraStyleHeader();
			if (mestraStyleHeader.equalsIgnoreCase(mestraStyles.getMainMandatoryStyleHeader())) {
				if (mestraSRStag != null) {
					// WARNING: need to call this.add (and not collection.add) because we want 
					// to identify SSS Requirement duplicates
					this.add(mestraSRStag);
				}
				mestraSRStag = new MestraSRS(mestraStyles);
				String strMarker = mestraMarker.getMestraMarkerValue();
				mestraSRStag.setUniqueId(UniqueId++);
				mestraSRStag.setIdentifier(strMarker);
				mestraSRStag.setShortFileName(mestraFile.getShortFileName());

			} else {
				if (mestraStyleHeader.equalsIgnoreCase(mestraStyles.getTraceabilityStyleHeader())) {
					if (mestraSRStag != null) {
						mestraSRStag.setStrSSS_References(mestraMarker.getMestraMarkerValue());
					}

				} else {
					Iterator<MestraStyle> IterTwo = mestraStyles.getValues();
					while (IterTwo.hasNext()) {
						MestraStyle mestraStyle = IterTwo.next();
						if (mestraStyle.getMestraStyleHeader().equalsIgnoreCase(mestraStyleHeader)) {
							
							//logger.info("Header= "+ mestraStyle.getMestraStyleHeader() + " --- value= " + mestraMarker.getMestraMarkerValue());
							if (mestraSRStag != null) {
								// if a mestra style is found before the the mestra mandatory style (REQ) the style is ignored 
								mestraSRStag.setMestraValue(mestraStyle.getMestraStyleHeader(), mestraMarker.getMestraMarkerValue(), configuration);
							} 
						}
					}
				}
			}
		}	
		
		// add the last one
		if (mestraSRStag != null) {
		    // need to call this.add in order to check duplicates
			this.add(mestraSRStag);
		}
	}

    public ArrayList<MestraSRS> getMestraSRS_Collection() {
        return this.mestraSRS_Collection;
    }
}
