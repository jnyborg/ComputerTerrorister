package game;

import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

import client.GameClient;
import server.GameServer;

public class Screen extends JFrame {
	private JLabel[][] labels = new JLabel[20][20];
	private ArrayList<Timer> timers = new ArrayList<Timer>();
	
	// Draws the map for the client side
	private String[][] level = { // put in GameHandler later
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

	public Screen() {
		super("Computer Terrorister");

		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setLocation(100,0);
		this.setSize(500, 500);
		this.setResizable(true);
		this.setLayout(new GridLayout(20, 20, 0, 0));
		draw();
		this.setAlwaysOnTop(true);
		this.setVisible(true);
		this.addWindowListener(new WindowEventHandler());
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}
	
	/*
	 * Moves player on screen
	 */
	public void movePlayerOnScreen(int oldX, int oldY, int x, int y,
			String playerDirection) {

		labels[oldX][oldY].setIcon(new ImageIcon("./Image/Gulv2.png"));

		if (playerDirection.equals("r")) {
			labels[x][y].setIcon(new ImageIcon("./Image/Helthoejre.png"));
		}
		;
		if (playerDirection.equals("l")) {
			labels[x][y].setIcon(new ImageIcon("./Image/Heltvenstre.png"));
		}
		;
		if (playerDirection.equals("u")) {
			labels[x][y].setIcon(new ImageIcon("./Image/HeltOp.png"));
		}
		;
		if (playerDirection.equals("d")) {
			labels[x][y].setIcon(new ImageIcon("./Image/HeltNed.png"));
		}
		;
	}

	/**
	 * Draw a player for the first time.
	 * 
	 * @param tokens
	 */
	public void drawPlayer(int x, int y, String playerDirection) {

		if (playerDirection.equals("r")) {
			labels[x][y].setIcon(new ImageIcon("./Image/Helthoejre.png"));
		}
		;
		if (playerDirection.equals("l")) {
			labels[x][y].setIcon(new ImageIcon("./Image/Heltvenstre.png"));
		}
		;
		if (playerDirection.equals("u")) {
			labels[x][y].setIcon(new ImageIcon("./Image/HeltOp.png"));
		}
		;
		if (playerDirection.equals("d")) {
			labels[x][y].setIcon(new ImageIcon("./Image/HeltNed.png"));
		}
		;

	}
	
	public void removePlayer(Integer x, Integer y){
			labels[x][y].setIcon(new ImageIcon("./Image/Gulv2.png"));
	}
	
	public void drawTreasure(int x, int y) {
		labels[x][y].setIcon(new ImageIcon("./Image/Treasure.png"));
	}
	
	public void fireGun(String direction, int oldX, int oldY, int hitXY){
		ArrayList<String> beams = new ArrayList<String>();
		
		/*
		 * Shooting in the right direction
		 */
		if(direction.equals("r")){
			for(int i = (oldX+1); i <= hitXY; i++){
				if(i == (oldX+1)){
					labels[oldX+1][oldY].setIcon(new ImageIcon("./Image/ildHoejre.png"));
				}else if(i == hitXY){
					labels[hitXY][oldY].setIcon(new ImageIcon("./Image/ildModMurOest.png"));
				}else{
					labels[i][oldY].setIcon(new ImageIcon("./Image/ildVandret.png"));
				}
				beams.add(i + "," + oldY);
			}
			deleteBeam(beams);
			
			/*
			 * Shooting in the left direction
			 */
		}else if(direction.equals("l")){
			for(int i = (oldX-1); i >= hitXY; i--){
				if(i == (oldX-1)){
					labels[oldX-1][oldY].setIcon(new ImageIcon("./Image/ildVenstre.png"));
				}else if(i == hitXY){
					labels[hitXY][oldY].setIcon(new ImageIcon("./Image/ildModMurVest.png"));
				}else{
					labels[i][oldY].setIcon(new ImageIcon("./Image/ildVandret.png"));
				}
				beams.add(i +"," + oldY);
			}
			deleteBeam(beams);
			
			/*
			 * Shooting in the upwards direction
			 */
		}else if(direction.equals("u")){
			for(int i = (oldY-1); i >= hitXY; i--){
				if(i == (oldY-1)){
					labels[oldX][oldY-1].setIcon(new ImageIcon("./Image/ildOp.png"));
				}else if(i == hitXY){
					labels[oldX][hitXY].setIcon(new ImageIcon("./Image/ildModMurNord.png"));
				}else{
					labels[oldX][i].setIcon(new ImageIcon("./Image/ildLodret.png"));
				}
				beams.add(oldX + "," + i);
			}
			deleteBeam(beams);
		
		/*
		 * Shooting in the downwards direction
		 */
		}else if(direction.equals("d")){
			for(int i = (oldY+1); i <= hitXY; i++){
				if(i == (oldY+1)){
					labels[oldX][oldY+1].setIcon(new ImageIcon("./Image/ildNed.png"));
				}else if(i == hitXY){
					labels[oldX][hitXY].setIcon(new ImageIcon("./Image/ildModMurSyd.png"));
				}else{
					labels[oldX][i].setIcon(new ImageIcon("./Image/ildLodret.png"));
				}
				beams.add(oldX + "," + i);
			}
			deleteBeam(beams);
		}
	}
	
	/*
	 * Removes the pistol beam
	 */
	public void deleteBeam(final ArrayList<String> beams){
		final Timer timer = new Timer();
		timers.add(timer);
		TimerTask timerTask = new TimerTask() {
			@Override
			public void run() {
				for(String s: beams){
					String[] xy = s.split(","); 
					labels[Integer.parseInt(xy[0])][Integer.parseInt(xy[1])].setIcon(new ImageIcon("./Image/gulv2.png"));
					timers.remove(timer);
				}
			}
		};
		timer.schedule(timerTask, 60);
		
		
	}
	
	public void meleeHit(String playerName, int score){
		//is we wanted to draw animation for melee hit
	}
	
	public void placeMine(int x, int y){
		labels[x][y].setIcon(new ImageIcon("./Image/Mine.png"));
	}
	
	public void putFloor(int x, int y){
		labels[x][y].setIcon(new ImageIcon("./Image/gulv2.png"));
	}
	
	
	/*
	 * Draws the playing board
	 */
	public void draw() {
		for (int j = 0; j < 20; j++) {
			for (int i = 0; i < 20; i++) {
				if (level[i][j].equalsIgnoreCase("w")) {
					JLabel l = new JLabel(new ImageIcon("./Image/mur1.png"));
					l.setSize(50, 50);
					this.add(l);
					labels[i][j] = l;
				} else if (level[i][j].equalsIgnoreCase("e")) {
					JLabel l = new JLabel(new ImageIcon("./Image/gulv2.png"));
					l.setSize(50, 50);
					this.add(l);
					labels[i][j] = l;
				}

			}

		}
	
	}
	
	/*
	 * Resets the game
	 */
	public void reset() {
		for (int j = 0; j < 20; j++) {
			for (int i = 0; i < 20; i++) {
				labels[i][j] = null;
			}
		}
	}
}

class WindowEventHandler extends WindowAdapter {

  public void windowClosing(WindowEvent evt) {
	GameClient.disconnect();
    System.exit(0);
  }

}

