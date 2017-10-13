package com.wingarden.cicd.jenkins.common.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.wingarden.cicd.jenkins.common.dal.model.ProjectStatus;

import hudson.model.AbstractProject;
import hudson.model.Computer;
import hudson.model.Job;
import hudson.model.Node;
import hudson.model.TopLevelItem;
import hudson.model.View;
import jenkins.model.Jenkins;

@SuppressWarnings("rawtypes")
public class JenkinsUtil {

	public static List<View> getViews(String... viewNames) {
		List<View> views = new ArrayList<>();
		for (String vName : viewNames) {
			views.add(Jenkins.getInstance().getView(vName));
		}
		return views;
	}

	public static List<View> getViews(List<String> viewNames) {
		if (CollectionUtils.isNotEmpty(viewNames)) {
			return getViews(viewNames.toArray(new String[] {}));
		}
		return Collections.EMPTY_LIST;
	}

	public static Set<Job> getViewJobs(List<String> groupIds) {
		Set<Job> jobs = new HashSet<>();
		View view = null;
		for (String viewName : groupIds) {
			view = Jenkins.getInstance().getView(viewName);
			if (null != view) {
				for (TopLevelItem item : view.getItems())
					jobs.addAll(item.getAllJobs());
			}
		}
		return jobs;
	}

	public static List<AbstractProject> getViewProjects(List<String> groupIds) {
		return getProjects(groupIds, null, null);
	}

	public List<AbstractProject> getAllProjects() {
		return getProjects(null, null, null);
	}

	public static List<AbstractProject> getProjects(List<String> groupIds, List<String> projectIds,
			List<String> statusList) {
		if (CollectionUtils.isEmpty(groupIds) && CollectionUtils.isEmpty(projectIds)
				&& CollectionUtils.isEmpty(statusList)) {
			return Jenkins.getInstance().getAllItems(AbstractProject.class);
		}

		List<AbstractProject> projects = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(groupIds)) {
			for (Job job : getViewJobs(groupIds)) {
				if (AbstractProject.class.isInstance(job)) {
					AbstractProject prj = (AbstractProject) job;
					boolean accepted = true;
					if (accepted && CollectionUtils.isNotEmpty(projectIds)) {
						accepted = projectIds.contains(prj.getName());
					}
					if (accepted && CollectionUtils.isNotEmpty(statusList)) {
						accepted = statusList.contains(ProjectStatus.parseStatus(prj.getIconColor()).getName());
					}
					if (accepted) {
						projects.add(prj);
					}
				}
			}
		}

		if (CollectionUtils.isEmpty(groupIds) && CollectionUtils.isNotEmpty(projectIds)) {
			for (String projectId : projectIds) {
				TopLevelItem item = Jenkins.getInstance().getItem(projectId);
				if (AbstractProject.class.isInstance(item) && !projectIds.contains(item.getName())) {
					AbstractProject prj = AbstractProject.class.cast(item);
					if (CollectionUtils.isNotEmpty(statusList)
							&& statusList.contains(ProjectStatus.parseStatus(prj.getIconColor()).getName())) {
						projects.add(prj);
					}
				}
			}
		}
		return projects;
	}

	public static Node getCurrentNode() {
		Node node = null;
		if (null != Computer.currentComputer()) {
			node = Computer.currentComputer().getNode();
		}
		return node;
	}
	
	public static String getCurrentNodeName() {
		Node node = getCurrentNode();
		return null == node ? null : node.getNodeName();
	}
}
