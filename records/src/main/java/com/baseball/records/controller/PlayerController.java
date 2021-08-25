package com.baseball.records.controller;

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

import com.baseball.records.common.Const;
import com.baseball.records.controller.rest._RestDefaultController;
import com.baseball.records.service.PlayerService;
import com.baseball.records.service.TeamService;
import com.baseball.records.vo.rest.PlayerListVo;
import com.baseball.records.vo.rest.PlayerVo;
import com.baseball.records.vo.rest.TeamVo;

@RestController
@RequestMapping(value = "/player")
public class PlayerController extends _RestDefaultController{

	private final Logger logger = LoggerFactory.getLogger(PlayerController.class);

	@Autowired
	PlayerService playerService;

	@Autowired
	TeamService teamService;

	/**
	 * 선수 리스트
	 * @param teamSeqNo	팀 시퀀스 번호
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(@RequestParam(name = "teamSeqNo", required = true) int teamSeqNo) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("player/list");

		try {
			TeamVo teamVo = teamService.getTeamInfo(teamSeqNo);

			PlayerVo vo = new PlayerVo();
			vo.setTeamSeqNo(teamSeqNo);
			vo.setPlayerPosition(1);
			List<PlayerListVo> pitcherList = playerService.getPlayerList(vo);

			vo.setPlayerPosition(2);
			List<PlayerListVo> hitterList = playerService.getPlayerList(vo);

			mav.addObject("teamInfo", teamVo);
			mav.addObject("pitcherList", pitcherList);
			mav.addObject("hitterList", hitterList);
		}catch (Exception e) {
			logger.info("error : {}", e);
		}

		return mav;
	}

	/**
	 * 선수 등록/상세 정보
	 * @param teamSeqNo	팀 시퀀스 번호
	 * @param seqNo	선수 시퀀스 번호
	 * @return
	 */
	@RequestMapping(value = "/info")
    public ModelAndView info(@RequestParam(name = "teamSeqNo", required = true) int teamSeqNo, @RequestParam(name = "seqNo", required = false, defaultValue = "0") int seqNo) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("player/info");

		try {
			PlayerVo vo = new PlayerVo();
			if(seqNo > 0) {
				vo = playerService.getPlayerInfo(seqNo);
			}

			mav.addObject("teamSeqNo", teamSeqNo);
			mav.addObject("info", vo);
		}catch (Exception e) {
			logger.info("error : {}", e);
		}

		return mav;
	}

	/**
	 * 선수 정보 등록/수정
	 * @param reqVo
	 * @return
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseEntity<?> save(@RequestBody PlayerVo reqVo) {

		try {
			int seqNo = reqVo.getSeqNo();
			int result = (seqNo > 0) ? playerService.updatePlayerInfo(reqVo) : playerService.insertPlayerInfo(reqVo);
		}catch (Exception e) {
			logger.info("error : {}", e);
			return error(Const.ERROR_CODE.PLAYER_INFO_SAVE_ERROR);
		}

        return succuss(reqVo.getTeamSeqNo());
    }

	/**
	 * 선수 정보 삭제
	 * @param seqNo	선수 시퀀스 번호
	 * @return
	 */
	@RequestMapping(value = "/delete/{seqNo}", method = RequestMethod.POST)
    public ResponseEntity<?> delete(@PathVariable(name = "seqNo", required = true) int seqNo) {

		try {
			int result = playerService.deletePlayerInfo(seqNo);
		}catch (Exception e) {
			logger.info("error : {}", e);
			return error(Const.ERROR_CODE.PLAYER_INFO_DELETE_ERROR);
		}

        return succuss();
    }

}
