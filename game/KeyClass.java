package game;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class KeyClass implements KeyListener {
	private gameplayer g;
	
	public KeyClass(gameplayer g){
		this.g = g;
	}

		public void keyPressed(KeyEvent ke) {
			if (ke.getKeyCode() == ke.VK_UP) {
				g.PlayerMoved("up");
			}

			if (ke.getKeyCode() == ke.VK_DOWN) {
				g.PlayerMoved("down");
			}
			if (ke.getKeyCode() == ke.VK_LEFT) {
				g.PlayerMoved("left");
			}
			if (ke.getKeyCode() == ke.VK_RIGHT) {
				g.PlayerMoved("right");
			}
	}

		public void keyReleased(KeyEvent ke) {
			
		}

		public void keyTyped(KeyEvent arg0) {

		}
}