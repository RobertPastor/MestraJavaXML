package mestra;

/**
 * class created April 2007
 * Author : Robert Pastor
 * this class manages a collection of MESTRA MF tags which could have
 * been extracted from several files
 * 
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import configuration.ConfigurationFileBaseReader;
import configuration.MestraStyle;
import configuration.MestraStylesMap;

public class MestraMF_Collection {
	
	final static Logger logger = Logger.getLogger(MestraMF_Collection.class.getName()); 

	ArrayList<MestraMF> mestraMF_Collection = null;
	
	public MestraMF_Collection() {
		this.mestraMF_Collection = new ArrayList<MestraMF>();
	}

	private void checkDuplicates(MestraMF mestraMF) {
		Iterator<MestraMF> Iter = this.mestraMF_Collection.iterator();
		while (Iter.hasNext()) {
			MestraMF aMestraMF = Iter.next();
			if (aMestraMF.getIdentifier().equalsIgnoreCase(mestraMF.getIdentifier())) {
				mestraMF.setDuplicated(true);
				aMestraMF.setDuplicated(true);
			}
		}
	}

	public boolean add(MestraMF mestraMF) {
		checkDuplicates(mestraMF);
		return this.mestraMF_Collection.add(mestraMF);
	}
	
	public boolean addAll(MestraMF_Collection aMestraMF_Collection) {
		if (aMestraMF_Collection != null) {
			Iterator<MestraMF> Iter = aMestraMF_Collection.iterator();
			while (Iter.hasNext()) {
				this.mestraMF_Collection.add(Iter.next());
			}
			return (true);
		}
		return false;
	}

	public Iterator<MestraMF> iterator() {
		return this.mestraMF_Collection.iterator();
	}
	
	public int size() {
		return this.mestraMF_Collection.size();
	}
	
	public void init(ConfigurationFileBaseReader configuration, 
			MestraFile mestraFile) {
		
		MestraMF mestraMFtag = null;
		MestraStylesMap mestraStyles = configuration.getMestraStyles(mestraFile.getFileType());

		int UniqueId = 1;
		
		Iterator<MestraMarker> Iter = mestraFile.getMestraMarkers().iterator();
		while (Iter.hasNext()){
			MestraMarker mestraMarker = Iter.next();

			String mestraStyleStr = mestraMarker.getMestraStyle().getMestraStyle();
			if (mestraStyleStr.equalsIgnoreCase(mestraStyles.getMainMandatoryStyle())) {
				if (mestraMFtag != null) {
					this.add(mestraMFtag);
				}
				mestraMFtag = new MestraMF(mestraStyles);
				String strMarker = mestraMarker.getMestraMarkerValue();
				mestraMFtag.setUniqueId(UniqueId++);
				mestraMFtag.setIdentifier(strMarker);
				
				mestraMFtag.setShortFileName(mestraFile.getShortFileName());
			} else {
				if (mestraStyleStr.equalsIgnoreCase(mestraStyles.getTraceabilityStyle())) {
					String strMarker = mestraMarker.getMestraMarkerValue();
					if (mestraMFtag != null) {
						mestraMFtag.setDesignParts(strMarker);
					}
				} else {
					Iterator<MestraStyle> IterTwo = mestraStyles.getValues();
					while (IterTwo.hasNext()) {
						MestraStyle mestraStyle = IterTwo.next();
						if (mestraStyle.getMestraStyle().equalsIgnoreCase(mestraStyleStr)) {
							
							if (mestraMFtag != null) {
								mestraMFtag.setMestraValue(mestraStyle.getMestraStyleHeader(), mestraMarker.getMestraMarkerValue(), configuration);
							}
						}
					}
				}
			}
			
		}	
		// add the last one
		if (mestraMFtag != null) {
		    // need to call this.add in order to call the checkDuplicates method
			this.add(mestraMFtag);
		}
	}

    public ArrayList<MestraMF> getMestraMF_Collection() {
        return this.mestraMF_Collection;
    }

}
