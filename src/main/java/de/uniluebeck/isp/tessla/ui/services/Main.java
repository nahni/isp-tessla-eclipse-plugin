package de.uniluebeck.isp.tessla.ui.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.log4j.Logger;

import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.exceptions.DockerException;

import de.uniluebeck.isp.tessla.model.TeSSLaProject;
import de.uniluebeck.isp.tessla.util.PreferencesUtil;

public class Main {

	final static Logger logger = Logger.getLogger(Main.class);
	
	TeSSLaProject activeProject;
	
	public static void main(String[] args) throws FileNotFoundException, DockerCertificateException, IOException, DockerException, InterruptedException {
		Main main = new Main();
		main.copyFiles();
		main.run();
		
	}
	
	public void run() throws FileNotFoundException, DockerCertificateException, IOException, DockerException, InterruptedException{
		
		activeProject = PreferencesUtil.getTesslaProjectConfig();
		
		DockerService dockerSerivce = new DockerService(activeProject);
		CommandArgsService commandArgsService = new CommandArgsService(activeProject);
		
		String[] compileToLLVM_BC_Args = commandArgsService.getCompileToLLVM_BC_Args();
		dockerSerivce.runDockerCommandAvoidingWordSplitting2(compileToLLVM_BC_Args);
		
		String[] instrument_BC_Args = commandArgsService.getInstrument_BC_Args();
		dockerSerivce.runDockerCommandAvoidingWordSplitting2(instrument_BC_Args);
		
		String[] compileToBinary_Args = commandArgsService.getCompileToBinary_Args();
		dockerSerivce.runDockerCommandAvoidingWordSplitting2(compileToBinary_Args);
		
		String[] runBinary_Args = commandArgsService.getRunBinary_Args();
		dockerSerivce.runDockerCommandAvoidingWordSplitting2(runBinary_Args);
		
		String[] addStandardLibrary_Args = commandArgsService.getAddStandardLibrary_Args();
		dockerSerivce.runDockerCommandAvoidingWordSplitting2(addStandardLibrary_Args);

		//docker exec tessla sh -c cd /tessla && tessla spec2.tessla traces.log
		String[] runTeSSLa_Args = commandArgsService.getRunTeSSLa_Args();
		dockerSerivce.runDockerCommandAvoidingWordSplitting2(runTeSSLa_Args);
		
//		
//		dockerSerivce.stopContainer();
	}
	
	public void copyFiles(){
		activeProject = PreferencesUtil.getTesslaProjectConfig();
		
		//iwie sollte das wo anders hin oder die Methode umbeannt werden
		WorkingDirFileService workingDirFileService = new WorkingDirFileService(activeProject);
//		workingDirFileService.createWorkingDir();
//		workingDirFileService.transferFilesToContainer();
		workingDirFileService.createWorkingDirWithCopiedRessources();
	}

}
