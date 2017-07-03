package de.uniluebeck.isp.tessla.util;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import de.uniluebeck.isp.tessla.testing.util.TestingUtil;

public class TeSSLaFileManagerTest {

	@Test
	public void collectCFunctionsFromSourceFile_test() {
		String sampleProjectPath = TestingUtil.getProjectPathToSampleProject();
		String sourceFile = sampleProjectPath + "/foo.c";
		
		TeSSLaFileManager teSSLaFileManager = new TeSSLaFileManager();
		List<String> result = null;
		try {
			result = teSSLaFileManager.collectCFunctionsFromSourceFile(sourceFile, sampleProjectPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertEquals(3, result.size());
		
		assertEquals("foo.c:sub", result.get(0));
		assertEquals("foo.c:add", result.get(1));
		assertEquals("foo.c:main", result.get(2));
		
	}
}
