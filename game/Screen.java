package game;

import java.awt.GridLayout;



import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;


public class Screen  extends JFrame {
	private JLabel[][] labels = new JLabel[20][20];
	
	
	
	private String[][] level= {
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


	
	public Screen()
	{
		super("TKgame v. 1.0");
	
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setLocation(100, 100);
		this.setSize(500, 499);
		this.setResizable(true);
		this.setLayout(new GridLayout(20, 20, 0, 0));
		draw();
		this.setAlwaysOnTop(true);
		this.setVisible(true);
	}
	public void movePlayerOnScreen(int oldX, int oldY, int x, int y,String playerDirection) {
	
		labels[oldX][oldY].setIcon(new ImageIcon("./Image/Gulv2.png"));

		if (playerDirection.equals("r")) {
			labels[x][y].setIcon(
					new ImageIcon("./Image/Helthoejre.png"));
		};
		if (playerDirection.equals("l")) {
			labels[x][y].setIcon(
					new ImageIcon("./Image/Heltvenstre.png"));
		};
		if (playerDirection.equals("u")) {
			labels[x][y].setIcon(
					new ImageIcon("./Image/HeltOp.png"));
		};
		if (playerDirection.equals("d")) {
			labels[x][y].setIcon(
					new ImageIcon("./Image/HeltNed.png"));
		};
}
	/**
	 * Given a token consisting of one or more tokens x#y#direction seperated by ¤, draw players.
	 * @param tokens
	 */
	public void drawPlayers(String tokens) {
		int x,y;
		String playerDirection;
		String[] setupSplit = tokens.split("¤");
		for(String s : setupSplit){
			String[] userInfo = s.split("#");
			x = Integer.parseInt(userInfo[1]);
			y = Integer.parseInt(userInfo[2]); 
			playerDirection = userInfo[3];
			
			if (playerDirection.equals("r")) {
				labels[x][y].setIcon(
						new ImageIcon("./Image/Helthoejre.png"));
			};
			if (playerDirection.equals("l")) {
				labels[x][y].setIcon(
						new ImageIcon("./Image/Heltvenstre.png"));
			};
			if (playerDirection.equals("u")) {
				labels[x][y].setIcon(
						new ImageIcon("./Image/HeltOp.png"));
			};
			if (playerDirection.equals("d")) {
				labels[x][y].setIcon(
						new ImageIcon("./Image/HeltNed.png"));
			};
		}
}
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
	//	labels[posX][posY].setIcon(
			//	new ImageIcon("./Image/HeltOp.png"));
	}
}
