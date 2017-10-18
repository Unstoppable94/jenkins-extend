package com.wingarden.cicd.jenkins.plugins.statisticsplugin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.wingarden.cicd.jenkins.common.utils.JsonUtil;
import com.wingarden.cicd.jenkins.plugins.statisticsplugin.dal.model.BuildStat;
import com.wingarden.cicd.jenkins.plugins.statisticsplugin.dal.model.ProjectStat;

import tools.ProjectGen;

public class StatisticsPluginTest {
	private static Map<String, String> headers = new HashMap<>();
	@ClassRule
	public static JenkinsRule j = new JenkinsRule();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		if (j.jenkins.isUseCrumbs()) {
			headers.put(j.jenkins.getCrumbIssuer().getCrumbRequestField(), j.jenkins.getCrumbIssuer().getCrumb());
		}
		headers.put("accept", "application/json");
		deployPluginByHttp();

		j.jenkins.setNumExecutors(30);
		ProjectGen.initProject4Test(j);
		Thread.currentThread().sleep(5000);
	}


	@Test
	public void testGetUrlName() throws Exception {
		HttpResponse<String> url = Unirest.post(j.getURL() + "statistics/url").headers(headers).asString();
		Assert.assertEquals("statistics", url.getBody());
	}

	@Test
	public void testDoBuildStat() throws Exception{
		com.alibaba.fastjson.JSONObject json = new com.alibaba.fastjson.JSONObject();
		List<String> groupIds = new ArrayList<>();
		json.put("groupIds", groupIds);
		headers.put("Content-Type", "application/json");
		HttpResponse<JsonNode> ret = Unirest.post(j.getURL() + "statistics/buildstat").headers(headers)
				.body(json.toJSONString()).asJson();

		Assert.assertEquals(1, ret.getBody().getObject().getInt("rc"));
		Assert.assertEquals("success", ret.getBody().getObject().getString("msg"));
		org.json.JSONObject data = ret.getBody().getObject().getJSONObject("data");
		BuildStat stat = JsonUtil.parseObject(data.toString(), BuildStat.class);

		Assert.assertEquals(4, stat.getTotal());
		Assert.assertEquals(1, stat.getSuccess());
		Assert.assertEquals(1, stat.getAborted());
		Assert.assertEquals(1, stat.getFailed());
		Assert.assertEquals(1, stat.getRunning());
		Assert.assertEquals(0, stat.getPending());//项目处于inqueue状态，此时buid对象未生成
	}

	@Test
	public void testDoProjectStat() throws Exception {
		com.alibaba.fastjson.JSONObject json = new com.alibaba.fastjson.JSONObject();
		List<String> groupIds = new ArrayList<>();
		json.put("groupIds", groupIds);
		headers.put("Content-Type", "application/json");
		HttpResponse<JsonNode> ret = Unirest.post(j.getURL() + "statistics/projectstat").headers(headers)
				.body(json.toJSONString()).asJson();

		Assert.assertEquals(1, ret.getBody().getObject().getInt("rc"));
		Assert.assertEquals("success", ret.getBody().getObject().getString("msg"));
		org.json.JSONObject data = ret.getBody().getObject().getJSONObject("data");
		ProjectStat stat = JsonUtil.parseObject(data.toString(), ProjectStat.class);

		Assert.assertEquals(30, stat.getTotal());
		Assert.assertEquals(1, stat.getSuccess());
		Assert.assertEquals(1, stat.getAborted());
		Assert.assertEquals(1, stat.getFailed());
		Assert.assertEquals(1, stat.getRunning());
		Assert.assertEquals(1, stat.getDisabled());
		Assert.assertEquals(1, stat.getInqueue());
		Assert.assertEquals(24, stat.getNobuilt());

		groupIds.add("view1");
		ret = Unirest.post(j.getURL() + "statistics/projectstat").headers(headers).body(json.toJSONString()).asJson();
		Assert.assertEquals(1, ret.getBody().getObject().getInt("rc"));
		Assert.assertEquals("success", ret.getBody().getObject().getString("msg"));
		data = ret.getBody().getObject().getJSONObject("data");
		stat = JsonUtil.parseObject(data.toString(), ProjectStat.class);

		Assert.assertEquals(10, stat.getTotal());
		Assert.assertEquals(1, stat.getSuccess());
		Assert.assertEquals(0, stat.getAborted());
		Assert.assertEquals(0, stat.getFailed());
		Assert.assertEquals(0, stat.getRunning());
		Assert.assertEquals(0, stat.getDisabled());
		Assert.assertEquals(1, stat.getInqueue());
		Assert.assertEquals(8, stat.getNobuilt());

		groupIds.add("view2");
		ret = Unirest.post(j.getURL() + "statistics/projectstat").headers(headers).body(json.toJSONString()).asJson();
		Assert.assertEquals(1, ret.getBody().getObject().getInt("rc"));
		Assert.assertEquals("success", ret.getBody().getObject().getString("msg"));
		data = ret.getBody().getObject().getJSONObject("data");
		stat = JsonUtil.parseObject(data.toString(), ProjectStat.class);

		Assert.assertEquals(20, stat.getTotal());
		Assert.assertEquals(1, stat.getSuccess());
		Assert.assertEquals(1, stat.getAborted());
		Assert.assertEquals(0, stat.getFailed());
		Assert.assertEquals(1, stat.getRunning());
		Assert.assertEquals(0, stat.getDisabled());
		Assert.assertEquals(1, stat.getInqueue());
		Assert.assertEquals(16, stat.getNobuilt());
		
		groupIds.add("view3");
		ret = Unirest.post(j.getURL() + "statistics/projectstat").headers(headers).body(json.toJSONString()).asJson();
		Assert.assertEquals(1, ret.getBody().getObject().getInt("rc"));
		Assert.assertEquals("success", ret.getBody().getObject().getString("msg"));
		data = ret.getBody().getObject().getJSONObject("data");
		stat = JsonUtil.parseObject(data.toString(), ProjectStat.class);

		Assert.assertEquals(30, stat.getTotal());
		Assert.assertEquals(1, stat.getSuccess());
		Assert.assertEquals(1, stat.getAborted());
		Assert.assertEquals(1, stat.getFailed());
		Assert.assertEquals(1, stat.getRunning());
		Assert.assertEquals(1, stat.getDisabled());
		Assert.assertEquals(1, stat.getInqueue());
		Assert.assertEquals(24, stat.getNobuilt());
	}

	private static void deployPluginByHttp() throws Exception {
		Unirest.post(j.getURL() + "pluginManager/uploadPlugin").headers(headers)
				.field("file", new File(StatisticsPluginTest.class.getResource("/statistics-plugin.hpi").toURI()))
				.asJson();
		String name = j.getPluginManager().getPlugin("statistics-plugin").getDisplayName();
		if (!"statistics-plugin".equals(name)) {
			throw new Exception("Failed to deploy plugin");
		}
	}

}
