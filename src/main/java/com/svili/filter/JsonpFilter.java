package com.svili.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * jsonp跨域请求处理
 * 
 * @author lishiwei
 * @data 2017年4月21日
 *
 */
public class JsonpFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpServletRequest;
		if (request instanceof HttpServletRequest) {
			httpServletRequest = (HttpServletRequest) request;
			String requestURL = httpServletRequest.getRequestURL() + "?" + httpServletRequest.getQueryString();
			// 过滤jsonp的回调函数callback
			String uri = filterUrlCallbcak(requestURL, request.getParameter("callback"), request.getParameter("_"));
		}
		HttpServletResponse httpServletResponse;
		if (response instanceof HttpServletResponse) {
			httpServletResponse = (HttpServletResponse) response;
			// 设置跨域
			httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
			chain.doFilter(request, httpServletResponse);
		}
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {

	}

	private String filterUrlCallbcak(String url, String param, String _param) {
		String result = url.replace("&callback=" + param, "");
		result = result.replace("&_=" + _param, "");
		return result;
	}

}
