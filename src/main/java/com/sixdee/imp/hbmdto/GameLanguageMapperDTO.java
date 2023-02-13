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


public class GameLanguageMapperDTO  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int Id ;
	private String languageID;
	private String gameName;
	private String gameDesc;
	private String gameImage;
	private String gameID;
	private GameMasterDTO gameMaster;
	
	
	
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public String getLanguageID() {
		return languageID;
	}
	public void setLanguageID(String languageID) {
		this.languageID = languageID;
	}
	public String getGameName() {
		return gameName;
	}
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	public String getGameDesc() {
		return gameDesc;
	}
	public void setGameDesc(String gameDesc) {
		this.gameDesc = gameDesc;
	}
	public String getGameImage() {
		return gameImage;
	}
	public void setGameImage(String gameImage) {
		this.gameImage = gameImage;
	}
	public String getGameID() {
		return gameID;
	}
	public void setGameID(String gameID) {
		this.gameID = gameID;
	}
	public GameMasterDTO getGameMaster() {
		return gameMaster;
	}
	public void setGameMaster(GameMasterDTO gameMaster) {
		this.gameMaster = gameMaster;
	}
	
	
	
	
	
}
