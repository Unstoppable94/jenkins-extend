package com.wingarden.cicd.jenkins.common.dal.model;

import java.util.Collection;

import com.google.common.base.Predicate;

import hudson.model.Job;
import hudson.model.Run;
import hudson.model.View;
import hudson.util.RunList;

@SuppressWarnings("rawtypes")
public class BuildList<R extends Run> {
	private RunList<R> runList;

	public BuildList() {
		runList = new RunList<>();
	}

	public BuildList(Job j) {
		runList = new RunList<>(j);
	}

	public BuildList(View view) {
		runList = new RunList<>(view);
	}

	public BuildList(Collection<Job> jobs) {
		runList = new RunList<>(jobs);
	}

	public RunList<R> successOnly() {
		return runList.filter(new Predicate<R>() {
			public boolean apply(R r) {
				return BuildStatus.SUCCESS.isStatus(r.getIconColor());
			}
		});
	}

	public RunList<R> failedOnly() {
		return runList.filter(new Predicate<R>() {
			public boolean apply(R r) {
				return BuildStatus.FAILED.isStatus(r.getIconColor());
			}
		});
	}

	public RunList<R> runningOnly() {
		return runList.filter(new Predicate<R>() {
			public boolean apply(R r) {
				return BuildStatus.RUNNING.isStatus(r.getIconColor());
			}
		});
	}

	public RunList<R> abortedOnly() {
		return runList.filter(new Predicate<R>() {
			public boolean apply(R r) {
				return BuildStatus.ABORTED.isStatus(r.getIconColor());
			}
		});
	}

	public RunList<R> pendingOnly() {
		return runList.filter(new Predicate<R>() {
			public boolean apply(R r) {
				return BuildStatus.PENDING.isStatus(r.getIconColor());
			}
		});
	}
	
	public RunList<R> byTimestamp(final long start, final long end) {
        return runList.byTimestamp(start, end);
    }

	public RunList<R> getRunList() {
		return runList;
	}
	
	
}
