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
				
			}
			if (ke.getKeyCode() == ke.VK_DOWN) {
				g.movePlayer("d");
				
			}
			if (ke.getKeyCode() == ke.VK_LEFT) {
				g.movePlayer("l");
				
			}
			if (ke.getKeyCode() == ke.VK_RIGHT) {
				g.movePlayer("r");
				
			}
			if (ke.getKeyCode() == ke.VK_SPACE) {
				
			}
	}

		public void keyReleased(KeyEvent ke) {
			
		}

		public void keyTyped(KeyEvent arg0) {

		}
}