package magic.ui.widget;

import javax.swing.JButton;
import javax.swing.SwingUtilities;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.border.Border;

public class ButtonControlledPopup extends TexturedPanel implements ActionListener, FocusListener {

	private static final long serialVersionUID = 54232L;
	
	public static final int STARTING_WIDTH = 200;
	public static final int STARTING_HEIGHT = 200;
	
	private final JDialog dialog;
	private final JButton invokePopupButton;
	private final String hidePopupButtonText;
	private final String showPopupButtonText;
	private boolean popupToggledByButton; // ensures clicking hide button won't hide and then immediately show due to both action event and focus event
	private boolean focusHidePending;
	
	public ButtonControlledPopup(JFrame frame, JButton toggleButton, String hidePopupButtonText, String showPopupButtonText) {
		this.invokePopupButton = toggleButton;
		this.hidePopupButtonText = hidePopupButtonText;
		this.showPopupButtonText = showPopupButtonText;
		this.dialog = new JDialog(frame);
		
		final Border border = BorderFactory.createRaisedBevelBorder();
		setBorder(border);
		
		dialog.setUndecorated(true);
		dialog.setSize(STARTING_WIDTH, STARTING_HEIGHT);
		
		invokePopupButton.addActionListener(this);
		dialog.addFocusListener(this);
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
		dialog.requestFocus();
	}
	
	public void hidePopup() {
		invokePopupButton.setText(showPopupButtonText);
		dialog.setVisible(false);
	}
	
	@Override
	public void actionPerformed(final ActionEvent event) {
		popupToggledByButton = true;
		
		// set popup visibility
		if (!dialog.isVisible() && invokePopupButton.getText().equals(showPopupButtonText)) {
			showPopup();
		} else if (!focusHidePending) {
			// hide
			hidePopup();
		}
	}
	
	@Override
	public void focusLost(FocusEvent e) {
		popupToggledByButton = false;
		focusHidePending = true;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				if (!popupToggledByButton && dialog.isVisible()) {
					focusHidePending = false;
					invokePopupButton.doClick();
				}
			}
		});
	}

	@Override
	public void focusGained(FocusEvent e) {
	}

}