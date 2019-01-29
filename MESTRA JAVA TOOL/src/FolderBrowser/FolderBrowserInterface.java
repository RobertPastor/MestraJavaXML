package FolderBrowser;

import java.io.File;

public interface FolderBrowserInterface {

    public File [] GetFilesInFolder (String[] FileExtension);
    public File [] GetFilesInFolder (String[] FileExtension, boolean recursiveSearch);
    public boolean FolderExists();
    
    
}
