package com.wingarden.cicd.jenkins.plugins.statisticsplugin.dal.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

import com.wingarden.cicd.jenkins.common.utils.DateToolkit;
import com.wingarden.cicd.jenkins.plugins.statisticsplugin.dal.model.BuildStat;

import tools.ProjectGen;

public class BuildStatDaoTest {
	
	private BuildStatDao dao = new BuildStatDao();
	@ClassRule
	public static JenkinsRule j = new JenkinsRule();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		j.jenkins.setNumExecutors(50);
		ProjectGen.initProject4Test(j);
		Thread.currentThread().sleep(5000);
	}

	@Test
	public void testGetCurStat() {
		List<String> groupIds= new ArrayList<>();
		List<String> projectIds= new ArrayList<>();
		Date begTime = DateToolkit.utilStrToDate("2017-10-09 00:00:00");
		Date begTime2 = DateToolkit.utilStrToDate("2017-10-09 00:00:01");
		BuildStat stat = dao.getCurStat(null, null, null, null);
		Assert.assertEquals(4, stat.getTotal());
		Assert.assertEquals(1, stat.getSuccess());
		Assert.assertEquals(1, stat.getAborted());
		Assert.assertEquals(1, stat.getFailed());
		Assert.assertEquals(1, stat.getRunning());
		Assert.assertEquals(0, stat.getPending());//项目处于inqueue状态，此时buid对象未生成
		
		stat = dao.getCurStat(null, null, begTime, new Date());
		Assert.assertEquals(4, stat.getTotal());
		Assert.assertEquals(1, stat.getSuccess());
		Assert.assertEquals(1, stat.getAborted());
		Assert.assertEquals(1, stat.getFailed());
		Assert.assertEquals(1, stat.getRunning());
		Assert.assertEquals(0, stat.getPending());
		
		stat = dao.getCurStat(null, null, begTime, begTime2);
		Assert.assertEquals(0, stat.getTotal());
		Assert.assertEquals(0, stat.getSuccess());
		Assert.assertEquals(0, stat.getAborted());
		Assert.assertEquals(0, stat.getFailed());
		Assert.assertEquals(0, stat.getRunning());
		Assert.assertEquals(0, stat.getPending());
		
		groupIds.add("view1");
		stat = dao.getCurStat(groupIds, null, null, null);
		Assert.assertEquals(1, stat.getTotal());
		Assert.assertEquals(1, stat.getSuccess());
		Assert.assertEquals(0, stat.getAborted());
		Assert.assertEquals(0, stat.getFailed());
		Assert.assertEquals(0, stat.getRunning());
		Assert.assertEquals(0, stat.getPending());
		
		groupIds.add("view2");
		stat = dao.getCurStat(groupIds, null, null, null);
		Assert.assertEquals(3, stat.getTotal());
		Assert.assertEquals(1, stat.getSuccess());
		Assert.assertEquals(1, stat.getAborted());
		Assert.assertEquals(0, stat.getFailed());
		Assert.assertEquals(1, stat.getRunning());
		Assert.assertEquals(0, stat.getPending());
		
		projectIds.add("prject_noexist");
		stat = dao.getCurStat(groupIds, projectIds, null, null);
		Assert.assertEquals(0, stat.getTotal());
		Assert.assertEquals(0, stat.getSuccess());
		Assert.assertEquals(0, stat.getAborted());
		Assert.assertEquals(0, stat.getFailed());
		Assert.assertEquals(0, stat.getRunning());
		Assert.assertEquals(0, stat.getPending());
		
		projectIds.add("view3_project_3");
		groupIds.add("view3");
		stat = dao.getCurStat(groupIds, projectIds, null, null);
		Assert.assertEquals(1, stat.getTotal());
		Assert.assertEquals(0, stat.getSuccess());
		Assert.assertEquals(0, stat.getAborted());
		Assert.assertEquals(1, stat.getFailed());
		Assert.assertEquals(0, stat.getRunning());
		Assert.assertEquals(0, stat.getPending());
		
		stat = dao.getCurStat(groupIds, projectIds, begTime, begTime2);
		Assert.assertEquals(0, stat.getTotal());
		Assert.assertEquals(0, stat.getSuccess());
		Assert.assertEquals(0, stat.getAborted());
		Assert.assertEquals(0, stat.getFailed());
		Assert.assertEquals(0, stat.getRunning());
		Assert.assertEquals(0, stat.getPending());
	}

}
