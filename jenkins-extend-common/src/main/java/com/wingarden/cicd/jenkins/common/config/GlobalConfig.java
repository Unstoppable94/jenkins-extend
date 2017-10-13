package com.wingarden.cicd.jenkins.common.config;

import jenkins.model.Jenkins;

public class GlobalConfig {

	public static final String FS_DATA_ROOT_DIR;
	private static final String PULIG_DATA_BASEPATH = "plugin-ext/datas/";//TODO:evn->configfile->default
	
	static {
		String jenkinsPath  = Jenkins.getInstance().getRootDir().getPath();
//		String workPath = Jenkins.getInstance().getWorkspaceFor(null);
		if (!jenkinsPath.endsWith("/")) {
			jenkinsPath = jenkinsPath + "/";
		}
		FS_DATA_ROOT_DIR = jenkinsPath + PULIG_DATA_BASEPATH;
	}
}
