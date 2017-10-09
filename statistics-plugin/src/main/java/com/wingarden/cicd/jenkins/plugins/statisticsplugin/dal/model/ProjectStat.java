package com.wingarden.cicd.jenkins.plugins.statisticsplugin.dal.model;

import java.util.Date;

import com.wingarden.cicd.jenkins.common.infrastructure.AbstractResource;

public class ProjectStat extends AbstractResource {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3965133700132728058L;
	private Date projectStatTime;
	private long total;
	private long failed;
	private long running;
	private long inqueue;
	private long nobuilt;
	private long success;
	private long aborted;
	private long disabled;
	private String nodeName;
	
	public Date getProjectStatTime() {
		return projectStatTime == null ? null : (Date)projectStatTime.clone();
	}

	public void setProjectStatTime(Date projectStatTime) {
		this.projectStatTime = (Date)projectStatTime.clone();
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public long getFailed() {
		return failed;
	}

	public void setFailed(long failed) {
		this.failed = failed;
	}

	public long getRunning() {
		return running;
	}

	public void setRunning(long running) {
		this.running = running;
	}

	public long getInqueue() {
		return inqueue;
	}

	public void setInqueue(long inqueue) {
		this.inqueue = inqueue;
	}

	public long getNobuilt() {
		return nobuilt;
	}

	public void setNobuilt(long nobuilt) {
		this.nobuilt = nobuilt;
	}

	public long getSuccess() {
		return success;
	}

	public void setSuccess(long success) {
		this.success = success;
	}

	public long getAborted() {
		return aborted;
	}

	public void setAborted(long aborted) {
		this.aborted = aborted;
	}

	public long getDisabled() {
		return disabled;
	}

	public void setDisabled(long disabled) {
		this.disabled = disabled;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getXmlFileName() {
		return getProjectStatTime().getTime() + "_" + this.getClass().getSimpleName() + "@" + getId();
	}
}
