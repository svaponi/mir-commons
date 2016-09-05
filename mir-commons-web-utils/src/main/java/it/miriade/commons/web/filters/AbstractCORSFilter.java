package it.miriade.commons.web.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author svaponi
 * @created Mar 16, 2015 12:04:41 PM 2015-02-20.12:04
 */
public abstract class AbstractCORSFilter implements Filter, CORS {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	public abstract void addHeaders(HttpServletResponse response);

	public final void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

		boolean isHttpResponse = response instanceof HttpServletResponse;
		HttpServletResponse httpResponse = null;

		if (isHttpResponse) {
			httpResponse = ((HttpServletResponse) response);
			addHeaders(httpResponse);
		}

		filterChain.doFilter(request, response);
	}

	public void init(FilterConfig filterConfig) {
		logger.trace("Filter " + filterConfig.getFilterName() + " initiated with " + filterConfig);
	}

	public void destroy() {
		logger.trace("Filter destroyed");
	}

}