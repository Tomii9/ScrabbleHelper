package tomii;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
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
	
	public Word getBestWord(List<Character> hand, Board board) {
		boolean firstTurn = board.getSquare(7, 7) == EMPTY;
		resetBestWord();
		this.board = board;
		this.hand = hand;
		largestWord = "";
		largestScore = -1;
		largestBeginsAtX = -1;
		largestBeginsAtY = -1;
		List<AnchorSquare> anchorSquares = getAnchorSquares(board);
		Word result = new Word();
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
			tempWordTotal = 0;
			/*if (firstTurn) {
				limit = 0;
			}*/
			int leftPartLength = getLeftPart(tempAnchorSquare).length();
			if (limit==0 && tempAnchorSquare.getY() >= leftPartLength && !firstTurn) {
				tempAnchorSquare.setY(anchorSquares.get(i).getY()-leftPartLength);
				extendRight(new String(), trie.getRoot(), tempAnchorSquare, tempAnchorSquare, false);
				//leftPart(new String(), trie.getRoot(), limit, tempAnchorSquare, false);
			} else {
				leftPart(new String(), trie.getRoot(), limit, anchorSquares.get(i), tempAnchorSquare, false);

			}
			
		}
		
		if (!firstTurn) {
			
			/*for (int i=0; i<anchorSquares.size(); i++) {
				limit = getLimit(anchorSquares.get(i), board);
				tempAnchorSquare = new AnchorSquare();
				tempAnchorSquare.setX(anchorSquares.get(i).getX());
				tempAnchorSquare.setY(anchorSquares.get(i).getY() + 1);
				tempWordTotal = 0;
				if ((anchorSquares.get(i).getY() < 14 && board.getSquare(anchorSquares.get(i).getX(), anchorSquares.get(i).getY() + 1) == EMPTY) &&
						(anchorSquares.get(i).getY() > 0 && board.getSquare(anchorSquares.get(i).getX(), anchorSquares.get(i).getY() - 1) == EMPTY)) {
					//leftPart(new String(), trie.getRoot(), limit, tempAnchorSquare, tempAnchorSquare, false);
					extendRight(new String(), trie.getRoot(), tempAnchorSquare, tempAnchorSquare, true);
				}
				
			}*/
			
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
				tempWordTotal = 0;
				/*if (firstTurn) {
					limit = 0;
				}*/
				int leftPartLength = getLeftPart(tempAnchorSquare).length();
				if (limit==0 && tempAnchorSquare.getY() >= leftPartLength && !firstTurn) {
					tempAnchorSquare.setY(anchorSquares.get(i).getY()-leftPartLength);
					extendRight(new String(), trie.getRoot(), tempAnchorSquare, tempAnchorSquare, true);
					//leftPart(new String(), trie.getRoot(), limit, tempAnchorSquare, true);
				} else {
					leftPart(new String(), trie.getRoot(), limit, anchorSquares.get(i), tempAnchorSquare, true);

				}
				
			}
			/*for (int i=0; i<anchorSquares.size(); i++) {
				limit = getLimit(anchorSquares.get(i), board);
				tempAnchorSquare = new AnchorSquare();
				tempAnchorSquare.setX(anchorSquares.get(i).getX());
				tempAnchorSquare.setY(anchorSquares.get(i).getY() + 1);
				tempWordTotal = 0;
				if (firstTurn) {
					limit = 0;
				}
				if ((anchorSquares.get(i).getY() < 14 && board.getSquare(anchorSquares.get(i).getX(), anchorSquares.get(i).getY() + 1) == EMPTY) &&
						(anchorSquares.get(i).getY() > 0 && board.getSquare(anchorSquares.get(i).getX(), anchorSquares.get(i).getY() - 1) == EMPTY)) {
					leftPart(bestWord, trie.getRoot(), limit, tempAnchorSquare, tempAnchorSquare, true);
					//extendRight(new String(), trie.getRoot(), tempAnchorSquare, tempAnchorSquare, true);
				}		
			}*/
			board.transponeBoard();
		
		}
		if (tempLargestScore < largestScore && !firstTurn) {
			if (largestIsDown) {
				int temp = largestBeginsAtX;
				largestBeginsAtX = largestBeginsAtY;
				largestBeginsAtY = temp;
			}
			
			boolean success = board.placeWord(largestWord, largestBeginsAtX, largestBeginsAtY, largestIsDown);
			if (!success) { //TODO SKIP CURRENT AND GET NEXT BEST
				System.out.println("SHIT1");
				System.out.println(largestWord);
				System.out.println(largestBeginsAtX);
				System.out.println(largestBeginsAtY);
				System.out.println(largestScore);
				System.out.println(largestIsDown);
			} else {
				result = new Word(largestWord, largestBeginsAtX, largestBeginsAtY, largestIsDown, largestScore);
				System.out.println(largestWord);
				System.out.println(largestBeginsAtX);
				System.out.println(largestBeginsAtY);
				System.out.println(largestScore);
			}
			
		} else if (!firstTurn){
			if (tempLargestIsDown) {
				int temp = tempLargestBeginsAtX;
				tempLargestBeginsAtX = tempLargestBeginsAtY;
				tempLargestBeginsAtY = temp;
			}
			
			boolean success = board.placeWord(tempResult, tempLargestBeginsAtX, tempLargestBeginsAtY, tempLargestIsDown);
			if (!success) { //TODO SKIP CURRENT AND GET NEXT BEST
				System.out.println("SHIT2");
				System.out.println(tempResult);
				System.out.println(tempLargestBeginsAtX);
				System.out.println(tempLargestBeginsAtY);
				System.out.println(tempLargestScore);
				System.out.println(tempLargestIsDown);
			} else {
				result = new Word(tempResult, tempLargestBeginsAtX, tempLargestBeginsAtY, tempLargestIsDown, tempLargestScore);
				System.out.println(tempResult);
				System.out.println(tempLargestBeginsAtX);
				System.out.println(tempLargestBeginsAtY);
				System.out.println(tempLargestScore);
				System.out.println(tempLargestIsDown);
			}
		} else {
			boolean success = board.placeWord(largestWord, largestBeginsAtX, largestBeginsAtY, largestIsDown);
			if (!success) { //TODO SKIP CURRENT AND GET NEXT BEST
				System.out.println("SHIT3");
				System.out.println(largestWord);
				System.out.println(largestBeginsAtX);
				System.out.println(largestBeginsAtY);
				System.out.println(largestScore);
				System.out.println(largestIsDown);
			} else {
				result = new Word(largestWord, largestBeginsAtX, largestBeginsAtY, largestIsDown, largestScore);
			}
		}
		return result;
	}
	
	public boolean refreshCache() {
		words = wordJDBCTemplate.refreshCache();
		trie = new Trie(words);
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
	
	public void leftPart (String partialWord, Node node, int limit, AnchorSquare anchor, AnchorSquare start, boolean transponed) {
		
		extendRight(partialWord, node, anchor, start, transponed);
		Node newNode = new Node();
		
		if (limit > 0 && node != null) {
			
			Set<Character> children = node.getChildren().keySet();
			
			for (Character character : children) {
				if (hand.contains(character)) {
					hand.remove(new Character(character));
					newNode = node.getChild(character);
					if (start.getY() > 0 && crossCheck(character, start, transponed)) {
						AnchorSquare tempStart = new AnchorSquare();
						tempStart.setX(start.getX());
						tempStart.setY(start.getY()-1);
						leftPart(character + partialWord, newNode, limit-1, anchor, tempStart, transponed);
					}
					hand.add(character);
					
				} else if (hand.contains(new Character('*'))) {
					Set<Character> allLetters = lettervalues.keySet();
					for (Character character2 : allLetters) {
						hand.remove(new Character('*'));
						newNode = node.getChild(character2);
						if (start.getY() > 0 && crossCheck(character2, start, transponed)) {
							AnchorSquare tempStart = new AnchorSquare();
							tempStart.setX(start.getX());
							tempStart.setY(start.getY()-1);
							leftPart(character2 + partialWord, newNode, limit-1, anchor, tempStart, transponed);
						}
						hand.add(new Character('*'));
					}
				}
			}
		}
	}
	
	private void extendRight(String partialWord, Node node, AnchorSquare newSquare, AnchorSquare start, boolean transponed) {
		Node newNode = new Node();
		AnchorSquare nextSquare = new AnchorSquare();
		if (node != null) {
			
			
			if(newSquare.getY() == 15 || board.getSquare(newSquare.getX(), newSquare.getY()) == EMPTY) {
				if (trie.containsWord(partialWord) && node.isValid() &&
						(newSquare.getY() == 14 || board.getSquare(newSquare.getX(), newSquare.getY()+1) == EMPTY) &&
						!alreadyOnBoard(start, partialWord.toCharArray())) {
					sumWordValue(partialWord, start.getX(), start.getY(), true, transponed);
				}
				Set<Character> childNodes = node.getChildren().keySet();
				
				for (Character c : childNodes) {
					if (hand.contains(c) && crossCheck(c, newSquare, transponed)) {
						hand.remove(new Character(c));
						newNode = node.getChild(c);
						if (newSquare.getY() < 14) {
							nextSquare.setX(newSquare.getX());
							nextSquare.setY(newSquare.getY()+1);
							extendRight(partialWord+c, newNode, nextSquare, start, transponed);
						}
						hand.add(c);
					} else if (hand.contains('*')) {
						Set<Character> allLetters = lettervalues.keySet();
						for (Character c2: allLetters) {
							if (node.getChild(c2) != null && crossCheck(c2, newSquare, transponed)) {
								hand.remove(new Character('*'));
								newNode = node.getChild(c2);
								if (newSquare.getY() < 14) {
									nextSquare.setX(newSquare.getX());
									nextSquare.setY(newSquare.getY()+1);
									extendRight(partialWord+c2, newNode, nextSquare, start, transponed);
								}
								hand.add('*');
							}
						}
					}
				}
			} else if (newSquare.getY() < 15){
				Character letterOnBoard = board.getSquare(newSquare.getX(), newSquare.getY());
				
				if (node.getChild(letterOnBoard) != null) {
					nextSquare.setX(newSquare.getX());
					nextSquare.setY(newSquare.getY()+1);
					extendRight(partialWord+letterOnBoard, node.getChild(letterOnBoard), nextSquare, start, transponed);
				}	
			}
		}
	}
	
	private boolean crossCheck(Character c, AnchorSquare anchorSquare, boolean transponed) {
		
		String toCheck = c.toString();
		int x = anchorSquare.getX();
		int y = anchorSquare.getY();
		AnchorSquare crossStart = new AnchorSquare();
		if (x > 0 && board.getSquare(x-1, y) != EMPTY) {
			while (x>0 && board.getSquare(x-1, y) != EMPTY) {
				toCheck = board.getSquare(x-1, y) + toCheck;
				x--;
			}
			crossStart.setX(x);
			crossStart.setY(y);
		}
		
		x = anchorSquare.getX();
		if (x < 14 && board.getSquare(x+1, y) != EMPTY) {
			while (x < 14 &&board.getSquare(x+1, y) != EMPTY) {
				toCheck = toCheck + board.getSquare(x+1, y);
				x++;
			}
			
		}
		
		if (!trie.containsWord(toCheck) && toCheck.length() > 1) { //TODO
			return false;
		} else if (trie.containsWord(toCheck)) {
			//sumWordValue(toCheck, crossStart.getX(), crossStart.getY(), false, transponed);
		}
		
		/*toCheck = c.toString();
		if (y > 0 && board.getSquare(x, y-1) != EMPTY) {
			while (y > 0 && board.getSquare(x, y-1) != EMPTY) {
				toCheck = board.getSquare(x, y-1) + toCheck;
				y--;
			}
		}
		
		y = anchorSquare.getY();
		if (y<14 &&board.getSquare(x, y+1) != EMPTY) {
			while (y < 14 && board.getSquare(x, y+1) != EMPTY) {
				toCheck = toCheck + board.getSquare(x, y+1);
				y++;
			}
		}
		
		
		
		if (!trie.containsWord(toCheck) && toCheck.length() > 1) {
			return false;
		} else if (trie.containsWord(toCheck)) {
			System.out.println(toCheck);
			//System.out.println(sumWordValue(toCheck, crossStart.getX(), crossStart.getY(), false, transponed));
		}*/
		return true;
	}
	
	private int sumWordValue(String word, int x, int y, boolean lastCheck, boolean trans) {
		
		int result = 0;
		int wordMultiplier = 0;
		int letterMultiplier = 0;
		char[] wordCharArray = word.toCharArray();
		for (int i=0; i<wordCharArray.length; i++) {

			wordMultiplier += board.getWordMultiplier(x, y + i);
			if (board.getLetterMultiplier(x, y+i) != 0) {
				letterMultiplier += (board.getLetterMultiplier(x, y+i))*lettervalues.get(wordCharArray[i]);
			} else {
				letterMultiplier += lettervalues.get(new Character(wordCharArray[i]));
			}
			
			if (wordMultiplier != 0) {
				result = wordMultiplier * letterMultiplier;
			} else {
				result = letterMultiplier;
			}
			if (!lastCheck) {
				tempWordTotal += result;
				return tempWordTotal;
			} else if (result + tempWordTotal > largestScore) {
				largestBeginsAtX = x;
				largestBeginsAtY = y;
				largestScore = result + tempWordTotal;
				largestWord = word;
				largestIsDown = trans;
				tempWordTotal = 0;
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
		largestIsDown = false;
	}

}





















