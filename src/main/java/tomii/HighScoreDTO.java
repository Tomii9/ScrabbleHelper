package tomii;

/*
 * This Class is to be sent as JSON to the client. DO NOT include any logic
 */
import java.sql.Date;

public class HighScoreDTO {
	private int highscore;
	private String user;
	private Date date;
	
	public HighScoreDTO() {
		
	}
	
	public HighScoreDTO(int highscore, String user, Date date) {
		this.highscore = highscore;
		this.user = user;
		this.date = date;
	}
	
	public int getHighscore() {
		return highscore;
	}
	
	public void setHighscore(int highscore) {
		this.highscore = highscore;
	}
	
	public String getUser() {
		return user;
	}
	
	public void setUser(String user) {
		this.user = user;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	
}
