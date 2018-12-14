package com.orange.web;

import javax.servlet.http.HttpSession;


public class HttpSessionUtils {
	public static final String USER_SESSION_KEY = "sessionedUser";
	public static boolean isLoginUser(HttpSession session) {
		Object sessionedUser = session.getAttribute(USER_SESSION_KEY);
		if(sessionedUser == null) {
			return false;
		}
		return true;
	}
	public static String getUserFormSession(HttpSession session) {
		if(!isLoginUser(session)) {
			return null;
		}
		return (String) session.getAttribute(USER_SESSION_KEY);
	}
}
