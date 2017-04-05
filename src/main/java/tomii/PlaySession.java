package tomii;

import java.util.List;
import java.util.Vector;

public class PlaySession {
	
	WordFinder wordfinder = new WordFinder();
	Board board = new Board();
	List<Character> hand = new Vector<Character>();
	String user;
	
	public PlaySession(String user) {
		this.user = user;
	}
	
	public String getUser() {
		return user;
	}
	
	public boolean refreshCache() {
		return wordfinder.refreshCache();
	}
	
	public boolean placeWord(String word, int posX, int posY, boolean down) {
		return board.placeWord(word, posX, posY, down);
	}
	
	public boolean containsWord(String word) {
		return wordfinder.containsWord(word);
	}
	
	public char[][] getBoard() {
		return board.getBoard();
	}

}
