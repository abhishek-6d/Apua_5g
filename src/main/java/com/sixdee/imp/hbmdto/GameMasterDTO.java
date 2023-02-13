package com.sixdee.imp.hbmdto;

/**
 * 
 * @author @jith
 * @version 1.0
 * 
 * <p>
 * <b><u>Development History</u></b><br>
 * <table border="1" width="100%">
 * <tr>
 * <td width="15%"><b>Date</b></td>
 * <td width="20%"><b>Author</b></td>
 * </tr>
 * <tr>
 * <td>March 08,2018 04:23:10 PM</td>
 * <td>@jith</td>
 * </tr>
 * </table>
 * </p>
 */



import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;


public class GameMasterDTO  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int gameID ;
	private String gameType;
	private String oFLevels;
	private String gameStartDate;
	private String gameEndDate;
	private String gameMode;
	private Set<GameLanguageMapperDTO> gameLanguageMapper;
	
	
	
	public int getGameID() {
		return gameID;
	}
	public void setGameID(int gameID) {
		this.gameID = gameID;
	}
	public String getGameType() {
		return gameType;
	}
	public void setGameType(String gameType) {
		this.gameType = gameType;
	}
	public String getoFLevels() {
		return oFLevels;
	}
	public void setoFLevels(String oFLevels) {
		this.oFLevels = oFLevels;
	}
	public String getGameStartDate() {
		return gameStartDate;
	}
	public void setGameStartDate(String gameStartDate) {
		this.gameStartDate = gameStartDate;
	}
	public String getGameEndDate() {
		return gameEndDate;
	}
	public void setGameEndDate(String gameEndDate) {
		this.gameEndDate = gameEndDate;
	}
	public String getGameMode() {
		return gameMode;
	}
	public void setGameMode(String gameMode) {
		this.gameMode = gameMode;
	}
	public Set<GameLanguageMapperDTO> getGameLanguageMapper() {
		return gameLanguageMapper;
	}
	public void setGameLanguageMapper(Set<GameLanguageMapperDTO> gameLanguageMapper) {
		this.gameLanguageMapper = gameLanguageMapper;
	}

	
	
	
}
