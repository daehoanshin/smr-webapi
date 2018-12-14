package com.abl.rtc.api.mgr;

import org.eclipse.core.runtime.IProgressMonitor;
import org.springframework.stereotype.Component;

import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.workitem.client.IWorkItemClient;
import com.ibm.team.workitem.client.WorkItemWorkingCopy;
import com.ibm.team.workitem.common.model.IState;
import com.ibm.team.workitem.common.model.IWorkItem;
import com.ibm.team.workitem.common.model.Identifier;
import com.ibm.team.workitem.common.workflow.IWorkflowAction;
import com.ibm.team.workitem.common.workflow.IWorkflowInfo;

@Component
public class WorkflowMGR {

	/**
	 * 작업항목워킹카피에 실행 대상 액션의 ID를 설정한다.
	 * 
	 * @param workingCopy
	 * @param actionId
	 */
	public void setWorkflowAction(WorkItemWorkingCopy workingCopy, String actionId) {
		workingCopy.setWorkflowAction(actionId);
	}

	/**
	 * 액션ID로 액션명을 조회한다.
	 * 
	 * @param workitemClient
	 * @param monitor
	 * @param workitem
	 * @param actionId
	 * @return
	 * @throws TeamRepositoryException
	 */
	public String findActionNameById(IWorkItemClient workitemClient, IProgressMonitor monitor, IWorkItem workitem, String actionId)
			throws TeamRepositoryException {
		IWorkflowInfo workflowInfo = findWorkflowInfo(workitemClient, monitor, workitem);
		if (workflowInfo != null) {
			Identifier<IWorkflowAction>[] allActionIds = workflowInfo.getAllActionIds();
			for (Identifier<IWorkflowAction> acId : allActionIds) {
				if (acId.getStringIdentifier().equals(actionId)) {
					return workflowInfo.getActionName(acId);
				}
			}
		}
		return null;
	}

	/**
	 * 액션명으로 액션ID를 조회한다.
	 * 
	 * @param workitemClient
	 * @param monitor
	 * @param workitem
	 * @param actionName
	 * @return
	 * @throws TeamRepositoryException
	 */
	public String findActionIdByName(IWorkItemClient workitemClient, IProgressMonitor monitor, IWorkItem workitem, String actionName)
			throws TeamRepositoryException {
		IWorkflowInfo workflowInfo = findWorkflowInfo(workitemClient, monitor, workitem);
		if (workflowInfo != null) {
			Identifier<IWorkflowAction>[] allActionIds = workflowInfo.getAllActionIds();
			for (Identifier<IWorkflowAction> acId : allActionIds) {
				if (workflowInfo.getActionName(acId).equals(actionName)) {
					return acId.getStringIdentifier();
				}
			}
		}
		return null;
	}

	/**
	 * 상태ID로 상태명을 조회한다.
	 * 
	 * @param workitemClient
	 * @param monitor
	 * @param workitem
	 * @param stateId
	 * @return
	 * @throws TeamRepositoryException
	 */
	public String findStateNameById(IWorkItemClient workitemClient, IProgressMonitor monitor, IWorkItem workitem, String stateId)
			throws TeamRepositoryException {
		IWorkflowInfo workflowInfo = findWorkflowInfo(workitemClient, monitor, workitem);
		if (workflowInfo != null) {
			Identifier<IState>[] stateIds = workflowInfo.getStateIds(IWorkflowInfo.ALL_STATES);
			for (Identifier<IState> state : stateIds) {
				if (state.getStringIdentifier().equals(stateId)) {
					return workflowInfo.getStateName(state);
				}
			}
			// return workflowInfo.getStateName(workitem.getState2());
		}
		return null;
	}

	/**
	 * 상태명으로 상태ID를 조회한다.
	 * 
	 * @param workitemClient
	 * @param monitor
	 * @param workitem
	 * @param stateName
	 * @return
	 * @throws TeamRepositoryException
	 */
	public String findStateIdByName(IWorkItemClient workitemClient, IProgressMonitor monitor, IWorkItem workitem, String stateName)
			throws TeamRepositoryException {
		IWorkflowInfo workflowInfo = findWorkflowInfo(workitemClient, monitor, workitem);
		if (workflowInfo != null) {
			Identifier<IState>[] stateIds = workflowInfo.getStateIds(IWorkflowInfo.ALL_STATES);
			for (Identifier<IState> state : stateIds) {
				if (workflowInfo.getStateName(state).equals(stateName)) {
					return state.getStringIdentifier();
				}
			}
		}
		return null;
	}

	/**
	 * 작업항목의 워크플로우 정보 레퍼런스를 조회한다.
	 * 
	 * @param workitemClient
	 * @param monitor
	 * @param workitem
	 * @return
	 * @throws TeamRepositoryException
	 */
	public IWorkflowInfo findWorkflowInfo(IWorkItemClient workitemClient, IProgressMonitor monitor, IWorkItem workitem)
			throws TeamRepositoryException {
		if (workitem != null) {
			return workitemClient.findWorkflowInfo(workitem, monitor);
		}
		return null;
	}
}
