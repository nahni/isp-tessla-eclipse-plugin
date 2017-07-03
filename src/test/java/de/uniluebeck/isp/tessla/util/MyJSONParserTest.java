package de.uniluebeck.isp.tessla.util;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.List;

import org.junit.Test;

public class MyJSONParserTest {

	@Test
	public void parse_test() {
		//TODO
		String tsslJSON ="C:/Annika/Studium/3 Semester/SSE Projekt/TeSSLa Plugin/workspace/de.uniluebeck.isp.Tessla.ui/test/resources/tessla.json";
		
		File tsslJSONFile = new File(tsslJSON);
		
		List<String> outputArgs = MyJSONParser.parse(tsslJSONFile);
		
        String command = "";
        for (String string : outputArgs) {
            command = command + " " + string;
        }
        
        assertEquals(" -o 11:diff -o 12:error", command);
	}

}
