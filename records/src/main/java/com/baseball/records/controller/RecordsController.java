package com.baseball.records.controller;

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
@RequestMapping(value = "records")
public class RecordsController extends _RestDefaultController{

	private final Logger logger = LoggerFactory.getLogger(RecordsController.class);

	@Autowired
	TeamService teamService;

	/**
	 * 팀별 기록 리스트
	 * @param teamSeqNo	팀 시퀀스 번호
	 * @return
	 */
	@RequestMapping(value = "list")
	public ModelAndView list(@RequestParam(value = "teamSeqNo", required = true) int teamSeqNo) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("records/list");

		try {
			TeamVo teamInfo = teamService.getTeamInfo(teamSeqNo);

			mav.addObject("teamInfo", teamInfo);
		}catch (Exception e) {
			logger.info("error : {}", e);

		}

		return mav;
	}
}
