package tomii;

import java.util.HashMap;
import java.util.Map;

public class Node {
	
	private Character ch;
	private String partialWord;
	private Map<Character, Node> children = new HashMap<Character, Node>();
	private boolean isValid;
	
	public Node() {
		
	}
	
	public Node(char ch, String partialWord) {
		this.ch = ch;
		this.partialWord = partialWord;
	}
	
	public boolean addChild(Node child) {
		
		if (children.containsKey(child.getChar())) {
			return false;
		}
		
		children.put(child.getChar(), child);
		return true;
	}
	
	public String getPartialWord() {
		return partialWord;
	}
	
	public Node getChild(char c) {
		return children.get(c);
	}
	
	public boolean isValid() {
		return isValid;
	}
	
	public void setValidity(boolean validity) {
		this.isValid = validity;
	}
	
	public char getChar() {
		return ch;
	}
	
	public Map<Character, Node> getChildren() {
		return children;
	}
	
	public boolean ifContainsChild(char ch) {
		return children.containsKey(ch);
	}
	

}
