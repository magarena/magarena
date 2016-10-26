package magic.ui.screen.widget;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import magic.data.MagicIcon;
import magic.model.MagicDeck;
import magic.ui.MagicImages;
import magic.ui.ScreenController;
import magic.translate.MText;

@SuppressWarnings("serial")
public final class SampleHandActionButton extends ActionBarButton {

    // translatable strings
    private static final String _S1 = "Sample Hand";
    private static final String _S2 = "Generate sample Hands from this deck.";
    private static final String _S3 = "A deck with a minimum of 7 cards is required first.";

    private SampleHandActionButton() {}

    public static ActionBarButton createInstance(final MagicDeck deck) {
        return new ActionBarButton(
                MagicImages.getIcon(MagicIcon.HAND_ICON),
                MText.get(_S1), MText.get(_S2),
                new SampleHandAction(deck));
    }

    private static class SampleHandAction extends AbstractAction {

        private final MagicDeck deck;

        public SampleHandAction(final MagicDeck deck) {
            this.deck = deck;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (deck.size() >= 7) {
                ScreenController.showSampleHandScreen(deck);
            } else {
                showInvalidActionMessage(MText.get(_S3));
            }
        }

        private void showInvalidActionMessage(final String message) {
            ScreenController.showWarningMessage(message);
        }

    }

}
