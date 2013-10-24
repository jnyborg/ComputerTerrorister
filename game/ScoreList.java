package game;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ScoreList extends JFrame {

	/**
	 * @param args
	 */
	ArrayList<Player> players;
//	Player me;
	private ArrayList<JLabel> labels = new ArrayList<JLabel>();

	
	public ScoreList( ArrayList<Player> players) {
		super("TKgame v. 1.0");
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setLocation(600,100);
		this.setSize(100, 500);
		this.setResizable(true);
		this.setLayout(new GridLayout(20, 20, 0, 0));
		this.setVisible(true);
		this.players = players;
		draw();
		this.setAlwaysOnTop(true);
	}
	public void draw() {
		for (int j = 0; j < players.size(); j++) {
				JLabel l = new JLabel(players.get(j).ToString());
				l.setSize(50,200);
				this.add(l);
				labels.add(l);
		}	
	}	
		
	public void updateScoreOnScreenAll() {
		if (players.size() > labels.size()) {
			//new players
			for (int j = labels.size(); j < players.size(); j++) {
				JLabel l = new JLabel(players.get(j).ToString());
				l.setSize(50,200);
				this.add(l);
				labels.add(l);
			}		
		}
		if (players.size() < labels.size()) {
			//players left game
			for (int j = labels.size(); j > players.size(); j--) {
				System.out.println(j);
				JLabel lbl = labels.remove(j-1);
				this.remove(lbl);
				this.repaint();
			}	
		}
		for (int j = 0; j < players.size(); j++) {
			labels.get((j)).setText(players.get(j).ToString());
			
		}
	
	}
	
	public void addPlayer(Player player) {
		players.add(player);
		updateScoreOnScreenAll();
	}


	
}	
	
