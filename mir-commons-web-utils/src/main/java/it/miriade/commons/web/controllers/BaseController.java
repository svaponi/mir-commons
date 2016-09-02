package it.miriade.commons.web.controllers;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.ModelMap;

import it.miriade.commons.utils.StringHandler;

/**
 * @author svaponi
 * @created Jun 24, 2015 12:38:35 PM
 */
public class BaseController {

	protected String redirect(String target) {
		return "redirect:" + target;
	}

	protected boolean hasQueryString(ServletRequest request) {
		if (request instanceof HttpServletRequest) {
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			String query = httpRequest.getQueryString();
			return StringHandler.hasText(query);
		}
		return false;
	}

	protected void injectParameters(ModelMap map, ServletRequest request) {

		for (String key : request.getParameterMap().keySet()) {
			map.addAttribute(key, request.getParameter(key));
		}

	}
}