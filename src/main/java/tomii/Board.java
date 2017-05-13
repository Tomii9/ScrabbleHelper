package tomii;

public class Board {
	
	int[][] wordMultipliers;
	int[][] letterMultipliers;
	char[][] board;
	
	public Board() {
		wordMultipliers = new int[][]{	{3, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 3},
	 			   						{0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0},
	 			   						{0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0},
	 			   						{0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0},
	 			   						{0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0},
	 			   						{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
	 			   						{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
	 			   						{3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3},
	 			   						{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
	 			   						{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
	 			   						{0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0},
	 			   						{0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0},
	 			   						{0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0},
	 			   						{0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0},
	 			   						{3, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 3}};
		
		letterMultipliers = new int[][]{{0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0},
										{0, 0, 0, 0, 0, 3, 0, 0, 0, 3, 0, 0, 0, 0, 0},
										{0, 0, 0, 0, 0, 0, 2, 0, 2, 0, 0, 0, 0, 0, 0},
										{2, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 2},
										{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
										{0, 3, 0, 0, 0, 3, 0, 0, 0, 3, 0, 0, 0, 3, 0},
										{0, 0, 2, 0, 0, 0, 2, 0, 2, 0, 0, 0, 2, 0, 0},
										{0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0},
										{0, 0, 2, 0, 0, 0, 2, 0, 2, 0, 0, 0, 2, 0, 0},
										{0, 3, 0, 0, 0, 3, 0, 0, 0, 3, 0, 0, 0, 3, 0},
										{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
										{2, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 2},
										{0, 0, 0, 0, 0, 0, 2, 0, 2, 0, 0, 0, 0, 0, 0},
										{0, 0, 0, 0, 0, 3, 0, 0, 0, 3, 0, 0, 0, 0, 0},
										{0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0}};
		
		board = new char[15][15];
	}
	
	public char getSquare(int x, int y) {
		return board[x][y];
	}
	
	public char[][] getBoard() {
		return board;
	}
	
	public int getWordMultiplier(int x, int y) {
		return wordMultipliers[x][y];
	}
	
	public int getLetterMultiplier(int x, int y) {
		return letterMultipliers[x][y];
	}
	
	public boolean placeWord(String word, int posX, int posY, boolean down) {
		
		char[] boardSave = new char[word.length()];
		String sameWord = "";
		
		if (down && posX+word.length()<=15) {
				
			for (int i=0;i<word.length(); i++) {
				if (isOverWrite(posX+i, posY, word.charAt(i))) {
					for (int k=0; k<i; k++) {
						board[posX+k][posY] = boardSave[k];
					}
					return false;
				}
				boardSave[i] = board[posX+i][posY]; 
				sameWord = sameWord.concat(String.valueOf(board[posX+i][posY]));
				board[posX+i][posY]=word.charAt(i);
			}
			
			if (word.equals(sameWord)) {
				return false;
			}
			return true;
			
		} else if (!down && posY+word.length()<=15) {
			
			for (int j=0; j<word.length(); j++) {
				
				
				/* For some Reason cloning the board does not work, thus approach was chosen.
				 * on every letter placement the original is being saved. In case of an overWrite
				 * the word gets rolled back
				 */
				if (isOverWrite(posX, posY+j, word.charAt(j))) {
					for (int k=0; k<j; k++) {
						board[posX][posY+k] = boardSave[k];
					}
					return false;
				}
				boardSave[j] = board[posX][posY+j];
				sameWord = sameWord.concat(String.valueOf(board[posX][posY+j]));
				board[posX][posY+j]=word.charAt(j);
			}
			
			if (word.equals(sameWord)) {
				return false;
			}
			return true;
		}
		return false;
	}
	
	private boolean isOverWrite(int posX, int posY, char c) {
		char ch = board[posX][posY];
		return ch != '\u0000' && ch != c;
	}
	
	public void transponeBoard() {
		char[][] tmp = new char[15][15];
		for(int i = 0; i < 15; i++) {
			for(int j = 0; j < 15; j++) {
				tmp[j][i] = board[i][j];
			}
		}
		board = tmp;
	}
}
