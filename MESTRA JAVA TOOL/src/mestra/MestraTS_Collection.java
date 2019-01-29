package mestra;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import configuration.ConfigurationFileBaseReader;
import configuration.MestraStyle;
import configuration.MestraStylesMap;

public class MestraTS_Collection {

	final static Logger logger = Logger.getLogger(MestraTS_Collection.class.getName()); 

	ArrayList<MestraTS> mestraTS_Collection = null;

	public MestraTS_Collection() {
		this.mestraTS_Collection = new ArrayList<MestraTS>();
	}

	public int size () {
		return this.mestraTS_Collection.size();
	}

	public ArrayList<MestraTS> getMestraTS_Collection() {
		return this.mestraTS_Collection;
	}

	public Iterator<MestraTS> iterator() {
		return this.mestraTS_Collection.iterator();
	}

	private void checkDuplicates(MestraTS mestraTS) {
		Iterator<MestraTS> Iter = this.mestraTS_Collection.iterator();
		while (Iter.hasNext()) {
			MestraTS aMestraTS = Iter.next();
			if (aMestraTS.getIdentifier().equalsIgnoreCase(mestraTS.getIdentifier())) {
				mestraTS.setDuplicated(true);
				aMestraTS.setDuplicated(true);
			}
		}
	}

	public boolean add(MestraTS mestraTS) {
		checkDuplicates(mestraTS);
		return this.mestraTS_Collection.add(mestraTS);
	}
	
	public boolean addAll(MestraTS_Collection aMestraTS_Collection) {
		if (aMestraTS_Collection != null) {
			Iterator<MestraTS> Iter = aMestraTS_Collection.iterator();
			while (Iter.hasNext()) {
				// WARNING: need to call this.add (and not collection.add) because we want 
				// to call Check Duplicates and identify duplicates

				this.add(Iter.next());
			}
			return (true);
		}
		return false;
	}

	public void init(ConfigurationFileBaseReader configuration, 
			MestraFile mestraFile) {

		MestraTS mestraTStag = null;
		MestraStylesMap mestraStyles = configuration.getMestraStyles(mestraFile.getFileType());

		int UniqueId = 1;

		Iterator<MestraMarker> Iter = mestraFile.getMestraMarkers().iterator();
		while (Iter.hasNext()){
			MestraMarker mestraMarker = Iter.next();

			String mestraStyleStr = mestraMarker.getMestraStyle().getMestraStyle();
			if (mestraStyleStr.equalsIgnoreCase(mestraStyles.getMainMandatoryStyle())) {
				if (mestraTStag != null) {
					this.add(mestraTStag);
				}
				mestraTStag = new MestraTS(mestraStyles);
				String strMarker = mestraMarker.getMestraMarkerValue();
				mestraTStag.setUniqueId(UniqueId++);
				mestraTStag.setIdentifier(strMarker.trim());
				
				mestraTStag.setShortFileName(mestraFile.getShortFileName());
			} else {
				if (mestraStyleStr.equalsIgnoreCase(mestraStyles.getTraceabilityStyle())) {
					String strMarker = mestraMarker.getMestraMarkerValue();
					if (mestraTStag != null) {
						mestraTStag.setTestedSRSrequirements(strMarker);
					}
				} else {
					
					Iterator<MestraStyle> IterTwo = mestraStyles.getValues();
					while (IterTwo.hasNext()) {
						MestraStyle mestraStyle = IterTwo.next();
						if (mestraStyle.getMestraStyle().equalsIgnoreCase(mestraStyleStr)) {
							
							if (mestraTStag != null) {
								mestraTStag.setMestraValue(mestraStyle.getMestraStyleHeader(), mestraMarker.getMestraMarkerValue(), configuration);
							}
						}
					}
					
				}
			}

		}
		// add the last one
		if (mestraTStag != null) {
			this.add(mestraTStag);
		}
	
	}
}
