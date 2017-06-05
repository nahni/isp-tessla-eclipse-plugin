package de.uniluebeck.isp.tessla.ui.services;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.LogStream;
import com.spotify.docker.client.DefaultDockerClient.Builder;
import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.ContainerConfig;
import com.spotify.docker.client.messages.ContainerCreation;
import com.spotify.docker.client.messages.ContainerInfo;
import com.spotify.docker.client.messages.ExecCreation;
import com.spotify.docker.client.messages.HostConfig;
import com.spotify.docker.client.messages.PortBinding;

import de.uniluebeck.isp.tessla.model.TeSSLaProject;
import de.uniluebeck.isp.tessla.util.TeSSLaFileManager;

public class PatchAssemblyService {

//	private final static String PROJECT_PATH = "C:/Annika/Studium/3 Semester/SSE Projekt/TeSSLa Plugin/Dateien zum ausprobieren/dummyProjectPath/sub_add_alternation";
	private final static String PROJECT_PATH = "/home/annika/geteilt/dummyProjectPath/sub_add_alternation";
	
	private final static String OUTPUT_DIR = "";
	private final static String BIN_NAME = "";
	
	TeSSLaProject activeProject;
	
	public void start(){
		activeProject = new TeSSLaProject(PROJECT_PATH, OUTPUT_DIR, BIN_NAME);
		
		try {
			startDocker();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DockerCertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DockerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void startDocker() throws DockerCertificateException, FileNotFoundException, IOException, DockerException, InterruptedException {
		Builder builder = DefaultDockerClient.fromEnv();
		builder.connectTimeoutMillis(60000);
		builder.readTimeoutMillis(60000);
		
		final DockerClient docker = builder.build();
		
//		final File imageFile = new File("C:/Annika/Studium/3 Semester/SSE Projekt/TeSSLa Plugin/Atom/tessla");
		final File imageFile = new File("/home/annika/Entwicklung/Files/tessla");
		final String image = "tessla" + System.nanoTime();
		try (InputStream imagePayload = new BufferedInputStream(new FileInputStream(imageFile))) {
		  docker.create(image, imagePayload);
		}
		
		final String[] ports = {"80"};
		final Map<String, List<PortBinding>> portBindings = new HashMap<>();
		for (String port : ports) {
		    List<PortBinding> hostPorts = new ArrayList<>();
		    
		    // die 192.168.99.100 geht bei Linux iwie nicht. Funktioniert nur auf Windows
//		    hostPorts.add(PortBinding.of("192.168.99.100", port));
		    //und das geht bei mir nur auf Windows aber nicht auf Linux
		    hostPorts.add(PortBinding.of("127.0.0.1", port));
		    portBindings.put(port, hostPorts);
		}
		
//		-v /Users/<path>:/<container path> ...
//		https://docs.docker.com/engine/tutorials/dockervolumes/#mount-a-host-directory-as-a-data-volume
//		If you are using Docker Machine on Mac or Windows, your Docker Engine daemon has only limited access to your macOS or Windows filesystem. Docker Machine tries to auto-share your /Users (macOS) or C:\Users (Windows) directory. S
//		All other paths come from your virtual machine�s filesystem, so if you want to make some other host folder available for sharing, you need to do additional work.
		final HostConfig hostConfig = HostConfig.builder()
				.portBindings(portBindings)
//				.appendBinds("/local/path:/remote/path")
//				.appendBinds("C:/Users/lenovo/SSEProjekt/shared:c:/src")
//				.appendBinds("/Users/lenovo/SSEProjekt/shared:/src")
				.appendBinds("/home/annika/geteilt:/usr/geteilt")
				.build();

//		docker.pull("busybox");
		

		// Create container with exposed ports
		final ContainerConfig containerConfig = ContainerConfig.builder()
		    .hostConfig(hostConfig)
//		    .image("busybox").exposedPorts(ports)
		    .image("tessla").exposedPorts(ports)
		    //-rm
		    .cmd("sh", "-c", "while :; do sleep 1; done")
//		    .cmd(args)
		    .build();

		final ContainerCreation creation = docker.createContainer(containerConfig);
		final String id = creation.id();

		// Inspect container
		final ContainerInfo info = docker.inspectContainer(id);

		System.out.println("info: " + info);
		// geht in Linux iwie nicht, funktioniert nur auf Windows. Ka warum
//		Volume volumes = docker.inspectVolume(id);
//		System.out.println("volumes: " + volumes);
		
		// Start container
		docker.startContainer(id);

		// Exec command inside running container with attached STDOUT and STDERR
//		final String[] command = {"bash", "-c", "ls"};
		String[] command = getPatchAssemblyArgs();
		final ExecCreation execCreation = docker.execCreate(
		    id, command, DockerClient.ExecCreateParam.attachStdout(),
		    DockerClient.ExecCreateParam.attachStderr());
		
		
		final LogStream output = docker.execStart(execCreation.id());
		final String execOutput = output.readFully();

		System.out.println("execOutput: " + execOutput);
		
		// Kill container
		docker.killContainer(id);

		System.out.println("Container killed");
		
		// Remove container
		docker.removeContainer(id);

		// Close the docker client
		docker.close();

	}

	private String[] getPatchAssemblyArgs() {
//	    const projPath  = this.activeProject.projPath
//	    const binName   = this.activeProject.binName
//	    const outputDir = this.activeProject.outputDir
		boolean buildAssembly = true;
		String activeProject_projPath = "C:/Annika/Studium/3 Semester/SSE Projekt/TeSSLa Plugin/Dateien zum ausprobieren/";
		String activeProject_binName = "sub_add_alternation";
		String outFile = "build/" + activeProject_binName + (buildAssembly ? ".bc" : "");
		
		//TODO rausnehmen
		activeProject_projPath = "usr/geteilt/";
		outFile = "usr/geteilt/foo";
		
		//-----
	    // fetch all tessla files from project directory
//	    let args = [
//	      'exec',
//	      'tessla',
//	      '/usr/lib/llvm-3.8/bin/opt',
//	      '-load',
//	      '/InstrumentFunctions/libInstrumentFunctions.so',
//	      '-instrument_function_calls',
//	      path.join('build', binName + '.bc')
//	    ]
//
//	    TeSSLaFileManager.collectCFunctionsFromSourceFile({
//	      sourceFile:  this.activeProject.tesslaFiles[0],
//	      projectPath: this.activeProject.projPath
//	    }).forEach(function(value, index, array) {
//	      args = args.concat(['-instrument', value])
//	    })
//
//	    // create command and args
//	    args = args.concat(['-o', 'build/instrumented_' + binName + '.bc'])		
		//-----
		
		
		
		List<String> args = new ArrayList<String>();
	    String builded = "build" + activeProject_binName + ".bc";  	    		
		args.addAll(
				Arrays.asList("/usr/lib/llvm-3.8/bin/opt", "-load", "/InstrumentFunctions/libInstrumentFunctions.so", "-instrument_function_calls ", builded));

//	      sourceFile:  this.activeProject.tesslaFiles[0],
//	      projectPath: this.activeProject.projPath
		String sourceFile = activeProject.getTeSSLaFiles().get(0).getAbsolutePath();
		String projectPath = activeProject.getProjectPath();
		
		TeSSLaFileManager teSSLaFileManager = new TeSSLaFileManager();
		try {
			List<String> cFunctions = teSSLaFileManager.collectCFunctionsFromSourceFile(sourceFile, projectPath);
			for (String function : cFunctions) {
//				args = args.concat(['-instrument', value])
				args.add("-instrument " + function);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		// put c files into args array
		// args = args.concat(this.activeProject.cFiles.map((arg) => {
		// return path.relative(this.activeProject.projPath, arg).replace(/\\/g,
		// '/')
		// }))
		args.add(projectPath + "/foo.c");

		String command = "";
		for (String string : args) {
			command = command + " " + string;
		}
		
		///usr/lib/llvm-3.8/bin/opt -load /InstrumentFunctions/libInstrumentFunctions.so -instrument_function_calls  buildsub_add_alternation.bc -instrument add -instrument sub /home/annika/geteilt/dummyProjectPath/sub_add_alternation/foo.c
		System.out.println("Args: " + command);
		
		String[] argsArray = new String[args.size()];
		argsArray = args.toArray(argsArray);

		return argsArray;
	}
}
