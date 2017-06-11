package de.uniluebeck.isp.tessla.util;

import java.io.File;

import org.apache.commons.lang.StringUtils;

public class FileUtil {

	public static String getBasename(String path){
		if(StringUtils.isBlank(path)){
			return null;
		}
//      The path.basename() methods returns the last portion of a path, similar to the Unix basename command.
//      For example:
//      path.basename('/foo/bar/baz/asdf/quux.html');
//      Returns: 'quux.html'
		if(path.endsWith("/")){
			path = path.substring(0, path.length() - 1);
		}
		
		path = path.substring(path.lastIndexOf("/")+1);
		
		return path;
	}
	
	public static String getRelativePath(String from, String to){
//		String path = "/var/data/stuff/xyz.dat";
//		String base = "/var/data";
//		String relative = new File(base).toURI().relativize(new File(path).toURI()).getPath();
		String relative = new File(from).toURI().relativize(new File(to).toURI()).getPath();

		return relative;
	}
}
