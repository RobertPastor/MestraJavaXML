package mestra;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import configuration.ConfigurationFileBaseReader;
import configuration.MestraStyle;
import configuration.MestraStylesMap;



/**
 * 
 * this class manages a set of SDD markers 
 * such as discovered after analyzing a SDD file.
 * @author PASTOR Robert
 * @since June 2007
 *
 */
public class MestraSDD_Collection {
	
	final static Logger logger = Logger.getLogger(MestraSDD_Collection.class.getName()); 
	
	ArrayList<MestraSDD> mestraSDD_Collection = null;
	
	public MestraSDD_Collection() {
		this.mestraSDD_Collection = new ArrayList<MestraSDD>();
	}
	
	public Iterator<MestraSDD> iterator() {
		return this.mestraSDD_Collection.iterator();
	}

	private void checkDuplicates(MestraSDD mestraSDD) {
		Iterator<MestraSDD> Iter = mestraSDD_Collection.iterator();
		while (Iter.hasNext()) {
			MestraSDD aMestraSDD = Iter.next();
			if (aMestraSDD.getIdentifier().equalsIgnoreCase(mestraSDD.getIdentifier())) {
				mestraSDD.setDuplicated(true);
				aMestraSDD.setDuplicated(true);
			}
		}
	}
	
	public boolean add(MestraSDD mestraSDD) {
		checkDuplicates(mestraSDD);
		return this.mestraSDD_Collection.add(mestraSDD);
	}
	
	public boolean addAll(MestraSDD_Collection aMestraSDD_Collection) {
		if (aMestraSDD_Collection != null) {
			Iterator<MestraSDD> Iter = aMestraSDD_Collection.iterator();
			while (Iter.hasNext()) {
				// WARNING: need to call this.add (and not collection.add) because we want 
				// to call Check Duplicates and identify duplicates
				MestraSDD mestraSDD = Iter.next();
				//logger.info ( "MestraSDD_Collection: addAll: "+mestraSDD.getIdentifier());
				this.add(mestraSDD);
			}
			return (true);
		}
		return false;
	}

	public void init(ConfigurationFileBaseReader configuration, 
			MestraFile mestraFile) {

		MestraSDD mestraSDDtag = null;
		MestraStylesMap mestraStyles = configuration.getMestraStyles(mestraFile.getFileType());

		int UniqueId = 1;
		
		Iterator<MestraMarker> Iter = mestraFile.getMestraMarkers().iterator();
		while (Iter.hasNext()){
			MestraMarker mestraMarker = Iter.next();

			String mestraStyleStr = mestraMarker.getMestraStyle().getMestraStyle();
			if (mestraStyleStr.equalsIgnoreCase(mestraStyles.getMainMandatoryStyle())) {
				if (mestraSDDtag != null) {
					this.add(mestraSDDtag);
				}
				
				mestraSDDtag = new MestraSDD(mestraStyles);
				String strMarker = mestraMarker.getMestraMarkerValue();
				mestraSDDtag.setUniqueId(UniqueId++);
				mestraSDDtag.setIdentifier(strMarker.trim());
				
				mestraSDDtag.setShortFileName(mestraFile.getShortFileName());
			} else {
				if (mestraStyleStr.equalsIgnoreCase(mestraStyles.getTraceabilityStyle())) {
					String strMarker = mestraMarker.getMestraMarkerValue();
					if (mestraSDDtag != null) {
						mestraSDDtag.setSRSimplementedRequirements(strMarker);
					}
				} else {
					
					Iterator<MestraStyle> IterTwo = mestraStyles.getValues();
					while (IterTwo.hasNext()) {
						MestraStyle mestraStyle = IterTwo.next();
						if (mestraStyle.getMestraStyle().equalsIgnoreCase(mestraStyleStr)) {
							
							if (mestraSDDtag != null) {
								mestraSDDtag.setMestraValue(mestraStyle.getMestraStyleHeader(), mestraMarker.getMestraMarkerValue(), configuration);
							} 
						}
					}
				}
			}
		}	
		
		// add the last one
		if (mestraSDDtag != null) {
			this.add(mestraSDDtag);
		}
	
	}

    public ArrayList<MestraSDD> getMestraSDD_Collection() {
        return mestraSDD_Collection;
    }


}
