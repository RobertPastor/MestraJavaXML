package FolderBrowser;


/*
 * Fichier com/eteks/outils/OutilsFichier.java
 *
 * Copyright (C) 2003-2006 Emmanuel PUYBARET / eTeks <info@eteks.com>. All Rights Reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
import java.io.File;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * retrieve a list of files with predefined extension (*.xls, .xml) from a predefined folder.
 * @author t0007330
 * @since June 2007
 *
 */
public class FolderBrowser implements FolderBrowserInterface {

	final static Logger logger = Logger.getLogger(FolderBrowser.class.getName()); 

    private String folderPath = "";

    public FolderBrowser(String FolderPath) {
        this.folderPath = FolderPath;
    }

    /**
     * Check that the file at the provided path exists and is a folder
     */
    public boolean FolderExists() {
        File file = new File (this.folderPath);
        if (file.exists()) {
            if (file.isDirectory()) {
                return true;
            }
        }
        return false;
    }
    /**
     * Returns the list of files of the folder provided in parameter<br>
     * and filtered with the provided file extensions: *.xls, *.xml
     */
    private File [] searchFileFolder (String[] fileExtensions, boolean recursiveSearch) throws IllegalArgumentException   {
        File file = new File (this.folderPath);
        logger.info( file.getAbsolutePath() );

        /*
         * if the folder does not exists
         * or if it is a file, throw an exception
         */
        if ( !file.exists() || !file.isDirectory() ) {
        	logger.log(Level.SEVERE , " file does not exist or is a directory ");
            throw new IllegalArgumentException (file + " unknown");
        }

        // Recursive search of files from the folder
        TreeSet<File> sortedFiles = new TreeSet<File>();
        searchFileFolder (sortedFiles , file , fileExtensions , recursiveSearch);

        // Copy the file names in an array of the same size
        File [] files = new File [sortedFiles.size()];
        sortedFiles.toArray(files);
        return files;
    }

    /**
     * Build a list of files with a defined extension(s)<br>
     * from a starting folder recursively browser the sub folders
     * 
     * @param files
     * @param folderPath
     * @param fileExtensions
     * @param recursiveSearch
     */
    private void searchFileFolder (TreeSet<File> files,
            File folderPath, 
            String[] fileExtensions,
            boolean recursiveSearch) {
    	
        logger.info( folderPath.getAbsolutePath() );
        /*
         * search sub folders 
         * and files with the expected extension
         */ 
        File [] filePath = folderPath.listFiles();
        for (int i = 0; i < filePath.length; i++) {
            // if the path is a folder
            logger.info( "Current file/folder: "+ filePath[i].getAbsolutePath());

            if (filePath[i].isDirectory() && (recursiveSearch == true))
                // Recursiv search of files from this folder
                searchFileFolder (files, filePath [i], fileExtensions, recursiveSearch);

            // add the file with the expected extension to the list
            else {
            	for (String fileExtension : fileExtensions) {
            		if (filePath[i].getName().endsWith(fileExtension)) {
            			logger.info( filePath[i].getAbsolutePath() );
            			files.add (filePath [i]);
            		}
            	}
            }
        }
    }

    /**
     * Return the list of files found from a given starting folder.
     */
    public File[] GetFilesInFolder(String[] FileExtension) throws IllegalArgumentException {
        return searchFileFolder (FileExtension,true);
    }
    
    public File[] GetFilesInFolder(String[] fileExtensions, boolean recursiveSearch) throws IllegalArgumentException {
    	return searchFileFolder (fileExtensions, recursiveSearch);
    }

}

