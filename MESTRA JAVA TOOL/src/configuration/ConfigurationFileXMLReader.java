package configuration;

import gui.StatusBar;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Read an XML configuration file.
 * @since July 2016
 * @author Robert PASTOR
 */
public class ConfigurationFileXMLReader extends ConfigurationFileBaseReader {
	
	final static Logger logger = Logger.getLogger(ConfigurationFileXMLReader.class.getName()); 
	
	private StatusBar statusBar = null;
	private boolean ConfigurationOK = false;

	public ConfigurationFileXMLReader(File file) {
		super(file);
		parseXmlFile();
	}

	public ConfigurationFileXMLReader(String _xmlConfigurationFilePath, StatusBar statusBar) {
		super(_xmlConfigurationFilePath);
		this.setStatusBar(statusBar);
		parseXmlFile();
	}

	public void parseXmlFile(){
		
		logger.info( "parse Xml File =  " + this.configurationFilePath);
		this.ConfigurationOK = false;

		//get the factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		try {

			//Using factory get an instance of document builder
			DocumentBuilder builder  = dbf.newDocumentBuilder();

			File xmlConfigurationFile = new File(this.configurationFilePath);

			if (xmlConfigurationFile.exists() && !xmlConfigurationFile.isDirectory()) {
				logger.info( "file with path: " + this.configurationFilePath + " is existing ");

				//parse using builder to get DOM representation of the XML file
				Document document = builder.parse(xmlConfigurationFile);

				//Affiche la version de XML
				logger.info( "xml version = " + document.getXmlVersion());

				//Affiche l'encodage
				logger.info( "xml encoding = " + document.getXmlEncoding());	

				//Affiche s'il s'agit d'un document standalone		
				logger.info( "is stand alone: "  + document.getXmlStandalone());
				logger.info( "======================");

				final Element racine = document.getDocumentElement();
				logger.info( " root = " + racine.getNodeName());

				final NodeList racineNoeuds = racine.getChildNodes();
				logger.info( "======================");

				final int nbRacineNoeuds = racineNoeuds.getLength();
				logger.info( "======================");

				for (int i = 0; i < nbRacineNoeuds; i++) {
					
					if(racineNoeuds.item(i).getNodeType() == Node.ELEMENT_NODE) {
						
						final Node configSubNode = racineNoeuds.item(i);

						logger.info( "Element Node = " + configSubNode.getNodeName());
						
						if (configSubNode.getNodeName().equalsIgnoreCase("CSCI")) {
							logger.info(" =========== it is the CSCI main node ============== ");
							readCSCI_Names ( configSubNode );
						}
						
						if (configSubNode.getNodeName().equalsIgnoreCase("methodLevel")) {
							logger.info(" =========== it is the METHOD LEVEL main node ============== ");
							readMethodLevelNodes( configSubNode );
						}
						
						if (configSubNode.getNodeName().equalsIgnoreCase("mestra")) {
							logger.info(" =========== it is the MESTRA main node ============== ");
							readMestraSubNodes( configSubNode );
						}
					}
				}
				// set configuration is OK
				this.ConfigurationOK = true;
			}else {
				logger.log(Level.SEVERE, " =========> file is NOT existing ");
			}

		} catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch(SAXException se) {
			se.printStackTrace();
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	private void readMestraSubNodes(Node mestraMainNode) {
		
		this.configurationMainMap = new ConfigurationMainMap();
		
		final NodeList mestraChildNodes = mestraMainNode.getChildNodes();
		for (int j=0 ; j < mestraChildNodes.getLength() ; j++) {
			
			if ( mestraChildNodes.item(j).getNodeType() == Node.ELEMENT_NODE ) {
				
				final Node mestra_file_node = mestraChildNodes.item(j);
				//logger.info( "mestra Node = " + mestra_file_node.getNodeName() );
				
				String documentName = mestra_file_node.getNodeName() ;
				//logger.info( "================= mestra document name = " + mestra_file_node.getNodeName() + " ================== ");

				MestraStylesMap mestraStylesMap = new MestraStylesMap();
								
				// create a Map with this key as entry
				final NodeList documentChildNodes = mestra_file_node.getChildNodes();
				int order = 1;
				for (int k=0; k < documentChildNodes.getLength() ; k++) {
					
					if (documentChildNodes.item(k).getNodeType() == Node.ELEMENT_NODE) {
						
						Node mestraTag = documentChildNodes.item(k);
						//logger.info( " document name = " + documentName + " tag node = " + mestraTag.getNodeName() + " mestra style = " + mestraTag.getTextContent());
						String mestraFile = documentName;
						String mestraStyle = mestraTag.getTextContent();
						String mestraStyleHeader = mestraTag.getNodeName();
						
						Map<String, Boolean> attributesMap = new TreeMap<String, Boolean>();
						
						final Element mestraTagElement = (Element) mestraTag;
						//NamedNodeMap namedNodeMap = mestraTagElement.getAttributes();
						
						for (String attribute : MestraStyle.mestraAttributes) {
							String attributeValue = mestraTagElement.getAttribute(attribute);
							if (attributeValue.equalsIgnoreCase("true")) {
								//logger.info("attribute = " + attribute + " --- value = " + attributeValue);
							} else {
								//logger.info("attribute = " + attribute + " --- value not SET => false " );
							}
							attributesMap.put( attribute , attributeValue.equalsIgnoreCase("true"));
						}
						
						//logger.info( " order = " + order);
						mestraStylesMap.put(mestraStyleHeader, new MestraStyle(mestraFile, mestraStyle, mestraStyleHeader, attributesMap , order++));
					}
				}
				this.configurationMainMap.add(documentName, mestraStylesMap);
			}
		}
	}
	
	private void readMethodLevelNodes(Node methodLevelMainNode) {
		
		this.methodLevelSafety = new MethodLevelSafety();

		final NodeList methodLevelChildNodes = methodLevelMainNode.getChildNodes();
		
		for (int j=0 ; j < methodLevelChildNodes.getLength() ; j++) {
			if ( methodLevelChildNodes.item(j).getNodeType() == Node.ELEMENT_NODE ) {
				
				final Node methodLevelSubNode = methodLevelChildNodes.item(j);
				//logger.info( "method Level Node = " + methodLevelSubNode.getNodeName() );
				if (methodLevelSubNode.getNodeName() == "IADT") {
					
					//logger.info(" IADT sub node ");
					final NodeList IADTChildNodes = methodLevelSubNode.getChildNodes();
					for (int k=0; k < IADTChildNodes.getLength() ; k++ ) {
						if ( IADTChildNodes.item(k).getNodeType() == Node.ELEMENT_NODE ) {
							
							final Node IADTsubNode = IADTChildNodes.item(k);
							//logger.info( " IADT sub nodes " + IADTsubNode.getNodeName() + " - text content = " +  IADTsubNode.getTextContent());
							this.methodLevelSafety.addMethod(IADTsubNode.getTextContent());
						}
					}
					
				}
				if (methodLevelSubNode.getNodeName() == "Level") {
					//logger.info( " Level sub node ");
					
					final NodeList levelChildNodes = methodLevelSubNode.getChildNodes();
					for (int k=0; k < levelChildNodes.getLength() ; k++ ) {
						if ( levelChildNodes.item(k).getNodeType() == Node.ELEMENT_NODE ) {
							
							final Node levelSubNode = levelChildNodes.item(k);
							//logger.info( " Level sub nodes " + levelSubNode.getNodeName() + " - text content = " +  levelSubNode.getTextContent());
							this.methodLevelSafety.addLevel(levelSubNode.getTextContent());
						}
					}
				}
			}
		}
	}
	
	private void readCSCI_Names(Node csciMainNode) {

		this.known_CSCIs = new KnownCSCIs();
		// Iterate over each cell in the row and print out the cell's content
		final NodeList mestraChildNodes = csciMainNode.getChildNodes();
		
		for (int j=0 ; j < mestraChildNodes.getLength() ; j++) {
			if ( mestraChildNodes.item(j).getNodeType() == Node.ELEMENT_NODE ) {
				final Node CSCI_node = mestraChildNodes.item(j);
				//logger.info( "CSCI Node = " + CSCI_node.getNodeName() + " text content = " + CSCI_node.getTextContent());
				this.known_CSCIs.add( CSCI_node.getTextContent() );
			}
		}
	}
	
	
	/**
	 * @return the configurationOK
	 */
	public boolean isConfigurationOK() {
		return this.ConfigurationOK;
	}

	public StatusBar getStatusBar() {
		return statusBar;
	}

	public void setStatusBar(StatusBar statusBar) {
		this.statusBar = statusBar;
	}
}
