package tomii;

import java.util.List;

public class Trie {
	private Node root = new Node('\0', "");
	
	public Trie (List<WordDTO> words) {
		for (WordDTO word : words) {
			addWord(word.getWord().toCharArray());
		}
	}
	
	public Node getRoot() {
		return root;
	}
	
	public void addWord (char[] word) {
		
		Node currentNode = root;
		for(int i=0; i<word.length; i++) {
			if (!currentNode.ifContainsChild(word[i])) {
				currentNode.addChild(new Node(word[i], currentNode.getPartialWord() + word[i]));
			}
			currentNode = currentNode.getChild(word[i]);
		}
		currentNode.setValidity(true);
	}
	
	public boolean contains(char[] word, boolean isSubString) {
		Node node = getNode(word);
		boolean containsSubString = false;
		boolean containsWord = false;
		
		containsSubString = isSubString && node != null;
		containsWord = !isSubString && node != null && node.isValid();
		
		return containsSubString || containsWord;
	}
	
	public boolean containsPrefix(String prefix) {
		return contains(prefix.toCharArray(), true);
	}
	
	public boolean containsWord(String word) {
		return contains(word.toCharArray(), false);
	}
	
	public Node getNode(char[] word) {
		Node currentNode = root;
		for (int i=0; i<word.length; i++) {
			currentNode = currentNode.getChild(word[i]);
			if (currentNode == null) {
				return null;
			}
		}
		return currentNode;
	}
}
