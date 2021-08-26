package com.baseball.records.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baseball.records.vo.rest.TeamVo;

@Service
public class TeamService extends _DefaultService{

	public List<TeamVo> getTeamList() {
		return sql.selectList("team.getTeamList");
	}

	public TeamVo getTeamInfo(int seqNo) {
		return sql.selectOne("team.getTeamInfo", seqNo);
	}

	public int insertTeamInfo(TeamVo vo) {
		return sql.insert("team.insertTeamInfo", vo);
	}

	public int updateTeamInfo(TeamVo vo) {
		return sql.update("team.updateTeamInfo", vo);
	}

	public int deleteTeamInfo(int seqNo) {
		return sql.delete("team.deleteTeamInfo", seqNo);
	}

}
