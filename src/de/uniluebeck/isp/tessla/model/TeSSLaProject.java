package de.uniluebeck.isp.tessla.model;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import de.uniluebeck.isp.tessla.util.FileFilter;

public class TeSSLaProject {

	private String projectPath;
	private String outputDir;
	private String binName;
	
	public TeSSLaProject(String projectPath, String outputDir, String binName){
		this.projectPath = projectPath;
		this.outputDir = outputDir;
		this.binName = binName;
	}
	
	public List<File> getCFiles(){
		FileFilter filter = new FileFilter();
		File[] files = filter.find(projectPath, ".c");
		
		if(files == null || files.length == 0){
			return null;
		}
		
		return Arrays.asList(files);
	}
	
	public List<File> getTeSSLaFiles(){
		FileFilter filter = new FileFilter();
		File[] files = filter.find(projectPath, ".tessla");
		
		if(files == null || files.length == 0){
			return null;
		}
		
		return Arrays.asList(files);
	}
	
	public String getProjectPath() {
		return projectPath;
	}
	
	public void setProjectPath(String projectPath) {
		this.projectPath = projectPath;
	}
	
	public String getOutputDir() {
		return outputDir;
	}
	
	public void setOutputDir(String outputDir) {
		this.outputDir = outputDir;
	}
	
	public String getBinName() {
		return binName;
	}
	
	public void setBinName(String binName) {
		this.binName = binName;
	}
}
