package com.baseball.records.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.baseball.records.controller.rest._RestDefaultController;
import com.baseball.records.service.TeamService;
import com.baseball.records.vo.rest.TeamVo;

@RestController
public class PageController extends _RestDefaultController {

	Logger logger = LoggerFactory.getLogger(PageController.class);

	@Autowired
	TeamService teamService;

	@RequestMapping(value = "/main/index")
    public ModelAndView index() {
		ModelAndView mav = new ModelAndView();

		try {

		}catch (Exception e) {
			logger.info("error : {}", e);
		}

    	mav.setViewName("main/index");
        return mav;
    }

}
