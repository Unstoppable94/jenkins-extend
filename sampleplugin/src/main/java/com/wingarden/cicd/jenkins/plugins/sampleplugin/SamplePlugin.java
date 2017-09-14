package com.wingarden.cicd.jenkins.plugins.sampleplugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kohsuke.stapler.Stapler;
import org.kohsuke.stapler.WebMethod;
import org.kohsuke.stapler.export.Exported;
import org.kohsuke.stapler.export.ExportedBean;
import org.kohsuke.stapler.interceptor.RequirePOST;
import org.kohsuke.stapler.json.JsonBody;
import org.kohsuke.stapler.verb.DELETE;
import org.kohsuke.stapler.verb.PUT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wingarden.cicd.jenkins.common.dal.dao.BaseDao;
import com.wingarden.cicd.jenkins.common.infrastructure.InvisiblePlugin;
import com.wingarden.cicd.jenkins.common.utils.StringToolkit;
import com.wingarden.cicd.jenkins.plugins.sampleplugin.dal.dao.PersonDao;
import com.wingarden.cicd.jenkins.plugins.sampleplugin.dal.model.Person;

import hudson.Extension;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Extension
@ExportedBean
public class SamplePlugin extends InvisiblePlugin {
	private static final Logger log = LoggerFactory.getLogger(SamplePlugin.class);
	private static final BaseDao<Person> personDao = new PersonDao();

	@Override
	@WebMethod(name = "url")
	@Exported(name = "url")
	public String getUrlName() {
		return "sample";
	}

	/**
	 * Usage Example:
	 * 
	 * <pre>
	 * curl -X POST -H Content-Type:application/json ${JENKINS_PATH}/sample/create \
	 * -d '{"address":{"displayName":"address","id":"111","link":"广州市天河区","name":"Address"},"age":22,"displayName":"李征","name":"lizheng"}'
	 * </pre>
	 * 
	 * @param json
	 * @return
	 */
	@RequirePOST
	public Object doCreate(@JsonBody JSONObject json) {
		log.info("[create]" + json);
		Person person = (Person) JSONObject.toBean(json, Person.class);
		personDao.insert(person);
		return JSONObject.fromObject(person).toString();
	}

	/**
	 * Usage Example:
	 * 
	 * <pre>
	 * curl -X DELETE -H Content-Type:application/json   ${JENKINS_PATH}/sample/delete \
	 * -d '{"id":"694d53226b744686b6882673efafcec1"}'
	 * </pre>
	 * 
	 * @param json
	 * @return
	 */
	@DELETE
	public Object doDelete(@JsonBody JSONObject json) {
		log.info("[delete]" + json);
		String id = json.getString("id");
		json = new JSONObject();
		json.put("result", "fail");
		if (personDao.deleteById(id)) {
			json.put("result", "success");
		}
		return json.toString();
	}

	/**
	 * Usage Example:
	 * 
	 * <pre>
	 * curl ${JENKINS_PATH}/sample/person/personid/api/json
	 * curl ${JENKINS_PATH}/sample/person/personid/api/xml
	 * </pre>
	 * 
	 * @param id
	 * @return
	 */
	public Object getPerson(String id) {
		log.info("[person]" + id);
		return personDao.queryOne(id);
	}

	/**
	 * Usage Example:
	 * <pre>
	 * curl ${JENKINS_PATH}/sample/persons/personid1,personid2,...
	 * </pre>
	 * @return
	 */
	@WebMethod(name = "persons")
	public Object getPersons() {
		String ids = Stapler.getCurrentRequest().getRestOfPath();
		log.info("[person]" + ids);
		List<Person> persons = null;
		if (null == ids || ids.equals("")) {
			persons = personDao.queryAll();
		} else {
			ids = ids.substring(1).split("/")[0];
			String[] idArray = ids.split("\\,");
			persons = personDao.queryIds(idArray);
		}
		return JSONArray.fromObject(persons).toString();
	}

	/**
	 * Usage Example:
	 * <pre>
	 * curl -X PUT -H Content-Type:application/json
	 * ${JENKINS_PATH}/jenkins/sample/modify \ -d
	 * '{"displayName":"李征1","id":"694d53226b744686b6882673efafcec1","name":"lizheng",
	 * "address":{"displayName":"address","id":"111","name":"Address","link":"广州市天河区222"},"age":22}'
	 * </pre>
	 * @param json
	 * @return
	 */
	@PUT
	public Object doModify(@JsonBody JSONObject json) {
		log.info("[doModify]" + json);
		Person person = (Person) JSONObject.toBean(json, Person.class);
		personDao.update(person);
		return JSONObject.fromObject(person).toString();
	}

	/**
	 * Usage Example:
	 * <pre>
	 * curl -X POST -H Content-Type:application/json ${JENKINS_PATH}/sample/list \
 	 * -d '{"ids": ["694d53226b744686b6882673efafcec1","fc842e38c6f5427c961be56378fd14e1"]}'
 	 * </pre>
	 * @param qeryJson
	 * @return
	 */
	@RequirePOST
	public Object doList(@JsonBody JSONObject qeryJson) {
		log.info("[doList]" + qeryJson);
		String[] ids = StringToolkit.join(qeryJson.getJSONArray("ids"), ",", "").split("\\,");
		List<Person> ret = personDao.queryIds(ids);
		JSONObject json = new JSONObject();
		json.put("data", ret);
		return json.toString();
	}
}
