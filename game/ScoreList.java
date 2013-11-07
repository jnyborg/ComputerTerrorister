package game;

import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class ScoreList extends JFrame {

	//Key: PlayerName
	private HashMap<String, JLabel> labels = new HashMap<String, JLabel>();
	private JLabel time, lastWinner, scoreHeader;

	
	public ScoreList() {
		super("Computer Terrorister v. 1.6");
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setLocation(600,100);
		this.setSize(100, 500);
		this.setResizable(true);
		this.setLayout(new GridLayout(20, 20, 0, 0));
		this.setVisible(true);
		this.setAlwaysOnTop(true);
		time = new JLabel();		
		time.setSize(50,200);		
		this.add(time);
		lastWinner = new JLabel();		
		lastWinner.setSize(50,200);		
		this.add(lastWinner);
		scoreHeader = new JLabel("Score:");		
		scoreHeader.setSize(50,200);		
		this.add(scoreHeader);
		
		
		
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
	
	public void removePlayer(String player){
		this.remove(labels.get(player));
		labels.remove(labels.get(player));
		repaint();
	}

	public void setTime(String time) {
		this.time.setText("Time: " + time);
	}

	public void setLastWinner(String lastWinner) {
		this.lastWinner.setText("The last winner is " + lastWinner);
	}
	
	


	
}	
	
