package tools;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jvnet.hudson.test.JenkinsRule;

import hudson.model.BallColor;
import hudson.model.Cause;
import hudson.model.FreeStyleBuild;
import hudson.model.FreeStyleProject;
import hudson.model.ListView;
import hudson.model.View;
import hudson.model.queue.QueueTaskFuture;
import hudson.tasks.Shell;

public class ProjectGen {
	private JenkinsRule j;
	private Map<String, FreeStyleProject> map = new HashMap<>();

	public ProjectGen(JenkinsRule j) {
		this.j = j;
	}

	public void genProjects(String namePrefix, int count,String viewName) throws Exception{
		if( null == j.jenkins.getView(viewName)) {
			j.jenkins.addView(new ListView(viewName));
		}
		View v = j.jenkins.getView(viewName);
		for (int i = 0; i < count; i++) {
			try {
				FreeStyleProject p = j.createFreeStyleProject(namePrefix + "_" + i);
				ListView.class.cast(v).add(p);
				map.put(p.getName(), p);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public FreeStyleProject getProject(String pName) {
		return map.get(pName);
	}
	
	private void test() throws Exception{
		FreeStyleProject project1 = j.createFreeStyleProject("testproject");
		project1.setConcurrentBuild(true);
		project1.getBuildersList().add(new Shell("sleep 5"));
		QueueTaskFuture<FreeStyleBuild> f=project1.scheduleBuild2(1,new Cause.UserIdCause());
		while (true) {
			FreeStyleBuild build = f.getStartCondition().get();
			System.out.println(build.getIconColor());
			if (build.getIconColor().equals(BallColor.BLUE)) {
				break;
			}
		}
		f = project1.scheduleBuild2(1,new Cause.UserIdCause());
		while (true) {
			FreeStyleBuild build = f.getStartCondition().get();
			System.out.println(build.getIconColor());
			if (build.getIconColor().equals(BallColor.BLUE)) {
//				f.cancel(true);
				break;
			}
		}
		project1.scheduleBuild2(2);
		project1.scheduleBuild2(3);

		FreeStyleProject project2 = j.createFreeStyleProject("testproject1");
		project2.getBuildersList().add(new Shell("gg 5"));
		project2.scheduleBuild(new Cause.UserIdCause());

		FreeStyleProject project3 = j.createFreeStyleProject("testproject2");
		project3.scheduleBuild(new Cause.UserIdCause());

	}
	
	public static void initProject4Test(JenkinsRule j) throws Exception{
		j.jenkins.setNumExecutors(50);
		ProjectGen pg = new ProjectGen(j);
		pg.genProjects("view1_project", 10, "view1");
		pg.genProjects("view2_project", 10, "view2");
		pg.genProjects("view3_project", 10, "view3");
		
		BuildGen bg = new BuildGen(j);
		FreeStyleProject prj = pg.getProject("view1_project_1");
		bg.triggerSuccessBuild(prj, 1);
		
		prj = pg.getProject("view2_project_2");
		bg.triggerAbortedBuild(prj, 1);
		
		prj = pg.getProject("view3_project_3");
		bg.triggerFailedBuild(prj, 1);
		
		prj = pg.getProject("view1_project_2");
		bg.triggerPendingBuild(prj, 1);
		
		prj = pg.getProject("view2_project_1");
		bg.triggerRunningBuild(prj, 1);
		
		prj = pg.getProject("view3_project_1");
		prj.disable();
	}
}
