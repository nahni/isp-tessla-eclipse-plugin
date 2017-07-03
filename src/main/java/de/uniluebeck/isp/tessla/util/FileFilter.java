package de.uniluebeck.isp.tessla.util;

import java.io.File;
import java.io.FilenameFilter;

public class FileFilter {

	/**
	 * 
	 * @param dirName, the path to the directory to search in
	 * @param fileExtension, the file extension of the files which sould be returned (e.g.: .txt)
	 * @return
	 */
    public File[] find(String dirName, String fileExtension){
        File dir = new File(dirName);

        return dir.listFiles(new FilenameFilter() { 
                 public boolean accept(File dir, String filename)
                      { return filename.endsWith(fileExtension); }
        } );

    }
	
}
