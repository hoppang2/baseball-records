package com.baseball.records.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController{

	private String VIEW_PATH = "/error/";

	/**
	 * 에러페이지
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/error")
	public String handleError(HttpServletRequest request) {
		Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

		if(status != null) {
			int statusCode = Integer.valueOf(status.toString());

			if(statusCode == HttpStatus.BAD_REQUEST.value()) {
				return VIEW_PATH + "400";
			}

			if(statusCode == HttpStatus.NOT_FOUND.value()) {
				return VIEW_PATH + "404";
			}

			if(statusCode == HttpStatus.FORBIDDEN.value()) {
				return VIEW_PATH + "500";
			}
		}

		return VIEW_PATH + "error";
	}

}
