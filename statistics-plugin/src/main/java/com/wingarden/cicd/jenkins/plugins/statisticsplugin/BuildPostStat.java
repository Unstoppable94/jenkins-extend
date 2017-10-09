package com.wingarden.cicd.jenkins.plugins.statisticsplugin;

import java.io.IOException;
import java.util.Properties;
import java.util.Set;

import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.model.Label;
import hudson.model.Node;
import hudson.model.TopLevelItem;
import hudson.tasks.BuildWrapper;
import hudson.tasks.BuildWrapperDescriptor;
import jenkins.security.MasterToSlaveCallable;

//TODO:未完成
public class BuildPostStat extends BuildWrapper {
	
	public boolean statOnSlave;//全局配置，指定是否通过slave节点计算统计信息

	@Override
	public Environment setUp(AbstractBuild build, Launcher launcher, BuildListener listener)
			throws IOException, InterruptedException {
		// TearDown
		class TearDownImpl extends Environment {

			@Override
			public boolean tearDown(AbstractBuild build, BuildListener listener)
					throws IOException, InterruptedException {
				if (statOnSlave) {
					executeOnSlaves(build, listener);
				}
				return super.tearDown(build, listener);
			}

		}

		return new TearDownImpl();
	}

	@SuppressWarnings("rawtypes")
	private void executeOnSlaves(AbstractBuild build, BuildListener listener) {
		listener.getLogger().println("run BuildPostStat");
		String runNode = build.getBuiltOnStr();

		if (runNode.length() == 0) {
			listener.getLogger().println("running on master");
		} else {
			listener.getLogger().println("running on " + runNode);
		}

		AbstractProject project = build.getProject();
		Label assignedLabel = project.getAssignedLabel();
		if (assignedLabel == null) {
			listener.getLogger().println("skipping roaming project.");
			return;
		}
		Set<Node> nodesForLabel = assignedLabel.getNodes();
		if (nodesForLabel != null) {
			for (Node node : nodesForLabel) {
				if (!runNode.equals(node.getNodeName())) {
					String normalizedName = "".equals(node.getNodeName()) ? "master" : node.getNodeName();
					listener.getLogger().println("run build statistics on " + normalizedName);
					try {
						statBuildDataOn(project, listener, node, normalizedName);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}
		}
	}

	@SuppressWarnings("rawtypes")
	private void statBuildDataOn(AbstractProject project, BuildListener listener, Node node, String nodeName) throws Exception{
//		if (null != node) {
//			node.getChannel().call(new MasterToSlaveCallable<Properties,Exception>(){
//			    public Properties call() throws Exception{
//			        return System.getProperties();
//			    }
//			});
//			if (project instanceof TopLevelItem) {
//				FilePath fp = node.getWorkspaceFor((TopLevelItem) project);
//			}
//		}
	}

	//@Extension
	public static final class DescriptorImpl extends BuildWrapperDescriptor {
		public DescriptorImpl() {
			super(BuildPostStat.class);
		}

		public String getDisplayName() {
			return "Update the lastest BuildStatData when the build ";
		}

		public boolean isApplicable(AbstractProject<?, ?> item) {
			return true;
		}

	}
}
