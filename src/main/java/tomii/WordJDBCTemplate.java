package tomii;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

public class WordJDBCTemplate{

	private JdbcTemplate jdbcTemplateObject;
	
	public WordJDBCTemplate() {
	}
	
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplateObject = new JdbcTemplate(dataSource);
	}

	public void create(String word) {
		String SQL = "insert into WORDS (word, uploader) values (?, ?)";
		jdbcTemplateObject.update(SQL, word, "admin");
		System.out.println("Created new word " + word);
	}

	public void delete(String word) {
		String SQL = "Delete from WORDS where word = ?";
		jdbcTemplateObject.update(SQL, word);
		System.out.println("Deleted Word " + word );
		
	}
	
	public List <WordDTO> refreshCache() {
		String SQL = "select word, name from words w, users u where w.uploader = u.id";
		return jdbcTemplateObject.query(SQL, new WordMapper());
	}
	
	public List<HighScoreDTO> getTop3Scores() {
		String SQL = "select * from (select h.score, u.name, h.date_of_ach from high_scores h, users u where h.player=u.id order by score desc) where rownum <4";
		return jdbcTemplateObject.query(SQL, new Top3ScoreMapper());
	}
	
	public HighScoreDTO getScoreOfPlayer (String user) {
		String SQL = "select h.score, u.name, h.date_of_ach from high_scores h, users u where h.player=u.id and u.name = ?";
		return jdbcTemplateObject.queryForObject(SQL, new Object[]{user}, new Top3ScoreMapper());
	}

}
