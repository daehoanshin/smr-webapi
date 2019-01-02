package com.orange.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/harweb/servlet")
public class ScmController {
	@RequestMapping(value = "custermg.ItsmToScmInterface", method = RequestMethod.POST)
	public String ItsmToScmInterface() {
		return "scm/interface";
	}

	@RequestMapping("/staticPage")
	public String getIndexPage() {
		return "interface.jsp";

	}
}
