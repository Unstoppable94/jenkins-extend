package com.wingarden.cicd.jenkins.plugins.statisticsplugin.dal.dao;

import java.util.Date;
import java.util.List;

import com.wingarden.cicd.jenkins.common.dal.dao.BaseFsDao;
import com.wingarden.cicd.jenkins.common.dal.model.ProjectList;
import com.wingarden.cicd.jenkins.common.dal.model.ProjectStatus;
import com.wingarden.cicd.jenkins.common.utils.CollectionUtils;
import com.wingarden.cicd.jenkins.common.utils.JenkinsUtil;
import com.wingarden.cicd.jenkins.plugins.statisticsplugin.dal.model.ProjectStat;

import hudson.model.AbstractProject;
import hudson.model.View;

@SuppressWarnings("rawtypes")
public class ProjectStatDao extends BaseFsDao<ProjectStat> {

	/**
	 * 获取当前最新的统计数量。从Jenkins实例中实时获取所有项目数量并概述状态统计，生成统计对象后，先保存后返回。
	 * 
	 * @param groupIds 项目组标识数组
	 * @return 当前时刻项目状态数量统计对象
	 */
	public ProjectStat getCurStat(List<String> groupIds) {
		ProjectList<AbstractProject> projects = null;
		if (CollectionUtils.isNotEmpty(groupIds)) {
			projects = new ProjectList<>(AbstractProject.class,JenkinsUtil.getViews(groupIds).toArray(new View[] {}));
		} else {
			projects = new ProjectList<>(AbstractProject.class,true);
		}
		ProjectStat projectStat = buildProjectStat(projects);
		insert(projectStat);
		return projectStat;
	}

	private ProjectStat buildProjectStat(ProjectList<AbstractProject> projects) {
		ProjectStat projectStat = new ProjectStat();
		projectStat.setProjectStatTime(new Date());
		long total = 0;
		long failed = 0;
		long running = 0;
		long inqueue = 0;
		long nobuilt = 0;
		long success = 0;
		long aborted = 0;
		long disabled = 0;
		for (AbstractProject prj : projects) {
			ProjectStatus status = ProjectStatus.parseStatus(prj.getIconColor());
			total++;
			//对于inquque状态的特殊处理
			if (prj.isInQueue() && status != ProjectStatus.RUNNING) {
				inqueue++;
				continue;
			}
			switch (status) {
			case NOBUILT:
				nobuilt++;
				break;
			case RUNNING:
				running++;
				break;
			case ABORTED:
				aborted++;
				break;
			case INQUEUE:
				inqueue++;
				break;
			case FAILED:
				failed++;
				break;
			case SUCCESS:
				success++;
				break;
			case DISABLED:
				disabled++;
				break;
			default:
				break;
			}
		}
		projectStat.setTotal(total);
		projectStat.setFailed(failed);
		projectStat.setAborted(aborted);
		projectStat.setDisabled(disabled);
		projectStat.setInqueue(inqueue);
		projectStat.setNobuilt(nobuilt);
		projectStat.setRunning(running);
		projectStat.setSuccess(success);
		projectStat.setNodeName(JenkinsUtil.getCurrentNodeName());
		return projectStat;
	}
}
