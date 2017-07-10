package de.uniluebeck.isp.tessla.ui.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import de.uniluebeck.isp.tessla.model.TeSSLaProject;
import de.uniluebeck.isp.tessla.util.Constants;
import de.uniluebeck.isp.tessla.util.WorkingDirFileFilter;

public class WorkingDirFileService {

	final static Logger logger = Logger.getLogger(WorkingDirFileService.class);
	
	private TeSSLaProject activeProject;
	private ConsoleService consoleService;
	
	public WorkingDirFileService(TeSSLaProject activeProject){
		this.activeProject = activeProject;
		this.consoleService = ConsoleService.getInstance();
	}
	
	private void printStd(String text){
		consoleService.writeTo(Constants.STDOUT_CONSOLE_NAME, text);
	}
	
	private void printErr(String text){
		consoleService.writeTo(Constants.ERR_CONSOLE_NAME, text);
	}
	
	/**
	 * Erstelt das Verzeichnis auf dem Host-Computer, das in den Docker-Container gemappt wird 
	 * und kopiert die Dateien aus dem Workspace dorthin
	 */
	public void createWorkingDirWithCopiedRessources(){
		createWorkingDir();
		transferFilesToContainer();
	}
	
	/**
	 * Erstelt das Verzeichnis auf dem Host-Computer, das in den Docker-Container gemappt wird auf /tessla
	 */
	private void createWorkingDir(){
		printStd("ContainerDir: " + activeProject.getContainerDir());
		Path path = Paths.get(activeProject.getContainerDir());
		try {
			Files.createDirectories(path);
		} catch (IOException e) {
			logger.error("working directory could not be created", e);
			printErr(e.getMessage());
		}
	}
	
	/**
	 * Kopiert die Dateien aus dem Workspace in das Verzeichnis ContainerDir
	 */
	private void transferFilesToContainer(){
		
		try {
			FileUtils.cleanDirectory(new File(activeProject.getContainerDir()));
			Files.createDirectories(Paths.get(activeProject.getContainerDir()+"/build"));
		
			createZlogFile();
			
			File src = new File(activeProject.getProjectPath());
			File dest = new File(activeProject.getContainerDir());
			
			FileUtils.copyDirectory(src, dest, new WorkingDirFileFilter());
			
		} catch (IOException e) {
			logger.error("files from working directory couldn't be copied to container directory", e);
			printErr("files from working directory couldn't be copied to container directory");
		}
		
	}
	
	private void createZlogFile(){

		try {
			PrintWriter out = new PrintWriter(this.activeProject.getContainerDir() + "/" + "zlog.conf");
			
			String binName   = this.activeProject.getBinName();
			
			String atom_config_variableValueFormatting = "%m %d(%s) %us%n";
			String atom_config_functionCallFormatting = "%m nil %d(%s) %us%n";
			String formats = "[formats]\n";
			formats = formats + "variable_values = \"" + atom_config_variableValueFormatting + "\"\n";
			formats = formats + "function_calls = \""  + atom_config_functionCallFormatting + "\"\n";
			
			String rules = "[rules]\n";
			rules = rules + "variable_values_cat.DEBUG \"" + "instrumented_" + binName + ".trace\"; variable_values\n";
			rules = rules + "function_calls_cat.DEBUG \""  + "instrumented_" + binName + ".trace\"; function_calls\n";
			
			FileUtils.writeStringToFile(new File(this.activeProject.getContainerDir() + "/build/" + "zlog.conf"), formats + rules);
			
		} catch (FileNotFoundException e) {
			logger.error("zlog-File could not be created", e);
			printErr("zlog-File could not be created");
		} catch (IOException e) {
			logger.error("zlog-File could not be created", e);
			printErr("zlog-File could not be created");
		}
	}
	
	
	
}
 