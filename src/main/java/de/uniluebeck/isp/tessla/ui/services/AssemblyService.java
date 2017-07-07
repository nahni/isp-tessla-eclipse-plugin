package de.uniluebeck.isp.tessla.ui.services;

import de.uniluebeck.isp.tessla.model.TeSSLaProject;

public class AssemblyService {

	TeSSLaProject activeProject;
	
	public AssemblyService(TeSSLaProject activeProject){
		this.activeProject = activeProject;
	}
	
	public void patchAssembly() {
//		getInstrument_BC_Args:  instrument sub_add_alternation.bc
		CommandArgsService commandArgsService = new CommandArgsService(activeProject);
		String[] instrument_BC_Args = commandArgsService.getInstrument_BC_Args();
		
		DockerService dockerSerivce = new DockerService(activeProject);
		dockerSerivce.runDockerCommandAvoidingWordSplitting2(instrument_BC_Args);
	}
	
	public void buildAssembly() {
//		getCompileToBinary_Args:  clang -linstrumentation sub_add_alternation.bc -o sub_add_alternation
		CommandArgsService commandArgsService = new CommandArgsService(activeProject);
		String[] compileToBinary_Args = commandArgsService.getCompileToBinary_Args();
		
		DockerService dockerSerivce = new DockerService(activeProject);
		dockerSerivce.runDockerCommandAvoidingWordSplitting2(compileToBinary_Args);
	}
}
