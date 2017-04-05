package tomii;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class WordMapper implements RowMapper<Word> {

	@Override
	public Word mapRow(ResultSet rs, int rownum) throws SQLException {
		Word word = new Word();
		word.setWord(rs.getString("WORD"));
		return word;
	}
	
}
