package com.wingarden.cicd.jenkins.plugins.statisticsplugin.dal.model;

public class BuildDetial {
	private String runTime;
	private int number;
	public void setRunTime(String runTime) {
		this.runTime = runTime;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public String getRunTime() {
		return runTime;
	}
	public int getNumber() {
		return number;
	}
}
