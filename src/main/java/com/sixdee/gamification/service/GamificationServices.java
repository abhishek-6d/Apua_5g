/**
 * 
 */
package com.sixdee.gamification.service;

import java.util.ArrayList;

import com.sixdee.gamification.service.dto.req.GetMyGamesReqDTO;
import com.sixdee.gamification.service.dto.req.GetMyGamingGiftsReqDTO;
import com.sixdee.gamification.service.dto.req.GetMyGamingHistoryReqDTO;
import com.sixdee.gamification.service.dto.req.GetMyInfoReqDTO;
import com.sixdee.gamification.service.dto.req.PlayMyGameReqDTO;
import com.sixdee.gamification.service.dto.req.UpdateAccountReqDTO;
import com.sixdee.gamification.service.dto.resp.AchievementInfoDTO;
import com.sixdee.gamification.service.dto.resp.GameInformation;
import com.sixdee.gamification.service.dto.resp.Games;
import com.sixdee.gamification.service.dto.resp.GetMyGamesRespDTO;
import com.sixdee.gamification.service.dto.resp.GetMyGamingGiftRespDTO;
import com.sixdee.gamification.service.dto.resp.GetMyGamingHistoryRespDTO;
import com.sixdee.gamification.service.dto.resp.GetMyInfoRespDTO;
import com.sixdee.gamification.service.dto.resp.MileStoneInformationDTO;
import com.sixdee.gamification.service.dto.resp.MileStones;
import com.sixdee.gamification.service.dto.resp.OptionsDTO;
import com.sixdee.gamification.service.dto.resp.PlayMyGameRespDTO;
import com.sixdee.gamification.service.dto.resp.QuizDTO;
import com.sixdee.gamification.service.dto.resp.QuizMaster;
import com.sixdee.gamification.service.dto.resp.UpdateAccountRespDTO;
import com.sixdee.gamification.service.impl.GamificationServiceImpl;

/**
 * @author rahul.kr
 *
 */
public class GamificationServices implements GamificationServiceImpl{

	@Override
	public GetMyGamesRespDTO getMyGames(GetMyGamesReqDTO getMyGamesReqDTO) {
		GetMyGamesRespDTO getMyGamesRespDTO = null;
		try {
			getMyGamesRespDTO = new GetMyGamesRespDTO();
			getMyGamesRespDTO.setTxnId(getMyGamesReqDTO.getTxnId());
			getMyGamesRespDTO.setTimeStamp(getMyGamesReqDTO.getTimeStamp());
			getMyGamesRespDTO.setMsisdn(getMyGamesReqDTO.getMsisdn());
			Games games  = new Games();
			getMyGamesRespDTO.setGames(games);
			GameInformation[] gameList = new GameInformation[2];
			games.setGamesInformation(gameList);
			GameInformation game = new GameInformation();
			game.setGameId("GM01");
			game.setGameDesc("This is a tutorial game where u need to answer the questions");
			game.setGameName("Whatsapp quiz");
			game.setGameStartDate("2017-09-01 00:00:00");
			game.setGameEndDate("2017-12-31 23:59:59");
			game.setGameType("1");
			game.setRewardType("1");
			//game.set
			MileStoneInformationDTO mileStoneInformation = new MileStoneInformationDTO();
			mileStoneInformation.setMileStoneId("GM01MS01");
			mileStoneInformation.setMileStoneDesc("Give 5 correct answers with in 7 questions and win gold badge");
			mileStoneInformation.setMileStoneName("Novice");
			AchievementInfoDTO achievementDTO = new AchievementInfoDTO();
			mileStoneInformation.setAchievementDTO(achievementDTO);
			
			achievementDTO.setAchievemntType(1);
			achievementDTO.setAchievementRank(1);
			achievementDTO.setAchievementImagePath("http://ServerLMS/ImageRepo/GM01MS01.jpg");
			mileStoneInformation.setAchievementDTO(achievementDTO);
			MileStones mileStones = new MileStones();
			MileStoneInformationDTO[] mileStoneInformationDTOs = new MileStoneInformationDTO[1];
			mileStoneInformationDTOs[0]=mileStoneInformation;
			mileStones.setMileStoneInformation(mileStoneInformationDTOs);
			game.setMileStones(mileStones);
			
			GameInformation game1 = new GameInformation();
			game1.setGameId("GM02");
			game1.setGameDesc("This is a tutorial game where u need to answer the questions");
			game1.setGameName("FB quiz");
			game1.setGameStartDate("2017-09-01 00:00:00");
			game1.setGameEndDate("2017-12-31 23:59:59");
			game1.setGameType("1");
			game1.setRewardType("1");
			//game.set
			MileStoneInformationDTO mileStoneInformation1 = new MileStoneInformationDTO();
			mileStoneInformation1.setMileStoneId("GM02MS01");
			mileStoneInformation1.setMileStoneDesc("Give 5 correct answers with in 7 questions and win gold badge");
			mileStoneInformation1.setMileStoneName("Novice");
			AchievementInfoDTO achievementDTO1 = new AchievementInfoDTO();
			mileStoneInformation1.setAchievementDTO(achievementDTO1);
			
			achievementDTO1.setAchievemntType(1);
			achievementDTO1.setAchievementRank(1);
			achievementDTO1.setAchievementImagePath("http://ServerLMS/ImageRepo/GM01MS01.jpg");
			//mileStoneInformation.setAchievementDTO(achievementDTO1);
			MileStones mileStones1 = new MileStones();
			MileStoneInformationDTO[] mileStoneInformationDTOs1 = new MileStoneInformationDTO[1];
			mileStoneInformationDTOs1[0]=(mileStoneInformation1);
			mileStones1.setMileStoneInformation(mileStoneInformationDTOs1);
			QuizMaster quizMaster = new QuizMaster();
			quizMaster.setListAllQuestions(false);
			QuizDTO[] quizList = new QuizDTO[1];
			QuizDTO quizDTO = new QuizDTO();
			quizDTO.setQuestion("Captian of cricket team");
			quizDTO.setId(1);
			OptionsDTO[] optionsList = new OptionsDTO[2];
			OptionsDTO optionsDTO = new OptionsDTO();
			optionsDTO.setOptId(1);
			optionsDTO.setOptions("XXX");
			optionsList[0]=(optionsDTO);
			optionsDTO = new OptionsDTO();
			optionsDTO.setOptId(2);
			optionsDTO.setOptions("YYY");
			optionsList[1]=(optionsDTO);
			optionsDTO = new OptionsDTO();
			optionsDTO.setOptId(3);
			optionsDTO.setOptions("ZZZ");
			optionsList[2]=(optionsDTO);
			quizList[0]=quizDTO;
			quizDTO.setOptionList(optionsList);
			quizMaster.setQuizList(quizList);
			gameList[0]=(game1);
			gameList[1]=game;
			games.setGamesInformation(gameList);
			getMyGamesRespDTO.setGames(games);
		}catch (Exception e) {
			// TODO: handle exception
		}
		return getMyGamesRespDTO;
	}

	@Override
	public GetMyInfoRespDTO getMyInformation(GetMyInfoReqDTO getMyInfoReqDTO) {
		GetMyInfoRespDTO getMyInfoResp = new GetMyInfoRespDTO();
		getMyInfoResp.setTxnId(getMyInfoReqDTO.getTxnId());
		getMyInfoResp.setMsisdn("95261893");
		getMyInfoResp.setNationalId("MT-NT23313");
		getMyInfoResp.setNationalIdType("Aadhar");
		GameInformation gameInformation = new GameInformation();
		gameInformation.setGameId("GM01");
		gameInformation.setGameName("Whatsapp quiz");
		gameInformation.setGameDesc("This is a tutorial game where u need to answer the questions");
		gameInformation.setGameStartDate("2017-09-01 00:00:00");
		gameInformation.setGameEndDate("2017-12-31 23:59:59");
		gameInformation.setGameType("1");
		gameInformation.setRewardType("1");
		
		gameInformation.setMileStoneAchieved("Novice");
		gameInformation.setNextMileStone("Amateur");
		gameInformation.setValuesEarned("1000");
		gameInformation.setValueToNextMileStone("1000");
		
		//getMyInfoResp.setGameInformation(gameInformation);
		return getMyInfoResp;
	}

	@Override
	public GetMyGamingHistoryRespDTO getMyGamingHistory(GetMyGamingHistoryReqDTO getMyGamingHistoryReqDTO) {
		GetMyGamingHistoryRespDTO getMyGamingHistoryRespDTO = new GetMyGamingHistoryRespDTO();
		getMyGamingHistoryRespDTO.setTxnId(getMyGamingHistoryReqDTO.getTxnId());
		getMyGamingHistoryRespDTO.setMsisdn(getMyGamingHistoryReqDTO.getMsisdn());
//		getM
		return getMyGamingHistoryRespDTO;
	}

	@Override
	public GetMyGamingGiftRespDTO getMyGamingGift(GetMyGamingGiftsReqDTO getMyGamingGiftRespDTO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UpdateAccountRespDTO updateAccount(UpdateAccountReqDTO updateAccountRequestDTO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PlayMyGameRespDTO playMyGame(PlayMyGameReqDTO playGameReqDTO) {
		// TODO Auto-generated method stub
		return null;
	}


	


}
