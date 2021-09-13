package org.aery.study.java.io.tool;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GeneralLogger {

	public static boolean ShowThread = false;

	public static boolean ShowPosition = false;

	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

	public static String getTitle() {
		return getTitle(null);
	}

	public static String getTitle(String id) {
		Thread currentThread = Thread.currentThread();
		StackTraceElement ste = currentThread.getStackTrace()[2];

		String title = "[" + getDateTime() + "]";

		if (ShowThread) {
			title += "[" + currentThread.getName() + "]";
		}

		if (ShowPosition) {
			String className = ste.getClassName();
			className = className.substring(className.lastIndexOf(".") + 1);
			String position = className + "." + ste.getMethodName() + (ste.isNativeMethod() ? "(Native Method)"
					: (ste.getFileName() != null && ste.getLineNumber() >= 0
							? "(" + ste.getFileName() + ":" + ste.getLineNumber() + ")"
							: (ste.getFileName() != null ? "(" + ste.getFileName() + ")" : "(Unknown Source)")));

			title += "[" + position + "]";
		}

		return title + " " + (id == null ? "" : id + " ");
	}

	public static String getDateTime() {
		LocalDateTime ldt = LocalDateTime.now();
		return ldt.format(FORMATTER);
	}

	public static String error(Throwable t) {
		return error(t, null);
	}

	public static String error(Throwable t, String msg) {
		StringWriter errors = new StringWriter();
		t.printStackTrace(new PrintWriter(errors));

		if (msg == null) {
			return errors.toString();
		} else {
			return msg + "\r\n\t" + errors.toString();
		}
	}

	public static void connection_is_broken() {
		String title = getTitle(null);
		System.out.println(title + "connection is broken");
	}

	public static void connection_is_broken(String id) {
		String title = getTitle(id);
		System.out.println(title + "connection is broken");
	}

}
