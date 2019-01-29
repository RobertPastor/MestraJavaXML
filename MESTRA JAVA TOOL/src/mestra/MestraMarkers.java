package mestra;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * 
 * This class manages a rough list of MESTRA Markers that are fully UN-ORGANISED
 * as extracted from the WORD Document.
 * 
 * @since May 2007
 * @author Pastor Robert
 * 
*/
public class MestraMarkers {

	private ArrayList<MestraMarker> mestraMarkers;

	public MestraMarkers() {
		this.mestraMarkers = new ArrayList<MestraMarker>();
	}

	/**
	 * @return the mestraMarkers
	 */
	public ArrayList<MestraMarker> getMestraMarkers() {
		return this.mestraMarkers;
	}

	/**
	 * @param mestraMarkers the mestraMarkers to set
	 */
	public void setMestraMarkers(ArrayList<MestraMarker> mestraMarkers) {
		this.mestraMarkers = mestraMarkers;
	}

	public boolean add(MestraMarker mestraMarker) {
		if (this.mestraMarkers != null) {
			return this.mestraMarkers.add(mestraMarker);
		}
		return false;
	}

	public Iterator<MestraMarker> iterator() {
		return this.mestraMarkers.iterator();
	}
	
	public int size() {
		return this.mestraMarkers.size();
	}
	
	public MestraMarker getLast() {
		return this.mestraMarkers.get(this.size()-1);
	}

	public void clear() {
		this.mestraMarkers.clear();
	}
}
