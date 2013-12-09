package magic.ui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;

import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import net.miginfocom.swing.MigLayout;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.MenuButton;
import magic.ui.widget.MenuPanel;
import magic.ui.widget.TexturedPanel;

@SuppressWarnings("serial")
public class CardExplorerScreenOptions extends TexturedPanel implements IMenuOverlay {

    private final MagicFrame frame;

    public CardExplorerScreenOptions(final MagicFrame frame0) {

        this.frame = frame0;

        setBackground(FontsAndBorders.IMENUOVERLAY_BACKGROUND_COLOR);
        setLayout(new MigLayout("insets 0, gap 10, flowx, center, center"));
        add(getGeneralMenu());

        addMouseListener(new MouseAdapter() {});
        addMouseMotionListener(new MouseMotionAdapter() {});

        getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "closeMenu");
        getActionMap().put("closeMenu", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
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
            public void actionPerformed(final ActionEvent e) {
                hideOverlay();
            }
        }));
        menuPanel.refreshLayout();
        menuPanel.setBackground(FontsAndBorders.IMENUOVERLAY_MENUPANEL_COLOR);
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
        for (final Component component : getComponents()) {
            if (component instanceof MenuPanel) {
                ((MenuPanel) component).setVisible(false);
            }
        }
    }

}
