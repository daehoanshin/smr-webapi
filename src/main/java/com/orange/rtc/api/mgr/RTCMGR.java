package com.orange.rtc.api.mgr;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.springframework.stereotype.Component;

import com.orange.rtc.api.common.SysoutProgressMonitor;
import com.ibm.team.links.client.ILinkManager;
import com.ibm.team.process.client.IProcessItemService;
import com.ibm.team.process.common.IProjectArea;
import com.ibm.team.repository.client.IContentManager;
import com.ibm.team.repository.client.IItemManager;
import com.ibm.team.repository.client.ITeamRepository;
import com.ibm.team.repository.client.TeamPlatform;
import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.workitem.client.IWorkItemClient;
import com.ibm.team.workitem.common.IAuditableCommon;
import com.ibm.team.workitem.common.IQueryCommon;
import com.ibm.team.workitem.common.model.IWorkItem;
import com.ibm.team.workitem.common.model.IWorkItemType;

@Component
public class RTCMGR {

	// RTC Repository Address
	private String REPOSITORY_ADDRESS;
	// RTC user
	private String USER = "";
	// RTC password
	private String PASSWORD = "";

	private ITeamRepository repo;
	private IProgressMonitor monitor;

	private IProcessItemService itemService;
	private IWorkItemClient workitemClient;
	private IItemManager itemManager = null;
	private ILinkManager linkManager = null;
	private IAuditableCommon auditableCommon = null;
	private IQueryCommon queryCommon = null;
	private IContentManager contentManager;

	private List<IProjectArea> projectAreas;

	public void setREPOSITORY_ADDRESS(String rEPOSITORY_ADDRESS) {
		REPOSITORY_ADDRESS = rEPOSITORY_ADDRESS;
	}

	public void setUSER(String uSER) {
		USER = uSER;
	}

	public void setPASSWORD(String pASSWORD) {
		PASSWORD = pASSWORD;
	}

	public IProgressMonitor getMonitor() {
		return monitor;
	}

	public ITeamRepository getRepo() {
		return repo;
	}

	public IProcessItemService getItemService() {
		return itemService;
	}

	public IWorkItemClient getWorkitemClient() {
		return workitemClient;
	}

	public IItemManager getItemManager() {
		return itemManager;
	}

	public ILinkManager getLinkManager() {
		return linkManager;
	}

	public IAuditableCommon getAuditableCommon() {
		return auditableCommon;
	}

	public IQueryCommon getQueryCommon() {
		return queryCommon;
	}

	public IContentManager getContentManager() {
		return contentManager;
	}

	public void startup() throws TeamRepositoryException {
		if (!TeamPlatform.isStarted()) {
			TeamPlatform.startup();

			monitor = new SysoutProgressMonitor();
			repo = login(monitor);

			initRTCService();
		}
	}

	public void shutdown() {
		if (TeamPlatform.isStarted()) {
			TeamPlatform.shutdown();
			System.out.println("shutdown!");
		}
	}

	/**
	 * 프로젝트영역명으로 RTC에서 해당 프로젝트영역 조회
	 * 
	 * @param repo
	 * @param monitor
	 * @param projectAreaName
	 * @return
	 * @throws TeamRepositoryException
	 */
	public IProjectArea findProjectArea(ITeamRepository repo, IProgressMonitor monitor, String projectAreaName) throws TeamRepositoryException {
		projectAreas = findProjectAreas(repo, monitor);
		if (projectAreas != null) {
			for (IProjectArea pja : projectAreas) {
				if (pja.getName().equals(projectAreaName)) {
					return pja;
				}
			}
		}
		return null;
	}

	/**
	 * RTC의 모든 프로젝트영역 조회
	 * 
	 * @param repo
	 * @param monitor
	 * @return
	 * @throws TeamRepositoryException
	 */
	@SuppressWarnings("unchecked")
	private List<IProjectArea> findProjectAreas(ITeamRepository repo, IProgressMonitor monitor) throws TeamRepositoryException {
		initRTCService();
		if (projectAreas == null) {
			return itemService.findAllProjectAreas(null, monitor);
		} else {
			return null;
		}
	}

	public IWorkItem createWorkItem(ITeamRepository repo, IProgressMonitor monitor, IProjectArea projectArea, String workitemTypeId)
			throws TeamRepositoryException {
		initRTCService();

		IWorkItemType workitemType = workitemClient.findWorkItemType(projectArea, workitemTypeId, monitor);
		System.out.println("workitemType DisplayName=" + workitemType.getDisplayName());

		return null;
	}

	/**
	 * 작업에 필요한 RTC Client 서비스 초기화
	 */
	private void initRTCService() {
		if (itemService == null)
			itemService = (IProcessItemService) repo.getClientLibrary(IProcessItemService.class);
		if (workitemClient == null)
			workitemClient = (IWorkItemClient) repo.getClientLibrary(IWorkItemClient.class);
		if (itemManager == null)
			itemManager = repo.itemManager();
		if (linkManager == null)
			linkManager = (ILinkManager) repo.getClientLibrary(ILinkManager.class);
		if (auditableCommon == null)
			auditableCommon = (IAuditableCommon) repo.getClientLibrary(IAuditableCommon.class);
		if (queryCommon == null)
			queryCommon = (IQueryCommon) repo.getClientLibrary(IQueryCommon.class);
		if (contentManager == null)
			contentManager = repo.contentManager();
	}

	public ITeamRepository login(IProgressMonitor monitor) throws TeamRepositoryException {
		ITeamRepository repository = TeamPlatform.getTeamRepositoryService().getTeamRepository(REPOSITORY_ADDRESS);
		repository.registerLoginHandler(new ITeamRepository.ILoginHandler() {
			public ILoginInfo challenge(ITeamRepository repository) {
				return new ILoginInfo() {
					public String getUserId() {
						return USER;
					}

					public String getPassword() {
						return PASSWORD;
					}
				};
			}
		});
		monitor.subTask("Contacting " + repository.getRepositoryURI() + "...");
		repository.login(monitor);
		monitor.subTask("Connected");
		return repository;
	}
}