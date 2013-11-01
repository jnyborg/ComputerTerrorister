package game;

import java.util.Random;

public class Chest {
	private int x, y;
	private Random random;
	private boolean isItem;
	private int numberOfCoins;
	private int itemChoice;
	private final int ITEM_GUN = 1, ITEM_MINE = 2;

	public Chest(int x, int y) {
		this.x = x;
		this.y = y;
		random = new Random();
		generateTreasure();

	}

	public void generateTreasure() {
		isItem = random.nextBoolean();
		if (isItem) {
			boolean isGun = random.nextBoolean();
			if (isGun) {
				itemChoice = ITEM_GUN;
			} else {
				itemChoice = ITEM_MINE;
			}
		} else {
			numberOfCoins = random.nextInt(100) + 10;
		}

	}

	/**
	 * Return a token for this chest. Looks like this: t:x#y#true#itemChoice\n
	 * or t:x#y#false#numberOfCoins\n Where itemChoice is 0 for gun, and 1 for
	 * mine.
	 * 
	 * @return
	 */
	public String getToken() {
		if (isItem)
			return "t:" + x + "#" + y + "#" + isItem + "#" + itemChoice + "\n";
		else
			return "t:" + x + "#" + y + "#" + isItem + "#" + numberOfCoins
					+ "\n";
	}

	public boolean isItem() {
		return isItem;
	}

	public int getNumberOfCoins() {
		return numberOfCoins;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getItemChoice() {
		return itemChoice;
	}

}
