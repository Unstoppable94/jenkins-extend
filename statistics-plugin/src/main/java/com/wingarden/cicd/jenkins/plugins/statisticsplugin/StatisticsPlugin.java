package com.wingarden.cicd.jenkins.plugins.statisticsplugin;

import java.util.Date;
import java.util.List;

import org.kohsuke.stapler.WebMethod;
import org.kohsuke.stapler.export.Exported;
import org.kohsuke.stapler.export.ExportedBean;
import org.kohsuke.stapler.interceptor.RequirePOST;
import org.kohsuke.stapler.json.JsonBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wingarden.cicd.jenkins.common.infrastructure.InvisiblePlugin;
import com.wingarden.cicd.jenkins.common.utils.DateToolkit;
import com.wingarden.cicd.jenkins.common.utils.ExceptionUtils;
import com.wingarden.cicd.jenkins.common.utils.JsonResponseUtils;
import com.wingarden.cicd.jenkins.common.utils.JsonUtil;
import com.wingarden.cicd.jenkins.plugins.statisticsplugin.dal.model.ClusterStatData;

import hudson.Extension;
import jenkins.model.Jenkins;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Extension
@ExportedBean
public class StatisticsPlugin extends InvisiblePlugin {
	private static final Logger log = LoggerFactory.getLogger(StatisticsPlugin.class);
	private ClusterStatData statData = new ClusterStatData();

	@Override
	@WebMethod(name = "url")
	@Exported(name = "url")
	public String getUrlName() {
		return "statistics";
	}

	@RequirePOST
	@WebMethod(name = "buildstat")
	public Object doBuildStat(@JsonBody JSONObject json) {
		log.info("Param is [{}]",json.toString());
		try {
			List<String> groupIds = null;
			List<String> projectIds = null;
			Date beginTime = null;
			Date endTime = null;
			
			//入参处理
			if (json.containsKey("groupIds")) {
				groupIds = JSONArray.toList(json.getJSONArray("groupIds"), String.class);
			}
			if (json.containsKey("projectIds")) {
				projectIds = JSONArray.toList(json.getJSONArray("projectIds"), String.class);
			}
			if (json.containsKey("beginTime")) {
				beginTime = DateToolkit.utilStrToDate(json.getString("beginTime"));
			}
			if (json.containsKey("endTime")) {
				endTime = DateToolkit.utilStrToDate(json.getString("endTime"));
			}
			
			//时间校验
			if (null == beginTime && null != endTime) {
				return JsonResponseUtils.error("开始时间与结束必须同时存在！");
			}
			if (null != beginTime && null == endTime) {
				endTime = new Date();
			}
			if (null != beginTime && null != endTime && !endTime.after(beginTime)) {
				return JsonResponseUtils.error("结束时间不能大于开始时间！");
			}
			
			return JsonResponseUtils.success(statData.getCurtBuildStat(groupIds, projectIds, beginTime, endTime));
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResponseUtils.error(ExceptionUtils.makeStackTrace(e));
		}
	}

	@RequirePOST
	@WebMethod(name = "projectstat")
	public Object doProjectStat(@JsonBody JSONObject json) {
		log.info("Param is [{}]",json.toString());
		System.out.println(json.toString());
		try {
			List<String> groupIds = null;
			if (json.containsKey("groupIds")) {
				groupIds = JsonUtil.parseObject(json.getString("groupIds"), new com.alibaba.fastjson.TypeReference<List<String>>(){});
			}
			return JsonResponseUtils.success(statData.getCurProjectStat(groupIds));
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResponseUtils.error(ExceptionUtils.makeStackTrace(e));
		}
	}

	public static StatisticsPlugin getInstance() {
		return Jenkins.getInstance().getExtensionList(StatisticsPlugin.class).get(0);
	}

	public ClusterStatData getStatData() {
		return statData;
	}

	public void setStatData(ClusterStatData statData) {
		this.statData = statData;
	}

}
