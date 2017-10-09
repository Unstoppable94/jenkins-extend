package com.wingarden.cicd.jenkins.plugins.statisticsplugin.dal.model;

import java.util.Date;

import org.kohsuke.stapler.export.Exported;

import com.wingarden.cicd.jenkins.common.infrastructure.AbstractResource;

public class BuildStat extends AbstractResource {
	/**
	 * 
	 */
	private static final long serialVersionUID = 456719882786746519L;
	private Date statTime;
	private Date buildTime;
	private long total;
	private long failed;
	private long running;
	private long success;
	private long aborted;
	private long pending;
	private String nodeName;

	@Exported
	public Date getStatTime() {
		return statTime == null ? null : (Date) statTime.clone();
	}

	public void setStatTime(Date statTime) {
		this.statTime = (statTime == null) ? null : Date.class.cast(statTime.clone());
	}

	@Exported
	public Date getBuildTime() {
		return buildTime == null ? null : (Date) buildTime.clone();
	}

	public void setBuildTime(Date buildTime) {
		this.buildTime = (buildTime == null) ? null : Date.class.cast(buildTime.clone());
	}

	@Exported
	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	@Exported
	public long getFailed() {
		return failed;
	}

	public void setFailed(long failed) {
		this.failed = failed;
	}

	@Exported
	public long getRunning() {
		return running;
	}

	public void setRunning(long running) {
		this.running = running;
	}

	@Exported
	public long getSuccess() {
		return success;
	}

	public void setSuccess(long success) {
		this.success = success;
	}

	@Exported
	public long getAborted() {
		return aborted;
	}

	public void setAborted(long aborted) {
		this.aborted = aborted;
	}

	@Exported
	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	@Exported
	public long getPending() {
		return pending;
	}

	public void setPending(long pending) {
		this.pending = pending;
	}

	public String getXmlFileName() {
		return getStatTime().getTime() + "_" + this.getClass().getSimpleName() + "@" + getId();
	}
}
