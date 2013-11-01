package game;

import java.util.Random;

public class Mine {
	private Player player;
	private Random random;
	private int x,y;
	private int damage;
	
	
	public Mine(Player player, int x, int y){
		this.player=player;
		this.x=x;
		this.y=y;
		random = new Random();
		damage = random.nextInt(80) + 20;
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
