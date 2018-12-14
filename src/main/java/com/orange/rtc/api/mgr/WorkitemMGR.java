package com.orange.rtc.api.mgr;


import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.springframework.stereotype.Component;

import com.ibm.team.links.common.IReference;
import com.ibm.team.process.common.IProjectArea;
import com.ibm.team.repository.client.IItemManager;
import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.workitem.client.IDetailedStatus;
import com.ibm.team.workitem.client.IWorkItemClient;
import com.ibm.team.workitem.client.WorkItemWorkingCopy;
import com.ibm.team.workitem.common.IAuditableCommon;
import com.ibm.team.workitem.common.model.IWorkItem;
import com.ibm.team.workitem.common.model.IWorkItemHandle;
import com.ibm.team.workitem.common.model.IWorkItemReferences;
import com.ibm.team.workitem.common.model.IWorkItemType;
import com.ibm.team.workitem.common.model.WorkItemEndPoints;

@Component
public class WorkitemMGR {
	
	/**
	 * 작업항목 저장
	 * 
	 * ~ 'workingCopy.setWorkflowAction(actionId)'후 저장 시  
	 * 'com.ibm.team.repository.client.util.ThreadCheck checkLongOpsAllowed' 메시지 출력됨. UI Thread 문제인것 같으나 해결방법이 없음. 상태는 잘 변경됨.
	 * @param workitemClient
	 * @param monitor
	 * @param workitemWorkingCopy
	 * @return
	 * @throws TeamRepositoryException
	 */
	public int saveWorkitem(IWorkItemClient workitemClient, IProgressMonitor monitor, WorkItemWorkingCopy workitemWorkingCopy) throws TeamRepositoryException{
		IWorkItem workitem = null;
		IDetailedStatus result = null;
		try{
			workitem = workitemWorkingCopy.getWorkItem();
			result = workitemWorkingCopy.save(monitor);
			
			if(result.isOK()){
				return workitem.getId();
			}else{
				throw new TeamRepositoryException("Error saving work item", result.getException());
			}
		}finally{
			workitemClient.getWorkItemWorkingCopyManager().disconnect(workitem);
		}
	}
	
	/**
	 * 작업항목 생성
	 * @param workitemClient
	 * @param monitor
	 * @param projectArea
	 * @param workitemTypeID
	 * @return
	 * @throws TeamRepositoryException
	 */
	public WorkItemWorkingCopy createWorkitem(IWorkItemClient workitemClient, IProgressMonitor monitor, IProjectArea projectArea, IWorkItemType workitemType) throws TeamRepositoryException{
		IWorkItemHandle wiHandle = workitemClient.getWorkItemWorkingCopyManager().connectNew(workitemType, monitor);
		return workitemClient.getWorkItemWorkingCopyManager().getWorkingCopy(wiHandle);
	}
	
	/**
	 * 작업항목 삭제
	 * @param workitemClient
	 * @param monitor
	 * @param workitemWorkingCopy
	 * @throws TeamRepositoryException
	 */
	public int deleteWorkitem(IWorkItemClient workitemClient, IProgressMonitor monitor, WorkItemWorkingCopy workitemWorkingCopy) throws TeamRepositoryException{
		IWorkItem workitem = null;
		IStatus result = null;
		try{
			workitem = workitemWorkingCopy.getWorkItem();
			result = workitemWorkingCopy.delete(monitor);
			
			if(result.isOK()){
				return workitem.getId();
			}else{
				throw new TeamRepositoryException("Error delete work item", result.getException());
			}
		}finally{
			workitemClient.getWorkItemWorkingCopyManager().disconnect(workitem);
		}
	}
	
	/**
	 * 워크아이템타입ID에 해당하는 워크아이템타입 레퍼런스를 리턴한다.
	 * @param workitemClient
	 * @param projectArea
	 * @param workitemTypeID
	 * @param monitor
	 * @return
	 * @throws TeamRepositoryException
	 */
	public IWorkItemType findWorkItemType(IWorkItemClient workitemClient, IProgressMonitor monitor, IProjectArea projectArea, String workitemTypeID)
			throws TeamRepositoryException{
		return workitemClient.findWorkItemType(projectArea, workitemTypeID, monitor);
	}
	
	/**
	 * 작업항목ID로 해당 작업항목을 조회하여 리턴한다.
	 * @param workitemClient
	 * @param monitor
	 * @param wiID
	 * @return
	 * @throws TeamRepositoryException
	 */
	public IWorkItem findWorkitemById(IWorkItemClient workitemClient, IProgressMonitor monitor, int wiID) throws TeamRepositoryException{
		if(wiID > 0){
			return workitemClient.findWorkItemById(wiID, IWorkItem.FULL_PROFILE, monitor);
		}
		return null;
	}
	
	/**
	 * 워크아이템 카피 리턴
	 * @param workitemClient
	 * @param monitor
	 * @param workitem
	 * @return
	 * @throws TeamRepositoryException 
	 */
	public WorkItemWorkingCopy getWorkingCopy(IWorkItemClient workitemClient, IProgressMonitor monitor, IWorkItem workitem) throws TeamRepositoryException{
		if(workitem != null){
			workitemClient.getWorkItemWorkingCopyManager().connect(workitem, IWorkItem.FULL_PROFILE, null);
			return workitemClient.getWorkItemWorkingCopyManager().getWorkingCopy((IWorkItemHandle) workitem.getItemHandle());
		}else{
			throw new IllegalArgumentException("Workitem is null");
		}
	}
	
	/**
	 * 첨부파일 모두 삭제
	 * @param auditableCommon
	 * @param itemManager
	 * @param monitor
	 * @param references
	 * @throws Exception
	 */
	public void removeAttachment(IAuditableCommon auditableCommon, IItemManager itemManager, IProgressMonitor monitor, IWorkItemReferences references) throws Exception{
		
		if(references != null){
			List<IReference> attachmentRefs = references.getReferences(WorkItemEndPoints.ATTACHMENT);
			for(IReference attachment : attachmentRefs){
				references.remove(attachment);
			}
		}
	}

}
