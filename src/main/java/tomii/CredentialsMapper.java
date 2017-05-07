package tomii;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class CredentialsMapper implements RowMapper<CredentialsDTO>{

	@Override
	public CredentialsDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
		CredentialsDTO credentialsDTO = new CredentialsDTO();
		credentialsDTO.setUserName(rs.getString("NAME"));
		credentialsDTO.setPassWord(rs.getString("PASSWORD"));
		credentialsDTO.setType(rs.getString("TYPE"));
		return credentialsDTO;
	}

}
