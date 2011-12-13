package magic.ui;

import magic.data.IconImages;
import magic.data.URLUtils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

class AboutDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	private static final Font FONT_BOLD48 = new Font("dialog", Font.BOLD, 48);
	private static final Font FONT_BOLD12 = new Font("dialog", Font.BOLD, 12);
	private static final Font FONT_PLAIN12 = new Font("dialog", Font.PLAIN, 12);
	private static final Font FONT_SMALL = new Font("dialog", Font.PLAIN, 9);
	
	private static final String FORUM_URL = "http://www.slightlymagic.net/forum/" +
			"viewforum.php?f=82&sid=08ef9e6ebbb231a0c7ef65b3f12a5d77";
	private static final String REPO_URL = "http://code.google.com/p/magarena/";
	private static final String GNU_TEXT = "<html>This program is free software: you can " +
			"redistribute it and/or modify it under the terms<br />of the GNU General " +
			"Public License as published by the Free Software Foundation.</html>";
	private static final String FORUM_DISPLAY_LINK = "www.slightlymagic.net";
	private static final String REPO_DISPLAY_LINK = "code.google.com/p/magarena";

	private final JButton okButton;
	
	public AboutDialog(final MagicFrame frame) {
		super(frame, true);
		this.setLayout(new BorderLayout());
		this.setTitle("About Magarena");
		this.setSize(600, 320);
		this.setLocationRelativeTo(frame);
		this.setResizable(false);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		final JPanel aboutPanel = new JPanel();
		aboutPanel.setLayout(null);

		final ImageIcon logoIcon = new ImageIcon(IconImages.WIZARD);
		final JLabel logoLabel = new JLabel(logoIcon);
		logoLabel.setSize(logoIcon.getIconWidth(), logoIcon.getIconHeight());
		aboutPanel.add(logoLabel);

		final JLabel magLabel = new JLabel("Magarena");
		magLabel.setBounds(205, 5, 390, 55);
		magLabel.setFont(FONT_BOLD48);
		aboutPanel.add(magLabel);
		
		final JLabel descriptionLabel = new JLabel("Fantasy card strategy game"
				+ " against computer opponent");
		descriptionLabel.setBounds(250, 60, 390, 25);
		descriptionLabel.setFont(FONT_BOLD12);
		aboutPanel.add(descriptionLabel);

		final JLabel versionLabel = new JLabel("Version: " + VersionPanel.getVersion());
		versionLabel.setBounds(250, 105, 320, 25);
		versionLabel.setFont(FONT_PLAIN12);
		aboutPanel.add(versionLabel);
		
		final JLabel forumTextLabel = new JLabel("Visit the forum:");
		forumTextLabel.setBounds(250, 130, 120, 25);
		forumTextLabel.setFont(FONT_PLAIN12);
		aboutPanel.add(forumTextLabel);

		final JLabel forumLabel = new JLabel(FORUM_DISPLAY_LINK);
		forumLabel.setBounds(335, 130, 320, 25);
		forumLabel.setFont(FONT_PLAIN12);
		forumLabel.setForeground(Color.BLUE);
		aboutPanel.add(forumLabel);

		forumLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(final MouseEvent e) {
				URLUtils.openURL(FORUM_URL);
			}

			@Override
			public void mouseEntered(final MouseEvent e) {
				setCursor(new Cursor(Cursor.HAND_CURSOR));
				forumLabel.setText("<html><u>" + FORUM_DISPLAY_LINK + "</u></html>");
			}

			@Override
			public void mouseExited(final MouseEvent e) {
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				forumLabel.setText(FORUM_DISPLAY_LINK);
			}
		});
		
		final JLabel repoTextLabel = new JLabel("Our code repository:");
		repoTextLabel.setBounds(250, 145, 120, 25);
		repoTextLabel.setFont(FONT_PLAIN12);
		aboutPanel.add(repoTextLabel);

		final JLabel repoLabel = new JLabel(REPO_DISPLAY_LINK);
		repoLabel.setBounds(365, 145, 320, 25);
		repoLabel.setFont(FONT_PLAIN12);
		repoLabel.setForeground(Color.BLUE);
		aboutPanel.add(repoLabel);

		repoLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(final MouseEvent e) {
				URLUtils.openURL(REPO_URL);
			}

			@Override
			public void mouseEntered(final MouseEvent e) {
				setCursor(new Cursor(Cursor.HAND_CURSOR));
				repoLabel.setText("<html><u>" + REPO_DISPLAY_LINK + "</u></html>");
			}

			@Override
			public void mouseExited(final MouseEvent e) {
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				repoLabel.setText(REPO_DISPLAY_LINK);
			}
		});

		final JLabel gnuLabel = new JLabel(GNU_TEXT);
		gnuLabel.setBounds(210, 190, 367, 50);
		gnuLabel.setFont(FONT_SMALL);
		gnuLabel.setOpaque(false);
		aboutPanel.add(gnuLabel);

		okButton = new JButton("OK");
		okButton.setFocusable(false);
		okButton.setIcon(IconImages.OK);
		okButton.addActionListener(this);

		final JPanel buttonPanel = new JPanel();
		buttonPanel.setPreferredSize(new Dimension(0, 45));
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 15, 0));
		buttonPanel.add(okButton);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(aboutPanel, BorderLayout.CENTER);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);

		setVisible(true);
	}
		
	@Override
	public void actionPerformed(final ActionEvent event) {
		final Object source = event.getSource();
		if (source == okButton) {
			dispose();
		}
	}
}
