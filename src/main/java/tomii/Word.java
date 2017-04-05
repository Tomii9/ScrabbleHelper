package tomii;

public class Word {
	private String word;
	private int x;
	private int y;
	
	public Word () {
	}
	
	public Word (String word, int x, int y) {
		this.word = word;
		this.x = x;
		this.y = y;
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

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	
}
