package de.uniluebeck.isp.tessla.ui.services;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import de.uniluebeck.isp.tessla.model.TeSSLaProject;

public class PatchedBinaryService {

	// private final static String PROJECT_PATH = "C:/Annika/Studium/3
	// Semester/SSE Projekt/TeSSLa Plugin/Dateien zum
	// ausprobieren/dummyProjectPath/sub_add_alternation";
	private final static String PROJECT_PATH = "/home/annika/geteilt/dummyProjectPath/sub_add_alternation";

	private final static String OUTPUT_DIR = "";
	private final static String BIN_NAME = "";

	TeSSLaProject activeProject;

	public PatchedBinaryService() {
		activeProject = new TeSSLaProject(PROJECT_PATH, OUTPUT_DIR, BIN_NAME);
	}
	
	public String[] getRunPatchedBinaryArgs() {
		//The os.homedir() method returns the home directory of the current user as a string.
//		this.containerDir   = path.join(os.homedir(), '.tessla-env')
		//containerDir sollte /home/annika/.tessla-env sein
		String containerDir = System.getProperty("user.home") + ".tessla-env";
		containerDir = activeProject.getContainerDir();
		
		String traceFilePath = containerDir + "instrumented_" + this.activeProject.getBinName() + ".trace";
		
	    // remove old trace file before new one would be created
//	    var traceFile = path.join(this.containerDir, 'instrumented_' + this.activeProject.binName + '.trace')
	    File traceFile = new File(traceFilePath);
		if(traceFile.exists()){
			DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			File renamedFile = new File(traceFilePath + "." + sdf.format(new Date()));
			traceFile.renameTo(renamedFile);
		}
//		String traceFile = 
//	    		if ( fs.existsSync(traceFile) ) {
//	      fs.renameSync(traceFile, traceFile + '.' + (+new Date()))
//	    }

	    // get docker args
//	    const args = ['exec', 'tessla', './build/instrumented_' + this.activeProject.binName]

	    List<String> args = new ArrayList<String>();
	    args.addAll(Arrays.asList("./build/instrumented_" + this.activeProject.getBinName()));
	    		
        String command = "";
        for (String string : args) {
            command = command + " " + string;
        }
        System.out.println("getRunPatchedBinaryArgs: " + command);
        
        String[] argsArray = new String[args.size()];
        argsArray = args.toArray(argsArray);

        return argsArray;
	}
}
