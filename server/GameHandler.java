package server;

import game.Player;
import game.ScoreList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/*
 * Handles the player movements
 */
public class GameHandler {
	//SingleTon
	private static GameHandler instance = null;
	public static GameHandler getInstance(){
		if(instance == null){
			instance = new GameHandler();
		}
		return instance;
	}
	
	private List<Player> players;
	private ScoreList scoreList;
	private String[][] level = {
		{ "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w",
			"w", "w", "w", "w", "w", "w", "w", "w" },
	{ "w", "e", "e", "e", "e", "e", "e", "e", "e", "w", "w", "e", "e",
			"e", "e", "e", "e", "e", "e", "w" },
	{ "w", "e", "w", "e", "e", "w", "e", "e", "w", "w", "w", "e", "w",
			"e", "e", "w", "e", "e", "w", "w" },
	{ "w", "e", "w", "e", "e", "w", "e", "e", "e", "w", "w", "e", "w",
			"e", "e", "w", "e", "e", "w", "w" },
	{ "w", "e", "e", "w", "e", "e", "e", "e", "e", "e", "e", "e", "e",
			"e", "e", "e", "e", "e", "e", "w" },
	{ "w", "e", "w", "e", "w", "e", "w", "e", "w", "e", "w", "e", "w",
			"e", "e", "w", "e", "e", "w", "w" },
	{ "w", "e", "w", "e", "e", "e", "e", "e", "w", "w", "w", "e", "w",
			"e", "e", "w", "e", "e", "w", "w" },
	{ "w", "e", "w", "e", "e", "e", "e", "e", "w", "e", "w", "e", "w",
			"e", "e", "w", "e", "e", "w", "w" },
	{ "w", "e", "e", "e", "w", "e", "w", "e", "e", "w", "e", "e", "w",
			"e", "e", "w", "e", "e", "e", "w" },
	{ "w", "e", "e", "e", "e", "e", "w", "e", "e", "w", "e", "e", "w",
			"e", "e", "w", "e", "e", "e", "w" },
	{ "w", "e", "w", "w", "e", "w", "w", "e", "e", "e", "e", "e", "e",
			"e", "e", "w", "e", "e", "w", "w" },
	{ "w", "e", "e", "w", "e", "w", "e", "e", "e", "e", "w", "e", "e",
			"e", "e", "w", "e", "e", "w", "w" },
	{ "w", "e", "e", "e", "e", "e", "e", "e", "e", "w", "w", "e", "w",
			"e", "e", "w", "e", "e", "w", "w" },
	{ "w", "e", "e", "e", "e", "e", "e", "e", "e", "e", "w", "e", "w",
			"e", "e", "w", "e", "e", "w", "w" },
	{ "w", "e", "e", "e", "e", "e", "e", "e", "e", "w", "e", "e", "e",
			"e", "e", "w", "e", "e", "w", "w" },
	{ "w", "e", "e", "w", "e", "e", "e", "e", "e", "e", "e", "e", "e",
			"e", "e", "e", "e", "e", "w", "w" },
	{ "w", "e", "e", "w", "e", "w", "w", "w", "e", "e", "w", "e", "w",
			"e", "e", "w", "w", "e", "w", "w" },
	{ "w", "e", "w", "e", "e", "e", "e", "e", "e", "w", "w", "e", "w",
			"e", "e", "e", "e", "e", "w", "w" },
	{ "w", "e", "e", "e", "w", "e", "e", "e", "w", "w", "e", "e", "w",
			"e", "e", "e", "e", "e", "e", "w" },
	{ "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w",
			"w", "w", "w", "w", "w", "w", "w", "w" }, };
	private ArrayList<String> spawns = new ArrayList<String>();
	
	/*
	 * Constructor to initialize the list of players
	 */
	public GameHandler() {
		players = new ArrayList<Player>();		
		calculateSpawns();
		createTreasures();
	}
	
	public void createTreasures(){
		Timer timer = new Timer();
		TimerTask timerTask = new TimerTask(){
			@Override
			public void run(){
				String token = getRandom();
				try{
					GameServer.getInstance().createTreasures(token);
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		};
		timer.schedule(timerTask, 20000, 20000);
	}
	
	
	public void calculateSpawns() { 
		for (int x = 0; x < level.length; x++) {
			for (int y = 0; y < level.length; y++) {
				if (level[x][y].equals("e")) {
					spawns.add(x +"#" + y);
				}
			}
		}
	}
	
	public String playerMoved(String token) {
		String[] tokens = token.split("#");
		String playerName = tokens[0];
		String direction = tokens[1];
		Player player = null;
		String pos;
		for (Player p : players) {
			if (p.getName().equals(playerName)) {
				player = p;
				break;
			}
		}
		player.setDirection(tokens[1]);
		int x = player.getXpos(),y = player.getYpos();
		int oldX = x,oldY = y;
		
		if (direction.equals("r")) {
			x = player.getXpos() + 1;
		};
		if (direction.equals("l")) {
			x = player.getXpos() - 1;
		};
		if (direction.equals("u")) {
			y = player.getYpos() - 1;
		};
		if (direction.equals("d")) {
			y = player.getYpos() + 1;
		};
		if (level[x][y].equals("w")) {
			player.subOnePoint();
			pos = "w:" + player.getName() + "#" + player.getPoint();
		} 
		else {
			player.addOnePoint();
//			scoreList.updateScoreOnScreenAll(); //TODO: Implement
			player.setXpos(x);
			player.setYpos(y);
			pos = "p:" + player.getName() + "#" + oldX + "#" + oldY + "#" + x + "#" + y + "#" + direction + "#" + player.getPoint();
		}
		return pos;
	}

	public List<Player> getPlayers() {
		return players;
	}
	
	public void setPlayers(List<Player> players) {
		this.players = players;
	}

	public ScoreList getScoreList() {
		return scoreList;
	}

	public void setScoreList(ScoreList scoreList) {
		this.scoreList = scoreList;
	}
	
	/**
	 * Adds a player to the game. 
	 * Player(s) will always start at the same position X,Y(5,7) - and facing the upwards direction.
	 * @param name
	 */
	public void addPlayer(String name) {
		Player player = new Player(name);	
		player.setDirection("u");
		String spawn = getRandom();
		String[] position = spawn.split("#");
		player.setXpos(Integer.parseInt(position[0]));
		player.setYpos(Integer.parseInt(position[1]));
		players.add(player);
	}
	/**
	 * Returns a given player token, with format: name#x#y#direction
	 * @param name
	 * @return
	 */
	public String getPlayerToken(String name){
		String result = "";
		for(Player p : players){
			if(p.getName().equals(name)){
				result = p.getName() + "#" + p.getXpos() + "#" + p.getYpos() + "#" + p.getDirection() + "#" + p.getPoint(); 
			}
		}
		return result;
	}
	
	/**
	 * Returns a token with format: name#x#y#direction for all players seperated by ¤
	 * @return the token
	 */
	public String getAllPlayerTokens(){
		//denne printer kun en spiller ud! Add metoden overskriver spillere....
		String result="";
		for(Player p : players){
			result += p.getName() + "#" + p.getXpos() + "#" + p.getYpos() + "#" + p.getDirection() + "#" + p.getPoint() + "¤";
		}
		System.out.println("reusltafter " + result );
		return result;
	}
	
	public String getRandom() {		
		Random random = new Random();	
		return spawns.get(random.nextInt(spawns.size()-1));	
	}
	

}
