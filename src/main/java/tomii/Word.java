package tomii;

public class Word {
	private String word;
	private String uploader;
	
	public Word () {
	}

	public Word (String word) {
		this.word = word;
	}

	public String getWord() {
		return word;
	}	

	public void setWord(String word) {
		this.word = word;
	}
	
	public String getUploader() {
		return uploader;
	}

	public void setUploader(String uploader) {
		this.uploader = uploader;
	}
}
