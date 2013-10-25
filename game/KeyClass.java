package game;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import client.GameClient;

public class KeyClass implements KeyListener {
	private GameClient g;
	
	public KeyClass(GameClient g) {
		this.g = g;
	}
		public void keyPressed(KeyEvent ke) {
			if (ke.getKeyCode() == ke.VK_UP) {
				g.movePlayer("u");
				System.out.println("move up");
			}
			if (ke.getKeyCode() == ke.VK_DOWN) {
				g.movePlayer("d");
				System.out.println("move down");
			}
			if (ke.getKeyCode() == ke.VK_LEFT) {
				g.movePlayer("l");
				System.out.println("move left");
			}
			if (ke.getKeyCode() == ke.VK_RIGHT) {
				g.movePlayer("r");
				System.out.println("move right");
			}
	}

		public void keyReleased(KeyEvent ke) {
			
		}

		public void keyTyped(KeyEvent arg0) {

		}
}