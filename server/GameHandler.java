package server;

import game.Chest;
import game.Mine;
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
	// SingleTon
	private static GameHandler instance = null;
	private static Timer timer;
	private static int interval;

	public static GameHandler getInstance() {
		if (instance == null) {
			instance = new GameHandler();
		}
		return instance;
	}

	private static List<Player> players;
	private String[][] level = {
			{ "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w",
				"w", "w", "w", "w", "w", "w", "w" },
			{ "w", "e", "e", "e", "e", "e", "e", "e", "e", "e", "e", "e", "e",
					"e", "e", "e", "e", "e", "w", "w" },
			{ "w", "e", "w", "e", "w", "e", "w", "e", "w", "e", "w", "e", "w", //BOMBER MAN STYLE
					"e", "w", "e", "w", "e", "e", "w" },
			{ "w", "e", "e", "e", "e", "e", "e", "e", "e", "e", "e", "e", "e",
					"e", "e", "e", "e", "e", "w", "w" },
			{ "w", "e", "w", "e", "w", "e", "w", "e", "w", "e", "w", "e", "w", //BOMBER MAN STYLE
						"e", "w", "e", "w", "e", "e", "w" },
			{ "w", "e", "e", "e", "e", "e", "e", "e", "e", "e", "e", "e", "e",
					"e", "e", "e", "e", "e", "w", "w" },
			{ "w", "e", "w", "e", "w", "e", "w", "e", "w", "e", "w", "e", "w", //BOMBER MAN STYLE
						"e", "w", "e", "w", "e", "e", "w" },
			{ "w", "e", "e", "e", "e", "e", "e", "e", "e", "e", "e", "e", "e",
					"e", "e", "e", "e", "e", "w", "w" },
			{ "w", "e", "w", "e", "w", "e", "w", "e", "w", "e", "w", "e", "w", //BOMBER MAN STYLE
						"e", "w", "e", "w", "e", "e", "w" },
			{ "w", "e", "e", "e", "e", "e", "e", "e", "e", "e", "e", "e", "e",
					"e", "e", "e", "e", "e", "w", "w" },
			{ "w", "e", "w", "e", "w", "e", "w", "e", "w", "e", "w", "e", "w", //BOMBER MAN STYLE
						"e", "w", "e", "w", "e", "e", "w" },
			{ "w", "e", "e", "e", "e", "e", "e", "e", "e", "e", "e", "e", "e",
					"e", "e", "e", "e", "e", "w", "w" },
			{ "w", "e", "w", "e", "w", "e", "w", "e", "w", "e", "w", "e", "w", //BOMBER MAN STYLE
						"e", "w", "e", "w", "e", "e", "w" },
			{ "w", "e", "e", "e", "e", "e", "e", "e", "e", "e", "e", "e", "e",
					"e", "e", "e", "e", "e", "w", "w" },
			{ "w", "e", "w", "e", "w", "e", "w", "e", "w", "e", "w", "e", "w", //BOMBER MAN STYLE
						"e", "w", "e", "w", "e", "e", "w" },
			{ "w", "e", "e", "e", "e", "e", "e", "e", "e", "e", "e", "e", "e",
					"e", "e", "e", "e", "e", "w", "w" },
			{ "w", "e", "w", "e", "w", "e", "w", "e", "w", "e", "w", "e", "w", //BOMBER MAN STYLE
						"e", "w", "e", "w", "e", "e", "w" },
			{ "w", "e", "e", "e", "e", "e", "e", "e", "e", "e", "e", "e", "e",
					"e", "e", "e", "e", "e", "e", "w" },
			{ "w", "e", "w", "e", "w", "e", "w", "e", "w", "e", "w", "e", "w",
					"e", "w", "e", "w", "e", "w", "w" },
			{ "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w",
				"w", "w", "w", "w", "w", "w", "w" }, };

	private static ArrayList<String> spawns = new ArrayList<String>();
	private static ArrayList<Chest> chests = new ArrayList<Chest>();
	private static ArrayList<Mine> mines = new ArrayList<Mine>();

	/*
	 * Constructor to initialize the list of players
	 */
	public GameHandler() {
		players = new ArrayList<Player>();
		calculateSpawns();
		createTreasures();
		timer = new Timer();
		gameTimer();
	}

	public void createTreasures() {
		Timer timer = new Timer();
		TimerTask timerTask = new TimerTask() {
			@Override
			public void run() {
				String[] chestPosition = getRandomSpawn().split("#");
				Chest chest = new Chest(Integer.parseInt(chestPosition[0]),
						Integer.parseInt(chestPosition[1]));
				chests.add(chest);
				GameServer.getInstance().fireEvent(chest.getToken());
			}
		};
		timer.schedule(timerTask, 6000, 6000);
	}

	public void calculateSpawns() {
		for (int x = 0; x < level.length; x++) {
			for (int y = 0; y < level.length; y++) {
				if (level[x][y].equals("e")) {
					spawns.add(x + "#" + y);
				}
			}
		}
	}
	
	public void movePlayer(String moveData) {
		//Setup
		String[] data = moveData.split("#");
		Player player = getPlayer(data[0]);
		String direction = data[1];	
		int x = player.getXpos(), y = player.getYpos();
		int oldX = x, oldY = y;
		boolean isX = isX(direction), countUp = countUp(direction);
		Chest c;
		String result = "";
		
		//Check if player should turn or should move
		if (player.getDirection().equals(direction)) {
			//Figure out to which coordinate player i moving to, and save in temporaryPos
			if (isX) {
				if (countUp)
					x++;
				else
					x--;
			} else {
				if (countUp)
					y++;
				else 
					y--;
			}
			
			//Is player walking into wall, player or mine, do nothing
			if (level[x][y].equals("w") || isPlayerHere(x, y, player) != null || isMineHere(x, y) != null) {
				//Do nothing, result = ""
			} 
			//Check if player walks over chest
			else if ((c = isChestHere(x, y)) != null) {
				chests.remove(c);
				if (!c.isItem()) {
					player.addPoints(c.getNumberOfCoins());
				} else {
					player.giveWeapon(c.getItemChoice());
				}
				player.setXpos(x);player.setYpos(y);
				result = "p:chest:" + player.getName() + "#" + oldX + "#" + oldY + "#" + x
						+ "#" + y + "#" + direction + "#" + player.getPoint();
			} 
			//Move player as normal
			else {
				result = "p:" + player.getName() + "#" + oldX + "#" + oldY + "#" + x + "#" + y + "#" + direction;
				player.setXpos(x);player.setYpos(y);
			}
				
		} 
		//Turn player
		else {
			player.setDirection(direction);
			result = "p:" + player.getName() + "#" + oldX + "#" + oldY + "#" + x
					+ "#" + y + "#" + direction + "#" + player.getPoint();
		}
		
		GameServer.getInstance().fireEvent(result+"\n");	
		
	}

	public Player isPlayerHere(int x, int y, Player me) {
		Player result = null;
		for (Player p : players) {
			if (p != me) {
				if (p.getXpos() == x && p.getYpos() == y) {
					result = p;
				}
			}
		}
		return result;
	}
	
	public Mine isMineHere(int x, int y) {
		Mine m = null;
		for (Mine mine : mines) {
			if (mine.getX() == x && mine.getY() == y) {
				m = mine;
			}
		}
		return m;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public void setPlayers(List<Player> players) {
		this.players = players;
	}
	
	public boolean isX(String direction) {
		boolean isX = false;
		if (direction.equals("r") || direction.equals("l")) {
			isX = true;		
		} else if (direction.equals("u") || direction.equals("d")) {
			isX = false; 
		}
		return isX;
	}
	
	public boolean countUp(String direction) {
		boolean countUp = false;
		if (direction.equals("r") || direction.equals("d")) {
			countUp = true;		
		} else if (direction.equals("l") || direction.equals("u")) {
			countUp = false; 
		}
		return countUp;
	}

	/**
	 * Adds a player to the game. Player(s) will always start at the same
	 * position X,Y(5,7) - and facing the upwards direction.
	 * 
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
	 * 
	 * @param name
	 * @return
	 */
	public String getPlayerToken(String name) {
		String result = "";
		for (Player p : players) {
			if (p.getName().equals(name)) {
				result = p.getName() + "#" + p.getXpos() + "#" + p.getYpos()
						+ "#" + p.getDirection() + "#" + p.getPoint();
			}
		}
		return result;
	}

	/**
	 * Returns a token with format: name#x#y#direction for all players seperated
	 * by ¤
	 * 
	 * @return the token
	 */
	public static String getGameData() {
		String result = "newgame:";
		for (Player p : players) {
			result += p.getName() + "#" + p.getXpos() + "#" + p.getYpos() + "#"
					+ p.getDirection() + "#" + p.getPoint() + "¤";
		}
		result += "%c:";
		for (Chest c : chests) {
			result += c.getX()+"#"+c.getY()+"¤";
		}
		result += "%m:";
		for (Mine m : mines) {
			result += m.getX() + "#" + m.getY() + "¤";
		}
		return result;
	}

	public static String getRandomSpawn() {
		Random random = new Random();
		return spawns.get(random.nextInt(spawns.size() - 1));
	}

	public Chest isChestHere(int x, int y) {
		for (Chest c : chests) {
			if (c.getX() == x && c.getY() == y) {
				return c;
			}
		}
		return null;
	}

	public void useWeapon(String playerName) {
		String token = null;
		Player player = getPlayer(playerName);
		int x = player.getXpos();
		int y = player.getYpos();
		String direction = player.getDirection();
		if (player.getItem() == 0) {
			Player playerHit;
			if ((playerHit = useMelee(x, y, direction)) != null) {
				token = "action:melee:" + playerHit.getName() + "#" + playerHit.getPoint();
			}
		} else if (player.getItem() == 1) {
			player.useWeapon();
			token = calculateBeams(x, y, direction);
		} else if (player.getItem() == 2) {
			Mine m = useMine(x, y, direction, player);
			if (m != null){
				token = "action:mine:" + m.getX() + "#" + m.getY();
				player.useWeapon();
				m.startTimer();
			} 
		}
		GameServer.getInstance().fireEvent(token+"\n");

	}

	private Player useMelee(int x, int y, String direction) {
		Player playerHit = null;
		if (direction.equals("r")) {
			if ((playerHit = isPlayerHere(x + 1, y, null)) != null) {
				playerHit.subOnePoint();
			}

		} else if (direction.equals("l")) {
			if ((playerHit = isPlayerHere(x - 1, y, null)) != null) {
				playerHit.subOnePoint();
			}
		} else if (direction.equals("u")) {
			if ((playerHit = isPlayerHere(x, y - 1, null)) != null) {
				playerHit.subOnePoint();
			}
		} else if (direction.equals("d")) {
			if ((playerHit = isPlayerHere(x, y + 1, null)) != null) {
				playerHit.subOnePoint();
			}
		}
		return playerHit;

	}
	
	public String calculateBeams(int x, int y, String direction) {
		int xPos = x, yPos = y;
		boolean countUp = false, isX = false;
		if (direction.equals("r")) {
			isX = true; countUp = true;
		} else if (direction.equals("l")) {
			isX = true; countUp = false;
		} else if (direction.equals("u")) {
			isX = false; countUp = false;
		} else if (direction.equals("d")) {
			isX = false; countUp = true;
		}
		boolean hitSomething = false; Player playerHit; Chest chestHit; String result = ""; int hitXY;
		while (!hitSomething) {
			if (isX) {
				if (countUp) {
					xPos++;
					hitXY = xPos-1;
				} else {
					xPos--;
					hitXY = xPos+1;
				}
			} else {
				if (countUp) {
					yPos++;
					hitXY = yPos-1;
				} else {
					yPos--;
					hitXY = yPos+1;
				}
			}
			if((playerHit = isPlayerHere(xPos, yPos, null)) != null) {
				playerHit.subPoints(20);
				result = "action:gun:p:" + direction + "#" + x + "#" + y + "#" + hitXY + "#" + playerHit.getName() + "#" + playerHit.getPoint();
				hitSomething = true;
			} else if ((chestHit = isChestHere(xPos, yPos)) != null) {
				chests.remove(chestHit);
				result = "action:gun:c:" + direction + "#" + x + "#" + y + "#" + hitXY + "#" + xPos + "#" + yPos;
				hitSomething = true;
			} else if (level[xPos][yPos].equals("w")) {
				result = "action:gun:w:" + direction + "#" + x + "#" + y + "#" + hitXY + "#" + "" + "#" + "";
				hitSomething = true;
			} 
			
		}
		return result;	
		
	}
	
	public void detonateMine(Mine mine) {
		String token = "";
		int x = mine.getX();
		int y = mine.getY();
		token += calculateBeams(x, y, "r") + "¤";
		token += calculateBeams(x, y, "l") + "¤";
		token += calculateBeams(x, y, "u") + "¤";
		token += calculateBeams(x, y, "d");
		mines.remove(mine);
		GameServer.getInstance().fireEvent(token + "¤action:boom:"+x+"#"+y + "\n");
		
	}

	private Mine useMine(int x, int y, String direction, Player player) {
		int xPos = x; int yPos = y;
		Mine m = null;
		if (direction.equals("r")) {
			xPos++;
			if(isPlayerHere(xPos, y , null) == null && isChestHere(xPos, yPos) == null && level[xPos][yPos] != "w"){
				m = new Mine(player, xPos, yPos);
				mines.add(m);
			} 

		} else if (direction.equals("l")) {
			xPos--;
			if(isPlayerHere(xPos, y , null) == null && isChestHere(xPos, yPos) == null && level[xPos][yPos] != "w"){
				m = new Mine(player, xPos, yPos);
				mines.add(m);
			} 

		} else if (direction.equals("u")) {
			yPos--;
			if(isPlayerHere(x, yPos , null) == null && isChestHere(xPos, yPos) == null && level[xPos][yPos] != "w"){
				m = new Mine(player, xPos, yPos);
				mines.add(m);
			} 

		} else if (direction.equals("d")) {
			yPos++;
			if(isPlayerHere(x, yPos , null) == null && isChestHere(xPos, yPos) == null && level[xPos][yPos] != "w"){
				m = new Mine(player, xPos, yPos);
				mines.add(m);
			} 
		}
		
		
		return m;
	}

	public Player getPlayer(String playerName) {
		Player player = null;
		for (Player p : players) {
			if (p.getName().equals(playerName)) {
				player = p;
				break;
			}
		}
		return player;
	}
	
	public static void gameTimer() {
		int delay = 1000;
		int period = 1000;
		timer.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				setInterval();
				GameServer.getInstance().fireEvent("time:"+interval + "\n");
				
			}
		}, delay, period);
	}
	
	private static final int setInterval() {
		if (interval == 0) {
			interval = 120;
			chests.clear();
			mines.clear();
			resetGame();
		}
		return --interval;
	}
	
	public static void resetGame() {
		chests.clear();
		mines.clear();
		for (Player p : players) {
			p.setPoint(0);
			String[] result = getRandomSpawn().split("#");
			p.setXpos(Integer.parseInt(result[0]));
			p.setYpos(Integer.parseInt(result[1]));
		}
		GameServer.getInstance().fireEvent(getGameData() + "\n");
	}

}
