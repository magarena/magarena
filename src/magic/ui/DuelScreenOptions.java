package magic.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;

import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import net.miginfocom.swing.MigLayout;
import magic.data.GeneralConfig;
import magic.ui.widget.MenuPanel;
import magic.ui.widget.TexturedPanel;

@SuppressWarnings("serial")
public class DuelScreenOptions extends TexturedPanel implements IMenuOverlay {

    private static Color BACKGROUND_COLOR = new Color(0, 0, 0, 150);
    private static Color MENUPANEL_COLOR = new Color(0, 0, 0, 230);

    private final MagicFrame frame;

    public DuelScreenOptions(final MagicFrame frame0) {

        this.frame = frame0;

        setBackground(BACKGROUND_COLOR);
        setLayout(new MigLayout("insets 0, gap 10, flowx, center, center"));
        add(getScreenMenu());
        add(getGeneralMenu());

        addMouseListener(new MouseAdapter() {});
        addMouseMotionListener(new MouseMotionAdapter() {});

        getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "closeMenu");
        getActionMap().put("closeMenu", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });

        frame0.setGlassPane(this);
        setVisible(true);

    }

    private MenuPanel getGeneralMenu() {
        final MenuPanel menuPanel = new GeneralMenuPanel(frame, this);
        menuPanel.setBackground(MENUPANEL_COLOR);
        return menuPanel;
    }

    private MenuPanel getScreenMenu() {

        final MenuPanel menu = new MenuPanel("Game Options");

        menu.addMenuItem("Concede game", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.concedeGame();
                setVisible(false);
            }
        });
        menu.addMenuItem("Restart game", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.resetGame();
                setVisible(false);
            }
        });
        final boolean isTextMode = GeneralConfig.getInstance().getTextView();
        menu.addMenuItem(isTextMode ? "Image mode" : "Text mode", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setTextImageMode(!isTextMode);
                setVisible(false);
            }
        });
        menu.addBlankItem();
        menu.addMenuItem("Close menu", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hideOverlay();
            }
        });

        menu.refreshLayout();
        menu.setBackground(MENUPANEL_COLOR);
        return menu;
    }

    /* (non-Javadoc)
     * @see magic.ui.IMenuOverlay#hideOverlay()
     */
    @Override
    public void hideOverlay() {
        setVisible(false);
    }

    /* (non-Javadoc)
     * @see magic.ui.IMenuOverlay#hideAllMenuPanels()
     */
    @Override
    public void hideAllMenuPanels() {
        for (Component component : getComponents()) {
            if (component instanceof MenuPanel) {
                ((MenuPanel) component).setVisible(false);
            }
        }
    }

}
