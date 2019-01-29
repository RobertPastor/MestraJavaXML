package configuration;

import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;


/**
 * Configuration Map : a structure to relate the name of the Main MESTRA document (SSS, SRS, SDD, MF, TS) <br>
 * and a map of its related Mestra TAGs.<br>
 * 
 * @author t0007330
 * @since July 2016
 *
 */
public class ConfigurationMainMap {
	
	final static Logger logger = Logger.getLogger(ConfigurationMainMap.class.getName()); 

	/**
	 * the key here is a document name SSS SRS SDD MF TS
	 * there are specific classes for each of these documents
	 */
		
	private Map<String, MestraStylesMap> configurationMainMap = null;

	public ConfigurationMainMap() {
		
		this.setConfigurationMainMap(new TreeMap<String, MestraStylesMap>());
	}
	
	public void add(String documentName , MestraStylesMap mestraStylesMap) {
		logger.info( "Document Name = " + documentName);
		this.configurationMainMap.put(documentName, mestraStylesMap);
	}

	public Map<String, MestraStylesMap> getConfigurationMainMap() {
		return configurationMainMap;
	}

	public void setConfigurationMainMap(Map<String, MestraStylesMap> configurationMainMap) {
		this.configurationMainMap = configurationMainMap;
	}
	
	public MestraStylesMap getMestraStyles(String mestraDocument) {
		
		return this.configurationMainMap.get(mestraDocument);
	}

	public boolean hasSSSMap() {
		return this.configurationMainMap.get("SSS") != null;
	}

	public boolean hasSRSMap() {
		return this.configurationMainMap.get("SRS") != null;
	}

	public boolean hasSDDMap() {
		return this.configurationMainMap.get("SDD") != null;
	}

	public boolean hasMFMap() {
		return this.configurationMainMap.get("MF") != null;
	}

	public boolean hasTSMap() {
		return this.configurationMainMap.get("TS") != null;
	}

	public void createMap(String key, MestraStylesMap mestraStylesMap) {
		this.configurationMainMap.put(key, mestraStylesMap);	
	}
}
