package magic.ui.screen.duel.mulligan;

import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import magic.ui.screen.cardflow.*;

@SuppressWarnings("serial")
class MulliganLayeredPane extends JLayeredPane {

    private final JPanel contentPanel;
    private final FlashTextOverlay flashOverlay;

    MulliganLayeredPane(final JPanel contentPanel, final FlashTextOverlay flashOverlay) {

        this.contentPanel = contentPanel;
        this.flashOverlay = flashOverlay;

        updateLayout();

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent event) {
                resizeComponents();
            }
        });
    }

    private void updateLayout() {
        removeAll();
        // layers from bottom to top.
        add(contentPanel, Integer.valueOf(0));
        add(flashOverlay, Integer.valueOf(1));
        revalidate();
    }

    private void resizeComponents() {
        Dimension sz = getSize();
        contentPanel.setSize(sz);
        Dimension pref = flashOverlay.getPreferredSize();
        flashOverlay.setBounds(
            (sz.width - pref.width) / 2,
            (sz.height - pref.height) / 2,
            pref.width,
            pref.height
        );
    }

}
