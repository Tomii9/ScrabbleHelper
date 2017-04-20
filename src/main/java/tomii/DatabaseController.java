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
}
