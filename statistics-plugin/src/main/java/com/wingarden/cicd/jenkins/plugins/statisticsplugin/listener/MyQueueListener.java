package com.wingarden.cicd.jenkins.plugins.statisticsplugin.listener;

import hudson.Extension;
import hudson.model.Queue.BlockedItem;
import hudson.model.Queue.BuildableItem;
import hudson.model.Queue.LeftItem;
import hudson.model.Queue.WaitingItem;
import hudson.model.queue.QueueListener;

//@Extension
public class MyQueueListener extends QueueListener{
	@Override
	public void onEnterWaiting(WaitingItem wi) {
		// TODO Auto-generated method stub
		System.out.println("onEnterWaiting");
		System.out.println(wi.task.getDisplayName());
		System.out.println(wi.task.getClass());
		super.onEnterWaiting(wi);
	}

	@Override
	public void onLeaveWaiting(WaitingItem wi) {
		// TODO Auto-generated method stub
		System.out.println("onLeaveWaiting");
		System.out.println(wi.task.getDisplayName());
		System.out.println(wi.task.getClass());
		super.onLeaveWaiting(wi);
	}

	@Override
	public void onEnterBlocked(BlockedItem bi) {
		// TODO Auto-generated method stub
		System.out.println("onEnterBlocked");
		System.out.println(bi.task.getDisplayName());
		System.out.println(bi.task.getClass());
		super.onEnterBlocked(bi);
	}

	@Override
	public void onLeaveBlocked(BlockedItem bi) {
		// TODO Auto-generated method stub
		System.out.println("onLeaveBlocked");
		System.out.println(bi.task.getDisplayName());
		System.out.println(bi.task.getClass());
		super.onLeaveBlocked(bi);
	}

	@Override
	public void onEnterBuildable(BuildableItem bi) {
		// TODO Auto-generated method stub
		System.out.println("onEnterBuildable");
		System.out.println(bi.task.getDisplayName());
		System.out.println(bi.task.getClass());
		super.onEnterBuildable(bi);
	}

	@Override
	public void onLeaveBuildable(BuildableItem bi) {
		// TODO Auto-generated method stub
		System.out.println("onLeaveBuildable");
		System.out.println(bi.task.getDisplayName());
		System.out.println(bi.task.getClass());
		super.onLeaveBuildable(bi);
	}

	@Override
	public void onLeft(LeftItem li) {
		// TODO Auto-generated method stub
		System.out.println("onLeft");
		System.out.println(li.task.getDisplayName());
		System.out.println(li.task.getClass());
		super.onLeft(li);
	}
	
	
}
