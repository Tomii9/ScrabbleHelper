package tomii;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RequestController {

	WordFinder wordFinder = new WordFinder();
	Board board = new Board();
	
    @RequestMapping("/getbestmatch")
    public Word getWord(@RequestParam Map<String,String> requestParams) {
    	String regex = requestParams.get("regex");
    	String letters = requestParams.get("letters");
    	String linetype = requestParams.get("linetype");
    	String[] hand = letters.split("(?!^)");
        return wordFinder.getBestMatch(regex, hand, linetype);
    }
    
    @RequestMapping("/getmatches")
    public List<Word> getWords(@RequestParam Map<String,String> requestParams) {
    	String regex = requestParams.get("regex");
    	String letters = requestParams.get("letters");
    	String linetype = requestParams.get("linetype");
    	String[] hand = letters.split("(?!^)");
    	return wordFinder.getMatches(regex, hand, linetype);
    }
    
    @RequestMapping("/refreshcache")
    public boolean refreshCache() {
    	return wordFinder.refreshCache();
    }
    @RequestMapping("/placeword")
    public boolean placeWord(@RequestParam Map<String, String> requestParams){
    	String word = requestParams.get("word");
    	int posX = Integer.parseInt(requestParams.get("x"));
    	int posY = Integer.parseInt(requestParams.get("y"));
    	boolean down = Boolean.valueOf(requestParams.get("down"));
    	return board.placeWord(word, posX, posY, down);
    }
    
    @RequestMapping("/admin/banuser")
    public void banUser(@RequestParam String user){
    	
    }
    
    @RequestMapping("/gethighscore")
    public int getHighScore(@RequestParam String user) {
    	return 0;
    }
    
    @RequestMapping("/sethighscore")
    public void setHighScore(@RequestParam int score) {
    	
    }
    
    @RequestMapping("/treetest")
    public boolean treetest(@RequestParam String word) {
    	
    	return wordFinder.contains(word);
    }
    
}
