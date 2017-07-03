package de.uniluebeck.isp.tessla.testing.util;

public class TestingUtil {

	private static final String SAMPLE_PROJECT_RELATIVE_PATH = "/test/resources/sub_add_alternation/";
	
	
	public static String getProjectPathToSampleProject(){
		String workspacePath = System.getProperty("user.dir");
		String sampleProjectPath = workspacePath + SAMPLE_PROJECT_RELATIVE_PATH;
		
		return sampleProjectPath;
	}
}
