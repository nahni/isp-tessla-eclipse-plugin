package de.uniluebeck.isp.tessla.model;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;

import org.junit.Test;

import de.uniluebeck.isp.tessla.testing.util.TestingUtil;

public class TeSSLaProjectTest {

	@Test
	public void getCFiles_test() {
		String sampleProjectPath = TestingUtil.getProjectPathToSampleProject();
		
		TeSSLaProject teSSLaProject = new TeSSLaProject(sampleProjectPath, null, null);
		List<File> result = teSSLaProject.getCFiles();
		
		assertEquals(1, result.size());
		assertEquals("foo.c", result.get(0).getName());
	}
	
	@Test
	public void getTeSSLaFiles_test() {
		String sampleProjectPath = TestingUtil.getProjectPathToSampleProject();
		
		TeSSLaProject teSSLaProject = new TeSSLaProject(sampleProjectPath, null, null);
		List<File> result = teSSLaProject.getTeSSLaFiles();
		
		assertEquals(1, result.size());
		assertEquals("spec.tessla", result.get(0).getName());
	}
	
	@Test
	public void getBinName_test() {
		String sampleProjectPath = TestingUtil.getProjectPathToSampleProject();
		
		TeSSLaProject teSSLaProject = new TeSSLaProject(sampleProjectPath, null, null);
		String reuslt = teSSLaProject.getBinName();
		
		assertEquals("sub_add_alternation", reuslt);
	}
}
