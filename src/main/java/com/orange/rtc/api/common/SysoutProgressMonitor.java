package com.orange.rtc.api.common;

import org.eclipse.core.runtime.IProgressMonitor;
import org.springframework.stereotype.Component;

/*******************************************************************************
 * Licensed Materials - Property of IBM (c) Copyright IBM Corporation 2006,
 * 2012. All Rights Reserved.
 * 
 * Note to U.S. Government Users Restricted Rights: Use, duplication or
 * disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 *******************************************************************************/

@Component
public class SysoutProgressMonitor implements IProgressMonitor {

	public void beginTask(String name, int totalWork) {
		print(name);
	}

	public void done() {
	}

	public void internalWorked(double work) {
	}

	public boolean isCanceled() {
		return false;
	}

	public void setCanceled(boolean value) {
	}

	public void setTaskName(String name) {
		print(name);
	}

	public void subTask(String name) {
		print(name);
	}

	public void worked(int work) {
	}

	private void print(String name) {
		if (name != null && !"".equals(name))
			System.out.println(name);
	}
}
