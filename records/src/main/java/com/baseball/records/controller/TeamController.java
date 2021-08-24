package com.baseball.records.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.baseball.records.controller.rest._RestDefaultController;
import com.baseball.records.service.TeamService;
import com.baseball.records.vo.rest.TeamVo;

@RestController
public class TeamController extends _RestDefaultController{
	Logger logger = LogManager.getLogger(TeamController.class);

	@Autowired
	TeamService teamService;

	@RequestMapping(value = "/team/list", method = RequestMethod.GET)
    public ResponseEntity<?> list() {
		List<TeamVo> teamList = teamService.getTeamList();
        return succuss(teamList);
    }

	@RequestMapping(value = "/team/{seqNo}", method = RequestMethod.GET)
    public ModelAndView info(@PathVariable(name = "seqNo") int seqNo) {
		ModelAndView mav = new ModelAndView();
    	mav.setViewName("team/list");

    	TeamVo teamInfo = teamService.getTeamInfo(seqNo);
    	List<List<String>> resultList = null;

    	mav.addObject("teamInfo", teamInfo);
    	mav.addObject("list", resultList);
        return mav;
    }
}
