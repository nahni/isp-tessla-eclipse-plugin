package de.uniluebeck.isp.tessla.ui.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.exceptions.DockerException;

import de.uniluebeck.isp.tessla.model.TeSSLaProject;

public class Main2 {

	private final static String PROJECT_PATH = "/home/annika/Entwicklung/Spielwiese/dummyProjectPath3/sub_add_alternation";

	private final static String OUTPUT_DIR = "";
	private final static String BIN_NAME = "";
	
	TeSSLaProject activeProject;
	
	public static void main(String[] args) throws FileNotFoundException, DockerCertificateException, IOException, DockerException, InterruptedException {
		Main2 main = new Main2();
		main.copyFiles();
		main.run();
	}
	
	public void run() throws FileNotFoundException, DockerCertificateException, IOException, DockerException, InterruptedException{
		activeProject = new TeSSLaProject(PROJECT_PATH, OUTPUT_DIR, BIN_NAME);
		
		DockerService2 dockerSerivce = new DockerService2();
//		dockerSerivce.startDocker(activeProject);
		
		Thread.sleep(3000);
		
		CommandArgsService commandArgsService = new CommandArgsService(activeProject);
		
		String[] compileToLLVM_BC_Args = commandArgsService.getCompileToLLVM_BC_Args();
//		dockerSerivce.runDockerCommand2(StringUtils.join(compileToLLVM_BC_Args, " "));
		dockerSerivce.runDockerCommandAvoidingWordSplitting2(compileToLLVM_BC_Args);
		
		Thread.sleep(3000);
		
		String[] instrument_BC_Args = commandArgsService.getInstrument_BC_Args();
//		dockerSerivce.runDockerCommand2(StringUtils.join(instrument_BC_Args, " "));
		dockerSerivce.runDockerCommandAvoidingWordSplitting2(instrument_BC_Args);
		
		Thread.sleep(3000);
		
		String[] compileToBinary_Args = commandArgsService.getCompileToBinary_Args();
//		dockerSerivce.runDockerCommand2(StringUtils.join(compileToBinary_Args, " "));
		dockerSerivce.runDockerCommandAvoidingWordSplitting2(compileToBinary_Args);
		
		Thread.sleep(3000);
		
		String[] runBinary_Args = commandArgsService.getRunBinary_Args();
//		dockerSerivce.runDockerCommand2(StringUtils.join(runBinary_Args, " "));
		dockerSerivce.runDockerCommandAvoidingWordSplitting2(runBinary_Args);
		
		Thread.sleep(3000);
		
		//das hier funktioniert nicht 
		//cat: '>': No such file or directory
//		cat: spec2.tessla: No such file or directory
		//gleicher Befehl auf der Konsole per Hand funktioniert
		String[] addStandardLibrary_Args = commandArgsService.getAddStandardLibrary_Args();
//		dockerSerivce.runDockerCommand2(StringUtils.join(addStandardLibrary_Args, " "));
//		dockerSerivce.runDockerCommand2(StringUtils.join(addStandardLibrary_Args, " "));
//		dockerSerivce.runDockerCommand2("'cat spec.tessla /usr/local/opt/tessla_rv/streams.tessla > spec2.tessla'");
//		dockerSerivce.runDockerCommand2("sh -c 'cd /tessla && cat spec.tessla /usr/local/opt/tessla_rv/streams.tessla > spec2.tessla'");
//		dockerSerivce.runDockerCommandCat();
//		dockerSerivce.runDockerCommandCat2(activeProject);
		
		List<String> args2 = new ArrayList<String>();
		args2.addAll(Arrays.asList("docker", "exec", "tessla", "sh", "-c", "cat spec.tessla /usr/local/opt/tessla_rv/streams.tessla > spec2.tessla"));
		
		String[] argsArray2 = new String[args2.size()];
		argsArray2 = args2.toArray(argsArray2);
		dockerSerivce.runDockerCommandAvoidingWordSplitting(argsArray2);
		
		Thread.sleep(3000);

		//das gleiche Problem: das geht mit docker exec nicht
//		sudo docker run --volume /home/annika/.tessla-env:/tessla --rm tessla sh -c 'cd /tessla && tessla spec2.tessla traces.log'
		String[] runTeSSLa_Args = commandArgsService.getRunTeSSLa_Args();
//		dockerSerivce.runDockerCommand2(StringUtils.join(runTeSSLa_Args, " "));
		
		//ganz komisch. AUs eclipse raus geht das nicht. Fehler:
		// /tessla: 1: /tessla: Syntax error: Unterminated quoted string
		//aber wenn ich das 1:1 so ins terminal kopiere funktionierts
//		dockerSerivce.runDockerCommandRunTessla(activeProject);
		
		// das hier duerfte funktionieren
		//sudo docker exec tessla sh -c 'cd /tessla && tessla spec2.tessla traces.log'
		List<String> args = new ArrayList<String>();
		args.addAll(Arrays.asList("docker", "exec", "tessla", "sh", "-c", "cd /tessla && tessla spec2.tessla traces.log"));
		
		String[] argsArray = new String[args.size()];
		argsArray = args.toArray(argsArray);
		dockerSerivce.runDockerCommandAvoidingWordSplitting(argsArray);
		
		
		
	}
	
	public void copyFiles(){
		activeProject = new TeSSLaProject(PROJECT_PATH, OUTPUT_DIR, BIN_NAME);
		
		//iwie sollte das wo anders hin oder die Methode umbeannt werden
		WorkingDirFileService workingDirFileService = new WorkingDirFileService(activeProject);
		workingDirFileService.createWorkingDir();
		workingDirFileService.transferFilesToContainer();
	}

}
