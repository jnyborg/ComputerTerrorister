package game;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import client.GameClient;

public class KeyClass implements KeyListener {
	private GameClient g;
	
		public void keyPressed(KeyEvent ke) {
			if (ke.getKeyCode() == ke.VK_UP) {
				GameClient.movePlayer("u");
				System.out.println("move up");
			}

			if (ke.getKeyCode() == ke.VK_DOWN) {
				GameClient.movePlayer("d");
			}
			if (ke.getKeyCode() == ke.VK_LEFT) {
				GameClient.movePlayer("l");
			}
			if (ke.getKeyCode() == ke.VK_RIGHT) {
				GameClient.movePlayer("r");
			}
	}

		public void keyReleased(KeyEvent ke) {
			
		}

		public void keyTyped(KeyEvent arg0) {

		}
}