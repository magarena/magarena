package magic.ui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;

import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import magic.data.URLUtils;
import magic.ui.screen.widget.MenuButton;
import magic.ui.screen.widget.MenuPanel;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.TexturedPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public abstract class ScreenOptionsOverlay extends TexturedPanel {

    private static final String DOCUMENTATION_URL = "http://code.google.com/p/magarena/wiki/AboutMagarena?tm=6";

    private final MenuPanel screenMenu;

    public ScreenOptionsOverlay(final MagicFrame frame) {

        setBackground(FontsAndBorders.IMENUOVERLAY_BACKGROUND_COLOR);
        setLayout(new MigLayout("insets 0, gap 10, flowx, center, center"));

        screenMenu = getScreenMenu();

        addScreenSpecificMenu();
        addGeneralOptionsMenu(frame);

        getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "closeMenu");
        getActionMap().put("closeMenu", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                setVisible(false);
            }
        });

        addMouseListener(new MouseAdapter() {});
        addMouseMotionListener(new MouseMotionAdapter() {});
        addKeyListener(new KeyAdapter() {});

        frame.setGlassPane(this);
        setVisible(true);

    }

    private void addGeneralOptionsMenu(final MagicFrame frame) {

        final MenuPanel menu = new MenuPanel("General Options");

        // Help stuff.
        menu.addMenuItem("ReadMe", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                frame.showReadMeScreen();
                hideOverlay();
            }
        });
        menu.addMenuItem("Online help", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                URLUtils.openURL(DOCUMENTATION_URL);
                hideOverlay();
            }
        });
        menu.addMenuItem("Keywords glossary", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                frame.showKeywordsScreen();
                hideOverlay();
            }
        });
        menu.addBlankItem();

        // System stuff.
        menu.addMenuItem("Preferences", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                hideAllMenuPanels();
                frame.openPreferencesDialog();
                hideOverlay();
            }
        });
        menu.addBlankItem();

        menu.addMenuItem("Quit to main menu", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                frame.showMainMenuScreen();
                hideOverlay();
            }
        });
        menu.addMenuItem("Quit to desktop", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                frame.quitToDesktop(false);
            }
        });

        if (showGeneralCloseMenuOption()) {
            menu.addBlankItem();
            menu.addMenuItem(new MenuButton("Close Menu", new AbstractAction() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    hideOverlay();
                }
            }));
        }

        menu.refreshLayout();
        menu.setBackground(FontsAndBorders.IMENUOVERLAY_MENUPANEL_COLOR);
        add(menu);
    }

    private void addScreenSpecificMenu() {
        if (screenMenu != null) {
            add(screenMenu);
        }
    }

    private boolean showGeneralCloseMenuOption() {
        return screenMenu == null;
    }

    protected abstract MenuPanel getScreenMenu();

    public void hideOverlay() {
        setVisible(false);
    }

    public void hideAllMenuPanels() {
        for (final Component component : getComponents()) {
            if (component instanceof MenuPanel) {
                ((MenuPanel) component).setVisible(false);
            }
        }
    }

}
