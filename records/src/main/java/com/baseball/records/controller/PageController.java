package com.baseball.records.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.baseball.records.controller.rest._RestDefaultController;
import com.baseball.records.service.TeamService;
import com.baseball.records.vo.rest.TeamVo;

@RestController
public class PageController extends _RestDefaultController {

	Logger logger = LogManager.getLogger(PageController.class);

	@Autowired
	TeamService teamService;

	@RequestMapping(value = "/main/index")
    public ModelAndView index() {
		ModelAndView mav = new ModelAndView();
    	mav.setViewName("main/index");

    	sql.selectList("records.selectList");

    	List<TeamVo> teamList = teamService.getTeamList();

    	mav.addObject("teamList", teamList);
        return mav;
    }

	@RequestMapping(value = "/team/player")
    public ModelAndView player() {
		ModelAndView mav = new ModelAndView();
    	mav.setViewName("team/player");
		return mav;
	}
}
