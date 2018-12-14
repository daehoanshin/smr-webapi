package com.orange.rtc.api.common;


import org.eclipse.core.runtime.IProgressMonitor;

import com.orange.rtc.api.mgr.WorkflowMGR;
import com.orange.rtc.api.mgr.WorkitemMGR;
import com.ibm.team.workitem.client.IWorkItemClient;
import com.ibm.team.workitem.client.WorkItemWorkingCopy;
import com.ibm.team.workitem.common.model.IWorkItem;

public class CommonApiMgr {
	protected WorkitemMGR workitemMGR;
	protected WorkflowMGR workflowMGR;
	protected IWorkItemClient workitemClient;
	protected IProgressMonitor monitor;
	protected WorkItemWorkingCopy workingCopy;
	protected IWorkItem workItem;
}
