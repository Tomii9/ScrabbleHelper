package tomii;

import java.util.List;
import java.util.Vector;

public class ScoreHandler {
	
	String user;
	DatabaseController db = new DatabaseController();
	
	public ScoreHandler (String user) {
		this.user = user;
	}
	
	public List<HighScoreDTO> getHighScores() {
		List<HighScoreDTO> top3Scores = new Vector<HighScoreDTO>();
		
		top3Scores = db.getHighScores();
		
		boolean userInTop3 = false;
		for (HighScoreDTO highScoreDTO : top3Scores) {
			if (highScoreDTO.getUser().equals(user)) {
				userInTop3 = true;
			}
		}
		
		if (userInTop3) {
			return top3Scores;
		}
		
		HighScoreDTO userScore = db.getScoreOfPlayer(user);
		top3Scores.add(userScore);
		
		return top3Scores;
	}

}
