package tomii;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RequestController {

	LoginService loginService = new LoginService();
	AdminService adminService = new AdminService();
	Set<String> adminTokens = new HashSet<String>();
	
	//keys are tokens which are generated on login
	HashMap<String, PlaySession> playSessions = new HashMap<String, PlaySession>();
	HashMap<String, ScoreHandler> scoreHandlers = new HashMap<String, ScoreHandler>();
    
    @RequestMapping("/refreshcache")
    public boolean refreshCache(@RequestParam String token) {
    	return playSessions.get(token).refreshCache();
    }
    
    @RequestMapping("/placeword")
    public boolean placeWord(@RequestParam Map<String, String> requestParams){
    	String word = requestParams.get("word");
    	int posX = Integer.parseInt(requestParams.get("x"));
    	int posY = Integer.parseInt(requestParams.get("y"));
    	boolean down = Boolean.valueOf(requestParams.get("down"));
    	String token = requestParams.get("token");
    	return playSessions.get(token).placeWord(word, posX, posY, down);
    }
    
    @RequestMapping("/login")
    public SessionDTO login(@RequestParam Map<String, String> requestParams) {
    	String user = requestParams.get("user");
    	String password = requestParams.get("password");
    	String toRemove = "";
    	for (Map.Entry<String, PlaySession> session : playSessions.entrySet()) {
			if (session.getValue().getUser().equals(user)) {
				toRemove = session.getKey();
			}
		}
    	
    	if (!toRemove.equals("")) {
    		playSessions.remove(toRemove);
    		scoreHandlers.remove(toRemove);
			if (adminTokens.contains(toRemove)) {
				adminTokens.remove(toRemove);
			}
    	}
    	
    	SessionDTO sessionDTO = loginService.login(user, password);
    	playSessions.put(sessionDTO.getToken(), new PlaySession(user));
    	scoreHandlers.put(sessionDTO.getToken(), new ScoreHandler(user));
    	if (sessionDTO.getType().equals("admin")) {
    		adminTokens.add(sessionDTO.getToken());
    	}
    	return sessionDTO;
    }
    
    @RequestMapping("/gettopscores")
    public List<HighScoreDTO> getTopScores(@RequestParam String token) {
    	return scoreHandlers.get(token).getHighScores();
    }
    
    @RequestMapping("/getownhighscore")
    public HighScoreDTO getOwnHighScore(@RequestParam String token) {
    	return scoreHandlers.get(token).getOwnHighScore();
    }
    
    @RequestMapping("/sethighscore")
    public boolean setHighScore(@RequestParam Map<String, String> requestParams) {
    	int score = Integer.parseInt(requestParams.get("score"));
    	String token = requestParams.get("token");
    	return scoreHandlers.get(token).updateHighScore(score);
    }
    
    @RequestMapping("/checklegitimacy")
    public boolean treetest(@RequestParam Map<String, String> requestParams) {
    	String word = requestParams.get("word");
    	String token = requestParams.get("token");
    	return playSessions.get(token).containsWord(word);
    }
    
    @RequestMapping("/sethand")
    public boolean setHand(@RequestParam Map<String, String> requestParams) {
    	String hand = requestParams.get("hand");
    	String token = requestParams.get("token");
    	return playSessions.get(token).setHand(hand);
    }
    
    @RequestMapping("/getbestword")
    public WordDTO getBestWord(@RequestParam String token) {
    	return playSessions.get(token).getBestWord();
    }
    
    @RequestMapping("/resetboard")
    public boolean resetBoard(@RequestParam String token) {
    	return playSessions.get(token).resetBoard();
    }
    
    @RequestMapping("/logout")
    public boolean logout(@RequestParam String token) {
    	if (playSessions.keySet().contains(token)) {
    		playSessions.remove(token);
    		scoreHandlers.remove(token);
    	} else {
    		return false;
    	}
    	return true;
    }
    
    @RequestMapping("/register")
    public boolean register(@RequestParam Map<String, String> requestParams) {
    	String user = requestParams.get("user");
    	String password = requestParams.get("password");
    	return loginService.register(user, password);
    }
    
    @RequestMapping("/admin/banuser")
    public boolean banUser(@RequestParam Map<String, String> requestParams){
    	String user = requestParams.get("user");
    	String token = requestParams.get("token");
    	if (!adminTokens.contains(token)) {
    		return false;
    	}
    	adminService.banUser(user);
    	return true;
    }
    
    @RequestMapping("/admin/resethighscore")
    public boolean resetHighScore(@RequestParam Map<String, String> requestParams) {
    	String user = requestParams.get("user");
    	String token = requestParams.get("token");
    	if (!adminTokens.contains(token)) {
    		return false;
    	}
    	adminService.resetHighScoreOfPlayer(user);
    	return true;
    }
    
    @RequestMapping("/admin/addword")
    public boolean addWord(@RequestParam Map<String, String> requestParams) {
    	
    	String word = requestParams.get("word");
    	String token = requestParams.get("token");
    	if (!adminTokens.contains(token)) {
    		return false;
    	}
    	adminService.addWord(word, playSessions.get(token).getUser());
    	playSessions.get(token).refreshCache();
    	return true;
    }
    
    @RequestMapping("/admin/deleteword")
    public boolean deleteWord(@RequestParam Map<String, String> requestParams) {
    	
    	String word = requestParams.get("word");
    	String token = requestParams.get("token");
    	if (!adminTokens.contains(token)) {
    		return false;
    	}
    	adminService.deleteWord(word);
    	playSessions.get(token).refreshCache();
    	return true;
    }
    
    @RequestMapping("/admin/testing")
    public void testing(@RequestParam String pass) {
    	String token = "test";
    	if (pass.equals("f7rzv3ge") && !adminTokens.contains("token")) {
			playSessions.put(token, new PlaySession("admin"));
			scoreHandlers.put(token, new ScoreHandler("admin"));
			adminTokens.add(token);
    	}
    }
    @RequestMapping("/status")
    public String getStatus() {
    	return "Server is up and running";
    }
    
}
