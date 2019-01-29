package mestra;

import configuration.MestraStyle;


/**
 * 
 * This class manages one MESTRA Marker as extracted from the WORD Document.
 * 
 * A Mestra Marker is composed of
 * 1) a style as expected to found in the file (provided the file type)
 * 2) a String representing the value of the paragraph with the above style
 * 
 * @since March 2007
 * @author Robert Pastor
 * 
 */
public class MestraMarker {

	private String mestraMarkerValue;
	private MestraStyle mestraStyle;
	
	public MestraMarker() {
		this.mestraMarkerValue = new String();
		this.mestraStyle = new MestraStyle();
	}
	
	public MestraMarker(MestraStyle aMestraStyle) {
		this.mestraMarkerValue = new String();
		this.mestraStyle = new MestraStyle(aMestraStyle);
	}
	
	public MestraMarker(String strMarker, MestraStyle aMestraStyle) {
		this.mestraMarkerValue = strMarker;
		this.mestraStyle = new MestraStyle(aMestraStyle);
	}

	/**
	 * @return the mestraMarker
	 */
	public String getMestraMarkerValue() {
		return this.mestraMarkerValue;
	}
	/**
	 * @param mestraMarker the mestraMarker to set
	 */
	public void setMestraMarkerValue(String mestraMarkerValue) {
		this.mestraMarkerValue = mestraMarkerValue;
		this.mestraStyle.setMestraValue(mestraMarkerValue);
	}
	/**
	 * @return the mestraStyle
	 */
	public MestraStyle getMestraStyle() {
		return this.mestraStyle;
	}
	/**
	 * @param mestraStyle the mestraStyle to set
	 */
	public void setMestraStyle(MestraStyle mestraStyle) {
		this.mestraStyle = mestraStyle;
	}
}
