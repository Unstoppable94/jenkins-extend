package com.wingarden.cicd.jenkins.common.infrastructure;

import hudson.model.Api;
import hudson.model.RootAction;

public abstract class InvisiblePlugin implements RootAction,ApiAble{

	public final String getIconFileName() {
		return null;
	}

	public final String getDisplayName() {
		return null;
	}
	
	public final Api getApi() {
        return new ApiExt(this);
    }
	
	public abstract String getUrlName();
}
