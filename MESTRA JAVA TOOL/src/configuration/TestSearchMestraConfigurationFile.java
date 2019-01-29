package configuration;

import java.io.File;
import java.util.logging.Logger;

import FolderBrowser.FolderBrowser;

public class TestSearchMestraConfigurationFile {

	final static Logger logger = Logger.getLogger(TestSearchMestraConfigurationFile.class.getName()); 

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String[] fileExtensions = {"xml","xls"};
		logger.info( getMestraConfigurationFilePath(fileExtensions) );
	}
	
	private static String getMestraConfigurationFilePath(String[] fileExtensions) {
		
		String MestraConfigurationFilePath = "";
		String rootPath = getMestraToolRootDirectory();
		logger.info( rootPath );
		FolderBrowser folderBrowser = new FolderBrowser(rootPath);
		File[] files = folderBrowser.GetFilesInFolder(fileExtensions, false);
		for (int i=0 ; i < files.length ; i++) {
			File file = files[i];
			logger.info( file.getName() );
			if (file.getName().startsWith("Mestra_Tool") && (file.getName().indexOf("V2")>0) || (file.getName().indexOf("V3")>0)) {
				logger.info( "Mestra File found : " + file.getName() );
				if (file.getName().indexOf("V2")>0) {
					logger.info( " V2 version");
				}
				if (file.getName().indexOf("V3")>0) {
					logger.info( " V3 version");
				}
				MestraConfigurationFilePath = file.getAbsolutePath();
			}
		}
		return MestraConfigurationFilePath;
	}
	
    private static String getMestraToolRootDirectory() {
    	
    	String root = "";
    	File[] roots = File.listRoots();
		for(int i = 0 ; i < roots.length ; i++) {
			File f = roots[i];
			String name = f.toString();
			logger.info( " Platform roots: "+name);
			File mestraToolDir = new File(name+"Mestra Tool");
			if (mestraToolDir.exists()) {
				logger.info( "Main Gui App: " + mestraToolDir.getAbsolutePath() + "..... exists!!");
				// we return the first one found
				return mestraToolDir.getAbsolutePath();
			}
		}
		return root;
    }

}
