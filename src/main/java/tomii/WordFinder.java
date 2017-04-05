package tomii;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.springframework.boot.context.annotation.DeterminableImports;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class WordFinder {

	List<Word> words = new Vector<Word>();
	ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
	WordJDBCTemplate wordJDBCTemplate = (WordJDBCTemplate)context.getBean("WordJDBCTemplate");
	Map<Character, Integer> lettervalues = new HashMap<Character, Integer>();
	List<Character> hand = new Vector<Character>();
	Board board = new Board();
	Trie trie;	
	char EMPTY = '\u0000';
	
	int tempWordTotal = 0;
	int largestBeginsAtX = 0;
	int largestBeginsAtY = 0;
	int largestScore = 0;
	String largestWord = "";
	
	public WordFinder() {
		lettervalues.put('a', 1);
		lettervalues.put('b', 3);
		lettervalues.put('c', 3);
		lettervalues.put('d', 2);
		lettervalues.put('e', 1);
		lettervalues.put('f', 4);
		lettervalues.put('g', 2);
		lettervalues.put('h', 4);
		lettervalues.put('i', 1);
		lettervalues.put('j', 8);
		lettervalues.put('k', 5);
		lettervalues.put('l', 1);
		lettervalues.put('m', 3);
		lettervalues.put('n', 1);
		lettervalues.put('o', 1);
		lettervalues.put('p', 3);
		lettervalues.put('q', 10);
		lettervalues.put('r', 1);
		lettervalues.put('s', 1);
		lettervalues.put('t', 1);
		lettervalues.put('u', 1);
		lettervalues.put('v', 1);
		lettervalues.put('w', 4);
		lettervalues.put('x', 8);
		lettervalues.put('y', 4);
		lettervalues.put('z', 10);
	}
	
	public Word getBestWord(List<Character> hand, Board board) {
		resetBestWord();
		this.board = board;
		List<AnchorSquare> anchorSquares = getAnchorSquares(board);
		List<AnchorSquare> tempSquares = new Vector<AnchorSquare>();
		String bestWord = new String();
		Word result = new Word();
		int size = anchorSquares.size();
		
		String tempResult = new String();
		int tempLargestScore = -1;
		
		return result;
	}
	
	public boolean refreshCache() {
		words = wordJDBCTemplate.refreshCache();
		trie = new Trie(words);
		return true;
	}

	public boolean containsWord(String string) {
		return trie.containsWord(string);
	}
	
	private List<AnchorSquare> getAnchorSquares(Board board) {
		List<AnchorSquare> anchorSquares = new Vector<AnchorSquare>();
		AnchorSquare tmpSquare = new AnchorSquare();
		if (board.getSquare(8, 8) == EMPTY) {
			tmpSquare.setX(8);
			tmpSquare.setY(8);
			anchorSquares.add(tmpSquare);
		} else {
			
			for (int i=0; i<15; i++) {
				for (int j=0; j<15; j++) {
					if (board.getSquare(i, j) != EMPTY) {
						if (j>0 && board.getSquare(i, j - 1) == EMPTY) {
							tmpSquare.setX(i);
							tmpSquare.setY(j - 1);
							anchorSquares.add(tmpSquare);
							tmpSquare = new AnchorSquare();	
						}
					}
				}
			}	
		}
		return anchorSquares;
	}
	
	public Word getBestWord() {
		Word result = new Word();
		List<AnchorSquare> anchorSquares = new Vector<AnchorSquare>();
		List<AnchorSquare> tempSquares = new Vector<AnchorSquare>();
		anchorSquares = getAnchorSquares(board);
		int size = anchorSquares.size();
		
		String tempResult = new String();
		int tempLargestScore = -1;
		return result;
	}
	
	private int getLimit(AnchorSquare anchorSquare, Board board) {
		int limit = 0;
		int y = anchorSquare.getY();
		while (board.getSquare(anchorSquare.getX(), y) == EMPTY) {
			limit++;
			y--;
		}
		return limit;
	}
	
	public void leftPart (String partialWord, Node node, int limit,
			AnchorSquare anchorSquare, AnchorSquare start, boolean transponed) {
		
		extendRight(partialWord, node, anchorSquare, anchorSquare, start, transponed);
		Node newNode = new Node();
		
		if (limit > 0) {
			
			Set<Character> children = node.getChildren().keySet();
			
			for (Character character : children) {
				if (hand.contains(character)) {
					hand.remove(character);
					newNode = node.getChild(character);
					start.setY(start.getY() - 1);
					leftPart(character + partialWord, newNode, limit-1, anchorSquare, start, transponed);
					hand.add(character);
					
				} else if (hand.contains('*')) {
					Set<Character> allLetters = lettervalues.keySet();
					for (Character character2 : allLetters) {
						hand.remove('*');
						newNode = node.getChild(character2);
						start.setY(start.getY() - 1);
						leftPart(character2 + partialWord, newNode, limit-1, anchorSquare, start, transponed);
						hand.add('*');
					}
				}
			}
		}
	}
	
	private void extendRight(String partialWord, Node node, AnchorSquare anchorSquare, 
			AnchorSquare newSquare, AnchorSquare start, boolean transponed) {
		Node newNode = new Node();
		AnchorSquare nextSquare = new AnchorSquare();
		if(board.getSquare(newSquare.getX(), newSquare.getY()) == EMPTY) {
			if (trie.containsWord(partialWord) && node.isValid()) {
				if (7-hand.size() < partialWord.length()) {
					sumWordValue(partialWord, start.getX(), start.getY(), true, transponed);
				} else if(board.getSquare(7, 7) == EMPTY) {
					sumWordValue(partialWord, start.getX(), start.getY(), true, transponed);
				}
			}
			Set<Character> childNodes = node.getChildren().keySet();
			
			for (Character c : childNodes) {
				if (hand.contains(c) && crossCheck(c, newSquare, transponed)) {
					hand.remove(c);
					newNode = node.getChild(c);
					nextSquare.setX(newSquare.getX());
					nextSquare.setY(newSquare.getY()+1);
					extendRight(partialWord+c, newNode, anchorSquare, nextSquare, start, transponed);
					hand.add(c);
				} else if (hand.contains('*')) {
					Set<Character> allLetters = lettervalues.keySet();
					for (Character c2: allLetters) {
						hand.remove('*');
						newNode = node.getChild(c2);
						nextSquare.setX(newSquare.getX());
						nextSquare.setY(newSquare.getY()+1);
						extendRight(partialWord+c2, newNode, anchorSquare, nextSquare, start, transponed);
						hand.add('*');
					}
				}
			}
		} else {
			Character letterOnBoard = board.getSquare(newSquare.getX(), newSquare.getY());
			
			if (node.getChild(letterOnBoard) != null) {
				nextSquare.setX(newSquare.getX());
				nextSquare.setY(newSquare.getY());
				extendRight(partialWord+letterOnBoard, node.getChild(letterOnBoard), anchorSquare, nextSquare, start, transponed);
			}	
		}
	}
	
	private boolean crossCheck(Character c, AnchorSquare anchorSquare, boolean transponed) {
		
		String toCheck = c.toString();
		int x = anchorSquare.getX();
		int y = anchorSquare.getY();
		
		if (board.getSquare(x-1, y) != EMPTY) {
			while (board.getSquare(x-1, y) != EMPTY) {
				toCheck = board.getSquare(x-1, y) + toCheck;
				x--;
			}
		}
		
		x = anchorSquare.getX();
		if (board.getSquare(x+1, y) != EMPTY) {
			while (board.getSquare(x+1, y) != EMPTY) {
				toCheck = board.getSquare(x+1, y) + toCheck;
				x++;
			}
			
		}
		
		if (!trie.containsWord(toCheck)) {
			return false;
		}
		
		toCheck = c.toString();
		if (board.getSquare(x, y-1) != EMPTY) {
			while (board.getSquare(x, y-1) != EMPTY) {
				toCheck = board.getSquare(x, y-1) + toCheck;
				y--;
			}
		}
		
		y = anchorSquare.getY();
		if (board.getSquare(x, y+1) != EMPTY) {
			while (board.getSquare(x, y+1) != EMPTY) {
				toCheck = toCheck + board.getSquare(x, y+1);
				y++;
			}
		}
		
		if (!trie.containsWord(toCheck)) {
			return false;
		}
		return true;
	}
	
	private int sumWordValue(String word, int x, int y, boolean lastCheck, boolean trans) {
		
		int result = 0;
		int wordMultiplier = 0;
		int letterMultiplier = 0;
		char[] wordCharArray = word.toCharArray();
		for (int i=0; i<wordCharArray.length; i++) {
			if (x < 15 && i+y < 15) {
				wordMultiplier += board.getWordMultiplier(x, y + i);
				if (board.getLetterMultiplier(x, y+i) != 0) {
					letterMultiplier += (board.getLetterMultiplier(x, y+i))*lettervalues.get(wordCharArray[i]);
				} else {
					letterMultiplier += lettervalues.get(wordCharArray[i]);
				}
			} else {
				return -1;
			}
			
			if (wordMultiplier != 0) {
				result = wordMultiplier * letterMultiplier;
			}
			if (!lastCheck) {
				tempWordTotal += result;
			} else {
				largestBeginsAtX = x;
				largestBeginsAtY = y;
				largestScore = result;
				largestWord = word;
			}
		}
		return result;
	}
	
	private void resetBestWord() {
		tempWordTotal = 0;
		largestBeginsAtX = 0;
		largestBeginsAtY = 0;
		largestScore = 0;
		largestWord = "";
	}

}





















