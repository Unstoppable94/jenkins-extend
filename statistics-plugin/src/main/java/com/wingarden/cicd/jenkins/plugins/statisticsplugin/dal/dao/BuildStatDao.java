package com.wingarden.cicd.jenkins.plugins.statisticsplugin.dal.dao;

import java.util.Date;
import java.util.List;

import com.wingarden.cicd.jenkins.common.dal.dao.BaseFsDao;
import com.wingarden.cicd.jenkins.common.dal.model.BuildList;
import com.wingarden.cicd.jenkins.common.dal.model.BuildStatus;
import com.wingarden.cicd.jenkins.common.dal.model.ProjectList;
import com.wingarden.cicd.jenkins.common.utils.CollectionUtils;
import com.wingarden.cicd.jenkins.common.utils.JenkinsUtil;
import com.wingarden.cicd.jenkins.plugins.statisticsplugin.dal.model.BuildStat;

import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.View;
import hudson.util.RunList;

@SuppressWarnings("rawtypes")
public class BuildStatDao extends BaseFsDao<BuildStat> {

	/**
	 * 根据项目范围（项目组、指定项目）、构建时间，按状态统计构建的数量
	 * @param groupIds 项目组标识集合
	 * @param projectIds  项目标识集合
	 * @param beginTime 起始时间
	 * @param endTime 结束时间
	 * @return 构建状态数量统计对象
	 */
	public BuildStat getCurStat(List<String> groupIds,List<String> projectIds,Date beginTime,Date endTime) {
		ProjectList<AbstractProject> projects = null;
		if (CollectionUtils.isEmpty(groupIds) && CollectionUtils.isEmpty(projectIds)){
			projects = new ProjectList<>(AbstractProject.class,true);
		}else {
			if (CollectionUtils.isNotEmpty(groupIds)) {
				projects = new ProjectList<>(AbstractProject.class,JenkinsUtil.getViews(groupIds).toArray(new View[] {}));
			}
			
			if (CollectionUtils.isNotEmpty(projectIds)) {
				projects = projects.nameProjects(projectIds);
			}
		}
		
		RunList<AbstractBuild> bList = new BuildList<>(projects).getRunList();
		if (null != beginTime && null != endTime && endTime.after(beginTime)) {
			bList = bList.byTimestamp(beginTime.getTime(), endTime.getTime());
		}
		BuildStat stat = buildBuildStat(bList);
		insert(stat);
		return stat;
	}

	private BuildStat buildBuildStat(RunList<AbstractBuild> builds) {
		BuildStat stat = new BuildStat();
		stat.setStatTime(new Date());
		long total = 0;
		long failed = 0;
		long running = 0;
		long success = 0;
		long aborted = 0;
		long pending = 0;
		for (AbstractBuild b : builds) {
			BuildStatus status = BuildStatus.parseStatus(b.getIconColor());
			total++;
			switch (status) {
			case RUNNING:
				running++;
				break;
			case ABORTED:
				aborted++;
				break;
			case FAILED:
				failed++;
				break;
			case SUCCESS:
				success++;
				break;
			case PENDING:
				pending++;
				break;
			default:
				break;
			}
		}
		stat.setTotal(total);
		stat.setFailed(failed);
		stat.setAborted(aborted);
		stat.setRunning(running);
		stat.setSuccess(success);
		stat.setPending(pending);
		stat.setNodeName(JenkinsUtil.getCurrentNodeName());
		return stat;
	}
}
