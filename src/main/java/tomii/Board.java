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
		
		char[][] boardSave = board.clone();
		String sameWord = "";
		
		if (down && posX+word.length()<=15) {
				
			for (int i=0;i<word.length(); i++) {
				if (isOverWrite(posX+i, posY, word.charAt(i))) {
					board = boardSave;
					return false;
				}
				sameWord = sameWord.concat(String.valueOf(board[posX+i][posY]));
				board[posX+i][posY]=word.charAt(i);
			}
			
			if (word.equals(sameWord)) {
				return false;
			}
			
			print();
			return true;
			
		} else if (!down && posY+word.length()<=15) {
			
			for (int j=0; j<word.length(); j++) {
				
				if (isOverWrite(posX, posY+j, word.charAt(j))) {
					board = boardSave;
					return false;
				}
				sameWord = sameWord.concat(String.valueOf(board[posX][posY+j]));
				board[posX][posY+j]=word.charAt(j);
			}
			
			if (word.equals(sameWord)) {
				return false;
			}
			
			print();
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
	
	public void print() {
		for(int i = 0; i < 15; i++) {
			for(int j = 0; j < 15; j++) {
				if (board[i][j] == '\n') {
					System.out.println("0 ");
				}
				System.out.print(board[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();
	}
}
