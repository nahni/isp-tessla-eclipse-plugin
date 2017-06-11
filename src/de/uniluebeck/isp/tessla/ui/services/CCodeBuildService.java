package de.uniluebeck.isp.tessla.ui.services;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import de.uniluebeck.isp.tessla.model.TeSSLaProject;
import de.uniluebeck.isp.tessla.util.FileUtil;

public class CCodeBuildService {

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
		String binName = activeProject.getBinName();
		String outFile = "build/" + binName + (buildAssembly ? ".bc" : "");
		
		
		List<String> args = new ArrayList<String>();
		args.addAll(Arrays.asList("clang", "-o", outFile));

		if (buildAssembly) {
			args.addAll(Arrays.asList("-emit-llvm", "-S"));
		}

		List<File> cFiles = activeProject.getCFiles();
		if(CollectionUtils.isNotEmpty(cFiles)){
			for (File cFile : cFiles) {
				String relativePath = FileUtil.getRelativePath(activeProject.getProjectPath(), cFile.getAbsolutePath());
				args.add(relativePath);
			}
		}

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
