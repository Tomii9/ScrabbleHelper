package tomii;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

public class WordJDBCTemplate{

	private JdbcTemplate jdbcTemplateObject;
	private DataSource datasource;
	public WordJDBCTemplate() {
	}
	
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplateObject = new JdbcTemplate(dataSource);
		this.datasource = dataSource;
	}
	
	public DataSource getDataSource() {
		return datasource;
	}
	
	public void register(String user, String password) {
		String SQL = "insert into USERS values ((select max(id) from users) +1, ?, 'normal', ?)";
		jdbcTemplateObject.update(SQL, user, password);
		SQL = "insert into high_scores values (0, (select max(id) from users), systimestamp";
		jdbcTemplateObject.update(SQL);
	}

	public void addWord(String word, String user) {
		String SQL = "insert into WORDS (word, uploader) values (?, (select id from users where name = ?))";
		jdbcTemplateObject.update(SQL, word, user);
		System.out.println("Created new word " + word);
	}

	public void deleteWord(String word) {
		String SQL = "Delete from WORDS where word = ?";
		jdbcTemplateObject.update(SQL, word);
		System.out.println("Deleted Word " + word );
		
	}
	
	public void banUser(String user) {
		String SQL = "Delete from WORDS where uploader = (select id from users where name = ?)";
		jdbcTemplateObject.update(SQL, user);
		SQL = "Delete from high_scores where player = (select id from users where name = ?)";
		jdbcTemplateObject.update(SQL, user);
		SQL = "Delete from users where name = ?";
		jdbcTemplateObject.update(SQL, user);
	}
	
	public void resetHighScore(String user) {
		String SQL = "update high_scores set score = 0 where player = (select id from users where name = ?)";
		jdbcTemplateObject.update(SQL, user);
	}
	
	public List <WordDTO> refreshCache() {
		String SQL = "select word, name from words w, users u where w.uploader = u.id";
		return jdbcTemplateObject.query(SQL, new WordMapper());
	}
	
	public List<HighScoreDTO> getTop3Scores() {
		String SQL = "select * from (select h.score, u.name, h.date_of_ach from high_scores h, users u where h.player=u.id order by score desc) where rownum <4";
		return jdbcTemplateObject.query(SQL, new Top3ScoreMapper());
	}
	
	public HighScoreDTO getScoreOfPlayer(String user) {
		String SQL = "select h.score, u.name, h.date_of_ach from high_scores h, users u where h.player=u.id and u.name = ?";
		return jdbcTemplateObject.queryForObject(SQL, new Object[]{user}, new Top3ScoreMapper());
	}
	
	public boolean updateHighScore(String user, int score) {
		String SQL = "update high_scores set score = ?, date_of_ach = systimestamp where player = (select id from users where name = ?)";
		jdbcTemplateObject.update(SQL, score, user);
		return true;
	}
	
	public CredentialsDTO getUser(String user) {
		String SQL = "select * from users where name = ?";
		CredentialsDTO credentialsDTO = new CredentialsDTO();
		try {
			credentialsDTO = jdbcTemplateObject.queryForObject(SQL, new Object[]{user}, new CredentialsMapper());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
		return credentialsDTO;
	}

}
