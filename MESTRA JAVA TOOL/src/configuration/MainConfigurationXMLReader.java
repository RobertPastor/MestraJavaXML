package configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

public class MainConfigurationXMLReader {

	final static Logger logger = Logger.getLogger(MainConfigurationXMLReader.class.getName()); 

	private static ConfigurationFileXMLReader xmlConfigurationFileReader = null;

	public static void main(String[] args) {

		logger.info( " =========== main starting ===============");

		String filePath = "D:\\Users\\T0007330\\Documents\\03 - Languages_IDE_and_Tools\\06 - JAVA\\MESTRA JAVA TOOL\\src\\configuration\\Mestra_Tool_V3.xml";
		String shortFileName = "Mestra_Tool_V3.xml";
		InputStream inputStream = MainConfigurationXMLReader.class.getResourceAsStream(shortFileName);
		if (inputStream != null) {

			logger.info( " resource found ");
			xmlConfigurationFileReader = new ConfigurationFileXMLReader(filePath, null);
			xmlConfigurationFileReader.parseXmlFile();
		}

		logger.info( " ============= main ending ================ ");
		try {
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}




}
