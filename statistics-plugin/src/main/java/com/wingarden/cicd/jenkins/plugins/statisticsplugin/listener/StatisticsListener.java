package com.wingarden.cicd.jenkins.plugins.statisticsplugin.listener;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.wingarden.cicd.jenkins.plugins.statisticsplugin.StatisticsPlugin;
import com.wingarden.cicd.jenkins.plugins.statisticsplugin.dal.model.BuildStat;
import com.wingarden.cicd.jenkins.plugins.statisticsplugin.dal.model.ProjectStat;

import hudson.EnvVars;
import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.model.Environment;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.model.Run.RunnerAbortedException;
import hudson.model.listeners.RunListener;
import hudson.util.LogTaskListener;

//@Extension
public class StatisticsListener extends RunListener<Run<?, ?>> {
	private static final Logger log = Logger.getLogger(StatisticsListener.class.getName());

	@Override
	public void onCompleted(Run r, TaskListener listener) {
		System.out.println("MyRunListener::onCompleted");
		System.out.println(" RunListener is:::" + r);
//		System.out.println(r.getExecutor().getOwner().getDisplayName());
		System.out.println("The Color is:::" + r.getIconColor().getDescription());
		super.onCompleted(r, listener);
//		StatisticsPlugin plugin = StatisticsPlugin.getInstance();
//		BuildStat buildStat = plugin.getStatData().getBuildStatData().get(0);
//
//		buildStat.setNodeName(getNodeName(r));
//
//		buildStat.setSuccess(buildStat.getSuccess() + 1);
		// TODO:对于构建，监听每个构建的最后执行
	}

	@Override
	public void onFinalized(Run<?, ?> r) {
		System.out.println("MyRunListener::onFinalized");
		System.out.println(" RunListener is:::" + r);
		System.out.println("The Color is:::" + r.getIconColor().getDescription());
		System.out.println("The build size is:::" + r.getParent().getBuilds().size());
		super.onFinalized(r);
	}

	@Override
	public void onInitialize(Run<?, ?> r) {
		System.out.println("MyRunListener::onInitialize");
		System.out.println(" RunListener is:::" + r);
		System.out.println("The Color is:::" + r.getIconColor().getDescription());
		super.onInitialize(r);
	}

	@Override
	public void onStarted(Run<?, ?> r, TaskListener listener) {
		System.out.println("MyRunListener::onStarted");
		System.out.println(" RunListener is:::" + r);
		System.out.println("The Color is:::" + r.getIconColor().getDescription());
		super.onStarted(r, listener);
	}

	@Override
	public Environment setUpEnvironment(AbstractBuild build, Launcher launcher, BuildListener listener)
			throws IOException, InterruptedException, RunnerAbortedException {
		System.out.println("MyRunListener::setUpEnvironment");
		System.out.println("The Color is:::" + build.getIconColor().getDescription());
		// TearDown
		class TearDownImpl extends Environment {

			@Override
			public boolean tearDown(AbstractBuild build, BuildListener listener)
					throws IOException, InterruptedException {
				listener.getLogger()
						.println("MyRunListener#setUpEnvironment#tearDown::The build is  :::" + build.getDisplayName());
				System.out
						.println("MyRunListener#setUpEnvironment#tearDown::The build is  :::" + build.getDisplayName());
				System.out.println("The Color is:::" + build.getIconColor().getDescription());
				return super.tearDown(build, listener);
			}

		}

		return new TearDownImpl();
	}

	@Override
	public void onDeleted(Run<?, ?> r) {
		System.out.println("MyRunListener::onDeleted");
		System.out.println(" RunListener is:::" + r);
		System.out.println("The Color is:::" + r.getIconColor().getDescription());
		super.onDeleted(r);
	}

	private String getNodeName(Run r) {
		String nodeName = "master";

		try {
			EnvVars envVars = r.getEnvironment(new LogTaskListener(log, Level.INFO));
			nodeName = envVars.get("NODE_NAME");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return nodeName;
	}
}
