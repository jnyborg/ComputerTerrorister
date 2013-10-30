package game;

import java.awt.GridLayout;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class ScoreList extends JFrame {

	//Key: PlayerName
	private HashMap<String, JLabel> labels = new HashMap<String, JLabel>();

	
	public ScoreList() {
		super("TKgame v. 1.0");
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setLocation(600,100);
		this.setSize(100, 500);
		this.setResizable(true);
		this.setLayout(new GridLayout(20, 20, 0, 0));
		this.setVisible(true);
		this.setAlwaysOnTop(true);
	}
//	public void drawPlayer() {
//		for (int j = 0; j < players.size(); j++) {
//				JLabel l = new JLabel(playerName + ": " + score);
//				l.setSize(50,200);
//				this.add(l);
//				labels.add(l);
//		}	
//	}	
		
//	public void updateScoreOnScreenAll() {
//		if (players.size() > labels.size()) {
//			//new players
//			for (Map.Entry<String, String> entry : players.entrySet()) {			
//				JLabel l = new JLabel(entry.getKey() + ": " + entry.getValue());
//				l.setSize(50,200);
//				this.add(l);
//				labels.add(l);
//			}		
//		}
//		if (players.size() < labels.size()) {
//			//players left game
//			for (int j = labels.size(); j > players.size(); j--) {
//				System.out.println(j);
//				JLabel lbl = labels.remove(j-1);
//				this.remove(lbl);
//				this.repaint();
//			}	
//		}
//		for (int j = 0; j < players.size(); j++) {
//			labels.get((j)).setText(players.get(j).toString());
//			
//		}
//	
//	}
	
	public void updateScore(String player, String score) {
		JLabel l = labels.get(player);
		l.setText(player + ": " + score);
		//TODO: Is put() necessary or not. Make sure it is raintæt.
		labels.put(player, l);
	}
	
	public void addPlayer(String player, String score) {
		JLabel l = new JLabel();		
		l.setSize(50,200);
		
		this.add(l);
		l.setText(player + ": " + score);
		labels.put(player, l);
		
		
	}


	
}	
	
