package com.wingarden.cicd.jenkins.plugins.statisticsplugin.listener;

import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.model.Item;
import hudson.model.listeners.ItemListener;

//@Extension
public class MyItemListener extends ItemListener {

	@Override
	public void onCreated(Item item) {
		// TODO Auto-generated method stub
		System.out.println("MyItemListener#onCreated!");
		System.out.println(item.getFullDisplayName());
		System.out.println(item.getClass());
		if (item instanceof AbstractProject) {
			System.out.println("The Color is:::" + ((AbstractProject)item).getIconColor().getDescription());
		}
		
		super.onCreated(item);
	}
	
	@Override
	public void onDeleted(Item item) {
		// TODO Auto-generated method stub
		System.out.println("MyItemListener#onDeleted!");
		System.out.println(item.getFullDisplayName());
		System.out.println(item.getClass());
		super.onDeleted(item);
	}
	
}
