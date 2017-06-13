package de.uniluebeck.isp.tessla.ui.services;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;

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
		
//		dockerSerivce.startDocker(activeProject);
//		
////		docker exec tessla clang -o /tessla/sub_add_alternation/build/sub_add_alternation.bc -emit-llvm -S /tessla/sub_add_alternation/foo.c
//		System.out.println("onBuildCCode");
//		CCodeBuildService cCodeBuilder = new CCodeBuildService();
//		String[] cCodeArgs = cCodeBuilder.getBuildCCodeArgs();
//		dockerSerivce.runDockerCommand(cCodeArgs);
//		
//		
//		Thread.sleep(3000);
//		
////		docker exec tessla /usr/lib/llvm-3.8/bin/opt -load /InstrumentFunctions/libInstrumentFunctions.so -instrument_function_calls  build/sub_add_alternation.bc -instrument add -instrument sub -o build/instrumented/sub_add_alternation.bc
//		System.out.println("onPatchAssembly");
//		AssemblyService assemblyService = new AssemblyService();
//		String[] patchAssemblyArgs = assemblyService.getPatchAssemblyArgs();
//		//das hier funktioniert in Java iwie nicht, ka warum
////		dockerSerivce.runDockerCommand(patchAssemblyArgs);
//		dockerSerivce.runDockerCommand2(StringUtils.join(patchAssemblyArgs, " "));
//		
//		// docker exec tessla clang++ build/instrumented/sub_add_alternation.bc -o build/instrumented/sub_add_alternation -lzlog -lpthread -L/usr/local/lib -L/InstrumentFunctions -lLogger
//		System.out.println("onBuildAssembly");
//		String[] buildAssemblyArgs = assemblyService.getBuildAssemblyArgs();
//		dockerSerivce.runDockerCommand(buildAssemblyArgs);
//		
//		
//		
//		
//		
//		// docker exec tessla ./build/instrumented/sub_add_alternation
//		//geht immernoch nicht. Muss nachhelfen:
//		//docker exec -it tessla bash
//		// cd build
//		// ./instrumented_sub_add_alternation
//		System.out.println("RunPatchedBinary");
//		PatchedBinaryService patchedBinaryService = new PatchedBinaryService();
//		String[] runPatchedBinaryArgs = patchedBinaryService.getRunPatchedBinaryArgs();
////		dockerSerivce.runDockerCommand(runPatchedBinaryArgs);
//		dockerSerivce.runDockerCommand3(StringUtils.join(runPatchedBinaryArgs, " "));
//		
//		
////		rpc error: code = 2 desc = oci runtime error: exec failed: container_linux.go:247: starting container process caused "exec: \"./build/instrumented/sub_add_alternation\": permission denied"
//		//braucht gksu davor, aber gksu kann ich bei den anderen nicht vor packen weil dann fehler kommt: gksudo: Ungültige Option -- o
//		
		
		
		
		
		
		
		
		System.out.println("BuildTeSSLa");
		TeSSLaService teSSLaService = new TeSSLaService();
		String[] buildTeSSLaArgs = teSSLaService.getBuildTeSSLaArgs();
//		dockerSerivce.runDockerCommand(buildTeSSLaArgs);
		dockerSerivce.runCommandForBuildTeSSLa(StringUtils.join(buildTeSSLaArgs, " "), activeProject);
//		
		
		//Das hier geht iwie auch nur in der Konsole (also zumindest ohne Fehler in der Konsole, aber das was rauskommen soll kommt tortzdem nicht raus)
		//aber programmatisch aus Java kommt der Fehler: /tessla_server: Syntax error: Unterminated quoted string
		System.out.println("RunTeSSLa");
		String[] runTeSSLaArgs = teSSLaService.getRunTeSSLaArgs();
//		dockerSerivce.runDockerCommand(runTeSSLaArgs);
		dockerSerivce.runDockerCommand2(StringUtils.join(runTeSSLaArgs, " "));
		
		
//		dockerSerivce.removeContainer();
		
	}
	
	public void copyFiles(){
		activeProject = new TeSSLaProject(PROJECT_PATH, OUTPUT_DIR, BIN_NAME);
		
		//iwie sollte das wo anders hin oder die Methode umbeannt werden
		WorkingDirFileService workingDirFileService = new WorkingDirFileService(activeProject);
		workingDirFileService.createWorkingDir();
		workingDirFileService.transferFilesToContainer();
	}
	

}
