package magic.ui.screen.deck.hand;

import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JLayeredPane;
import magic.ui.screen.cardflow.*;
import magic.ui.widget.cards.canvas.CardsCanvas;

@SuppressWarnings("serial")
class SampleHandLayeredPane extends JLayeredPane {

    private final CardsCanvas cardsCanvas;
    private final FlashTextOverlay flashOverlay;

    SampleHandLayeredPane(final CardsCanvas cardsCanvas, final FlashTextOverlay flashOverlay) {

        this.cardsCanvas = cardsCanvas;
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
        add(cardsCanvas, 0);
        add(flashOverlay, 1);
        revalidate();
    }

    private void resizeComponents() {
        Dimension sz = getSize();
        cardsCanvas.setSize(sz);
        Dimension pref = flashOverlay.getPreferredSize();
        flashOverlay.setBounds(
            (sz.width - pref.width) / 2,
            (sz.height - pref.height) / 2,
            pref.width,
            pref.height
        );
    }

}
