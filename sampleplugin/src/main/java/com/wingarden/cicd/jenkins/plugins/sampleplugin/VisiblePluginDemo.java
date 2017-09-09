package com.wingarden.cicd.jenkins.plugins.sampleplugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.kohsuke.stapler.Stapler;
import org.kohsuke.stapler.WebMethod;
import org.kohsuke.stapler.export.Exported;
import org.kohsuke.stapler.export.ExportedBean;
import org.kohsuke.stapler.interceptor.RequirePOST;
import org.kohsuke.stapler.json.JsonBody;
import org.kohsuke.stapler.json.JsonResponse;
import org.kohsuke.stapler.verb.DELETE;
import org.kohsuke.stapler.verb.PUT;

import com.wingarden.cicd.jenkins.common.infrastructure.InvisiblePlugin;
import com.wingarden.cicd.jenkins.plugins.sampleplugin.dal.model.Address;
import com.wingarden.cicd.jenkins.plugins.sampleplugin.dal.model.Person;

import hudson.Extension;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Extension
@ExportedBean
public class VisiblePluginDemo extends InvisiblePlugin {

	@Exported(name="data")
	public final static Map<String, Person> dataMap = new ConcurrentHashMap<>();
	
	static {
		for (int i = 0; i < 10; i++) {
			Person person = new Person();
			person.setName(String.format("person%s", i));
			person.setAge(i);
			person.setDisplayName(String.format("人员%s", i));
			Address address = new Address();
			address.setLink(String.format("The addres is Street %s Road %s", i,i));
			person.setAddress(address);
			dataMap.put(person.getName(), person);
		}
	}
	
	@Override
	@WebMethod(name="url")
	@Exported(name="url")
	public String getUrlName() {
		return "sample2";
	}
	
	@RequirePOST
	public Object doCreate(@JsonBody JSONObject json) {
		System.out.println("[create]" + json);
		Person person = (Person)JSONObject.toBean(json, Person.class);
		if (!dataMap.containsKey(person.getName())) {
			dataMap.put(person.getName(), person);
		}
		return JSONObject.fromObject(person).toString();
	}

	@DELETE
	public Object doDelete(@JsonBody JSONObject json) {
		System.out.println("[delete]" + json);
		String id = json.getString("id");
		Person person = dataMap.remove(id);
		return JSONObject.fromObject(person).toString();
	}

	public Object getPerson(String id) {
		System.out.println("[person]" + id);
		System.out.println(dataMap.get(id));
		return dataMap.get(id);
	}
	
	@WebMethod(name="persons")
	public Object getPersons() {
		String ids = Stapler.getCurrentRequest().getRestOfPath();
		System.out.println("[persons]" + ids);
		List<Person> persons = new ArrayList<>();
		if (null != ids && !"".equals(ids)) {
			ids = ids.substring(1).split("/")[0];
			System.out.println("[person ids]" + ids);
			for (String id : ids.split(",")) {
				if (dataMap.containsKey(id)) {
					persons.add(dataMap.get(id));
				}
			}
		}
		return JSONArray.fromObject(persons).toString();
	}

	@PUT
	public Object doModify(@JsonBody JSONObject json) {
		System.out.println("[doModify]" + json);
		Person person = (Person)JSONObject.toBean(json, Person.class);
		if (dataMap.containsKey(person.getName())) {
			dataMap.put(person.getName(),person);
		}
		return JSONObject.fromObject(person).toString();
	}

	@RequirePOST
	@JsonResponse
	public Object doList(@JsonBody JSONObject qeryJson) {
		System.out.println("[doList]" + qeryJson);
		List<Person> ret = new ArrayList<>();
		for (Object name : qeryJson.getJSONArray("names")) {
			if (dataMap.containsKey(name.toString())) {
				ret.add(dataMap.get(name.toString()));
			}
		}
		
		Map<String, List<Person>> retMap = new HashMap<>();
		retMap.put("data", ret);
		return retMap;
	}

}
