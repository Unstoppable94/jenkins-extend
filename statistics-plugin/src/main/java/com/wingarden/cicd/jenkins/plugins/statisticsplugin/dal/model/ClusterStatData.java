package com.wingarden.cicd.jenkins.plugins.statisticsplugin.dal.model;

import java.util.Date;
import java.util.List;

import com.wingarden.cicd.jenkins.plugins.statisticsplugin.dal.dao.BuildStatDao;
import com.wingarden.cicd.jenkins.plugins.statisticsplugin.dal.dao.ProjectStatDao;

public class ClusterStatData {
	private static final ProjectStatDao projectStatDao = new ProjectStatDao();
	private static final BuildStatDao BuildStatDao = new BuildStatDao();
	
	public ProjectStat getCurProjectStat(List<String> groupIds) {
		return projectStatDao.getCurStat(groupIds);
	}
	
	public BuildStat getCurtBuildStat(List<String> groupIds,List<String> projectIds,Date beginTime,Date endTime) {
		return BuildStatDao.getCurStat(groupIds, projectIds, beginTime, endTime);
	}

	public List<BuildDetial> getCurtBuildStat(Date beginTime, Date endTime) {
		// TODO Auto-generated method stub
		
		return BuildStatDao.getCurStat(beginTime, endTime);
	}
}
