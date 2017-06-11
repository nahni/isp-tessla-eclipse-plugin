package de.uniluebeck.isp.tessla.ui.services;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import de.uniluebeck.isp.tessla.model.TeSSLaProject;
import de.uniluebeck.isp.tessla.util.FileUtil;

public class CCodeBuildService {
	// private final static String PROJECT_PATH = "C:/Annika/Studium/3
	// Semester/SSE Projekt/TeSSLa Plugin/Dateien zum
	// ausprobieren/dummyProjectPath/sub_add_alternation";
	private final static String PROJECT_PATH = "/home/annika/Entwicklung/Spielwiese/dummyProjectPath2/sub_add_alternation";

	private final static String OUTPUT_DIR = "";
	private final static String BIN_NAME = "";

	TeSSLaProject activeProject;

	public CCodeBuildService() {
		activeProject = new TeSSLaProject(PROJECT_PATH, OUTPUT_DIR, BIN_NAME);
	}

	public String[] getBuildCCodeArgs() {
		
		//iwie sollte das wo anders hin oder die Methode umbeannt werden
		WorkingDirFileService workingDirFileService = new WorkingDirFileService(activeProject);
		workingDirFileService.createWorkingDir();
		workingDirFileService.transferFilesToContainer();
		
		boolean buildAssembly = true;
		String activeProject_projPath = activeProject.getProjectPath();
//		String activeProject_binName = "sub_add_alternation";
		String binName = activeProject.getBinName();
//		String outFile = "build/" + binName + (buildAssembly ? ".bc" : "");
		String outFile = activeProject.getProjectPath() + "/build/" + binName + (buildAssembly ? ".bc" : "");
		
		activeProject_projPath = "/home/annika/Entwicklung/Spielwiese/dummyProjectPath2/sub_add_alternation";
//		outFile = "/home/annika/Entwicklung/Spielwiese/dummyProjectPath2/sub_add_alternation/build/" + binName;
		outFile = "build/" + binName;
		//TOOD wenn das mit der WoringDir geklappt hat wegnehmen
		outFile = "/tessla/" + outFile;
		
		
		// TODO rausnehmen
//		String activeProject_projPath = "usr/geteilt/";
//		outFile = "usr/geteilt/foo";

		
		List<String> args = new ArrayList<String>();
		// TODO exec und tessla war noch angegeben:
		// args.addAll(Arrays.asList("exec", "tessla", "clang", "-o", outFile));
		args.addAll(Arrays.asList("clang", "-o", outFile));

		if (buildAssembly) {
			args.addAll(Arrays.asList("-emit-llvm", "-S"));
		}

		// put c files into args array
		// args = args.concat(this.activeProject.cFiles.map((arg) => {
		// return path.relative(this.activeProject.projPath, arg).replace(/\\/g,
		// '/')
		// }))
		//TODO
//		List<File> cFiles = activeProject.getCFiles();
//		if(CollectionUtils.isNotEmpty(cFiles)){
//			for (File file : cFiles) {
//				args.add(file.getPath());
//			}
//		}
		
		List<File> cFiles = activeProject.getCFiles();
		if(CollectionUtils.isNotEmpty(cFiles)){
			for (File cFile : cFiles) {
				
				String path = cFile.getAbsolutePath();
				String base = activeProject.getProjectPath();
				String relative = new File(base).toURI().relativize(new File(path).toURI()).getPath();

				String relativePath = FileUtil.getRelativePath(activeProject.getProjectPath(), cFile.getAbsolutePath());
				
				//TOOD wenn das mit der WoringDir geklappt hat wegnehmen
				relativePath = "/tessla/" + relativePath;
				
				args.add(relativePath);
				
//				String path = "/var/data/stuff/xyz.dat";
//				String base = "/var/data";
//				String relative = new File(base).toURI().relativize(new File(path).toURI()).getPath();


			}
		}
		
		
//		args.add(activeProject_projPath + "/" + "foo.c");

        String command = "";
        for (String string : args) {
            command = command + " " + string;
        }
        System.out.println("getBuildCCodeArgs: " + command);
		
		String[] argsArray = new String[args.size()];
		argsArray = args.toArray(argsArray);

		return argsArray;
	}
}
