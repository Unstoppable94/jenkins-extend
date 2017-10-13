package com.wingarden.cicd.jenkins.plugins.sampleplugin;

import java.io.Serializable;
import java.util.List;

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
public class SamplePlugin extends InvisiblePlugin implements Serializable {
	private static final Logger log = LoggerFactory.getLogger(SamplePlugin.class);
	private static final BaseDao<Person> personDao = new PersonDao();

	@Override
	@WebMethod(name = "url")
	@Exported(name = "url")
	public String getUrlName() {
		return "sample";
	}

//	@WebMethod(name = "getprops")
//	@Exported(name = "props")
//	public String getProps() throws Exception {
//		String ret = "";
//		if (Jenkins.getInstance().getComputer("test_agent") != null) {
//			VirtualChannel channel = Jenkins.getInstance().getComputer("test_agent").getChannel();
//			FilePath filePath = new FilePath(channel, "/home/test");
//
//			try {
//				filePath.mkdirs();
//				filePath.child("test.txt").touch(System.currentTimeMillis());
//			} catch (IOException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			} catch (InterruptedException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//			try {
//				// final SerializableJenkinsFactory factory = new SerializableJenkinsFactory();
//				Properties props = channel.call(new MasterToSlaveCallable<Properties, Exception>() {
//					public Properties call() throws Exception {
//						// System.out.println("run on " + factory.create().getDisplayName());
//						System.out.println("Jenkins instance is :" + Jenkins.getInstance());
//						return System.getProperties();
//					}
//				});
//
//				System.out.println(props);
//				ret = props.toString();
//				System.out.println("###########################################");
//				System.out.println(System.getProperties());
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (RuntimeException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//
//		return ret;
//	}
//
//	@WebMethod(name = "readlog")
//	@Exported(name = "readlog")
//	public String readBackSomeFileFromMaster() throws Exception {
//		final String path = new File(Jenkins.getInstance().root, "config.xml").getAbsolutePath();
//		System.out.println(path);
//		// final Jenkins jenkins = Jenkins.getInstance();
//		// Computer c = Jenkins.getInstance().getComputer("test_agent");
//		Computer c = Jenkins.getInstance().getComputer("test_agent");
//		if (c.isOffline()) {
//			System.out.println("offline::" + System.currentTimeMillis());
//			c.waitUntilOnline();
//			System.out.println("online::" + System.currentTimeMillis());
//		}
//		VirtualChannel channel = c.getChannel();
//		final ChannelProperty<Person> prop = new ChannelProperty<>(Person.class, "person1");
//		System.out.println("prop is::" + prop);// prop is::hudson.remoting.ChannelProperty@25e165d6
//		if (channel instanceof Channel) {
//			System.out.println("is channel");
//			((Channel) channel).setProperty("nodeName", "test_agent");
//
//			Person person = new Person();
//			((Channel) channel).setProperty(prop, person);
//			((Channel) channel).setProperty("person", person);
//			System.out.println("Jenkins is:" + person);
//		}
//		String ret = channel.call(new SlaveToMasterCallable<String, Exception>() {
//			public String call() throws Exception {
//				// System.out.println("run on " + jenkins.toComputer().getName());
//				FilePath filePath = new FilePath(SlaveComputer.getChannelToMaster(), path);
//				String ret = filePath.readToString();
//				System.out.println("The Computer is:" + SlaveComputer.currentComputer());
//				System.out.println("The Executor is :" + Executor.currentExecutor().getDisplayName());
//				System.out.println("Channel name is::" + Channel.current().getName());
//				System.out.println("Node name is::" + Channel.current().getRemoteProperty("nodeName"));
//				// final ChannelProperty<Person> prop = new ChannelProperty<>(Person.class,
//				// "person1");
//				System.out.println("prop is::" + prop);// but here:prop is::hudson.remoting.ChannelProperty@4879d0b
//				Person person = Channel.current().getRemoteProperty(prop);
//				System.out.println("Jenkins is:" + person);
//				System.out.println("Person is::" + Channel.current().getRemoteProperty("person"));
//				System.out.println("Person is::" + Channel.current().getRemoteProperty(prop));
//				System.out.println("read:: " + ret);
//
//				// System.out.println(Computer.currentComputer().getNode().getNodeName());//NullPointException
//
//				filePath.act(new SlaveToMasterFileCallable<String>() {
//					@Override
//					public String invoke(File f, VirtualChannel channel) throws IOException, InterruptedException {
//						if (channel instanceof Channel) {
//							System.out.println("is master channel");
//							System.out.println("Jenkins master is:::" + ((Channel) channel).getProperty(prop));
//							System.out.println("Jenkins master is:::" + ((Channel) channel).getProperty("nodeName"));
//							System.out.println("Jenkins master is:::" + ((Channel) channel).getProperty("person"));
//						}
//						return null;
//					}
//				});
//				return ret;
//			}
//		});
//
//		return ret;
//	}

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
	 * 
	 * <pre>
	 * curl ${JENKINS_PATH}/sample/persons/personid1,personid2,...
	 * </pre>
	 * 
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
	 * 
	 * <pre>
	 * curl -X PUT -H Content-Type:application/json
	 * ${JENKINS_PATH}/jenkins/sample/modify \ -d
	 * '{"displayName":"李征1","id":"694d53226b744686b6882673efafcec1","name":"lizheng",
	 * "address":{"displayName":"address","id":"111","name":"Address","link":"广州市天河区222"},"age":22}'
	 * </pre>
	 * 
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
	 * 
	 * <pre>
	 * curl -X POST -H Content-Type:application/json ${JENKINS_PATH}/sample/list \
	 * -d '{"ids": ["694d53226b744686b6882673efafcec1","fc842e38c6f5427c961be56378fd14e1"]}'
	 * </pre>
	 * 
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
