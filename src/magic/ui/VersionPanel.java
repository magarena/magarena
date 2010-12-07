package magic.ui;

import java.awt.BorderLayout;

import javax.swing.JLabel;

import magic.ui.widget.BackgroundLabel;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.TexturedPanel;

public class VersionPanel extends TexturedPanel {

	private static final long serialVersionUID = 1L;
	
	private static final String VERSION = "Magarena 1.0";
	private static final String AUTHOR = "by ubeefx";
	private static final String WEB = "http://magarena.dyndns.org";
	private static final String SPACING = "   ";
	private static final String VERSION_TEXT = VERSION + SPACING + AUTHOR + SPACING + WEB;

	public VersionPanel() {

		setLayout(new BorderLayout());
		add(new BackgroundLabel(),BorderLayout.CENTER);
		final JLabel versionLabel=new JLabel(VERSION_TEXT);
		versionLabel.setFont(FontsAndBorders.FONT2);
		versionLabel.setBorder(FontsAndBorders.EMPTY_BORDER);
		add(versionLabel,BorderLayout.SOUTH);
	}
}