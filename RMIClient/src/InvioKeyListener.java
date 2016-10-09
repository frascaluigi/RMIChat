import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public abstract class InvioKeyListener implements KeyListener{
	
	protected abstract boolean conditition();
	
	protected abstract void doAction();
	
	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if ( e.getKeyCode() == KeyEvent.VK_ENTER )
			if ( conditition() )
				doAction();
	}
}
	
