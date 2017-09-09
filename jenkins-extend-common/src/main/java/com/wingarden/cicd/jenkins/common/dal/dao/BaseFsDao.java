package com.wingarden.cicd.jenkins.common.dal.dao;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import com.wingarden.cicd.jenkins.common.infrastructure.AbstractResource;
import com.wingarden.cicd.jenkins.common.utils.LogUtils;
import com.wingarden.cicd.jenkins.common.utils.UUIDGenerator;
import com.wingarden.cicd.jenkins.common.utils.XmlUtil;

public class BaseFsDao<T extends AbstractResource> implements BaseDao<T> {

	private T tmpRs;

	public BaseFsDao() {
		try {
			tmpRs = getResourceClz().newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int countAll() {
		File file = new File(tmpRs.getParentPath());
		File[] files = file.listFiles(new FileFilter() {

			@Override
			public boolean accept(File listFile) {
				return listFile.isFile() && listFile.getName().matches(tmpRs.getXmlFileNamePattern());
			}
		});
		return null != files ? files.length : 0;
	}

	@Override
	public int count(Object param) {
		// TODO:待补充
		throw new UnsupportedOperationException();
	}

	@Override
	public List<T> queryAll() {
		List<T> retList = new ArrayList<>();
		File file = new File(tmpRs.getParentPath());
		File[] files = file.listFiles(new FileFilter() {
			@Override
			public boolean accept(File listFile) {
				return listFile.isFile() && listFile.getName().matches(tmpRs.getXmlFileNamePattern());
			}
		});
		try {
			if (null != files) {
				for (File file2 : files) {
					retList.add((T) XmlUtil.load(file2));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return retList;
	}

	@Override
	public List<T> queryIds(final String... ids) {
		LogUtils.debug("[{}---queryIds]{}", this.getClass().getName(),(null == ids ? "" : ids).toString());
		List<T> retList = new ArrayList<>();
		File file = new File(tmpRs.getParentPath());
		File[] files = file.listFiles(new FileFilter() {
			@Override
			public boolean accept(File listFile) {
				return listFile.isFile() && listFile.getName().matches(tmpRs.getXmlFileNamePattern(ids));
			}
		});
		try {
			if (null != files) {
				for (File file2 : files) {
					retList.add((T) XmlUtil.load(file2));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return retList;
	}
	@Override
	public T queryOne(String id) {
		File file = new File(getResourcePathById(id));
		T ret = null;
		try {
			ret = (T) XmlUtil.load(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ret;
	}

	@Override
	public List<T> query(Object param) {
		// TODO:待补充
		throw new UnsupportedOperationException();
	}

	@Override
	public T insert(T rs) {
		try {
			if (null == rs.getId() || rs.getId().isEmpty()) {
				rs.setId(UUIDGenerator.getUUID32());
			}
			XmlUtil.insertResource(rs);
		} catch (IOException e) {
			return null;
		}
		return rs;
	}

	@Override
	public int batchInsert(List<T> resources) {
		int cnt = 0;
		for (T rs : resources) {
			try {
				T result = insert(rs);
				if (null != result) {
					cnt++;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return cnt;
	}

	@Override
	public T update(T rs) {
		try {
			XmlUtil.updateResource(rs);
		} catch (IOException e) {
			return null;
		}
		return rs;
	}

	@Override
	public boolean deleteById(String id) {
		return new File(getResourcePathById(id)).delete();
	}

	@Override
	public int deleteByIds(String... ids) {
		int cnt = 0;
		for (String id : ids) {
			if (deleteById(id)) {
				cnt++;
			}
		}
		return cnt;
	}

	@Override
	public int delete(Object param) {
		// TODO:待补充
		throw new UnsupportedOperationException();
	}

	protected Class<T> getResourceClz() {
		return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	private synchronized String getResourcePathById(String id) {
		tmpRs.setId(id);
		return new StringBuilder().append(tmpRs.getParentPath()).append(tmpRs.getXmlFileName()).append(".xml")
				.toString();
	}
}
