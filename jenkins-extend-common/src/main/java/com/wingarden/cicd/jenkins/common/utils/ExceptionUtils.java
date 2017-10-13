package com.wingarden.cicd.jenkins.common.utils;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class ExceptionUtils {
	static class ThrowableCreatedElsewhere extends Throwable {
		private static final long serialVersionUID = 1L;

		public ThrowableCreatedElsewhere(Throwable throwable) {
			super(throwable.getClass() + " created elsewhere");
			this.setStackTrace(throwable.getStackTrace());
		}

		@Override
		public Throwable fillInStackTrace() {
			return this;
		}
	}

	public static <T extends Throwable & SensibleClone<T>> T fixStackTrace(T throwable) {
		throwable = throwable.sensibleClone();

		if (throwable.getCause() == null) {
			try {
				throwable.initCause(new ThrowableCreatedElsewhere(throwable));
			} catch (IllegalStateException e) {
			}
		}

		throwable.fillInStackTrace();
		StackTraceElement[] existing = throwable.getStackTrace();
		StackTraceElement[] newTrace = new StackTraceElement[existing.length - 1];
		System.arraycopy(existing, 1, newTrace, 0, newTrace.length);
		throwable.setStackTrace(newTrace);
		return throwable;
	}

	public static String makeStackTrace(Throwable throwable) {
		String text = "";
		PrintStream printStream = null;
		try {
			ByteArrayOutputStream baOutStream = new ByteArrayOutputStream();
			printStream = new PrintStream(baOutStream, false, StandardCharsets.UTF_8.toString());
			throwable.printStackTrace(printStream);
			printStream.flush();
			text = baOutStream.toString(StandardCharsets.UTF_8.toString());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} finally {
			if (null != printStream) {
				printStream.close();
			}
		}

		return text;
	}
}
