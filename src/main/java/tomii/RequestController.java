package tomii;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RequestController {

	PlaySession playSession = new PlaySession("Admin");
	ScoreHandler scoreHandler = new ScoreHandler("Admin");
    
    @RequestMapping("/refreshcache")
    public boolean refreshCache() {
    	return playSession.refreshCache();
    }
    @RequestMapping("/placeword")
    public boolean placeWord(@RequestParam Map<String, String> requestParams){
    	String word = requestParams.get("word");
    	int posX = Integer.parseInt(requestParams.get("x"));
    	int posY = Integer.parseInt(requestParams.get("y"));
    	boolean down = Boolean.valueOf(requestParams.get("down"));
    	return playSession.placeWord(word, posX, posY, down);
    }
    
    @RequestMapping("/admin/banuser")
    public void banUser(@RequestParam String user){
    	
    }
    
    @RequestMapping("/getownhighscore")
    public int getHighScores(@RequestParam String user) {
    	return 0;
    }
    
    @RequestMapping("/gettopscores")
    public List<HighScoreDTO> getTopScores() {
    	return scoreHandler.getHighScores();
    }
    
    @RequestMapping("/sethighscore")
    public void setHighScore(@RequestParam int score) {
    	
    }
    
    @RequestMapping("/checklegitimacy")
    public boolean treetest(@RequestParam String word) {
    	return playSession.containsWord(word);
    }
    
    @RequestMapping("/sethand")
    public boolean setHand(@RequestParam String hand) {
    	return playSession.setHand(hand);
    }
    
    @RequestMapping("/getbestword")
    public WordDTO getBestWord() {
    	return playSession.getBestWord();
    }
    
    @RequestMapping("/syncboard")
    public char[][] syncBoard() {
    	return playSession.getBoard();
    }
    
    @RequestMapping("/resetboard")
    public boolean resetBoard() {
    	return playSession.resetBoard();
    }
    
}
