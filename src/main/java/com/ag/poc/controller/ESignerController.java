package com.ag.poc.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import com.ag.poc.constant.DealSignConstants;
import com.ag.poc.service.ESignerService;
import com.ag.poc.service.dto.GetDocumentResponse;

@Controller
public class ESignerController {

	@Autowired
	private ESignerService sESignerService;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Map<String, Object> model) {
		model.put("message", "Reader !!");
		model.put("signatures", this.sESignerService.findAll());
		return DealSignConstants.INDEX_PAGE;
	}

	@RequestMapping(value = "/pdf", method = RequestMethod.GET)
	public RedirectView processPdf(Map<String, Object> model, @RequestParam("dealGuid") String dealGuid) {
		model.put("message", "You are in new page ");
		String redirectUrl = this.sESignerService.scrapAndEsign(dealGuid);
		RedirectView redirect = new RedirectView(redirectUrl);
		redirect.setExposeModelAttributes(false);
		return redirect;
	}

	@RequestMapping(value = "/download", method = RequestMethod.GET)
	public HttpEntity<byte[]> download(@RequestParam("id") Long id) {
		GetDocumentResponse response = this.sESignerService.downloadDoc(id);
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_PDF);
		header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + response.getFileName().replace(" ", "_"));
		header.setContentLength(response.getData().length);
		return new HttpEntity<byte[]>(response.getData(), header);
	}

}
