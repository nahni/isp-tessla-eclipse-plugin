package de.uniluebeck.isp.tessla.ui.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import de.uniluebeck.isp.tessla.model.TeSSLaProject;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class RunHandler extends AbstractHandler {

//	private final static String PROJECT_PATH = "C:/Annika/Studium/3 Semester/SSE Projekt/TeSSLa Plugin/Dateien zum ausprobieren/dummyProjectPath/sub_add_alternation";
	private final static String PROJECT_PATH = "/home/annika/geteilt/dummyProjectPath/sub_add_alternation";
	
	private final static String OUTPUT_DIR = "";
	private final static String BIN_NAME = "";
	
	TeSSLaProject activeProject;
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		activeProject = new TeSSLaProject(PROJECT_PATH, OUTPUT_DIR, BIN_NAME);
		
//		Command command = event.getCommand();
//		command.getId();
//		
//		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
//		MessageDialog.openInformation(
//				window.getShell(),
//				"Ui",
//				"Run");
		return null;
	}
	

	private void getRunPatchedBinaryArgs() {
//		PatchedBinaryService patchedBinaryService = new PatchedBinaryService();
		
//		return patchedBinaryService.getRunPatchedBinaryArgs();
	}
}
