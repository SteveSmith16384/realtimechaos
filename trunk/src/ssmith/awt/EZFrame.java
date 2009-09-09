package ssmith.awt;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

public class EZFrame extends JFrame implements MouseInputListener, WindowListener, KeyListener, Runnable, ComponentListener {

	public static final int INSET_TOP=23, INSET_SIDE=5;

	public Thread t = new Thread(this);

	public EZFrame() {
		this(200,200,"Form 1");
	}

	public EZFrame(int width, int height, String title) {
		addWindowListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
		super.addComponentListener(this);
		//super(this);
		setBackground(new Color(0,0,0));
		this.setSize(width+this.getInsets().left+this.getInsets().right, height+this.getInsets().top+this.getInsets().bottom);
		this.setTitle(title);
	}

	public void start() {
		t.start();
	}

        public void run() {
		}

        public void sleep(long ms) throws InterruptedException {
                Thread.sleep(ms);
        }

	public void centreForm() {
	  int form_width = this.getWidth();
	  int screen_width = Toolkit.getDefaultToolkit().getScreenSize().width;
	  int form_height = this.getHeight();
	  int screen_height = Toolkit.getDefaultToolkit().getScreenSize().height;
	  this.setLocation((screen_width-form_width)/2, (screen_height-form_height)/2);
	}

	//*****************************************
	// Keyboard events
	public void keyPressed(KeyEvent e) {
	}

	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {
	}

	//*****************************************
	// Mouse events
	public void mouseClicked(MouseEvent evt) {
	}

	public void mouseEntered(MouseEvent evt) {
	}

	public void mouseExited(MouseEvent evt) {
	}

	public void mousePressed(MouseEvent evt) {
	}

	public void mouseReleased(MouseEvent evt) {
	}

        public void mouseDragged(MouseEvent evt) {
        }

        public void mouseMoved(MouseEvent evt) {
        }

	//*****************************************
	// Window events
	public void windowActivated(WindowEvent evt) {
		//System.out.println("windowActivated");
	}

	public void windowClosed(WindowEvent evt) {
		//System.out.println("windowClosed");
	}

	public void windowClosing(WindowEvent evt) {
		//System.out.println("windowClosing");
	}

	public void windowDeactivated(WindowEvent evt) {
		//System.out.println("windowDeactivated");
	}

	public void windowDeiconified(WindowEvent evt) {
		//System.out.println("windowDeiconified");
	}

	public void windowIconified(WindowEvent evt) {
		//System.out.println("windowIconified");
	}

	public void windowOpened(WindowEvent evt) {
		//System.out.println("windowOpened");
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ComponentListener#componentResized(java.awt.event.ComponentEvent)
	 */
	public void componentResized(ComponentEvent arg0) {

	}

	/* (non-Javadoc)
	 * @see java.awt.event.ComponentListener#componentMoved(java.awt.event.ComponentEvent)
	 */
	public void componentMoved(ComponentEvent arg0) {
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ComponentListener#componentShown(java.awt.event.ComponentEvent)
	 */
	public void componentShown(ComponentEvent arg0) {
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ComponentListener#componentHidden(java.awt.event.ComponentEvent)
	 */
	public void componentHidden(ComponentEvent arg0) {
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
/*	public void actionPerformed(ActionEvent arg0) {
		System.out.println("state " + arg0.toString());
		
	}*/

/*	public void windowStateChanged(WindowEvent state) {
		System.out.println("state " + state.getID());
	}*/

	/* (non-Javadoc)
	 * @see java.awt.event.AWTEventListener#eventDispatched(java.awt.AWTEvent)
	 */
/*	public void eventDispatched(AWTEvent arg0) {
		System.out.println("event " + arg0.getID());
	}*/

	//***************************
	
}