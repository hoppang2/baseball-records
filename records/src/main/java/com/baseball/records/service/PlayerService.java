package com.baseball.records.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baseball.records.vo.rest.PlayerListVo;
import com.baseball.records.vo.rest.PlayerVo;

@Service
public class PlayerService extends _DefaultService{

	public List<PlayerListVo> getPlayerList(PlayerVo vo) {
		return sql.selectList("player.getPlayerList", vo);
	}

	public PlayerVo getPlayerInfo(int seqNo) {
		return sql.selectOne("player.getPlayerInfo", seqNo);
	}

	public int insertPlayerInfo(PlayerVo vo) {
		return sql.insert("player.insertPlayerInfo", vo);
	}

	public int updatePlayerInfo(PlayerVo vo) {
		return sql.update("player.updatePlayerInfo", vo);
	}

	public int deletePlayerInfo(int seqNo) {
		return sql.delete("player.deletePlayerInfo", seqNo);
	}



}
