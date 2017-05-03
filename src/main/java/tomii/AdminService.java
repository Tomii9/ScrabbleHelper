package tomii;

public class AdminService {
	
	DatabaseController db = new DatabaseController();

	public boolean deleteWord(String word) {
		db.deleteWord(word);
		return true;
	}
	
	public boolean addWord(String word, String user) {
		db.addWord(word, user);
		return true;
	}
	
	public boolean resetHighScoreOfPlayer(String user) {
		db.resetHighScore(user);
		return true;
	}
	public boolean banUser(String user) {
		db.banUser(user);
		return true;
	}
}
