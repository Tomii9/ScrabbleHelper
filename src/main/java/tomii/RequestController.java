package tomii;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RequestController {

	WordFinder wordFinder = new WordFinder();
	
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
    public void refreshCache() {
    	wordFinder.refreshCache();
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
