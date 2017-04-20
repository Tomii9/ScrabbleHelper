package tomii;

import java.sql.Date;

public class HighScoreDTO {
	private int highscore;
	private String user;
	private Date date;
	
	public HighScoreDTO() {
		
	}
	
	public HighScoreDTO(int highscore, String user) {
		this.highscore = highscore;
		this.user = user;
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
