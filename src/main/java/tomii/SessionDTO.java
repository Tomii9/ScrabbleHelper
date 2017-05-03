package tomii;

public class SessionDTO {
	String token;
	String type;
	String errorMessage;
	
	public SessionDTO(String token, String type, String errorMessage) {
		super();
		this.token = token;
		this.type = type;
		this.errorMessage = errorMessage;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMssage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	
}
