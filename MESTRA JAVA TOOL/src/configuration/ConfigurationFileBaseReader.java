package configuration;

import java.io.File;
import java.util.logging.Logger;

import mestra.MestraFileType;
import mestra.MestraFileType.MestraFileTypeEnum;

public class ConfigurationFileBaseReader {

	final static Logger logger = Logger.getLogger(ConfigurationFileBaseReader.class.getName()); 

	protected String configurationFilePath = "";
	protected String configurationFileDirectory = "";
	protected File configurationFile = null;

	protected KnownCSCIs known_CSCIs = null;
	protected MethodLevelSafety methodLevelSafety = null;

	// a map for each kind of document SSS SRS SDD MF TS
	protected ConfigurationMainMap configurationMainMap = null;

	public ConfigurationFileBaseReader() {
		this.configurationFile = null;
		this.configurationFilePath = "";
		this.configurationFileDirectory = "";
		
		this.configurationMainMap = new ConfigurationMainMap();
	}

	public ConfigurationFileBaseReader (String configurationFilePath) {
		this.configurationFile = null;
		setConfigurationFilePath( configurationFilePath );
		try {
			this.configurationFile = new File(configurationFilePath);
		} finally {
		}
		setExcelFileDirectory();
		this.configurationMainMap = new ConfigurationMainMap();

	}

	public ConfigurationFileBaseReader (File configurationFile) {

		setConfigurationFile ( configurationFile );
		setConfigurationFilePath( configurationFile.getAbsolutePath() );
		setExcelFileDirectory();
		this.configurationMainMap = new ConfigurationMainMap();

	}

	public void setExcelFileDirectory() {

		this.configurationFileDirectory = "";
		File file = new File(this.configurationFilePath);
		if (file.exists()) {
			if (file.getParentFile().isDirectory()) {
				logger.info( "parent directory = " + file.getParentFile().getAbsolutePath());
				this.configurationFileDirectory = file.getParentFile().getAbsolutePath();
			}
		}
	}


	public String getConfigurationFilePath() {
		return configurationFilePath;
	}

	public void setConfigurationFilePath(String configurationFilePath) {
		this.configurationFilePath = configurationFilePath;
	}

	public File getConfigurationFile() {
		return this.configurationFile;
	}

	public void setConfigurationFile(File configurationFile) {
		this.configurationFile = configurationFile;
	}

	public KnownCSCIs getKnown_CSCIs() {
		return known_CSCIs;
	}


	/**
	 * @return the methodLevelSafety
	 */
	public MethodLevelSafety getMethodLevelSafety() {
		return this.methodLevelSafety;
	}

	/**
	 * @return the mestraStyles_SSS
	 */
	public MestraStylesMap getMestraStyles_SSS() {
		return this.configurationMainMap.getMestraStyles("SSS");
	}

	/**
	 * @return the mestraStyles_SRS
	 */
	public MestraStylesMap getMestraStyles_SRS() {
		return this.configurationMainMap.getMestraStyles("SRS");
	}

	/**
	 * @return the mestraStyles_SDD
	 */
	public MestraStylesMap getMestraStyles_SDD() {
		return this.configurationMainMap.getMestraStyles("SDD");
	}

	/**
	 * @return the mestraStyles_MF
	 */
	public MestraStylesMap getMestraStyles_MF() {
		return this.configurationMainMap.getMestraStyles("MF");
	}

	/**
	 * @return the mestraStyles_TS
	 */
	public MestraStylesMap getMestraStyles_TS() {
		return this.configurationMainMap.getMestraStyles("TS");
	}

	/**
	 * returns the MestraStyles as extracted from the Configuration file
	 * 
	 */
	public MestraStylesMap getMestraStyles(MestraFileTypeEnum fileTypeEnum) {
		switch (fileTypeEnum) {

		case SSS:
			return this.configurationMainMap.getMestraStyles("SSS");
		case SRS:
			return this.configurationMainMap.getMestraStyles("SRS");
		case SDD:
			return this.configurationMainMap.getMestraStyles("SDD");
		case MF:
			return this.configurationMainMap.getMestraStyles("MF");
		case TS:
			return this.configurationMainMap.getMestraStyles("TS");
		default:
			return null;
		}
	}

	/**
	 * returns the MestraStyles as extracted from the Configuration file.
	 * 
	 */
	public MestraStylesMap getMestraStyles(MestraFileType mestraFileType) {
		switch (mestraFileType.getMestraFileType()) {
		case SSS:
			return this.configurationMainMap.getMestraStyles("SSS");
		case SRS:
			return this.configurationMainMap.getMestraStyles("SRS");
		case SDD:
			return this.configurationMainMap.getMestraStyles("SDD");
		case MF:
			return this.configurationMainMap.getMestraStyles("MF");
		case TS:
			return this.configurationMainMap.getMestraStyles("TS");
		default:
			return null;		
		}
	}
}
