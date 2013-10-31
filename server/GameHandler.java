package server;

import game.Chest;
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
	private ArrayList<Chest> chests = new ArrayList<Chest>();
	
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
				String[] chestPosition = getRandomSpawn().split("#");
				Chest chest = new Chest(Integer.parseInt(chestPosition[0]), Integer.parseInt(chestPosition[1]));
				chests.add(chest);
				try{
					GameServer.getInstance().addTreasure(chest.getToken());
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
		int x = player.getXpos(),y = player.getYpos();
		int oldX = x,oldY = y;
		boolean playerAhead = false;
		if(player.getDirection().equals(direction)){
			if (direction.equals("r")) {
				x = player.getXpos() + 1;
				if(isPlayerHere(x, oldY, player)){
					playerAhead = true;
				}
				//todo
				//isPowerUpHere();
			};
			if (direction.equals("l")) {
				x = player.getXpos() - 1;
				if(isPlayerHere(x, oldY, player)){
					playerAhead = true;
				}
			};
			if (direction.equals("u")) {
				y = player.getYpos() - 1;
				if(isPlayerHere(oldX, y, player)){
					playerAhead = true;
				}
			};
			if (direction.equals("d")) {
				y = player.getYpos() + 1;
				if(isPlayerHere(oldX, y, player)){
					playerAhead = true;
				}
			};
			if(!playerAhead){
				//no player ahead of you, youre free to move around unless wall is ahead
				if (level[x][y].equals("w")) {
					player.subOnePoint();
					pos = "w:" + player.getName() + "#" + player.getPoint();
				} 
				else {
					System.out.println("addOnePoint");
					player.addOnePoint();
					player.setXpos(x);
					player.setYpos(y);
					pos = "p:" + player.getName() + "#" + oldX + "#" + oldY + "#" + x + "#" + y + "#" + direction + "#" + player.getPoint();
				}
			}else{
				//player is ahead of you, you cant move
				pos = "p:" + player.getName() + "#" + oldX + "#" + oldY + "#" + oldX + "#" + oldY + "#" + player.getDirection() + "#" + player.getPoint();
			}
		} else{
			//used for turning player if not same direction as input
			player.setDirection(tokens[1]);
			pos = "p:" + player.getName() + "#" + oldX + "#" + oldY + "#" + x + "#" + y + "#" + direction + "#" + player.getPoint();
		}
		return pos;
	}
	
	public boolean isPlayerHere(int x, int y, Player me){
		boolean isHere = false;
		ArrayList<Player> tempPlayers = new ArrayList<Player>();
		for(Player p: players){
			if(p.getName() != me.getName()){
				tempPlayers.add(p);
			}
		}
		for(Player p: tempPlayers){
			if(p.getXpos() == x && p.getYpos() == y){
				isHere = true;
			}
		}
		return isHere;
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
		String spawn = getRandomSpawn();
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
	
	public String getRandomSpawn() {		
		Random random = new Random();	
		return spawns.get(random.nextInt(spawns.size()-1));	
	}
	
	public Chest isChestHere(int x, int y) {
		for (Chest c : chests) {
			if (c.getX() == x && c.getY() == y) {
				return c;
			}
		}
		return null;
	}
	

}
