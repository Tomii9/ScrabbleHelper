package tomii;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class Top3ScoreMapper implements RowMapper<HighScoreDTO>{
	
	@Override
	public HighScoreDTO mapRow(ResultSet rs, int rownum) throws SQLException {
		HighScoreDTO highScore = new HighScoreDTO();
		highScore.setHighscore(rs.getInt("SCORE"));
		highScore.setUser(rs.getString("NAME"));
		highScore.setDate(rs.getDate("DATE_OF_ACH"));
		return highScore;
	}
}
