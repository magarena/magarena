package magic.ui.screen.cardflow;

import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JLayeredPane;

@SuppressWarnings("serial")
class CardFlowLayeredPane extends JLayeredPane {

    private final CardFlowPanel cardFlowPanel;
    private final FlashTextOverlay flashOverlay;

    CardFlowLayeredPane(final CardFlowPanel cardFlowPanel, final FlashTextOverlay flashOverlay) {

        this.cardFlowPanel = cardFlowPanel;
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
        add(cardFlowPanel, Integer.valueOf(0));
        add(flashOverlay, Integer.valueOf(1));
        revalidate();
    }

    private void resizeComponents() {
        Dimension sz = getSize();
        cardFlowPanel.setSize(sz);
        Dimension pref = flashOverlay.getPreferredSize();
        flashOverlay.setBounds(
            (sz.width - pref.width) / 2,
            (sz.height - pref.height) / 2,
            pref.width,
            pref.height
        );
    }

}
