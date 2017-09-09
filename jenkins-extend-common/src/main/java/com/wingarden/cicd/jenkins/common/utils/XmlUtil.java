package com.wingarden.cicd.jenkins.common.utils;

import java.io.File;
import java.io.IOException;

import com.thoughtworks.xstream.XStream;
import com.wingarden.cicd.jenkins.common.infrastructure.Resource;

import hudson.XmlFile;
import hudson.model.Job;
import hudson.util.XStream2;

public final class XmlUtil {

	public static Object load(File file) throws IOException {
		XmlFile xmlFile = new XmlFile(XSTREAM, file);
		return xmlFile.read();
	}

	public static Object load(String filePath) throws IOException {
		return load(new File(filePath));
	}

	public static void write(String filePath, Object bean) throws IOException {
		write(new File(filePath), bean);
	}

	public static void write(File file, Object bean) throws IOException {
		XmlFile xmlFile = new XmlFile(XSTREAM, file);
		xmlFile.write(bean);
	}

	public static void insertResource(Resource rs) throws IOException {
		File file = new File(rs.getResourcePath());
		if (!file.exists()) {
			write(file, rs);
		} else {
			LogUtils.error("[XmlUtil#insertResource ERROR]file[{}] exists,can't be inserted!", file.getPath());
		}
	}
	
	public static void updateResource(Resource rs) throws IOException {
		File file = new File(rs.getResourcePath());
		if (file.exists()) {
			write(file, rs);
		} else {
			LogUtils.error("[XmlUtil#updateResource ERROR]file[{}] not exists,can't be updated!", file.getPath());
		}
	}

	/**
	 * Used to load/save job configuration.
	 *
	 * When you extend {@link Job} in a plugin, try to put the alias so that it
	 * produces a reasonable XML.
	 */
	public static final XStream XSTREAM = new XStream2();

	/**
	 * Alias to {@link #XSTREAM} so that one can access additional methods on
	 * {@link XStream2} more easily.
	 */
	public static final XStream2 XSTREAM2 = (XStream2) XSTREAM;
}
