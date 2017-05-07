package tomii;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DatabaseController {
	
	ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
	WordJDBCTemplate jdbcTemplate = (WordJDBCTemplate)context.getBean("WordJDBCTemplate");

	public List<WordDTO> getWords() {
		try {
			return jdbcTemplate.refreshCache();
		} catch (Exception E) {
			return null;
		}
	}
	
	public List<HighScoreDTO> getHighScores() {
		try {
			return jdbcTemplate.getTop3Scores();
		} catch (Exception E) {
			return null;
		}
		
	}
	
	public HighScoreDTO getScoreOfPlayer(String user) {
		try {
			return jdbcTemplate.getScoreOfPlayer(user);
		} catch (Exception E) {
			return null;
		}
	}
	
	public boolean updateHighScore(String user, int score) {
		try {
			return jdbcTemplate.updateHighScore(user, score);
		} catch (Exception E) {
			return false;
		}
	}
	
	public CredentialsDTO getUser(String user) {
		try {
			return jdbcTemplate.getUser(user);
		} catch (Exception e) {
			return null;
		}
	}
	
	public boolean register(String user, String password) {
		try {
			jdbcTemplate.register(user, password);
		} catch (Exception E) {
			return false;
		}
		return true;
	}
	
	public boolean addWord(String word, String user) {
		try {
			jdbcTemplate.addWord(word, user);
		} catch (Exception E) {
			return false;
		}
		return true;
	}
	
	public boolean deleteWord(String word) {
		try {
			jdbcTemplate.deleteWord(word);
		} catch (Exception E) {
			return false;
		}
		return true;
	}
	
	public boolean banUser(String user) {
		try {
			jdbcTemplate.banUser(user);
		} catch (Exception E) {
			return false;
		}
		return true;
	}
	
	public boolean resetHighScore(String user) {
		try {
			jdbcTemplate.resetHighScore(user);
		} catch (Exception E) {
			return false;
		}
		return true;
	}
}
