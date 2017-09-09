package com.wingarden.cicd.jenkins.plugins.sampleplugin.dal.model;

import org.kohsuke.stapler.export.Exported;

import com.wingarden.cicd.jenkins.common.infrastructure.AbstractResource;

public class Address extends AbstractResource{

	private String link;

	@Exported
	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	@Override
	public String getDisplayName() {
		return "address";
	}
}
