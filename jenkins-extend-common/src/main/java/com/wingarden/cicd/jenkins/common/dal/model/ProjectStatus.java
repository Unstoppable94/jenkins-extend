package com.wingarden.cicd.jenkins.common.dal.model;

import hudson.model.BallColor;

public enum ProjectStatus {

	FAILED("failed", BallColor.RED), SUCCESS("success", BallColor.BLUE, BallColor.YELLOW), RUNNING("running",
			BallColor.RED_ANIME, BallColor.YELLOW_ANIME, BallColor.BLUE_ANIME, BallColor.GREY_ANIME,
			BallColor.DISABLED_ANIME, BallColor.NOTBUILT_ANIME,BallColor.ABORTED_ANIME), INQUEUE("inqueue", BallColor.GREY), NOBUILT("nobuilt",
					BallColor.NOTBUILT), DISABLED("disabled",
							BallColor.DISABLED), ABORTED("aborted", BallColor.ABORTED),UNKNOW("unknow");

	private final String name;
	private final BallColor[] colors;

	private ProjectStatus(String name, BallColor... ballColors) {
		this.name = name;
		this.colors = ballColors;
	}

	public String getName() {
		return this.name;
	}

	public static ProjectStatus parseStatus(String name) {
		switch (name) {
		case "failed":
			return ProjectStatus.FAILED;
		case "success":
			return ProjectStatus.SUCCESS;
		case "running":
			return ProjectStatus.RUNNING;
		case "inqueue":
			return ProjectStatus.INQUEUE;
		case "nobuilt":
			return ProjectStatus.NOBUILT;
		case "aborted":
			return ProjectStatus.ABORTED;
		case "disabled":
			return ProjectStatus.DISABLED;
		default:
			break;
		}
		return UNKNOW;
	}

	public static ProjectStatus parseStatus(BallColor statusColor) {
		switch (statusColor) {
		case NOTBUILT:
			return ProjectStatus.NOBUILT;
		case RED_ANIME:
		case YELLOW_ANIME:
		case BLUE_ANIME:
		case GREY_ANIME:
		case DISABLED_ANIME:
		case NOTBUILT_ANIME:
		case ABORTED_ANIME:
			return ProjectStatus.RUNNING;
		case ABORTED:
			return ProjectStatus.ABORTED;
		case GREY:
			return ProjectStatus.INQUEUE;
		case RED:
			return ProjectStatus.FAILED;
		case BLUE:
		case YELLOW:
			return ProjectStatus.SUCCESS;
		case DISABLED:
			return ProjectStatus.DISABLED;
		default:
			break;
		}
		return UNKNOW;
	}
	
	public boolean isStatus(BallColor statusColor) {
		for (BallColor ballColor : colors) {
			if (ballColor == statusColor) {
				return true;
			}
		}
		return false;
	}
}
