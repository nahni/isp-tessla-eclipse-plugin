package de.uniluebeck.isp.tessla.ui.services;

import de.uniluebeck.isp.tessla.model.TeSSLaProject;

public class PatchedBinaryService {

	TeSSLaProject activeProject;

	public PatchedBinaryService(TeSSLaProject activeProject) {
		this.activeProject = activeProject;
	}

	/**
	 * Patched Binary
	 */
	public void runPatchedBinary() {
		// getRunBinary_Args: ./sub_add_alternation
		CommandArgsService commandArgsService = new CommandArgsService(activeProject);
		String[] runBinary_Args = commandArgsService.getRunBinary_Args();

		DockerService dockerSerivce = new DockerService(activeProject);
		dockerSerivce.runDockerCommandAvoidingWordSplitting2(runBinary_Args);
	}
}
