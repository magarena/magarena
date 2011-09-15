package magic.ui.widget;

import javax.swing.JButton;
import javax.swing.SwingUtilities;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowEvent;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.border.Border;

public class ButtonControlledPopup extends TexturedPanel implements ActionListener, WindowFocusListener {

	private static final long serialVersionUID = 54232L;
	
	public static final int STARTING_WIDTH = 200;
	public static final int STARTING_HEIGHT = 200;
	
	private final JDialog dialog;
	private final JButton invokePopupButton;
	private final String hidePopupButtonText;
	private final String showPopupButtonText;
	private final Timer timer;
	
	private boolean popupJustToggled;
	
	public ButtonControlledPopup(JFrame frame, JButton toggleButton, String hidePopupButtonText, String showPopupButtonText) {
		this.invokePopupButton = toggleButton;
		this.hidePopupButtonText = hidePopupButtonText;
		this.showPopupButtonText = showPopupButtonText;
		this.dialog = new JDialog(frame);
		this.timer = new Timer();
		
		final Border border = BorderFactory.createRaisedBevelBorder();
		setBorder(border);
		
		dialog.setUndecorated(true);
		dialog.setSize(STARTING_WIDTH, STARTING_HEIGHT);
		
		invokePopupButton.addActionListener(this);
		dialog.add(this);
	}
	
	public void setPopupSize(int width, int height) {
		// jdialog will not resize by itself
		dialog.setSize(width, height);
	}
	
	public void showPopup() {
		// set location relative to button
		Point location = invokePopupButton.getLocation();
		SwingUtilities.convertPointToScreen(location, invokePopupButton.getParent());
		location.translate(0, invokePopupButton.getHeight()
				+ (invokePopupButton.getBorder() == null ? 0
					: invokePopupButton.getBorder().getBorderInsets(invokePopupButton).bottom));
		dialog.setLocation(location);

		// showPopup the popup if not visible
		invokePopupButton.setText(hidePopupButtonText);
		dialog.setVisible(true);
		dialog.addWindowFocusListener(this);
		dialog.requestFocus();
	}
	
	public void hidePopup() {
		invokePopupButton.setText(showPopupButtonText);
		dialog.setVisible(false);
	}
	
	class ResponsePreventer extends TimerTask {
		public void run() {
			popupJustToggled = false;
		}
	}
	
	@Override
	public void actionPerformed(final ActionEvent event) {
		if(popupJustToggled) {
			// focus event just hid popup -> this event is probably from clicking on the hide button -> don't do anything
			return;
		}
		
		// set popup visibility
		if (!dialog.isVisible()) {
			showPopup();
		} else {
			// hide - taken care of by focusLost
		}
	}
	
	@Override
	public void windowLostFocus(WindowEvent e) {
		System.out.println("focuslost");
		popupJustToggled = true;
		timer.schedule(new ResponsePreventer(), 300); // don't allow clicks on hide button to reshow popup immediately by disabling response for < 1 s
		
		if (dialog.isVisible()) {
			hidePopup();
		}
	}

	@Override
	public void windowGainedFocus(WindowEvent e) {
	}
}