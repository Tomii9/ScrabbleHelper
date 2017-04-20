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
	
	public boolean setHand(String handString) {
		hand = new Vector<Character>();
		for (int i=0; i<handString.length(); i++) {
			hand.add(handString.charAt(i));
		}
		return true;
	}
	
	public WordDTO getBestWord() {
		return wordfinder.getBestWord(hand, board);
	}
	
	public boolean resetBoard() {
		board = new Board();
		return true;
	}

}
