package configuration;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.logging.Logger;

/**
 * This class stores the styles as extracted from the configuration file.<br>
 * private Map<String, MestraStyle> mestraStylesMap = null; <br>
 * 
 * @since June 2007
 * @author : Robert Pastor
 * 
 */
public class MestraStylesMap {

	final static Logger logger = Logger.getLogger(MestraStylesMap.class.getName()); 

	// we use here a SET to avoid duplicates in the MESTRA Styles
	//private ArrayList<MestraStyle> mestraStyles = null; 

	// a key and the related Mestra Styles
	// the String represents the Mestra Header hence the logical name such as Requirement, Changes, Revision , Allocation etc.
	private Map<String, MestraStyle> mestraStylesMap = null;

	/**
	 * Constructor
	 */
	public MestraStylesMap(){
		this.mestraStylesMap = new TreeMap<String, MestraStyle>();
	}
	
	public MestraStylesMap(Map<String, MestraStyle> _mestraStylesMap) {
		this.mestraStylesMap = _mestraStylesMap;
	}
	
	public int size() {
		return this.mestraStylesMap.size();
	}
	
	public MestraStylesMap deepCopy() {
		
		Map<String, MestraStyle> copy = new TreeMap<String, MestraStyle>();
		
		for(Entry<String, MestraStyle> entry : this.mestraStylesMap.entrySet()){
		    copy.put(entry.getKey(), new MestraStyle(entry.getValue()));
		}
		return new MestraStylesMap(copy);
	}
	
	/**
	 * Add a style to the List of MESTRA Styles
	 * @param mestraStyle
	 * @return
	 */
	public void add(MestraStyle mestraStyle) {
		logger.info( "Mestra Styles add: "+mestraStyle.getMestraStyle()+" header: "+mestraStyle.getMestraStyleHeader());
		mestraStylesMap.put(mestraStyle.getMestraStyleHeader(), new MestraStyle(mestraStyle.getMestraFileType(),
				mestraStyle.getMestraStyle(),
				mestraStyle.getMestraStyleHeader(),
				mestraStyle.getAttributesMap(),
				mestraStyle.getOrder()));
	}

	public void put(String mestraStyleHeader, MestraStyle mestraStyle ){
		this.mestraStylesMap.put(mestraStyleHeader, mestraStyle);
	}

	/**
	 * @return the mestraStyles
	 */
	public Map<String, MestraStyle> getMestraStylesMap() {
		return this.mestraStylesMap;
	}


	/**
	 * The main style is the style marked as MANDATORY
	 * Other secondary styles are found between 2 main styles
	 * @return String
	 */
	public String getMainMandatoryStyle() {

		for(Map.Entry<String, MestraStyle> entry : this.mestraStylesMap.entrySet()) {
			//String key = entry.getKey();
			MestraStyle mestraStyle = entry.getValue();
			
			if (mestraStyle.isMandatory()) {
				//logger.info( " it is a main mandatory style - style header = " + mestraStyle.getMestraStyleHeader() + " style value = " + mestraStyle.getMestraStyle());
				return mestraStyle.getMestraStyle();
			}
		}
		return "";
	}
	
	public String getMainMandatoryStyleHeader() {
		for(Map.Entry<String, MestraStyle> entry : this.mestraStylesMap.entrySet()) {
			//String key = entry.getKey();
			MestraStyle mestraStyle = entry.getValue();
			
			if (mestraStyle.isMandatory()) {
				//logger.info( " it is a main mandatory style - style header = " + mestraStyle.getMestraStyleHeader() );
				return mestraStyle.getMestraStyleHeader();
			}
		}
		return "";
	}
	
	
	public Iterator<MestraStyle> getValues() {
		
		return this.mestraStylesMap.values().iterator();
	}
	
	public String getStyle(int mestraStyleOrder) {
		
		for(Map.Entry<String, MestraStyle> entry : this.mestraStylesMap.entrySet()) {
			//String key = entry.getKey();
			MestraStyle mestraStyle = entry.getValue();
			
			if (mestraStyle.getOrder() == mestraStyleOrder) {
				return mestraStyle.getMestraStyle();
			}
		}
		return "";
	}

	public String getStyleHeader(int styleIndex) {

		for(Map.Entry<String, MestraStyle> entry : this.mestraStylesMap.entrySet()) {
			MestraStyle mestraStyle = entry.getValue();
			if (mestraStyle.getOrder() == styleIndex) {
				return mestraStyle.getMestraStyleHeader();
			}
		}
		return "";
	}

	public String getTitleStyle() {
		for(Map.Entry<String, MestraStyle> entry : this.mestraStylesMap.entrySet()) {
			MestraStyle mestraStyle = entry.getValue();
			if (mestraStyle.hasTitleAttribute()) {
				return mestraStyle.getTitleStyle();
			}
		}
		return "";
	}
	
	public String getTitleValue() {
		for(Map.Entry<String, MestraStyle> entry : this.mestraStylesMap.entrySet()) {
			MestraStyle mestraStyle = entry.getValue();
			if (mestraStyle.hasTitleAttribute()) {
				return mestraStyle.getMestraValue();
			}
		}
		return "";
	}
	
	public void setTitleValue(String value) {
		for(Map.Entry<String, MestraStyle> entry : this.mestraStylesMap.entrySet()) {
			MestraStyle mestraStyle = entry.getValue();
			if (mestraStyle.hasTitleAttribute()) {
				mestraStyle.setMestraValue(value);
			}
		}
	}

	public String getChangesStyle() {
		for(Map.Entry<String, MestraStyle> entry : this.mestraStylesMap.entrySet()) {
			MestraStyle mestraStyle = entry.getValue();
			if (mestraStyle.hasChangesAttribute()) {
				return mestraStyle.getChangesStyle();
			}
		}
		return "";
	}

	public String getRevisionStyle() {
		for(Map.Entry<String, MestraStyle> entry : this.mestraStylesMap.entrySet()) {
			MestraStyle mestraStyle = entry.getValue();
			if (mestraStyle.hasRevisionAttribute()) {
				return mestraStyle.getTitleStyle();
			}
		}
		return "";
	}

	public String getMethodLevelSafetyStyle() {
		for(Map.Entry<String, MestraStyle> entry : this.mestraStylesMap.entrySet()) {
			MestraStyle mestraStyle = entry.getValue();
			if (mestraStyle.hasMethodLevelSafetyAttribute()) {
				return mestraStyle.getMethodLevelSafetyStyle();
			}
		}
		return "";
	}

	public String getAllocationStyle() {
		for(Map.Entry<String, MestraStyle> entry : this.mestraStylesMap.entrySet()) {
			MestraStyle mestraStyle = entry.getValue();
			if (mestraStyle.hasTraceabilityAttribute()) {
				return mestraStyle.getTraceabilityStyle();
			}
		}
		return "";
	}
	
	public String getAllocationStyleHeader() {
		for(Map.Entry<String, MestraStyle> entry : this.mestraStylesMap.entrySet()) {
			MestraStyle mestraStyle = entry.getValue();
			if (mestraStyle.hasTraceabilityAttribute()) {
				return mestraStyle.getTraceabilityStyleHeader();
			}
		}
		return "";
	}
	
	
	public String getTraceabilityStyle() {
		for(Map.Entry<String, MestraStyle> entry : this.mestraStylesMap.entrySet()) {
			MestraStyle mestraStyle = entry.getValue();
			if (mestraStyle.hasTraceabilityAttribute()) {
				return mestraStyle.getTraceabilityStyle();
			}
		}
		return "";
	}
	
	public String getTraceabilityStyleHeader() {
		for(Map.Entry<String, MestraStyle> entry : this.mestraStylesMap.entrySet()) {
			MestraStyle mestraStyle = entry.getValue();
			if (mestraStyle.hasTraceabilityAttribute()) {
				return mestraStyle.getTraceabilityStyleHeader();
			}
		}
		return "";
	}


	public void setChangeHistoryValue(String changeHistoryValue) {
		for(Map.Entry<String, MestraStyle> entry : this.mestraStylesMap.entrySet()) {
			MestraStyle mestraStyle = entry.getValue();
			if (mestraStyle.hasChangesAttribute()) {
				mestraStyle.setMestraValue(changeHistoryValue);
			}
		}		
	}
	
	public boolean hasStyle(String wordFileStyleStr) {
		for(Map.Entry<String, MestraStyle> entry : this.mestraStylesMap.entrySet()) {
			MestraStyle mestraStyle = entry.getValue();
			if (mestraStyle.getMestraStyle().equalsIgnoreCase(wordFileStyleStr)) {
				return true;
			}
		}
		return false;
	}

	public String getMestraStyleHeader(String wordFileStyleStr) {
		for(Map.Entry<String, MestraStyle> entry : this.mestraStylesMap.entrySet()) {
			MestraStyle mestraStyle = entry.getValue();
			if (mestraStyle.getMestraStyle().equalsIgnoreCase(wordFileStyleStr)) {
				return mestraStyle.getMestraStyleHeader();
			}
		}
		return "";
	}

	public void setReleaseRevision(String releaseRevision) {
		for(Map.Entry<String, MestraStyle> entry : this.mestraStylesMap.entrySet()) {
			MestraStyle mestraStyle = entry.getValue();
			if (mestraStyle.hasRevisionAttribute()) {
				mestraStyle.setMestraValue(releaseRevision);
			}
		}		
	}

	public void setMethodLevelSafety(String strMethodLevelSafety,
			ConfigurationFileBaseReader aConfiguration) {
		for(Map.Entry<String, MestraStyle> entry : this.mestraStylesMap.entrySet()) {
			MestraStyle mestraStyle = entry.getValue();
			if (mestraStyle.hasMethodLevelSafetyAttribute()) {
				mestraStyle.setMestraValue(strMethodLevelSafety);
			}
		}	
		
	}
	
}
