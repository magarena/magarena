package magic.ui.widget;

import javax.swing.JButton;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class ButtonControlledPopup extends JPopupMenu implements ActionListener, FocusListener {

	private static final long serialVersionUID = 54232L;
	
	private final JButton invokePopupButton;
	private final String hidePopupButtonText;
	private final String showPopupButtonText;
	
	public ButtonControlledPopup(JButton toggleButton, String hidePopupButtonText, String showPopupButtonText) {
		this.invokePopupButton = toggleButton;
		this.hidePopupButtonText = hidePopupButtonText;
		this.showPopupButtonText = showPopupButtonText;
		
		invokePopupButton.addActionListener(this);
		addFocusListener(this);
	}
	
	public void showPopup() {
		// set location relative to button
		Point location = invokePopupButton.getLocation();
		SwingUtilities.convertPointToScreen(location, invokePopupButton.getParent());
		location.translate(0, invokePopupButton.getHeight()
				+ (invokePopupButton.getBorder() == null ? 0
					: invokePopupButton.getBorder().getBorderInsets(invokePopupButton).bottom));
		setLocation(location);

		// showPopup the popup if not visible
		invokePopupButton.setText(hidePopupButtonText);
		setVisible(true);
		requestFocus();
	}
	
	public void hidePopup() {
		invokePopupButton.setText(showPopupButtonText);
		setVisible(false);
	}
	
	@Override
	public void actionPerformed(final ActionEvent event) {
		// set popup window visibility
		if (!isVisible()) {
			showPopup();
		} else {
			// hidePopup it otherwise
			hidePopup();
		}
	}
	
	@Override
	public void focusLost(FocusEvent e) {
		hidePopup();
	}

	@Override
	public void focusGained(FocusEvent e) {
	}

}