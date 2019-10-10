package com.ag.poc.integration.scrapping.service.dealhub;

import java.net.HttpCookie;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.ag.poc.constant.DealSignConstants;

@Component
public class MapperImpl implements Mapper {

	@Override
	public String mapResponseAndGetAuthToken(HttpHeaders httpHeaders) {
		List<String> cookieList = httpHeaders.get(HttpHeaders.SET_COOKIE);
		StringBuffer authToken = new StringBuffer();
		if (!CollectionUtils.isEmpty(cookieList)) {
			cookieList.forEach(cookie -> {
				HttpCookie.parse(cookie).forEach(item -> {
					authToken.append(item.toString()).append(DealSignConstants.SEMICOLON);
				});
			});
			return authToken.toString();
		}
		return DealSignConstants.EMPTY_STRING;
	}

	@Override
	public String getDocGuid(String response) {
		return JsonUtil.getStringNodeValue(JsonUtil.retrieveNodeValueByPath(JsonUtil.toJson(response), DealSignConstants.DEAL_DOCUMENT),
				DealSignConstants.GUID);
	}

}
