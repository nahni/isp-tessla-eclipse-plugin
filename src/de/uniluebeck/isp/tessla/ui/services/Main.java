package de.uniluebeck.isp.tessla.ui.services;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.exceptions.DockerException;

import de.uniluebeck.isp.tessla.model.TeSSLaProject;

public class Main {

	private final static String PROJECT_PATH = "/home/annika/Entwicklung/Spielwiese/dummyProjectPath2/sub_add_alternation";

	private final static String OUTPUT_DIR = "";
	private final static String BIN_NAME = "";

	TeSSLaProject activeProject;
	
	public static void main(String[] args) throws FileNotFoundException, DockerCertificateException, IOException, DockerException, InterruptedException {
		Main main = new Main();
//		main.copyFiles();
		main.buildCCode();

	}
	
	public void buildCCode() throws FileNotFoundException, DockerCertificateException, IOException, DockerException, InterruptedException{
		
		activeProject = new TeSSLaProject(PROJECT_PATH, OUTPUT_DIR, BIN_NAME);
		
		DockerService dockerSerivce = new DockerService();
		
		dockerSerivce.startDocker(activeProject);
		
//		System.out.println("onBuildCCode");
//		CCodeBuildService cCodeBuilder = new CCodeBuildService();
//		String[] cCodeArgs = cCodeBuilder.getBuildCCodeArgs();
//		dockerSerivce.runDockerCommand(cCodeArgs);
		
//		copyFiles();
		
		//das hier funktioniert in Java iwie nicht, ka warum
//		docker exec tessla /usr/lib/llvm-3.8/bin/opt -load /InstrumentFunctions/libInstrumentFunctions.so -instrument_function_calls  build/sub_add_alternation.bc -instrument add -instrument sub -o build/instrumented/sub_add_alternation.bc
//		System.out.println("onPatchAssembly");
		AssemblyService assemblyService = new AssemblyService();
//		String[] patchAssemblyArgs = assemblyService.getPatchAssemblyArgs();
//		dockerSerivce.runDockerCommand(patchAssemblyArgs);
		
		
		// docker exec tessla clang++ build/instrumented/sub_add_alternation.bc -o build/instrumented/sub_add_alternation -lzlog -lpthread -L/usr/local/lib -L/InstrumentFunctions -lLogger
//		System.out.println("onBuildAssembly");
//		String[] buildAssemblyArgs = assemblyService.getBuildAssemblyArgs();
//		dockerSerivce.runDockerCommand(buildAssemblyArgs);
		
		System.out.println("RunPatchedBinary");
		PatchedBinaryService patchedBinaryService = new PatchedBinaryService();
		String[] runPatchedBinaryArgs = patchedBinaryService.getRunPatchedBinaryArgs();
		dockerSerivce.runDockerCommand(runPatchedBinaryArgs);
		
//		System.out.println("BuildTeSSLa");
//		TeSSLaService teSSLaService = new TeSSLaService();
//		String[] buildTeSSLaArgs = teSSLaService.getBuildTeSSLaArgs();
//		dockerSerivce.runDockerCommand(buildTeSSLaArgs);
//		
//		System.out.println("RunTeSSLa");
//		String[] runTeSSLaArgs = teSSLaService.getRunTeSSLaArgs();
//		dockerSerivce.runDockerCommand(runTeSSLaArgs);
		
		
	}
	
	public void copyFiles(){
		activeProject = new TeSSLaProject(PROJECT_PATH, OUTPUT_DIR, BIN_NAME);
		
		//iwie sollte das wo anders hin oder die Methode umbeannt werden
		WorkingDirFileService workingDirFileService = new WorkingDirFileService(activeProject);
		workingDirFileService.createWorkingDir();
		workingDirFileService.transferFilesToContainer();
	}
	

}
