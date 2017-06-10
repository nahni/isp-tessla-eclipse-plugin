package de.uniluebeck.isp.tessla.util;

import java.io.File;

public class WorkingDirFileFilter implements java.io.FileFilter{

	@Override
	public boolean accept(File file) {
		
		if(file == null){
			return false;
		}
		
		String path = file.getAbsolutePath();
		
		//do not copy gcc flags
		if(FileUtil.getBasename(path).equals(".gcc-flags.json")){
			return false;
		}
		
		//do not copy build directory
		if(FileUtil.getBasename(path).equals("build")){
			return false;
		}
		
		return true;
	}

}
