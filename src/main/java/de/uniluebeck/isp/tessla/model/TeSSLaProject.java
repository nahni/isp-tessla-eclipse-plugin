package de.uniluebeck.isp.tessla.model;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import de.uniluebeck.isp.tessla.ui.Activator;
import de.uniluebeck.isp.tessla.ui.TesslaPreferencePage;
import de.uniluebeck.isp.tessla.util.FileFilter;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class TeSSLaProject {

	private String containerDir;
	private String dockerFile;
	public String getDockerFile() {
		return dockerFile;
	}

	private String outputDir;
	private String binName;
	
	public TeSSLaProject(){
	}
	
	
	public TeSSLaProject(String containerDir, String dockerFile){
		this.containerDir = containerDir;
		this.dockerFile = dockerFile;
	}
	
//	public String getDockerContainerPath(){
//		return loadPluginSettings(TesslaPreferencePage.DOCKER_FILE_PREFERENCE);
//	}
//	
//	private String loadPluginSettings(String key) {
//		IEclipsePreferences prefs =
//			    InstanceScope.INSTANCE.getNode(Activator.PLUGIN_ID);
//
//		return prefs.get(key, "def");
//	}

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
		//TODO
//		geht das nur im Plugin? So wirfts nen Fehler: Workspace is closed.
//		projectPath = ResourcesPlugin.getWorkspace().getRoot().getLocation().toString();
		
		System.out.println("Test workspacedir: " + ResourcesPlugin.getWorkspace().getRoot().getLocation().toString());
		
		//TODO das hier nur kurz fuer die Entwicklung. Wird spaeter der Workspace-Path
		IEclipsePreferences prefs = InstanceScope.INSTANCE.getNode(Activator.PLUGIN_ID);
		return prefs.get(TesslaPreferencePage.PROJECT_PATH_PREFERENCE, null);
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
//		if(StringUtils.isEmpty(containerDir)){
//			//TODO
//			this.containerDir = "/home/annika/.tessla-env";
//		}
		
		
//		return loadPluginSettings(TesslaPreferencePage.CONTAINER_DIR_PREFERENCE);
		
		return containerDir;
	}
}
