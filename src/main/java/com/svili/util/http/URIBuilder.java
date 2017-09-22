package com.svili.util.http;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

/***
 * URI构建器
 * 
 * @author svili
 * @data 2017年8月22日
 *
 */
public class URIBuilder {

	/**
	 * 
	 * @param url
	 *            url or uri</br>
	 *            e.g. https://www.baidu.com or
	 *            https://www.baidu.com?param_1=value...
	 * @param urlParams
	 *            if name or value isEmpty,it will be not append to URI.</br>
	 * @return URI
	 */
	public static String build(String url, Map<String, String> urlParams) {
		String uri = url;
		if (urlParams != null && !urlParams.isEmpty()) {
			StringBuilder urlParam = new StringBuilder();
			for (Entry<String, String> entry : urlParams.entrySet()) {
				String name = entry.getKey();
				String value = entry.getValue();
				if (!StringUtils.isEmpty(name) && !StringUtils.isEmpty(value)) {
					urlParam.append("&");
					urlParam.append(name).append("=");
					// there have no use for encode
					urlParam.append(value);
				}
			}

			if (url.contains("?")) {
				uri = url + urlParam.toString();
			}

			if (!url.contains("?")) {
				uri = url + urlParam.replace(0, 1, "?").toString();
			}

		}
		return uri;
	}

}
