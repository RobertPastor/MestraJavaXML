package configuration;

import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import FolderBrowser.FolderBrowser;


/**
 * this class is responsible for finding the XLS or XML configuration files
 * storing them in a MAP and launching their analysis.
 * If an XLS file is available , then the V2 configuration is retrieved and the EXCEL table view is displayed.<br>
 * If an XML file is available , then the V3 configuration is retrieved and the XML tree view is displayed.<br>
 * 
 * @author t0007330
 * @since July 2016
 *
 */
public class ConfigurationFilesFactory {

	final static Logger logger = Logger.getLogger(ConfigurationFilesFactory.class.getName()); 

	// first String = file Extension (xls or xml) and second String = file Path
	private String[] fileExtensions = {"xml","xls"};
	// file prefix WARNING with UNDERSCORE
	private String configurationFilePrefix = "Mestra_Tool";
	// mestra root folder : MESTRA TOOL with UNDERSCORE as in file prefix !!!
	private String mestraRootFolderName = "";
	
	private ConfigurationFileXLSReader configurationFileXLSReader = null;
	private ConfigurationFileXMLReader configurationFileXMLReader = null;
	
	private Map<String, String> configurationFilePathMap = null;
	private Map<String, ConfigurationFileBaseReader> configurationBaseReaderMap = null;
	
	public ConfigurationFilesFactory( String mestraRootFolderName) {

		this.mestraRootFolderName = mestraRootFolderName;
		this.configurationFilePathMap = new TreeMap<String , String>();
		this.configurationBaseReaderMap = new TreeMap<String , ConfigurationFileBaseReader>();
		getMestraConfigurationFilePath(mestraRootFolderName);
	}

	/**
	 * new method is to search a file with a specific pattern in the file name.
	 * Warning: several file paths (with xls or xml extensions) can be returned !!!
	 */
	private void getMestraConfigurationFilePath( String mestraRootFolderName ) {

		String rootPath = getMestraToolRootDirectory(mestraRootFolderName);
		logger.info( rootPath );
		
		FolderBrowser folderBrowser = new FolderBrowser(rootPath);
		try {
			File[] files = folderBrowser.GetFilesInFolder(fileExtensions, false);
			for (int i=0 ; i < files.length ; i++) {
				File file = files[i];
				logger.info( file.getName() );
				if (file.getName().startsWith(this.configurationFilePrefix) 
						&& (file.getName().indexOf("V2")>0) || (file.getName().indexOf("V3")>0)) {
					
					logger.info( "Mestra File found : " + file.getName() );
					if (file.getName().indexOf("V2")>0) {
						logger.info( "Mestra V2 tool version");
					}
					if (file.getName().indexOf("V3")>0) {
						logger.info( "Mestra V3 tool version");
					}
					if (file.getName().endsWith(".xls")){
						this.configurationFilePathMap.put("xls", file.getAbsolutePath() );
						//launch EXCEL file analysis
						this.configurationFileXLSReader = new ConfigurationFileXLSReader(file);
						this.configurationBaseReaderMap.put("xls", this.configurationFileXLSReader);
						
					}
					if (file.getName().endsWith(".xml")){
						this.configurationFilePathMap.put("xml", file.getAbsolutePath() );
						// launch XML file analysis
						this.configurationFileXMLReader = new ConfigurationFileXMLReader(file);
						this.configurationBaseReaderMap.put("xml", this.configurationFileXMLReader);
					}
				}
			}
		} catch (IllegalArgumentException e) {
			logger.log(Level.SEVERE , "Exception = " + e.getLocalizedMessage() );
		}
	}

	/**
	 * checks whether a configuration file is existing
	 */
	public boolean checkXMLConfigurationFileExists() {

		boolean configurationFileExists = false;
		// we have to browse through the map
		Iterator<Map.Entry<String, String>> Iter = this.configurationFilePathMap.entrySet().iterator();
		while (Iter.hasNext()) {
			Map.Entry<String, String> entry = Iter.next();
			
			if (entry.getKey().equalsIgnoreCase("xml")) {
				configurationFileExists = true;
			}
		}
		return configurationFileExists;
	}
					
		
	/**
	 * looking for a drive C: or D: or E: having a folder called "Mestra Tool"
	 * @return folder absolute path
	 */
	public String getMestraToolRootDirectory(String folderName) {

		String root = "";
		// get file systems roots
		File[] roots = File.listRoots();
		for(int i = 0 ; i < roots.length ; i++) {
			File file = roots[i];
			String name = file.toString();
			logger.info( "Main Gui App: Platform roots: "+name);
			
			// mestra Root Folder Name = "MESTRA TOOL"
			File mestraToolDir = new File(name + mestraRootFolderName);
			if (mestraToolDir.exists()) {
				logger.info( "Configuration Files Factory : " + mestraToolDir.getAbsolutePath() + "..... exists!!");
				// we return the first one found
				return mestraToolDir.getAbsolutePath();
			}
		}
		return root;
	}
	
	
	public String getConfigurationFilePath(String fileExtension) {
		
		// we have to browse through the map
		Iterator<Map.Entry<String, String>> Iter = this.configurationFilePathMap.entrySet().iterator();
		while (Iter.hasNext()) {
			Map.Entry<String, String> entry = Iter.next();
			
			if (entry.getKey().equalsIgnoreCase(fileExtension)) {
				return entry.getValue();
			}
			if (entry.getKey().equalsIgnoreCase(fileExtension)) {
				return entry.getValue();
			}
		}
		return "";
	}
	
	public Map<String, String> getConfigurationFilesMap() {

		return this.configurationFilePathMap;
	}

	public void setConfigurationFilesMap(Map<String, String> configurationFilePathMap) {
		this.configurationFilePathMap = configurationFilePathMap;
	}

	public boolean isV2_XLS() {
		boolean configurationXLS = false;
		boolean configurationXML = false;
		// we have to browse through the map
		Iterator<Map.Entry<String, String>> Iter = this.configurationFilePathMap.entrySet().iterator();
		while (Iter.hasNext()) {
			Map.Entry<String, String> entry = Iter.next();
					
			if (entry.getKey().equalsIgnoreCase("xls")) {
				configurationXLS = true;
			}
			if (entry.getKey().equalsIgnoreCase("xml")) {
				configurationXML = true;
			}
			if (configurationXLS && !configurationXML) {
				configurationXLS = true;
			}
			if (configurationXML) {
				configurationXLS = false;
			}
		}
		return configurationXLS;
	}
	
	public boolean isV3_XML() {
		
		boolean configurationXLS = false;
		boolean configurationXML = false;
		// we have to browse through the map
		Iterator<Map.Entry<String, String>> Iter = this.configurationFilePathMap.entrySet().iterator();
		while (Iter.hasNext()) {
			Map.Entry<String, String> entry = Iter.next();
					
			if (entry.getKey().equalsIgnoreCase("xls")) {
				configurationXLS = true;
			}
			if (entry.getKey().equalsIgnoreCase("xml")) {
				configurationXML = true;
			}
			if (configurationXLS && !configurationXML) {
				configurationXML = false;
			}
			if (configurationXML) {
				configurationXML = true;
			}
		}
		return configurationXML;
	}

	public ConfigurationFileBaseReader getConfigurationBaseReader() {
		if (this.isV2_XLS()) {
			return this.configurationBaseReaderMap.get("xls");
		}
		if (this.isV3_XML()) {
			return this.configurationBaseReaderMap.get("xml");
		}
		return null;
	}
	
	public void seConfigurationBaseReader(ConfigurationFileBaseReader configurationFileBaseReader) {
		if (configurationFileBaseReader instanceof ConfigurationFileXLSReader) {
			this.configurationBaseReaderMap.put("xls", configurationFileBaseReader);
		}
		if (configurationFileBaseReader instanceof ConfigurationFileXMLReader) {
			this.configurationBaseReaderMap.put("xml", configurationFileBaseReader);
		}
		
	}
}
