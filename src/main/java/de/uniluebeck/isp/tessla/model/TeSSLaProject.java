package de.uniluebeck.isp.tessla.model;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import de.uniluebeck.isp.tessla.util.FileFilter;
import org.eclipse.core.resources.ResourcesPlugin;

public class TeSSLaProject {

	private String outputDir;
	private String binName;
	
	public TeSSLaProject(){
	}
	
	public List<File> getCFiles(){
		FileFilter filter = new FileFilter();
		File[] files = filter.find(getProjectPath(), ".c");
		
		if(files == null || files.length == 0){
			return null;
		}
		
		return Arrays.asList(files);
	}
	
	public List<File> getTeSSLaFiles(){
		FileFilter filter = new FileFilter();
		File[] files = filter.find(getProjectPath(), ".tessla");
		
		if(files == null || files.length == 0){
			return null;
		}
		
		return Arrays.asList(files);
	}
	
	public String getProjectPath() {
//		geht das nur im Plugin? So wirfts nen Fehler: Workspace is closed.
//		projectPath = ResourcesPlugin.getWorkspace().getRoot().getLocation().toString();
//		System.out.println(projectPath);
		String projectPath = "/home/annika/Entwicklung/Spielwiese/dummyProjectPath3/sub_add_alternation";
		return projectPath;
	}
	
	public String getOutputDir() {
		return outputDir;
	}
	
	public void setOutputDir(String outputDir) {
		this.outputDir = outputDir;
	}
	
	/**
	 * der BinName ist einfach der Orndername des Projekts
	 * @return
	 */
	public String getBinName() {
		if(StringUtils.isBlank(binName)){
//      The path.basename() methods returns the last portion of a path, similar to the Unix basename command.
//      For example:
//      path.basename('/foo/bar/baz/asdf/quux.html');
//      Returns: 'quux.html'
//    this.binName = path.basename(this.projPath).replace(' ', '_')
			String project = getProjectPath();
			if(project.endsWith("/")){
				project = project.substring(0, project.length() - 1);
			}
			
			project = project.substring(project.lastIndexOf("/")+1);
			
			this.binName = project;
		}
		
		return this.binName;
	}
	
	public void setBinName(String binName) {
		this.binName = binName;
	}
	
	public String getContainerDir(){
		//Das Eclipse muss im Moment als Admin gestratet werden, daher waere das ContainerDir
		// root/.tessla zu Entwicklungszewecken hab ich das mal umgebogen
//		return System.getProperty("user.home") + "/" + ".tessla-env";
		
		return "/home/annika/.tessla-env";
	}
}
