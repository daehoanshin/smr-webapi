package com.abl.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.eclipse.core.runtime.IProgressMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.abl.domain.Authenticate;
import com.abl.rtc.api.client.ApprovalStatusAction;
import com.abl.rtc.api.common.IAttributeIDs;
import com.abl.rtc.api.mgr.RTCMGR;
import com.abl.rtc.api.mgr.WorkitemMGR;
import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.workitem.client.IWorkItemClient;


/**
 * @author dhshin
 * 
 */
@Controller
public class WorkItemStateController {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	@Resource
	private RTCMGR rtcMGR;
	@Resource
	private WorkitemMGR workitemMGR;

	@Resource
	private ApprovalStatusAction statusAction;
	
	@Value("#{users['rtc.admin.user']}")
	private String adminUserId;
	
	@Value("#{users['rtc.admin.password']}")
	private String adminUserPassword;


	@Deprecated
	@RequestMapping(value = "/approval/state/{wiid}", method = RequestMethod.POST)
	public String approvalState(Model model, HttpSession session, @PathVariable int wiid) throws TeamRepositoryException {
		logger.info("approval");
		//Authenticate authenticate = (Authenticate) session.getAttribute("authenticate");
		try {
			String actionId = (String) session.getAttribute("actionId");
			String oldStateId = (String) session.getAttribute("oldStateId");
			String userId = (String) session.getAttribute(HttpSessionUtils.USER_SESSION_KEY);
			
			
			logger.info("wiid = " + wiid + " actionId = " + actionId + " oldStateId = " + oldStateId);
			/*if (!password.equals(HttpSessionUtils.getUserFormSession(session))) {
				return "/login/form";
			}*/
			//boolean result = statusAction.findWorkItem(wiid, actionId , oldStateId);
			boolean result = true;
			model.addAttribute("result", result);
			model.addAttribute("wiid", wiid);
			model.addAttribute("userId", userId);
			
			session.removeAttribute(HttpSessionUtils.USER_SESSION_KEY);
			session.removeAttribute("actionId");
			session.removeAttribute("oldStateId");
		} finally {
			rtcMGR.shutdown();
		}
		return "users/approval";
	}
	
	@RequestMapping(value = "/approval", method = RequestMethod.POST)
	public String approval(Model model, String wiid, String actionId, String oldStateId) throws TeamRepositoryException {
		logger.info("approval");
		
		try {
			
			logger.info("wiid = " + wiid + " actionId = " + actionId + " oldStateId = " + oldStateId);
			int wiId = Integer.parseInt(wiid);
			
			rtcApiLogin(adminUserId, adminUserPassword);;
			boolean result = statusAction.findWorkItem(wiId, actionId , oldStateId);
			model.addAttribute("result", result);
			model.addAttribute("wiid", wiid);
			model.addAttribute("userId", "radmin");
			
			
		} finally {
			rtcMGR.shutdown();
		}
		return "users/result";
	}


	@RequestMapping("/loginForm")
	public String loginForm(Model model, String userId, int wiid, String actionId, String oldStateId) {
		Authenticate authenticate = new Authenticate();
		logger.info("adminUserId {} , adminUserPassword {} ", adminUserId, adminUserPassword);
		authenticate.setUserId(userId);
		authenticate.setWiid(wiid);
		authenticate.setActionId(actionId);
		authenticate.setOldStateId(oldStateId);
		model.addAttribute("authenticate", authenticate);
		return "users/login";
	}

	@RequestMapping("/login")
	public String login(@Valid Authenticate authenticate, BindingResult bindingResult, HttpSession session,
			Model model) {

		if (bindingResult.hasErrors()) {
			return "users/login";
		}
		//rtc 로그인처리
		if (!rtcApiLogin(authenticate.getUserId(), authenticate.getPassword())) {
			model.addAttribute("errorMessage", "비밀번호가 틀립니다.");
			return "users/login";
		}
		session.setAttribute(HttpSessionUtils.USER_SESSION_KEY, authenticate.getUserId());
		session.setAttribute("actionId", authenticate.getActionId());
		session.setAttribute("oldStateId", authenticate.getOldStateId());

		return "users/approval";
	}

	public boolean rtcApiLogin(String userId, String password) {
		logger.info("userId : {} password : {} ", userId, password);
		rtcMGR.setREPOSITORY_ADDRESS(IAttributeIDs.REPOSITORY_ADDRESS);
		// userid
		rtcMGR.setUSER(userId);
		// password
		rtcMGR.setPASSWORD(password);
		// RTC Client start
		try {
			rtcMGR.startup();
		} catch (TeamRepositoryException e) {
			logger.error(e.getMessage());
			rtcMGR.shutdown();
			return false;
		}

		IWorkItemClient workitemClient = rtcMGR.getWorkitemClient();
		IProgressMonitor monitor = (IProgressMonitor) rtcMGR.getMonitor();
		logger.info("workitemClient : {} " + workitemClient);
		if (workitemClient == null) {
			rtcMGR.shutdown();
			return false;
		}

		if (workitemClient != null && monitor != null) {
			statusAction.setWorkitemClient(workitemClient);
			statusAction.setMonitor(monitor);
		}
		return true;
	}

	@RequestMapping("/logout")
	public String loginForm(HttpSession session) {
		session.removeAttribute("userId");
		return "redirect:/";
	}
}
