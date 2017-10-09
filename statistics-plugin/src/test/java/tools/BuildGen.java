package tools;

import java.util.HashMap;
import java.util.Map;

import org.jvnet.hudson.test.JenkinsRule;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.wingarden.cicd.jenkins.common.dal.model.BuildStatus;

import hudson.model.FreeStyleBuild;
import hudson.model.FreeStyleProject;
import hudson.model.Label;
import hudson.model.labels.LabelAtom;
import hudson.tasks.Builder;
import hudson.tasks.Shell;

public class BuildGen {

	private JenkinsRule j;

	public BuildGen(JenkinsRule j) {
		this.j = j;
	}

	public void triggerBuild(FreeStyleProject prj, int buildCount, BuildStatus status) throws Exception {
		switch (status) {
		case RUNNING:
			triggerRunningBuild(prj, buildCount);
			break;
		case ABORTED:
			triggerAbortedBuild(prj, buildCount);
			break;
		case FAILED:
			triggerFailedBuild(prj, buildCount);
			break;
		case SUCCESS:
			triggerSuccessBuild(prj, buildCount);
			break;
		case PENDING:
			triggerPendingBuild(prj, buildCount);
			break;
		default:
			break;
		}
	}

	public void triggerFailedBuild(FreeStyleProject prj, int buildCount) throws Exception {
		String tUrl = new StringBuffer(j.jenkins.getRootUrl()).append("job/").append(prj.getName())
				.append("/build?delay=0sec").toString();
		Map<String, String> headers = new HashMap<>();
		if (j.jenkins.isUseCrumbs()) {
			headers.put(j.jenkins.getCrumbIssuer().getCrumbRequestField(), j.jenkins.getCrumbIssuer().getCrumb());
		}

		prj.setConcurrentBuild(true);
		Builder builder = new Shell("sleep 1 && errorshell");
		prj.getBuildersList().add(builder);

		for (int i = 0; i < buildCount; i++) {
			Unirest.post(tUrl).headers(headers).asString();
		}
		// if (prj.isLogUpdated()) {
		// prj.getBuildersList().remove(builder);
		// }
	}

	public void triggerRunningBuild(FreeStyleProject prj, int buildCount) throws Exception {
		String tUrl = new StringBuffer(j.jenkins.getRootUrl()).append("job/").append(prj.getName())
				.append("/build?delay=0sec").toString();
		Map<String, String> headers = new HashMap<>();
		if (j.jenkins.isUseCrumbs()) {
			headers.put(j.jenkins.getCrumbIssuer().getCrumbRequestField(), j.jenkins.getCrumbIssuer().getCrumb());
		}

		prj.setConcurrentBuild(true);
		Builder builder = new Shell("sleep 6000");
		prj.getBuildersList().add(builder);

		for (int i = 0; i < buildCount; i++) {
			Unirest.post(tUrl).headers(headers).asString();
		}

		// if (prj.isLogUpdated()) {
		// prj.getBuildersList().remove(builder);
		// }
	}

	public void triggerAbortedBuild(FreeStyleProject prj, int buildCount) throws Exception {
		String rUrl = new StringBuffer(j.jenkins.getRootUrl()).append("job/").append(prj.getName()).append("/")
				.toString();
		String tUrl = new StringBuffer(rUrl).append("build?delay=0sec").toString();
		Map<String, String> headers = new HashMap<>();
		if (j.jenkins.isUseCrumbs()) {
			headers.put(j.jenkins.getCrumbIssuer().getCrumbRequestField(), j.jenkins.getCrumbIssuer().getCrumb());
		}

		prj.setConcurrentBuild(true);
		Builder builder = new Shell("sleep 300");
		prj.getBuildersList().add(builder);

		for (int i = 0; i < buildCount; i++) {
			Unirest.post(tUrl).headers(headers).asString();
		}
		Thread.currentThread().sleep(5000);
		for (FreeStyleBuild b : prj.getBuilds()) {
			String aUrl = new StringBuffer(rUrl).append(b.number).append("/stop").toString();
			Unirest.post(aUrl).headers(headers).asString();
		}

		// if (prj.isLogUpdated()) {
		// prj.getBuildersList().remove(builder);
		// }
	}

	public void triggerSuccessBuild(FreeStyleProject prj, int buildCount) throws Exception {
		String tUrl = new StringBuffer(j.jenkins.getRootUrl()).append("job/").append(prj.getName())
				.append("/build?delay=0sec").toString();
		Map<String, String> headers = new HashMap<>();
		if (j.jenkins.isUseCrumbs()) {
			headers.put(j.jenkins.getCrumbIssuer().getCrumbRequestField(), j.jenkins.getCrumbIssuer().getCrumb());
		}

		prj.setConcurrentBuild(true);

		for (int i = 0; i < buildCount; i++) {
			Unirest.post(tUrl).headers(headers).asString();
		}
	}

	public void triggerPendingBuild(FreeStyleProject prj, int buildCount) throws Exception {
		String tUrl = new StringBuffer(j.jenkins.getRootUrl()).append("job/").append(prj.getName())
				.append("/build?delay=0sec").toString();
		Map<String, String> headers = new HashMap<>();
		if (j.jenkins.isUseCrumbs()) {
			headers.put(j.jenkins.getCrumbIssuer().getCrumbRequestField(), j.jenkins.getCrumbIssuer().getCrumb());
		}

		prj.setConcurrentBuild(false);
		prj.setAssignedLabel(new LabelAtom("unexist_node"));
//		 Builder builder = new Shell("sleep 3000");
//		 prj.getBuildersList().add(builder);

		for (int i = 0; i < buildCount; i++) {
			Unirest.post(tUrl).headers(headers).asString();
		}
	}
}
