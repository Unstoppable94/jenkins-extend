package com.wingarden.cicd.jenkins.plugins.sampleplugin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.jvnet.hudson.test.JenkinsRule;
import org.kohsuke.stapler.json.JsonResponse;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.wingarden.cicd.jenkins.plugins.sampleplugin.dal.model.Address;
import com.wingarden.cicd.jenkins.plugins.sampleplugin.dal.model.Person;

import net.sf.json.JSONObject;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SamplePluginTest {

	@ClassRule
	public static JenkinsRule j = new JenkinsRule();

	@Test
	public void testGetUrlName() throws Exception{
		Map<String, String> headers = new HashMap<>();
		if (j.jenkins.isUseCrumbs()) {
			headers.put(j.jenkins.getCrumbIssuer().getCrumbRequestField(), j.jenkins.getCrumbIssuer().getCrumb());
		}
		HttpResponse<String> url = Unirest.post(j.getURL()+"sample/url")
		  .headers(headers)
		  .asString();
		Assert.assertEquals("sample", url.getBody());
	}

	@Test
	public void test1_DeployPluginByHttp() throws Exception{
		Map<String, String> headers = new HashMap<>();
		headers.put("accept", "application/json");
		
		if (j.jenkins.isUseCrumbs()) {
			headers.put(j.jenkins.getCrumbIssuer().getCrumbRequestField(), j.jenkins.getCrumbIssuer().getCrumb());
		}
		HttpResponse<JsonNode> jsonResponse = Unirest.post(j.getURL()+"pluginManager/uploadPlugin")
		  .headers(headers)
		  .field("file", new File(getClass().getResource("/sampleplugin.hpi").toURI()))
		  .asJson();
		Assert.assertEquals("sampleplugin", j.getPluginManager().getPlugin("sampleplugin").getDisplayName());
		j.getInstance().getExtensionList(SamplePlugin.class);
		System.out.println("");
	}
	
	@Test
	public void test2_DoCreate() throws Exception{
		Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");
		if (j.jenkins.isUseCrumbs()) {
			headers.put(j.jenkins.getCrumbIssuer().getCrumbRequestField(), j.jenkins.getCrumbIssuer().getCrumb());
		}
		
		Person person = new Person();
		person.setId("person1");
		person.setAge(1);
		person.setName("人类1");
		Address address = new Address();
		address.setDisplayName("地址1");
		address.setId("addr1");
		address.setLink("广州市");
		address.setName("address1");
		person.setAddress(address);
		HttpResponse<JsonNode> ret = Unirest.post(j.getURL()+"sample/create")
		  .headers(headers)
		  .body(JSONObject.fromObject(person).toString())
		  .asJson();
		Assert.assertEquals("人类1", ret.getBody().getObject().getString("name"));
		
		person = new Person();
		person.setId("person2");
		person.setAge(2);
		person.setName("人类2");
		address = new Address();
		address.setDisplayName("地址2");
		address.setId("addr2");
		address.setLink("广州市2");
		address.setName("address1");
		person.setAddress(address);
		ret = Unirest.post(j.getURL()+"sample/create")
		  .headers(headers)
		  .body(JSONObject.fromObject(person).toString())
		  .asJson();
		
		Assert.assertEquals("人类2", ret.getBody().getObject().getString("name"));
	}

	@Test
	public void test4_DoDelete() throws Exception{
		Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");
		if (j.jenkins.isUseCrumbs()) {
			headers.put(j.jenkins.getCrumbIssuer().getCrumbRequestField(), j.jenkins.getCrumbIssuer().getCrumb());
		}
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", "person1");
		HttpResponse<JsonNode> ret = Unirest.delete(j.getURL()+"sample/delete")
		  .headers(headers)
		  .body(jsonObject.toString())
		  .asJson();
		Assert.assertEquals("success", ret.getBody().getObject().getString("result"));
	}

	@Test
	public void test3_GetPerson() throws Exception{
		Map<String, String> headers = new HashMap<>();
		if (j.jenkins.isUseCrumbs()) {
			headers.put(j.jenkins.getCrumbIssuer().getCrumbRequestField(), j.jenkins.getCrumbIssuer().getCrumb());
		}
		
		HttpResponse<JsonNode> ret = Unirest.get(j.getURL()+"sample/person/person1/api/json")
		  .headers(headers)
		  .asJson();
		Assert.assertEquals("人类1", ret.getBody().getObject().getString("name"));
	}

	@Test
	public void test5_GetPersons() throws Exception{
		Map<String, String> headers = new HashMap<>();
		if (j.jenkins.isUseCrumbs()) {
			headers.put(j.jenkins.getCrumbIssuer().getCrumbRequestField(), j.jenkins.getCrumbIssuer().getCrumb());
		}
		
		HttpResponse<JsonNode> ret = Unirest.get(j.getURL()+"sample/persons/person1,person2")
		  .headers(headers)
		  .asJson();
		Assert.assertEquals(1, ret.getBody().getArray().length());
	}

	@Test
	public void testDoModify() throws Exception{
		Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");
		if (j.jenkins.isUseCrumbs()) {
			headers.put(j.jenkins.getCrumbIssuer().getCrumbRequestField(), j.jenkins.getCrumbIssuer().getCrumb());
		}
		
		Person person = new Person();
		person.setId("person2");
		person.setName("人类22");
		HttpResponse<JsonNode> ret = Unirest.put(j.getURL()+"sample/modify")
		  .headers(headers)
		  .body(JSONObject.fromObject(person).toString())
		  .asJson();
		Assert.assertEquals("人类22", ret.getBody().getObject().getString("name"));
	}

	@Test
	public void testDoList() throws Exception{
		Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");
		if (j.jenkins.isUseCrumbs()) {
			headers.put(j.jenkins.getCrumbIssuer().getCrumbRequestField(), j.jenkins.getCrumbIssuer().getCrumb());
		}
		
		List<String> ids = new ArrayList<>();
		ids.add("person1");
		ids.add("person2");
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("ids", ids);
		/**
		 * 当SamplePluin#doList方法使用@JsonResponse标注时，由于stapler本身的处理将导致Unirest获取不了响应体
		 */
		HttpResponse<JsonNode> ret = Unirest.post(j.getURL()+"sample/list")
		  .headers(headers)
		  .body(jsonObject.toString())
		  .asJson();
		Assert.assertEquals(1, ret.getBody().getObject().getJSONArray("data").length());
	}
}
