package com.wingarden.cicd.jenkins.common.dal.dao;

import java.util.List;

import com.wingarden.cicd.jenkins.common.infrastructure.Resource;

public interface BaseDao<T extends Resource> {
	T insert(T rs);
	int batchInsert(List<T> resources);
	T update(T rs);
	int countAll();
	int count(Object param);
	boolean deleteById(String id);
	int deleteByIds(String ...ids);
	int delete(Object param);
	List<T> queryIds(String ... ids);
	List<T> queryAll();
	T queryOne(String id);
	List<T> query(Object param); 
}
