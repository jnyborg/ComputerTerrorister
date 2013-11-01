package game;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import server.GameHandler;

public class Mine {
	private Player player;
	private Random random;
	private int x,y;
	private int damage;
	private Mine mine;
	
	
	public Mine(Player player, int x, int y){
		this.player=player;
		this.x=x;
		this.y=y;
		random = new Random();
		damage = random.nextInt(80) + 20;
		mine = this;
	}
	
	public void startTimer() {
		new Timer().schedule(new TimerTask() {
			
			@Override
			public void run() {
				GameHandler.getInstance().detonateMine(mine);
				System.out.println("detonating...");
				
			}
		}, 5000);
	}
	
	public Player getMinePlayer() {
		return player;
	}
	public Random getRandom() {
		return random;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public int getDamage() {
		return damage;
	}
	
	
	
}
