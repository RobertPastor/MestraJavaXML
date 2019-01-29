package mestra;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import configuration.ConfigurationFileBaseReader;
import configuration.MestraStyle;
import configuration.MestraStylesMap;

public class MestraSSS_Collection {
	
	final static Logger logger = Logger.getLogger(MestraSSS_Collection.class.getName()); 

	ArrayList<MestraSSS> mestraSSS_Collection = null;

	public MestraSSS_Collection() {
		this.mestraSSS_Collection = new ArrayList<MestraSSS>();
	}

	public Iterator<MestraSSS> iterator() {
		return this.mestraSSS_Collection.iterator();
	}

	public int size() {
		return this.mestraSSS_Collection.size();
	}

	private void checkDuplicates(MestraSSS mestraSSS) {
		Iterator<MestraSSS> Iter = this.mestraSSS_Collection.iterator();
		while (Iter.hasNext()) {
			MestraSSS aMestraSSS = Iter.next();
			if (aMestraSSS.getIdentifier().equalsIgnoreCase(mestraSSS.getIdentifier())) {
				// need to set both Markers as duplicated
				// because both identifiers have to be highlighted
				mestraSSS.setDuplicated(true);
				aMestraSSS.setDuplicated(true);
			}
		}
	}

	public boolean add(MestraSSS mestraSSS) {
		checkDuplicates(mestraSSS);
		return this.mestraSSS_Collection.add(mestraSSS);
	}

	public boolean addAll(MestraSSS_Collection aMestraSSS_Collection) {
		if (aMestraSSS_Collection != null) {
			Iterator<MestraSSS> Iter = aMestraSSS_Collection.iterator();
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
	 * 
	 * @param configuration
	 * @param mestraMarkers
	 * @param mestraFile
	 */
	public void init(ConfigurationFileBaseReader configuration, 
			MestraFile mestraFile) {

		MestraSSS mestraSSStag = null;
		MestraStylesMap mestraStyles = configuration.getMestraStyles(mestraFile.getFileType());

		int UniqueId = 1;

		Iterator<MestraMarker> Iter = mestraFile.getMestraMarkers().iterator();
		while (Iter.hasNext()){
			MestraMarker mestraMarker = Iter.next();

			String mestraStyleHeader = mestraMarker.getMestraStyle().getMestraStyleHeader();
			if (mestraStyleHeader.equalsIgnoreCase(mestraStyles.getMainMandatoryStyleHeader())) {
				if (mestraSSStag != null) {
					// WARNING: need to call this.add (and not collection.add) because we want 
					// to identify SSS Requirement duplicates
					this.add(mestraSSStag);
				}
				mestraSSStag = new MestraSSS(mestraStyles);
				String strMarker = mestraMarker.getMestraMarkerValue();
				mestraSSStag.setUniqueId(UniqueId++);
				mestraSSStag.setIdentifier(strMarker);
				mestraSSStag.setShortFileName(mestraFile.getShortFileName());

			} else {
				if (mestraStyleHeader.equalsIgnoreCase(mestraStyles.getAllocationStyleHeader())) {
					if (mestraSSStag != null) {
						mestraSSStag.setRequirementAllocation(mestraMarker.getMestraMarkerValue(), configuration);
					}

				} else {
					Iterator<MestraStyle> IterTwo = mestraStyles.getValues();
					while (IterTwo.hasNext()) {
						MestraStyle mestraStyle = IterTwo.next();
						if (mestraStyle.getMestraStyleHeader().equalsIgnoreCase(mestraStyleHeader)) {
							
							// if a mestra style is found before the first mandatory style : it is ignored
							if (mestraSSStag != null) {
								mestraSSStag.setMestraValue(mestraStyle.getMestraStyleHeader(), mestraMarker.getMestraMarkerValue(), configuration);
							} 
						}
					}
				}
			}
		}	
		// add the last one
		if (mestraSSStag != null) {
			// WARNING: need to call this.add (and not collection.add) because we want 
			// to identify SSS Requirement duplicates
			this.add(mestraSSStag);
		}

	}

	public ArrayList<MestraSSS> getMestraSSS_Collection() {
		return this.mestraSSS_Collection;
	}

	public void setMestraSSS_Collection(ArrayList<MestraSSS> mestraSSS_Collection) {
		this.mestraSSS_Collection = mestraSSS_Collection;
	}

}
