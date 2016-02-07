package magic.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import magic.ui.MagicImages;
import magic.ui.URLUtils;
import magic.ui.MagicFrame;
import magic.ui.ScreenController;
import magic.translate.UiString;
import magic.ui.dialog.button.SaveButton;
import magic.ui.widget.LinkLabel;
import magic.utility.MagicSystem;

@SuppressWarnings("serial")
public class AboutDialog extends MagicDialog implements ActionListener {

    private static final Font FONT_BOLD48 = new Font("dialog", Font.BOLD, 48);
    private static final Font FONT_BOLD12 = new Font("dialog", Font.BOLD, 12);
    private static final Font FONT_PLAIN12 = new Font("dialog", Font.PLAIN, 12);
    private static final Font FONT_SMALL = new Font("dialog", Font.PLAIN, 9);

    private static final String GNU_TEXT = "<html>This program is free software: you can " +
            "redistribute it and/or modify it under the terms<br />of the GNU General " +
            "Public License as published by the Free Software Foundation.</html>";

    private final JButton okButton;

    public AboutDialog(final MagicFrame frame) {

        super(ScreenController.getMainFrame(), UiString.get("About..."), new Dimension(600, 320));

        final JPanel aboutPanel = new JPanel();
        aboutPanel.setLayout(null);

        final ImageIcon logoIcon = new ImageIcon(MagicImages.LOGO);
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

        final JLabel versionLabel = new JLabel("Version: " + MagicSystem.VERSION);
        versionLabel.setBounds(250, 105, 320, 25);
        versionLabel.setFont(FONT_PLAIN12);
        aboutPanel.add(versionLabel);

        final JLabel forumTextLabel = new LinkLabel("Magarena Project Website", URLUtils.URL_HOMEPAGE);
        forumTextLabel.setBounds(250, 126, 300, 25);
        forumTextLabel.setFont(FONT_PLAIN12);
        aboutPanel.add(forumTextLabel);

        final JLabel repoTextLabel = new LinkLabel("Discussion Forum", URLUtils.URL_FORUM);
        repoTextLabel.setBounds(250, 145, 300, 25);
        repoTextLabel.setFont(FONT_PLAIN12);
        aboutPanel.add(repoTextLabel);

        final JLabel memStatsLabel = new JLabel(MagicSystem.getHeapUtilizationStats().replace("\n", ", "));
        memStatsLabel.setBounds(210, 160, 367, 50);
        memStatsLabel.setFont(FONT_SMALL);
        memStatsLabel.setOpaque(false);
        aboutPanel.add(memStatsLabel);

        final JLabel gnuLabel = new JLabel(GNU_TEXT);
        gnuLabel.setBounds(210, 190, 367, 50);
        gnuLabel.setFont(FONT_SMALL);
        gnuLabel.setOpaque(false);
        aboutPanel.add(gnuLabel);

        okButton = new SaveButton("OK");
        okButton.setFocusable(false);
        okButton.addActionListener(this);

        final JPanel buttonPanel = new JPanel();
        buttonPanel.setPreferredSize(new Dimension(0, 45));
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        buttonPanel.add(okButton);

        final JPanel panel = getDialogContentPanel();
        panel.setLayout(new BorderLayout());
        panel.add(aboutPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    @Override
    public void actionPerformed(final ActionEvent event) {
        final Object source = event.getSource();
        if (source == okButton) {
            dispose();
        }
    }

    @Override
    protected AbstractAction getCancelAction() {
        return new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                dispose();
            }
        };
    }

}
