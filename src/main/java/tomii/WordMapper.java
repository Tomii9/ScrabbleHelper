package tomii;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class WordMapper implements RowMapper<WordDTO> {

	@Override
	public WordDTO mapRow(ResultSet rs, int rownum) throws SQLException {
		WordDTO word = new WordDTO();
		word.setWord(rs.getString("WORD"));
		return word;
	}
	
}
