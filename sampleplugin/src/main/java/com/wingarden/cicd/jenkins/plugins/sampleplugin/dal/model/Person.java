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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + age;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Person other = (Person) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (age != other.age)
			return false;
		return true;
	}
	
	
}
