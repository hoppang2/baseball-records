package com.baseball.records.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.baseball.records.controller.rest._RestDefaultController;
import com.baseball.records.service.TeamService;
import com.baseball.records.vo.rest.TeamVo;

@RestController
@RequestMapping(value = "/team")
public class TeamController extends _RestDefaultController{

	private final Logger logger = LoggerFactory.getLogger(TeamController.class);

	@Autowired
	TeamService teamService;

	/**
	 * 팀 리스트 조회
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("team/list");

		try {
			List<TeamVo> list = teamService.getTeamList();
			mav.addObject("list", list);
		}catch (Exception e) {
			logger.info("error : {}", e);
		}

        return mav;
    }

	/**
	 * 팀 리스트 조회
	 * @return
	 */
	@RequestMapping(value = "/selectList", method = RequestMethod.GET)
    public ResponseEntity<?> selectList() {
		List<TeamVo> teamList = new ArrayList<TeamVo>();

		try {
			teamList = teamService.getTeamList();
		}catch (Exception e) {
			logger.info("error : {}", e);
		}

        return succuss(teamList);
    }

	/**
	 * 해당 팀 정보 조회
	 * @param seqNo	팀 시퀀스 번호
	 * @return
	 */
	@RequestMapping(value = "/info", method = RequestMethod.GET)
    public ModelAndView info(@RequestParam(name = "seqNo", required = false, defaultValue = "0") int seqNo) {
		ModelAndView mav = new ModelAndView();
    	mav.setViewName("team/info");

    	try {
    		TeamVo info = new TeamVo();

    		if(seqNo > 0) {
    			info = teamService.getTeamInfo(seqNo);
    		}

    		mav.addObject("info", info);
		}catch (Exception e) {
			logger.info("error : {}", e);
		}

        return mav;
    }

	/**
	 * 팀 정보 등록/수정
	 * @param reqVo
	 * @return
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseEntity<?> save(@RequestBody TeamVo reqVo) {

    	try {
        	int seqNo = reqVo.getSeqNo();
        	int result = (seqNo > 0) ? teamService.updateTeamInfo(reqVo) : teamService.insertTeamInfo(reqVo);
		}catch (Exception e) {
			logger.info("error : {}", e);
		}

        return succuss();
    }

	/**
	 * 팀 정보 삭제
	 * @param seqNo	팀 시퀀스 번호
	 * @return
	 */
	@RequestMapping(value = "/delete/{seqNo}", method = RequestMethod.POST)
    public ResponseEntity<?> save(@PathVariable(value = "seqNo", required = true) int seqNo) {

		try {
			int result = teamService.deleteTeamInfo(seqNo);
		}catch (Exception e) {
			logger.info("error : {}", e);
		}

		return succuss();
	}

}
