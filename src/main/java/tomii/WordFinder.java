package tomii;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;	
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class WordFinder {

	List<Word> words = new Vector<Word>();
	ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
	WordJDBCTemplate wordJDBCTemplate = (WordJDBCTemplate)context.getBean("WordJDBCTemplate");
	Map<String, Integer> lettervalues = new HashMap<String, Integer>();
	
	Trie trie;
	
	public WordFinder() {
		lettervalues.put("a", 1);
		lettervalues.put("b", 3);
		lettervalues.put("c", 3);
		lettervalues.put("d", 2);
		lettervalues.put("e", 1);
		lettervalues.put("f", 4);
		lettervalues.put("g", 2);
		lettervalues.put("h", 4);
		lettervalues.put("i", 1);
		lettervalues.put("j", 8);
		lettervalues.put("k", 5);
		lettervalues.put("l", 1);
		lettervalues.put("m", 3);
		lettervalues.put("n", 1);
		lettervalues.put("o", 1);
		lettervalues.put("p", 3);
		lettervalues.put("q", 10);
		lettervalues.put("r", 1);
		lettervalues.put("s", 1);
		lettervalues.put("t", 1);
		lettervalues.put("u", 1);
		lettervalues.put("v", 1);
		lettervalues.put("w", 4);
		lettervalues.put("x", 8);
		lettervalues.put("y", 4);
		lettervalues.put("z", 10);
	}
	
	public List<Word> getMatches(String regex, String[] hand, String linetype) {
		
		regex = regex.replaceAll("\\.", ".?");
		regex = regex.replaceAll("_", "[a-zA-Z]{1}");
		Map <String, Integer> handmap = new HashMap<String, Integer>();
		for (String letter : hand) {
			if (handmap.containsKey(letter)) {
				handmap.replace(letter, handmap.get(letter)+1);
			} else {
				handmap.put(letter, 1);
			}
		}
		
		Pattern p = Pattern.compile(regex);
		List<Word> results = new ArrayList<Word>();
		for (Word word : words) {
			Matcher m = p.matcher(word.getWord());
			if (m.matches() && containsOnlyLettersFromHand(word.getWord(), hand)) {
				results.add(word);
			}
		}
		return results;
	}
	
	public Word getBestMatch(String regex, String[] hand, String linetype) {
		List<Word> results = getMatches(regex, hand, linetype);
		
		if (results.size()==0) {
			return null;
		}
		
		Word best = results.get(0);
		int maxvalue = 0;
		for (Word word : results) {
			int actualvalue = 0;
			for (int i=0; i<word.getWord().length(); i++) {
				actualvalue += lettervalues.get(String.valueOf(word.getWord().charAt(i)));
				
			}
			if (actualvalue > maxvalue) {
				maxvalue = actualvalue;
				best = word;
			}
		}
		
		;
		return best;
	}
	
	public void refreshCache() {
		words = wordJDBCTemplate.refreshCache();
		trie = new Trie(words);
		System.out.println("done");
	}
	
	private boolean containsOnlyLettersFromHand(String word, String[] hand) {
	    return Arrays.stream(hand).parallel().allMatch(word::contains);
	}
	
	public boolean contains(String string) {
		return trie.containsWord(string);
	}
}
