package server;

import game.Player;
import game.ScoreList;

import java.util.ArrayList;
import java.util.List;

/*
 * Handles the player movements
 */
public class GameHandler {
	private List<Player> players;
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
	private ScoreList scoreList;
	
	/*
	 * Constructor to initialize the list of players
	 */
	public GameHandler() {
		players = new ArrayList<Player>();
		
	}
	
	public String playerMoved(String token) {
		String[] tokens = token.split("#");
		String playerName = tokens[0];
		String direction = tokens[1];
		Player me = null;
		String pos = "You cannot move";
		for (Player p : players) {
			if (p.getName().equals(playerName)) {
				me = p;
				break;
			}
		}
		me.setDirection(tokens[1]);
		int x = me.getXpos(),y = me.getYpos();
		int oldX = x,oldY = y;
		
		if (direction.equals("r")) {
			x = me.getXpos() + 1;
		};
		if (direction.equals("l")) {
			x = me.getXpos() - 1;
		};
		if (direction.equals("u")) {
			y = me.getYpos() - 1;
		};
		if (direction.equals("d")) {
			y = me.getYpos() + 1;
		};
		if (level[x][y].equals("w")) {
			me.subOnePoint();
//			scoreList.updateScoreOnScreenAll(); //TODO: Implement
		} 
		else {
			me.addOnePoint();
//			scoreList.updateScoreOnScreenAll(); //TODO: Implement
			me.setXpos(x);
			me.setYpos(y);
			pos = "p:" + oldX + "#" + oldY + "#" + x + "#" + y + "#" + direction;
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
		players.add(player);
		player.setDirection("u");
		player.setXpos(5);
		player.setYpos(7);
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
				result = p.getName() + "#" + p.getXpos() + "#" + p.getYpos() + "#" + p.getDirection();
			}
		}
		return result;
	}
	
	/**
	 * Returns a token with format: name#x#y#direction for all players seperated by ¤
	 * @return the token
	 */
	public String getAllPlayerTokens(){
		String result = "";
		for(Player p : players){
			result = result + p.getName() + "#" + p.getXpos() + "#" + p.getYpos() + "#" + p.getDirection() + "¤";
		}
		return result;
	}
	
}
