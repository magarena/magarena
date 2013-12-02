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
import magic.ui.widget.MenuButton;
import magic.ui.widget.MenuPanel;
import magic.ui.widget.TexturedPanel;

@SuppressWarnings("serial")
public class CardExplorerScreenOptions extends TexturedPanel implements IMenuOverlay {

    private static Color BACKGROUND_COLOR = new Color(0, 0, 0, 150);
    private static Color MENUPANEL_COLOR = new Color(0, 0, 0, 230);

    private final MagicFrame frame;

    public CardExplorerScreenOptions(final MagicFrame frame0) {

        this.frame = frame0;

        setBackground(BACKGROUND_COLOR);
        setLayout(new MigLayout("insets 0, gap 10, flowx, center, center"));
        add(getGeneralMenu());

        addMouseListener(new MouseAdapter() {});
        addMouseMotionListener(new MouseMotionAdapter() {});

        getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "closeMenu");
        getActionMap().put("closeMenu", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hideOverlay();
            }
        });

        frame0.setGlassPane(this);
        setVisible(true);

    }

    private MenuPanel getGeneralMenu() {
        final MenuPanel menuPanel = new GeneralMenuPanel(frame, this);
        menuPanel.addBlankItem();
        menuPanel.addMenuItem(new MenuButton("Close Menu", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hideOverlay();
            }
        }));
        menuPanel.refreshLayout();
        menuPanel.setBackground(MENUPANEL_COLOR);
        return menuPanel;
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
