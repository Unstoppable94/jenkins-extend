package com.wingarden.cicd.jenkins.plugins.statisticsplugin.dal.dao;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

import com.wingarden.cicd.jenkins.plugins.statisticsplugin.dal.model.ProjectStat;

import hudson.model.FreeStyleProject;
import tools.BuildGen;
import tools.ProjectGen;

public class ProjectStatDaoTest {

	private ProjectStatDao dao = new ProjectStatDao();
	@ClassRule
	public static JenkinsRule j = new JenkinsRule();
	private static ProjectGen pg;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		j.jenkins.setNumExecutors(50);
		pg = new ProjectGen(j);
		pg.genProjects("view1_project", 10, "view1");
		pg.genProjects("view2_project", 10, "view2");
		pg.genProjects("view3_project", 10, "view3");
	}

	@Test
	public void testGetCurStat() throws Exception {
		ProjectStat stat = dao.getCurStat(null);
		Assert.assertEquals(30, stat.getTotal());
		
		List<String> groupIds = new ArrayList<>();
		groupIds.add("view1");
		stat = dao.getCurStat(groupIds);
		Assert.assertEquals(10, stat.getTotal());
		
		groupIds.add("view2");
		stat = dao.getCurStat(groupIds);
		Assert.assertEquals(20, stat.getTotal());
		
		groupIds.add("view3");
		stat = dao.getCurStat(groupIds);
		Assert.assertEquals(30, stat.getTotal());
		
		BuildGen bg = new BuildGen(j);
		FreeStyleProject prj = pg.getProject("view1_project_1");
		bg.triggerSuccessBuild(prj, 1);
		
		prj = pg.getProject("view2_project_2");
		bg.triggerAbortedBuild(prj, 1);
		
		prj = pg.getProject("view3_project_3");
		bg.triggerFailedBuild(prj, 1);
		
		prj = pg.getProject("view1_project_2");
		bg.triggerPendingBuild(prj, 1);
		
		prj = pg.getProject("view2_project_1");
		bg.triggerRunningBuild(prj, 1);
//		pg.getProject("view2_project_1").setConcurrentBuild(false);
//		pg.getProject("view2_project_1").setAssignedLabel(new LabelAtom("unexist_node"));
		
		prj = pg.getProject("view3_project_1");
		prj.disable();
		
		Thread.currentThread().sleep(5000);
		stat = dao.getCurStat(null);
		Assert.assertEquals(1, stat.getSuccess());
		Assert.assertEquals(1, stat.getAborted());
		Assert.assertEquals(1, stat.getFailed());
		Assert.assertEquals(1, stat.getRunning());
		Assert.assertEquals(1, stat.getDisabled());
		Assert.assertEquals(1, stat.getInqueue());
		Assert.assertEquals(24, stat.getNobuilt());
	}

}
