package magic.ui;

import magic.data.IconImages;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class AboutDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	private static final Font FONT_BOLD48 = new Font("dialog", Font.BOLD, 48);
	private static final Font FONT_BOLD12 = new Font("dialog", Font.BOLD, 12);
	private static final Font FONT_PLAIN12 = new Font("dialog", Font.PLAIN, 12);
	private static final Font FONT_SMALL = new Font("dialog", Font.PLAIN, 9);
	
	private static final String FORUM_URL = "http://www.slightlymagic.net/forum/"
			+ "viewforum.php?f=82&sid=08ef9e6ebbb231a0c7ef65b3f12a5d77";
	private static final String REPO_URL = "http://code.google.com/p/magarena/";
	private static final String GNU_TEXT = "This program is free software: you "
			+ "can redistribute it and/or modify it under the terms of the GNU "
			+ "General Public License as published by the Free Software Foundation.";
	private static final String forumDisplayLink = "www.slightlymagic.net";
	private static final String repoDisplayLink = "code.google.com/p/magarena";

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

		final JLabel forumLabel = new JLabel(forumDisplayLink);
		forumLabel.setBounds(335, 130, 320, 25);
		forumLabel.setFont(FONT_PLAIN12);
		forumLabel.setForeground(Color.BLUE);
		aboutPanel.add(forumLabel);

		forumLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(final MouseEvent e) {
				openURL(FORUM_URL);
			}

			@Override
			public void mouseEntered(final MouseEvent e) {
				setCursor(new Cursor(Cursor.HAND_CURSOR));
				forumLabel.setText("<html><u>" + forumDisplayLink + "</u></html>");
			}

			@Override
			public void mouseExited(final MouseEvent e) {
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				forumLabel.setText(forumDisplayLink);
			}
		});
		
		final JLabel repoTextLabel = new JLabel("Our code repository:");
		repoTextLabel.setBounds(250, 145, 120, 25);
		repoTextLabel.setFont(FONT_PLAIN12);
		aboutPanel.add(repoTextLabel);

		final JLabel repoLabel = new JLabel(repoDisplayLink);
		repoLabel.setBounds(365, 145, 320, 25);
		repoLabel.setFont(FONT_PLAIN12);
		repoLabel.setForeground(Color.BLUE);
		aboutPanel.add(repoLabel);

		repoLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(final MouseEvent e) {
				openURL(REPO_URL);
			}

			@Override
			public void mouseEntered(final MouseEvent e) {
				setCursor(new Cursor(Cursor.HAND_CURSOR));
				repoLabel.setText("<html><u>" + repoDisplayLink + "</u></html>");
			}

			@Override
			public void mouseExited(final MouseEvent e) {
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				repoLabel.setText(repoDisplayLink);
			}
		});

		final JTextArea gnuTextArea = new JTextArea(GNU_TEXT);
		gnuTextArea.setBounds(210, 220, 367, 50);
		gnuTextArea.setFont(FONT_SMALL);
		gnuTextArea.setEditable(false);
		gnuTextArea.setOpaque(false);
		gnuTextArea.setLineWrap(true);
		aboutPanel.add(gnuTextArea);

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

	private static void openURL(final String url) {
		try {
			Desktop desktop = null;
			if (Desktop.isDesktopSupported()) {
				desktop = Desktop.getDesktop();
			}

			if (desktop != null) {
				final URI uri = new URI(url);
				desktop.browse(uri);
			}
		} catch (IOException ioe) {
			System.err.println("ERROR! Unable to launch browser");
            System.err.println(ioe.getMessage());
			ioe.printStackTrace();
		} catch (URISyntaxException use) {
			System.err.println("ERROR! Invalid URI");
            System.err.println(use.getMessage());
			use.printStackTrace();
		}
	}
		
	@Override
	public void actionPerformed(final ActionEvent event) {
		final Object source = event.getSource();
		if (source == okButton) {
			dispose();
		}
	}
}
