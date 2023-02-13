/**
 * 
 */
package com.sixdee.gamification.service.impl;

import com.sixdee.gamification.service.dto.req.GetMyGamesReqDTO;
import com.sixdee.gamification.service.dto.req.GetMyGamingGiftsReqDTO;
import com.sixdee.gamification.service.dto.req.GetMyGamingHistoryReqDTO;
import com.sixdee.gamification.service.dto.req.GetMyInfoReqDTO;
import com.sixdee.gamification.service.dto.req.PlayMyGameReqDTO;
import com.sixdee.gamification.service.dto.req.UpdateAccountReqDTO;
import com.sixdee.gamification.service.dto.resp.GetMyGamesRespDTO;
import com.sixdee.gamification.service.dto.resp.GetMyGamingGiftRespDTO;
import com.sixdee.gamification.service.dto.resp.GetMyGamingHistoryRespDTO;
import com.sixdee.gamification.service.dto.resp.GetMyInfoRespDTO;
import com.sixdee.gamification.service.dto.resp.PlayMyGameRespDTO;
import com.sixdee.gamification.service.dto.resp.UpdateAccountRespDTO;

/**
 * @author rahul.kr
 *
 */
public interface GamificationServiceImpl {

	public GetMyGamesRespDTO getMyGames(GetMyGamesReqDTO getMyGamesReqDTO);
	
	public GetMyInfoRespDTO getMyInformation(GetMyInfoReqDTO getMyInfoReqDTO);
	
	public GetMyGamingHistoryRespDTO getMyGamingHistory(GetMyGamingHistoryReqDTO getMyGamingHistoryReqDTO);
	
	public GetMyGamingGiftRespDTO getMyGamingGift(GetMyGamingGiftsReqDTO getMyGamingGiftRespDTO);
	
	public UpdateAccountRespDTO updateAccount(UpdateAccountReqDTO updateAccountRequestDTO); 
	
	public PlayMyGameRespDTO playMyGame(PlayMyGameReqDTO playGameReqDTO);
	
}
