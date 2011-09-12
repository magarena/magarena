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
	private final String hideButtonText;
	private final String showButtonText;
	
	public ButtonControlledPopup(JButton toggleButton, String hideButtonText, String showButtonText) {
		this.invokePopupButton = toggleButton;
		this.hideButtonText = hideButtonText;
		this.showButtonText = showButtonText;
		
		invokePopupButton.addActionListener(this);
		addFocusListener(this);
	}
	
	public void actionPerformed(final ActionEvent event) {
		// set popup window visibility
		if (!isVisible()) {
			// set location relative to button
			Point location = invokePopupButton.getLocation();
			SwingUtilities.convertPointToScreen(location, invokePopupButton.getParent());
			location.translate(0, invokePopupButton.getHeight()
					+ (invokePopupButton.getBorder() == null ? 0
						: invokePopupButton.getBorder().getBorderInsets(invokePopupButton).bottom));
			setLocation(location);

			// show the popup if not visible
			invokePopupButton.setText(hideButtonText);
			setVisible(true);
			requestFocus();
		} else {
			// hide it otherwise
			invokePopupButton.setText(showButtonText);
			setVisible(false);
		}
	}
	
	@Override
	public void focusLost(FocusEvent e) {
		setVisible(false);
	}

	@Override
	public void focusGained(FocusEvent e) {
	}

}