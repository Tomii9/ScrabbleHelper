package tomii;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

public class WordFinder {

	Map<Character, Integer> lettervalues = new HashMap<Character, Integer>();
	List<Character> hand = new Vector<Character>();
	Board board = new Board();
	Trie trie;	
	char EMPTY = '\u0000';
	
	int largestBeginsAtX = 0;
	int largestBeginsAtY = 0;
	int largestScore = 0;
	boolean largestIsDown = false;
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
	  	
	private boolean loneAnchor(AnchorSquare anchor) { 
		int x = anchor.getX();
		int y = anchor.getY();
		return (y > 0 && board.getSquare(x, y-1) == EMPTY) && (y < 14 && board.getSquare(x, y+1) == EMPTY);
	}
	
	public WordDTO getBestWord(List<Character> hand, Board board) {
		boolean firstTurn = board.getSquare(7, 7) == EMPTY;
		resetBestWord();
		this.board = board;
		this.hand = hand;
		largestWord = "";
		largestScore = -1;
		largestBeginsAtX = -1;
		largestBeginsAtY = -1;
		List<AnchorSquare> anchorSquares = getAnchorSquares(board);
		WordDTO result = new WordDTO();
		String tempResult = new String();
		int tempLargestScore = -1;
		int tempLargestBeginsAtX = -1;
		int tempLargestBeginsAtY = -1;
		boolean tempLargestIsDown = false;
		AnchorSquare tempAnchorSquare = new AnchorSquare();
		int limit;
		
		for (int i=0; i<anchorSquares.size(); i++) {
			limit = getLimit(anchorSquares.get(i), board);
			tempAnchorSquare = new AnchorSquare();
			tempAnchorSquare.setX(anchorSquares.get(i).getX());
			tempAnchorSquare.setY(anchorSquares.get(i).getY());
			int leftPartLength = getLeftPart(tempAnchorSquare).length();
			if ((limit==0 && tempAnchorSquare.getY() >= leftPartLength) && !firstTurn) {
				tempAnchorSquare.setY(anchorSquares.get(i).getY()-leftPartLength);
				extendRight(new String(), trie.getRoot(), tempAnchorSquare, tempAnchorSquare, false, 0);
			} else if (loneAnchor(tempAnchorSquare) || firstTurn) {
				leftPartForLoneAnchor(new String(), trie.getRoot(), limit, anchorSquares.get(i), tempAnchorSquare, false, 0);
			} else {
				leftPart(new String(), trie.getRoot(), limit, anchorSquares.get(i), tempAnchorSquare, false, 0);

			}
			
		}
		
		if (!firstTurn) {
			
			tempResult = largestWord;
			tempLargestScore = largestScore;
			tempLargestBeginsAtX = largestBeginsAtX;
			tempLargestBeginsAtY = largestBeginsAtY;
			tempLargestIsDown = largestIsDown;
			
			board.transponeBoard();
			
			anchorSquares = getAnchorSquares(board);
			for (int i=0; i<anchorSquares.size(); i++) {
				limit = getLimit(anchorSquares.get(i), board);
				tempAnchorSquare = new AnchorSquare();
				tempAnchorSquare.setX(anchorSquares.get(i).getX());
				tempAnchorSquare.setY(anchorSquares.get(i).getY());
				int leftPartLength = getLeftPart(tempAnchorSquare).length();
				if (limit==0 && tempAnchorSquare.getY() >= leftPartLength && !firstTurn) {
					tempAnchorSquare.setY(anchorSquares.get(i).getY()-leftPartLength);
					extendRight(new String(), trie.getRoot(), tempAnchorSquare, tempAnchorSquare, true, 0);
				} else if (loneAnchor(tempAnchorSquare)) {
					leftPartForLoneAnchor(new String(), trie.getRoot(), limit, anchorSquares.get(i), tempAnchorSquare, true, 0);
				} else {
					leftPart(new String(), trie.getRoot(), limit, anchorSquares.get(i), tempAnchorSquare, true, 0);

				}
				
			}
			
			board.transponeBoard();
		}
		if (tempLargestScore < largestScore && !firstTurn) {
			if (largestIsDown) {
				int temp = largestBeginsAtX;
				largestBeginsAtX = largestBeginsAtY;
				largestBeginsAtY = temp;
			}
			boolean success = board.placeWord(largestWord, largestBeginsAtX, largestBeginsAtY, largestIsDown);
			if (!success) { 
				return result;
			} else {
				result = new WordDTO(largestWord, largestBeginsAtX, largestBeginsAtY, largestIsDown, largestScore);
			}
		} else if (!firstTurn){
			if (tempLargestIsDown) {
				int temp = tempLargestBeginsAtX;
				tempLargestBeginsAtX = tempLargestBeginsAtY;
				tempLargestBeginsAtY = temp;
			}
			boolean success = board.placeWord(tempResult, tempLargestBeginsAtX, tempLargestBeginsAtY, tempLargestIsDown);
			if (!success) {
				return result;
			} else {
				result = new WordDTO(tempResult, tempLargestBeginsAtX, tempLargestBeginsAtY, tempLargestIsDown, tempLargestScore);
			}
		} else {
			boolean success = board.placeWord(largestWord, largestBeginsAtX, largestBeginsAtY, largestIsDown);
			if (!success) {
				return result;
			} else {
				result = new WordDTO(largestWord, largestBeginsAtX, largestBeginsAtY, largestIsDown, largestScore);
			}
		}
		return result;
	}
	
	public boolean refreshCache() {
		trie = new Trie(new DatabaseController().getWords());
		return true;
	}
	
	public String getLeftPart(AnchorSquare anchorSquare) {
		String leftPart = "";
		int y = anchorSquare.getY();
		int x = anchorSquare.getX();
		while (y>0 && board.getSquare(x, y-1) != EMPTY) {
			leftPart = leftPart.concat(String.valueOf(board.getSquare(x, y-1)));
			y--;
		}
		leftPart = new StringBuffer(leftPart).reverse().toString();
		return leftPart;
	}

	public boolean containsWord(String string) {
		return trie.containsWord(string);
	}
	
	private List<AnchorSquare> getAnchorSquares(Board board) {
		List<AnchorSquare> anchorSquares = new Vector<AnchorSquare>();
		AnchorSquare tmpSquare = new AnchorSquare();
		if (board.getSquare(7, 7) == EMPTY) {
			tmpSquare.setX(7);
			tmpSquare.setY(7);
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
						if (j<14 && board.getSquare(i, j+1) == EMPTY) {
							tmpSquare.setX(i);
							tmpSquare.setY(j + 1);
							anchorSquares.add(tmpSquare);
							tmpSquare = new AnchorSquare();	
						}
						if (i>0 && board.getSquare(i-1, j) == EMPTY) {
							tmpSquare.setX(i - 1);
							tmpSquare.setY(j);
							anchorSquares.add(tmpSquare);
							tmpSquare = new AnchorSquare();	
						}
						if (i<14 && board.getSquare(i+1, j) == EMPTY) {
							tmpSquare.setX(i + 1);
							tmpSquare.setY(j);
							anchorSquares.add(tmpSquare);
							tmpSquare = new AnchorSquare();	
						}
					}
				}
			}	
		}
		return anchorSquares;
	}
	
	private int getLimit(AnchorSquare anchorSquare, Board board) {
		int limit = 0;
		int y = anchorSquare.getY();
		while (y>0 && board.getSquare(anchorSquare.getX(), y-1) == EMPTY) {
			if ((y>1 && board.getSquare(anchorSquare.getX(), y-2) == EMPTY) || y==1) {
				limit++;
				y--;
			} else {
				break;
			}
		}
		return limit;
	}
	
	private boolean alreadyOnBoard(AnchorSquare start, char[] word) {
		int x = start.getX();
		int y = start.getY();
		
		for (int i=0; i<word.length; i++) {
			if (board.getSquare(x, y+i) != word[i]) {
				return false;
			}
		}
		return true;
	}
	
	public void leftPart (String partialWord, Node node, int limit, AnchorSquare anchor, AnchorSquare start, boolean transponed, int crossSum) {
		
		extendRight(partialWord, node, anchor, start, transponed, crossSum);
		Node newNode = new Node();
		
		if (limit > 0 && node != null) {
			
			Set<Character> children = node.getChildren().keySet();
			
			for (Character character : children) {
				if (hand.contains(character)) {
					hand.remove(new Character(character));
					newNode = node.getChild(character);
					AnchorSquare tempStart = new AnchorSquare();
					tempStart.setX(start.getX());
					tempStart.setY(start.getY()-1);
					int crossValue = crossCheck(character, tempStart);
					if (start.getY() > 0 && crossValue != -1) {
						leftPart(character + partialWord, newNode, limit-1, anchor, tempStart, transponed, crossValue+crossSum);
					}
					hand.add(character);
					
				} else if (hand.contains(new Character('.'))) {
					Set<Character> allLetters = lettervalues.keySet();
					for (Character character2 : allLetters) {
						hand.remove(new Character('.'));
						newNode = node.getChild(character2);
						AnchorSquare tempStart = new AnchorSquare();
						tempStart.setX(start.getX());
						tempStart.setY(start.getY()-1);
						int crossValue = crossCheck(character2, tempStart);
						if (start.getY() > 0 && crossValue != -1) {
							leftPart(character2 + partialWord, newNode, limit-1, anchor, tempStart, transponed, crossValue+crossSum);
						}
						hand.add(new Character('.'));
					}
				}
			}
		}
	}
	
	private void extendRight(String partialWord, Node node, AnchorSquare newSquare, AnchorSquare start, boolean transponed, int crossSum) {
		Node newNode = new Node();
		AnchorSquare nextSquare = new AnchorSquare();
		if (node != null) {
			
			
			if(newSquare.getY() == 15 || board.getSquare(newSquare.getX(), newSquare.getY()) == EMPTY) {
				if (newSquare.getY()+1 == 16) {
					return;
				}
				if (trie.containsWord(partialWord) && node.isValid() &&
						(newSquare.getY() == 14 || board.getSquare(newSquare.getX(), newSquare.getY()+1) == EMPTY) &&
						!alreadyOnBoard(start, partialWord.toCharArray())) {
					sumWordValue(partialWord, start.getX(), start.getY(), transponed, crossSum);
				}
				Set<Character> childNodes = node.getChildren().keySet();
				
				for (Character c : childNodes) {
					int crossValue = crossCheck(c, newSquare);
					if (hand.contains(c) && crossValue != -1) {
						hand.remove(new Character(c));
						newNode = node.getChild(c);
						if (newSquare.getY() < 14) {
							nextSquare.setX(newSquare.getX());
							nextSquare.setY(newSquare.getY()+1);
							extendRight(partialWord+c, newNode, nextSquare, start, transponed, crossSum + crossValue);
						}
						hand.add(c);
					} else if (hand.contains('.')) {
						Set<Character> allLetters = lettervalues.keySet();
						for (Character c2: allLetters) {
							crossValue = crossCheck(c2, newSquare);
							if (node.getChild(c2) != null && crossValue != -1) {
								hand.remove(new Character('.'));
								newNode = node.getChild(c2);
								if (newSquare.getY() < 14) {
									nextSquare.setX(newSquare.getX());
									nextSquare.setY(newSquare.getY()+1);
									extendRight(partialWord+c2, newNode, nextSquare, start, transponed, crossSum + crossValue);
								}
								hand.add('.');
							}
						}
					}
				}
			} else if (newSquare.getY() < 15){
				Character letterOnBoard = board.getSquare(newSquare.getX(), newSquare.getY());
				
				if (node.getChild(letterOnBoard) != null) {
					nextSquare.setX(newSquare.getX());
					nextSquare.setY(newSquare.getY()+1);
					extendRight(partialWord+letterOnBoard, node.getChild(letterOnBoard), nextSquare, start, transponed, crossSum);
				}	
			}
		}
	}
	
	private int crossCheck(Character c, AnchorSquare anchorSquare) {

		String toCheck = c.toString();
		int x = anchorSquare.getX();
		int y = anchorSquare.getY();
		AnchorSquare crossStart = new AnchorSquare();
		crossStart.setX(x);
		crossStart.setY(y);
		if (x > 0 && y<15 && board.getSquare(x-1, y) != EMPTY) {
			while (x>0 && board.getSquare(x-1, y) != EMPTY) {
				toCheck = board.getSquare(x-1, y) + toCheck;
				x--;
			}
			crossStart.setX(x);
		}
		
		x = anchorSquare.getX();
		if (x < 14 && y<15 && board.getSquare(x+1, y) != EMPTY) {
			while (x < 14 && board.getSquare(x+1, y) != EMPTY) {
				toCheck = toCheck + board.getSquare(x+1, y);
				x++;
			}
			
		}
		
		if (!trie.containsWord(toCheck) && toCheck.length() > 1) {
			return -1;
		} else if (toCheck.length()==1) {
			return 0;
		}

		return sumCrossValue(toCheck, crossStart.getX(), crossStart.getY());
	}
	
	private int sumWordValue(String word, int x, int y, boolean trans, int crossSum) {
		
		int result = 0;
		int wordMultiplier = 0;
		int letterMultiplier = 0;
		char[] wordCharArray = word.toCharArray();
		for (int i=0; i<wordCharArray.length; i++) {

			wordMultiplier += board.getWordMultiplier(x, y + i);
			if (board.getLetterMultiplier(x, y+i) != 0) {
				letterMultiplier += (board.getLetterMultiplier(x, y+i))*lettervalues.get(wordCharArray[i]);
			} else {
				letterMultiplier += lettervalues.get(wordCharArray[i]);
			}
			
			if (wordMultiplier != 0) {
				result = wordMultiplier * letterMultiplier + crossSum;
			} else {
				result = letterMultiplier + crossSum;
			}
			if (result > largestScore) {
				largestBeginsAtX = x;
				largestBeginsAtY = y;
				largestScore = result;
				largestWord = word;
				largestIsDown = trans;
			}
		}
		return result;
	}
	
	private int sumCrossValue(String word, int x, int y) {
		int result = 0;
		int wordMultiplier = 0;
		int letterMultiplier = 0;
		char[] wordCharArray = word.toCharArray();
		for (int i=0; i<wordCharArray.length; i++) {

			wordMultiplier += board.getWordMultiplier(x + i, y);
			if (board.getLetterMultiplier(x + i, y) != 0) {
				letterMultiplier += (board.getLetterMultiplier(x + i, y))*lettervalues.get(wordCharArray[i]);
			} else {
				letterMultiplier += lettervalues.get(wordCharArray[i]);
			}
			
			if (wordMultiplier != 0) {
				result = wordMultiplier * letterMultiplier;
			} else {
				result = letterMultiplier;
			}
		}
		return result;
	}
	
	private void resetBestWord() {
		largestBeginsAtX = 0;
		largestBeginsAtY = 0;
		largestScore = 0;
		largestWord = "";
		largestIsDown = false;
	}
	
	public void leftPartForLoneAnchor (String partialWord, Node node, int limit, AnchorSquare anchor, AnchorSquare start, boolean transponed, int crossSum) {
		
		extendRightForLoneAnchor(partialWord, node, anchor, start, transponed, crossSum);
		Node newNode = new Node();
		
		if (limit > 0 && node != null) {
			
			Set<Character> children = node.getChildren().keySet();
			
			for (Character character : children) {
				if (hand.contains(character)) {
					hand.remove(new Character(character));
					newNode = node.getChild(character);
					AnchorSquare tempStart = new AnchorSquare();
					tempStart.setX(start.getX());
					tempStart.setY(start.getY()-1);
					int crossValue = crossCheck(character, tempStart);
					if (start.getY() > 0 && crossValue != -1) {
						leftPartForLoneAnchor(character + partialWord, newNode, limit-1, anchor, tempStart, transponed, crossValue+crossSum);
					}
					hand.add(character);
					
				} else if (hand.contains(new Character('.'))) {
					Set<Character> allLetters = lettervalues.keySet();
					for (Character character2 : allLetters) {
						hand.remove(new Character('.'));
						newNode = node.getChild(character2);
						AnchorSquare tempStart = new AnchorSquare();
						tempStart.setX(start.getX());
						tempStart.setY(start.getY()-1);
						int crossValue = crossCheck(character2, tempStart);
						if (start.getY() > 0 && crossValue != -1) {
							leftPartForLoneAnchor(character2 + partialWord, newNode, limit-1, anchor, tempStart, transponed, crossValue+crossSum);
						}
						hand.add(new Character('.'));
					}
				}
			}
		}
	}

	
	private void extendRightForLoneAnchor(String partialWord, Node node, AnchorSquare newSquare, AnchorSquare start, boolean transponed, int crossSum) {
		Node newNode = new Node();
		AnchorSquare nextSquare = new AnchorSquare();
		if (node != null) {
			
			
			Set<Character> childNodes = node.getChildren().keySet();
			
			for (Character c : childNodes) {
				int crossValue = crossCheck(c, newSquare);
				if (hand.contains(c) && crossValue != -1) {
					hand.remove(new Character(c));
					newNode = node.getChild(c);
					if (newSquare.getY() < 14) {
						nextSquare.setX(newSquare.getX());
						nextSquare.setY(newSquare.getY()+1);
						extendRight(partialWord+c, newNode, nextSquare, start, transponed, crossSum + crossValue);
					}
					hand.add(c);
				} else if (hand.contains('.')) {
					Set<Character> allLetters = lettervalues.keySet();
					for (Character c2: allLetters) {
						crossValue = crossCheck(c2, newSquare);
						if (node.getChild(c2) != null && crossValue != -1) {
							hand.remove(new Character('.'));
							newNode = node.getChild(c2);
							if (newSquare.getY() < 14) {
								nextSquare.setX(newSquare.getX());
								nextSquare.setY(newSquare.getY()+1);
								extendRight(partialWord+c2, newNode, nextSquare, start, transponed, crossSum + crossValue);
							}
							hand.add('.');
						}
					}
				}
			}

		}
	}

}





















