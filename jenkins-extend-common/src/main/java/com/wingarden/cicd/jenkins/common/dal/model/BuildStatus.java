package com.wingarden.cicd.jenkins.common.dal.model;

import hudson.model.BallColor;

public enum BuildStatus {
	FAILED("failed", BallColor.RED), 
	SUCCESS("success", BallColor.BLUE, BallColor.YELLOW), 
	RUNNING("running",
			BallColor.RED_ANIME, BallColor.YELLOW_ANIME, BallColor.BLUE_ANIME, BallColor.GREY_ANIME,
			BallColor.DISABLED_ANIME, BallColor.NOTBUILT_ANIME,BallColor.ABORTED_ANIME), 
	
	ABORTED("aborted", BallColor.ABORTED),
	PENDING("pending", BallColor.GREY),
	UNKNOW("unknow");
	
	private final String name;
	private final BallColor[] colors;

	private BuildStatus(String name, BallColor... ballColors) {
		this.name = name;
		this.colors = ballColors;
	}

	public String getName() {
		return this.name;
	}
	
	public boolean isStatus(BallColor statusColor) {
		for (BallColor ballColor : colors) {
			if (ballColor == statusColor) {
				return true;
			}
		}
		return false;
	}
	
	public static BuildStatus parseStatus(BallColor statusColor) {
		switch (statusColor) {
		case RED_ANIME:
		case YELLOW_ANIME:
		case BLUE_ANIME:
		case GREY_ANIME:
		case DISABLED_ANIME:
		case NOTBUILT_ANIME:
		case ABORTED_ANIME:
			return BuildStatus.RUNNING;
		case ABORTED:
			return BuildStatus.ABORTED;
		case RED:
			return BuildStatus.FAILED;
		case GREY:
			return BuildStatus.PENDING;
		case BLUE:
		case YELLOW:
			return BuildStatus.SUCCESS;
		default:
			break;
		}
		return UNKNOW;
	}
	
}
