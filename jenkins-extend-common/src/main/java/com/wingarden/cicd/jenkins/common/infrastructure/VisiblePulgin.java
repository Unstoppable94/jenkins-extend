package com.wingarden.cicd.jenkins.common.infrastructure;

import hudson.model.Api;
import hudson.model.RootAction;

public abstract class VisiblePulgin implements RootAction ,ApiAble{
	
	public abstract String getIconFileName();

	public abstract String getDisplayName();
	
	public abstract String getUrlName();
	
	public final Api getApi() {
        return new ApiExt(this);
    }
	
}
