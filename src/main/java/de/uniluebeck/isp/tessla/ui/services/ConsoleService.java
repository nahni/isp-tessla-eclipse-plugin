package de.uniluebeck.isp.tessla.ui.services;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

import de.uniluebeck.isp.tessla.util.Constants;

public class ConsoleService {
	
	private static ConsoleService instance = new ConsoleService();
	private IConsoleManager consoleManager;
	private Map<String,MessageConsole> consoles;
	
	private ConsoleService(){
		ConsolePlugin plugin = ConsolePlugin.getDefault();
	    this.consoleManager = plugin.getConsoleManager();
	    this.consoles = new HashMap<String, MessageConsole>(); 
	    createConsole(Constants.STDOUT_CONSOLE_NAME);
	    createConsole(Constants.ERR_CONSOLE_NAME);
	    createConsole(Constants.TESSLA_CONSOLE_NAME);
	    createConsole(Constants.DOCKER_CONSOLE_NAME);
	}
	
	public static ConsoleService getInstance(){
		return ConsoleService.instance;
	}

	public void createConsole(String name){
		MessageConsole newConsole = new MessageConsole(name, null);
	    this.consoleManager.addConsoles(new IConsole[]{newConsole});	
	    consoles.put(name, newConsole);
	}

	public void writeTo(String consoleName, String message){
		MessageConsole console = consoles.get(consoleName);
	    MessageConsoleStream out = console.newMessageStream();
	    out.println(message);
	}
    
	
}
