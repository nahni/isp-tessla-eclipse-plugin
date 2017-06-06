package de.uniluebeck.isp.tessla.ui.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.uniluebeck.isp.tessla.model.TeSSLaProject;
import de.uniluebeck.isp.tessla.util.TeSSLaFileManager;

public class AssemblyService {
//	private final static String PROJECT_PATH = "C:/Annika/Studium/3 Semester/SSE Projekt/TeSSLa Plugin/Dateien zum ausprobieren/dummyProjectPath/sub_add_alternation";
	private final static String PROJECT_PATH = "/home/annika/geteilt/dummyProjectPath/sub_add_alternation";
	
	private final static String OUTPUT_DIR = "";
	private final static String BIN_NAME = "";
	
	TeSSLaProject activeProject;
	
	public AssemblyService(){
		activeProject = new TeSSLaProject(PROJECT_PATH, OUTPUT_DIR, BIN_NAME);
	}
	
	public String[] getPatchAssemblyArgs() {
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
	
	public String[] getBuildAssemblyArgs() {

        String activeProject_binName = "sub_add_alternation";
        
//        const args = [
//                      'exec',
//                      'tessla',
//                      'clang++',
//                      path.join('build', 'instrumented_' + this.activeProject.binName + '.bc'),
//                      '-o',
//                      'build/instrumented_' + this.activeProject.binName,
//                      '-lzlog',
//                      '-lpthread',
//                      '-L/usr/local/lib',
//                      '-L/InstrumentFunctions',
//                      '-lLogger'
//                    ]
                            
        // check if the tessla docker container is still running
//        this.checkDocker()                       

        List<String> args = new ArrayList<String>();
        args.addAll(Arrays.asList("clang++", "instrumented_" + this.activeProject.getBinName() + ".bc", "-o",
                "build/instrumented_" + this.activeProject.getBinName(), "-lzlog", "-lpthread", "-L/usr/local/lib",
                "-L/InstrumentFunctions", "-lLogger"));

        String command = "";
        for (String string : args) {
            command = command + " " + string;
        }
        System.out.println(command);
        
        String[] argsArray = new String[args.size()];
        argsArray = args.toArray(argsArray);

        return argsArray;
	}
}
