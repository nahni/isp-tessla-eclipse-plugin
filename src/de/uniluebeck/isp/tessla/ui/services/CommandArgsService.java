package de.uniluebeck.isp.tessla.ui.services;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import de.uniluebeck.isp.tessla.model.TeSSLaProject;
import de.uniluebeck.isp.tessla.util.FileUtil;

public class CommandArgsService {

	TeSSLaProject activeProject;
	
	public CommandArgsService(TeSSLaProject activeProject){
		this.activeProject = activeProject;
	}
	
	public String[] getCompileToLLVM_BC_Args() {
		List<String> args = new ArrayList<String>();
		
		String binName = activeProject.getBinName();
//		String outFile = "build/" + binName + ".bc";
		String outFile = binName + ".bc";
		
//		docker run --volume /home/annika/Downloads/Docker2_Beispiel:/tessla --rm tessla sh -c 'cd /tessla && clang -ggdb -Wall -pedantic -c -emit-llvm main.c -o main.bc'
//		clang -ggdb -Wall -pedantic -c -emit-llvm main.c -o main.bc'
	
		args.addAll(Arrays.asList("clang", "-ggdb", "-Wall", "-pedantic", "-c", "-emit-llvm"));
		
		List<File> cFiles = activeProject.getCFiles();
		if(CollectionUtils.isNotEmpty(cFiles)){
			for (File cFile : cFiles) {
				String relativePath = FileUtil.getRelativePath(activeProject.getProjectPath(), cFile.getAbsolutePath());
				args.add(relativePath);
			}
		}
		
		args.addAll(Arrays.asList("-o", outFile));
		
        String command = "";
        for (String string : args) {
            command = command + " " + string;
        }
        System.out.println("getCompileToLLVM_BC_Args: " + command);
		
		String[] argsArray = new String[args.size()];
		argsArray = args.toArray(argsArray);

		return argsArray;
	}
	
	public String[] getInstrument_BC_Args() {
		List<String> args = new ArrayList<String>();
		
		String binName = activeProject.getBinName();
		String bc_file = binName + ".bc";
		
//		docker run --volume /home/annika/Downloads/Docker2_Beispiel:/tessla --rm tessla sh -c 'cd /tessla && instrument main.bc'
		
		args.addAll(Arrays.asList("instrument", bc_file));
		
        String command = "";
        for (String string : args) {
            command = command + " " + string;
        }
        System.out.println("getInstrument_BC_Args: " + command);
		
		String[] argsArray = new String[args.size()];
		argsArray = args.toArray(argsArray);

		return argsArray;
	}
	
	public String[] getCompileToBinary_Args() {
		List<String> args = new ArrayList<String>();
		
		String binName = activeProject.getBinName();
		String bc_file = binName + ".bc";
		
		String outFile = this.activeProject.getBinName();
		
//		docker run --volume /home/annika/Downloads/Docker2_Beispiel:/tessla --rm tessla sh -c 'cd /tessla && clang -linstrumentation main.bc -o main'
//		clang -linstrumentation main.bc -o main
		
		args.addAll(Arrays.asList("clang", "-linstrumentation", bc_file, "-o", outFile));
		
        String command = "";
        for (String string : args) {
            command = command + " " + string;
        }
        System.out.println("getCompileToBinary_Args: " + command);
		
		String[] argsArray = new String[args.size()];
		argsArray = args.toArray(argsArray);

		return argsArray;
		
	}
	
	public String[] getRunBinary_Args() {
		List<String> args = new ArrayList<String>();
		
		String execFile = this.activeProject.getBinName();
		
//		docker run --volume /home/annika/Downloads/Docker2_Beispiel:/tessla --rm tessla sh -c 'cd /tessla && ./main'
		
		
		args.add("./" + execFile);
		
        String command = "";
        for (String string : args) {
            command = command + " " + string;
        }
        System.out.println("getRunBinary_Args: " + command);
		
		String[] argsArray = new String[args.size()];
		argsArray = args.toArray(argsArray);

		return argsArray;
		
	}
	
	public String[] getAddStandardLibrary_Args() {
		List<String> args = new ArrayList<String>();
		
		// docker exec tessla sh -c cat spec.tessla /usr/local/opt/tessla_rv/streams.tessla > spec2.tessla
		args.addAll(Arrays.asList("sh", "-c", "cat spec.tessla /usr/local/opt/tessla_rv/streams.tessla > spec2.tessla"));
		
		
        String command = "";
        for (String string : args) {
            command = command + " " + string;
        }
        System.out.println("getAddStandardLibrary_Args: " + command);
		
		String[] argsArray = new String[args.size()];
		argsArray = args.toArray(argsArray);

		return argsArray;
	}
	
	public String[] getRunTeSSLa_Args() {
		List<String> args = new ArrayList<String>();
		
		//docker exec tessla sh -c cd /tessla && tessla spec2.tessla traces.log
		args.addAll(Arrays.asList("sh", "-c", "cd /tessla && tessla spec2.tessla traces.log"));
		
        String command = "";
        for (String string : args) {
            command = command + " " + string;
        }
        System.out.println("getRunTeSSLa_Args: " + command);
		
		String[] argsArray = new String[args.size()];
		argsArray = args.toArray(argsArray);

		return argsArray;
		
		
	}
}
