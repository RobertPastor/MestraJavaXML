package configuration;

import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;


public class MestraStyle {

	final static Logger logger = Logger.getLogger(MestraStyle.class.getName()); 

	// keys of the Mestra Base styles
	public static String[] mestraAttributes = {"mandatory" , "display", "title", "revision", "changes", "methodLevelSafety" , "traceability"};

	// the file containing the style
	private String MestraFileType = "";
	private String MestraStyle = "";
	private String MestraStyleHeader = "";
	private String MestraValue = "";

	// order in which the style is read from the configuration file
	private int order = 0;
	private Map<String, Boolean> attributesMap = null;
	
	/**
	 * Empty Constructor
	 */
	public MestraStyle(){

		this.MestraFileType = "";
		this.MestraStyle = "";
		this.MestraStyleHeader = "";
		this.MestraValue = "";

		this.attributesMap = new TreeMap<String, Boolean>();
		for (String mestraAttribute : mestraAttributes) {
			this.attributesMap.put(mestraAttribute, new Boolean(false));
		}
		this.order = 0;
	}

	/**
	 * Constructor
	 * @param aMestraStyle
	 */
	public MestraStyle(MestraStyle aMestraStyle) {

		this.MestraFileType = aMestraStyle.MestraFileType;
		this.MestraStyle = aMestraStyle.MestraStyle;
		this.MestraStyleHeader = aMestraStyle.MestraStyleHeader;
		this.MestraValue = aMestraStyle.MestraValue;

		this.attributesMap = new TreeMap<String, Boolean>();
		
		for (Map.Entry<String, Boolean> entry : aMestraStyle.attributesMap.entrySet()) {
			String key = entry.getKey();
			boolean value = entry.getValue();
			this.attributesMap.put(key, new Boolean(value));
		}
		this.order = aMestraStyle.order;
	}

	/**
	 * Constructor
	 * @param mestraStyle
	 * @param mestraStyleHeader
	 */
	public MestraStyle(String mestraStyle,
			String mestraStyleHeader){

		this.MestraFileType = "";
		this.MestraStyle = mestraStyle;
		this.MestraStyleHeader = mestraStyleHeader;
		this.MestraValue = "";

		this.attributesMap = new TreeMap<String, Boolean>();
		for (String mestraAttribute : mestraAttributes) {
			this.attributesMap.put(mestraAttribute, new Boolean(false));
		}
		this.order = 0;
	}


	/**
	 * Constructor
	 * @param mestraFileType
	 * @param mestraStyle
	 * @param mestraStyleHeader
	 * @param display
	 */
	public MestraStyle (String mestraFileType,
			String mestraStyle,
			String mestraStyleHeader) {

		this.MestraFileType = mestraFileType;
		this.MestraStyle = mestraStyle;
		this.MestraStyleHeader = mestraStyleHeader;
		this.MestraValue = mestraStyleHeader;

		this.attributesMap = new TreeMap<String, Boolean>();
		for (String mestraAttribute : mestraAttributes) {
			this.attributesMap.put(mestraAttribute, new Boolean(false));
		}
		this.order = 0;
	}


	public MestraStyle(String mestraFileType, 
			String mestraStyle,
			String mestraStyleHeader,
			Map<String, Boolean> _attributesMap, 
			int order) {

		this.MestraFileType = mestraFileType;
		this.MestraStyle = mestraStyle;
		this.MestraStyleHeader = mestraStyleHeader;
		this.MestraValue = "";

		this.attributesMap = new TreeMap<String, Boolean>();

		for (Map.Entry<String, Boolean> entry : _attributesMap.entrySet()) {
			String key = entry.getKey();
			boolean value = entry.getValue();
			this.attributesMap.put(key, new Boolean(value));
		}
		this.order = order;
	}

	public String getMestraStyle() {
		return MestraStyle;
	}

	public void setMestraStyle(String mestraStyle) {
		MestraStyle = mestraStyle;
	}

	public String getMestraStyleHeader() {
		return MestraStyleHeader;
	}

	public void setMestraStyleHeader(String mestraStyleHeader) {
		MestraStyleHeader = mestraStyleHeader;
	}

	/**
	 * @return the mestraDisplayStyle
	 */
	public boolean getDisplayMestraStyle() {
		return this.attributesMap.get("display");
	}
	
	
	/**
	 * @param mestraDisplayStyle the mestraDisplayStyle to set
	 */
	public void setDisplayMestraStyle(boolean mestraDisplayStyle) {
		logger.info ( "MestraStyle: set Display Mestra Style: " + mestraDisplayStyle);
		this.attributesMap.put("display", mestraDisplayStyle);
	}
	/**
	 * @return the mestraFileType
	 */
	public String getMestraFileType() {
		return this.MestraFileType;
	}
	/**
	 * @param mestraFileType the mestraFileType to set
	 */
	public void setMestraFileType(String mestraFileType) {
		this.MestraFileType = mestraFileType;
	}

	public boolean isMandatory() {
		if (this.attributesMap.containsKey("mandatory")) {
			return this.attributesMap.get("mandatory");
		}
		return false;
	}

	public boolean isTraceability() {
		if (this.attributesMap.containsKey("traceability")) {
			return this.attributesMap.get("traceability");
		}
		return false;
	}

	public int getOrder() {
		return this.order;
	}
	
	public Map<String, Boolean> getAttributesMap() {
		return attributesMap;
	}

	public boolean hasTitleAttribute() {
		if (this.attributesMap.containsKey("title")) {
			return this.attributesMap.get("title");
		}
		return false;
	}

	public String getTitleStyle() {
		for (Map.Entry<String, Boolean> entry : this.attributesMap.entrySet()) {
			String key = entry.getKey();
			if (key.equalsIgnoreCase("title")) {
				return this.MestraStyle;
			}
		}
		return "";
	}

	public boolean hasChangesAttribute() {
		if (this.attributesMap.containsKey("changes")) {
			return this.attributesMap.get("changes");
		}
		return false;
	}

	public String getChangesStyle() {
		for (Map.Entry<String, Boolean> entry : this.attributesMap.entrySet()) {
			String key = entry.getKey();
			if (key.equalsIgnoreCase("changes")) {
				return this.MestraStyle;
			}
		}
		return "";
	}

	public boolean hasRevisionAttribute() {
		if (this.attributesMap.containsKey("revision")) {
			return this.attributesMap.get("revision");
		}
		return false;
	}

	public boolean hasMethodLevelSafetyAttribute() {
		if (this.attributesMap.containsKey("methodLevelSafety")) {
			return this.attributesMap.get("methodLevelSafety");
		}
		return false;
	}

	public String getMethodLevelSafetyStyle() {
		for (Map.Entry<String, Boolean> entry : this.attributesMap.entrySet()) {
			String key = entry.getKey();
			if (key.equalsIgnoreCase("methodLevelSafety")) {
				return this.MestraStyle;
			}
		}
		return "";
	}

	public boolean hasTraceabilityAttribute() {
		if (this.attributesMap.containsKey("traceability")) {
			return this.attributesMap.get("traceability");
		}
		return false;
	}

	public String getTraceabilityStyle() {
		for (Map.Entry<String, Boolean> entry : this.attributesMap.entrySet()) {
			String key = entry.getKey();
			if (key.equalsIgnoreCase("traceability")) {
				return this.MestraStyle;
			}
		}
		return "";
	}

	public String getTraceabilityStyleHeader() {
		for (Map.Entry<String, Boolean> entry : this.attributesMap.entrySet()) {
			String key = entry.getKey();
			if (key.equalsIgnoreCase("traceability")) {
				return this.MestraStyleHeader;
			}
		}
		return "";
	}
	
	public void setMestraValue(String value) {
		if (this.MestraValue.length()>0) {
			this.MestraValue += " " + value;
		} else {
			this.MestraValue = value;
		}
	}
	
	public String getMestraValue() {
		return this.MestraValue;
	}

}
