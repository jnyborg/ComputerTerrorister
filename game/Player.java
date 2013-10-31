package game;

public class Player {
	String name;
	int xpos;
	int ypos;
	int point;
	String direction;
	int item;
	final int ITEM_MELEE = 0, ITEM_GUN = 1, ITEM_MINE = 2;
	int ammonition;
	

	public Player(String name) {
		this.name = name;
		xpos = 5;
		ypos = 7;
		point = 0;
		direction = "u";
		item = ITEM_MELEE;
		ammonition = 0;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public int getPoint() {
		return point;
	}

	public int getXpos() {
		return xpos;
	}

	public void setXpos(int xpos) {
		this.xpos = xpos;
	}

	public int getYpos() {
		return ypos;
	}

	public void setYpos(int ypos) {
		this.ypos = ypos;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String ToString() {
		return name + "   " + point;
	}

	public void addOnePoint() {
		point++;
	}

	public void subOnePoint() {
		point--;
	}
	
	public void addPoints(int points){
		point= point + points;
	}
	
	public void subPoints(int points){
		point= point - points;
	}

	public void giveWeapon(int itemChoice) {
		if (itemChoice == ITEM_GUN) {
			item = ITEM_GUN;
			ammonition = 10;
		} else if (itemChoice == ITEM_MINE) {
			item = ITEM_MINE;
			ammonition = 1;
		}
	}
	
	public void useWeapon() {
		if (ammonition != 0) {
			ammonition--;
		} else {
			item = ITEM_MELEE;
		}		
		
	}
	
	public int getItem() {
		return item;
	}
}
