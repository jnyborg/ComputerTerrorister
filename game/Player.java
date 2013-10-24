package game;

public class Player {
String name;
int xpos;
int ypos;
int point;
String direction;

public Player (String name) {
	this.name = name;
	xpos = 5;
	ypos = 7;
	point = 0;
	direction = "up";
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

String ToString () {
	return name + "   " + point;
}
void addOnePoint() {
	
	point ++;
}

void subOnePoint() {
	point --;
}
}
