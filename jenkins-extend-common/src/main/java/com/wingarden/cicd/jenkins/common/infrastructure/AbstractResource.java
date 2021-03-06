package com.wingarden.cicd.jenkins.common.infrastructure;

import java.util.Arrays;

import org.acegisecurity.AccessDeniedException;
import org.kohsuke.stapler.export.Exported;
import org.kohsuke.stapler.export.ExportedBean;

import com.wingarden.cicd.jenkins.common.config.GlobalConfig;
import com.wingarden.cicd.jenkins.common.utils.LogUtils;
import com.wingarden.cicd.jenkins.common.utils.StringToolkit;
import com.wingarden.cicd.jenkins.common.utils.XmlUtil;

import hudson.model.Api;
import hudson.security.ACL;
import hudson.security.Permission;
import jenkins.model.Jenkins;

@ExportedBean(defaultVisibility = 99999)
public abstract class AbstractResource implements Resource {
	private static final long serialVersionUID = -8183217865217330650L;
	private String id;
	private String name;
	private String displayName;

	public AbstractResource() {
		XmlUtil.XSTREAM.alias(this.getClass().getSimpleName(), this.getClass());
	}

	@Override
	public ACL getACL() {
		return Jenkins.getInstance().getACL();
	}

	@Override
	public void checkPermission(Permission permission) throws AccessDeniedException {
		getACL().checkPermission(permission);
	}

	@Override
	public boolean hasPermission(Permission permission) {
		return getACL().hasPermission(permission);
	}

	@Override
	public final Api getApi() {
		return new ApiExt(this);
	}

	@Override
	@Exported
	public String getUrl() {
		return this.getClass().getName().replaceAll("\\.", "_");
	}

	public String getParentPath() {
		return new StringBuilder().append(getRootPath()).append(getUrl()).append("/").toString();
	}

	@Override
	public String getResourcePath() {
		return new StringBuilder().append(getParentPath()).append(getXmlFileName()).append(".xml").toString();
	}

	public String getRootPath() {
		return GlobalConfig.FS_DATA_ROOT_DIR;
	}

	public String getXmlFileName() {
		return this.getClass().getSimpleName() + "@" + getId();
	}

	public String getXmlFileNamePattern(String... ids) {
		StringBuilder pattern = new StringBuilder().append("^").append(this.getClass().getSimpleName());
		if (null == ids || ids.length < 1) {
			pattern.append("\\@.*\\.xml$");
		} else {
			pattern.append("\\@(").append(StringToolkit.join(Arrays.asList(ids), "|", "")).append(")")
			.append("\\.xml$");
		}
		LogUtils.debug(String.format("[%s#getXmlFileNamePattern]---pattern is:%s",this.getClass().getName(), pattern.toString()));
		return pattern.toString();
	}

	@Exported
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Exported
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	@Exported
	public String getDisplayName() {
		return displayName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((displayName == null) ? 0 : displayName.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractResource other = (AbstractResource) obj;
		if (displayName == null) {
			if (other.displayName != null)
				return false;
		} else if (!displayName.equals(other.displayName))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	
}