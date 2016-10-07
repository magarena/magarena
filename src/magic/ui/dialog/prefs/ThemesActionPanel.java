package magic.ui.dialog.prefs;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import magic.data.MagicIcon;
import magic.translate.UiString;
import magic.ui.MagicImages;
import magic.ui.MagicSound;
import magic.ui.helpers.UrlHelper;
import magic.ui.theme.ThemeFactory;
import magic.ui.helpers.DesktopHelper;
import magic.ui.utility.GraphicsUtils;
import magic.utility.MagicFileSystem;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class ThemesActionPanel extends JPanel {

    // translatable strings.
    private static final String _S1 = "Open theme folder";
    private static final String _S2 = "Opens the selected theme folder in file explorer.";
    private static final String _S3 = "Get more themes...";
    private static final String _S4 = "Opens the themes wiki page in your browser which has links to many more themes. To install, copy the theme zip file into the 'themes' folder and restart Magarena.";

    private final ThemesPanel themesPanel;

    ThemesActionPanel(ThemesPanel aThemesPanel, MouseListener aListener) {
        this.themesPanel = aThemesPanel;
        setLayout(new MigLayout("insets 0, gapx 0"));
        add(getThemeFolderButton(aListener), "w 32!");
        add(getMoreThemesButton(aListener), "w 32!");
    }

    private void doOpenThemeFolder() {
        String themeName = themesPanel.getSelectedThemeName();
        File themeFile = ThemeFactory.getThemeFile(themeName);
        try {
            if (themeFile == null || themeFile.isFile()) {
                DesktopHelper.openMagicDirectory(MagicFileSystem.DataPath.THEMES);
            } else {
                DesktopHelper.openDirectory(themeFile);
            }
        } catch (IOException ex) {
            System.err.println(ex);
            MagicSound.BEEP.play();
        }
    }

    private JButton getThemeFolderButton(MouseListener aListener) {
        JButton btn = new JButton(getActionIcon(MagicIcon.OPEN));
        btn.setToolTipText(String.format("<b>%s</b><br>%s", UiString.get(_S1), UiString.get(_S2)));
        btn.addMouseListener(aListener);
        btn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doOpenThemeFolder();
            }
        });
        return btn;
    }

    private JButton getMoreThemesButton(MouseListener aListener) {
        JButton btn = new JButton(getActionIcon(MagicIcon.OPTIONS));
        btn.setToolTipText(String.format("<b>%s</b><br>%s", UiString.get(_S3), UiString.get(_S4)));
        btn.addMouseListener(aListener);
        btn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UrlHelper.openURL(UrlHelper.URL_THEMES);
            }
        });
        return btn;
    }

    private ImageIcon getActionIcon(MagicIcon icon) {
        final ImageIcon blackIcon = (ImageIcon) GraphicsUtils.getRecoloredIcon(MagicImages.getIcon(icon), Color.BLACK);
        final BufferedImage scaledImage = GraphicsUtils.scale(GraphicsUtils.getConvertedIcon(blackIcon), 18, 18);
        return new ImageIcon(scaledImage);

    }
}
