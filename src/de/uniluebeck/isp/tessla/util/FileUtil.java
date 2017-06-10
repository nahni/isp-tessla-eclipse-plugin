package de.uniluebeck.isp.tessla.util;

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
}
