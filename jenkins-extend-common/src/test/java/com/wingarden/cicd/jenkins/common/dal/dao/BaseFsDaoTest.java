package com.wingarden.cicd.jenkins.common.dal.dao;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runners.MethodSorters;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;

import com.wingarden.cicd.jenkins.common.infrastructure.AbstractResource;

import hudson.FilePath;
import jenkins.model.Jenkins;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//注意：使用PowerMockRunner的情况下，使用@FixMethodOrder不生效，不能保证测试方法的执行顺序。因而改用PowerMockRule
//@RunWith(PowerMockRunner.class)
@PrepareForTest({ Jenkins.class })
public class BaseFsDaoTest {
	private static TestDao dao;
	private static File root ;
//	@Mock
//	private Jenkins jenkins;
	
//  在本测试类中，ExpectedException的方式不生效
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Rule
	public PowerMockRule rule = new PowerMockRule();

	@Before
	public void setUp() throws Exception {
		PowerMockito.mockStatic(Jenkins.class);
		Jenkins jenkins = PowerMockito.mock(Jenkins.class);
		PowerMockito.when(Jenkins.getInstance()).thenReturn(jenkins);
		PowerMockito.when(Jenkins.getInstance().getRootDir()).thenReturn(root);
	}
	
	static {
		//以下语句放入init方法中，会报空指针异常
		File tmpDir = new File(System.getProperty("java.io.tmpdir")+"/jenkins");
		if (!tmpDir.exists()) {
			tmpDir.mkdirs();
		}
		root = tmpDir;
		dao = new TestDao();
	}
	
	@BeforeClass
	public static void init() {
		
	}
	
	@AfterClass
	public static void clear() {
		try {
			FilePath filePath = new FilePath(root);
			filePath.deleteContents();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testCountAll() {
		int num = dao.countAll();
		Assert.assertEquals(7, num);
	}

	@Test(expected=UnsupportedOperationException.class)
//	@Test
	public void testCount() {
//		expectedEx.expect(UnsupportedOperationException.class);
		dao.count(new Object());
	}

	@Test
	public void testQueryAll() {
		List<TestResource> rss = dao.queryAll();
		Assert.assertNotNull(rss);
		Assert.assertEquals(7, rss.size());
	}

	@Test
	public void testQueryIds() {
		String[] ids = { "14", "5" };
		List<TestResource> rss = dao.queryIds(ids);
		Assert.assertNotNull(rss);
		Assert.assertEquals(1, rss.size());
	}

	@Test
	public void testQueryOne() {
		TestResource rs = dao.queryOne("2");
		Assert.assertEquals("test2", rs.getName());
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testQuery() {
//		expectedEx.expect(UnsupportedOperationException.class);
		dao.query(new Object());
	}

	@Test
	public void test1_Insert() {
		TestResource rs = new TestResource();
		rs.setId("1");
		rs.setName("test1");
		rs.setDisplayName("test1");
		dao.insert(rs);

		int num = dao.countAll();
		Assert.assertEquals(1, num);
	}

	@Test
	public void test2_BatchInsert() {
		List<TestResource> resources = new ArrayList<>();
		for (int i = 2; i <= 10; i++) {
			TestResource rs = new TestResource();
			rs.setId(i + "");
			rs.setName("test" + i);
			rs.setDisplayName("test" + i);
			resources.add(rs);
		}
		dao.batchInsert(resources);

		int num = dao.countAll();
		Assert.assertEquals(10, num);
	}

	@Test
	public void test3_Update() {
		TestResource rs = dao.queryOne("1");
		rs.setName("chgname");
		rs.setDisplayName("chgname");
		rs = dao.update(rs);
		Assert.assertEquals("chgname", rs.getName());
		Assert.assertEquals("chgname", rs.getDisplayName());
	}

	@Test
	public void test4_DeleteById() {
		boolean ret = dao.deleteById("10");
		Assert.assertTrue(ret);
		Assert.assertEquals(9, dao.countAll());
	}

	@Test
	public void test5_DeleteByIds() {
		String[] ids = { "8", "9" };
		int num = dao.deleteByIds(ids);
		Assert.assertEquals(2, num);
		Assert.assertEquals(7, dao.countAll());
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testDelete() {
		dao.delete(new Object());
	}

	@Test
	public void testGetResourceClz() {
		Assert.assertEquals(TestResource.class.getName(), dao.getResourceClz().getName());
	}

	static class TestResource extends AbstractResource {

	}

	static class TestDao extends BaseFsDao<TestResource> {

	}
}
