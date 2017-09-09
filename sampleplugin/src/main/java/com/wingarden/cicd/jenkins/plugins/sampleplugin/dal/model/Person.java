package com.wingarden.cicd.jenkins.plugins.sampleplugin.dal.model;

import org.kohsuke.stapler.export.Exported;

import com.wingarden.cicd.jenkins.common.infrastructure.AbstractResource;

public class Person extends AbstractResource {
	private int age;
	private Address address;

	@Exported
	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	@Exported
	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}
}
