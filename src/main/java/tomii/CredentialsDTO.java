package tomii;

public class CredentialsDTO {
	String userName;
	String passWord;
	String type;
	
	public CredentialsDTO() {
		
	}
	public CredentialsDTO(String userName, String passWord, String type) {
		super();
		this.userName = userName;
		this.passWord = passWord;
		this.type = type;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassWord() {
		return passWord;
	}
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
}
