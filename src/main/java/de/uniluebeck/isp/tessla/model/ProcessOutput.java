package de.uniluebeck.isp.tessla.model;

public class ProcessOutput {

	String info;
	
	String error;
	
	public ProcessOutput(String info, String error){
		this.info = info;
		this.error = error;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
	
}
