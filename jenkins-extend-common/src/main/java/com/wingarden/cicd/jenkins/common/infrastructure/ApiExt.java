package com.wingarden.cicd.jenkins.common.infrastructure;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletException;

import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import hudson.model.Api;
import jenkins.model.Jenkins;

public class ApiExt extends Api {

	public ApiExt(Object bean) {
        super(bean);
    }
	
	/**
	 * Generate raw string.
	 * @param req 请求
	 * @param rsp 响应
	 * @throws IOException 文件操作异常
	 * @throws ServletException 异常
	 */
    public void doRaw(StaplerRequest req, StaplerResponse rsp) throws IOException, ServletException {
        setHeaders(rsp);
        rsp.setContentType("text/plain;charset=UTF-8");
        Writer wr = rsp.getWriter();
        wr.write(bean.toString());
        wr.close();
    }
    
    private void setHeaders(StaplerResponse rsp) {
        rsp.setHeader("X-Jenkins", Jenkins.VERSION);
        rsp.setHeader("X-Jenkins-Session", Jenkins.SESSION_HASH);
    }
}
