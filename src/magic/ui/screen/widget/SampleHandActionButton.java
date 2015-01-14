package magic.ui.screen.widget;

import magic.data.IconImages;
import magic.model.MagicDeck;
import magic.ui.MagicFrame;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import java.awt.event.ActionEvent;
import magic.ui.ScreenController;

@SuppressWarnings("serial")
public final class SampleHandActionButton extends ActionBarButton {
    private SampleHandActionButton() {}

    public static ActionBarButton createInstance(final MagicDeck deck, final MagicFrame frame) {
        return new ActionBarButton(
                IconImages.HAND_ICON,
                "Sample Hand", "Generate sample Hands from this deck.",
                new SampleHandAction(deck, frame));
    }

    private static class SampleHandAction extends AbstractAction {

        private final MagicDeck deck;
        private final MagicFrame frame;

        public SampleHandAction(final MagicDeck deck, final MagicFrame frame) {
            this.deck = deck;
            this.frame = frame;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (deck.size() >= 7) {
                ScreenController.showSampleHandScreen(deck);
            } else {
                showInvalidActionMessage("A deck with a minimum of 7 cards is required first.");
            }
        }

        private void showInvalidActionMessage(final String message) {
            JOptionPane.showMessageDialog(
                    frame, message, "Invalid Action", JOptionPane.INFORMATION_MESSAGE);
        }

    }

}
