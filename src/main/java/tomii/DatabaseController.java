package tomii;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DatabaseController {
	
	ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
	WordJDBCTemplate wordJDBCTemplate = (WordJDBCTemplate)context.getBean("WordJDBCTemplate");

	public List<WordDTO> getWords() {
		return wordJDBCTemplate.refreshCache();
	}
	
	public List<HighScoreDTO> getHighScores() {
		return wordJDBCTemplate.getTop3Scores();
	}
	
	public HighScoreDTO getScoreOfPlayer(String user) {
		return wordJDBCTemplate.getScoreOfPlayer(user);
	}
	
	public boolean updateHighScore(String user, int score) {
		return wordJDBCTemplate.updateHighScore(user, score);
	}
	
	public CredentialsDTO getUser(String user) {
		return wordJDBCTemplate.getUser(user);
	}
	
	public boolean register(String user, String password) {
		wordJDBCTemplate.register(user, password);
		return true;
	}
	
	public boolean addWord(String word, String user) {
		wordJDBCTemplate.addWord(word, user);
		return true;
	}
	
	public boolean deleteWord(String word) {
		wordJDBCTemplate.deleteWord(word);
		return true;
	}
	
	public boolean banUser(String user) {
		wordJDBCTemplate.banUser(user);
		return true;
	}
	
	public boolean resetHighScore(String user) {
		wordJDBCTemplate.resetHighScore(user);
		return true;
	}
}
