package magic.ui;

import magic.ui.helpers.UrlHelper;
import magic.translate.MText;
import magic.ui.utility.MagicStyle;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import magic.ui.screen.interfaces.IThemeStyle;
import magic.ui.screen.widget.PlainMenuButton;
import magic.ui.screen.widget.MenuPanel;
import magic.ui.theme.Theme;
import magic.ui.widget.TexturedPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public abstract class ScreenOptionsOverlay extends TexturedPanel implements IThemeStyle {

    // translatable strings
    private static final String _S1 = "General Options";
    private static final String _S2 = "ReadMe";
    private static final String _S3 = "Online help";
    private static final String _S4 = "Keywords glossary";
    private static final String _S5 = "Preferences";
    private static final String _S6 = "Quit to main menu";
    private static final String _S7 = "Quit to desktop";
    private static final String _S8 = "Close Menu";

    private final MenuPanel screenMenu;
    private MenuPanel menu = null;

    protected abstract MenuPanel getScreenMenu();
    protected abstract boolean showPreferencesOption();

    public ScreenOptionsOverlay() {

        refreshStyle();
        setBackground(FontsAndBorders.IMENUOVERLAY_BACKGROUND_COLOR);
        setLayout(new MigLayout("insets 0, gap 10, flowx, center, center"));

        screenMenu = getScreenMenu();

        addScreenSpecificMenu();
        addGeneralOptionsMenu();

        getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "closeMenu");
        getActionMap().put("closeMenu", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                hideOverlay();
            }
        });

        addMouseListener(new MouseAdapter() {});
        addMouseMotionListener(new MouseMotionAdapter() {});
        addKeyListener(new KeyAdapter() {});

        ScreenController.getFrame().setGlassPane(this);
        setVisible(true);

    }

    private void addGeneralOptionsMenu() {

        menu = new MenuPanel(MText.get(_S1));

        // Help stuff.
        menu.addMenuItem(MText.get(_S2), new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                ScreenController.showReadMeScreen();
                hideOverlay();
            }
        });
        menu.addMenuItem(MText.get(_S3), new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                UrlHelper.openURL(UrlHelper.URL_USERGUIDE);
                hideOverlay();
            }
        });
        menu.addMenuItem(MText.get(_S4), new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                ScreenController.showKeywordsScreen();
                hideOverlay();
            }
        });
        menu.addBlankItem();

        if (showPreferencesOption()) {
            menu.addMenuItem(MText.get(_S5), new AbstractAction() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    hideAllMenuPanels();
                    ScreenController.showPreferencesDialog();
                    hideOverlay();
                }
            });
            menu.addBlankItem();
        }

        menu.addMenuItem(MText.get(_S6), new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                ScreenController.showMainMenuScreen();
                hideOverlay();
            }
        });
        menu.addMenuItem(MText.get(_S7), new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                ScreenController.getFrame().quitToDesktop(false);
            }
        });

        if (showGeneralCloseMenuOption()) {
            menu.addBlankItem();
            menu.addMenuItem(new PlainMenuButton(MText.get(_S8), new AbstractAction() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    hideOverlay();
                }
            }));
        }

        refreshStyle();
        menu.refreshLayout();
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

    @Override
    public final void refreshStyle() {
        if (menu != null) {
            final Color refBG = MagicStyle.getTheme().getColor(Theme.COLOR_TITLE_BACKGROUND);
            final Color BG = MagicStyle.getTranslucentColor(refBG, 230);
            menu.setBackground(BG);
        }
    }


}
