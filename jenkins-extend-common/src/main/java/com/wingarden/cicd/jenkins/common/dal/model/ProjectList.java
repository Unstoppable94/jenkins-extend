package com.wingarden.cicd.jenkins.common.dal.model;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.wingarden.cicd.jenkins.common.utils.CollectionUtils;

import hudson.model.Job;
import hudson.model.TopLevelItem;
import hudson.model.View;
import jenkins.model.Jenkins;

@SuppressWarnings("rawtypes")
public class ProjectList<P extends Job> extends AbstractList<P> {
	private Iterable<P> base;
	private Integer size;
	private Class<P> cls;

	public ProjectList(Class<P> cls) {
		this.cls = cls;
		base = Collections.emptyList();
		size = 0;
	}

	public ProjectList(Class<P> cls,String... projectNames) {
		this.cls = cls;
		if (CollectionUtils.isNotEmpty(projectNames)) {
			Set<P> projects = new HashSet<>();
			for (String pName : projectNames) {
				TopLevelItem item = Jenkins.getInstance().getItem(pName);
				if (cls.isInstance(item)) {
					projects.add(cls.cast(item));
				}
			}
			base = projects;
			size = projects.size();
		} else {
			base = Collections.emptyList();
			size = 0;
		}
	}

	public ProjectList(Class<P> cls,View... views) {
		this.cls = cls;
		if (CollectionUtils.isNotEmpty(views)) {
			Set<P> projects = new HashSet<>();
			for (View view : views) {
				for (TopLevelItem item : view.getItems()) {
					if (cls.isInstance(item)) {
						projects.add(cls.cast(item));
					}
				}
			}
			base = projects;
			size = projects.size();
		} else {
			base = Collections.emptyList();
			size = 0;
		}
	}

	public ProjectList(Class<P> cls,boolean getAll) {
		this.cls = cls;
		if (getAll) {
			List<P> projects = Jenkins.getInstance().getItems(this.cls);// 只获取TopLevel的项目
			size = projects.size();
			base = projects;
		} else {
			base = Collections.emptyList();
			size = 0;
		}
	}

	@Override
	public P get(int index) {
		return Iterators.get(iterator(), index);
	}

	@Override
	public int size() {
		if (size == null) {
			int sz = 0;
			for (P p : this) {
				sz++;
			}
			size = sz;
		}
		return size;
	}

	@Override
	public Iterator<P> iterator() {
		return base.iterator();
	}

	@Override
	public List<P> subList(int fromIndex, int toIndex) {
		List<P> r = new ArrayList<P>();
		Iterator<P> itr = iterator();
		Iterators.skip(itr, fromIndex);
		for (int i = toIndex - fromIndex; i > 0; i--) {
			r.add(itr.next());
		}
		return r;
	}

	@Override
	public int indexOf(Object o) {
		int index = 0;
		for (P p : this) {
			if (p.equals(o))
				return index;
			index++;
		}
		return -1;
	}

	@Override
	public int lastIndexOf(Object o) {
		int a = -1;
		int index = 0;
		for (P p : this) {
			if (p.equals(o))
				a = index;
			index++;
		}
		return a;
	}

	@Override
	public boolean isEmpty() {
		return !iterator().hasNext();
	}

	public ProjectList<P> filter(Predicate<P> predicate) {
		size = null;
		base = Iterables.filter(base, predicate);
		return this;
	}

	public ProjectList<P> viewProject(String viewName) {
		return viewsProjects(viewName);
	}

	public ProjectList<P> viewsProjects(final String... viewNames) {
		if (CollectionUtils.isEmpty(viewNames)) {
			return this;
		}
		return filter(new Predicate<P>() {
			public boolean apply(P p) {
				for (String vName : viewNames) {
					View view = Jenkins.getInstance().getView(vName);
					if (null != view && p instanceof TopLevelItem && view.contains(TopLevelItem.class.cast(p))) {
						return true;
					}
				}
				return false;
			}
		});
	}

	public P nameProject(String projectName) {
		return nameProjects(projectName).get(0);
	}

	public ProjectList<P> nameProjects(final String... projectNames) {
		if (CollectionUtils.isEmpty(projectNames)) {
			return this;
		}
		return filter(new Predicate<P>() {
			public boolean apply(P p) {
				for (String pName : projectNames) {
					if (pName.equals(p.getName())) {
						return true;
					}
				}
				return false;
			}
		});
	}

	public ProjectList<P> nameProjects(List<String> prjList) {
		String[] projectNames = new String[] {};
		if (CollectionUtils.isNotEmpty(prjList)) {
			projectNames = prjList.toArray(projectNames);
		}
		return nameProjects(projectNames);
	}

	public ProjectList<P> statusProjects(final ProjectStatus... projectStatuses) {
		if (CollectionUtils.isEmpty(projectStatuses)) {
			return this;
		}
		return filter(new Predicate<P>() {
			public boolean apply(P p) {
				for (ProjectStatus projectStatus : projectStatuses) {
					ProjectStatus status = ProjectStatus.parseStatus(p.getIconColor());
					if (projectStatus == status) {
						return true;
					}
					//对于inquque状态的特殊处理
					if (projectStatus == ProjectStatus.INQUEUE) {
						if (p.isInQueue() && status != ProjectStatus.RUNNING) {
							return true;
						}
					}
				}
				return false;
			}
		});
	}

	public ProjectList<P> successOnly() {
		return statusProjects(ProjectStatus.SUCCESS);
	}

	public ProjectList<P> failedOnly() {
		return statusProjects(ProjectStatus.FAILED);
	}

	public ProjectList<P> inqueueOnly() {
		return statusProjects(ProjectStatus.INQUEUE);
	}

	public ProjectList<P> runningOnly() {
		return statusProjects(ProjectStatus.RUNNING);
	}

	public ProjectList<P> nobuitOnly() {
		return statusProjects(ProjectStatus.NOBUILT);
	}

	public ProjectList<P> disabledOnly() {
		return statusProjects(ProjectStatus.DISABLED);
	}

}
