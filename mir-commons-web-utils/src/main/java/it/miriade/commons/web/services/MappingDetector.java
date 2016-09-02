package it.miriade.commons.web.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * Servizio per ricavare tutte le risorse web mappate da Spring, ovvero tutti i metodi dei controllers. Il risultato è
 * diviso per controller dunque usa una MultiMap (mappa con key ripetute, implementata come una mappa di array).
 * 
 * @author svaponi
 * @created Mar 16, 2015 12:31:58 PM
 */
@Service("MappingDetector")
public class MappingDetector {

	private static boolean REMOVE_CTRL_SUFFIX = false;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired(required = false)
	// @Qualifier("HandlerMapping")
	private RequestMappingHandlerMapping handlerMapping;

	private Map<String, List<String>> mappings;
	private Map<String, List<String>> methods;

	public RequestMappingHandlerMapping getHandlerMapping() {
		return handlerMapping;
	}

	public void setHandlerMapping(RequestMappingHandlerMapping handlerMapping) {
		this.handlerMapping = handlerMapping;
	}

	public Map<String, List<String>> getMappings() {
		if (mappings == null)
			mappings = new HashMap<>();
		return mappings;
	}

	private Map<String, List<String>> addMapping(String bean, String method) {
		if (getMappings().containsKey(bean)) {
			getMappings().get(bean).add(method);
		} else {
			List<String> list = new ArrayList<String>();
			list.add(method);
			getMappings().put(bean, list);
		}
		return getMappings();
	}

	public Map<String, List<String>> getMethods() {
		if (methods == null)
			methods = new HashMap<>();
		return methods;
	}

	private Map<String, List<String>> addMethod(String bean, String method) {
		if (getMethods().containsKey(bean)) {
			getMethods().get(bean).add(method);
		} else {
			List<String> list = new ArrayList<String>();
			list.add(method);
			getMethods().put(bean, list);
		}
		return getMethods();
	}

	public Map<String, List<String>> detect() {

		if (handlerMapping == null) {

			logger.warn("Request mapping not detectable, MappingDetector service could not initialize 'HandlerMapping'");
			return Collections.emptyMap();

		}

		if (getMappings().isEmpty()) {

			Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();

			for (Entry<RequestMappingInfo, HandlerMethod> item : handlerMethods.entrySet()) {
				RequestMappingInfo reqMappingInfo = item.getKey();
				HandlerMethod handMethod = item.getValue();

				RequestMethod[] httpMethods = handMethod.getMethod().getAnnotation(RequestMapping.class).method();
				// Se il metodo risponde solo ad OPTIONS --> skip..
				if (httpMethods.length == 1 && httpMethods[0].equals(RequestMethod.OPTIONS))
					continue;

				String bean = handMethod.getBeanType().getSimpleName();
				// logger.trace("Controller bean: " + bean + " " + Arrays.deepToString(httpMethods));

				if (REMOVE_CTRL_SUFFIX && bean.endsWith("Controller"))
					bean = bean.substring(0, bean.length() - "Controller".length());

				for (String urlPattern : reqMappingInfo.getPatternsCondition().getPatterns())
					addMapping(bean, urlPattern);

				for (RequestMethod reqMethod : reqMappingInfo.getMethodsCondition().getMethods())
					addMethod(bean, reqMethod.name());
			}

			// evito di ciclare la collection se la priorità del log inibirebbe la stampa
			if (logger.isTraceEnabled())
				for (Object bean : mappings.keySet())
					logger.trace("Controller bean " + bean.toString() + " values: " + mappings.get(bean));

		}

		return getMappings();
	}
}
