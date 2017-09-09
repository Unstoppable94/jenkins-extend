package com.wingarden.cicd.jenkins.common.infrastructure;

import hudson.model.ModelObject;
import hudson.security.AccessControlled;

public interface Resource extends ModelObject, ApiAble,AccessControlled{
	String getId();
	void setId(String id);
	String getName();
	String getUrl();
	String getResourcePath();
	String getRootPath();
}
