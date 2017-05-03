package tomii;

import java.security.SecureRandom;
import java.math.BigInteger;

public class LoginService {
	
	DatabaseController db = new DatabaseController();
	
	public LoginService() {
		
	}
	
	public SessionDTO login (String user, String password) {
		CredentialsDTO credentialsDTO = db.getUser(user);
		
		if (credentialsDTO == null) {
			return new SessionDTO("", "", "No user exists by this name!");
		} else if (!password.equals(credentialsDTO.getPassWord())) {
			return new SessionDTO("", "", "Incorrect password!");
		} else {
			SecureRandom random = new SecureRandom();
			
			
			return new SessionDTO(new BigInteger(130, random).toString(32), credentialsDTO.getType(), "");
		}
	}
	
	public boolean register (String user, String password) {
		if (ifUserExists(user)) {
			return false;
		}
		db.register(user, password);
		return true;
	}
	
	private boolean ifUserExists(String user) {
		if (db.getUser(user) != null) {
			return true;
		}
		return false;
	}
}
