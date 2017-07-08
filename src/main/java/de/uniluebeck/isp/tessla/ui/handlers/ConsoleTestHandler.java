package de.uniluebeck.isp.tessla.ui.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import de.uniluebeck.isp.tessla.ui.services.ConsoleService;
import de.uniluebeck.isp.tessla.util.Constants;

public class ConsoleTestHandler extends AbstractHandler{

	private ConsoleService consoleService;
	
	@Override
	public Object execute(ExecutionEvent arg0) throws ExecutionException {
		
		this.consoleService = ConsoleService.getInstance();
		printStd("Standard");
		printErr("Error");
		printDocker("Docker");
		printTessla("Tessla");
		System.out.println("console test finished");
		
		return null;
	}
	
	private void printStd(String text){
		consoleService.writeTo(Constants.STDOUT_CONSOLE_NAME, text);
	}
	
	private void printErr(String text){
		consoleService.writeTo(Constants.ERR_CONSOLE_NAME, text);
	}
	
	private void printDocker(String text){
		consoleService.writeTo(Constants.DOCKER_CONSOLE_NAME, text);
	}
	
	private void printTessla(String text){
		consoleService.writeTo(Constants.TESSLA_CONSOLE_NAME, text);
	}
	
}
